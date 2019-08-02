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

package pheonix.KingKongVid.downloaaderVideoPackage.history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pheonix.KingKongVid.downloaaderVideoPackage.PheonixFragment;
import pheonix.KingKongVid.downloaaderVideoPackage.R;
import pheonix.KingKongVid.downloaaderVideoPackage.PheonixActivity;
import pheonix.KingKongVid.downloaaderVideoPackage.utils.Utils;

public class History extends PheonixFragment implements PheonixActivity.OnBackPressedListener {
    private View view;
    private EditText searchText;
    private RecyclerView visitedPagesView;

    private List<VisitedPage> visitedPages;
    private HistorySQLite historySQLite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);

        if (view == null) {
            getLMvdActivity().setOnBackPressedListener(this);

            view = inflater.inflate(R.layout.history, container, false);
            searchText = view.findViewById(R.id.historySearchText);
            ImageView searchButton = view.findViewById(R.id.historySearchIcon);
            visitedPagesView = view.findViewById(R.id.visitedPages);
            TextView clearHistory = view.findViewById(R.id.clearHistory);

            historySQLite = new HistorySQLite(getActivity());
            visitedPages = historySQLite.getAllVisitedPages();

            visitedPagesView.setAdapter(new VisitedPagesAdapter());
            visitedPagesView.setLayoutManager(new LinearLayoutManager(getActivity()));
            visitedPagesView.addItemDecoration(Utils.createDivider(getActivity()));

            clearHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historySQLite.clearHistory();
                    visitedPages.clear();
                    visitedPagesView.getAdapter().notifyDataSetChanged();
                }
            });

            searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    searchGo();
                    return false;
                }
            });

            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchGo();
                }
            });
        }

        return view;
    }

    private void searchGo() {
        if (getActivity().getCurrentFocus() != null) {
            Utils.hideSoftKeyboard(getActivity(), getActivity().getCurrentFocus().getWindowToken());
            visitedPages = historySQLite.getVisitedPagesByKeyword(searchText.getText()
                    .toString());
            visitedPagesView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onBackpressed() {
        getLMvdActivity().getBrowserManager().unhideCurrentWindow();
        getFragmentManager().beginTransaction().remove(this).commit();
    }

    private class VisitedPagesAdapter extends RecyclerView.Adapter<VisitedPagesAdapter.VisitedPageItem> {
        @NonNull
        @Override
        public VisitedPageItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new VisitedPageItem(inflater.inflate(R.layout.history_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VisitedPageItem holder, int position) {
            holder.bind(visitedPages.get(position));
        }

        @Override
        public int getItemCount() {
            return visitedPages.size();
        }

        class VisitedPageItem extends RecyclerView.ViewHolder {
            private TextView title;

            VisitedPageItem(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.visitedPageTitle);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getLMvdActivity().browserClicked();
                        getLMvdActivity().getBrowserManager().newWindow(visitedPages.get
                                (getAdapterPosition()).link);
                    }
                });
                itemView.findViewById(R.id.visitedPageDelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        historySQLite.deleteFromHistory(visitedPages.get(getAdapterPosition()).link);
                        visitedPages.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    }
                });
            }

            void bind(VisitedPage page) {
                title.setText(page.title);
            }
        }
    }
}
