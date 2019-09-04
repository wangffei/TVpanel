package com.e.util;

import com.alibaba.fastjson.JSONObject;
import com.e.db.DbUtil;
import com.e.db.KeyValueDb;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KVMap {
    private static DbManager manager = null ;

    private Map<String , String> map = new HashMap<String , String>() ;

    private static KVMap kvmap = null ;

    /**
     * 构造方法私有化
     */
    private KVMap(){
        manager = DbUtil.getDbManager() ;
        //将数据库中所有的数据查出来放入map集合中
        List<KeyValueDb> list = null ;
        try{
            list = manager.findAll(KeyValueDb.class) ;
            if(list == null){
                return ;
            }
        }catch(Exception e){
            e.printStackTrace();
            return ;
        }
        for(KeyValueDb kv : list){
            map.put(kv.getKey() , kv.getValue()) ;
        }
    }

    public synchronized static KVMap getInstance(){
        if(kvmap == null){
            kvmap = new KVMap() ;
            if(manager == null){
                throw new NullPointerException("manager can't be null ") ;
            }
        }
        return kvmap ;
    }

    public void put(String key , String value) throws DbException {

        if(map.get(key) == null){
            KeyValueDb kv = new KeyValueDb() ;
            kv.setKey(key);
            kv.setValue(value);
            manager.save(kv);
        }else{
            KeyValue kv = new KeyValue("v" , value) ;
            manager.update(KeyValueDb.class , WhereBuilder.b("k" , "=" , key) , kv) ;
        }
        map.put(key , value) ;
    }

    public String get(String key){
        return map.get(key) ;
    }

    public JSONObject getJson(String key){
        return JSONObject.parseObject(map.get(key));
    }

    public int delete(String key) throws DbException {
        if(map.get(key) == null){
            return 0 ;
        }
        int count = manager.delete(KeyValueDb.class , WhereBuilder.b("k" , "=" , key)) ;
        map.remove(key) ;
        return count ;
    }
}
