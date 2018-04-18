package com.ufo.tiago.moods;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import db_models.DaoSession;
import db_models.Parameters;
import db_models.Team;
import db_models.UserSession;
import utils.Constants;
import utils.NetworkHelper;

public class LoadingActivity extends AppCompatActivity {

    private List<ImageView> imgsViews;
    private DaoSession daoSession;
    private boolean fromNotificationToSendMood;
    private int i;
    private NetworkHelper networkHelper;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        daoSession = ((DaoMoodsApp)getApplication()).getDaoSession();
        networkHelper = new NetworkHelper(this);

        fromNotificationToSendMood = getIntent().getBooleanExtra(Constants.NOTIFICATION_MOOD,false);

        imgsViews = new ArrayList<ImageView>();
        i = 0;
        //Set the images in the array
        imgsViews.add((ImageView)findViewById(R.id.mood_happy_img));
        imgsViews.add((ImageView)findViewById(R.id.mood_normal_img));
        imgsViews.add((ImageView)findViewById(R.id.mood_sad_img));
        imgsViews.add((ImageView)findViewById(R.id.mood_angry_img));
        //Do the effect
        countImages();


        //Get the info of the database
        List<UserSession> sessions = daoSession.getUserSessionDao().loadAll();

        if(sessions.isEmpty()){
            //There isn't a user loged - Got to login
            Intent goToLoading = new Intent(LoadingActivity.this,LoginActivity.class);
            goToLoading.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goToLoading);
        }else {
            //TODO Get the params and configure from server
            userSession = sessions.get(0);
            new GetParameters(networkHelper.jsonForGetParamsLoading(userSession.getId_server(),userSession.getRol_id(),Constants.formatDateToSend(new Date())))
                    .execute();
        }

    }

    /**
     * Set the visibility of the moods images, for the loading effect
     */
    private void countImages(){
        new CountDownTimer(500,1000) {

            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                if(i == imgsViews.size()){
                    for(int j = 0; j < imgsViews.size();j++){
                        imgsViews.get(j).setVisibility(View.INVISIBLE);
                    }
                    i=0;
                }else {
                    imgsViews.get(i).setVisibility(View.VISIBLE);
                    i++;
                }
                start();
            }
        }.start();
    }

    /** --------------------------------------------------------------------------------------------
     * Asynctask: Bring the params of configuration and more, from the server
     -------------------------------------------------------------------------------------------- */
    public class GetParameters extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject jsonObject;

        public GetParameters(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            //TODO do the petition
            Log.e("JSON TO SEND", jsonObject.toString());
            return networkHelper.sendPOST(jsonObject,Constants.GET_PARAMS_URL);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            boolean error = (jsonObject == null);
            if (error) {
                //Show error - Network error
                Intent goToError = new Intent(LoadingActivity.this, ErrorActivity.class);
                goToError.putExtra(Constants.ERROR_NETWORK, getString(R.string.network_error));
                startActivity(goToError);
            } else {

                Log.e("JSON RESPONSE", jsonObject.toString());
                try {
                    boolean ans = jsonObject.getBoolean(Constants.ANS);

                    if (ans){
                        JSONObject body = jsonObject.getJSONObject(Constants.BODY);
                        JSONObject paramsCia = body.getJSONObject(Constants.CIA_PARAMS);
                        String startHour = paramsCia.getString(Constants.START_HOUR)+":00";
                        String endtHour = paramsCia.getString(Constants.END_HOUR)+":00";
                        String laboralDays = paramsCia.getString(Constants.LABORAL_DAYS);
                        double thershold = Double.parseDouble(paramsCia.getString(Constants.THERSHOLD));

                        Log.e("startHour",startHour);
                        Log.e("endtHour",endtHour);

                        boolean envio_mood_jornada = body.getBoolean(Constants.WORKDAY_SENDED);
                        //TODO Handle the teams
                        JSONArray teams = body.getJSONArray(Constants.TEAMS);
                        if(teams.length()>0) {
                            daoSession.getTeamDao().deleteAll();
                        }
                        for (int i =0; i < teams.length(); i++){
                            JSONObject jsonObjectTeam = teams.getJSONObject(i);
                            Log.e("TEAM",jsonObjectTeam.toString());
                            String idequipo = jsonObjectTeam.getString(Constants.ID_TEAM);
                            String name = jsonObjectTeam.getString(Constants.NAME);
                            Team teamy = new Team();
                            teamy.setIdequipo(idequipo);
                            teamy.setName(name);
                            Log.e("TEAM TO INSERT",teamy.toString());
                            boolean alreadyIn = false;
//                            for (int j = 0; j < teamsInBd.size() && alreadyIn == false; j++){
//                                Team teaminBd = teamsInBd.get(j);
//                                if (teaminBd.getIdequipo().equals(team.getIdequipo())){
//                                    //Update the existing team
//                                    teaminBd.setName(team.getName());
//                                    daoSession.getTeamDao().update(teaminBd);
//                                    alreadyIn = true;
//                                }
//                            }
                            if (!alreadyIn){
                                //Insert for the firs time
                                daoSession.getTeamDao().insert(teamy);
                                Log.e("TO INSERT",teamy.toString());
                                Log.d("Teams", String.valueOf(daoSession.getTeamDao().loadAll().size()));
                            }


                            if(jsonObjectTeam.getString(Constants.START_HOUR)!=null && !jsonObjectTeam.getString(Constants.START_HOUR).equals("")) {
                                startHour = jsonObjectTeam.getString(Constants.START_HOUR) + ":00";
                                Log.e("TEAM HS",startHour);
                            }
                            if(jsonObjectTeam.getString(Constants.END_HOUR)!=null && !jsonObjectTeam.getString(Constants.END_HOUR).equals("")) {
                                endtHour = jsonObjectTeam.getString(Constants.END_HOUR) + ":00";
                                Log.e("TEAM HE",endtHour);
                            }
                            if(jsonObjectTeam.getString(Constants.LABORAL_DAYS)!=null && !jsonObjectTeam.getString(Constants.LABORAL_DAYS).equals("")) {
                                laboralDays = jsonObjectTeam.getString(Constants.LABORAL_DAYS);
                                Log.e("TEAM LD",laboralDays);
                            }
                        }

                        JSONArray teamsLeader = body.getJSONArray(Constants.TEAMS_LEADER);
                        //List<Team> teamsInBd = daoSession.getTeamDao().loadAll();
                        if(teamsLeader.length()>0) {
                            daoSession.getTeamDao().deleteAll();
                        }
                        for (int i = 0; i < teamsLeader.length(); i++){
                            JSONObject teamLead = teamsLeader.getJSONObject(i);
                            String idequipo = teamLead.getString(Constants.ID_TEAM);
                            String name = teamLead.getString(Constants.NAME);
                            Team team = new Team();
                            team.setIdequipo(idequipo);
                            team.setName(name);
                            boolean alreadyIn = false;
//                            for (int j = 0; j < teamsInBd.size() && alreadyIn == false; j++){
//                                Team teaminBd = teamsInBd.get(j);
//                                if (teaminBd.getIdequipo().equals(team.getIdequipo())){
//                                    //Update the existing team
//                                    teaminBd.setName(team.getName());
//                                    daoSession.getTeamDao().update(teaminBd);
//                                    alreadyIn = true;
//                                }
//                            }
                            if (!alreadyIn){
                                //Insert for the firs time
                                daoSession.getTeamDao().insert(team);
                            }

                            if(teamLead.getString(Constants.START_HOUR)!=null && !teamLead.getString(Constants.START_HOUR).equals("")) {
                                startHour = teamLead.getString(Constants.START_HOUR) + ":00";
                            }
                            if(teamLead.getString(Constants.END_HOUR)!=null && !teamLead.getString(Constants.END_HOUR).equals("")) {
                                endtHour = teamLead.getString(Constants.END_HOUR) + ":00";
                            }
                            if(teamLead.getString(Constants.LABORAL_DAYS)!=null && !teamLead.getString(Constants.LABORAL_DAYS).equals("")) {
                                laboralDays = teamLead.getString(Constants.LABORAL_DAYS);
                            }

                        }

                        List<Parameters> parameters = daoSession.getParametersDao().loadAll();
                        if (parameters.isEmpty()) {
                            //Create the params for first time
                            Parameters params = new Parameters();
                            params.setStart_hour(startHour);
                            params.setEnd_hour(endtHour);
                            params.setLaboral_days(laboralDays);
                            params.setThreshold(thershold);
                            daoSession.getParametersDao().insert(params);
                        } else {
                            //Update the params
                            Parameters params = parameters.get(0);
                            params.setStart_hour(startHour);
                            params.setEnd_hour(endtHour);
                            params.setLaboral_days(laboralDays);
                            params.setThreshold(thershold);
                            daoSession.getParametersDao().update(params);
                        }

                        //Verify if is collaborator
                        if (userSession.getRol_id().equals(Constants.ROL_COLLABORATOR)) {
                            Calendar calendar = Calendar.getInstance();
                            Calendar calendar2 = Calendar.getInstance();
                            calendar.setTime(Constants.hourOfString(startHour));
                            calendar2.setTime( Constants.hourOfString(endtHour));
                            //Constants.setAlarmForMoods(getApplicationContext(), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
                            //Constants.setAlarmForMoods(getApplicationContext(), 19, 5, (int) System.currentTimeMillis());
                            Constants.setAlarmForMoods(getApplicationContext(), calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    calendar2.get(Calendar.HOUR_OF_DAY),
                                    calendar2.get(Calendar.MINUTE),
                                    (int) System.currentTimeMillis());
                            //Constants.setAlarmForMoods(getApplicationContext(), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), (int) System.currentTimeMillis());
                        }else{
                            //Clear all alarms TODO
                        }

                        if (fromNotificationToSendMood && userSession.getRol_id().equals(Constants.ROL_COLLABORATOR)) { // Verify is not leader
                            //Go to the menu
                            Intent goToMoods = new Intent(LoadingActivity.this, MoodsActivity.class);
                            goToMoods.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(goToMoods);
                            finish();
                        } else {
                            //Go to the menu
                            Intent goToMenu = new Intent(LoadingActivity.this, CollaboratorMenuActivity.class);
                            goToMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(goToMenu);
                            finish();
                        }

                    }else{
                        String errorInJson = jsonObject.getString(Constants.ERROR);
                        //Go to the error activity
                        Intent goToError = new Intent(LoadingActivity.this, ErrorActivity.class);
                        goToError.putExtra(Constants.ERROR_RESPONSE,errorInJson);
                        startActivity(goToError);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(LoadingActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }
            }
        }

    }
}
