package com.ufo.tiago.moods;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import model.Mood;
import utils.Constants;
import utils.MoodRecyclerAdapter;

public class MoodsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMoods;
    private MoodRecyclerAdapter moodRecyclerAdapter;
    private GridLayoutManager moodsLayoutManager;
    public Button btnContinue;
    private ImageButton btnMood1,btnMood2,btnMood3,btnMood4,btnMood5;
    private ImageButton btnBack;

    private List<Mood> moodsArray;
    private Mood moodSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moods);

        //Initialize the moods array
        moodsArray = new ArrayList<Mood>();
        moodsArray.add(new Mood("mood_1","Muy bien",5,R.drawable.mood_happy));
        moodsArray.add(new Mood("mood_2","Bien",4,R.drawable.mood_normal));
        moodsArray.add(new Mood("mood_5","Indiferente",3,R.drawable.mood_5));
        moodsArray.add(new Mood("mood_3","Regular",2,R.drawable.mood_sad));
        moodsArray.add(new Mood("mood_4","Mal",1,R.drawable.mood_angry));

        btnMood1 = (ImageButton) findViewById(R.id.btn_mood_1);
        btnMood2 = (ImageButton) findViewById(R.id.btn_mood_2);
        btnMood3 = (ImageButton) findViewById(R.id.btn_mood_3);
        btnMood4 = (ImageButton) findViewById(R.id.btn_mood_4);
        btnMood5 = (ImageButton) findViewById(R.id.btn_mood_5);

        //Set the content on the recyclerview of moods
        //moodsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        moodsLayoutManager = new GridLayoutManager(this, 1);
        //moodsLayoutManager.setSpanSizeLookup(new  MySizeLookup());
        recyclerViewMoods = (RecyclerView) findViewById(R.id.moods_recyclerview);
        setMoodAdapter(moodsArray);

        btnContinue = (Button) findViewById(R.id.continue_btn);
        btnBack = (ImageButton) findViewById(R.id.back_img);

        // Click on back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Click on continue
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Continue to event selection
                Intent goToEvent = new Intent(MoodsActivity.this,EventSelectionActivity.class);
                goToEvent.putExtra(Constants.MOOD_SELECTED,moodSelected);
                startActivity(goToEvent);
            }
        });

        btnContinue.setVisibility(View.GONE);

        /**
         * Click on moods
         */
        btnMood1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Continue to event selection
                moodSelected = moodsArray.get(0);
                Intent goToEvent = new Intent(MoodsActivity.this,EventSelectionActivity.class);
                goToEvent.putExtra(Constants.MOOD_SELECTED,moodSelected);
                startActivity(goToEvent);
            }
        });
        btnMood2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Continue to event selection
                moodSelected = moodsArray.get(1);
                Intent goToEvent = new Intent(MoodsActivity.this,EventSelectionActivity.class);
                goToEvent.putExtra(Constants.MOOD_SELECTED,moodSelected);
                startActivity(goToEvent);
            }
        });
        btnMood3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Continue to event selection
                moodSelected = moodsArray.get(3);
                Intent goToEvent = new Intent(MoodsActivity.this,EventSelectionActivity.class);
                goToEvent.putExtra(Constants.MOOD_SELECTED,moodSelected);
                startActivity(goToEvent);
            }
        });
        btnMood4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Continue to event selection
                moodSelected = moodsArray.get(4);
                Intent goToEvent = new Intent(MoodsActivity.this,EventSelectionActivity.class);
                goToEvent.putExtra(Constants.MOOD_SELECTED,moodSelected);
                startActivity(goToEvent);
            }
        });
        btnMood5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Continue to event selection
                moodSelected = moodsArray.get(2);
                Intent goToEvent = new Intent(MoodsActivity.this,EventSelectionActivity.class);
                goToEvent.putExtra(Constants.MOOD_SELECTED,moodSelected);
                startActivity(goToEvent);
            }
        });

    }

    /**
     * Set the moods recyclerview adapter with a list of categoies
     */
    private void setMoodAdapter(List<Mood> moodsList){
        moodRecyclerAdapter = new MoodRecyclerAdapter(moodsList,this,this);
        recyclerViewMoods.setAdapter(moodRecyclerAdapter);
        recyclerViewMoods.setLayoutManager(moodsLayoutManager);
    }

    public Mood getMoodSelected() {
        return moodSelected;
    }

    public void setMoodSelected(Mood moodSelected) {
        this.moodSelected = moodSelected;
    }
}
