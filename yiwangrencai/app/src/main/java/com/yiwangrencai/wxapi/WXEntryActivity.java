package com.yiwangrencai.wxapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hss01248.dialog.StyledDialog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.BaseActiviyt;
import com.yiwangrencai.ywkj.activity.FindSceretActivity;
import com.yiwangrencai.ywkj.activity.MainActivity;
import com.yiwangrencai.ywkj.activity.RegisterActivity;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.Md5Util;
import com.yiwangrencai.ywkj.view.CountDownTimerUtils;
import com.yiwangrencai.ywkj.view.MyPopuwindown;
import com.yiwangrencai.ywkj.view.MySexPopuwin;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;


/**
 * Created by Administrator on 2017/6/5.
 */

public class WXEntryActivity extends BaseActiviyt implements IWXAPIEventHandler, View.OnClickListener {

    private FrameLayout back;
    private EditText et_login_user_name;
    private EditText et_login_password;
    private EditText et_login_user_phone;
    private EditText et_verification_code;
    private TextView tv_login;
    private TextView tv_register;

    public WXEntryActivity() {
        super();
    }

    @Override
    public int getLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    public void initData() {

    }

    private String register;
    private LinearLayout ll_qq_login;
    private LinearLayout ll_wechat_login;
    private LinearLayout linearLayout_user_name;
    private LinearLayout lineatLayout_password;
    private LinearLayout linearLaypout_phone;
    private RelativeLayout relative_verification_code;
    private IUiListener listener;
    private TextView text_secret;
    private TextView text_common_login;
    private TextView text_phone_login;
    private TextView text_verification_code;
    private View view_common, view_right_down, view_right_up;
    private View view_phone, view_left_up, view_left_down;

    @Override
    public void initView() {
        Intent intent = getIntent();
        register = intent.getStringExtra("register");

        back = (FrameLayout) findViewById(R.id.fram_job_back);
        et_login_user_name = (EditText) findViewById(R.id.et_login_user_name);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        et_login_user_phone = (EditText) findViewById(R.id.et_login_user_phone);
        et_verification_code = (EditText) findViewById(R.id.et_verification_code);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        ll_qq_login = (LinearLayout) findViewById(R.id.ll_qq_login);
        ll_wechat_login = (LinearLayout) findViewById(R.id.ll_wechat_login);
        text_secret = (TextView) findViewById(R.id.text_secret);
        text_common_login = (TextView) findViewById(R.id.text_common_login);
        text_phone_login = (TextView) findViewById(R.id.text_phone_login);
        linearLayout_user_name = (LinearLayout) findViewById(R.id.linearLayout_user_name);
        lineatLayout_password = (LinearLayout) findViewById(R.id.lineatLayout_password);
        linearLaypout_phone = (LinearLayout) findViewById(R.id.linearLaypout_phone);
        relative_verification_code = (RelativeLayout) findViewById(R.id.relative_verification_code);
        text_verification_code = (TextView) findViewById(R.id.text_verification_code);

        et_login_user_name.setFocusable(true);
        et_login_user_name.setFocusableInTouchMode(true);
        et_login_user_name.requestFocus();

        view_common = (View) findViewById(R.id.view_common);
        view_phone = (View) findViewById(R.id.view_phone);
        view_left_up = findViewById(R.id.view_left_up);
        view_left_down = findViewById(R.id.view_left_down);
        view_right_up = findViewById(R.id.view_right_up);
        view_right_down = findViewById(R.id.view_right_down);

        tv_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        back.setOnClickListener(this);
        ll_qq_login.setOnClickListener(this);
        ll_wechat_login.setOnClickListener(this);
        text_secret.setOnClickListener(this);
        text_common_login.setOnClickListener(this);
        text_phone_login.setOnClickListener(this);
        text_verification_code.setOnClickListener(this);

        api = WXAPIFactory.createWXAPI(this, API_ID);
        api.handleIntent(getIntent(), this);
    }

