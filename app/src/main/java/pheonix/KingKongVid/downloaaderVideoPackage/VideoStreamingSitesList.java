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

package pheonix.KingKongVid.downloaaderVideoPackage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VideoStreamingSitesList extends BaseAdapter {
    private List<Site> sites;
    private PheonixActivity activity;

    class Site {
        int drawable;
        String title;
        String url;
        int color;

        Site(int drawable,int color, String title, String url) {
            this.drawable = drawable;
            this.title = title;
            this.url = url;
            this.color=color;
        }
    }


    VideoStreamingSitesList(PheonixActivity activity) {
        this.activity = activity;
        sites = new ArrayList<>();
        //sites.add(new Site(R.drawable.favicon_youtube, "youtube", "https://m.youtube.com"));
        sites.add(new Site(R.drawable.favicon_facebook,R.color.facebook ,"Facebook", "https://m.facebook.com"));
        sites.add(new Site(R.drawable.favicon_dailymotion,R.color.dailymotion, "Dailymotion", "https://www" +
                ".dailymotion.com"));

        sites.add(new Site(R.drawable.favicon_twitter,R.color.twitter, "Twitter", "https://mobile.twitter.com"));
        sites.add(new Site(R.drawable.favicon_instagram, R.drawable.instagram_bg,"Instagram", "https://www" +
                ".instagram.com"));
        //sites.add(new Site(R.drawable.favicon_veoh,, "veoh", "https://www.veoh.com"));
        sites.add(new Site(R.drawable.favicon_vimeo,R.color.vimeo, "Vimeo", "https://vimeo.com"));
        sites.add(new Site(R.drawable.favicon_vk,R.color.vk, "vk", "https://m.vk.com"));
        //sites.add(new Site(R.drawable.favicon_fc2, "fc2", "https://video.fc2.com"));
        sites.add(new Site(R.drawable.favicon_vlive,R.color.vlive, "Vlive", "https://m.vlive.tv"));
        //sites.add(new Site(R.drawable.favicon_naver, "naver", "https://m.tv.naver.com"));
        //sites.add(new Site(R.drawable.favicon_metacafe, "metacafe", "https://www.metacafe.com"));
        sites.add(new Site(R.drawable.favicon_tudou,R.color.tudou, "Tudou", "https://www.tudou.com"));
        sites.add(new Site(R.drawable.favicon_youku,R.color.youku, "Youku", "https://m.youku.com"));
        sites.add(new Site(R.drawable.favicon_myspace, R.color.myspace,"Myspace", "https://myspace.com"));
        sites.add(new Site(R.drawable.favicon_vine, R.color.vine,"Vine", "https://vine.co"));
        sites.add(new Site(R.drawable.favicon_tumblr,R.color.tumblr, "Tumblr", "https://www.tumblr.com"));

    }

    @Override
    public int getCount() {
        return sites.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder{

        private ImageView icon;
        private LinearLayout background;
        private TextView names;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder mViewHolder = new ViewHolder();



        LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)convertView = vi.inflate(R.layout.imageforgrid,parent,false);


        mViewHolder.icon =(ImageView)convertView.findViewById(R.id.icon);

        mViewHolder.icon.setImageResource(sites.get(position).drawable);

        mViewHolder.background =(LinearLayout)convertView.findViewById(R.id.imageBackground);

        mViewHolder.background.setBackgroundResource(sites.get(position).color);



        mViewHolder.names =(TextView)convertView.findViewById(R.id.mediaText);
        mViewHolder.names.setText(sites.get(position).title);

        mViewHolder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TEST",sites.get(position).title);
                activity.getBrowserManager().newWindow(sites.get(position).url);
                //EditText urlBox =activity.findViewById(R.id.searchEditText);
                  //urlBox.setText(sites.get(position).url);


            }
        });

        //mViewHolder.eng.setText(id[position]);


        return convertView;
    }
}
