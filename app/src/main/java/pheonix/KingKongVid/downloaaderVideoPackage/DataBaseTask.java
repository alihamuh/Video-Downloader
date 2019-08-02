package pheonix.KingKongVid.downloaaderVideoPackage;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;

import pheonix.KingKongVid.downloaaderVideoPackage.download.DownloadVideo;


public class DataBaseTask extends AsyncTask <DownloadVideo,Void,Bitmap> {


      View itemView;
    public DataBaseTask(View v){
        this.itemView=v;
    }

    @Override
    public Bitmap doInBackground(DownloadVideo... indext) {


             Bitmap myBitmap=null;
            try {

                myBitmap =retriveVideoFrameFromVideo(indext[0].thumbnail);
            }catch (Throwable e){
                e.printStackTrace();
            }

            int x =myBitmap.getWidth();
            int y =myBitmap.getHeight();
             Bitmap newBitmap;
            if(x>y){
              int z =(x-y)/2;
                newBitmap= Bitmap.createBitmap(myBitmap,z,0,y,y);
            }else{
                int z =(y-x)/2;
                newBitmap=Bitmap.createBitmap(myBitmap,z,0,x,x);
            }

       return newBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        ImageView thumbnail = itemView.findViewById(R.id.video_thumbnail);

        thumbnail.setImageBitmap(bitmap);
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }



}
