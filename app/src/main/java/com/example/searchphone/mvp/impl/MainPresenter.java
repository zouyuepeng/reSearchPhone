package com.example.searchphone.mvp.impl;

import com.example.searchphone.business.HttpUntil;
import com.example.searchphone.model.Phone;
import com.example.searchphone.mvp.MvpMainView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
处理activity的逻辑
 */
public class MainPresenter extends BasePresenter{
    MvpMainView mvpMainView;
    String murl="https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
    Phone mphone;

    public MainPresenter(MvpMainView mainView){
        mvpMainView=mainView;
    }


    public Phone getPhoneInfo(){
        return mphone;
    }

    //获取手机号码
    public void searchPhoneInfo(String phone){
        if(phone.length()!=11){
            mvpMainView.showToast("请输入正确的手机号码");
            return;
        }
        //如果加载成功，泽获取网络数据
        mvpMainView.showLoading();
        //写http请求处理逻辑
        sendHttp(phone);
    }

    private void sendHttp(String phone) {
        Map<String,String> map=new HashMap<String, String>();
        map.put("tel",phone);
        HttpUntil httpUntil=new HttpUntil(new HttpUntil.HttpResponse() {
            @Override
            public void onSuccess(Object object) {
                String json=object.toString();
                int index=json.indexOf("{");
                json=json.substring(index,json.length());

                //使用jsonobject解析
                mphone=parseModelWithorgJson(json);
//                //Gson
//                mphone=parseModelWithGson(json);
//                //fastjson
//                mphone=parseModelWithFastJson(json);

                mvpMainView.hineLoading();
                mvpMainView.updateView();
            }

            @Override
            public void onFail(String error) {
                mvpMainView.showToast(error);
                mvpMainView.hineLoading();
            }
        });
        httpUntil.sendGetHttp(murl,map);
    }
    private Phone parseModelWithorgJson(String json){
        Phone phone=new Phone();
        try {
            JSONObject jsonObject=new JSONObject(json);
            String value=jsonObject.getString("telString");
            phone.setTelString(value);

            value=jsonObject.getString("province");
            phone.setProvince(value);

            value=jsonObject.getString("catName");
            phone.setCatName(value);

            value=jsonObject.getString("carrier");
            phone.setCarrier(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return phone;
    }
    private Phone parseModelWithGson(String json){
        Gson gson=new Gson();
        Phone phone=gson.fromJson(json,Phone.class);
        return phone;
    }
    private Phone parseModelWithFastJson(String json){
        Phone phone= com.alibaba.fastjson.JSONObject.parseObject(json,Phone.class);
        return phone;
    }
}
