package com.e.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e.interfaces.NavigationBar;
import com.e.tvpanel.MainActivity1;
import com.e.tvpanel.R;
import com.e.util.NavigationBarUtil;
import com.e.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class NavigationBarIconImpl implements NavigationBar {

    private NavigationBarUtil instance = NavigationBarUtil.getInstance() ;

    private Context context ;

    private List<ImageView> list = new ArrayList<>() ;

    /**
     * 纯图标类型导航栏
     * @param jsonObject
     */
    @Override
    public void initNavigationBar(Context context , JSONObject jsonObject , String server , RelativeLayout bottom) {
        this.context = context ;
        JSONArray array = jsonObject.getJSONArray("data") ;

        LinearLayout linearLayout = new LinearLayout(context) ;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.bar_icon_Lineay_width) , context.getResources().getDimensionPixelOffset(R.dimen.bar_icon_Lineay_height)) ;
        //linearLayout.setBackgroundColor(context.getResources().getColor(R.color.logo));

        for(int i=0 ; i<array.size() ; i++){
            String url = server + array.getJSONObject(i).getString("icon").substring(1) ;
            System.out.println(url);
            Bitmap bit = ViewUtil.getImage(url) ;
            ImageView img = new ImageView(context) ;
            img.setScaleType(ImageView.ScaleType.CENTER);
            img.setImageBitmap(bit);
            list.add(img) ;

            img.setOnClickListener(new MyEvent(jsonObject , i));

            if(i == 0){
                img.setBackground(context.getApplicationContext().getResources().getDrawable(R.drawable.image_view_border));
            }

            LinearLayout item = new LinearLayout(context) ;
            item.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
            item.addView(img , new LinearLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.bar_icon__width) , context.getResources().getDimensionPixelOffset(R.dimen.bar_icon__height)));

            linearLayout.addView(item , new LinearLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.bar_icon__width)+16 , context.getResources().getDimensionPixelOffset(R.dimen.bar_icon__height) , 1)) ;
        }

        bottom.addView(linearLayout , layoutParams);
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
            for(ImageView img : list){
                img.setBackground(null);
            }
            list.get(index).setBackground(context.getApplicationContext().getResources().getDrawable(R.drawable.image_view_border));
            instance.initContent(this.obj , this.index);
        }
    }
}
