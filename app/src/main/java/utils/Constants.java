package utils;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.ufo.tiago.moods.CollaboratorMenuActivity;
import com.ufo.tiago.moods.DaoMoodsApp;
import com.ufo.tiago.moods.LoadingActivity;
import com.ufo.tiago.moods.Notification_reciver;
import com.ufo.tiago.moods.R;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import db_models.DaoSession;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Tiago on 24/10/17.
 */

public class Constants {

    /** --------------------------------------------------------------------------------------------
     * CONSTANTS
     -------------------------------------------------------------------------------------------- */

    public static final String PUBLIC_KEY = "";
    //Images size
    public static final int IMAGE_COMPLETE = 0;
    public static final int IMAGE_THUMBNAIL = 1;

    //DB
    public static String DB_NAME = "moods_dao.db";
    public static String ROL_COLLABORATOR = "rol_1";
    public static int N_ROL_COLLABORATOR = 0;
    public static int N_ROL_LEADER = 1;
    public static String ROL_LEADER = "rol_2";
    public static String OPEN = "1";
    public static String CLOSE = "0";

    //Extras for intent
    public static String SUCCESS_RESET_PASSWORD_STR = "SUCCESS_RESET_PASSWORD_STR";
    public static String SUCCESS_SEND_STR = "SUCCESS_SEND_STR";
    public static String SUCCESS_RESET_PASSWORD_MAIL_STR = "SUCCESS_RESET_PASSWORD_MAIL_STR";
    public static String ERROR_NETWORK = "ERROR_NETWORK";
    public static String ERROR_AUTH = "ERROR_AUTH";
    public static String ERROR_RESPONSE = "ERROR_RESPONSE";
    public static String MOOD_SELECTED = "MOOD_SELECTED";
    public static String FORM_SELECTED = "FORM_SELECTED";
    public static String DAY_OF_WEEK = "DAY_OF_WEEK";
    public static String NOTIFICATION_MOOD = "NOTIFICATION_MOOD";

    //Params
    public static int FORM_PAGINATION = 10;
    public static final int TIMEOUTS = 3000;
    public static final String DAY_INDICATOR = "D";
    public static final String WEEK_INDICATOR = "S";
    public static final String MONTH_INDICATOR = "M";

    //Extras for JSON
    public static String USERNAME = "usuario";
    public static String PASSWORD = "contrasena";
    public static String DATE = "fecha";
    public static String USER_ID = "idusuario";
    public static String ROL_ID = "ID_ROL";
    public static String ANSWER_DATE = "fecha_respuesta";
    public static String VALUE = "valor";
    public static String WORKDAY = "jornada";
    public static String EVENT_ID = "idevento";
    public static String MOOD_ID = "idmood";
    public static String EVENT_TEXT = "texto_evento";
    public static String FORM_ID = "idencuesta";
    public static String ANSWERS = "respuestas";
    public static String QUESTION_ID = "idpregunta";
    public static String ID_FORM_QUESTION = "idencuesta_pregunta";
    public static String OPTION_ID = "idopcion";
    public static String OPEN_QUESTION = "respuesta_abierta";
    public static String INDICATOR = "indicador";
    public static String TEAM_ID = "idequipo";
    public static String DAYS_WEEKS = "dias_semana";
    public static String WEEKS = "semanas";
    public static String WEEK = "semana";
    public static String DAY = "dia";
    public static String ANS = "ans";
    public static String ERROR = "error";
    public static String BODY = "body";
    public static String ID = "ID";
    public static String NAME = "nombre";
    public static String MAIL = "correo";
    public static String PHONE = "telefono";
    public static String POSITION = "cargo";
    public static String FK_ID_ROL = "fk_id_rol";
    public static String NROL = "nrol";
    public static String WORKDAY_SENDED = "envio_mood_jornada";
    public static String CIA_PARAMS = "parametros_compania";
    public static String START_HOUR = "hora_inicio";
    public static String END_HOUR = "hora_fin";
    public static String LABORAL_DAYS = "semana_laboral";
    public static String THERSHOLD = "umbral";
    public static String TEAMS = "equipos";
    public static String TEAMS_LEADER = "equipos_lider";
    public static String ID_TEAM = "idequipo";
    public static String EVENTS = "eventos";
    public static String CAMPAIGN_IMG = "campana_img";
    public static String CAMPAIGN_URL = "campana_url";
    public static String TOTAL_QUESTIONS = "total_preguntas";
    public static String CLOSE_DATE = "fecha_cierre";
    public static String STATUS = "estado";
    public static String OPEN_TEXT = "text_abierta";
    public static String NEEDED_QUESTION = "es_obligatoria";
    public static String QUESTIONS = "preguntas";
    public static String CATEGORY = "categoria";
    public static String QUESTION_TEXT = "texto_pregunta";
    public static String CONSECUTIVE = "consecutivo";
    public static String RESPONE_OPTIONS = "opciones_respuestas";
    public static String ID_FORM_OPTION_RESPONSE = "idencuesta_opcion";
    public static String LABEL = "label";
    public static String QUESTION_LABEL = "label_pregunta";
    public static String WORKINGDAY_DAY = "jornada_dia";
    public static String WORKINGDAY_LATE = "jornada_tarde";
    public static String MOODS = "moods";
    public static String AVERAGE = "promedio";
    public static String ESPECTED_ANSWERS = "respuestas_esperadas";
    public static String SENDED_ANSWERS = "total_respuestas_enviadas";
    public static String ID_MOOD = "ID_mood";
    public static String QUANTITY = "cantidad";
    public static String AVERAGE_DEVIATION = "desviacion_promedio";
    public static String OK = "ok";
    public static String NOT_OK = "nok";


