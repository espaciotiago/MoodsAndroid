package utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ufo.tiago.moods.FormsActivity;
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

public class QuestionsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<QuestionViewHolder> holdersList;
    private List<FormQuestion> questionList;
    private List<FormOption> optionList;
    private Context context;
    private QuestionsActivity questionsActivity;
    private int actualPage;

    public QuestionsRecyclerAdapter(List<FormQuestion> questionList, Context context, QuestionsActivity questionsActivity,int actualPage, List<FormOption> optionList) {
        this.holdersList = new ArrayList<>();
        this.questionList = questionList;
        this.context = context;
        this.questionsActivity = questionsActivity;
        this.optionList = optionList;
        this.actualPage = actualPage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        RecyclerView.ViewHolder viewHolder = new QuestionViewHolder(v,context,questionsActivity,optionList);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FormQuestion formQuestion = questionList.get(position);
        QuestionViewHolder questionViewHolder = (QuestionViewHolder) holder;
        questionViewHolder.setFormQuestion(formQuestion);

        int index = (actualPage-1)*Constants.FORM_PAGINATION + position+1;
        questionViewHolder.txtQuestionHeader.setText(index+". "+formQuestion.getHeaderText());

        holdersList.add(questionViewHolder);
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
        return questionList.size();
    }
}
