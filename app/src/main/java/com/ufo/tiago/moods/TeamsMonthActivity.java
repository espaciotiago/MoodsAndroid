package com.ufo.tiago.moods;

import android.content.Intent;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import db_models.DaoSession;
import db_models.Team;
import db_models.UserSession;
import utils.Constants;
import utils.NetworkHelper;

public class TeamsMonthActivity extends AppCompatActivity {

    private ImageButton btnBack,btnHome;
    private ImageView imgProm;
    private TextView txtDate,txtProm,txtDesv,txtTotalEnv;
    private ProgressBar loader;
    private Spinner spnTeams;
    private Button btnS1,btnS2,btnS3,btnS4,btnS5,btnS6;
    private LineChart lineChartMonth;

    private Date currentDate;
    private Calendar cal;
    private List<String> teamList;
    private List<String> teamListLabels;
    private String[] xWeeks;
    private List<Entry> entries;
    private NetworkHelper networkHelper;
    private UserSession userSession;
    private DaoSession daoSession;
    private String teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_month);

        daoSession = ((DaoMoodsApp)getApplication()).getDaoSession();
        networkHelper = new NetworkHelper(this);
        List<UserSession> sessions = daoSession.getUserSessionDao().loadAll();
        userSession = sessions.get(0);

        xWeeks = new String[] { "S1","S2","S3","S4","S5","S6" };

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
        int mt = cal.get(Calendar.MONTH);
        int total_weeks = Constants.getTotalWeeksOfMonth(cal.getTime());

        //Init the UI
        loader = (ProgressBar) findViewById(R.id.loader);
        btnBack = (ImageButton) findViewById(R.id.back_img);
        txtDate = (TextView) findViewById(R.id.date_txt);
        spnTeams = (Spinner) findViewById(R.id.teams_spinner);
        lineChartMonth = (LineChart) findViewById(R.id.linewchart_month);
        btnS1 = (Button) findViewById(R.id.btn_s1);
        btnS2 = (Button) findViewById(R.id.btn_s2);
        btnS3 = (Button) findViewById(R.id.btn_s3);
        btnS4 = (Button) findViewById(R.id.btn_s4);
        btnS5 = (Button) findViewById(R.id.btn_s5);
        btnS6 = (Button) findViewById(R.id.btn_s6);
        txtProm = (TextView) findViewById(R.id.txt_prom);
        txtDesv = (TextView) findViewById(R.id.txt_desviacion);
        txtTotalEnv = (TextView) findViewById(R.id.txt_total_resp);
        imgProm = (ImageView) findViewById(R.id.img_prom);
        btnHome = (ImageButton) findViewById(R.id.btn_home);

        txtDate.setText(month_date.format(cal.getTime()));

        //Set visibility of remaining weeks
        if(6<=total_weeks){
            //Are 6 weeks
            btnS6.setVisibility(View.VISIBLE);
        }
        if(5<=total_weeks){
            //Are 5 Weeks
            btnS5.setVisibility(View.VISIBLE);
        } //Else, just 4 weeks

        //Set the teams
        List<Team> teams = daoSession.getTeamDao().loadAll();
        for (int i = 0; i < teams.size(); i ++){
            Team t = teams.get(i);
            teamList.add(t.getIdequipo());
            teamListLabels.add(t.getName());
        }
        setSpinnerTeams(teamListLabels);

        //Set the data of chart
        entries = new ArrayList<Entry>();

        //Format the X axis of chart
        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xWeeks[(int) value];
            }
        };

        XAxis xAxis = lineChartMonth.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        //Selection of a Week
        //Click on S1
        btnS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.WEEK_OF_MONTH,1);
                Intent goToWeek = new Intent(TeamsMonthActivity.this,TeamsWeekActivity.class);
                goToWeek.putExtra(Constants.DAY_OF_WEEK,calendar.getTime());
                startActivity(goToWeek);
                //finish();
            }
        });

        //Click on S2
        btnS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.WEEK_OF_MONTH,2);
                Intent goToWeek = new Intent(TeamsMonthActivity.this,TeamsWeekActivity.class);
                goToWeek.putExtra(Constants.DAY_OF_WEEK,calendar.getTime());
                startActivity(goToWeek);
                //finish();
            }
        });

        //Click on S3
        btnS3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.WEEK_OF_MONTH,3);
                Intent goToWeek = new Intent(TeamsMonthActivity.this,TeamsWeekActivity.class);
                goToWeek.putExtra(Constants.DAY_OF_WEEK,calendar.getTime());
                startActivity(goToWeek);
                //finish();
            }
        });

        //Click on S4
        btnS4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.WEEK_OF_MONTH,4);
                Intent goToWeek = new Intent(TeamsMonthActivity.this,TeamsWeekActivity.class);
                goToWeek.putExtra(Constants.DAY_OF_WEEK,calendar.getTime());
                startActivity(goToWeek);
                //finish();
            }
        });

        //Click on S5
        btnS5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.WEEK_OF_MONTH,5);
                Intent goToWeek = new Intent(TeamsMonthActivity.this,TeamsWeekActivity.class);
                goToWeek.putExtra(Constants.DAY_OF_WEEK,calendar.getTime());
                startActivity(goToWeek);
                //finish();
            }
        });

        //Click on S6
        btnS6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.WEEK_OF_MONTH,6);
                Intent goToWeek = new Intent(TeamsMonthActivity.this,TeamsWeekActivity.class);
                goToWeek.putExtra(Constants.DAY_OF_WEEK,calendar.getTime());
                startActivity(goToWeek);
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

        spnTeams.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the team id
                teamId = teamList.get(position);
                new getStatsForTeam(networkHelper.jsonForGetMonthStats(userSession.getId_server(),Constants.formatDateToSend(cal.getTime()),
                        teamId,Constants.getDatesWeeksFromMonth(cal.getTime(),daoSession)))
                        .execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeamsMonthActivity.this,CollaboratorMenuActivity.class);
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
    private void setMonthChart(List<Entry> entries){
        LineDataSet dataSet = new LineDataSet(entries, "Moods");
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setColor(getResources().getColor(R.color.colorPrimary));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorAccent));
        LineData lineData = new LineData(dataSet);
        Description description = new Description();
        description.setText("");
        lineChartMonth.getAxisLeft().setAxisMaxValue(new Float(4.5));
        lineChartMonth.getAxisLeft().setAxisMinValue(0);
        lineChartMonth.getAxisLeft().setDrawGridLines(false);
        lineChartMonth.getXAxis().setDrawGridLines(false);
        lineChartMonth.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChartMonth.getAxisRight().setDrawGridLines(false);
        lineChartMonth.getAxisRight().setEnabled(false);
        lineChartMonth.setDescription(description);
        lineChartMonth.setData(lineData);
        lineChartMonth.invalidate();
    }

    /**
     * Set the adapter for the spinner
     * @param teams
     */
    private void setSpinnerTeams(List<String> teams){
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TeamsMonthActivity.this, R.layout.spinner_item, teams);
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
                Intent goToError = new Intent(TeamsMonthActivity.this, ErrorActivity.class);
                goToError.putExtra(Constants.ERROR_RESPONSE, "Error en el servidor");
                startActivity(goToError);
            }else {
                JSONObject body = null;
                try {
                    boolean ans = jsonObject.getBoolean(Constants.ANS);
                    if(ans) {
                        body = jsonObject.getJSONObject("body");
                        if (body != null) {
                            Log.e("JSON BODY",body.toString());
                            entries = new ArrayList<>();
                            JSONArray semanas = body.getJSONArray("semanas");
                            String total_respuestas_esperadas = body.getString("total_respuestas_esperadas");
                            String desviacion_promedio = body.getString("desviacion_promedio");
                            double totalRes = 0;
                            double prom = 0;
                            for (int i = 0; i < semanas.length(); i ++){
                                JSONObject semana = semanas.getJSONObject(i);
                                String promedioStr = semana.getString("promedio");
                                String enviadasStr = semana.getString("num_respuestas_enviadas");
                                double promedioDouble = 0;
                                if(promedioStr != null && !promedioStr.equals("")){
                                    promedioDouble = Double.parseDouble(promedioStr);
                                }
                                double totalEnv = 0;
                                if(enviadasStr!=null && !enviadasStr.equals("")){
                                    totalEnv = Double.parseDouble(enviadasStr);
                                    totalRes += totalEnv;
                                }
                                prom+=promedioDouble;
                                Entry entry = new Entry(i,(float)promedioDouble);
                                entries.add(entry);
                            }
                            setMonthChart(entries);
                            //Set the other info
                            prom = prom/semanas.length();
                            DecimalFormat df = new DecimalFormat("####0.00");
                            imgProm.setImageResource(Constants.getResourceOfAverage(prom));
                            txtTotalEnv.setText(totalRes+"");
                            txtProm.setText(df.format(prom));
                            txtDesv.setText(desviacion_promedio);
                        }
                    }else{
                        String errorInJson = jsonObject.getString(Constants.ERROR);
                        //Go to the error activity
                        Intent goToError = new Intent(TeamsMonthActivity.this, ErrorActivity.class);
                        goToError.putExtra(Constants.ERROR_RESPONSE,errorInJson);
                        startActivity(goToError);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(TeamsMonthActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }

            }
        }

    }
}
