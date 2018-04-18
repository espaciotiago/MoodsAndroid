package utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;

import com.ufo.tiago.moods.EventSelectionActivity;
import com.ufo.tiago.moods.MoodsActivity;
import com.ufo.tiago.moods.R;

import java.util.ArrayList;
import java.util.List;

import model.Event;
import model.Mood;

/**
 * Created by Tiago on 26/10/17.
 */

public class EventRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<EventViewHolder> holdersList;
    private List<Event> eventsList;
    private Context context;
    private EventSelectionActivity eventSelectionActivity;

    public EventRecyclerAdapter(List<Event> eventsList, Context context, EventSelectionActivity eventSelectionActivity) {
        holdersList = new ArrayList<>();
        this.eventsList = eventsList;
        this.context = context;
        this.eventSelectionActivity = eventSelectionActivity;
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_selection, parent, false);
        RecyclerView.ViewHolder viewHolder = new EventViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final Event event = eventsList.get(position);
        final EventViewHolder eventViewHolder = (EventViewHolder) holder;

        eventViewHolder.chxEvent.setText(event.getLabel());
        eventViewHolder.chxEvent.setChecked(event.isChecked());

        //When a option is selected
        eventViewHolder.chxEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                removeAllSelections();

                //Other option is selected
                if(isChecked && event.getLabel().equals(context.getString(R.string.other_option)) || event.getLabel().equals("Other")){
                    //eventSelectionActivity.scrollOther.setVisibility(View.VISIBLE);
                    eventViewHolder.viewOther.setVisibility(View.VISIBLE);
                    eventViewHolder.editOther.setFocusableInTouchMode(true);
                    eventViewHolder.editOther.requestFocus();
                    eventSelectionActivity.setOtherSeleced(true);
                }else if(!isChecked && event.getLabel().equals(context.getString(R.string.other_option))){
                    //eventSelectionActivity.scrollOther.setVisibility(View.INVISIBLE);
                    eventViewHolder.viewOther.setVisibility(View.GONE);
                    eventSelectionActivity.setOtherSeleced(false);
                }
                //Set the selection
                if (isChecked){
                    eventSelectionActivity.setSelectedEvent(event);
                    event.setChecked(true);
                }else {
                    eventSelectionActivity.setSelectedEvent(null);
                    event.setChecked(false);
                }

                if(eventSelectionActivity.getSelectedEvent()!=null){
                    eventSelectionActivity.btnSend.setEnabled(true);
                    eventSelectionActivity.setPositionSelected(position);
                }else {
                    eventSelectionActivity.btnSend.setEnabled(false);
                }
            }
        });

        holdersList.add(eventViewHolder);

    }

    /**
     * Set an error in the "other" edit text if is empty
     * @param position
     */
    public void notifyError(int position){
        holdersList.get(position).editOther.setError(context.getString(R.string.needed_field));
    }

    public String getOtherText(int position){
        return holdersList.get(position).editOther.getText().toString();
    }

    /**
     * Set all the items unchecked
     */
    private void removeAllSelections(){
        eventSelectionActivity.setSelectedEvent(null);
        for (int i = 0; i < holdersList.size();i++){
            if(i<eventsList.size()) {
                if (eventsList.get(i).isChecked()) {
                    holdersList.get(i).chxEvent.setChecked(false);
                }
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}
