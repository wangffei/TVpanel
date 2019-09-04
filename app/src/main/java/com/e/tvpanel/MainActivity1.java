package com.e.tvpanel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e.adapter.HttpAdapter;

import org.xutils.http.RequestParams;
import org.xutils.x;
import org.xutils.view.annotation.ViewInject ;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity1 extends AppCompatActivity {

    @ViewInject(R.id.bg)
    private ImageView bg ;

    @ViewInject(R.id.logo)
    private ImageView logo ;

    @ViewInject(R.id.content)
    private AbsoluteLayout content ;

    @ViewInject(R.id.bar)
    private LinearLayout bar ;

    private String language = "CN" ;
    private String server = "" ;

    //同步锁
    private Object lock = new Object() ;
    private boolean flag = false ;

    //b保存所有的导航栏控件
    private List<ImageView> list = new ArrayList<>() ;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("信息" , "初始化") ;
        super.onCreate(savedInstanceState);

        //不显示程序的标题栏
        requestWindowFeature( Window.FEATURE_NO_TITLE );

        //不显示系统的标题栏
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView(R.layout.activity_main);
        x.view().inject(this);

        init() ;
    }

    private void init() {
        String url = "http://192.168.123.22/vueDrag/screen/1/android/android.json" ;

        System.out.println(url);

        final RequestParams parame = new RequestParams(url) ;
        x.http().get(parame, new HttpAdapter<String>(){
            @Override
            public void onSuccess(String result) {
                Log.d("信息" , "请求成功") ;
                JSONObject jsonObject = JSONObject.parseObject(result) ;

                if(jsonObject.getInteger("code") == 200){

                    JSONObject data = jsonObject.getJSONObject("data") ;

                    if(data != null){
                        server = data.getString("SERVER") ;

                        JSONObject json = data.getJSONObject(language) ;

                        JSONObject desktop = json.getJSONObject("desktop") ;

                        if(desktop != null){

                            //1. 解析桌面背景
                            String bgUrl = desktop.getString("bg") ;
                            if(bgUrl != null){
                                Log.d("["+language+" -> desktop -> bg]" , server + bgUrl.substring(1)) ;
                                x.image().bind(bg , server + bgUrl.substring(1));
                            }

                            //2. logo解析
                            JSONObject logoData = desktop.getJSONObject("logo") ;
                            if(logoData != null){
                                final String logoUrl = logoData.getString("img") ;
                                Log.d("["+language+" -> desktop -> logo]" , server + logoUrl.substring(1)) ;
                                //开启一个http请求去获取图片数据
                                x.http().get(new RequestParams(server + logoUrl.substring(1)) , new HttpAdapter<byte[]>(){
                                    @Override
                                    public void onSuccess(byte[] result) {
                                        Bitmap bit = byte2Bitmap(result , new BitmapFactory.Options()) ;
                                        logo.setImageBitmap(bit);
                                    }
                                }) ;
                            }

                            //3. 解析内容组件和导航栏组件
                            JSONObject main = desktop.getJSONObject("main") ;

                            if(main != null){
                                JSONArray bars = main.getJSONArray("data") ;

                                //1. 第一个内容
                                JSONObject contentFirst = bars.getJSONObject(0).getJSONObject("content") ;
                                addContent(contentFirst , main.getBoolean("exist"));

                                //2. 导航
                                for(int i=0 ; i<bars.size() ; i++){
                                    String iconUrl = bars.getJSONObject(i).getString("icon") ;
                                    Log.d("导航栏" , server+iconUrl.substring(1)) ;

                                    byte[] results = download(server + iconUrl.substring(1)) ;

                                    ImageView img = new ImageView(MainActivity1.this) ;
                                    list.add(img) ;
                                    Bitmap bit = byte2Bitmap(results , new BitmapFactory.Options()) ;
                                    img.setImageBitmap(bit);
                                    img.setScaleType(ImageView.ScaleType.CENTER);
                                    if(i == 0){
                                        img.setBackground(MainActivity1.this.getApplicationContext().getResources().getDrawable(R.drawable.image_view_border));
                                    }

                                    RelativeLayout rl = new RelativeLayout(MainActivity1.this ) ;
                                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(135 , 135 ) ;
                                    rl.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
                                    rl.addView(img , params);

                                    //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(135 , 135 , 1 ) ;
                                    bar.addView(rl , new LinearLayout.LayoutParams(135 , 135 , 1));

                                    img.setOnClickListener(new MyEvent(bars.getJSONObject(i).getJSONObject("content") , i)) ;
                                }
                            }
                        }
                    }
                }
            }
        }) ;
    }

    public byte[] download(final String path) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream() ;
        synchronized (lock){
            try{
                new Thread(){
                    @Override
                    public void run() {
                        synchronized (lock) {
                            try{
                                URL url = new URL(path) ;
                                InputStream in = url.openStream() ;
                                int len = -1 ;
                                byte[] data = new byte[1024] ;

                                while((len = in.read(data)) != -1){
                                    out.write(data , 0 , len);
                                }

                                in.close();

                                lock.notify();

                                flag = true ;
                            } catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }.start() ;
                if(!flag){
                    lock.wait();
                }
                flag = false ;
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return out.toByteArray() ;
    }

    public void addContent(JSONObject obj){
        JSONObject layout = obj.getJSONObject("layout") ;
        JSONArray layout_1080 = layout.getJSONObject("1080p").getJSONArray("1") ;
        Log.d("layout " , layout.toJSONString()) ;

        add(layout_1080 , obj) ;
    }

    public void addContent(JSONObject obj , boolean hasBar){
        JSONObject layout = obj.getJSONObject("layout").getJSONObject("1080p") ;
        Log.d("layout " , layout.toJSONString()) ;
        JSONArray layout_1080 = null ;

        if(hasBar){
            layout_1080 = layout.getJSONArray("1") ;
        }else{
            layout_1080 = layout.getJSONArray("2") ;
        }

        add(layout_1080 , obj) ;
    }

    private void add(final JSONArray layout , JSONObject obj){
        JSONArray infos = obj.getJSONArray("info") ;

        for(int i=0 ; i<infos.size() ; i++){
            final int index = i ;
            JSONObject o = infos.getJSONObject(i) ;
            String imgUrl = o.getJSONArray("imgs").getString(0) ;
            System.out.println(server+imgUrl.substring(1));
            x.http().get(new RequestParams(server+imgUrl.substring(1)) , new HttpAdapter<byte[]>(){
                @Override
                public void onSuccess(byte[] result) {
                    ImageView img = new ImageView(MainActivity1.this) ;
                    Bitmap bit = byte2Bitmap(result , new BitmapFactory.Options()) ;
                    img.setImageBitmap(bit);
                    img.setScaleType(ImageView.ScaleType.FIT_XY);
                    AbsoluteLayout.LayoutParams absLayoutParams = new AbsoluteLayout.LayoutParams(
                            layout.getJSONObject(index).getInteger("width"),
                            layout.getJSONObject(index).getInteger("height"),
                            layout.getJSONObject(index).getInteger("left"),
                            layout.getJSONObject(index).getInteger("top")) ;
                    content.addView(img , absLayoutParams);
                }
            }) ;
        }
    }

    /**
     * @param 将字节数组转换为ImageView可调用的Bitmap对象
     * @param bytes
     * @param opts
     * @return Bitmap
     */
    public static Bitmap byte2Bitmap(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    class MyEvent implements View.OnClickListener {

        private JSONObject obj ;

        private int index ;

        MyEvent(JSONObject obj , int index){
            this.obj = obj ;
            this.index = index ;
        }

        @Override
        public void onClick(View v) {
            content.removeAllViews();
            addContent(this.obj);
            for(ImageView img : list){
                img.setBackground(null);
            }
            list.get(index).setBackground(MainActivity1.this.getApplicationContext().getResources().getDrawable(R.drawable.image_view_border));
        }
    }
}
