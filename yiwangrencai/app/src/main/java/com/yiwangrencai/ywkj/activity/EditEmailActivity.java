package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
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
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/29.
 */

public class EditEmailActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.edit_email_qq_activity;
    }

    @Override
    public void initData() {

    }

    private RelativeLayout phone;
    private RelativeLayout email;
    private RelativeLayout qq;
    private FrameLayout iv_back_basic;
    private TextView tv_save_data;
    private TextView tv_input_phone;
    private TextView tv_email_input;
    private TextView tv_input_qq;
    private String emailNumber;
    private String phoneNumber;
    private String qqNumber;

    @Override
    public void initView() {

        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("mobile");
        emailNumber = intent.getStringExtra("email");
        qqNumber = intent.getStringExtra("qq");
        phone = (RelativeLayout) findViewById(R.id.rl_phone);
        email = (RelativeLayout) findViewById(R.id.rl_email);
        qq = (RelativeLayout) findViewById(R.id.rl_qq);
        iv_back_basic = (FrameLayout) findViewById(R.id.iv_back_basic);
        tv_save_data = (TextView) findViewById(R.id.tv_next_basic);

        tv_input_phone = (TextView) findViewById(R.id.tv_input_phone);
        tv_email_input = (TextView) findViewById(R.id.tv_email_input);
        tv_input_qq = (TextView) findViewById(R.id.tv_input_qq);

        tv_input_phone.setText(phoneNumber);
        tv_email_input.setText(emailNumber);
        tv_input_qq.setText(qqNumber);

        phone.setOnClickListener(this);
        email.setOnClickListener(this);
        qq.setOnClickListener(this);
        iv_back_basic.setOnClickListener(this);
        tv_save_data.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back_basic://返回上一界面
                finish();
                break;
            case R.id.rl_phone://手机号码
                Intent intentPhone = new Intent(this, NameActivity.class);
                intentPhone.putExtra("params", "phone");
                intentPhone.putExtra("mobile", phoneNumber);
                startActivityForResult(intentPhone, 12);
                break;
            case R.id.rl_qq://qq号码
                Intent intentQQ = new Intent(this, NameActivity.class);
                intentQQ.putExtra("params", "qq");
                intentQQ.putExtra("mobile", qqNumber);
                startActivityForResult(intentQQ, 14);
                break;
            case R.id.rl_email://邮箱
                Intent intentEmail = new Intent(this, NameActivity.class);
                intentEmail.putExtra("params", "email");
                intentEmail.putExtra("mobile", emailNumber);
                startActivityForResult(intentEmail, 13);
                break;
            case R.id.tv_next_basic://保存按钮
                //显示上传数据的状态
                showUpdataPopuwind();
                break;
        }
    }

    String str;
    MyPopuwindown myPopuwindown;
    private String PHONE="^1\\d{10}$";
    private void showUpdataPopuwind() {
        if(phoneNumber==null||phoneNumber.isEmpty()){
            Toast.makeText(EditEmailActivity.this,"请输入手机号码",Toast.LENGTH_SHORT).show();
            return ;
        }

        Pattern pattern = Pattern.compile(ContentUrl.PHONE_PARTTEN);
        Matcher matcher = pattern.matcher(phoneNumber);

        if (!matcher.matches()) {
            Toast.makeText(EditEmailActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        StyledDialog.buildLoading().show();
        SharedPreferences shares = getSharedPreferences("data", MODE_PRIVATE);
        String id = shares.getString("resume_id", "");
        SharedPreferences sharess = getSharedPreferences("Activity", MODE_PRIVATE);
        String api_token = sharess.getString("api_token", "");
        OkHttpClient okhttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);
        formEncoding.add("mobile", phoneNumber);
        formEncoding.add("email", emailNumber);
        formEncoding.add("qq", qqNumber);
        Request request = new Request.Builder().addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_CONN)
                .post(formEncoding.build())
                .build();
        okhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                if (e.getMessage()!=null){
                    handler.sendEmptyMessage(1200);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                System.out.println("666666666666" + str);
                handler.sendEmptyMessage(1000);

            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    JSONObject jsonObject= JSON.parseObject(str);
                    String code=jsonObject.getString("code");
                    if (code.equals("1")){

                        SharedPreferences share=getSharedPreferences("data",MODE_PRIVATE);
                        SharedPreferences.Editor editor=share.edit();
                        editor.putString("resume_mobile",phoneNumber);
                        editor.commit();

                        Intent intent=new Intent(EditEmailActivity.this,EditResumeTwoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        finish();

                    }
                    StyledDialog.dismissLoading();
                    break;
                case 1200:
                    //myPopuwindown.dismiss();
                    StyledDialog.dismissLoading();
                    Toast.makeText(EditEmailActivity.this,"更新数据失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            switch (requestCode) {
                case 12:
                    phoneNumber = data.getExtras().getString("phone");
                    tv_input_phone.setText(phoneNumber);
                    break;
                case 13:
                    emailNumber = data.getExtras().getString("email");
                    tv_email_input.setText(emailNumber);
                    break;
                case 14:
                    qqNumber = data.getExtras().getString("qq");
                    tv_input_qq.setText(qqNumber);
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }
}
