package com.e.interfaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e.tvpanel.R;
import com.e.util.KVMap;
import com.e.util.ViewUtil;

public abstract class ViewHandler {
    protected RelativeLayout logo ;
    protected RelativeLayout clock ;
    public AbsoluteLayout content ;
    public RelativeLayout bars ;
    protected RelativeLayout pmd ;
    public String server ;
    public abstract void init(Context context , JSONObject jsonObject , String language) ;
    public abstract void doHandler() ;

    protected void initView(Context context , final JSONObject jsonObject , RelativeLayout top , RelativeLayout center , RelativeLayout bottom , RelativeLayout bottomPmd){
        if(jsonObject.getJSONObject("logo").getString("img") != null){
            logo = new RelativeLayout(context) ;
        }
        if(jsonObject.getJSONObject("clock").getString("pos") != null){
            clock = new RelativeLayout(context) ;
        }
        if(jsonObject.getJSONObject("main").getJSONArray("data").size() != 0){
            content = new AbsoluteLayout(context) ;
        }
        if(jsonObject.getJSONObject("main").getBoolean("exist")){
            bars = new RelativeLayout(context) ;
        }
        if(jsonObject.getJSONObject("pmd").getString("title") != null && !jsonObject.getJSONObject("pmd").getString("title").trim().equals("")){
            pmd = new RelativeLayout(context) ;
        }
        if(jsonObject.getString("bg") != null && !jsonObject.getString("bg").trim().equals("")) {
            Consumer<Bitmap> fun = new Consumer<Bitmap>(){
                @Override
                public void accept(Bitmap bitmap) {
                    ImageView bg = (ImageView) ViewUtil.findById(R.id.bg);
                    // 下载图片
                    bg.setImageBitmap(bitmap);
                }
            } ;
            ViewUtil.getImage(server + jsonObject.getString("bg").substring(1) , fun);
        }

        //初始化布局
        LinearLayout linearLayout = (LinearLayout) ViewUtil.findById(R.id.panel) ;

        top.setVerticalGravity(Gravity.CENTER);
        center.setHorizontalGravity(Gravity.CENTER);
        bottom.setHorizontalGravity(Gravity.CENTER);
        bottomPmd.setHorizontalGravity(Gravity.CENTER);
        bottomPmd.setVerticalGravity(Gravity.CENTER);

        String type = jsonObject.getJSONObject("main").getString("type") ;
        KVMap map = KVMap.getInstance() ;
        if(type != null && type.equals("bar_icon")) {
            if(map.get("type") != null && map.get("type").equals("1")){
                top.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.top_width1_icon) , context.getResources().getDimensionPixelOffset(R.dimen.top_height1_icon)));
                center.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.center_width1_icon) , context.getResources().getDimensionPixelOffset(R.dimen.center_height1_icon)));
                bottom.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.bottom_width1_icon) , context.getResources().getDimensionPixelOffset(R.dimen.bottom_height1_icon)));
            }
        }

        if(type != null && type.equals("bar_txt")){
            if(map.get("type") != null && map.get("type").equals("1")){
                top.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.top_width1_txt) , context.getResources().getDimensionPixelOffset(R.dimen.top_height1_txt)));
                center.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.center_width1_txt) , context.getResources().getDimensionPixelOffset(R.dimen.center_height1_txt)));
                bottom.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.bottom_width1_txt) , context.getResources().getDimensionPixelOffset(R.dimen.bottom_height1_txt)));
            }
            if(map.get("type") != null && map.get("type").equals("2")){
                top.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.top_width2_txt) , context.getResources().getDimensionPixelOffset(R.dimen.top_height2_txt)));
                center.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.center_width2_txt) , context.getResources().getDimensionPixelOffset(R.dimen.center_height2_txt)));
                bottom.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.bottom_width2_txt) , context.getResources().getDimensionPixelOffset(R.dimen.bottom_height2_txt)));
                bottomPmd.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.bottom_pmd_width2_txt) , context.getResources().getDimensionPixelOffset(R.dimen.bottom_pmd_height2_txt)));
            }
            if(map.get("type") != null && map.get("type").equals("3")){
                top.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.top_width3_txt) , context.getResources().getDimensionPixelOffset(R.dimen.top_height3_txt)));
                center.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.center_width3_txt) , context.getResources().getDimensionPixelOffset(R.dimen.center_height3_txt)));
                bottom.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.bottom_width3_txt) , context.getResources().getDimensionPixelOffset(R.dimen.bottom_height3_txt)));
            }
        }

        if(map.get("type") != null && map.get("type").equals("6")){
            top.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.top_width6) , context.getResources().getDimensionPixelOffset(R.dimen.top_height6)));
            center.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.center_width6) , context.getResources().getDimensionPixelOffset(R.dimen.center_height6)));
            bottom.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.bottom_width6) , context.getResources().getDimensionPixelOffset(R.dimen.bottom_height6)));
        }

        if(map.get("type") != null && map.get("type").equals("7")){
            top.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.top_width7) , context.getResources().getDimensionPixelOffset(R.dimen.top_height7)));
            center.setLayoutParams(new RelativeLayout.LayoutParams(context.getResources().getDimensionPixelOffset(R.dimen.center_width7) , context.getResources().getDimensionPixelOffset(R.dimen.center_height7)));
        }

//        top.setBackground(context.getResources().getDrawable(R.color.colorAccent));
//        center.setBackground(context.getResources().getDrawable(R.color.colorPrimary));
//        bottom.setBackground(context.getResources().getDrawable(R.color.colorPrimaryDark));

        linearLayout.addView(top);
        linearLayout.addView(center);
        linearLayout.addView(bottom);
        linearLayout.addView(bottomPmd);
    }

    protected void initContentByIndex(final Context context , JSONObject jsonObject , int index , String server ,final AbsoluteLayout content){
        content.removeAllViews();

        JSONArray temp = jsonObject.getJSONArray("data") ;
        final JSONArray layout = temp.getJSONObject(index).getJSONObject("content").getJSONObject("layout").getJSONObject(context.getString(R.string.fbl)).getJSONArray("1") ;
        JSONArray contents = temp.getJSONObject(index).getJSONObject("content").getJSONArray("info") ;
        for(int j=0 ; j<contents.size() ; j++){
            final int m = j ;
            String url = server + contents.getJSONObject(j).getJSONArray("imgs").getString(0).substring(1) ;
            Consumer<Bitmap> fun = new Consumer<Bitmap>(){
                @Override
                public void accept(Bitmap bitmap) {
                    ImageView img = new ImageView(context) ;
                    img.setImageBitmap(bitmap);
                    img.setScaleType(ImageView.ScaleType.FIT_XY);
                    //img.setVisibility(View.GONE);

                    AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
                            layout.getJSONObject(m).getInteger("width") ,
                            layout.getJSONObject(m).getInteger("height") ,
                            layout.getJSONObject(m).getInteger("left") ,
                            layout.getJSONObject(m).getInteger("top")
                    ) ;

//            System.out.println(layout.getJSONObject(j).getInteger("width"));
//            System.out.println(layout.getJSONObject(j).getInteger("height"));

                    content.addView(img , params);
                }
            } ;
            ViewUtil.getImage(url , fun);
        }
    }
}
