package com.e.control;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.e.impl.ViewHandlerImpl1;
import com.e.impl.ViewHandlerImpl2;
import com.e.impl.ViewHandlerImpl3;
import com.e.impl.ViewHandlerImpl4;
import com.e.impl.ViewHandlerImpl5;
import com.e.impl.ViewHandlerImpl6;
import com.e.impl.ViewHandlerImpl7;
import com.e.interfaces.ViewHandler;
import com.e.tvpanel.R;
import com.e.util.KVMap;

import org.xutils.ex.DbException;

public class DispatcherClass {

    private JSONObject jsonObject ;

    private String language ;

    private Context context ;

    public DispatcherClass(Context context , JSONObject jsonObject , String language){
        this.jsonObject = jsonObject ;
        this.language = language ;
        this.context = context ;
    }

    public void handler() throws DbException {
        JSONObject obj = jsonObject.getJSONObject("data").getJSONObject(language).getJSONObject("desktop") ;
        ViewHandler handler = null ;
        KVMap map = KVMap.getInstance() ;
        // 有导航栏
        if(obj.getJSONObject("main").getBoolean("exist")){
            String pos = obj.getJSONObject("main").getString("pos") ;
            if(pos != null && pos.trim().equals("b")){
                map.put("type" , "1");
                handler = new ViewHandlerImpl1() ;
            }else if(pos != null && pos.trim().equals("t")){
                String pos_pmd = obj.getJSONObject("pmd").getString("pos") ;
                String title = obj.getJSONObject("pmd").getString("title") ;
                // 跑马灯在下面
                if(title != null && !title.trim().equals("") && pos_pmd != null && pos_pmd.trim().equals("b")){
                    map.put("type" , "2");
                    handler = new ViewHandlerImpl2() ;
                }else{ //跑马灯在上面或者不存在
                    map.put("type" , "3");
                    handler = new ViewHandlerImpl3() ;
                }
            }else if(pos != null && pos.trim().equals("l")){
                String pos_pmd = obj.getJSONObject("pmd").getString("pos") ;
                String title = obj.getJSONObject("pmd").getString("title") ;
                // 跑马灯在下面
                if(title != null && !title.trim().equals("") && pos_pmd != null && pos_pmd.trim().equals("b")){
                    map.put("type" , "4");
                    handler = new ViewHandlerImpl4() ;
                    //暂不处理
                }else{ //跑马灯在上面或者不存在
                    map.put("type" , "5");
                    //暂不处理
                    handler = new ViewHandlerImpl5() ;
                }
            }
        }else{ //没有导航栏
            String pos = obj.getJSONObject("pmd").getString("pos") ;
            // 跑马灯在下面
            if(pos != null && pos.trim().equals("b")){
                // 没有导航栏，跑马灯在下面
                map.put("type" , "6");
                handler = new ViewHandlerImpl6() ;
            }else{ //跑马灯在上面或者不存在
                // 没有导航栏，跑马灯不在下面
                map.put("type" , "7");
                handler = new ViewHandlerImpl7() ;
            }
        }

        System.out.println(map.get("type"));
        handler.init(context , jsonObject , language);
        handler.doHandler();
    }
}
