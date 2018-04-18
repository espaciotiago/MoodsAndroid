package com.ufo.tiago.moods;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tiago on 31/01/18.
 */

public class FcmMessaginService extends FirebaseMessagingService {

    String type = "";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("REMOTE",remoteMessage.getData().toString());
        if (remoteMessage.getData().size() > 0) {
            type = "json";
            sendNotification(remoteMessage.getData().toString());
        }
        if (remoteMessage.getNotification() !=null) {
            type = "message";
            String goTo = "";
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }
    private void sendNotification(String messageBody){
        String id="",message="",title="";
        String goTo = "";
        String[] arrayMs= new String[2];

        if(type.equals("json")) {
            Log.e("GOTO",messageBody);
            messageBody = messageBody.replace(":","");
            messageBody = messageBody.replace(" ","");
            messageBody = messageBody.replace("=",":");
            messageBody = messageBody.replace("!","");

            try {
                JSONObject jsonObject = new JSONObject(messageBody);
                Log.e("JSON",jsonObject.toString());
                //id = jsonObject.getString("id");
                message = jsonObject.getString("body");
                message = message.replace("."," ");
                title = jsonObject.getString("title");
                goTo = jsonObject.getString("code");

            } catch (JSONException e) {
                //            }
                Log.e("Ex",e.getMessage());
            }
        }
        else if(type.equals("message"))
        {
            message = messageBody;
            //arrayMs = message.split("_");
            //goTo = arrayMs[0];
            //message = arrayMs[1];
        }

        Intent intent;
        PendingIntent pendingIntent;
        if(goTo.equals("1")) {
            //Go to forms
            intent = new Intent(this, FormsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }else if(goTo.equals("2")){
            //Go to Campaigns
            intent = new Intent(this, CampaignActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }else{
            //Go to Loading
            intent = new Intent(this, LoadingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(getString(R.string.app_name));
        notificationBuilder.setContentText(message);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notificationBuilder.setSound(soundUri);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher));
        notificationBuilder.setAutoCancel(true);
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        //notificationBuilder.setVibrate(new long[] { 1000, 1000});
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notificationBuilder.build());
    }
}
