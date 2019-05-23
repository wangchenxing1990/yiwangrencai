package com.yiwangrencai.ywkj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.tools.UiUtils;
import com.yiwangrencai.ywkj.view.LoginOffPopuWindow;

import java.io.File;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by Administrator on 2017/5/6.
 */

public class SystemSetActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_system_setting;
    }

    @Override
    public void initData() {

    }

    private FrameLayout fram_job_back;
    private Button button_login_off;
    private RelativeLayout rl_mine_system_setting;
    private RelativeLayout rl_mine_system_settin;
    private RelativeLayout rl_mine_system_settingg;
    private RelativeLayout relative_current_verson;
    private TextView textviewcache;
    private TextView text_current_version;
    long fileSize = 0;
    String cacheSize = "0KB";

    @Override
    public void initView() {
        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        button_login_off = (Button) findViewById(R.id.button_login_off);
        rl_mine_system_setting = (RelativeLayout) findViewById(R.id.rl_mine_system_setting);
        rl_mine_system_settin = (RelativeLayout) findViewById(R.id.rl_mine_system_settin);
        rl_mine_system_settingg = (RelativeLayout) findViewById(R.id.rl_mine_system_settingg);
        relative_current_verson = (RelativeLayout) findViewById(R.id.relative_current_verson);
        textviewcache = (TextView) findViewById(R.id.textviewcache);
        text_current_version = (TextView) findViewById(R.id.text_current_version);
        fram_job_back.setOnClickListener(this);
        button_login_off.setOnClickListener(this);
        rl_mine_system_setting.setOnClickListener(this);
        rl_mine_system_settin.setOnClickListener(this);
        rl_mine_system_settingg.setOnClickListener(this);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = packageInfo.versionName;
            text_current_version.setText("V " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //缓存的大小
        File filesDir = getFilesDir();// /data/data/package_name/files
        File file = getCacheDir();
        fileSize += getDirSize(filesDir);
        fileSize += getDirSize(file);
        if (fileSize > 0) {
            cacheSize = formatFileSize(fileSize);
        }
        textviewcache.setText(cacheSize);

    }

    private String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    private long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back://返回
                finish();
                break;
            case R.id.rl_mine_system_setting://修改密码
                Intent intent = new Intent(SystemSetActivity.this, EditSecretActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_mine_system_settin://清理缓存
                clearAppCaches();
                //    deleteFilesByDirectory(SystemSetActivity.this.getCacheDir());
                break;
            case R.id.rl_mine_system_settingg://关于我们
                Intent intentUs = new Intent(SystemSetActivity.this, AboutUsACtivity.class);
                startActivity(intentUs);
                break;
            case R.id.button_login_off://退出登录
                showPopuwindow();
                break;
            case R.id.tv_off_cancel://弹出框的取消按钮
                loginOffWindow.dismiss();
                break;
            case R.id.tv_off_sure://弹出框得确认按钮
                logOff();
                break;
        }
    }


    public void clearAppCaches() {
        //final AppContext ac = (AppContext) activity.getApplication();
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    //  ToastMessage(ac, "缓存清除成功");
//                    Toast.makeText(SystemSetActivity.this,"缓存清除成功",Toast.LENGTH_SHORT).show();
                    textviewcache.setText("0KB");

                } else {
                    //ToastMessage(ac, "缓存清除失败");
                }
            }
        };
        new Thread() {
            public void run() {
                Message msg = new Message();
                try {
                    clearAppCache();
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
        //清除webview缓存
        //  @SuppressWarnings("deprecation")
        // File file = CacheManager.getCacheFileBaseDir();

        //先删除WebViewCache目录下的文件

//        if (file != null && file.exists() && file.isDirectory()) {
//            for (File item : file.listFiles()) {
//                item.delete();
//            }
//            file.delete();
//        }
//        deleteDatabase("webview.db");
//        deleteDatabase("webview.db-shm");
//        deleteDatabase("webview.db-wal");
//        deleteDatabase("webviewCache.db");
//        deleteDatabase("webviewCache.db-shm");
//        deleteDatabase("webviewCache.db-wal");
        //清除数据缓存
        clearCacheFolder(getFilesDir(), System.currentTimeMillis());
        clearCacheFolder(getCacheDir(),System.currentTimeMillis());
        //2.2版本才有将应用缓存转移到sd卡的功能
//        if(isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)){
//            clearCacheFolder(getExternalCacheDir(this),System.currentTimeMillis());
//        }

    }

    /**
     * 清除缓存目录
     *
     * @param dir     目录
     * @param curTime 当前系统时间
     * @return
     */
    private int clearCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    private LoginOffPopuWindow loginOffWindow;

    /**
     * 显示是否退出选项按钮
     */
    private void showPopuwindow() {

        loginOffWindow = new LoginOffPopuWindow(SystemSetActivity.this, R.layout.login_off_popuwindown);
        View view = loginOffWindow.getView();
        loginOffWindow.showAtLocation(SystemSetActivity.this.findViewById(R.id.system_setting_popuwind), Gravity.CENTER, 0, 0);
        TextView tv_off_cancel = (TextView) view.findViewById(R.id.tv_off_cancel);
        TextView tv_off_sure = (TextView) view.findViewById(R.id.tv_off_sure);
        tv_off_cancel.setOnClickListener(this);
        tv_off_sure.setOnClickListener(this);

    }

    /**
     * 退出登录
     */
    private void logOff() {

        SharedPreferences share = getSharedPreferences("Activity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("api_token", "");
        editor.commit();

        SharedPreferences shares = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorrs = shares.edit();
        editorrs.putString("id", "");
        editorrs.putString("username", "");
        editorrs.putString("mobile", "");
        editorrs.putString("degree1", "");
        editorrs.putString("chkphoto_open", "");
        editorrs.putString("resume_status", "");
        editorrs.putString("resume_id", "");
        editorrs.putString("name", "");
        editorrs.putString("resume_mobile", "");
        editorrs.putString("job_status", "");
        editorrs.putString("avatar", "");
        editorrs.putString("email", "");
        editorrs.putString("sex", "");
        editorrs.putString("job_status", "");
        editorrs.putString("part_resume_id", "");
        editorrs.putString("resume_eid", "");
        editorrs.putString("chat_pwd", "");
        editorrs.commit();
        JMessageClient.logout();
        Intent intent = new Intent(SystemSetActivity.this, MainActivity.class);
        intent.putExtra("register", "offLogin");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        loginOffWindow.dismiss();

        //  Toast.makeText(SystemSetActivity.this, "退出登录成功" + edi, Toast.LENGTH_SHORT).show();

    }
}
