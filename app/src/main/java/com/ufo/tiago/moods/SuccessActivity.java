package com.ufo.tiago.moods;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import utils.Constants;

public class SuccessActivity extends AppCompatActivity {

    private TextView txtMessage,txtMail;
    private View viewMain;
    private boolean goToMenu;
    private String mail,message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        txtMessage = (TextView) findViewById(R.id.message_textview);
        txtMail = (TextView) findViewById(R.id.mail_textview);
        viewMain = (View) findViewById(R.id.mainview);

        //Get the Extras messages
        Intent intent = getIntent();
        message="";
        mail = "";
        goToMenu = false;
        if(intent.getStringExtra(Constants.SUCCESS_RESET_PASSWORD_STR)!=null){

            //Coming from reset password
            message = intent.getStringExtra(Constants.SUCCESS_RESET_PASSWORD_STR);
            mail = intent.getStringExtra(Constants.SUCCESS_RESET_PASSWORD_MAIL_STR);
        }else{
            //Coming from something well sended
            message = intent.getStringExtra(Constants.SUCCESS_SEND_STR);
            goToMenu = true;
        }

        //Set the parameters
        txtMessage.setText(message);
        txtMail.setText(mail);

        //Touch the screen
        viewMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP){
                    if (goToMenu){
                        //TODO Go to the menu
                        Intent goToBack = new Intent(SuccessActivity.this,CollaboratorMenuActivity.class);
                        goToBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goToBack);
                    }else{
                        Intent goToBack = new Intent(SuccessActivity.this,LoginActivity.class);
                        goToBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(goToBack);
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed(); Do nothing
    }
}
