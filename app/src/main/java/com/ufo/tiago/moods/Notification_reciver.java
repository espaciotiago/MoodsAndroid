package com.ufo.tiago.moods;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import db_models.DaoSession;
import db_models.Parameters;
import utils.Constants;

/**
 * Created by Tiago on 2/11/17.
 */

public class Notification_reciver extends BroadcastReceiver {

    private DaoSession daoSession;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Entro","A reciver!! " + Constants.formatDateToSend(new Date()));

        daoSession = ((DaoMoodsApp)context.getApplicationContext()).getDaoSession();
        Parameters parameters = daoSession.getParametersDao().loadAll().get(0);
        Date now = new Date();
        Date endHourDate = Constants.hourOfString(parameters.getEnd_hour());
        Date startHourDate = Constants.hourOfString(parameters.getStart_hour());
        Calendar calEnd = Calendar.getInstance();
        Calendar calStart = Calendar.getInstance();
        Calendar calNow = Calendar.getInstance();
        calEnd.setTime(endHourDate);
        calStart.setTime(startHourDate);
        calNow.setTime(now);
        int lateMinutes = calEnd.get(Calendar.MINUTE);
        int lateHour = calEnd.get(Calendar.HOUR_OF_DAY);
        int dayMinutes = calStart.get(Calendar.MINUTE);
        int dayHour = calStart.get(Calendar.HOUR_OF_DAY);
        int nowMinutes = calNow.get(Calendar.MINUTE);
        int nowHour = calNow.get(Calendar.HOUR_OF_DAY);


        if(nowHour == dayHour && nowMinutes==dayMinutes){
            Constants.notificateToSendMood(context);
        }
        else if(nowHour == lateHour && nowMinutes==lateMinutes) {
            Constants.notificateToSendMood(context);
        }
    }
}
