package com.example.searchphone.mvp.impl;

import android.content.Context;

public class BasePresenter {
    Context mcontext;
    public void attch(Context context){
        mcontext=context;
    }
    public void onPause(){}
    public void onResume(){}
    public void onDestroy(){
       mcontext=null;
    }
}
