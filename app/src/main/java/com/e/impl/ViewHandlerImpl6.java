package com.e.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.e.interfaces.ViewHandler;
import com.e.tvpanel.R;
import com.e.util.NavigationBarUtil;
import com.e.util.ViewUtil;

public class ViewHandlerImpl6 extends ViewHandler {

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
        LinearLayout linearLayout = (LinearLayout) ViewUtil.findById(R.id.panel) ;

        RelativeLayout top = new RelativeLayout(context) ;
        RelativeLayout bottom = new RelativeLayout(context) ;
        RelativeLayout center = new RelativeLayout(context) ;
        RelativeLayout bottomPmd = new RelativeLayout(context) ;

        initView(context , this.jsonObject , top , center , bottom , bottomPmd);

        if(logo != null){
            RelativeLayout.LayoutParams param_logo = new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.logo_width) , context.getResources().getDimensionPixelOffset(R.dimen.logo_height)) ;
            param_logo.leftMargin = context.getResources().getDimensionPixelOffset(R.dimen.logo_margin_left) ;
            logo.setLayoutParams(param_logo) ;
            top.addView(logo);

            ImageView img = new ImageView(context) ;
            String url = server + this.jsonObject.getJSONObject("logo").getString("img").substring(1) ;
            Bitmap bit = ViewUtil.getImage(url) ;
            img.setImageBitmap(bit);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            img.setMaxWidth(context.getResources().getDimensionPixelOffset(R.dimen.logo_width));
            img.setMaxHeight(context.getResources().getDimensionPixelOffset(R.dimen.logo_height));

            logo.addView(img);
        }

        if(content != null){
            RelativeLayout.LayoutParams vl = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT) ;
            center.addView(content , vl);
            initContentByIndex(context , jsonObject.getJSONObject("main") , 0 , server , content);
        }

        if(pmd != null){
            bottom.addView(pmd , new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.bottom_pmd_width) , context.getResources().getDimensionPixelOffset(R.dimen.bottom_pmd_height)));
            TextView txt = new TextView(context) ;
            txt.setText(this.jsonObject.getJSONObject("pmd").getString("title"));
            txt.setTextSize(20);
            txt.setTextColor(context.getResources().getColor(R.color.txt));
            pmd.addView(txt , new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }
}
