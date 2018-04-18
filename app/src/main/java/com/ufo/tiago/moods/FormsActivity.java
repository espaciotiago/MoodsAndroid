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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import db_models.DaoSession;
import db_models.UserSession;
import model.Event;
import model.Form;
import model.FormQuestion;
import utils.Constants;
import utils.EventRecyclerAdapter;
import utils.FormRecyclerAdapter;
import utils.NetworkHelper;

public class FormsActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private RecyclerView recyclerviewForms;
    private FormRecyclerAdapter formRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar loader;

    private List<Form> formList;
    private NetworkHelper networkHelper;
    private UserSession userSession;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);

        daoSession = ((DaoMoodsApp)getApplication()).getDaoSession();
        networkHelper = new NetworkHelper(this);
        List<UserSession> sessions = daoSession.getUserSessionDao().loadAll();
        userSession = sessions.get(0);

        formList = new ArrayList<>();

        // Init UI
        loader = (ProgressBar) findViewById(R.id.loader);
        btnBack = (ImageButton) findViewById(R.id.back_img);
        recyclerviewForms = (RecyclerView) findViewById(R.id.forms_recyclerview);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // Click on back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Get the active forms
        new getForms(networkHelper.jsonForGetForms(userSession.getId_server(),Constants.formatDateToSend(new Date()))).execute();

    }

    /**
     * Set the events recyclerview adapter with a list of forms
     */
    private void setFormAdapter(List<Form> formList){
        formRecyclerAdapter = new FormRecyclerAdapter(formList,this,this);
        recyclerviewForms.setAdapter(formRecyclerAdapter);
        recyclerviewForms.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(FormsActivity.this,CollaboratorMenuActivity.class);
        startActivity(i);
        finish();
    }

    /** --------------------------------------------------------------------------------------------
     * Asynctask: Bring the active forms from the server
     -------------------------------------------------------------------------------------------- */
    public class getForms extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject jsonObject;

        public getForms(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONObject doInBackground(Void... params) {
            Log.e("JSON TO SEND",jsonObject.toString());
            return networkHelper.sendPOST(jsonObject,Constants.GET_FORMS_URL);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            loader.setVisibility(View.GONE);
            boolean error = (jsonObject==null);

            if (error){
                //Go to the error activity
                Intent goToError = new Intent(FormsActivity.this, ErrorActivity.class);
                goToError.putExtra(Constants.ERROR_RESPONSE, "Error en el servidor");
                startActivity(goToError);
            }else {
                Log.e("JSON Response",jsonObject.toString());
                try {
                    boolean ans = jsonObject.getBoolean(Constants.ANS);

                    if (ans){
                        JSONArray body = jsonObject.getJSONArray(Constants.BODY);
                        for (int i = 0; i < body.length(); i++){
                            JSONObject form = body.getJSONObject(i);
                            String idencuesta = form.getString(Constants.FORM_ID);
                            String name = form.getString(Constants.NAME);
                            String closeDate = form.getString(Constants.CLOSE_DATE);
                            String status = form.getString(Constants.STATUS);
                            String labelQuestion = form.getString(Constants.QUESTION_LABEL);
                            int needed = Integer.parseInt(form.getString(Constants.NEEDED_QUESTION));
                            int totalQuestions = Integer.parseInt(form.getString(Constants.TOTAL_QUESTIONS));
                            boolean isNeeded = (needed==1)?true:false;
                            if(labelQuestion==null || labelQuestion.equals("null")) {
                                labelQuestion = "";
                            }
                            Form form1 = new Form(idencuesta,name,closeDate,totalQuestions,
                                    isNeeded,new ArrayList<FormQuestion>(),labelQuestion);

                            if(status.equals(Constants.OPEN)){
                                form1.setSended(false);
                                formList.add(form1);
                            }else{
                                form1.setSended(true);
                            }
                        }

                        new getFormsResponses(networkHelper.jsonForCloseSession(userSession.getId_server())).execute();

                    }else{
                        String errorInJson = jsonObject.getString(Constants.ERROR);
                        //Go to the error activity
                        Intent goToError = new Intent(FormsActivity.this, ErrorActivity.class);
                        goToError.putExtra(Constants.ERROR_RESPONSE,errorInJson);
                        startActivity(goToError);
                    }

                }catch (Exception e){
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(FormsActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }
                //Set the forms
                setFormAdapter(formList);
            }
        }

    }

    /** --------------------------------------------------------------------------------------------
     * Asynctask: Bring the active forms from the server
     -------------------------------------------------------------------------------------------- */
    public class getFormsResponses extends AsyncTask<Void, Void, JSONArray> {

        private JSONObject jsonObject;

        public getFormsResponses(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONArray doInBackground(Void... params) {
            Log.e("JSON TO SEND",jsonObject.toString());
            return networkHelper.sendPOSTArray(jsonObject,"http://apimoods.nicepeopleconsulting.com/v1/api/encuesta/get_mis_respuestas/");
        }

        @Override
        protected void onPostExecute(JSONArray jsonObject) {
            super.onPostExecute(jsonObject);
            loader.setVisibility(View.GONE);
            boolean error = (jsonObject==null);

            if (error){
                //Go to the error activity
                Intent goToError = new Intent(FormsActivity.this, ErrorActivity.class);
                goToError.putExtra(Constants.ERROR_RESPONSE, "Error en el servidor");
                startActivity(goToError);
            }else {
                try {
                    for (int i = 0; i < jsonObject.length(); i++){
                        JSONObject jsonObject1 = jsonObject.getJSONObject(i);
                        String formId = jsonObject1.getString("idencuesta");
                        for (int j = 0; j < formList.size(); j++){
                            String id = formList.get(j).getIdInServer();
                            Log.e("ID forms",id);
                            if(id.equals(formId)){
                                formList.get(j).setAlreadySended(true);
                            }
                        }
                    }
                }catch (Exception e){
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(FormsActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }
                //Set the forms
                setFormAdapter(formList);
            }
        }

    }
}
