package com.ufo.tiago.moods;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import db_models.DaoSession;
import db_models.Team;
import db_models.UserSession;
import utils.Constants;
import utils.NetworkHelper;

public class TeamsWeekActivity extends AppCompatActivity {

    private ImageButton btnBack,btnHome;
    private TextView txtDate,txtAverage,txtDesviacion,txtRespuestas;
    private ImageView imgAverage;
    private ProgressBar loader;
    private Spinner spnTeams;
    private Button btnL,btnM,btnMi,btnJ,btnV,btnS,btnD;
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
        setContentView(R.layout.activity_teams_week);

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
        txtAverage = (TextView) findViewById(R.id.average_txt);
        txtDesviacion = (TextView) findViewById(R.id.desviacion_txt);
        txtRespuestas = (TextView) findViewById(R.id.respuestas_txt);
        spnTeams = (Spinner) findViewById(R.id.teams_spinner);
        lineChartWek = (LineChart) findViewById(R.id.linewchart_week);
        btnL = (Button) findViewById(R.id.btn_l);
        btnM = (Button) findViewById(R.id.btn_m);
        btnMi = (Button) findViewById(R.id.btn_mi);
        btnJ = (Button) findViewById(R.id.btn_j);
        btnV = (Button) findViewById(R.id.btn_v);
        btnS = (Button) findViewById(R.id.btn_s);
        btnD = (Button) findViewById(R.id.btn_d);
        imgAverage = (ImageView) findViewById(R.id.mood_average_img);
        btnHome = (ImageButton) findViewById(R.id.btn_home);

