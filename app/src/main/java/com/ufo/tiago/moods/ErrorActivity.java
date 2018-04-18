package com.ufo.tiago.moods;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import utils.Constants;

public class ErrorActivity extends AppCompatActivity {

    private TextView txtMessage;
    private View viewMain;
    private boolean goToMenu,goToLogin;
    private String mail,message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        txtMessage = (TextView) findViewById(R.id.message_textview);
        viewMain = (View) findViewById(R.id.mainview);

        //Get the Extras messages
        Intent intent = getIntent();
        message="";
        mail = "";

        if(intent.getStringExtra(Constants.ERROR_NETWORK)!=null){
            //Coming from network error
            message = intent.getStringExtra(Constants.ERROR_NETWORK);
        }else{
            //Coming from somewhere error
            message = intent.getStringExtra(Constants.ERROR_RESPONSE);
        }

        //Set the parameters
        txtMessage.setText(message);

        //Touch the screen
        viewMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP){
                    finish();
                }
                return true;
            }
        });
    }
}
