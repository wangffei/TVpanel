package com.e.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.e.adapter.HttpAdapter;
import com.e.interfaces.Consumer;
import com.e.tvpanel.MainActivity;
import com.e.tvpanel.R;

import org.xutils.http.RequestParams;
import org.xutils.x;

public class ViewUtil {

    public static Bitmap byte2Bitmap(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null)
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            else
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    public static View findById(int id){
        View view = MainActivity.getViewById(id) ;

        return view ;
    }

    public static Bitmap getImage(String url){
        byte[] data = FileUtils.download(url) ;
        Bitmap bit = ViewUtil.byte2Bitmap(data , new BitmapFactory.Options()) ;
        return bit ;
    }

    public static void getImage(String url , final Consumer<Bitmap> fun){
        x.http().get(new RequestParams(url) , new HttpAdapter<byte[]>(){
            @Override
            public void onSuccess(byte[] result) {
                Bitmap bit = ViewUtil.byte2Bitmap(result , new BitmapFactory.Options()) ;
                fun.accept(bit); ;
            }
        }) ;
    }

}