    private MyPopuwindown myPopuwindown;
    private String flag;
    private IWXAPI api;
    private String API_ID = "wx0f342c7e15b4d310";
    private static final String TENCENT_ID = "100534531";
    private String flags = "common";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back://返回
                finish();
                break;
            case R.id.tv_login://正常登陆
                //用户登录
                userLogin();
                break;
            case R.id.tv_register://注册
                if ("qq".equals(flag)) {
                    Intent intent = new Intent(WXEntryActivity.this, RegisterActivity.class);
                    intent.putExtra("openid", openid);
                    intent.putExtra("nickname", nickname);
                    intent.putExtra("style", "qq");

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if ("wechat".equals(flag)) {
                    Intent intent = new Intent(WXEntryActivity.this, RegisterActivity.class);
                    intent.putExtra("openid", openid);
                    intent.putExtra("nickname", nickname);
                    intent.putExtra("style", "wechat");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(WXEntryActivity.this, RegisterActivity.class);
//                    intent.putExtra("openid", openid);
//                    intent.putExtra("nickname", nickname);
//                    intent.putExtra("style", "wechat");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                break;
            case R.id.ll_qq_login://QQ登陆
                login();
                break;
            case R.id.ll_wechat_login://微信登录

                api = WXAPIFactory.createWXAPI(this, API_ID, true);
                api.registerApp(API_ID);

                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk";
                api.sendReq(req);

                finish();
                break;
            case R.id.text_secret:
                Intent intent = new Intent(WXEntryActivity.this, FindSceretActivity.class);
                startActivity(intent);
                break;
            case R.id.text_common_login:

                flags = "common";
                text_common_login.setTextColor(getResources().getColor(R.color.foot_text_color_green));
                text_phone_login.setTextColor(0xff898989);
                view_common.setBackgroundResource(R.color.view_line_background_green);
                view_phone.setBackgroundResource(R.color.view_line_background);

                linearLayout_user_name.setVisibility(View.VISIBLE);
                lineatLayout_password.setVisibility(View.VISIBLE);

                linearLaypout_phone.setVisibility(View.GONE);
                relative_verification_code.setVisibility(View.GONE);

                et_login_user_phone.setFocusable(true);
                et_login_user_phone.setFocusableInTouchMode(true);
                et_login_user_phone.requestFocus();

                view_right_down.setVisibility(View.GONE);
                view_right_up.setVisibility(View.GONE);
                view_left_down.setVisibility(View.VISIBLE);
                view_left_up.setVisibility(View.VISIBLE);
                break;
            case R.id.text_phone_login:// todo

                flags = "phone";
                text_common_login.setTextColor(0xff898989);
                text_phone_login.setTextColor(getResources().getColor(R.color.foot_text_color_green));
                view_common.setBackgroundResource(R.color.view_line_background);
                view_phone.setBackgroundResource(R.color.view_line_background_green);

                linearLayout_user_name.setVisibility(View.GONE);
                lineatLayout_password.setVisibility(View.GONE);
                linearLaypout_phone.setVisibility(View.VISIBLE);
                relative_verification_code.setVisibility(View.VISIBLE);

                et_login_user_name.setFocusable(true);
                et_login_user_name.setFocusableInTouchMode(true);
                et_login_user_name.requestFocus();

                view_right_down.setVisibility(View.VISIBLE);
                view_right_up.setVisibility(View.VISIBLE);
                view_left_down.setVisibility(View.GONE);
                view_left_up.setVisibility(View.GONE);
                break;
            case R.id.text_verification_code:
                gainPhoneCode();//获取验证码
                break;
        }
    }

    private String codephone = "tV8tpft1b1wPtk58";
    private static final int PHONE_CPODE = 20;
    private static final int PHONE_CODE_ERROR = 1;
    private MyPopuwindown myPopuwindownn;
    private String stringCode;
    private int codess;