        txtDate.setText(getString(R.string.week)+" "+ wk +" "+ getString(R.string.of) + " " + month_date.format(cal.getTime()));

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
                            btnL.setVisibility(View.VISIBLE);
                            list.add("L");
                        }
                        break;
                    case Calendar.TUESDAY:
                        if(isDayInParams(day)) {
                            btnM.setVisibility(View.VISIBLE);
                            list.add("M");
                        }
                        break;
                    case Calendar.WEDNESDAY:
                        if(isDayInParams(day)) {
                            btnMi.setVisibility(View.VISIBLE);
                            list.add("X");
                        }
                        break;
                    case Calendar.THURSDAY:
                        if(isDayInParams(day)) {
                            Log.e("AFTER 2",calendar.get(Calendar.DATE)+"");
                            btnJ.setVisibility(View.VISIBLE);
                            list.add("J");
                        }
                        break;
                    case Calendar.FRIDAY:
                        if(isDayInParams(day)) {
                            btnV.setVisibility(View.VISIBLE);
                            list.add("V");
                        }
                        break;
                    case Calendar.SATURDAY:
                        if(isDayInParams(day)) {
                            btnS.setVisibility(View.VISIBLE);
                            list.add("S");
                        }
                        break;
                    case Calendar.SUNDAY:
                        if(isDayInParams(day)) {
                            btnD.setVisibility(View.VISIBLE);
                            list.add("D");
                        }
                        break;
                }
            }
        }

        xDays = new String[list.size()];
        xDays = list.toArray(xDays);
        Log.e("LISTA",list.size()+"");
        //Format the X axis of chart
        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xDays[(int) value];
            }
        };

        XAxis xAxis = lineChartWek.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        //Click on back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Click on days of week
        //Click on L (monday)
        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the date of the day
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cal.getTime());
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String outputDate = simpleDateFormat.format(calendar.getTime());
                Log.e("Lunes",outputDate);

                Intent i = new Intent(TeamsWeekActivity.this,TeamsActivity.class);
                i.putExtra(Constants.DAY_OF_WEEK,calendar.getTime());
                startActivity(i);
                //finish();
            }
        });

        //Click on M (Tuesday)
        btnM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the date of the day
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cal.getTime());
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String outputDate = simpleDateFormat.format(calendar.getTime());
                Log.e("Martes",outputDate);

                Intent i = new Intent(TeamsWeekActivity.this,TeamsActivity.class);
                i.putExtra(Constants.DAY_OF_WEEK,calendar.getTime());
                startActivity(i);
                //finish();
            }
        });

        //Click on MI (wednesday)
        btnMi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the date of the day
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cal.getTime());
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String outputDate = simpleDateFormat.format(calendar.getTime());
                Log.e("Miercoles",outputDate);

                Intent i = new Intent(TeamsWeekActivity.this,TeamsActivity.class);
                i.putExtra(Constants.DAY_OF_WEEK,calendar.getTime());
                startActivity(i);
                //finish();
            }
        });

        //Click on J (Thursday)
        btnJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the date of the day
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cal.getTime());
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String outputDate = simpleDateFormat.format(calendar.getTime());
                Log.e("jueves",outputDate);

                Intent i = new Intent(TeamsWeekActivity.this,TeamsActivity.class);
                i.putExtra(Constants.DAY_OF_WEEK,calendar.getTime());
                startActivity(i);
                //finish();
            }
        });

        //Click on V (Friday)
        btnV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the date of the day
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cal.getTime());
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String outputDate = simpleDateFormat.format(calendar.getTime());
                Log.e("viernes",outputDate);

                Intent i = new Intent(TeamsWeekActivity.this,TeamsActivity.class);
                i.putExtra(Constants.DAY_OF_WEEK,calendar.getTime());
                startActivity(i);
                //finish();
            }
        });

        //Click on S (Saturday)
        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the date of the day
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cal.getTime());
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String outputDate = simpleDateFormat.format(calendar.getTime());
                Log.e("Sabado",outputDate);

                Intent i = new Intent(TeamsWeekActivity.this,TeamsActivity.class);
                i.putExtra(Constants.DAY_OF_WEEK,calendar.getTime());
                startActivity(i);
                //finish();
            }
        });

        //Click on D (Sunday)
        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the date of the day
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cal.getTime());
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String outputDate = simpleDateFormat.format(calendar.getTime());
                Log.e("Domingo",outputDate);

                Intent i = new Intent(TeamsWeekActivity.this,TeamsActivity.class);
                i.putExtra(Constants.DAY_OF_WEEK,calendar.getTime());
                startActivity(i);
                //finish();
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
                        teamId,Constants.getDatesFromWeek(cal.getTime(),daoSession))).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeamsWeekActivity.this,CollaboratorMenuActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
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
        lineChartWek.getAxisLeft().setAxisMaxValue(new Float(4.5));
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
     * Set the adapter for the spinner
     * @param teams
     */
    private void setSpinnerTeams(List<String> teams){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TeamsWeekActivity.this, R.layout.spinner_item, teams);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        spnTeams.setAdapter(spinnerArrayAdapter);
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
            // do the petition
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
                Intent goToError = new Intent(TeamsWeekActivity.this, ErrorActivity.class);
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
                        int totalEsperadas = body.getInt("total_respuestas_esperadas");
                        int totalEnviadas = body.getInt("total_respuestas_enviadas");
                        double desviacionPromedio = body.getDouble("desviacion_promedio");
                        double totalPromedio = body.getDouble("promedio");
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

                        imgAverage.setImageResource(Constants.getResourceOfAverage(totalPromedio));
                        DecimalFormat df = new DecimalFormat("####0.00");
                        txtAverage.setText(df.format(totalPromedio));
                        txtRespuestas.setText(df.format(totalEnviadas));
                        txtDesviacion.setText(df.format(desviacionPromedio));

                    }else{
                        String errorInJson = jsonObject.getString(Constants.ERROR);
                        //Go to the error activity
                        Intent goToError = new Intent(TeamsWeekActivity.this, ErrorActivity.class);
                        goToError.putExtra(Constants.ERROR_RESPONSE,errorInJson);
                        startActivity(goToError);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(TeamsWeekActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }
            }
        }

    }
}
