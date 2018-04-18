package utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.ufo.tiago.moods.R;

/**
 * Created by Tiago on 5/02/18.
 */

public class EventMoodResponseHolder extends RecyclerView.ViewHolder{

    BarChart barChart;
    ImageView imgMood;
    TextView txtMood,txtSelected;

    public EventMoodResponseHolder(View itemView) {
        super(itemView);
        barChart = (BarChart) itemView.findViewById(R.id.barchart_event_mood);
        imgMood = (ImageView) itemView.findViewById(R.id.img_mood);
        txtMood = (TextView) itemView.findViewById(R.id.title_mood);
        txtSelected = (TextView) itemView.findViewById(R.id.txt_selected);
    }
}
