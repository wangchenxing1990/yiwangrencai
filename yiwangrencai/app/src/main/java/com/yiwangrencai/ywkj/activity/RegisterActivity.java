package com.yiwangrencai.ywkj.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.wxapi.WXEntryActivity;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.Md5Util;
import com.yiwangrencai.ywkj.view.CountDownTimerUtils;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Administrator on 2017/4/11.
 */
public class RegisterActivity extends BaseActiviyt implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {

    private FrameLayout fram_resume_back;
    private EditText et_phone_number;
    private EditText et_verification_code;
    private EditText et_one_password;
    private EditText et_two_password;
    private TextView tv_register;
    private Button get_code;

    private ImageView image_phone_number;
    private ImageView image_code_verification;
    private ImageView image_one_password;
    private ImageView iamge_two_password;
    private RelativeLayout relative_phone_number;
    private RelativeLayout relative_verification_code;
    private RelativeLayout relative_one_password;
    private RelativeLayout relative_two_password;
    private String stringCodee;
    private String api_token;
    private String username;
    private String mobile;
    private String resume_id;
    private String resume_status;
    private String avatar;
    private String name;
    private String sex;
    private String chkphoto_open;
    private String degree1;
    private String uid;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_two;
    }

    @Override
    public void initData() {

    }

    private String phone;
    private String code = "tV8tpft1b1wPtk58";
    private String registerCode;
    private String nickname;
    private String openid;
    private String styles;
    private String unionid;
    private String PHONE="^1\\d{10}$";

    @Override
    public void initView() {

        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        openid = intent.getStringExtra("openid");
        styles = intent.getStringExtra("style");
        unionid = intent.getStringExtra("unionid");

        relative_verification_code = (RelativeLayout) findViewById(R.id.relative_verification_code);
        relative_phone_number = (RelativeLayout) findViewById(R.id.relative_phone_number);
        relative_one_password = (RelativeLayout) findViewById(R.id.relative_one_password);
        relative_two_password = (RelativeLayout) findViewById(R.id.relative_two_password);

        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_verification_code = (EditText) findViewById(R.id.et_verification_code);
        et_one_password = (EditText) findViewById(R.id.et_one_password);
        et_two_password = (EditText) findViewById(R.id.et_two_password);

        image_phone_number = (ImageView) findViewById(R.id.image_phone_number);
        image_code_verification = (ImageView) findViewById(R.id.image_code_verification);
        image_one_password = (ImageView) findViewById(R.id.image_one_password);
        iamge_two_password = (ImageView) findViewById(R.id.iamge_two_password);

        image_phone_number.setOnClickListener(this);
        image_code_verification.setOnClickListener(this);
        image_one_password.setOnClickListener(this);
        iamge_two_password.setOnClickListener(this);

        tv_register = (TextView) findViewById(R.id.tv_register);
        get_code = (Button) findViewById(R.id.get_code);
        fram_resume_back = (FrameLayout) findViewById(R.id.fram_resume_back);


        fram_resume_back.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        get_code.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //提交注册
            case R.id.tv_register:
                registerUser();
                break;
            case R.id.get_code://获取验证码
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                phone = et_phone_number.getText().toString().trim();
                gainCode();
                break;
            case R.id.image_phone_number:
                et_phone_number.setText("");
                image_phone_number.setVisibility(View.INVISIBLE);
                break;
            case R.id.image_code_verification:
                et_verification_code.setText("");
                image_code_verification.setVisibility(View.INVISIBLE);
                break;
            case R.id.image_one_password:
                et_one_password.setText("");
                image_one_password.setVisibility(View.INVISIBLE);
                break;
            case R.id.iamge_two_password:
                et_two_password.setText("");
                iamge_two_password.setVisibility(View.INVISIBLE);
                break;
            //返回上一个界面
            case R.id.fram_resume_back:
                finish();
                break;
        }
    }

    /**
     * 注册用户
     */
    private void registerUser() {
        String phone = et_phone_number.getText().toString().trim();
        String code = et_verification_code.getText().toString().trim();
        String onepassword = et_one_password.getText().toString().trim();
        String twopassword = et_two_password.getText().toString().trim();

        SharedPreferences share = getSharedPreferences("data", MODE_PRIVATE);
        String device_token = share.getString("device_token", "");
        Log.e("aaa", phone);

        if (phone == null) {
            Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.length() != 11) {
            Toast.makeText(RegisterActivity.this, "手机号码是11位", Toast.LENGTH_SHORT).show();
            return;
        }

        Pattern pattern = Pattern.compile(ContentUrl.PHONE_PARTTEN);
        Matcher matcher = pattern.matcher(phone);

        if (!matcher.matches()) {
            Toast.makeText(RegisterActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (code == null || code.equals("")) {
            Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (onepassword.length() < 6) {
            Toast.makeText(RegisterActivity.this, "请输入至少6位以上的密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (twopassword.length() < 6) {
            Toast.makeText(RegisterActivity.this, "请输入至少6位以上的密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!onepassword.equals(twopassword)) {
            Toast.makeText(RegisterActivity.this, "两次密码输入的不一样", Toast.LENGTH_SHORT).show();
            return;
        }

        StyledDialog.buildLoading().show();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("username", "");
        formEncoding.add("password", et_two_password.getText().toString().trim());
        formEncoding.add("mobile", et_phone_number.getText().toString().trim());
        formEncoding.add("code", et_verification_code.getText().toString().trim());

        if ("qq".equals(styles)) {
            formEncoding.add("qqopenid", openid);
        } else if ("wechat".equals(styles)) {
            formEncoding.add("wechatopenid", openid);
            formEncoding.add("wechatunionid", unionid);
        }


        if (device_token != null || !"".equals(device_token)) {
            formEncoding.add("device_token", device_token);
        }

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.REGISTER_USER)
                .addHeader("Accept", "application/json")
                .post(formEncoding.build()).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.print("" + e.getMessage().toString());
                if (e.getMessage().toString() != null) {

                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                registerCode = response.body().string();
                codess = response.code();
                System.out.println("注册成功服务器返回的数据response" + registerCode);
                handler.sendEmptyMessage(2000);
            }
        });
    }

    /**
     * 获取验证码
     *
     * @return
     */
    private int codess;

    private void gainCode() {

        System.out.println("输入的MD5值是++++：" + Md5Util.getMd5(phone + code));
        System.out.println("输入的MD5值是++++222" + Md5Util.getMd5(Md5Util.getMd5(phone + code)));

        if (phone != null && !phone.isEmpty()) {

            Pattern pattern = Pattern.compile(ContentUrl.PHONE_PARTTEN);
            Matcher matcher = pattern.matcher(phone);
            if (!matcher.matches()) {
                Toast.makeText(RegisterActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                return;
            }

            StyledDialog.buildLoading().show();
            String ver_str = Md5Util.getMd5(Md5Util.getMd5(phone + code));

            OkHttpClient okHttpClient = new OkHttpClient();
            FormEncodingBuilder requestBody = new FormEncodingBuilder();
            requestBody.add("mobile", phone);
            requestBody.add("ver_str", ver_str);

            Request request = new Request.Builder()
                    .url(ContentUrl.BASE_URL + ContentUrl.SEND_CODE)
                    .addHeader("Accept", "application/json")
                    .post(requestBody.build())
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (e.getMessage() != null) {
                        handler.sendEmptyMessage(1200);
                    }
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    stringCodee = response.body().string();
                    codess = response.code();
                    System.out.println("请求验证码成功-------" + stringCodee);
                    handler.sendEmptyMessage(1000);
                }
            });
        } else {
            Toast.makeText(this, "手机号码不能为空,请输入手机号码", Toast.LENGTH_SHORT).show();
        }
    }

    Dialog dialogs;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    StyledDialog.dismissLoading();
                    System.out.println("获取的验证码" + stringCodee);
                    if (codess == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(stringCodee);
                            String codes = jsonObject.getString("code");
                            if ("1".equals(codes)) {
                                CountDownTimerUtils count = new CountDownTimerUtils(get_code, 60000, 1000);
                                count.start();
                                String msgCodee = jsonObject.getString("msg");
                                System.out.println("获取的验证码" + msgCodee);
                            } else if ("-1".equals(codes)) {
                                String msgCodee = jsonObject.getString("msg");
                                Toast.makeText(RegisterActivity.this, msgCodee, Toast.LENGTH_SHORT).show();
                            } else if ("0".equals(codes)) {
                                String msgCodee = jsonObject.getString("msg");
                                JSONArray jsonArray_data = jsonObject.optJSONArray("data");
                                if (jsonArray_data == null) {
                                    //必定是一个对象，处理返回数据
                                    JSONObject jsonObject_data = jsonObject.optJSONObject("data");
                                    int mobile_repeat = jsonObject_data.optInt("mobile_repeat");
                                    if (mobile_repeat == 1) {//手机号被注册,弹窗处理
                                        dialogs = StyledDialog.buildIosAlert("温馨提示", "您的手机号码已经注册过会员,您可以", new MyDialogListener() {
                                            @Override
                                            public void onFirst() {
                                                Intent intent = new Intent(RegisterActivity.this, FindSceretActivity.class);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onSecond() {
                                                Intent intent = new Intent(RegisterActivity.this, WXEntryActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onThird() {

                                            }
                                        })
                                                .setBtnSize(16)
                                                .setTitleColor(R.color.title_color)
                                                .setBtnText("找回密码", "动态码登陆", "继续注册").show();
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, msgCodee, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                    }
                    break;
                case 2000:
                    StyledDialog.dismissLoading();
                    if (codess == 200) {
                        com.alibaba.fastjson.JSONObject jsonObjectt = JSON.parseObject(registerCode);
                        String code = jsonObjectt.getString("code");
                        if (jsonObjectt.getString("msg").equals("手机号码已被注册")) {
                            Toast.makeText(RegisterActivity.this, "手机号码已被注册", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (jsonObjectt.getString("msg").equals("请先发送短信")) {
                            Toast.makeText(RegisterActivity.this, "请先获取验证码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if ("1".equals(code)) {
                            com.alibaba.fastjson.JSONObject data = JSON.parseObject(jsonObjectt.getString("data"));
                            uid = data.getString("uid");
                            api_token = data.getString("api_token");
                            username = data.getString("username");
                            mobile = data.getString("mobile");
                            resume_id = data.getString("resume_id");
                            degree1 = data.getString("degree");
                            resume_status = data.getString("resume_status");
                            avatar = data.getString("avatar");
                            name = data.getString("name");
                            sex = data.getString("sex");
                            chkphoto_open = data.getString("chkphoto_open");
                            String chat_pwd = data.getString("chat_pwd");
                            System.out.println("注册成功返回的data的数据api_token" + api_token);

                            SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("Activity", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putString("api_token", api_token).commit();

                            SharedPreferences shares = getSharedPreferences("data", MODE_PRIVATE);
                            SharedPreferences.Editor editor = shares.edit();
                            editor.putString("username", username);
                            editor.putString("id", resume_id);
                            editor.putString("uid", uid);
                            editor.putString("mobile", mobile);
                            editor.putString("degree", degree1);
                            editor.putString("resume_id", resume_id);
                            editor.putString("resume_status", resume_status);
                            editor.putString("avatar", avatar);
                            editor.putString("name", name);
                            editor.putString("sex", sex);
                            editor.putString("chkphoto_open", chkphoto_open);
                            editor.putString("chat_pwd", chat_pwd);
                            editor.commit();

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.putExtra("register", "register");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        } else if ("0".equals(code)) {
                            Toast.makeText(RegisterActivity.this, jsonObjectt.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else if ("-1".equals(code)) {
                            Toast.makeText(RegisterActivity.this, jsonObjectt.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } else {

                    }
                    break;
                case 1200:
                    StyledDialog.dismissLoading();
                    Toast.makeText(RegisterActivity.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
                    break;
                case NUMBER_IPONE:

                    break;
            }
        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    /**
     * 注册聊天的求职者的用户账号
     */
    private void registerPersoneralUser() {

        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", username);
        formEncoding.add("type", "0");

        final Request request = new Request.Builder()
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .url(ContentUrl.BASE_URL + "register_chat")
                .post(formEncoding.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                // loginHuanXin();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.i("aaaaaaaaaaaaaa", response.body().string());
                JMessageClient.login("personal_" + uid, "personal_" + uid, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.i("职位详情的登陆成功", i + "字符串" + s);
                    }
                });
            }
        });
    }


    /**
     * 输入之后的edittext之后的改变来设置edittext的显示状态
     *
     * @param
     */
    private static final int NUMBER_IPONE = 4;

    @Override
    public void afterTextChanged(Editable s) {

//        if (!s.toString().isEmpty()) {
////            if (s.toString().equals(et_user_name.getText().toString().trim())) {
////                image_user_name.setVisibility(View.VISIBLE);
////            } else
//            if (s.toString().equals(et_phone_number.getText().toString().trim())) {
//                image_phone_number.setVisibility(View.VISIBLE);
//            } else if (s.toString().equals(et_verification_code.getText().toString().trim())) {
//                image_code_verification.setVisibility(View.VISIBLE);
//            } else if (s.toString().equals(et_one_password.getText().toString().trim())) {
//                image_one_password.setVisibility(View.VISIBLE);
//            } else if (s.toString().equals(et_two_password.getText().toString().trim())) {
//                iamge_two_password.setVisibility(View.VISIBLE);
//            }
//        } else {
//
////            image_user_name.setVisibility(View.INVISIBLE);
//            image_phone_number.setVisibility(View.INVISIBLE);
//            image_code_verification.setVisibility(View.INVISIBLE);
//            image_one_password.setVisibility(View.INVISIBLE);
//            iamge_two_password.setVisibility(View.INVISIBLE);
//
//        }
    }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText) {

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 判断是否获取焦点,根据edittext是否获取焦点来显示不同的状态
     *
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (hasFocus) {//edittext获得焦点后显示的状态
            switch (v.getId()) {

            }

        } else {//没有获得焦点的edittext实现的状态

            relative_phone_number.setBackgroundResource(R.drawable.shape_corners_normal);
            image_phone_number.setVisibility(View.INVISIBLE);//点击删除的按钮图标隐藏

            relative_verification_code.setBackgroundResource(R.drawable.shape_corners_normal);
            image_code_verification.setVisibility(View.INVISIBLE);

            relative_one_password.setBackgroundResource(R.drawable.shape_corners_normal);
            image_one_password.setVisibility(View.INVISIBLE);

            relative_two_password.setBackgroundResource(R.drawable.shape_corners_normal);
            iamge_two_password.setVisibility(View.INVISIBLE);

            switch (v.getId()) {
                case R.id.et_one_password:
                    if (!et_one_password.getText().toString().trim().isEmpty() && et_one_password.getText().toString().trim().length() < 6) {
                        Toast.makeText(this, "至少应输入6位密码", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.et_two_password:
                    if (!et_one_password.getText().toString().trim().equals(et_two_password.getText().toString().trim())) {
                        Toast.makeText(this, "两次密码输入不相同", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
        StyledDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StyledDialog.dismissLoading();
        if (dialogs != null && dialogs.isShowing()) {
            dialogs.dismiss();
        }

    }
}
