package pheonix.KingKongVid.downloaaderVideoPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class pheonixArrayAdapter extends BaseAdapter{
    private final Context context;
    private final String[] engText;
    LayoutInflater inflater;


    public pheonixArrayAdapter(Context context, String[] eng) {
        this.context = context;
        this.engText = eng;
        inflater =(LayoutInflater.from(context));

    }


    @Override
    public int getCount() {
        return engText.length;
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

        private TextView eng;
        private TextView author;
        private Button likeBtn;
        private Button shareBtn;
        private Button saveBtn;
        private Button copyBtn;
        private LinearLayout snap;
   }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder mViewHolder = new ViewHolder();



        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //if (convertView == null)convertView = vi.inflate(R.layout.nav_button,parent,false);

        //mViewHolder.eng =(TextView)convertView.findViewById(R.id.engText);

        //mViewHolder.eng.setText(engText[position]);


        return convertView;
    }



}

