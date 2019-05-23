package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.yiwangrencai.ywkj.tools.UiUtils;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import java.io.IOException;

/**
 * Created by Administrator on 2017/4/26.
 */

public class ResumeStateActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.resume_state_activity;
    }

    @Override
    public void initData() {

    }

    private RelativeLayout rl_secrecy;
    private RelativeLayout rl_open;
    private RelativeLayout rl_no_open;
    private FrameLayout fram_job_back;
    private ImageView image_one;
    private ImageView image_two;
    private ImageView image_three;

    String flag;
    String resume_status;

    @Override
    public void initView() {

        SharedPreferences share = getSharedPreferences("data", MODE_PRIVATE);
        resume_status = share.getString("resume_status", "");

        Log.i("resujme", resume_status);

        rl_secrecy = (RelativeLayout) findViewById(R.id.rl_secrecy);
        rl_no_open = (RelativeLayout) findViewById(R.id.rl_no_open);
        rl_open = (RelativeLayout) findViewById(R.id.rl_open);
        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);

        image_one = (ImageView) findViewById(R.id.image_one);
        image_two = (ImageView) findViewById(R.id.image_two);
        image_three = (ImageView) findViewById(R.id.image_three);

        rl_secrecy.setOnClickListener(this);
        rl_no_open.setOnClickListener(this);
        rl_open.setOnClickListener(this);
        fram_job_back.setOnClickListener(this);

        if ("1".equals(resume_status)) {
            image_one.setVisibility(View.VISIBLE);
            image_two.setVisibility(View.INVISIBLE);
            image_three.setVisibility(View.INVISIBLE);
        } else if ("2".equals(resume_status)) {
            image_one.setVisibility(View.INVISIBLE);
            image_two.setVisibility(View.VISIBLE);
            image_three.setVisibility(View.INVISIBLE);
        } else if ("3".equals(resume_status)) {
            image_one.setVisibility(View.INVISIBLE);
            image_two.setVisibility(View.INVISIBLE);
            image_three.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back:
                Intent intent=new Intent(ResumeStateActivity.this,MainActivity.class);
                intent.putExtra("register","register");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_open:
                resume_status = "1";
                saveResumeState(resume_status);
                break;
            case R.id.rl_no_open:
                resume_status = "2";
                saveResumeState(resume_status);
                break;
            case R.id.rl_secrecy:
                resume_status = "3";
                saveResumeState(resume_status);
                break;
        }
    }

    int code;
    String str;
    MyPopuwindown myPopuwindown;
    /**
     * 修改简历公开状态并提交给服务器
     */
    String api_token;

    private void saveResumeState(String status) {

        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");

        StyledDialog.buildLoading().show();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", UiUtils.getId());
        formEncoding.add("resume_status", status);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_STATUS)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncoding.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("?????????????????", str);
                handler.sendEmptyMessage(100);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    if (code == 200) {
                        JSONObject jsonObject = JSON.parseObject(str);
                        String codess = jsonObject.getString("code");
                        if (codess.equals("1")) {
                            String message = jsonObject.getString("msg");
                            Toast.makeText(ResumeStateActivity.this, message, Toast.LENGTH_SHORT).show();
                            //改变简历公开状态
                            switchResumeStatus();
                        } else {
                            Toast.makeText(ResumeStateActivity.this, "简历公开状态改变失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ResumeStateActivity.this, "简历公开状态改变失败", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };

    /**
     * 改变简历公开状态
     */
    private void switchResumeStatus() {
        if (resume_status.equals("1")) {
            image_one.setVisibility(View.VISIBLE);
            image_two.setVisibility(View.INVISIBLE);
            image_three.setVisibility(View.INVISIBLE);

        } else if (resume_status.equals("2")) {
            image_one.setVisibility(View.INVISIBLE);
            image_two.setVisibility(View.VISIBLE);
            image_three.setVisibility(View.INVISIBLE);
        } else if (resume_status.equals("3")) {
            image_one.setVisibility(View.INVISIBLE);
            image_two.setVisibility(View.INVISIBLE);
            image_three.setVisibility(View.VISIBLE);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("resume_status", resume_status);
        edit.commit();
    StyledDialog.dismissLoading();
    }


    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ResumeStateActivity.this,MainActivity.class);
        intent.putExtra("register","register");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
