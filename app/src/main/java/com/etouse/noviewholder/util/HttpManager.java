package com.etouse.noviewholder.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sy_heima on 2016/9/13.
 */
public class HttpManager {


    private HttpManager() {

    }

    private static HttpManager sDownManager = new HttpManager();

    public static HttpManager getInstance() {
        return sDownManager;
    }

    //Get方式去获取数据
    public String dataGet(String url) {
        try {

            OkHttpClient okHttpClient = new OkHttpClient();
            Request reuqest = new Request.Builder()
                    .url(url)
                    .build();
            Response response = okHttpClient.newCall(reuqest).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            //这里必须返回
            return null;
        }
    }

    //Post方式去获取数据
    public String dataPost(String url, Map<String, String> map) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Response response = null;
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    //Get方式去获取数据
    public Response ResponseGet(String url) {
        try {

            OkHttpClient okHttpClient = new OkHttpClient();
            Request reuqest = new Request.Builder()
                    .url(url)
                    .build();
            Response response = okHttpClient.newCall(reuqest).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            //这里必须返回
            return null;
        }
    }

    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;

    }

}
