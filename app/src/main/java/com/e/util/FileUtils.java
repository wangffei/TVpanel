package com.e.util;

import android.content.Context;
import android.os.Environment;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

public class FileUtils {
    /**
     * 寻找sd卡的路径
     * @param context 当前activity对象
     * @return
     */
    public static String findSDcard(Context context){
        String path = null ;
        File file = Environment.getExternalStorageDirectory() ;
        File[] files = file.listFiles() ;
        for(File f : files){
            System.out.println(f);
        }
        path = files.length == 0 ? Environment.getDataDirectory().getAbsolutePath() : files[0].getAbsolutePath() ;
        return path ;
    }

    /**
     * 获取内部存储
     * @param context 当前activity对象
     * @return
     */
    public static String getSelfDirectory(Context context){
        String path = null ;
        path = Environment.getDataDirectory().getAbsolutePath() ;
        return path ;
    }

    /**
     * 使用字符流读取文件
     * @param file 文件路径
     * @return 文件内容
     * @throws Exception
     */
    public static String readFile2Buf(String file) throws Exception {
        BufferedReader reader = null ;
        StringBuffer buf = null ;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file) , "utf-8")) ;
            buf = new StringBuffer() ;
            String line = null ;

            while((line = reader.readLine()) != null){
                buf.append(line) ;
            }
        } finally {
            if(reader != null){
                reader.close();
            }
        }
        return buf.toString() ;
    }

    /**
     * 读取文件，返回json对象
     * @param file 文件路径
     * @return 返回json
     * @throws Exception
     */
    public static JSONObject readFile2Json(String file) throws Exception {
        String data = readFile2Buf(file) ;
        JSONObject jsonObject = JSONObject.parseObject(data) ;

        return jsonObject ;
    }

    /**
     * 根据路径获取文件io对象
     * @param file
     * @return
     * @throws Exception
     */
    private static InputStream getInputStream(String file) throws FileNotFoundException {
        InputStream in = new FileInputStream(file) ;
        return in ;
    }

    public static byte[] readFile2Byte(String file) throws Exception {
        InputStream in = null ;
        ByteArrayOutputStream out = new ByteArrayOutputStream() ;

        try{
            in = getInputStream(file) ;
            int len = -1 ;
            byte[] data = new byte[1024] ;

            while((len = in.read(data)) != -1){
                out.write(data , 0 , len);
            }
        } finally {
            out.close();
            if(in != null){
                in.close();
            }
        }
        return out.toByteArray() ;
    }

    /**
     * 将字节数组写入文件
     * @param file
     * @param data
     * @throws IOException
     */
    public static void writeFile2Byte(String file , byte[] data) throws IOException {
        OutputStream out = null ;
        try{
            out = new FileOutputStream(file) ;
            out.write(data);
        } finally {
            if(out != null){
                out.close();
            }
        }
    }

    /**
     * 将字符串写入文件
     * @param file
     * @param buf
     * @throws IOException
     */
    public static void writeFile2Buf(String file , String buf) throws IOException {
        BufferedWriter writer = null ;
        try{
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file) , "utf-8")) ;
            writer.write(buf);
        }finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private static Object lock = new Object() ;
    private static boolean flag = false ;
    public static byte[] download(final String path) {
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
}
