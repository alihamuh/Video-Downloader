/*
 *     LM videodownloader is a browser app for android, made to easily
 *     download videos.
 *     Copyright (C) 2018 Loremar Marabillas
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package pheonix.KingKongVid.downloaaderVideoPackage.browsing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import pheonix.KingKongVid.downloaaderVideoPackage.PheonixFragment;
import pheonix.KingKongVid.downloaaderVideoPackage.R;

/**
 * Created by loremar on 3/23/18.
 */

public class BrowserManager extends PheonixFragment {
    private List<BrowserWindow> windows;
    private RecyclerView allWindows;
    private AdBlocker adBlocker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        windows = new ArrayList<>();

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        //allWindows = (RecyclerView) inflater.inflate(R.layout.all_windows_popup, (ViewGroup)
        //getActivity().findViewById(android.R.id.content), false);

        allWindows =(RecyclerView)getLMvdActivity().findViewById(R.id.lst_menu_items);
        allWindows.setLayoutManager(new LinearLayoutManager(getActivity()));
        allWindows.setAdapter(new AllWindowsAdapter());

        File file = new File(getActivity().getFilesDir(), "ad_filters.dat");
        try {
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                adBlocker = (AdBlocker) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
            } else {
                adBlocker = new AdBlocker();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(adBlocker);
                objectOutputStream.close();
                fileOutputStream.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void newWindow(String url) {
        Bundle data = new Bundle();
        data.putString("url", url);
        BrowserWindow window = new BrowserWindow();
        window.setArguments(data);
        getFragmentManager().beginTransaction()
                .add(R.id.main, window, null)
                .commit();
        windows.add(window);
        getLMvdActivity().setOnBackPressedListener(window);
        if (windows.size() > 1) {
            window = windows.get(windows.size() - 2);
            if (window != null && window.getView() != null) {
                window.getView().setVisibility(View.GONE);
            }
        }
        updateNumWindows();
        allWindows.getAdapter().notifyDataSetChanged();
    }

    public void closeWindow(BrowserWindow window) {
        windows.remove(window);
        getFragmentManager().beginTransaction().remove(window).commit();
        if (windows.size() > 0) {
            BrowserWindow topWindow = windows.get(windows.size() - 1);
            if (topWindow != null && topWindow.getView() != null) {
                topWindow.getView().setVisibility(View.VISIBLE);
            }
            getLMvdActivity().setOnBackPressedListener(topWindow);
        } else {
            getLMvdActivity().setOnBackPressedListener(null);
        }
        updateNumWindows();
    }

    void switchWindow(int index) {   //getAdapterpostion()
        BrowserWindow topWindow = windows.get(windows.size() - 1);
        if (topWindow.getView() != null) {
            topWindow.getView().setVisibility(View.GONE);
        }
        initializingCurrentTab(windows.get(index).getWebView());
        BrowserWindow window = windows.get(index);
        windows.remove(index);

        windows.add(window);
        if (window.getView() != null) {
            window.getView().setVisibility(View.VISIBLE);
            getLMvdActivity().setOnBackPressedListener(window);
        }
        allWindows.getAdapter().notifyDataSetChanged();

    }

    public void initializingCurrentTab(final WebView page){

        TextView webTitle =getLMvdActivity().findViewById(R.id.current_windowTitle);
        webTitle.setText(""+page.getTitle());

        webTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page.reload();
            }
        });

        ImageView favIcon =getLMvdActivity().findViewById(R.id.current_fav_icon);
        favIcon.setImageBitmap(page.getFavicon());



    }

    void updateNumWindows() {
        for (BrowserWindow window : windows) {
            window.updateNumWindows(windows.size());
        }
    }

    public RecyclerView getAllWindows() {
        return allWindows;
    }

    public void hideCurrentWindow() {
        if (windows.size() > 0) {
            BrowserWindow topWindow = windows.get(windows.size() - 1);
            if (topWindow.getView() != null) {
                topWindow.getView().setVisibility(View.GONE);
            }
        }
    }

    public void unhideCurrentWindow() {
        if (windows.size() > 0) {
            BrowserWindow topWindow = windows.get(windows.size() - 1);
            if (topWindow.getView() != null) {
                topWindow.getView().setVisibility(View.VISIBLE);
                getLMvdActivity().setOnBackPressedListener(topWindow);
            }
        } else {
            getLMvdActivity().setOnBackPressedListener(null);
        }
    }

    public void updateAdFilters() {
        adBlocker.update(getActivity());
    }

    public boolean checkUrlIfAds(String url) {
        return adBlocker.checkThroughFilters(url);
    }

    private class AllWindowsAdapter extends RecyclerView.Adapter<WindowItem> {

        @NonNull
        @Override
        public WindowItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View item = inflater.inflate(R.layout.all_windows_popup_item, parent, false);
            return new WindowItem(item);
        }

        @Override
        public void onBindViewHolder(@NonNull WindowItem holder, int position) {
            holder.bind(windows.get(position).getWebView());
        }

        @Override
        public int getItemCount() {
            return windows.size()-1;
        }
    }

    private class WindowItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView windowTitle;
        ImageView favicon;
        String Weburl;
        ImageButton tabCancel;

        WindowItem(View itemView) {
            super(itemView);
            windowTitle = itemView.findViewById(R.id.windowTitle);
            favicon = itemView.findViewById(R.id.favicon);
            favicon.setOnClickListener(this);
            windowTitle.setOnClickListener(this);

            tabCancel =itemView.findViewById(R.id.tab_cancel);



        }

        void bind(final WebView webView) {
            windowTitle.setText(webView.getTitle());
            Weburl = webView.getOriginalUrl();
            favicon.setImageBitmap(webView.getFavicon());

            tabCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("Are you sure you want to close this window?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //EditText urlBox =(EditText)getActivity().findViewById(R.id.searchEditText);
                                        //urlBox.setText("");
                                        //getLMvdActivity().getBrowserManager().closeWindow(BrowserWindow.);
                                        Log.d("Cancel",""+getAdapterPosition());
                                        cancelWindow(getAdapterPosition());

                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .create()
                                .show();
                    }

            });
        }

        @Override
        public void onClick(View v) {
            switchWindow(getAdapterPosition());
            DrawerLayout drawer = getLMvdActivity().findViewById(R.id.drawer_layout);
            EditText url = getLMvdActivity().findViewById(R.id.searchEditText);
            url.setText(Weburl);
            drawer.closeDrawer(GravityCompat.START);

        }
    }

    void cancelWindow(int index) {   //getAdapterpostion()

        //initializingCurrentTab(windows.get(index).getWebView());
        //BrowserWindow window = windows.get(index);
        windows.remove(index);

        //windows.add(window);

        allWindows.getAdapter().notifyDataSetChanged();

    }
}
