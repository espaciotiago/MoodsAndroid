package utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.ufo.tiago.moods.QuestionsActivity;
import com.ufo.tiago.moods.R;

import java.util.ArrayList;
import java.util.List;

import model.FormOption;
import model.FormQuestion;

/**
 * Created by Tiago on 29/10/17.
 */

public class OptionRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OptionViewHolder> holdersList;
    private List<FormOption> optionList;
    private Context context;
    private QuestionsActivity questionsActivity;
    private QuestionViewHolder questionViewHolder;

    public OptionRecyclerAdapter(List<FormOption> optionList, Context context, QuestionsActivity questionsActivity, QuestionViewHolder questionViewHolder) {
        this.holdersList = holdersList;
        this.optionList = optionList;
        this.context = context;
        this.questionsActivity = questionsActivity;
        holdersList = new ArrayList<>();
        this.questionViewHolder = questionViewHolder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option, parent, false);
        RecyclerView.ViewHolder viewHolder = new OptionViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final FormOption formOption = optionList.get(position);
        final int pos = position;
        OptionViewHolder optionViewHolder = (OptionViewHolder) holder;

        optionViewHolder.chxOption.setText(formOption.getLabel());

        if (questionViewHolder.getFormQuestion()!=null && questionViewHolder.getFormQuestion().getSelectedOption()!=null ){
            if (questionViewHolder.getFormQuestion().getSelectedOption().getIdInServer().equals(formOption.getIdInServer())) {
                optionViewHolder.chxOption.setChecked(true);
            }
        }

        //When the option is selected
        optionViewHolder.chxOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                removeAllSelections();

                if (isChecked){
                    optionList.get(pos).setChecked(true);
                    questionViewHolder.getFormQuestion().setSelectedOption(formOption);

                }else{
                    optionList.get(pos).setChecked(false);
                    questionViewHolder.getFormQuestion().setSelectedOption(null);
                }
            }
        });

        holdersList.add(optionViewHolder);
    }

    /**
     * Set all the items unchecked
     */
    private void removeAllSelections(){

        for (int i = 0; i < holdersList.size();i++){
            if(i<optionList.size()) {
                if (optionList.get(i).isChecked()) {
                    holdersList.get(i).chxOption.setChecked(false);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return optionList.size();
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
