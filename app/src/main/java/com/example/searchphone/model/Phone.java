package com.example.searchphone.model;
//model层
/*
存储手机号信息
 */
public class Phone {
    private String telString;//手机号
    private String province;//归属地
    private String catName;//运营商
    private String carrier;//归属运营商

    public String getTelString() {
        return telString;
    }

    public void setTelString(String telString) {
        this.telString = telString;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }



}
