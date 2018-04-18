package com.ufo.tiago.moods;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import db_models.Parameters;
import db_models.UserSession;
import utils.Constants;
import utils.NetworkHelper;
import utils.Security;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText editUsername,editPassword;
    private TextView txtForgotPassword;
    private NetworkHelper networkHelper;
    private ProgressBar loader;
    private Security s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        s = new Security(Constants.PUBLIC_KEY,256);

        btnLogin = (Button) findViewById(R.id.login_btn);
        editUsername = (EditText) findViewById(R.id.username_edittext);
        editPassword = (EditText) findViewById(R.id.password_edittext);
        txtForgotPassword = (TextView) findViewById(R.id.forgot_password_textview);
        loader = (ProgressBar) findViewById(R.id.loader);

        networkHelper = new NetworkHelper(this);

        //Click on login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();

                if(username==null || username.equals("")) {
                    //Set error in username
                    editUsername.setError(getString(R.string.needed_field));
                }
                else if(password==null || password.equals("")){
                    //Set error in password
                    editPassword.setError(getString(R.string.needed_field));
                }else{
                    //Send the data
                    String token = FirebaseInstanceId.getInstance().getToken();
                    Log.e("TOKEN",token);
                    new SendLogin(networkHelper.jsonForSendLogin(username,password,Constants.formatDateToSend(new Date()),token)).execute();
                }
            }
        });

        //Click on forgot password
        txtForgotPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Go to Forgot password activity

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Intent goToForgotPass = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                    startActivity(goToForgotPass);
                }
                return true;
            }
        });
    }

    /** --------------------------------------------------------------------------------------------
     * Asynctask: Bring the params of configuration and more, from the server
     -------------------------------------------------------------------------------------------- */
    public class SendLogin extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject jsonObject;

        public SendLogin(JSONObject jsonObject) {
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
            String encrypted = s.toEncrypt(jsonObject.toString());
            //String decrypted = s.toDecrypt(encrypted);
            Log.e("JSON ENCRIP",encrypted);
            //Log.e("JSON DECRY",decrypted);

            return networkHelper.sendPOST(jsonObject,Constants.LOGIN_URL);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            loader.setVisibility(View.GONE);

            boolean error = (jsonObject == null);
            if(error){
                //Show error - Network error
                Intent goToError = new Intent(LoginActivity.this, ErrorActivity.class);
                goToError.putExtra(Constants.ERROR_NETWORK,getString(R.string.network_error));
                startActivity(goToError);
            }else{
                //Continue to loading
                //Save the user session
                Log.e("JSON",jsonObject.toString());
                try {
                    boolean ans = jsonObject.getBoolean(Constants.ANS);

                    if (ans){
                        JSONObject body = jsonObject.getJSONObject(Constants.BODY);
                        Log.e("BODY",body.toString());
                        String idusuario = body.getString(Constants.USER_ID);
                        String name = body.getString(Constants.NAME);
                        String mail = body.getString(Constants.MAIL);
                        String username = body.getString(Constants.USERNAME);
                        String phone = body.getString(Constants.PHONE);
                        String password = body.getString(Constants.PASSWORD);
                        String position = body.getString(Constants.POSITION);
                        String id_rol = body.getString(Constants.FK_ID_ROL);

                        UserSession userSession = new UserSession();
                        userSession.setName(name);
                        userSession.setId_server(idusuario);
                        userSession.setMail(mail);
                        userSession.setUsername(username);
                        userSession.setPhone(phone);
                        userSession.setRol_id(id_rol);
                        userSession.setPassword(password);
                        ((DaoMoodsApp)getApplication()).getDaoSession().getUserSessionDao().insert(userSession);

                        Intent goToLoading = new Intent(LoginActivity.this,LoadingActivity.class);
                        goToLoading.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goToLoading);

                    }else{
                        String errorInJson = jsonObject.getString(Constants.ERROR);
                        //Go to the error activity
                        Intent goToError = new Intent(LoginActivity.this, ErrorActivity.class);
                        goToError.putExtra(Constants.ERROR_RESPONSE,errorInJson + "\n" + "Error de autenticación: Nombre de usuario o contraseña incorrectos");
                        startActivity(goToError);
                    }

                }catch (Exception e){
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(LoginActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }
            }
        }

    }
}
