package utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.ufo.tiago.moods.EventSelectionActivity;
import com.ufo.tiago.moods.EventStatsDayActivity;
import com.ufo.tiago.moods.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Event;
import model.MoodEventContainer;
import model.MoodsEventResponse;

/**
 * Created by Tiago on 5/02/18.
 */

public class EventMoodResponseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EventMoodResponseHolder> holdersList;
    private List<MoodEventContainer> eventsList;
    private Context context;
    private EventStatsDayActivity eventStatsDayActivity;

    public EventMoodResponseAdapter(List<MoodEventContainer> eventsList, Context context, EventStatsDayActivity eventStatsDayActivity){
        Log.e("In holder","EventMoodResponseAdapter");
        holdersList = new ArrayList<>();
        this.eventsList = eventsList;
        this.context = context;
        this.eventStatsDayActivity = eventStatsDayActivity;
        setHasStableIds(true);
        Log.e("In holder",eventsList.size()+"");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bar_stats, parent, false);
        RecyclerView.ViewHolder viewHolder = new EventMoodResponseHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final MoodEventContainer event = eventsList.get(position);
        final EventMoodResponseHolder eventViewHolder = (EventMoodResponseHolder) holder;

        eventViewHolder.imgMood.setImageResource(event.getResource());
        eventViewHolder.txtMood.setText(event.getMoodTitle());

        /**
         * Do the charts
         */
        ArrayList<BarEntry> entries = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < event.getMoodsEventResponseList().size(); i++) {
            MoodsEventResponse mer = event.getMoodsEventResponseList().get(i);
            entries.add(new BarEntry(i, mer.getQuantity(), mer.getName()));
            labels.add(mer.getName());
        }
        BarDataSet dataSet = new BarDataSet(entries, "Cantidad de eventos");
        BarData data = new BarData(dataSet);
        Description description = new Description();
        description.setText("");
        eventViewHolder.barChart.getAxisLeft().setDrawGridLines(false);
        eventViewHolder.barChart.getXAxis().setDrawGridLines(false);
        eventViewHolder.barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        eventViewHolder.barChart.getAxisRight().setDrawGridLines(false);
        eventViewHolder.barChart.getAxisRight().setEnabled(false);
        eventViewHolder.barChart.setDescription(description);
        //eventViewHolder.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        eventViewHolder.barChart.setData(data);
        eventViewHolder.barChart.invalidate();

        eventViewHolder.barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                final String x = eventViewHolder.barChart.getXAxis().getValueFormatter().getFormattedValue(e.getX(),
                        eventViewHolder.barChart.getXAxis());
                try{
                    int pos = Integer.parseInt(x);
                    String label = labels.get(pos);
                    eventViewHolder.txtSelected.setText(label);
                }catch (Exception ex){

                }
            }

            @Override
            public void onNothingSelected() {
                eventViewHolder.txtSelected.setText("");
            }
        });
    }



    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
