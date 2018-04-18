package utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.ufo.tiago.moods.R;

/**
 * Created by Tiago on 29/10/17.
 */

public class OptionViewHolder extends RecyclerView.ViewHolder {

    public CheckBox chxOption;

    public OptionViewHolder(View itemView) {
        super(itemView);
        chxOption = (CheckBox) itemView.findViewById(R.id.option_chk);
    }
}
