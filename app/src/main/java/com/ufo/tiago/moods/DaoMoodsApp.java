package com.ufo.tiago.moods;

import android.app.Application;

import db_models.DaoMaster;
import db_models.DaoSession;
import utils.Constants;
import utils.DbOpenHelper;

/**
 * Created by Tiago on 1/11/17.
 */

public class DaoMoodsApp extends Application {

    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mDaoSession = new DaoMaster(new DbOpenHelper(this, Constants.DB_NAME).getWritableDb()).newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
