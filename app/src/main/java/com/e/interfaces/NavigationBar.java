package com.e.interfaces;

import android.content.Context;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;

public interface NavigationBar {

    void initNavigationBar(Context context , JSONObject jsonObject , String server , RelativeLayout bottom) ;

}
