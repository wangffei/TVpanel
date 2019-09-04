package com.e.db;

import com.e.tvpanel.MyApp;

import org.xutils.DbManager;
import org.xutils.config.DbConfigs;
import org.xutils.x;

public class DbUtil {
    private static DbManager manager = null ;
    /**
     * 获取数据库的管理对象
     * @return
     */
    public synchronized static DbManager getDbManager(){
        if(manager == null){
            manager = MyApp.getInstance().getManager() ;
        }
        return manager ;
    }
}
