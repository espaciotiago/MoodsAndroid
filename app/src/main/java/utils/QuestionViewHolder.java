package utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ufo.tiago.moods.QuestionsActivity;
import com.ufo.tiago.moods.R;

import java.util.ArrayList;
import java.util.List;

import model.Form;
import model.FormOption;
import model.FormQuestion;

/**
 * Created by Tiago on 29/10/17.
 */

public class QuestionViewHolder extends RecyclerView.ViewHolder {

    public TextView txtQuestionHeader;
    public RecyclerView recyclerViewOptions;
    private OptionRecyclerAdapter optionRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private QuestionsActivity questionsActivity;
    private List<FormOption> optionList;
    private FormQuestion formQuestion;

    public QuestionViewHolder(View itemView, Context context, QuestionsActivity questionsActivity,List<FormOption> optionList) {
        super(itemView);

        //Get the options
        this.optionList = new ArrayList<>();
        for (int i = 0; i < optionList.size(); i++){
            FormOption formOption = optionList.get(i);
            FormOption newFormOption = new FormOption(formOption.getIdInServer(),formOption.getLabel(),formOption.getValue());
            this.optionList.add(newFormOption);
        }

        //Set the UI
        txtQuestionHeader = (TextView) itemView.findViewById(R.id.question_header_txt);
        recyclerViewOptions = (RecyclerView) itemView.findViewById(R.id.option_recyclerview);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        this.context = context;
        this.questionsActivity = questionsActivity;
        //Set the adapter of the options
        setOptionAdapter(this.optionList,this);
    }

    /**
     * Set the options recyclerview adapter with a list of forms
     */
    private void setOptionAdapter(List<FormOption> optionList,QuestionViewHolder holder){
        optionRecyclerAdapter = new OptionRecyclerAdapter(optionList,context,questionsActivity,holder);
        recyclerViewOptions.setAdapter(optionRecyclerAdapter);
        recyclerViewOptions.setLayoutManager(layoutManager);
    }

    public FormQuestion getFormQuestion() {
        return formQuestion;
    }

    public void setFormQuestion(FormQuestion formQuestion) {
        this.formQuestion = formQuestion;
    }
}
