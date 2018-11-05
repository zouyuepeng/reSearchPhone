package com.example.searchphone.mvp;
/*
创建view接口层
 */

public interface MvpMainView extends MvpLoadingView {
    void showToast(String s);//显示信息
    void updateView();//更新界面


}
