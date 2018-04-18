package com.ufo.tiago.moods;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import db_models.DaoSession;
import db_models.Team;
import db_models.UserSession;
import utils.Constants;
import utils.NetworkHelper;

public class TimelineActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private TextView txtDate;
    private ProgressBar loader;
    private Spinner spnTeams;
    private LineChart lineChartWek;

    private Date currentDate;
    private Calendar cal;
    private List<String> teamList;
    private List<String> teamListLabels;
    private String[] xDays;
    private List<Entry> entries;
    private NetworkHelper networkHelper;
    private UserSession userSession;
    private DaoSession daoSession;
    private String teamId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        daoSession = ((DaoMoodsApp)getApplication()).getDaoSession();
        networkHelper = new NetworkHelper(this);
        List<UserSession> sessions = daoSession.getUserSessionDao().loadAll();
        userSession = sessions.get(0);

        //Get the date as parameter if exists
        Date paramDate = (Date) getIntent().getSerializableExtra(Constants.DAY_OF_WEEK);
        if(paramDate!=null && !paramDate.equals("")){ //There is a parameter
            currentDate = paramDate;
        }else{ //Today
            currentDate = new Date();
        }

        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.getDefault());
        teamList = new ArrayList<>();
        teamListLabels = new ArrayList<>();
        cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.setMinimalDaysInFirstWeek(1);
        int wk = cal.get(Calendar.WEEK_OF_MONTH);

        //Init the UI
        loader = (ProgressBar) findViewById(R.id.loader);
        btnBack = (ImageButton) findViewById(R.id.back_img);
        txtDate = (TextView) findViewById(R.id.date_txt);
        spnTeams = (Spinner) findViewById(R.id.teams_spinner);
        lineChartWek = (LineChart) findViewById(R.id.linewchart_week);

        //Set the teams
        List<Team> teams = daoSession.getTeamDao().loadAll();
        for (int i = 0; i < teams.size(); i ++){
            Team t = teams.get(i);
            teamList.add(t.getIdequipo());
            teamListLabels.add(t.getName());
        }
        setSpinnerTeams(teamListLabels);

        //Set the bottom bar buttons, depending of the current week
        int currentMonth = cal.get(Calendar.MONTH);
        ArrayList<String> list = new ArrayList();
        for (int i = 0; i < 7; i++){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(cal.getTime());
            calendar.set(Calendar.DAY_OF_WEEK, i);
            if(calendar.get(Calendar.MONTH)==currentMonth) {
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                switch (day){
                    case Calendar.MONDAY:
                        if(isDayInParams(day)) {
                            list.add("L");
                        }
                        break;
                    case Calendar.TUESDAY:
                        if(isDayInParams(day)) {
                            list.add("M");
                        }
                        break;
                    case Calendar.WEDNESDAY:
                        if(isDayInParams(day)) {
                            list.add("Mi");
                        }
                        break;
                    case Calendar.THURSDAY:
                        if(isDayInParams(day)) {
                            list.add("J");
                        }
                        break;
                    case Calendar.FRIDAY:
                        if(isDayInParams(day)) {
                            list.add("V");
                        }
                        break;
                    case Calendar.SATURDAY:
                        if(isDayInParams(day)) {
                            list.add("S");
                        }
                        break;
                    case Calendar.SUNDAY:
                        if(isDayInParams(day)) {
                            list.add("D");
                        }
                        break;
                }
            }
        }

