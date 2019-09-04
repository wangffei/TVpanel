package com.e.tvpanel;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.e.adapter.HttpAdapter;
import com.e.control.DispatcherClass;
import com.e.util.KVMap;

import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class MainActivity extends AppCompatActivity {

    private String url = "http://106.13.59.71/vueDrag/screen/1/android/android.json" ;

    private String language = "CN" ;

    private static MainActivity instance ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this ;

        //不显示程序的标题栏
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        //不显示系统的标题栏
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView(R.layout.activity_main1);
        x.view().inject(this);

        init() ;

//        KVMap map = KVMap.getInstance() ;
//        try {
//            String name = map.get("name") ;
//            System.out.println(name == null ? "null" : name);
//            map.put("name" , "wf");
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
    }

    public void init(){
        KVMap map = KVMap.getInstance() ;
        JSONObject obj = map.getJson("android") ;
        //判断是否有已经解析好的json,如果没有就从新请求
        if(obj == null){
            x.http().get(new RequestParams(url) , new HttpAdapter<String>(){
                @Override
                public void onSuccess(String result) {
                    //System.out.println(result);
                    DispatcherClass dispatcherClass = new DispatcherClass(MainActivity.this , JSONObject.parseObject(result) , language) ;
                    try {
                        dispatcherClass.handler();
                    } catch (DbException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this , "数据库存储异常" , Toast.LENGTH_LONG).show();
                    }
                }

//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//                    Toast.makeText(MainActivity.this , "网络请求失败" , Toast.LENGTH_LONG ).show() ;
//                }
            }) ;
        }
    }

    public static View getViewById(int id){
        return instance.findViewById(id) ;
    }
}
