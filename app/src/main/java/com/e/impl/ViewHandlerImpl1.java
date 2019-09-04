package com.e.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.e.interfaces.Consumer;
import com.e.interfaces.ViewHandler;
import com.e.tvpanel.R;
import com.e.util.NavigationBarUtil;
import com.e.util.ViewUtil;

/**
 * @desc 导航栏在下边，跑马灯在上边
 */
public class ViewHandlerImpl1 extends ViewHandler {

    private JSONObject jsonObject ;

    private String language ;

    private Context context ;

    /**
     * 初始化界面
     * @param context
     * @param jsonObject
     * @param language
     */
    @Override
    public void init(Context context , JSONObject jsonObject , String language) {
        server = jsonObject.getJSONObject("data").getString("SERVER") ;
        this.jsonObject = jsonObject.getJSONObject("data").getJSONObject(language).getJSONObject("desktop") ;
        this.language = language ;
        this.context = context ;
    }

    @Override
    public void doHandler() {
        final RelativeLayout top = new RelativeLayout(context) ;
        final RelativeLayout bottom = new RelativeLayout(context) ;
        final RelativeLayout center = new RelativeLayout(context) ;
        final RelativeLayout bottomPmd = new RelativeLayout(context) ;

        initView(context , this.jsonObject , top , center , bottom , bottomPmd);

        if(logo != null){
            Consumer<Bitmap> fun = new Consumer<Bitmap>(){
                @Override
                public void accept(Bitmap bitmap) {
                    RelativeLayout.LayoutParams param_logo = new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.logo_width) , context.getResources().getDimensionPixelOffset(R.dimen.logo_height)) ;
                    param_logo.leftMargin = context.getResources().getDimensionPixelOffset(R.dimen.logo_margin_left) ;
                    logo.setLayoutParams(param_logo) ;
                    top.addView(logo);

                    ImageView img = new ImageView(context) ;
                    img.setImageBitmap(bitmap);
                    img.setScaleType(ImageView.ScaleType.FIT_XY);
                    img.setMaxWidth(context.getResources().getDimensionPixelOffset(R.dimen.logo_width));
                    img.setMaxHeight(context.getResources().getDimensionPixelOffset(R.dimen.logo_height));

                    logo.addView(img);
                }
            } ;
            ViewUtil.getImage(server + ViewHandlerImpl1.this.jsonObject.getJSONObject("logo").getString("img").substring(1) , fun);
        }

        if(content != null){
            RelativeLayout.LayoutParams vl = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT) ;
            center.addView(content , vl);
            initContentByIndex(context , jsonObject.getJSONObject("main") , 0 , server , content);
        }

        if(bars != null){
            NavigationBarUtil.getInstance().init(context , this.jsonObject.getJSONObject("main") , server , bottom , content);
        }
    }
}
