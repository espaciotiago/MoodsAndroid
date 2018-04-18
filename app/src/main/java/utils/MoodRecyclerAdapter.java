package utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ufo.tiago.moods.CollaboratorMenuActivity;
import com.ufo.tiago.moods.EventSelectionActivity;
import com.ufo.tiago.moods.MoodsActivity;
import com.ufo.tiago.moods.R;

import java.util.ArrayList;
import java.util.List;

import model.Mood;

/**
 * Created by Tiago on 25/10/17.
 */

public class MoodRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MoodViewHolder> holdersList;
    private List<Mood> moodsList;
    private Context context;
    private MoodsActivity moodActivity;

    public MoodRecyclerAdapter(List<Mood> moodsList, Context context, MoodsActivity moodActivity) {
        this.moodsList = moodsList;
        this.context = context;
        this.moodActivity = moodActivity;
        holdersList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mood_selection, parent, false);
        RecyclerView.ViewHolder viewHolder = new MoodViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Mood mood = moodsList.get(position);
        final MoodViewHolder moodViewHolder = (MoodViewHolder) holder;
        moodViewHolder.moodImg.setImageResource(mood.getResource());
        moodViewHolder.moodTxt.setText(mood.getMood());

        if (mood.getIdInServer().equals("")){
            //moodViewHolder.itemView.setVisibility(View.GONE);
            ViewGroup.LayoutParams params= moodViewHolder.layout.getLayoutParams();
            params.width= 0;
            //params.height= 400;
            moodViewHolder.itemView.setLayoutParams(params);
        }

        //Mood Selected
        moodViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set this as selected
                //moodActivity.btnContinue.setBackground(context.getResources().getDrawable(R.drawable.orange_selector));
                moodActivity.btnContinue.setEnabled(true);
                removeAllSelection();
                moodActivity.setMoodSelected(mood);
                moodViewHolder.moodImg.setBackground(context.getResources().getDrawable(R.drawable.circle_shape));

                Intent goToEvent = new Intent(moodActivity.getApplicationContext(),EventSelectionActivity.class);
                goToEvent.putExtra(Constants.MOOD_SELECTED,mood);
                moodActivity.startActivity(goToEvent);
            }
        });

        holdersList.add(moodViewHolder);
    }

    /**
     * Remove the params of the selected mood
     */
    private void removeAllSelection(){
        moodActivity.setMoodSelected(null);
        for (int i = 0; i < holdersList.size(); i++){
            holdersList.get(i).moodImg.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return moodsList.size();
    }
}
