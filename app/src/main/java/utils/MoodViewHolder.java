package utils;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ufo.tiago.moods.R;

/**
 * Created by Tiago on 25/10/17.
 */

public class MoodViewHolder extends RecyclerView.ViewHolder{

    public ImageView moodImg;
    public TextView moodTxt;
    View layout;


    public MoodViewHolder(View itemView) {
        super(itemView);
        layout = (View) itemView.findViewById(R.id.layout);
        moodImg = (ImageView) itemView.findViewById(R.id.mood_img);
        moodTxt = (TextView) itemView.findViewById(R.id.mood_text);

    }
}
