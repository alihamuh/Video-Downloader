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

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pheonix.KingKongVid.downloaaderVideoPackage.PheonixApp;
import pheonix.KingKongVid.downloaaderVideoPackage.PermissionRequestCodes;
import pheonix.KingKongVid.downloaaderVideoPackage.R;
import pheonix.KingKongVid.downloaaderVideoPackage.download.DownloadManager;
import pheonix.KingKongVid.downloaaderVideoPackage.download.DownloadPermissionHandler;
import pheonix.KingKongVid.downloaaderVideoPackage.download.DownloadVideo;
import pheonix.KingKongVid.downloaaderVideoPackage.download.lists.DownloadQueues;
import pheonix.KingKongVid.downloaaderVideoPackage.utils.RenameDialog;
import pheonix.KingKongVid.downloaaderVideoPackage.utils.Utils;

/**
 * Created by loremar on 3/23/18.
 */

public abstract class VideoList {
    private Activity activity;
    private RecyclerView view;
    private List<Video> videos;

    class Video {
        String size, type, link, name, page, website;
        boolean chunked = false, checked = false, expanded = false;
        String thumbnail;
    }

    abstract void onItemDeleted();

    VideoList(Activity activity, RecyclerView view) {
        this.activity = activity;
        this.view = view;

        view.setAdapter(new VideoListAdapter());
        view.setLayoutManager(new LinearLayoutManager(activity));
        view.addItemDecoration(Utils.createDivider(activity));
        view.setHasFixedSize(true);

        videos = new ArrayList<>();
    }

    void recreateVideoList(RecyclerView view) {
        this.view = view;
        view.setAdapter(new VideoListAdapter());
        view.setLayoutManager(new LinearLayoutManager(activity));
        view.addItemDecoration(Utils.createDivider(activity));
        view.setHasFixedSize(true);
    }

