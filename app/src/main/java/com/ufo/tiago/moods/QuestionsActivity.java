package com.ufo.tiago.moods;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import db_models.DaoSession;
import db_models.UserSession;
import model.Form;
import model.FormOption;
import model.FormQuestion;
import utils.Constants;
import utils.FormRecyclerAdapter;
import utils.NetworkHelper;
import utils.QuestionsRecyclerAdapter;

public class QuestionsActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private Button btnNext;
    private Button btnPreview;
    private ProgressBar loader;
    private TextView txtFormTitle;
    private RecyclerView recyclerviewQuestions;
    private QuestionsRecyclerAdapter questionsRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText editComments;
    private View viewComments;

    private Form form;
    private List<FormQuestion> questionList;
    private List<FormOption> optionsList;
    private List<FormQuestion> pageQuestionList;
    private int totalPages;
    private int actualPage;
    private NetworkHelper networkHelper;
    private UserSession userSession;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        daoSession = ((DaoMoodsApp)getApplication()).getDaoSession();
        networkHelper = new NetworkHelper(this);

        //Get the info of the database
        List<UserSession> sessions = daoSession.getUserSessionDao().loadAll();
        userSession = sessions.get(0);

        //Get the form selected
        form = (Form) getIntent().getSerializableExtra(Constants.FORM_SELECTED);
        questionList = new ArrayList<>();

        // Init UI
        loader = (ProgressBar) findViewById(R.id.loader);
        btnBack = (ImageButton) findViewById(R.id.back_img);
        btnNext = (Button) findViewById(R.id.next_btn);
        btnPreview = (Button) findViewById(R.id.preview_btn);
        txtFormTitle = (TextView) findViewById(R.id.form_title_txt);
        recyclerviewQuestions = (RecyclerView) findViewById(R.id.questions_recyclerview);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        editComments = (EditText) findViewById(R.id.comment_question_edit);
        viewComments = (View) findViewById(R.id.comments_view);

        //Get the questions of the form TODO
        new getQuestions(networkHelper.jsonForGetQuestions(userSession.getId_server(),Constants.formatDateToSend(new Date()),form.getIdInServer())).execute();


        //Set the params of the UI
        txtFormTitle.setText(form.getName());
        //TODO Get the open question and set it
        editComments.setHint(form.getOpenQuestionLabel());

        // Click on back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Click on next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Is about to send
                if(actualPage == totalPages){
                    String openQuestionResponse = editComments.getText().toString();
                    //set the form
                    form.setQuestions(questionList);
                    form.setObservations(openQuestionResponse);

                    if(form.isOpenQuestionNeeded() && (form.getObservations()==null || form.getObservations().equals(""))) {
                        editComments.setError(getString(R.string.needed_field));
                        editComments.requestFocus();
                    }else{
                        //Send the form
                        boolean complete = true;
                        for (int i = 0; i < form.getQuestions().size() && complete;i++){

                            if(form.getQuestions().get(i).getSelectedOption()==null) {
                                complete = false;
                            }
                        }

                        if (complete) {
                            new sendForm(networkHelper.jsonForSendForm(userSession.getId_server(),Constants.formatDateToSend(new Date()),form)).execute();
                        }else {
                            Toast.makeText(getApplicationContext(),getString(R.string.incomplete_form),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    //TODO Save the questions ¿?
                    setQuestionAdapter(paginateList(),optionsList);
                }
            }
        });

        //Click  on preview
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewComments.setVisibility(View.GONE);
                previewPage();
                setQuestionAdapter(paginateList(),optionsList);
            }
        });
    }

    /**
     * Set the events recyclerview adapter with a list of forms
     */
    private void setQuestionAdapter(List<FormQuestion> questionList, List<FormOption> optionList){
        if(questionList!=null) {
            questionsRecyclerAdapter = new QuestionsRecyclerAdapter(questionList, this, this, actualPage,optionList);
            recyclerviewQuestions.setAdapter(questionsRecyclerAdapter);
            recyclerviewQuestions.setLayoutManager(layoutManager);
        }
    }

    /**
     *
     * @return
     */
    private List<FormQuestion> paginateList(){

        List<FormQuestion> pageList=null;
        if(actualPage<totalPages) {
            actualPage++;
            Log.e("ACTUL PAGE",actualPage+"");

            if (actualPage == 1) {
                if(questionList.size() < Constants.FORM_PAGINATION){
                    pageList = questionList;
                }else {
                    pageList = questionList.subList(0, Constants.FORM_PAGINATION * actualPage);
                }
            } else if (actualPage == totalPages) {
                pageList = questionList.subList((Constants.FORM_PAGINATION * actualPage) - Constants.FORM_PAGINATION, questionList.size());
            } else {
                pageList = questionList.subList((Constants.FORM_PAGINATION * actualPage) - Constants.FORM_PAGINATION, Constants.FORM_PAGINATION * actualPage);
            }
        }

        //Is the first page
        if (actualPage==1){
            btnPreview.setEnabled(false);
        }else{
            btnPreview.setEnabled(true);
        }
        //Is the last page
        if (actualPage==totalPages){
            viewComments.setVisibility(View.VISIBLE);
            btnNext.setText(getString(R.string.send));
        }else{
            btnNext.setText(getString(R.string.next_q));
        }

        return pageList;
    }

    private void previewPage(){
        actualPage=actualPage-2;
    }

    /** --------------------------------------------------------------------------------------------
     * Asynctask: Bring the active forms from the server
     -------------------------------------------------------------------------------------------- */
    public class getQuestions extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject jsonObject;

        public getQuestions(JSONObject jsonObject) {
            //TODO
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
            return networkHelper.sendPOST(jsonObject,Constants.GET_QUESTIONS_URL);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            loader.setVisibility(View.GONE);
            //TODO Get the response  - Handle errors
            boolean error = (jsonObject == null);

            if (error){
                //Go to the error activity
                Intent goToError = new Intent(QuestionsActivity.this, ErrorActivity.class);
                goToError.putExtra(Constants.ERROR_RESPONSE, "Error en el servidor");
                startActivity(goToError);
            }else {
                Log.e("JSON RESPONSE",jsonObject.toString());
                try {
                    boolean ans = jsonObject.getBoolean(Constants.ANS);
                    if (ans){
                        JSONObject body = jsonObject.getJSONObject(Constants.BODY);
                        Log.e("BODY",body.toString());
                        JSONArray questions = body.getJSONArray(Constants.QUESTIONS);
                        JSONArray options = body.getJSONArray(Constants.RESPONE_OPTIONS);

                        //Set the questions
                        for (int i = 0; i < questions.length(); i++){
                            JSONObject question = questions.getJSONObject(i);
                            String idencuesta = question.getString(Constants.ID_FORM_QUESTION);
                            String categoria = question.getString(Constants.CATEGORY);
                            String textoPregunta = question.getString(Constants.QUESTION_TEXT);
                            //int consecutive = Integer.parseInt(question.getString(Constants.CONSECUTIVE));
                            FormQuestion formQuestion = new FormQuestion(idencuesta,categoria,textoPregunta);
                            questionList.add(formQuestion);
                        }

                        //Set the options
                        optionsList = new ArrayList<>();
                        for (int i = 0; i < options.length(); i++){
                            JSONObject option = options.getJSONObject(i);
                            String idoption = option.getString(Constants.ID_FORM_OPTION_RESPONSE);
                            String label = option.getString(Constants.LABEL);
                            int value = Integer.parseInt(option.getString(Constants.VALUE));

                            Log.e("OPTION "+i,option.toString());
                            FormOption formOption = new FormOption(idoption,label,value);
                            optionsList.add(formOption);
                        }

                        //Set the forms
                        actualPage = 0;
                        totalPages = (int) questionList.size()/Constants.FORM_PAGINATION;
                        int res = questionList.size() % Constants.FORM_PAGINATION;
                        if(res > 0){
                            totalPages ++;
                        }
                        setQuestionAdapter(paginateList(),optionsList);

                    }else{
                        String errorInJson = jsonObject.getString(Constants.ERROR);
                        //Go to the error activity
                        Intent goToError = new Intent(QuestionsActivity.this, ErrorActivity.class);
                        goToError.putExtra(Constants.ERROR_RESPONSE,errorInJson);
                        startActivity(goToError);
                    }

                }catch (Exception e){
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(QuestionsActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }
            }
        }

    }

    /** --------------------------------------------------------------------------------------------
     * Asynctask: Send the form  to the server
     -------------------------------------------------------------------------------------------- */

    public class sendForm extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject jsonObject;

        public sendForm(JSONObject jsonObject) {
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
            return networkHelper.sendPOST(jsonObject,Constants.SEND_FORM_URL);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            loader.setVisibility(View.GONE);
            //TODO Get the response  - Handle errors
            boolean error = (jsonObject == null);

            if (error){
                //Go to the error activity
                Intent goToError = new Intent(QuestionsActivity.this, ErrorActivity.class);
                goToError.putExtra(Constants.ERROR_RESPONSE, "Error en el servidor");
                startActivity(goToError);
            }else {
                Log.e("JSON Response",jsonObject.toString());
                try {
                    boolean ans = jsonObject.getBoolean(Constants.ANS);
                    if (ans){
                        JSONArray body = jsonObject.getJSONArray(Constants.BODY);
                        Log.e("BODY",body.toString());

                        String[] quotesArray = getResources().getStringArray(R.array.quotes);
                        int quotesLenght = quotesArray.length;
                        Random r = new Random();
                        int index = r.nextInt(quotesLenght);
                        //Go to the success activity
                        Intent goToSuccess = new Intent(QuestionsActivity.this, SuccessActivity.class);
                        goToSuccess.putExtra(Constants.SUCCESS_SEND_STR, quotesArray[index]);
                        goToSuccess.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goToSuccess);
                        finish();

                    }else{
                        String errorInJson = jsonObject.getString(Constants.ERROR);
                        //Go to the error activity
                        Intent goToError = new Intent(QuestionsActivity.this, ErrorActivity.class);
                        goToError.putExtra(Constants.ERROR_RESPONSE,errorInJson);
                        startActivity(goToError);
                    }

                }catch (Exception e){
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(QuestionsActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }
            }
        }

    }
}
