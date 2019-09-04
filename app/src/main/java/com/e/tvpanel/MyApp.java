package com.e.tvpanel;

import android.app.Application;

import org.xutils.DbManager;
import org.xutils.x;

public class MyApp extends Application {

    private static MyApp instance ;

    public static MyApp getInstance(){
        return instance ;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        //x.Ext.setDebug(true);
        instance = this ;
    }

    public DbManager getManager(){
        DbManager.DaoConfig config = new DbManager.DaoConfig() ;
        config.setDbName("data.db") ;
        DbManager manager = x.getDb(config) ;
        return manager ;
    }
}
