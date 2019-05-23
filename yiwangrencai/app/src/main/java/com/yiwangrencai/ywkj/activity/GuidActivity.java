package com.yiwangrencai.ywkj.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.content.ContentUrl;

import java.io.File;
import java.io.IOException;

public class GuidActivity extends AppCompatActivity {

    int oldVersionCode;
    String oldVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guid);

        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            oldVersionCode = packageInfo.versionCode;
            oldVersionName = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        gainNewVersion();
        //欢迎的时间的总时间
//        Intent intent=new Intent(GuidActivity.this,MainActivity.class);
//        startActivity(intent);

    }

    /**
     * 获取新的版本号
     */
    int code;
    String string;
    private static final int EMPTY_SUCCESS=1;
    private void gainNewVersion() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.USER_CHECK_VERSION)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .post(form.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
               if(e.getMessage()!=null){
                   handler.sendEmptyMessage(EMPTY_SUCCESS);
               }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                string = response.body().string();
                code = response.code();
                Log.i("stringstring", string);
                Log.i("codecodecode", code + "");
                handler.sendEmptyMessage(130);
            }
        });
    }

    private final static int GUIDER_TIME = 1000;
    String versionCodeNew;
    String versionName;
    String url;
    String changeLog;
    String updateTips;
    String forcedUpdate;
    ProgressDialog progressDialog;
     File file;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case GUIDER_TIME:

                    /**
                     * 开启一个activity由此进入主界面首页界面
                     */
                    break;
                case EMPTY_SUCCESS:
                    SystemClock.sleep(3000);
                    Intent intentFaith=new Intent(GuidActivity.this,MainActivity.class);
                    startActivity(intentFaith);
                    break;
                case 130:
                    Log.i("stringstring2222", string);
                    if (code == 200) {
                        JSONObject jsonObject = JSON.parseObject(string);
                        versionCodeNew = jsonObject.getString("versionCode");
                        versionName = jsonObject.getString("versionName");
                        url = jsonObject.getString("url");
                        changeLog = jsonObject.getString("changeLog");
                        updateTips = jsonObject.getString("updateTips");
                        forcedUpdate = jsonObject.getString("forcedUpdate");

                        int newVersionCode = Integer.parseInt(versionCodeNew);
                        if (newVersionCode > oldVersionCode) {
                            //有版本更新弹出对话框是否现在
                            AlertDialog.Builder builder = new AlertDialog.Builder(GuidActivity.this);
                            builder.setMessage(changeLog);
                            builder.setTitle(updateTips);
                            file = new File(getExternalCacheDir().toString(),"yiwang.apk");
                            Log.i("userr",file+"");
                            if (!file.exists()){
                                file.mkdirs();
                            }else{
                                file.delete();
                            }

                            Log.i("userr",file+"");

                            try {
                                if (file.exists()){
                                    file.delete();
                                }
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("onStart11","开始下载");
                                    //进行版本更新 //
                                    HttpUtils httpUtils = new HttpUtils();
                                    httpUtils.download(url, file+"", new RequestCallBack<File>() {
                                        @Override
                                        public void onStart() {
                                            super.onStart();
                                            //Toast.makeText(GuidActivity.this,"点击确认下载",Toast.LENGTH_SHORT).show();
                                            Log.i("onStart22","开始下载");
                                            //显示进度条对话框
                                            progressDialog = new ProgressDialog(GuidActivity.this);
                                            progressDialog.setTitle("一网人才");
                                            progressDialog.setMessage("正在下载...");
                                            progressDialog.setProgressNumberFormat(" ");
                                            //可以看到进度的过程，水平的样式
                                            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                            progressDialog.setCanceledOnTouchOutside(false);
                                            //一定要显示出来
                                            progressDialog.show();
                                        }

                                        @Override
                                        public void onLoading(long total, long current, boolean isUploading) {
                                            super.onLoading(total, current, isUploading);
                                           // Toast.makeText(GuidActivity.this,"点击onLoading下载",Toast.LENGTH_SHORT).show();
                                            Log.i("onLoading","正在下载");
                                            if (progressDialog != null) {
                                                Log.i("ints",total+"");
                                                //设置最大进度
                                                progressDialog.setMax((int) total);
                                                //设置当前进度
                                                progressDialog.setProgress((int) current);
                                                Log.i("currentll",current+"");
                                            }
                                        }

                                        @Override
                                        public void onSuccess(ResponseInfo<File> responseInfo) {
                                          //  Toast.makeText(GuidActivity.this,"点击onSuccess下载",Toast.LENGTH_SHORT).show();
                                            Log.i("onSuccess","下载成功");
                                            if (progressDialog != null) {
                                                progressDialog.dismiss();
                                            }

                                            Log.i("result",responseInfo.result.length()+"");
                                            //这个就是目标下载文件
                                           // File apkFile = responseInfo.result;
                                           handler.sendEmptyMessage(190);
                                        }

                                        @Override
                                        public void onFailure(HttpException error, String msg) {

                                            if (progressDialog != null) {
                                                progressDialog.dismiss();
                                            }

                                       //  Toast.makeText(GuidActivity.this,"点击onFailure下载",Toast.LENGTH_SHORT).show();
                                            Log.i("onFailure","下载失败");
                                            Intent intent = new Intent(GuidActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);

                                            finish();
                                        }
                                    });

                                }
                            });

                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    if ("1".equals(forcedUpdate)) {
                                      finish();
                                    } else if ("0".equals(forcedUpdate)){
                                        Intent intent = new Intent(GuidActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                        //GuidActivity.this.finish();
                                    }
                                }
                            });
                            builder.create().setCanceledOnTouchOutside(false);
                            builder.create().show();
                        } else {
                            //没有版本更新
                            Log.i("nonono","没有版本更新");

                            SystemClock.sleep(1000);

                            Intent intent = new Intent(GuidActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(GuidActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                case 190:
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if (Build.VERSION.SDK_INT>=24){
                        Uri apkUri =
                                FileProvider.getUriForFile(GuidActivity.this, "com.yiwangrencai.fileprovider", file);
                        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    }else{
                        Uri fileUri = Uri.fromFile(file);
                        intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
//                    startActivityForResult(intent, 100);
                    break;

            }
        }
    };

    //这个方法是用来接受B界面的返回值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            //代表是我的界面开启的安装界面
            switch (resultCode) {
                case GuidActivity.RESULT_OK:
                    //用户点击确定按钮
                    break;

//                case GuidActivity.RESULT_CANCELED:
//
//                    //用户点击取消按妞
//                    System.out.println("用户取消安装");
//                    Intent intent=new Intent(GuidActivity.this,MainActivity.class);
//                    startActivity(intent);

//                    break;

                default:
                    break;
            }
        }

    }

}
