package utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.ufo.tiago.moods.R;

/**
 * Created by Tiago on 26/10/17.
 */

public class EventViewHolder extends RecyclerView.ViewHolder{

    public CheckBox chxEvent;
    public View viewOther;
    public EditText editOther;

    public EventViewHolder(View itemView) {
        super(itemView);
        chxEvent = (CheckBox) itemView.findViewById(R.id.event_checkbox);
        viewOther = (View) itemView.findViewById(R.id.viewother);
        editOther = (EditText) itemView.findViewById(R.id.other_edit);
    }
}
