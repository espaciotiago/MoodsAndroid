package com.ufo.tiago.moods;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import db_models.DaoSession;
import db_models.Team;
import db_models.UserSession;
import model.Event;
import model.Form;
import model.FormQuestion;
import utils.Constants;
import utils.NetworkHelper;

public class TeamsActivity extends AppCompatActivity {

    private ImageButton btnBack,btnEvents,btnHome;
    private PieChart pieChartDay,pieChartAfternoon;
    private TextView txtDate,txtAverageDay,txtAverageLate,txtTotalPregDay,txtTotalPregLate,txtPercentDay,txtPercentLate;
    private ImageView imgAverageDay,imgAverageLate;
    private ProgressBar loader;
    private Spinner spnTeams;
    private Button btnWeek;
    private Button btnMonth;

    private int[] moodColors;
    private Date currentDate;
    private List<PieEntry> entriesForDay;
    private List<PieEntry> entriesForAfternoon;
    private List<String> teamList;
    private List<String> teamListLabels;
    private NetworkHelper networkHelper;
    private UserSession userSession;
    private DaoSession daoSession;
    private String teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);

        daoSession = ((DaoMoodsApp)getApplication()).getDaoSession();
        networkHelper = new NetworkHelper(this);
        List<UserSession> sessions = daoSession.getUserSessionDao().loadAll();
        userSession = sessions.get(0);

        moodColors = new int[]{
                R.color.mood_happy,
                R.color.mood_normal,
                R.color.mood_dont_care,
                R.color.mood_sad,
                R.color.mood_angry,
        };

        //Get the date as parameter if exists
        Date paramDate = (Date) getIntent().getSerializableExtra(Constants.DAY_OF_WEEK);
        if(paramDate!=null && !paramDate.equals("")){ //There is a parameter
            currentDate = paramDate;
        }else{ //Today
            currentDate = new Date();
        }

        //final SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd");
        entriesForDay = new ArrayList<>();
        entriesForAfternoon = new ArrayList<>();
        teamList = new ArrayList<>();
        teamListLabels = new ArrayList<>();

        //Init the UI
        loader = (ProgressBar) findViewById(R.id.loader);
        btnBack = (ImageButton) findViewById(R.id.back_img);
        pieChartDay = (PieChart) findViewById(R.id.piechart_day);
        pieChartAfternoon = (PieChart) findViewById(R.id.piechart_afternoon);
        txtDate = (TextView) findViewById(R.id.date_txt);
        txtAverageDay = (TextView) findViewById(R.id.average_day_txt);
        txtAverageLate = (TextView) findViewById(R.id.average_late_txt);
        txtTotalPregDay = (TextView) findViewById(R.id.total_preg_day_txt);
        txtTotalPregLate = (TextView) findViewById(R.id.total_preg_late_txt);
        txtPercentDay = (TextView) findViewById(R.id.percent_day_txt);
        txtPercentLate = (TextView) findViewById(R.id.percent_late_txt);
        spnTeams = (Spinner) findViewById(R.id.teams_spinner);
        btnWeek = (Button) findViewById(R.id.week_btn);
        btnMonth = (Button) findViewById(R.id.month_btn);
        imgAverageDay = (ImageView) findViewById(R.id.mood_average_day_img);
        imgAverageLate = (ImageView) findViewById(R.id.mood_average_late_img);
        btnEvents = (ImageButton) findViewById(R.id.btn_bars);
        btnHome = (ImageButton) findViewById(R.id.btn_home);


        txtDate.setText(simpleDate.format(currentDate));

        // Set the spinner like a team selector
        //Set the teams
        List<Team> teams = daoSession.getTeamDao().loadAll();
        for (int i = 0; i < teams.size(); i ++){
            Team t = teams.get(i);
            teamList.add(t.getIdequipo());
            teamListLabels.add(t.getName());
        }
        setSpinnerTeams(teamListLabels);

        //Click on data of the day chart
        pieChartDay.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                pieChartDay.setCenterText(String.valueOf(e.getY()));
            }

            @Override
            public void onNothingSelected() {
                pieChartDay.setCenterText("");
            }
        });

        //Click on data of the afternoon chart
        pieChartAfternoon.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                pieChartAfternoon.setCenterText(String.valueOf(e.getY()));
            }

            @Override
            public void onNothingSelected() {
                pieChartAfternoon.setCenterText("");
            }
        });

        //Click on week
        btnWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToWeek = new Intent(TeamsActivity.this,TeamsWeekActivity.class);
                goToWeek.putExtra(Constants.DAY_OF_WEEK,currentDate);
                startActivity(goToWeek);
                //finish();
            }
        });

        //Cikc on month
        btnMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMonth = new Intent(TeamsActivity.this,TeamsMonthActivity.class);
                goToMonth.putExtra(Constants.DAY_OF_WEEK,currentDate);
                startActivity(goToMonth);
                //finish();
            }
        });

        //Click on back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Select a team
        spnTeams.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the team id
                teamId = teamList.get(position);
                new getStatsForTeam(networkHelper.jsonForGetDayStats(userSession.getId_server(),
                        simpleDate.format(currentDate),teamId))
                        .execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idUsuario = userSession.getId_server();
                String idEquipo = teamId;
                Intent i = new Intent(TeamsActivity.this,EventStatsDayActivity.class);
                i.putExtra("user",idUsuario);
                i.putExtra("team",idEquipo);
                i.putExtra("date",Constants.formatSimpleDate(currentDate));
                startActivity(i);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeamsActivity.this,CollaboratorMenuActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * Set the data for the day pie chart
     * @param entries
     */
    private void setPieChartDayData(List<PieEntry> entries){
        PieDataSet pieDataSet = new PieDataSet(entries,"");
        pieDataSet.setColors(moodColors,this);
        PieData pieData = new PieData(pieDataSet);
        Description description = new Description();
        description.setText("");
        pieChartDay.setDescription(description);
        pieChartDay.setDrawSliceText(false);
        pieChartDay.getLegend().setTextColor(getResources().getColor(R.color.textTitle));
        pieChartDay.getLegend().setFormSize(16);
        pieChartDay.getLegend().setTextSize(8);
        pieChartDay.getLegend().setForm(Legend.LegendForm.CIRCLE);
        pieChartDay.setHoleColor(getResources().getColor(R.color.menu_light));
        pieChartDay.setCenterTextColor(getResources().getColor(R.color.textTitle));
        pieChartDay.setData(pieData);
        pieChartDay.invalidate();
    }

    /**
     * Set the data for the afternoon pie chart
     * @param entries
     */
    private void setPieChartAfternoonData(List<PieEntry> entries){
        PieDataSet pieDataSet = new PieDataSet(entries,"");
        pieDataSet.setColors(moodColors,this);
        PieData pieData = new PieData(pieDataSet);
        Description description = new Description();
        description.setText("");
        pieChartAfternoon.setDescription(description);
        pieChartAfternoon.setDrawSliceText(false);
        pieChartAfternoon.getLegend().setTextColor(getResources().getColor(R.color.white));
        pieChartAfternoon.getLegend().setFormSize(16);
        pieChartAfternoon.getLegend().setTextSize(8);
        pieChartAfternoon.getLegend().setForm(Legend.LegendForm.CIRCLE);
        pieChartAfternoon.setHoleColor(getResources().getColor(R.color.menu_dark));
        pieChartAfternoon.setCenterTextColor(getResources().getColor(R.color.white));
        pieChartAfternoon.setData(pieData);
        pieChartAfternoon.invalidate();
    }

    /**
     * Set the adapter for the spinner
     * @param teams
     */
    private void setSpinnerTeams(List<String> teams){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TeamsActivity.this, R.layout.spinner_item, teams);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        spnTeams.setAdapter(spinnerArrayAdapter);
    }

    /** --------------------------------------------------------------------------------------------
     * Asynctask: Bring the stats of the day from the server
     -------------------------------------------------------------------------------------------- */
    public class getStatsForTeam extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject jsonObject;

        public getStatsForTeam(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.bringToFront();
            loader.setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            //TODO do the petition
            Log.e("JSON TO SEND",jsonObject.toString());
            return networkHelper.sendPOST(jsonObject,Constants.GET_DAILY_STATS_URL);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            loader.setVisibility(View.GONE);
            // Get the response  - Handle errors
            boolean error = (jsonObject==null);

            if (error){
                //Go to the error activity
                Intent goToError = new Intent(TeamsActivity.this, ErrorActivity.class);
                goToError.putExtra(Constants.ERROR_RESPONSE, "Error en el servidor");
                startActivity(goToError);
            }else {
                Log.e("JSON RESPONE",jsonObject.toString());
                entriesForDay = new ArrayList<>();
                entriesForAfternoon = new ArrayList<>();


                try {
                    boolean ans = jsonObject.getBoolean(Constants.ANS);

                    if (ans){
                        JSONObject body = jsonObject.getJSONObject(Constants.BODY);
                        Log.e("BODY",body.toString());
                        JSONObject jornadaDia = body.getJSONObject(Constants.WORKINGDAY_DAY);
                        JSONObject jornadaTarde = body.getJSONObject(Constants.WORKINGDAY_LATE);
                        //Set the charts
                        String promedioDia = jornadaDia.getString(Constants.AVERAGE);
                        String promedioTarde = jornadaTarde.getString(Constants.AVERAGE);
                        String totalRespuestasEnviadasDia = jornadaDia.getString(Constants.SENDED_ANSWERS);
                        String totalRespuestasEsperadasDia = jornadaDia.getString(Constants.ESPECTED_ANSWERS);
                        String totalRespuestasEnviadasTarde = jornadaTarde.getString(Constants.SENDED_ANSWERS);
                        String totalRespuestasEsperadasTarde = jornadaTarde.getString(Constants.ESPECTED_ANSWERS);
                        JSONArray moodsDia = jornadaDia.getJSONArray(Constants.MOODS);
                        JSONArray moodsaTrde = jornadaTarde.getJSONArray(Constants.MOODS);

                        for (int i = 0; i < moodsDia.length(); i++){
                            JSONObject mood = moodsDia.getJSONObject(i);
                            Log.e("MOOD DIA",mood.toString());
                            double valor = Double.parseDouble(mood.getString(Constants.VALUE));
                            String label = mood.getString(Constants.LABEL);
                            String idmood = mood.getString(Constants.MOOD_ID);
                            int cantidad = Integer.parseInt(mood.getString(Constants.QUANTITY));
                            PieEntry entry = new PieEntry((float) cantidad,label);
                            entriesForDay.add(entry);
                        }

                        for (int i = 0; i < moodsaTrde.length(); i++){
                            JSONObject mood = moodsaTrde.getJSONObject(i);
                            Log.e("MOOD TARDE",mood.toString());
                            double valor = Double.parseDouble(mood.getString(Constants.VALUE));
                            String label = mood.getString(Constants.LABEL);
                            String idmood = mood.getString(Constants.MOOD_ID);
                            int cantidad = Integer.parseInt(mood.getString(Constants.QUANTITY));
                            PieEntry entry = new PieEntry((float) cantidad,label);
                            entriesForAfternoon.add(entry);
                        }

                        PieEntry regDay = entriesForDay.get(2);
                        PieEntry malDay = entriesForDay.get(2);
                        PieEntry indiDay = entriesForDay.get(4);
                        entriesForDay.set(2,indiDay);
                        entriesForDay.set(3,regDay);
                        entriesForDay.set(4,malDay);

                        PieEntry regLate = entriesForAfternoon.get(2);
                        PieEntry malLate = entriesForAfternoon.get(3);
                        PieEntry indLate = entriesForAfternoon.get(4);
                        entriesForAfternoon.set(2,indLate);
                        entriesForAfternoon.set(3,regLate);
                        entriesForAfternoon.set(4,malLate);

                        setPieChartDayData(entriesForDay);
                        setPieChartAfternoonData(entriesForAfternoon);

                        //Set the other info
                        if (!promedioDia.equals("null") && !promedioDia.equals("")){
                            //PARSE INT . VERIFY
                            double promedioDiaDouble = Double.parseDouble(promedioDia);
                            DecimalFormat df = new DecimalFormat("####0.00");
                            promedioDia = df.format(promedioDiaDouble);
                            imgAverageDay.setImageResource(Constants.getResourceOfAverage(promedioDiaDouble));
                        }else{
                            promedioDia = "";
                        }
                        if (!promedioTarde.equals("null") && !promedioTarde.equals("")){
                            //PARSE INT . VERIFY
                            double promedioTardeDouble = Double.parseDouble(promedioTarde);
                            DecimalFormat df = new DecimalFormat("####0.00");
                            promedioTarde = df.format(promedioTardeDouble);
                            imgAverageLate.setImageResource(Constants.getResourceOfAverage(promedioTardeDouble));
                        }else{
                            promedioTarde = "";
                        }

                        txtAverageDay.setText(promedioDia);
                        txtAverageLate.setText(promedioTarde);
                        txtTotalPregDay.setText(totalRespuestasEnviadasDia);
                        txtTotalPregLate.setText(totalRespuestasEnviadasTarde);

                    }else{
                        String errorInJson = jsonObject.getString(Constants.ERROR);
                        //Go to the error activity
                        Intent goToError = new Intent(TeamsActivity.this, ErrorActivity.class);
                        goToError.putExtra(Constants.ERROR_RESPONSE,errorInJson);
                        startActivity(goToError);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(TeamsActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }
            }
        }

    }
}
