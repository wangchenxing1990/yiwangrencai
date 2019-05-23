package com.yiwangrencai.JpushReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.EditResumeTwoActivity;
import com.yiwangrencai.ywkj.activity.InterviewActivity;

import cn.jpush.android.api.JPushInterface;

import static cn.jpush.android.api.JPushInterface.getRegistrationID;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Bundle bundle=intent.getExtras();

        Log.d("TAG", "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " );
      //  throw new UnsupportedOperationException("Not yet implemented");
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d("TAG", "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d("TAG", "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            //processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d("TAG", "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d("TAG", "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d("TAG", "[MyReceiver] 用户点击打开了通知");
//
//          //打开自定义的Activity
          Intent i = new Intent(context, InterviewActivity.class);
        //  i.putExtras(bundle);
          //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
          context.startActivity(i);
            Log.e("Jpush","用户点击打开了通知");
            Toast.makeText(context,"用户点击打开了通知",Toast.LENGTH_SHORT).show();

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d("TAG", "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w("TAG", "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
            Log.d("TAG", "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }
}
