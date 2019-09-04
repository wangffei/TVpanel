package com.e.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e.interfaces.NavigationBar;
import com.e.tvpanel.R;
import com.e.util.NavigationBarUtil;
import com.e.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class NavigationBarTxtImpl implements NavigationBar {
    private NavigationBarUtil instance = NavigationBarUtil.getInstance() ;

    private Context context ;

    private List<TextView> list = new ArrayList<>() ;

    /**
     * 纯图标类型导航栏
     * @param jsonObject
     */
    @Override
    public void initNavigationBar(Context context , JSONObject jsonObject , String server , RelativeLayout bottom) {
        System.out.println("laile");
        this.context = context ;
        JSONArray array = jsonObject.getJSONArray("data") ;

        LinearLayout linearLayout = new LinearLayout(context) ;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.bar_txt_Lineay_width) , context.getResources().getDimensionPixelOffset(R.dimen.bar_txt_Lineay_height)) ;

        for(int i=0 ; i<array.size() ; i++){
            String name = array.getJSONObject(i).getString("name") ;
            System.out.println(name);
            TextView txt = new TextView(context) ;
            txt.setText(name) ;
            txt.setTextSize(24);
//            txt.setLineHeight(24);
            txt.setTextColor(context.getResources().getColor(R.color.txt));
            txt.setShadowLayer(6 , 4 , 4 , context.getResources().getColor(R.color.txt));
            list.add(txt) ;

            txt.setOnClickListener(new MyEvent(jsonObject , i));

            if(i == 0){
                txt.setTextSize(28);
//                txt.setLineHeight(28);
            }

            LinearLayout item = new LinearLayout(context) ;
//            item.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            item.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
            item.addView(txt , new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT));

            linearLayout.addView(item , new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT  , ViewGroup.LayoutParams.WRAP_CONTENT , 1)) ;
        }

        bottom.addView(linearLayout , layoutParams) ;
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
            for(TextView txt : list){
                txt.setTextSize(24);
//                txt.setLineHeight(24);
            }
            list.get(index).setTextSize(28);
//            list.get(index).setLineHeight(28);
            instance.initContent(this.obj , this.index);
        }
    }
}
