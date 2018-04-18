package utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ufo.tiago.moods.R;

/**
 * Created by Tiago on 27/10/17.
 */

public class FormViewHolder extends RecyclerView.ViewHolder{

    public TextView txtTitle,txtTotalQuestions,txtActiveUntil,txtStatus;


    public FormViewHolder(View itemView) {
        super(itemView);
        txtTitle = (TextView) itemView.findViewById(R.id.form_title_txt);
        txtTotalQuestions = (TextView) itemView.findViewById(R.id.total_questions_txt);
        txtActiveUntil = (TextView) itemView.findViewById(R.id.active_until_txt);
        txtStatus = (TextView) itemView.findViewById(R.id.status_txt);
    }
}