    void addItem(@Nullable String size, String type, String link, String name, String page,
                 boolean chunked, String website,String thumbnail) {
        Video video = new Video();
        video.size = size;
        video.type = type;
        video.link = link;
        video.name = name;
        video.page = page;
        video.chunked = chunked;
        video.website = website;
        video.thumbnail =thumbnail;

        boolean duplicate = false;
        for (Video v : videos) {
            if (v.link.equals(video.link)) {
                duplicate = true;
                break;
            }
        }
        if (!duplicate) {
            videos.add(video);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    view.getAdapter().notifyDataSetChanged();
                }
            });
        }
    }

    int getSize() {
        return videos.size();
    }

    void deleteCheckedItems() {
        for (int i = 0; i < videos.size(); ) {
            if (videos.get(i).checked) {
                videos.remove(i);
            } else i++;
        }
        ((VideoListAdapter) view.getAdapter()).expandedItem = -1;
        view.getAdapter().notifyDataSetChanged();
    }

    class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoItem> {
        int expandedItem = -1;

        List getVideos() {
            return videos;
        }

        @NonNull
        @Override
        public VideoItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(activity);
            return (new VideoItem(inflater.inflate(R.layout.videos_found_item, parent,
                    false)));
        }

        @Override
        public void onBindViewHolder(@NonNull VideoItem holder, int position) {
            holder.bind(videos.get(position));
        }

        @Override
        public int getItemCount() {
            return videos.size();
        }

        class VideoItem extends RecyclerView.ViewHolder implements CompoundButton
                .OnCheckedChangeListener, View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {
            TextView size;
            TextView name;
            TextView ext;
            CheckBox check;
            View expand;

            boolean adjustedLayout;

            VideoItem(View itemView) {
                super(itemView);
                adjustedLayout = false;
                size = itemView.findViewById(R.id.videoFoundSize);
                name = itemView.findViewById(R.id.videoFoundName);
                ext = itemView.findViewById(R.id.videoFoundExt);
                check = itemView.findViewById(R.id.videoFoundCheck);
                expand = itemView.findViewById(R.id.videoFoundExpand);
                check.setOnCheckedChangeListener(this);
                itemView.setOnClickListener(this);
                itemView.getViewTreeObserver().addOnGlobalLayoutListener(this);
                size.getViewTreeObserver().addOnGlobalLayoutListener(this);
                ext.getViewTreeObserver().addOnGlobalLayoutListener(this);
                check.getViewTreeObserver().addOnGlobalLayoutListener(this);
            }

            void bind(Video video) {
                if (video.size != null) {
                    String sizeFormatted = Formatter.formatShortFileSize(activity,
                            Long.parseLong(video.size));
                    size.setText(sizeFormatted);
                } else size.setText(" ");
                String extStr = "." + video.type;
                ext.setText(extStr);
                check.setChecked(video.checked);
                name.setText(video.name);
                //if (video.expanded) {
                    expand.setVisibility(View.VISIBLE);
                //} else {
                    //expand.setVisibility(View.GONE);
                //}
                expand.findViewById(R.id.videoFoundRename).setOnClickListener(this);
                expand.findViewById(R.id.videoFoundDownload).setOnClickListener(this);
                expand.findViewById(R.id.videoFoundDelete).setOnClickListener(this);
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                videos.get(getAdapterPosition()).checked = isChecked;
            }

            @Override
            public void onClick(View v) {
                if (v == expand.findViewById(R.id.videoFoundRename)) {
                    new RenameDialog(activity, name.getText().toString()) {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }

                        @Override
                        public void onOK(String newName) {
                            adjustedLayout = false;
                            videos.get(getAdapterPosition()).name = newName;
                            notifyItemChanged(getAdapterPosition());
                        }
                    };
                } else if (v == expand.findViewById(R.id.videoFoundDownload)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        new DownloadPermissionHandler(activity) {
                            @Override
                            public void onPermissionGranted() {
                                startDownload();
                            }
                        }.checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                PermissionRequestCodes.DOWNLOADS);
                    } else {
                        startDownload();
                    }
                } else if (v == expand.findViewById(R.id.videoFoundDelete)) {
                    new AlertDialog.Builder(activity)
                            .setMessage("Delete this item from the list?")
                            .setPositiveButton("YES", new DialogInterface
                                    .OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    videos.remove(getAdapterPosition());
                                    expandedItem = -1;
                                    notifyDataSetChanged();
                                    onItemDeleted();
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface
                                    .OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .create()
                            .show();
                } else {
                    if (expandedItem != -1) {
                        videos.get(expandedItem).expanded = false;
                        if (expandedItem != getAdapterPosition()) {
                            expandedItem = getAdapterPosition();
                            videos.get(getAdapterPosition()).expanded = true;
                        } else {
                            expandedItem = -1;
                        }
                    } else {
                        expandedItem = getAdapterPosition();
                        videos.get(getAdapterPosition()).expanded = true;
                    }
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onGlobalLayout() {
                if (!adjustedLayout) {
                    if (itemView.getWidth() != 0 && size.getWidth() != 0 && ext.getWidth() != 0 && check
                            .getWidth() != 0) {
                        int totalMargin = (int) TypedValue.applyDimension(TypedValue
                                        .COMPLEX_UNIT_DIP, 12,
                                activity.getResources().getDisplayMetrics());
                        int nameMaxWidth = itemView.getMeasuredWidth() - size.getMeasuredWidth() - ext
                                .getMeasuredWidth() - check.getMeasuredWidth() - totalMargin;
                        name.setMaxWidth(nameMaxWidth);
                        adjustedLayout = true;
                    }
                }

            }

            void startDownload() {
                Video video = videos.get(getAdapterPosition());
                DownloadQueues queues = DownloadQueues.load(activity);
                queues.insertToTop(video.size, video.type, video.link, video.name, video
                        .page, video.chunked, video.website,video.thumbnail);
                queues.save(activity);
                DownloadVideo topVideo = queues.getTopVideo();
                Intent downloadService = PheonixApp.getInstance().getDownloadService();
                PheonixApp.getInstance().stopService(downloadService);
                DownloadManager.stopThread();
                downloadService.putExtra("link", topVideo.link);
                downloadService.putExtra("name", topVideo.name);
                downloadService.putExtra("type", topVideo.type);
                downloadService.putExtra("size", topVideo.size);
                downloadService.putExtra("page", topVideo.page);
                downloadService.putExtra("chunked", topVideo.chunked);
                downloadService.putExtra("website", topVideo.website);
                PheonixApp.getInstance().startService(downloadService);
                videos.remove(getAdapterPosition());
                expandedItem = -1;
                notifyDataSetChanged();
                onItemDeleted();
                Toast.makeText(activity, "Downloading video in the background. Check the " +
                        "Downloads panel to see progress", Toast.LENGTH_LONG).show();
            }
        }
    }

    void saveCheckedItemsForDownloading() {
        DownloadQueues queues = DownloadQueues.load(activity);
        for (Video video : videos) {
            if (video.checked) {
                queues.add(video.size, video.type, video.link, video.name, video.page, video
                        .chunked, video.website,video.thumbnail);
            }
        }

        queues.save(activity);

        Toast.makeText(activity, "Selected videos are queued for downloading. Go to Downloads " +
                "panel to start downloading videos", Toast.LENGTH_LONG).show();
    }
}
