package com.ufo.tiago.moods;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.util.Date;

import utils.Constants;
import utils.NetworkHelper;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button btnResetPassword;
    private EditText editUsername;
    private ProgressBar loader;

    private NetworkHelper networkHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnResetPassword = (Button) findViewById(R.id.restart_password_btn);
        editUsername = (EditText) findViewById(R.id.username_edittext);
        loader = (ProgressBar) findViewById(R.id.loader);

        networkHelper = new NetworkHelper(this);

        //Click on reset password
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verify username - Not empty
                String username = editUsername.getText().toString();
                if(username!=null && !username.equals("")){
                    //TODO Send username and verify response
                    new RecoveryPassword(networkHelper.jsonForPasswordRecovery(username,Constants.formatDateToSend(new Date()))).execute();
                }else{
                    editUsername.setError(getString(R.string.needed_field));
                }

            }
        });
    }

    /** --------------------------------------------------------------------------------------------
     * Asynctask: Send the request of recovery
     -------------------------------------------------------------------------------------------- */
    public class RecoveryPassword extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject jsonObject;

        public RecoveryPassword(JSONObject jsonObject) {
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
            return networkHelper.sendPOST(jsonObject,Constants.FORGOT_PASSWORD_URL);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            loader.setVisibility(View.GONE);
            boolean error = (jsonObject==null);

            if (error){
                //Go to the error activity
                Intent goToError = new Intent(ForgotPasswordActivity.this, ErrorActivity.class);
                goToError.putExtra(Constants.ERROR_RESPONSE, "Error en el servidor");
                startActivity(goToError);
            }else {
                Log.e("JSON RESPONSE",jsonObject.toString());
                try {
                    boolean ans = jsonObject.getBoolean(Constants.ANS);

                    if (ans){
                        JSONObject body = jsonObject.getJSONObject(Constants.BODY);
                        String mail = body.getString(Constants.MAIL);
                        //Go to the success activity
                        Intent goToSuccess = new Intent(ForgotPasswordActivity.this,SuccessActivity.class);
                        goToSuccess.putExtra(Constants.SUCCESS_RESET_PASSWORD_STR,getString(R.string.your_new_password));
                        goToSuccess.putExtra(Constants.SUCCESS_RESET_PASSWORD_MAIL_STR,mail);
                        startActivity(goToSuccess);
                    }else{
                        String errorInJson = jsonObject.getString(Constants.ERROR);
                        //Go to the error activity
                        Intent goToError = new Intent(ForgotPasswordActivity.this, ErrorActivity.class);
                        goToError.putExtra(Constants.ERROR_RESPONSE, errorInJson + "\n" + "El nombre de usuario no es valido");
                        startActivity(goToError);
                    }

                }catch (Exception e){
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(ForgotPasswordActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }
            }
        }

    }
}
