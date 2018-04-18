package com.ufo.tiago.moods;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import db_models.DaoSession;
import db_models.Parameters;
import db_models.Team;
import db_models.UserSession;
import model.Event;
import model.Mood;
import model.ResponseMood;
import utils.Constants;
import utils.EventRecyclerAdapter;
import utils.MoodRecyclerAdapter;
import utils.NetworkHelper;

public class EventSelectionActivity extends AppCompatActivity {

    private ImageView imgSelectedMood;
    private ImageButton btnBack;
    public Button btnSend;
    public ScrollView scrollOther;
    private EventRecyclerAdapter eventRecyclerAdapter;
    private RecyclerView recyclerviewEvents;
    private LinearLayoutManager layoutManager;
    private ProgressBar loader;

    private Mood selectedMood;
    private Event selectedEvent;
    private List<Event> eventsList;
    private int positionSelected;
    private boolean otherSeleced;
    private NetworkHelper networkHelper;
    private UserSession userSession;
    private Parameters parameters;
    private DaoSession daoSession;
    private String teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_selection);

        daoSession = ((DaoMoodsApp)getApplication()).getDaoSession();
        networkHelper = new NetworkHelper(this);
        List<UserSession> sessions = daoSession.getUserSessionDao().loadAll();
        userSession = sessions.get(0);
        List<Parameters> parametersList = daoSession.getParametersDao().loadAll();
        parameters = parametersList.get(0);

        //Get the selected Mood
        Intent intent = getIntent();
        selectedMood = (Mood) intent.getSerializableExtra(Constants.MOOD_SELECTED);

        List<Team> teams = daoSession.getTeamDao().loadAll();
        if(teams.size()>0) {
            teamId = teams.get(0).getIdequipo();
            Log.d("TEAM ID EVENT", teamId);
        }

        //Init the UI elements
        loader = (ProgressBar) findViewById(R.id.loader);
        imgSelectedMood = (ImageView) findViewById(R.id.selected_mood_img);
        btnBack = (ImageButton) findViewById(R.id.back_img);
        btnSend = (Button) findViewById(R.id.send_btn);
        //Set the content on the recyclerview of moods
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerviewEvents = (RecyclerView) findViewById(R.id.events_recyclerview);
        //scrollOther = (ScrollView) findViewById(R.id.scroll_other);

        //Get the events
        new GetEvents(networkHelper.jsonForGetEvents(userSession.getId_server())).execute();

        //Set the UI info
        imgSelectedMood.setImageResource(selectedMood.getResource());

        // Click on back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedEvent!=null) {
                    String otherText = eventRecyclerAdapter.getOtherText(getPositionSelected());
                    boolean next = false;
                    //Other option selected
                    if (isOtherSeleced()) {
                        if (otherText == null || otherText.equals("")) {
                            eventRecyclerAdapter.notifyError(getPositionSelected());
                        } else {
                            selectedEvent.setExtraText(otherText);
                            next = true;
                        }
                    }else{ //Normal option selected
                        next = true;
                    }

                    if (next){
                        Log.e("Response mood","Entro");
                        //Send the mood selected and the asociated event
                        //Get the workday TODO
                        int workday = getCurrentJornada();
                        //Get the current date
                        String currentDate = Constants.formatDateToSend(new Date());
                        ResponseMood responseMood = new ResponseMood(selectedMood,selectedEvent,workday,currentDate,otherText);

                        new sendMood(networkHelper.jsonForSendMood(userSession.getId_server(),responseMood,teamId)).execute();
                    }
                }
            }
        });
    }

    /**
     * Set the events recyclerview adapter with a list of categoies
     */
    private void setEventAdapter(List<Event> eventList){
        eventRecyclerAdapter = new EventRecyclerAdapter(eventList,this,this);
        recyclerviewEvents.setAdapter(eventRecyclerAdapter);
        recyclerviewEvents.setLayoutManager(layoutManager);
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public int getPositionSelected() {
        return positionSelected;
    }

    public void setPositionSelected(int positionSelected) {
        this.positionSelected = positionSelected;
    }

    public boolean isOtherSeleced() {
        return otherSeleced;
    }

    public void setOtherSeleced(boolean otherSeleced) {
        this.otherSeleced = otherSeleced;
    }

    /**
     *
     * @return 
     */
    private int getCurrentJornada(){
        int jornada = 1;

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parser2 = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String date = parser2.format(new Date());
        String horaInico = parameters.getStart_hour();
        String horaFin = parameters.getEnd_hour();
        try {
            Date dateInicio = parser.parse(horaInico);
            Date dateFin = parser.parse(horaFin);
            Date now = parser.parse(date);
            if (now.after(dateInicio) && now.before(dateFin)){
                jornada = 1;
            }else if(now.after(dateFin)){
                jornada = 2;
            }else{
                jornada = 2;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("JORNADA",jornada+"");
        return jornada;
    }

    /** --------------------------------------------------------------------------------------------
     * Asynctask: Bring the params of configuration and more, from the server
     -------------------------------------------------------------------------------------------- */
    public class sendMood extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject jsonObject;

        public sendMood(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            //TODO do the petition
            Log.e("JSON TO SEND",jsonObject.toString());
            return networkHelper.sendPOST(jsonObject,Constants.SEND_MOOD_URL);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            loader.setVisibility(View.GONE);
            //TODO Get the response  - Handle errors
            boolean error = (jsonObject==null);

            if (error){
                //Go to the error activity
                Intent goToError = new Intent(EventSelectionActivity.this, ErrorActivity.class);
                goToError.putExtra(Constants.ERROR_RESPONSE, "Error en el servidor");
                startActivity(goToError);
            }else {
                Log.e("JSON RESPONSE",jsonObject.toString());
                try {
                    boolean ans = jsonObject.getBoolean(Constants.ANS);

                    if (ans){
                        String body = jsonObject.getString(Constants.BODY);
                        Log.e("BODY",body.toString());
                        //Init the events list
                        //Go to the success activity
                        //Set the quote text with a random phrase
                        String[] quotesArray = getResources().getStringArray(R.array.quotes);
                        int quotesLenght = quotesArray.length;
                        Random r = new Random();
                        int index = r.nextInt(quotesLenght);

                        Intent goToSuccess = new Intent(EventSelectionActivity.this, SuccessActivity.class);
                        goToSuccess.putExtra(Constants.SUCCESS_SEND_STR, quotesArray[index]);
                        goToSuccess.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goToSuccess);
                        finish();
                    }else{
                        String errorInJson = jsonObject.getString(Constants.ERROR);
                        //Go to the error activity
                        Intent goToError = new Intent(EventSelectionActivity.this, ErrorActivity.class);
                        goToError.putExtra(Constants.ERROR_RESPONSE,errorInJson + "\n" + "Error de autenticación: Nombre de usuario o contraseña incorrectos");
                        startActivity(goToError);
                    }

                }catch (Exception e){
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(EventSelectionActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }
            }
        }

    }

    /** --------------------------------------------------------------------------------------------
     * Asynctask: Bring the params of configuration and more, from the server
     -------------------------------------------------------------------------------------------- */
    public class GetEvents extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject jsonObject;

        public GetEvents(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            //Do the petition
            Log.e("JSON TO SEND",jsonObject.toString());
            return networkHelper.sendPOST(jsonObject,Constants.GET_EVENTS_URL);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            loader.setVisibility(View.GONE);
            //Get the response  - Handle errors
            boolean error = (jsonObject==null);

            if (error){
                //Go to the error activity
                Intent goToError = new Intent(EventSelectionActivity.this, ErrorActivity.class);
                goToError.putExtra(Constants.ERROR_RESPONSE, "Error en el servidor");
                startActivity(goToError);
            }else {
                Log.e("JSON RESPONSE",jsonObject.toString());
                try {
                    boolean ans = jsonObject.getBoolean(Constants.ANS);

                    if (ans){
                        JSONArray body = jsonObject.getJSONArray(Constants.BODY);
                        Log.e("BODY",body.toString());
                        //Init the events list
                        eventsList = new ArrayList<>();
                        for (int i = 0; i < body.length(); i++){
                            JSONObject events = body.getJSONObject(i);
                            String name = events.getString(Constants.NAME);
                            String idEvento = events.getString(Constants.EVENT_ID);
                            Event event = new Event(idEvento,name);
                            eventsList.add(event);
                        }
                        setEventAdapter(eventsList);
                    }else{
                        String errorInJson = jsonObject.getString(Constants.ERROR);
                        //Go to the error activity
                        Intent goToError = new Intent(EventSelectionActivity.this, ErrorActivity.class);
                        goToError.putExtra(Constants.ERROR_RESPONSE,errorInJson + "\n" + "Error de autenticación: Nombre de usuario o contraseña incorrectos");
                        startActivity(goToError);
                    }

                }catch (Exception e){
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(EventSelectionActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }
            }
        }

    }
}
