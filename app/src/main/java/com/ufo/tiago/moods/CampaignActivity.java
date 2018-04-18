package com.ufo.tiago.moods;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import db_models.DaoSession;
import db_models.UserSession;
import utils.Constants;
import utils.NetworkHelper;

public class CampaignActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageView imgCampana;
    private ProgressBar loader;
    private NetworkHelper networkHelper;
    private UserSession userSession;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);

        daoSession = ((DaoMoodsApp)getApplication()).getDaoSession();
        networkHelper = new NetworkHelper(this);
        List<UserSession> sessions = daoSession.getUserSessionDao().loadAll();
        userSession = sessions.get(0);

        //Init the UI
        btnBack = (ImageButton) findViewById(R.id.back_img);
        imgCampana = (ImageView) findViewById(R.id.img_campaign);
        loader = (ProgressBar) findViewById(R.id.loader);

        //Get the campaign
        new GetCampaign(networkHelper.jsonForGetCampaigne(userSession.getId_server(),Constants.formatDateToSend(new Date()))).execute();

        //Click on back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CampaignActivity.this,CollaboratorMenuActivity.class);
        startActivity(i);
        finish();
    }

    // ---------------------------------------------------------------------------------
    /**
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     *
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(String resId,
                                                         int reqWidth, int reqHeight, int pos) {

        // First decode with inJustDecodeBounds=true to check dimensions
        byte[] decodedString = Base64.decode(resId, Base64.NO_PADDING);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(decodedString,0,decodedString.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bp = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length, options);
        if(pos == Constants.IMAGE_COMPLETE){
            return bp;
        }else {
            int w = 256;
            int h = 256;
            Bitmap bitmap2 = ThumbnailUtils.extractThumbnail(bp, w, h);
            return bitmap2;
        }
    }

    /**
     * Processing Bitmaps Off the UI Thread
     */
    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int pos;
        private String data = "";

        public BitmapWorkerTask(ImageView imageView,int pos) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
            this.pos = pos;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            data = params[0];
            return decodeSampledBitmapFromResource(data,200,200,pos);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask =
                        getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {

                    BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                    imageView.setBackground(ob);
                }
            }
        }
    }

    /**
     *   </ Processing Bitmaps Off the UI Thread >
     */

    /**
     * Handle Concurrency
     */
    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    /**
     *
     * @param resId
     * @param imageView
     */
    public void loadBitmap(String resId, ImageView imageView,int pos) {
        if (cancelPotentialWork(resId, imageView)) {
            Bitmap mPlaceHolderBitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView,pos);
            final AsyncDrawable asyncDrawable =
                    new AsyncDrawable(getResources(), mPlaceHolderBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
        }
    }

    /**
     *
     * @param data
     * @param imageView
     * @return
     */
    public static boolean cancelPotentialWork(String data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapData = bitmapWorkerTask.data;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData.equals("") || bitmapData != data) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    /**
     *
     * @param imageView
     * @return
     */
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    /**
     * </ Handle Concurrency >
     */

    /** --------------------------------------------------------------------------------------------
     * Asynctask: GET THE  LAST CAMPAIGN FROM SERVER
     -------------------------------------------------------------------------------------------- */
    public class GetCampaign extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject jsonObject;

        public GetCampaign(JSONObject jsonObject) {
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
            return networkHelper.sendPOST(jsonObject, Constants.GET_CAMPAIGN_URL);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            loader.setVisibility(View.GONE);
            //TODO Get the response  - Handle errors
            boolean error = (jsonObject==null);

            if (error){
                //Go to the error activity
                Intent goToError = new Intent(CampaignActivity.this, ErrorActivity.class);
                goToError.putExtra(Constants.ERROR_RESPONSE, "Error en el servidor");
                startActivity(goToError);
            }else {
                Log.e("JSON RESPONSE",jsonObject.toString());
                //Go to the success activity
                // Get the image
                try {
                    boolean ans = jsonObject.getBoolean(Constants.ANS);

                    if (ans){
                        JSONObject body = jsonObject.getJSONObject(Constants.BODY);
                        String image_base64 = body.getString("image_base64");
                        //Log.e("IMAGE",image_base64);
                        loadBitmap(image_base64,imgCampana,Constants.IMAGE_COMPLETE);

                    }else{
                        String errorInJson = jsonObject.getString(Constants.ERROR);
                        //Go to the error activity
                        Intent goToError = new Intent(CampaignActivity.this, ErrorActivity.class);
                        goToError.putExtra(Constants.ERROR_RESPONSE,errorInJson);
                        startActivity(goToError);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("Error en json",e.toString());
                    //Go to the error activity
                    Intent goToError = new Intent(CampaignActivity.this, ErrorActivity.class);
                    goToError.putExtra(Constants.ERROR_RESPONSE, e.toString());
                    startActivity(goToError);
                }
            }
        }

    }
}
