package com.yiwangrencai.ywkj.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hss01248.dialog.StyledDialog;
import com.yiwangrencai.R;
import com.yiwangrencai.wxapi.WXEntryActivity;
import com.yiwangrencai.ywkj.fragment.ConversationListFragment;
import com.yiwangrencai.ywkj.fragment.FindFragment;
import com.yiwangrencai.ywkj.fragment.HomeFragent;
import com.yiwangrencai.ywkj.fragment.MineFragent;
import com.yiwangrencai.ywkj.fragment.ResumeFragent;
import com.yiwangrencai.ywkj.jmessage.BaseActivity;
import com.yiwangrencai.ywkj.jmessage.StorageUtil;
import com.yiwangrencai.ywkj.view.DropZoomScrollView;
import com.yiwangrencai.ywkj.view.MyLocationListener;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;

import cn.jpush.android.api.JPushInterface;

import static cn.jpush.android.api.JPushInterface.getRegistrationID;

/**
 * Created by Administrator on 2017/3/31.
 */
public class MainActivity extends BaseActivity {

    private TextView text_notifiction_number;
    private List<Fragment> fragmentList = new ArrayList<>();
    //标记注册和登陆成功的
    private String api_token;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_avtivity_two);

        initData();
        initView();

    }

    /**
     * 初始化fragment
     */
    public void initData() {

        fragmentList.add(new HomeFragent(MainActivity.this));
        fragmentList.add(new ConversationListFragment());
//        fragmentList.add(new FindFragment(MainActivity.this));
        fragmentList.add(new ResumeFragent(MainActivity.this));
        fragmentList.add(new MineFragent(MainActivity.this));

    }

    /**
     * 进行动态获取权限
     */
    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                ) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, BAIDU_READ_PHONE_STATE);
        } else {
            initBD();//进行定位
        }
    }

    private static final int BAIDU_READ_PHONE_STATE = 100;

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    initBD();//进行定位
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 百度定位
     */
    private void initBD() {
        myListener = new MyLocationListener();

        mLocationClient = new LocationClient(MainActivity.this);
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();

        mLocationClient.start();

    }

    /**
     * 初始化view
     */
    private RadioGroup mRadioGroup;
    private RadioButton mHomeButton;
    private RadioButton mResumeButton;
    private RadioButton mMessageButton;
    private RadioButton mMineButton;
    private String resume_id;

    public void initView() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        StorageUtil.init(MainActivity.this, null);
        String device_token = getRegistrationID(this);

        SharedPreferences share = getSharedPreferences("data", MODE_PRIVATE);
        resume_id = share.getString("resume_id", "");
        Log.i("resume_idresume_id",resume_id+"111111");
        SharedPreferences.Editor edit = share.edit();
        edit.putString("device_token", device_token);
        edit.commit();


        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();//动态获取权限
        } else {
            initBD();//否则就进行定位
        }

        SharedPreferences sharedPreferences = this.getSharedPreferences("Activity", Context.MODE_PRIVATE);
        api_token = sharedPreferences.getString("api_token", "");

        text_notifiction_number = (TextView) findViewById(R.id.text_notifiction_number);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mHomeButton = (RadioButton) findViewById(R.id.radio_home);
        mResumeButton = (RadioButton) findViewById(R.id.radio_resume);
        mMessageButton = (RadioButton) findViewById(R.id.radio_message);
        mMineButton = (RadioButton) findViewById(R.id.radio_mine);

        mRadioGroup.setOnCheckedChangeListener(new MyRadioGroup());

        Intent intent = getIntent();
        String register = intent.getStringExtra("register");

        if (register != null && !"".equals(register)) {
            if (register.equals("register")) {//简历界面
                mResumeButton.setChecked(true);
            } else if (register.equals("loginMine")) {
                //switchFragment(3);
                mHomeButton.setChecked(true);
            } else if (register.equals("offMine")) {
                // switchFragment(0);
                Log.i("api_token_api_token_api_token_", api_token + "1111111111");
                mHomeButton.setChecked(true);
                Intent intentOff = new Intent(MainActivity.this, WXEntryActivity.class);
                intentOff.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentOff);
                finish();
            } else if (register.equals("interView")) {
                mMineButton.setChecked(true);
               //switchFragment(3);
            }else if(register.equals("offLogin")){
                mHomeButton.setChecked(true);
            }else if(register.equals("message")){
                mMessageButton.setChecked(true);
            }
        } else {
            mHomeButton.setChecked(true);
        }
    }

   private class MyRadioGroup implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radio_home://首页
                    switchFragment(0);
                    break;
                case R.id.radio_resume://简历
                    switchFragment(2);
                    break;
                case R.id.radio_message://消息
                    if (!api_token.isEmpty() && !resume_id.isEmpty()) {
                        switchFragment(1);
                        return;
                    } else {
                        if (api_token.isEmpty()) {
                            Intent intent = new Intent(MainActivity.this, WXEntryActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            if (resume_id.isEmpty()) {
                                Intent intent = new Intent(MainActivity.this, EditResumeActivity.class);
                                startActivity(intent);
                            }
                        }

                        mHomeButton.setChecked(true);
                    }

                    break;
                case R.id.radio_mine://我的
                    if (api_token.isEmpty()) {
                        Intent intent = new Intent(MainActivity.this, WXEntryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        mHomeButton.setChecked(true);
                    } else {
                        switchFragment(3);
                    }
                    break;
            }
        }
    }

    int currentTabIndex = 0;
    private void switchFragment(int targetTabIndex) {
        // 获取碎片管理器开启事务
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        // 初始化当前下标以及目标下标碎片
        Fragment currentFragment = fragmentList.get(currentTabIndex);
        Fragment targetFragment = fragmentList.get(targetTabIndex);

        // 如果该目标碎片不存在栈中
        if (!targetFragment.isAdded()) {
            // 隐藏当前碎片,添加目标碎片
            transaction.hide(currentFragment).add(R.id.mianActivity_frame,
                    targetFragment).show(targetFragment);
          //  transaction.show(targetFragment);
        } else {
            // 该目标碎片已经存在栈中,隐藏当前碎片,展示目标碎片
            transaction.hide(currentFragment).show(targetFragment);
        }
        transaction.commit();
        // 当前碎片下标重新赋值为目标下标
        currentTabIndex = targetTabIndex;
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 100000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        // option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    /**
     * 显示通知的次数
     *
     * @param badges
     */
    public void gain(int badges) {
        if (badges != 0) {
            text_notifiction_number.setVisibility(View.VISIBLE);
            text_notifiction_number.setText(badges + "");
        } else {
            text_notifiction_number.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
        if (mLocationClient!=null){
            mLocationClient.stop();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        StyledDialog.dismissLoading();
        if (mLocationClient!=null){
            mLocationClient.stop();
        }
    }

}
