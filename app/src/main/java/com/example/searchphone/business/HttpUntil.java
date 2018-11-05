package com.example.searchphone.business;

import android.os.Looper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUntil {
    String murl;
    Map<String,String> mparm;
    HttpResponse mhttpResponse;
    android.os.Handler handler=new android.os.Handler(Looper.getMainLooper());
    private final OkHttpClient client=new OkHttpClient();
    public interface HttpResponse{
        void onSuccess(Object object);
        void onFail(String error);
    }
    public HttpUntil(HttpResponse httpResponse){
        mhttpResponse=httpResponse;
    }

    public void sendPostHttp(String url, Map<String,String>parm){
        sendHttp(url,parm,true);
    }
    public void sendGetHttp(String url, Map<String,String>parm){
        sendHttp(url,parm,false);
    }
    private void sendHttp(String url, Map<String,String>parm,boolean isPost){
        murl=url;
        mparm=parm;
       //编写http请求逻辑
        run(isPost);
    }

    private void run(boolean isPost) {
        //创建request请求
        Request request=createRequest(isPost);
        //创建请求队列
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(mhttpResponse!=null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mhttpResponse.onFail("请求错误");
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if(mhttpResponse==null)return;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!response.isSuccessful()){
                            mhttpResponse.onFail("请求失败");
                        }else {
                            try {
                                mhttpResponse.onSuccess(response.body().string());
                            } catch (IOException e) {
                                mhttpResponse.onFail("结果转换失败");
                            }
                        }
                    }
                });
            }
        });
    }
    //post请求
    private Request createRequest(boolean isPost){
        Request request;
        if(isPost){
            MultipartBody.Builder requestBodyBuilder=new MultipartBody.Builder();
            requestBodyBuilder.setType(MultipartBody.FORM);
            //遍历请求参数
            Iterator<Map.Entry<String,String>> iterator=mparm.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String,String> entry=iterator.next();
                requestBodyBuilder.addFormDataPart(entry.getKey(),entry.getValue());
            }
            request=new okhttp3.Request.Builder().url(murl).post(requestBodyBuilder.build()).build();

        }else {
             String urlStr=murl+"?"+MapParmToString(mparm);
             request=new Request.Builder().url(urlStr).build();
        }
        return request;
    }
    private String MapParmToString(Map<String,String> parm){
        StringBuilder stringBuilder=new StringBuilder();
        Iterator<Map.Entry<String,String>> iterator=parm.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry=iterator.next();
            stringBuilder.append(entry.getKey()+"="+entry.getValue()+"&");
        }
        String str=stringBuilder.toString().substring(0,stringBuilder.length()-1);
        return str;
    }
}
