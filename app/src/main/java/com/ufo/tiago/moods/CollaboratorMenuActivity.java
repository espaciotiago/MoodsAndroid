package com.ufo.tiago.moods;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import db_models.DaoSession;
import db_models.Parameters;
import db_models.UserSession;
import utils.Constants;
import utils.NetworkHelper;

public class CollaboratorMenuActivity extends AppCompatActivity {

    private TextView txtQuote,txtFirsItem;
    private ImageView imgBackground,imgFirstItem;
    private View viewMain,viewMoods,viewCampaigns,viewForms,viewTimeline;
    private TextView btnClose;

    public Calendar currentDate;
    private DaoSession daoSession;
    private UserSession userSession;
    private ProgressBar loader;
    private NetworkHelper networkHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborator_menu);

        daoSession = ((DaoMoodsApp)getApplication()).getDaoSession();
        //Get the info of the database
        List<UserSession> sessions = daoSession.getUserSessionDao().loadAll();
        userSession = sessions.get(0);

        //Initialize the UI elemants
        txtQuote = (TextView) findViewById(R.id.quote_textview);
        txtFirsItem = (TextView) findViewById(R.id.first_item_textview);
        imgBackground = (ImageView) findViewById(R.id.background_img);
        imgFirstItem = (ImageView) findViewById(R.id.frist_item_img);
        viewMain = (View) findViewById(R.id.mainview);
        viewMoods = (View) findViewById(R.id.moods_layout);
        viewCampaigns = (View) findViewById(R.id.campaigns_layout);
        viewForms = (View) findViewById(R.id.forms_layout);
        viewTimeline = (View) findViewById(R.id.tendency_layout);
        btnClose = (TextView) findViewById(R.id.close_session_btn);
        loader = (ProgressBar) findViewById(R.id.loader);

        //Initialize the logic elemnts
        currentDate = Calendar.getInstance();
        int currentHour = currentDate.get(Calendar.HOUR_OF_DAY);

        //Set the quote text with a random phrase
        String[] quotesArray = getResources().getStringArray(R.array.quotes);
        int quotesLenght = quotesArray.length;
        Random r = new Random();
        int index = r.nextInt(quotesLenght);
        txtQuote.setText(quotesArray[index]);

        // Get the configuration params to set the skin
        Parameters parameters = daoSession.getParametersDao().loadAll().get(0);
        Date endHourDate = Constants.hourOfString(parameters.getEnd_hour());
        Date startHourDate = Constants.hourOfString(parameters.getStart_hour());
        Calendar calEnd = Calendar.getInstance();
        Calendar calStart = Calendar.getInstance();
        calEnd.setTime(endHourDate);
        calStart.setTime(startHourDate);
        int late = calEnd.get(Calendar.HOUR_OF_DAY);

        networkHelper = new NetworkHelper(this);

        //Set the background, acording to the time
        if(currentHour>=late){
            viewMain.setBackgroundColor(getResources().getColor(R.color.menu_dark));
            imgBackground.setImageResource(R.drawable.backgorund_dark);
            txtQuote.setTextColor(getResources().getColor(R.color.white));
            btnClose.setTextColor(getResources().getColor(R.color.white));
        }
        //Set the menu acording to the user's rol
        boolean isLeader = userSession.getRol_id().equals(Constants.ROL_LEADER);

        if(isLeader){
            //Show teams in menu
            imgFirstItem.setImageResource(R.drawable.teams_icon);
            txtFirsItem.setText(getString(R.string.teams));
            //Set the click listener for the teams option

            viewMoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToTeams = new Intent(CollaboratorMenuActivity.this,TeamsActivity.class);
                    startActivity(goToTeams);
                }
            });
            viewTimeline.setVisibility(View.VISIBLE);

            viewTimeline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToTimeline = new Intent(CollaboratorMenuActivity.this,TimelineActivity.class);
                    startActivity(goToTimeline);
                }
            });
        }else{
            //Click on moods option
            viewMoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToMoods = new Intent(CollaboratorMenuActivity.this, MoodsActivity.class);
                    startActivity(goToMoods);
                }
            });

            viewTimeline.setVisibility(View.GONE);
        }


        //Click on campaigns
        viewCampaigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToCampaigns = new Intent(CollaboratorMenuActivity.this,CampaignActivity.class);
                startActivity(goToCampaigns);
            }
        });

        //Click on Forms
        viewForms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToForms = new Intent(CollaboratorMenuActivity.this,FormsActivity.class);
                startActivity(goToForms);
            }
        });

        //Click on close session
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CloseSessions(networkHelper.jsonForCloseSession(userSession.getId_server())).execute();
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(new Date());
//                Constants.setAlarmForMoods(getApplicationContext(),calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE)+1,(int) System.currentTimeMillis());
//                Constants.setAlarmForMoods(getApplicationContext(),calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE)+3,(int) System.currentTimeMillis());
            }
        });
    }

    /** --------------------------------------------------------------------------------------------
     * Asynctask: Bring the params of configuration and more, from the server
     -------------------------------------------------------------------------------------------- */
    public class CloseSessions extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject jsonObject;

        public CloseSessions(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            //Log.e("JSON DECRY",decrypted);
            return networkHelper.sendPOST(jsonObject,"http://apimoods.nicepeopleconsulting.com/v1/api/delete_id_dispositivo/");
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            loader.setVisibility(View.GONE);

            boolean error = (jsonObject == null);
            if(error){
                //Show error - Network error
                Intent goToError = new Intent(CollaboratorMenuActivity.this, ErrorActivity.class);
                goToError.putExtra(Constants.ERROR_NETWORK,getString(R.string.network_error));
                startActivity(goToError);
            }else{
                //Continue to loading
                //Save the user session
                Log.e("JSON",jsonObject.toString());
                try {
                    boolean ans = jsonObject.getBoolean(Constants.ANS);

                    if (ans){
                        ((DaoMoodsApp)getApplication()).getDaoSession().getUserSessionDao().deleteAll();
                        Intent goToLoading = new Intent(CollaboratorMenuActivity.this,LoadingActivity.class);
                        startActivity(goToLoading);
                        finish();
                    }else{
                        String errorInJson = jsonObject.getString(Constants.ERROR);
                        //Go to the error activity
                        Intent goToError = new Intent(CollaboratorMenuActivity.this, ErrorActivity.class);
                        goToError.putExtra(Constants.ERROR_RESPONSE,errorInJson + "\n" + "Error de autenticación: Nombre de usuario o contraseña incorrectos");
                        startActivity(goToError);
                    }

                }catch (Exception e){
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(CollaboratorMenuActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }
            }
        }

    }
}