    private void gainPhoneCode() {
        String phone = et_login_user_phone.getText().toString().trim();
        if (phone == null) {
            Toast.makeText(WXEntryActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
//        Pattern pattern = Pattern.compile("^((13[0-9])|(14[0-9])|(15([0-9]))|(18[0-9]))\\d{8}$");
        Pattern pattern = Pattern.compile(ContentUrl.PHONE_PARTTEN);
        Matcher matcher = pattern.matcher(phone);

        if (!matcher.matches()) {
            Toast.makeText(WXEntryActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        StyledDialog.buildLoading().show();
        String ver_str = Md5Util.getMd5(Md5Util.getMd5(phone + codephone));
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("mobile", phone);
        formEncoding.add("ver_str", ver_str);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + "user/user_login_smscode")
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .post(formEncoding.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(PHONE_CODE_ERROR);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                stringCode = response.body().string();
                codess = response.code();
                Log.i("strstrstrstr", stringCode);
                handler.sendEmptyMessage(PHONE_CPODE);
            }
        });
    }

    private String openid;


    public void login() {

        mTencent = Tencent.createInstance(TENCENT_ID, this.getApplicationContext());

        //   if (!mTencent.isSessionValid()) {
        mTencent.login(WXEntryActivity.this, "all", listener);
        //           }


        listener = new IUiListener() {

            @Override
            public void onComplete(Object o) {

                org.json.JSONObject jsonObject = (org.json.JSONObject) o;
                Log.i("jsonjsonjson", jsonObject.toString());
                try {
                    String ret = jsonObject.getString("ret");
                    if ("0".equals(ret)) {
                        openid = jsonObject.getString("openid");
                        access_token = jsonObject.getString("access_token");
                        String pay_token = jsonObject.getString("pay_token");
                        String expires_in = jsonObject.getString("expires_in");
                        String pfkey = jsonObject.getString("pfkey");
                        mTencent.setOpenId(openid);
                        mTencent.setAccessToken(access_token, expires_in);

                        Log.i("openid", openid);
                        Log.i("access_token", access_token);
                    }

                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {


            }

            @Override
            public void onCancel() {


            }
        };

    }

    private String string;
    private Tencent mTencent;

    /**
     * 用户登录
     */
    private void userLogin() {
        Request request = null;
        SharedPreferences share = getSharedPreferences("data", MODE_PRIVATE);
        String device_token = share.getString("device_token", "");

        if ("common".equals(flags)) {
            commonLogin();//普通登录
        } else if ("phone".equals(flags)) {

        }
        FormEncodingBuilder requestBody = new FormEncodingBuilder();

        if ("common".equals(flags)) {
            commonLogin();//普通登录
            String userName = et_login_user_name.getText().toString().trim();
            String password = et_login_password.getText().toString().trim();
            if (userName.isEmpty()) {
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, "请输入至少六位以上的密码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (device_token != null && !"".equals(device_token)) {
                requestBody.add("device_token", device_token);
            }

            Log.e("device_token", device_token);
            requestBody.add("device_token", device_token);
            requestBody.add("account", userName);
            requestBody.add("password", password);

        } else if ("phone".equals(flags)) {

            String phone = et_login_user_phone.getText().toString().trim();
            String phoneCode = et_verification_code.getText().toString().trim();
            if ("".equals(phone)) {
                Toast.makeText(WXEntryActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                return;
            }

//            Pattern pattern = Pattern.compile("^((13[0-9])|(14[0-9])|(15([0-9]))|(18[0-9]))\\d{8}$");
            Pattern pattern = Pattern.compile(ContentUrl.PHONE_PARTTEN);
            Matcher matcher = pattern.matcher(phone);

            if (!matcher.matches()) {
                Toast.makeText(WXEntryActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("".equals(phoneCode)) {
                Toast.makeText(WXEntryActivity.this, "请输入动态码", Toast.LENGTH_SHORT).show();
                return;
            }
            requestBody.add("mobile", phone);
            requestBody.add("code", phoneCode);

        }

        if ("qq".equals(styles)) {
            requestBody.add("qqopenid", openid);
            requestBody.add("nickname", nickname);
            if (unionid == null) {
                requestBody.add("qqunionid", "");
            } else {
                requestBody.add("qqunionid", unionid);
            }

        } else if ("wechat".equals(styles)) {
            Log.i("444454545454545", wechatopenid);
            requestBody.add("wechatopenid", wechatopenid);
            requestBody.add("wechatunionid", wechatunionid);
        }

        StyledDialog.buildLoading().show();
        OkHttpClient okHttpclient = new OkHttpClient();

        if ("common".equals(flags)) {
            Log.i("普通登录", "普通登录");
            request = new Request.Builder()
                    .url(ContentUrl.BASE_URL + ContentUrl.USER_LOGIN)
                    .addHeader("Accept", "application/json")
                    .post(requestBody.build())
                    .build();
        } else if ("phone".equals(flags)) {
            request = new Request.Builder()
                    .url(ContentUrl.BASE_URL + "user/mob_login")
                    .addHeader("Accept", "application/json")
                    .post(requestBody.build())
                    .build();
        }

        okHttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(3000);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                codes = response.code();
                string = response.body().string();
                Log.i("登陆成功返回的数据json", string);
                handler.sendEmptyMessage(1000);
            }
        });
    }

    /**
     * 普通登录
     */
    private void commonLogin() {

    }

    String access_token;
    String unionid;
    String wechatopenid;
    String wechatunionid;
    String username;
    String uid;
    String api_token;
    String avatar;
    String resume_id;
    String sex;
    String name;
    String resume_eid;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    if (codes != 200) {
                        Toast.makeText(WXEntryActivity.this, "联网获取数据失败", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jsonObject = JSON.parseObject(string);
                        String msggg = jsonObject.getString("msg");
                        String code = jsonObject.getString("code");

                        Log.i("用户名手机登录返回的数据", msggg);

                        if (msggg.equals("用户名或手机号码不存在")) {
                            Toast.makeText(WXEntryActivity.this, msggg, Toast.LENGTH_SHORT).show();
                            StyledDialog.dismissLoading();
                            return;
                        } else if (msggg.equals("密码错误")) {
                            Toast.makeText(WXEntryActivity.this, msggg, Toast.LENGTH_SHORT).show();
                            StyledDialog.dismissLoading();
                            return;
                        } else {
                            if ("1".equals(code)) {

                                String jsonData = jsonObject.getString("data");
                                JSONObject jsonObject1 = JSON.parseObject(jsonData);
                                api_token = jsonObject1.getString("api_token");
                                resume_id = jsonObject1.getString("resume_id");
                                uid = jsonObject1.getString("uid");
                                username = jsonObject1.getString("username");
                                String mobile = jsonObject1.getString("mobile");
                                String degree = jsonObject1.getString("degree");
                                String chkphoto_open = jsonObject1.getString("chkphoto_open");
                                String resume_status = jsonObject1.getString("resume_status");
                                name = jsonObject1.getString("name");
                                String resume_mobile = jsonObject1.getString("resume_mobile");
                                String job_status = jsonObject1.getString("job_status");
                                avatar = jsonObject1.getString("avatar");
                                String part_resume_id = jsonObject1.getString("part_resume_id");
                                sex = jsonObject1.getString("sex");
                                resume_eid = jsonObject1.getString("resume_eid");
                                String chat_pwd = jsonObject1.getString("chat_pwd");

                                Log.i("mobilemobile", mobile + "mobilemobile");
                                Log.i("resume_mobileresume_mobile", resume_mobile + "resume_mobile");
                                SharedPreferences sharedPreferences = getSharedPreferences("Activity", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editer = sharedPreferences.edit();
                                editer.putString("api_token", api_token);

                                editer.commit();
                                SharedPreferences share = getSharedPreferences("data", MODE_PRIVATE);
                                SharedPreferences.Editor editors = share.edit();
                                editors.putString("id", resume_id);
                                editors.putString("username", username);
                                editors.putString("resume_id", resume_id);
                                editors.putString("uid", uid);
                                editors.putString("mobile", mobile);
                                editors.putString("degree1", degree);
                                editors.putString("chkphoto_open", chkphoto_open);
                                editors.putString("resume_status", resume_status);
                                editors.putString("name", name);
                                editors.putString("resume_mobile", resume_mobile);
                                editors.putString("job_status", job_status);
                                editors.putString("avatar", avatar);
                                editors.putString("part_resume_id", part_resume_id);
                                editors.putString("sex", sex);
                                editors.putString("resume_eid", resume_eid);
                                editors.putString("chat_pwd", chat_pwd);

                                editors.commit();

                                registerPersoneralUser(chat_pwd);

                                //登陆成功关闭状态
                                StyledDialog.dismissLoading();

                                if (register != null) {
                                    if (register.equals("register")) {
                                        Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("register", "register");
                                        startActivity(intent);
                                    }
                                } else {
                                    Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("register", "loginMine");
                                    startActivity(intent);
                                }
                                finish();
                            }
                        }
                    }
                    break;
                case 3000:
                    Toast.makeText(WXEntryActivity.this, "登陆失败,请重新登陆", Toast.LENGTH_SHORT).show();
                    StyledDialog.dismissLoading();
                    break;
                case 112:
                    JSONObject json = JSON.parseObject(str);
                    JSONObject data = json.getJSONObject("data");
                    String done = data.getString("done");
                    Log.i("qqqqqqqq", str);
                    if ("0".equals(done)) {//没有绑定
                        //显示qq登录的弹出框

                        unionid = data.getString("qqunionid");
                        openid = data.getString("qqopenid");
                        flags = "common";
                        StyledDialog.dismissLoading();
                        revealQqLoginPopuwind("qq");

                    } else if ("1".equals(done)) {//已经绑定
                        StyledDialog.dismissLoading();

                        JSONObject user = data.getJSONObject("user");
                        parsenUserData(user);//解析数据
                    }
                    break;
                case 113:
                    if (codes == 200) {
                        JSONObject jsons = JSON.parseObject(str);
                        access_token = jsons.getString("access_token");
                        String expires_in = jsons.getString("expires_in");
                        String refresh_token = jsons.getString("refresh_token");
                        String openid = jsons.getString("openid");
                        String scope = jsons.getString("scope");
                        unionid = jsons.getString("unionid");

                        gainPersonalMess(openid, access_token);

                    } else {

                    }
                    break;
                case 114:
                    if (codes == 200) {
                        JSONObject jsons = JSON.parseObject(str);
                        nickname = jsons.getString("nickname");
                        openid = jsons.getString("openid");
                        unionid = jsons.getString("unionid");
                        Log.i("11111111111111111", nickname);
                        wechatLogin();
                    } else {

                    }
                    break;
                case 116:
                    if (codes == 200) {
                        JSONObject jsonss = JSON.parseObject(str);
                        String codess = jsonss.getString("code");
                        if ("1".equals(codess)) {
                            JSONObject jsonData = jsonss.getJSONObject("data");
                            String doneW = jsonData.getString("done");
                            if ("1".equals(doneW)) {
                                JSONObject user = jsonData.getJSONObject("user");
                                parsenUserData(user);//解析数据
                            } else if ("0".equals(doneW)) {
                                wechatopenid = jsonData.getString("wechatopenid");
                                wechatunionid = jsonData.getString("wechatunionid");
                                flags = "common";
                                revealQqLoginPopuwind("wechat");
                            }
                        } else {

                        }
                    } else {

                    }
                    break;
                case PHONE_CPODE:
                    Log.i("wwwwwwwwwwwww", "sdfsaddf " + codess);
                    if (codess == 200) {
                        JSONObject jsonObject = JSON.parseObject(stringCode);
                        String message = jsonObject.getString("msg");
                        String codess = jsonObject.getString("code");
                        Log.i("ssssssqqqqqqqqq", " " + codess + message);
                        if ("1".equals(codess)) {
                            CountDownTimerUtils count = new CountDownTimerUtils(text_verification_code, 60000, 1000);
                            count.start();
                        }
                        if (message != null && !"".equals(message)) {
                            Toast.makeText(WXEntryActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                        StyledDialog.dismissLoading();
                    } else {
                        StyledDialog.dismissLoading();
                    }
                    break;
                case PHONE_CODE_ERROR:
                    StyledDialog.dismissLoading();
                    break;
            }
        }
    };

    /**
     * 登录聊天的求职者的用户账号
     */
    private void registerPersoneralUser(final String pwd) {
        if (pwd!=null&&pwd.length()>0){
        JMessageClient.login("personal_" + uid, pwd, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.i("职位详情的登陆成功", i + "字符串" + s+"+psw:"+pwd);
                        updataJGData();
                    }
                });
        }
    }
//        OkHttpClient okHttpClient = new OkHttpClient();
//        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
//        formEncoding.add("id", uid);
//        formEncoding.add("type", "0");
//
//        final Request request = new Request.Builder()
//                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
//                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
//                .url(ContentUrl.BASE_URL + "register_chat")
//                .post(formEncoding.build())
//                .build();
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                // loginHuanXin();
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                Log.i("aaaaaaaaaaaaaa", response.body().string());
//                JMessageClient.login("personal_" + uid, "personal_" + uid, new BasicCallback() {
//                    @Override
//                    public void gotResult(int i, String s) {
//                        Log.i("职位详情的登陆成功", i + "字符串" + s);
//                        updataJGData();
//                    }
//                });
//            }
//        });


    private void updataJGData() {
        cn.jpush.im.android.api.model.UserInfo userInfo = JMessageClient.getMyInfo();
        Log.i("从本地获取当前登录用户的信息", "1111111" + userInfo);
        if (userInfo == null) {

        } else {
            userInfo.setAddress(avatar);
            userInfo.setRegion(resume_eid);
            userInfo.setNickname(name);
            if ("0".equals(sex)) {
                userInfo.setGender(cn.jpush.im.android.api.model.UserInfo.Gender.unknown);
            } else if ("1".equals(sex)) {
                userInfo.setGender(cn.jpush.im.android.api.model.UserInfo.Gender.male);
            } else if ("2".equals(sex)) {
                userInfo.setGender(cn.jpush.im.android.api.model.UserInfo.Gender.female);
            }

            JMessageClient.updateMyInfo(cn.jpush.im.android.api.model.UserInfo.Field.address, userInfo, new BasicCallback() {

                @Override
                public void gotResult(int i, String s) {
                    Log.i("更新头像", "i：" + i + "  s;" + s);
                }
            });
            JMessageClient.updateMyInfo(cn.jpush.im.android.api.model.UserInfo.Field.nickname, userInfo, new BasicCallback() {

                @Override
                public void gotResult(int i, String s) {
                    Log.i("更新头像", "i：" + i + "  s;" + s);
                }
            });
            JMessageClient.updateMyInfo(cn.jpush.im.android.api.model.UserInfo.Field.gender, userInfo, new BasicCallback() {

                @Override
                public void gotResult(int i, String s) {
                    Log.i("更新头像", "i：" + i + "  s;" + s);
                }
            });
            JMessageClient.updateMyInfo(cn.jpush.im.android.api.model.UserInfo.Field.region, userInfo, new BasicCallback() {

                @Override
                public void gotResult(int i, String s) {
                    Log.i("更新头像", "i：" + i + "  s;" + s);
                }
            });
        }
    }

    /**
     * 获取微信的个人信息
     *
     * @param openid
     * @param access_token
     */
    private void gainPersonalMess(String openid, String access_token) {
        String url = "https://api.weixin.qq.com/sns/userinfo?";
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        form.add("access_token", access_token);
        form.add("openid", openid);

        Request request = new Request.Builder()
                .url(url)
                .post(form.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                codes = response.code();
                Log.e("personalMess", str);
                handler.sendEmptyMessage(114);
            }
        });
    }