//        xDays = new String[list.size()];
//        xDays = list.toArray(xDays);
//        Log.e("LISTA",list.size()+"");
//        //Format the X axis of chart
//        IAxisValueFormatter formatter = new IAxisValueFormatter() {
//
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return xDays[(int) value];
//            }
//        };
//
//        XAxis xAxis = lineChartWek.getXAxis();
//        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
//        xAxis.setValueFormatter(formatter);

        //Click on back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Set the data of chart
        entries = new ArrayList<Entry>();

        spnTeams.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the team id
                teamId = teamList.get(position);
                new getStatsForTeam(networkHelper.jsonForGetWeekStats(userSession.getId_server(),
                        Constants.formatDateToSend(cal.getTime()),
                        teamId,Constants.getDatesFromMonth(cal.getTime()))).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Set the adapter for the spinner
     * @param teams
     */
    private void setSpinnerTeams(List<String> teams){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TimelineActivity.this, R.layout.spinner_item, teams);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        spnTeams.setAdapter(spinnerArrayAdapter);
    }

    /**
     * Set the charts of a week
     * @param entries
     */
    private void setWeekChart(List<Entry> entries){
        LineDataSet dataSet = new LineDataSet(entries, "Moods");
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setColor(getResources().getColor(R.color.colorPrimary));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorAccent));
        LineData lineData = new LineData(dataSet);
        Description description = new Description();
        description.setText("");
        lineChartWek.getAxisLeft().setAxisMaxValue(new Float(5));
        lineChartWek.getAxisLeft().setAxisMinValue(0);
        lineChartWek.getAxisLeft().setDrawGridLines(false);
        lineChartWek.getXAxis().setDrawGridLines(false);
        lineChartWek.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChartWek.getAxisRight().setDrawGridLines(false);
        lineChartWek.getAxisRight().setEnabled(false);
        lineChartWek.setDescription(description);
        lineChartWek.setData(lineData);
        lineChartWek.invalidate();
    }

    /**
     * True if a given day of week is in the String of days in the parameters
     * @param dayOfWeek
     * @return
     */
    private boolean isDayInParams(int dayOfWeek){

        boolean isDay = false;
        String[] daysOfParams = daoSession.getParametersDao().loadAll().get(0).getLaboral_days().split("_");
        for (int i =0; i < daysOfParams.length && !isDay; i++){
            String day = daysOfParams[i];
            if (dayOfWeek==Calendar.MONDAY && day.equals("L")){
                isDay = true;
            }else if(dayOfWeek==Calendar.TUESDAY && day.equals("Ma")){
                isDay = true;
            }else if(dayOfWeek==Calendar.WEDNESDAY && day.equals("Mi")){
                isDay = true;
            }else if(dayOfWeek==Calendar.THURSDAY && day.equals("J")){
                isDay = true;
            }else if(dayOfWeek==Calendar.FRIDAY && day.equals("V")){
                isDay = true;
            }else if(dayOfWeek==Calendar.SATURDAY && day.equals("S")){
                isDay = true;
            }else if(dayOfWeek==Calendar.SUNDAY && day.equals("D")){
                isDay = true;
            }
        }
        return isDay;
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
            //TODO Get the response  - Handle errors
            boolean error = (jsonObject==null);

            if (error){
                //Go to the error activity
                Intent goToError = new Intent(TimelineActivity.this, ErrorActivity.class);
                goToError.putExtra(Constants.ERROR_RESPONSE, "Error en el servidor");
                startActivity(goToError);
            }else {
                Log.e("JSON RESPONE",jsonObject.toString());
                try {
                    boolean ans = jsonObject.getBoolean(Constants.ANS);

                    if (ans){
                        entries = new ArrayList<Entry>();
                        JSONObject body = jsonObject.getJSONObject(Constants.BODY);
                        Log.e("BODY",body.toString());
                        //int totalEsperadas = body.getInt("total_respuestas_esperadas");
                        //double desviacionPromedio = body.getInt("desviacion_promedio");
                        JSONArray diasArray = body.getJSONArray("dias_semana");

                        Log.e("DIAS",diasArray.length()+"");

                        for (int i = 0; i < diasArray.length();i++){
                            JSONObject dia = (JSONObject) diasArray.get(i);
                            double promedio = dia.getDouble("promedio");
                            Entry entry = new Entry(i,(float)promedio);
                            entries.add(entry);

                            Log.e("DIA",dia.toString());

                        }

                        setWeekChart(entries);

                    }else{
                        String errorInJson = jsonObject.getString(Constants.ERROR);
                        //Go to the error activity
                        Intent goToError = new Intent(TimelineActivity.this, ErrorActivity.class);
                        goToError.putExtra(Constants.ERROR_RESPONSE,errorInJson);
                        startActivity(goToError);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(TimelineActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }
            }
        }

    }
}
