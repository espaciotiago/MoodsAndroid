package utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import db_models.DaoSession;
import model.Form;
import model.FormQuestion;
import model.Mood;
import model.ResponseMood;

/**
 * Created by Tiago on 25/10/17.
 */

public class NetworkHelper {

    /**
     * Logical elemnts
     */
    private Context context;

    /**
     * Constructor
     * @param context
     */
    public NetworkHelper(Context context){
        this.context = context;
    }

    /**
     * Send a POST request to the server
     * @param data
     * @param urlStr
     * @return
     */
    public JSONObject sendPOST(JSONObject data, String urlStr){

        try {

            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setConnectTimeout(Constants.TIMEOUTS);
            httpConn.setReadTimeout(Constants.TIMEOUTS);
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("POST");
            httpConn.connect();

            OutputStream os = httpConn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(data.toString());
            osw.flush();
            osw.close();

            InputStream is = httpConn.getInputStream();
            String parsedString = convertinputStreamToString(is);

            JSONObject jsnobject = new JSONObject(parsedString);

            return jsnobject;

        } catch (SocketTimeoutException se){
            Log.e("TIMEOUT","Timeout error");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Send a POST request to the server
     * @param data
     * @param urlStr
     * @return
     */
    public JSONArray sendPOSTArray(JSONObject data, String urlStr){

        try {

            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setConnectTimeout(Constants.TIMEOUTS);
            httpConn.setReadTimeout(Constants.TIMEOUTS);
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("POST");
            httpConn.connect();

            OutputStream os = httpConn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(data.toString());
            osw.flush();
            osw.close();

            InputStream is = httpConn.getInputStream();
            String parsedString = convertinputStreamToString(is);

            JSONArray jsnobject = new JSONArray(parsedString);

            return jsnobject;

        } catch (SocketTimeoutException se){
            Log.e("TIMEOUT","Timeout error");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Send a GET request to the server
     * @param urlStr
     * @return
     */
    public JSONObject sendGET(String urlStr){
        try {

            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setConnectTimeout(Constants.TIMEOUTS);
            httpConn.setReadTimeout(Constants.TIMEOUTS);
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            InputStream is = httpConn.getInputStream();
            String parsedString = convertinputStreamToString(is);

            JSONArray jsnarray = new JSONArray(parsedString);
            Log.d("JSONARR",jsnarray.toString());
            JSONObject jsonObject = new JSONObject();

            return jsonObject;

        } catch (SocketTimeoutException se){
            Log.e("TIMEOUT","Timeout error");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Send a GET request to the server
     * @param urlStr
     * @param param
     * @return
     */
    public JSONObject sendGETArr(String urlStr,String param){
        try {

            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setConnectTimeout(Constants.TIMEOUTS);
            httpConn.setReadTimeout(Constants.TIMEOUTS);
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            InputStream is = httpConn.getInputStream();
            String parsedString = convertinputStreamToString(is);

            JSONArray jsnarray = new JSONArray(parsedString);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(param,jsnarray);

            return jsonObject;

        } catch (SocketTimeoutException se){
            Log.e("TIMEOUT","Timeout error");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Send a GET request to the server
     * @param urlStr
     * @param param
     * @return
     */
    public JSONObject sendGETArrWithAuth(String urlStr,String param){
        try {
            // TODO: 9/12/16 Remplazar auth1 con el token correpondiente
            String auth1 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJVc2VySWQiOiIxIiwiVXNlck5hbWUiOiJhZG1pbiIsIkxvZ2luRGF0ZSI6NDU2fQ.lYEiAcPyvdOfpUsUzfx3psZApOMI2lbb12YSKwA-pUk";
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setConnectTimeout(Constants.TIMEOUTS);
            httpConn.setReadTimeout(Constants.TIMEOUTS);
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestProperty("Authorization",auth1);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            InputStream is = httpConn.getInputStream();
            String parsedString = convertinputStreamToString(is);

            JSONArray jsnarray = new JSONArray(parsedString);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(param,jsnarray);

            return jsonObject;

        } catch (SocketTimeoutException se){
            Log.e("TIMEOUT","Timeout error");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Send a GET request to the server
     * @param urlStr
     * @return
     */
    public JSONObject sendGETWithAuth(String urlStr){
        try {

            // TODO: 9/12/16 Remplazar auth1 con el token correpondiente
            String auth1 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJVc2VySWQiOiIxIiwiVXNlck5hbWUiOiJhZG1pbiIsIkxvZ2luRGF0ZSI6NDU2fQ.lYEiAcPyvdOfpUsUzfx3psZApOMI2lbb12YSKwA-pUk";
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setConnectTimeout(Constants.TIMEOUTS);
            httpConn.setReadTimeout(Constants.TIMEOUTS);
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            // TODO: 6/12/16 Cambiar por auth parametro
            httpConn.setRequestProperty("Authorization",auth1);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            InputStream is = httpConn.getInputStream();
            String parsedString = convertinputStreamToString(is);


            JSONObject jsnobject = new JSONObject(parsedString);

            return jsnobject;

        } catch (SocketTimeoutException se){
            Log.e("TIMEOUT","Timeout error");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Send a POST request to the server
     * @param data
     * @param urlStr
     * @return
     */
    public JSONObject sendPOSTWithAuth(JSONObject data, String urlStr, String auth){

        try {

            String auth1 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJVc2VySWQiOiIxIiwiVXNlck5hbWUiOiJhZG1pbiIsIkxvZ2luRGF0ZSI6NDU2fQ.lYEiAcPyvdOfpUsUzfx3psZApOMI2lbb12YSKwA-pUk";
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setConnectTimeout(Constants.TIMEOUTS);
            httpConn.setReadTimeout(Constants.TIMEOUTS);
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            // TODO: 6/12/16 Cambiar por auth parametro
            httpConn.setRequestProperty("Authorization",auth1);
            httpConn.setRequestMethod("POST");
            httpConn.connect();

            OutputStream os = httpConn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(data.toString());
            osw.flush();
            osw.close();

            InputStream is = httpConn.getInputStream();
            String parsedString = convertinputStreamToString(is);

            JSONObject jsnobject = new JSONObject(parsedString);

            return jsnobject;

        } catch (SocketTimeoutException se){
            Log.e("TIMEOUT","Timeout error");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Inputstring to String
     * @param ists
     * @return
     * @throws IOException
     */
    public String convertinputStreamToString(InputStream ists)
            throws IOException {
        if (ists != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader r1 = new BufferedReader(new InputStreamReader(ists, "UTF-8"));
                while ((line = r1.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                ists.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    /** --------------------------------------------------------------------------------------------
     * JSON Constuction for the params to be sended
     -------------------------------------------------------------------------------------------- */

    /**
     *
     * @param usuario
     * @param contrasena
     * @param fecha
     * @return
     */
    public JSONObject jsonForSendLogin(String usuario,String contrasena,String fecha,String token){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USERNAME, usuario);
            jsonObject.put(Constants.PASSWORD, contrasena);
            jsonObject.put(Constants.DATE, fecha);
            jsonObject.put("id_dispositivo", token);
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return jsonObject;
    }

    /**
     *
     * @param usuario
     * @param equipo
     * @param fecha
     * @return
     */
    public JSONObject jsonForGetBarStats(String usuario,String equipo,String fecha){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USERNAME, usuario);
            jsonObject.put("idequipo", equipo);
            jsonObject.put(Constants.DATE, fecha);
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return jsonObject;
    }

    /**
     *
     * @param usuario
     * @return
     */
    public JSONObject jsonForCloseSession(String usuario){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idusuario", usuario);
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return jsonObject;
    }

    /**
     *
     * @param userId
     * @param rolId
     * @param date
     * @return
     */
    public JSONObject jsonForGetParamsLoading(String userId,String rolId,String date){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_ID, userId);
            jsonObject.put(Constants.ROL_ID, rolId);
            jsonObject.put(Constants.DATE, date);
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return jsonObject;
    }

    /**
     *
     * @param userName
     * @param date
     * @return
     */
    public JSONObject jsonForPasswordRecovery(String userName,String date){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USERNAME, userName);
            jsonObject.put(Constants.DATE, date);
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return jsonObject;
    }

    /**
     *
     * @param userId
     * @return
     */
    public JSONObject jsonForGetEvents(String userId){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_ID, userId);
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return jsonObject;
    }

    /**
     *
     * @param userId
     * @return
     */
    public JSONObject jsonForSendMood(String userId, ResponseMood responseMood,String teamid){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_ID, userId);
            jsonObject.put(Constants.ANSWER_DATE, responseMood.getCurrentDate());
            jsonObject.put(Constants.VALUE, responseMood.getMood().getValue());
            jsonObject.put(Constants.WORKDAY, responseMood.getWorkday());
            jsonObject.put(Constants.EVENT_ID, responseMood.getEvent().getIdInServer());
            jsonObject.put(Constants.MOOD_ID, responseMood.getMood().getIdInServer());
            jsonObject.put(Constants.EVENT_TEXT, responseMood.getEventText());
            jsonObject.put("idequipo", teamid);
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return jsonObject;
    }


    /**
     *
     * @param userId
     * @param date
     * @return
     */
    public JSONObject jsonForGetForms(String userId,String date){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_ID, userId);
            jsonObject.put(Constants.DATE, date);
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return jsonObject;
    }

    /**
     *
     * @param userId
     * @param date
     * @param formId
     * @return
     */
    public JSONObject jsonForGetQuestions(String userId,String date,String formId){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_ID, userId);
            jsonObject.put(Constants.DATE, date);
            jsonObject.put(Constants.FORM_ID, formId);
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return jsonObject;
    }

    /**
     *
     * @param userId
     * @param date
     * @param form
     * @return
     */
    public JSONObject jsonForSendForm(String userId, String date, Form form){
        HashMap<String,ArrayList<HashMap<String,String>>> map = new HashMap<>();
        ArrayList<HashMap<String,String>> maps = new ArrayList<>();

        for (int i = 0; i < form.getQuestions().size(); i ++){
            HashMap<String,String> questionMap = new HashMap<>();
            FormQuestion formQuestion = form.getQuestions().get(i);
            questionMap.put(Constants.QUESTION_ID,formQuestion.getIdInServer());
            questionMap.put(Constants.OPTION_ID,formQuestion.getSelectedOption().getIdInServer());
            questionMap.put(Constants.VALUE,formQuestion.getSelectedOption().getValue()+"");
            maps.add(questionMap);
        }
        map.put(Constants.ANSWERS,maps);
        JSONObject jsonObject = new JSONObject(map);
        try {
            jsonObject.put(Constants.USER_ID, userId);
            jsonObject.put(Constants.DATE, date);
            jsonObject.put(Constants.FORM_ID, form.getIdInServer());
            jsonObject.put(Constants.OPEN_QUESTION, form.getObservations());
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return jsonObject;
    }

    /**
     *
     * @param userId
     * @param date
     * @return
     */
    public JSONObject jsonForGetCampaigne(String userId,String date){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_ID, userId);
            jsonObject.put(Constants.DATE, date);
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return jsonObject;
    }

    /**
     *
     * @param userId
     * @param date
     * @param teamId
     * @return
     */
    public JSONObject jsonForGetDayStats(String userId,String date,String teamId){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.USER_ID, userId);
            jsonObject.put(Constants.DATE, date);
            jsonObject.put(Constants.TEAM_ID,teamId);
            jsonObject.put(Constants.INDICATOR,Constants.DAY_INDICATOR);
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return jsonObject;
    }

    /**
     *
     * @param userId
     * @param date
     * @param teamId
     * @param dates
     * @return
     */
    public JSONObject jsonForGetWeekStats(String userId,String date,String teamId,List<String> dates){
        HashMap<String,ArrayList<HashMap<String,String>>> map = new HashMap<>();
        ArrayList<HashMap<String,String>> maps = new ArrayList<>();
        for (int i = 0; i < dates.size();i++){
            HashMap<String,String> dateMap = new HashMap<>();
            dateMap.put(Constants.DAY,dates.get(i));
            maps.add(dateMap);
        }
        map.put(Constants.DAYS_WEEKS,maps);

        JSONObject jsonObject = new JSONObject(map);
        try {
            jsonObject.put(Constants.USER_ID, userId);
            jsonObject.put(Constants.DATE, date);
            jsonObject.put(Constants.TEAM_ID,teamId);
            jsonObject.put(Constants.INDICATOR,Constants.WEEK_INDICATOR);
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return jsonObject;
    }

    /**
     *
     * @return
     */
    public JSONObject jsonForGetMonthByWeekStats(String idusuario, String idequipo, DaoSession c, Date date){
        ArrayList<HashMap<String,List<String>>> weeksByMontMap = Constants.getDatesWeeksFromMonth(date,c);
        return null;
    }

    /**
     *
     * @param userId
     * @param date
     * @param teamId
     * @param datesOfMonth
     * @return
     */
    public JSONObject jsonForGetMonthStats(String userId,String date,String teamId, ArrayList<HashMap<String,List<String>>> datesOfMonth){
        JSONArray jsonArray = new JSONArray();
        for (int i =0; i < datesOfMonth.size(); i++){
            HashMap<String,List<String>> mapOfWeek = datesOfMonth.get(i);
            int weekIndex = i+1;

            List<String> daysOfWeek = mapOfWeek.get("Semana_"+weekIndex);
            ArrayList<HashMap<String,String>> datesForThisWeek = new ArrayList<>();
            HashMap<String,ArrayList<HashMap<String,String>>> weeksMap = new HashMap<>();
            if (daysOfWeek!=null){
                for (int j = 0; j < daysOfWeek.size(); j++){
                    HashMap<String,String> mapOfDays = new HashMap<>();
                    String day = daysOfWeek.get(j);
                    mapOfDays.put(Constants.DAY,day);
                    datesForThisWeek.add(mapOfDays);
                }
                weeksMap.put(Constants.DAYS_WEEKS,datesForThisWeek);
                JSONObject weekJsonObject = new JSONObject(weeksMap);
                try {
                    weekJsonObject.put(Constants.WEEK, weekIndex);
                }catch (Exception e){

                }

                jsonArray.put(weekJsonObject);
            }
        }



        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.WEEKS,jsonArray);
            jsonObject.put(Constants.USER_ID, userId);
            jsonObject.put(Constants.DATE, date);
            jsonObject.put(Constants.TEAM_ID,teamId);
            jsonObject.put(Constants.INDICATOR,Constants.MONTH_INDICATOR);
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
        return jsonObject;
    }

}
