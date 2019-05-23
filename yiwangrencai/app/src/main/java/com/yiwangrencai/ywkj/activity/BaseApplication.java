package com.yiwangrencai.ywkj.activity;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.hss01248.dialog.StyledDialog;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by Administrator on 2017/4/8.
 */
public class BaseApplication extends Application {
    private static BaseApplication application;
    private static int mainTid;
    private static Handler handler;

    @Override
//  在主线程运行的
    public void onCreate() {
        super.onCreate();
         application=this;
         ZXingLibrary.initDisplayOpinion(this);
         StyledDialog.init(this);
        // initEMOption();
        JMessageClient.setDebugMode(true);
        JMessageClient.init(this);
        mainTid = android.os.Process.myTid();
        handler=new Handler();
    }

    public static Context getApplication() {

        return application;
    }
    public static int getMainTid() {
        return mainTid;
    }
    public static Handler getHandler() {
        return handler;
    }

    private void initEMOption() {
//        EMOptions options = new EMOptions();
//        // 默认添加好友时，是不需要验证的，改成需要验证
//        options.setAcceptInvitationAlways(false);
//        EaseUI.getInstance().init(this, options);
       // EMClient.getInstance().setDebugMode(true);
     //EMClient.getInstance().addClientListener();
    }

}