    //Urls
    public static String TEST_URL = "https://httpbin.org/post";
    public static String MOODS_URL = "http://apimoods.nicepeopleconsulting.com/v1/api";
    public static String LOGIN_URL = MOODS_URL + "/in";
    public static String GET_PARAMS_URL = MOODS_URL + "/load";
    public static String FORGOT_PASSWORD_URL = MOODS_URL + "/pasreco";
    public static String GET_EVENTS_URL = MOODS_URL + "/mood/eventos";
    public static String SEND_MOOD_URL = MOODS_URL + "/mood/";
    public static String GET_CAMPAIGN_URL = MOODS_URL + "/campana/user";
    public static String GET_FORMS_URL = MOODS_URL + "/usuario/todasencuestas";
    public static String GET_QUESTIONS_URL = MOODS_URL + "/usuario/encuesta";
    public static String SEND_FORM_URL = MOODS_URL + "/usuario/answer";
    public static String GET_DAILY_STATS_URL = MOODS_URL + "/team/stats";
    public static String GET_WEEKLY_STATS_URL = MOODS_URL + "/";
    public static String GET_MONTHLY_STATS_URL = MOODS_URL + "/";
    public static String GET_BARS_STATS_URL = MOODS_URL + "/team/barstats";

    /** --------------------------------------------------------------------------------------------
     * METHODS
     -------------------------------------------------------------------------------------------- */

    /**
     * Format a date
     * @param date
     * @return
     */
    public static String formatDateToSend(Date date){
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return simpleDate.format(date);
    }

    /**
     * Format a date
     * @param date
     * @return
     */
    public static String formatSimpleDate(Date date){
        SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd");
        return simpleDate.format(date);
    }

    /**
     * Get the dates of a week, given a date
     * Just the dates of those days what are in the same month  of the given date
     * @param date
     * @return
     */
    public static List<String> getDatesFromWeek(Date date, DaoSession c){
        List<String> dates = new ArrayList<>();
        //SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int currentMonth = cal.get(Calendar.MONTH);
        cal.setMinimalDaysInFirstWeek(1);

        for (int i = 0; i < 7; i++){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(cal.getTime());
            calendar.set(Calendar.DAY_OF_WEEK, i);
            if(calendar.get(Calendar.MONTH)==currentMonth) {
                String dateDay = simpleDate.format(calendar.getTime());
                if(isDayInParams(calendar.get(Calendar.DAY_OF_WEEK),c)){
                    dates.add(dateDay);
                }
            }
        }

        return dates;
    }

