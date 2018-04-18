package utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ufo.tiago.moods.EventSelectionActivity;
import com.ufo.tiago.moods.FormsActivity;
import com.ufo.tiago.moods.QuestionsActivity;
import com.ufo.tiago.moods.R;

import java.util.ArrayList;
import java.util.List;

import model.Event;
import model.Form;

/**
 * Created by Tiago on 27/10/17.
 */

public class FormRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<FormViewHolder> holdersList;
    private List<Form> formsList;
    private Context context;
    private FormsActivity formsActivity;

    public FormRecyclerAdapter(List<Form> formsList, Context context, FormsActivity formsActivity) {
        this.formsList = formsList;
        this.context = context;
        this.formsActivity = formsActivity;
        holdersList = new ArrayList<>();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_form_description, parent, false);
        RecyclerView.ViewHolder viewHolder = new FormViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Form form = formsList.get(position);
        FormViewHolder formViewHolder = (FormViewHolder) holder;

        // Get the answered questions
        String answeredQuestions = "0";
        String totalQuestionsText = answeredQuestions + "/" + form.getTotalQuestions();

        formViewHolder.txtTitle.setText(form.getName());
        formViewHolder.txtActiveUntil.setText(form.getCloseDate());
        formViewHolder.txtTotalQuestions.setText(totalQuestionsText);

        if(form.isAlreadySended()) {
            formViewHolder.txtStatus.setText(context.getString(R.string.status_sended));
            formViewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else{
            formViewHolder.txtStatus.setText(context.getString(R.string.status_pending));
            formViewHolder.txtStatus.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

        //Click on item
        formViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to the Questions of the form
                if (!form.isAlreadySended()) {
                    Intent goToQuestion = new Intent(context, QuestionsActivity.class);
                    goToQuestion.putExtra(Constants.FORM_SELECTED, form);
                    context.startActivity(goToQuestion);
                }
            }
        });

        holdersList.add(formViewHolder);
    }

    @Override
    public int getItemCount() {
        return formsList.size();
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