    /**
     * 登陆成功返回的数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private String nickname;
    private String qqopenid;

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11101 | resultCode == -1) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "返回数据为空", Toast.LENGTH_LONG);
            } else {
                Tencent.handleResultData(data, listener);
                QQToken qqToken = mTencent.getQQToken();
                UserInfo userInfo = new UserInfo(this, qqToken);
                userInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        org.json.JSONObject jsonObjects = (org.json.JSONObject) o;
                        Log.i("aassssss", jsonObjects.toString());
                        try {
                            String ret = jsonObjects.getString("ret");
                            Log.i("ret", ret);
                            if ("0".equals(ret)) {
                                nickname = jsonObjects.getString("nickname");
                                String gender = jsonObjects.getString("gender");
                                String province = jsonObjects.getString("province");
                                String city = jsonObjects.getString("city");

                                //QQ登录
                                QqLogin();
                            }
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(UiError uiError) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        }
    }

    /**
     * qq登录
     */

    private String str;

    private void QqLogin() {
        StyledDialog.buildLoading().show();

        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formEncod = new FormEncodingBuilder();
        formEncod.add("qqopenid", openid);
        formEncod.add("nickname", nickname);
        formEncod.add("qq_access_token", access_token);

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.USER_QQLOGIN)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .post(formEncod.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                code = response.code();
                str = response.body().string();
                Log.i("aaaa str", str);
                handler.sendEmptyMessage(112);
            }
        });
    }

    /**
     * 显示QQ登录绑定的弹出框
     */
    String styles = "";

    private void revealQqLoginPopuwind(final String style) {
        final MySexPopuwin mySexPopuwin = new MySexPopuwin(WXEntryActivity.this, R.layout.popuwindown_photo);
        mySexPopuwin.showAtLocation(WXEntryActivity.this.findViewById(R.id.login_mine_linearLayout), Gravity.BOTTOM, 0, 0);
        View view = mySexPopuwin.getView();
        TextView textTitle = (TextView) view.findViewById(R.id.textTitle);
        TextView textHasAccount = (TextView) view.findViewById(R.id.text_take_photo);
        TextView textRegister = (TextView) view.findViewById(R.id.text_pictuor);
        TextView text_cancle = (TextView) view.findViewById(R.id.text_cancle);

        if ("qq".equals(style)) {
            styles = "qq";
            textTitle.setText("您还没有绑定qq账号");
            textRegister.setText("现在去注册(自动绑定)");
            textHasAccount.setText("现在绑定已有账号");
        } else if ("wechat".equals(style)) {
            styles = "wechat";
            textTitle.setText("您还没有绑定微信账号");
            textRegister.setText("现在去注册(自动绑定)");
            textHasAccount.setText("现在绑定已有账号");
        }

        //点击现在去注册qqopenid,qqunionid
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WXEntryActivity.this, RegisterActivity.class);
                intent.putExtra("nickname", nickname);
                if ("qq".equals(styles)) {
                    intent.putExtra("style", "qq");
                    intent.putExtra("openid", openid);
                    intent.putExtra("unionid", unionid);

                } else if ("wechat".equals(styles)) {
                    intent.putExtra("openid", wechatopenid);
                    intent.putExtra("unionid", wechatunionid);
                    intent.putExtra("style", "wechat");

                }
                startActivity(intent);
            }
        });

        //点击现在去绑定已有账号
        textHasAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mySexPopuwin.dismiss();
                flag = "qq";
                if ("qq".equals(style)) {
                    tv_login.setText("立即登录并绑定qq");
                    tv_register.setText("注册并绑定qq");
                } else if ("wechat".equals(style)) {
                    tv_login.setText("立即登录并绑定微信");
                    tv_register.setText("注册并绑定微信");
                }
            }
        });

        //点击取消
        text_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySexPopuwin.dismiss();
            }
        });
    }

    /**
     * 解析QQ登陆成功获取的数据
     *
     * @param user
     */
    private void parsenUserData(JSONObject user) {

        username = user.getString("username");
        String mobile = user.getString("mobile");
        String email = user.getString("email");
        api_token = user.getString("api_token");
        uid = user.getString("uid");
        String resume_id = user.getString("resume_id");
        String resume_status = user.getString("resume_status");
        avatar = user.getString("avatar");
        String name = user.getString("name");
        sex = user.getString("sex");
        String degree = user.getString("degree");
        String chkphoto_open = user.getString("chkphoto_open");
        String part_resume_id = user.getString("part_resume_id");
        String resume_mobile = user.getString("resume_mobile");
        String job_status = user.getString("job_status");
        resume_eid = user.getString("resume_eid");
        String chat_pwd = user.getString("chat_pwd");

        SharedPreferences sharedPreferences = getSharedPreferences("Activity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editer = sharedPreferences.edit();
        editer.putString("api_token", api_token);
        editer.commit();

        SharedPreferences share = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editors = share.edit();
        editors.putString("id", resume_id);
        editors.putString("username", username);
        editors.putString("mobile", mobile);
        editors.putString("uid", uid);
        editors.putString("degree1", degree);
        editors.putString("chkphoto_open", chkphoto_open);
        editors.putString("resume_status", resume_status);
        editors.putString("resume_id", resume_id);
        editors.putString("name", name);
        editors.putString("resume_mobile", resume_mobile);
        editors.putString("job_status", job_status);
        editors.putString("avatar", avatar);
        editors.putString("email", email);
        editors.putString("sex", sex);
        editors.putString("job_status", job_status);
        editors.putString("part_resume_id", part_resume_id);
        editors.putString("resume_eid", resume_eid);
        editors.putString("chat_pwd", chat_pwd);
        editors.commit();

        registerPersoneralUser(chat_pwd);//登录极光账号

        Log.i("name", name);
        StyledDialog.dismissLoading();

        if (register != null) {
            if (register.equals("register")) {
                WXEntryActivity.this.finish();
                Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("register", "register");
                startActivity(intent);
            }
        } else {
            WXEntryActivity.this.finish();
            Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("register", "loginMine");
            startActivity(intent);
        }

    }

    /**
     * 微信登录
     */
    private void wechatLogin() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formd = new FormEncodingBuilder();
        formd.add("wechatopenid", openid);
        formd.add("wechatunionid", unionid);
        formd.add("nickname", nickname);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.USER_WXLOGIN)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api)
                .post(formd.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("wechatmess", str);
                handler.sendEmptyMessage(115);
            }
        });

    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    int code;
    int codes;
    @Override
    public void onResp(BaseResp baseResp) {
        final int rescode = baseResp.errCode;
        switch (rescode) {
            case BaseResp.ErrCode.ERR_OK:
                final String code = ((SendAuth.Resp) baseResp).code;
                Log.i("err_ok", code);
                String url = ContentUrl.BASE_URL + "wxlogin";
//                wx_code=code,
//                type='android_personal'

//                        openid,
//                unionid
                // String url = "https://api.weixin.qq.com/sns/oauth2/access_token?";
                OkHttpClient okHttpClient = new OkHttpClient();
                FormEncodingBuilder form = new FormEncodingBuilder();
//                form.add("appid", API_ID);
//                form.add("secret", "a0f0896065bc670448ced5ccc1d7f544");
//                form.add("code", code);
//                form.add("grant_type", "authorization_code");
                form.add("wx_code", code);
                form.add("type", "android_personal");
                Request request = new Request.Builder()
                        .url(ContentUrl.BASE_URL + "wxlogin")
                        .post(form.build())
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        str = response.body().string();
                        codes = response.code();
                        Log.e("strWXe", str);
                        handler.sendEmptyMessage(116);
                    }
                });
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }

}