    /**
     * Get the week number of a certain month
     * @param date
     * @return
     */
    public static int getTotalWeeksOfMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int maxWeeknumber = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
        return maxWeeknumber;
    }

    /**
     * Get a map of the dates of every day in the weeks of a month
     * @param date
     * @return
     */
    public static ArrayList<HashMap<String,List<String>>> getDatesWeeksFromMonth(Date date,DaoSession c){
        ArrayList<HashMap<String,List<String>>> dates = new ArrayList<>();
        //SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd");
        Log.e("Date",simpleDate.format(date));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int currentMonth = cal.get(Calendar.MONTH);
        Log.e("currentMonth",currentMonth+"");
        int totalWeeks = getTotalWeeksOfMonth(cal.getTime());

        for (int i = 1; i <= totalWeeks; i++){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(cal.getTime());
            calendar.set(Calendar.WEEK_OF_MONTH,i);
            calendar.getFirstDayOfWeek();
            int calMonth = calendar.get(Calendar.MONTH);
            if(calMonth < currentMonth){
                calendar.set(Calendar.MONTH,currentMonth);
                calendar.set(Calendar.DAY_OF_MONTH,1);
            }
            Log.e("Calendar",simpleDate.format(calendar.getTime()));
            List<String> datesOfWeek = getDatesFromWeek(calendar.getTime(),c);
            HashMap<String,List<String>> map = new HashMap<>();
            map.put("Semana_"+i,datesOfWeek);
            dates.add(map);
        }

        HashMap<String,ArrayList<HashMap<String,List<String>>>> map = new HashMap<>();
        map.put("Fechas",dates);
        JSONObject jsonObject = new JSONObject(map);
        Log.e("MAPA DE FECHAS",jsonObject.toString());

        return dates;
    }

    /**
     * Gets the date of a give string - In hour format
     * @param hour
     * @return
     */
    public static Date hourOfString(String hour){
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date;
        try {
            date = format.parse(hour);
        }catch (Exception e){
            date = null;
        }
        return date;
    }

    /**
     * Send a notification, to send a new Mood
     * @param context
     */
    public static void notificateToSendMood(Context context){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(context.getString(R.string.feeling))
                        .setAutoCancel(true);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setSound(soundUri);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher));
        mBuilder.setAutoCancel(true);
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        Intent resultIntent = new Intent(context, LoadingActivity.class);
        resultIntent.putExtra(NOTIFICATION_MOOD,true);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);
        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    /**
     *
     * @param context
     * @param hour
     * @param minutes
     * @param alarmId
     */
    public static void setAlarmForMoods(Context context,int hour,int minutes, int hourLate,int minutesLate,int alarmId){
        SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Log.e("In notification","Entro a configurar la notificacion");
        Log.e("Actual date",simpleDate.format(new Date()));
        //Constants.notificateToSendMood(CollaboratorMenuActivity.this);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minutes);

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int curr = now.get(Calendar.HOUR_OF_DAY);
        int currMin = now.get(Calendar.MINUTE);

        if(curr>hour){
            calendar.add(Calendar.DATE,1);
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,minutes);
            Log.e("Nueva date",simpleDate.format(calendar.getTime()));
        }else if(curr==hour){
            if(currMin>minutes){
                calendar.add(Calendar.DATE,1);
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,minutes);
                Log.e("Nueva date por min",simpleDate.format(calendar.getTime()));
            }
        }

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY));
        calendar2.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE)+2);

        Log.e("Alarma estimada a ",simpleDate.format(calendar.getTime()));
        //Intent intent = new Intent(context, Notification_reciver.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

        if(curr>hour){
            calendar2.add(Calendar.DATE,1);
            calendar2.set(Calendar.HOUR_OF_DAY,hour);
            calendar2.set(Calendar.MINUTE,minutes);
            Log.e("Nueva date 2",simpleDate.format(calendar2.getTime()));
        }else if(curr==hour){
            if(currMin>minutes){
                calendar2.add(Calendar.DATE,1);
                calendar2.set(Calendar.HOUR_OF_DAY,hour);
                calendar2.set(Calendar.MINUTE,minutes);
                Log.e("Nueva date por min 2",simpleDate.format(calendar2.getTime()));
            }
        }

        Log.e("Alarma estimada 2 a ",simpleDate.format(calendar2.getTime()));
        //PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar2.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent2);

        /**
         * For test
         */
        Intent intent = new Intent(context, Notification_reciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),60*1000,
                pendingIntent);
    }

    /**
     * True if a given day of week is in the String of days in the parameters
     * @param dayOfWeek
     * @return
     */
    private static boolean isDayInParams(int dayOfWeek,DaoSession daoSession){
        //DaoSession daoSession = ((DaoMoodsApp)c).getDaoSession();;
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

    /**
     * Get the dates of a month, given a date
     * Just the dates of those days what are in the same month  of the given date
     * @param date
     * @return
     */
    public static List<String> getDatesFromMonth(Date date){
        List<String> dates = new ArrayList<>();
        //SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int myMonth=cal.get(Calendar.MONTH);

        while (myMonth==cal.get(Calendar.MONTH)) {
            Log.e("DAYS OF MONTH",simpleDate.format(cal.getTime()));
            dates.add(simpleDate.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH, 1);

        }

        return dates;
    }

    /**
     * Get the image of a mood, given a average
     * @param average
     * @return
     */
    public static int getResourceOfAverage(double average){
        int moodRes = 0;

        if(average>=0 && average<2){
            moodRes = R.drawable.mood_angry;
        }else if(average>=2 && average<3){
            moodRes = R.drawable.mood_sad;
        }else if(average>=3 && average<4){
            moodRes = R.drawable.mood_5;
        }else if(average>=4 && average<4.8){
            moodRes = R.drawable.mood_normal;
        }else{
            moodRes = R.drawable.mood_happy;
        }

        return  moodRes;
    }
}
