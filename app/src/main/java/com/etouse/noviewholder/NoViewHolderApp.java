package com.etouse.noviewholder;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Administrator on 2017/6/27.
 */

public class NoViewHolderApp extends Application {
    //全局上下文
    public static Context mContext;
    //全局Handler
    public static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
