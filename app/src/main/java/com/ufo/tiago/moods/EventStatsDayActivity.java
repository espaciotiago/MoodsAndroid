package com.ufo.tiago.moods;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import db_models.UserSession;
import model.Event;
import model.MoodEventContainer;
import model.MoodsEventResponse;
import utils.Constants;
import utils.EventMoodResponseAdapter;
import utils.EventRecyclerAdapter;
import utils.NetworkHelper;

public class EventStatsDayActivity extends AppCompatActivity {

    private static int NUMBER_OF_MOODS = 5;
    private static String MOOD_PREFIX = "mood_";

    private ImageButton btnBack;
    private RecyclerView recyclerEvents;
    private EventMoodResponseAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ProgressBar loader;

    private NetworkHelper networkHelper;
    private String userId;
    private String teamId;
    private String date;
    private List<MoodEventContainer> containers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_stats_day);

        networkHelper = new NetworkHelper(this);
        userId = getIntent().getStringExtra("user");
        teamId = getIntent().getStringExtra("team");
        date = getIntent().getStringExtra("date");

        btnBack = (ImageButton) findViewById(R.id.back_img);
        recyclerEvents  = (RecyclerView) findViewById(R.id.recycler_events);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        loader = (ProgressBar) findViewById(R.id.loader);

        containers = new ArrayList<>();
        new GetStats(networkHelper.jsonForGetBarStats(userId,teamId,date)).execute();

        /**
         * Actions
         */
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Set the events recyclerview adapter with a list of categoies
     */
    private void setEventAdapter(List<MoodEventContainer> eventList){
        adapter = new EventMoodResponseAdapter(eventList,this,this);
        recyclerEvents.setAdapter(adapter);
        recyclerEvents.setLayoutManager(layoutManager);
    }

    /** --------------------------------------------------------------------------------------------
     * Asynctask: Bring the params of configuration and more, from the server
     -------------------------------------------------------------------------------------------- */
    public class GetStats extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject jsonObject;

        public GetStats(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            return networkHelper.sendPOST(jsonObject, Constants.GET_BARS_STATS_URL);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            loader.setVisibility(View.GONE);

            boolean error = (jsonObject == null);
            if(error){
                //Show error - Network error
                Intent goToError = new Intent(EventStatsDayActivity.this, ErrorActivity.class);
                goToError.putExtra(Constants.ERROR_NETWORK,getString(R.string.network_error));
                startActivity(goToError);
            }else{
                try {
                    for(int i = 0; i < NUMBER_OF_MOODS; i++){
                        int val = i+1;
                        String idMood = MOOD_PREFIX+val;
                        JSONArray moodArray = jsonObject.getJSONArray(idMood);
                        ArrayList<MoodsEventResponse> arrayList = new ArrayList<>();
                        for (int j =0;j< moodArray.length();j++){
                            JSONObject jsonMood = moodArray.getJSONObject(j);
                            String idevento = jsonMood.getString("idevento");
                            String nombre = jsonMood.getString("nombre");
                            int quantity = jsonMood.getInt("cantidad");
                            MoodsEventResponse mer = new MoodsEventResponse(nombre,idevento,quantity);
                            arrayList.add(mer);
                        }
                        MoodEventContainer m=null;
                        switch (idMood){
                            case "mood_1":
                                m = new MoodEventContainer(arrayList,R.drawable.mood_happy,"Muy Bien");
                                break;
                            case "mood_2":
                                m = new MoodEventContainer(arrayList,R.drawable.mood_normal,"Bien");
                                break;
                            case "mood_3":
                                m = new MoodEventContainer(arrayList,R.drawable.mood_sad,"Mal");
                                break;
                            case "mood_4":
                                m = new MoodEventContainer(arrayList,R.drawable.mood_angry,"Muy Mal");
                                break;
                            case "mood_5":
                                m = new MoodEventContainer(arrayList,R.drawable.mood_5,"Indiferente");
                                break;
                        }
                        if(m!=null){
                            containers.add(m);
                        }
                        setEventAdapter(containers);
                    }

//                    boolean ans = jsonObject.getBoolean(Constants.ANS);
//                    if (ans){
//                        JSONObject body = jsonObject.getJSONObject(Constants.BODY);
//                        Log.e("BODY",body.toString());
//
//
//                    }else{
//                        String errorInJson = jsonObject.getString(Constants.ERROR);
//                        //Go to the error activity
//                        Intent goToError = new Intent(EventStatsDayActivity.this, ErrorActivity.class);
//                        goToError.putExtra(Constants.ERROR_RESPONSE,errorInJson + "\n" + "Error de autenticación: Nombre de usuario o contraseña incorrectos");
//                        startActivity(goToError);
//                    }

                }catch (Exception e){
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(EventStatsDayActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }
            }
        }

    }
}
