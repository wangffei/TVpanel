package com.e.util;

import android.content.Context;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.e.impl.NavigationBarIconImpl;
import com.e.impl.NavigationBarTxtImpl;
import com.e.interfaces.NavigationBar;
import com.e.interfaces.ViewHandler;

import java.util.HashMap;
import java.util.Map;

public class NavigationBarUtil extends ViewHandler {

    private static Map<String , NavigationBar> map = new HashMap<>() ;

    private static NavigationBarUtil instance = new NavigationBarUtil();

    private Context context ;

    private String server_ ;

    private AbsoluteLayout content_ ;

    static{
        map.put("bar_icon" , new NavigationBarIconImpl()) ;
        map.put("bar_txt" , new NavigationBarTxtImpl()) ;
    }


    private NavigationBarUtil(){}

    public static NavigationBarUtil getInstance(){
        return instance ;
    }

    @Override
    public void init(Context context, JSONObject jsonObject, String language) {

    }

    @Override
    public void doHandler() {

    }

    public void init(Context context , JSONObject jsonObject , String server , RelativeLayout bottom , AbsoluteLayout content){
        this.context = context ;
        this.server_ = server ;
        this.content_ = content ;
        String type = jsonObject.getString("type") ;
        map.get(type).initNavigationBar(context , jsonObject , server , bottom);
    }

    public void initContent(JSONObject jsonObject , int index){
        initContentByIndex(context , jsonObject , index , server_ , content_);
    }
}
