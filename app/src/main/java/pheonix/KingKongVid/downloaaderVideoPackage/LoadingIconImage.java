package pheonix.KingKongVid.downloaaderVideoPackage;

import pheonix.KingKongVid.downloaaderVideoPackage.browsing.TouchableWebView;

public class LoadingIconImage {


    TouchableWebView page;

    public LoadingIconImage(TouchableWebView Page){
     this.page =Page;
    }

    public int LoadingImage(){

        if(page.getTitle().contains("facebook")){
            return R.drawable.favicon_facebook;
        }else if(page.getTitle().contains("twitter")){
            return R.drawable.favicon_twitter;
        }else if(page.getTitle().contains("tudou")){
            return R.drawable.favicon_tudou;
        }else if(page.getTitle().contains("tumblr")){
            return R.drawable.favicon_tumblr;
        }else if(page.getTitle().contains("twitter")){
            return R.drawable.favicon_dailymotion;
        }else if(page.getTitle().contains("fc2")){
            return R.drawable.favicon_fc2;
        }else if(page.getTitle().contains("instagram")){
            return R.drawable.favicon_instagram;
        }else if(page.getTitle().contains("metacafe")){
            return R.drawable.favicon_metacafe;
        }else if(page.getTitle().contains("myspace")){
            return R.drawable.favicon_myspace;
        }else if(page.getTitle().contains("vimeo")){
            return R.drawable.favicon_vimeo;
        }else if(page.getTitle().contains("vine")){
            return R.drawable.favicon_vine;
        }else if(page.getTitle().contains("vk")){
            return R.drawable.favicon_vk;
        }else if(page.getTitle().contains("youku")){
            return R.drawable.favicon_youku;
        }else if(page.getTitle().contains("vlive")){
            return R.drawable.favicon_vlive;
        }

        return -1;
    }
}
