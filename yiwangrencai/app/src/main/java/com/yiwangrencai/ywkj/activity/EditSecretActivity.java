package com.yiwangrencai.ywkj.activity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.yiwangrencai.ywkj.adapter.MyCompayBlackAdapter;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.jmessage.CropImageView;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import java.io.IOException;

/**
 * Created by Administrator on 2017/5/11.
 */

public class EditSecretActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_secret;
    }

    @Override
    public void initData() {

    }

    private FrameLayout iv_back_basic;
    private TextView tv_job_collection;
    private EditText editsecretold;
    private EditText editsecretone;
    private EditText editsecrettwo;
    String api_token;
    @Override
    public void initView() {
        SharedPreferences share=getSharedPreferences("Activity",MODE_PRIVATE);
        api_token=share.getString("api_token","");
        iv_back_basic = (FrameLayout) findViewById(R.id.iv_back_basic);
        tv_job_collection = (TextView) findViewById(R.id.tv_job_collection);
        editsecretold = (EditText) findViewById(R.id.editsecretold);
        editsecretone = (EditText) findViewById(R.id.editsecretone);
        editsecrettwo = (EditText) findViewById(R.id.editsecrettwo);

        iv_back_basic.setOnClickListener(this);
        tv_job_collection.setOnClickListener(this);
    }

    String oldcontent;
    String onecontent;
    String twocontent;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_basic://返回
                finish();
                break;
            case R.id.tv_job_collection://保存
                oldcontent = editsecretold.getText().toString().trim();
                onecontent = editsecretone.getText().toString().trim();
                twocontent = editsecrettwo.getText().toString().trim();
                saveData();//向服务器保存数据
                break;
        }
    }

    /**
     * 向服务器保存数据
     */
    private void saveData() {
        if (oldcontent.isEmpty()) {
            Toast.makeText(this, "旧密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (onecontent.isEmpty()) {
            Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (onecontent.length() < 6) {
            Toast.makeText(this, "新密码的字数要大于六位", Toast.LENGTH_SHORT).show();
            return;
        }
        if (twocontent.isEmpty()) {
            Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (twocontent.length() < 6) {
            Toast.makeText(this, "新密码的字数要大于六位", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oldcontent.equals(twocontent)) {
            Toast.makeText(this, "两次密码不相同", Toast.LENGTH_SHORT).show();
            return;
        }
        submitData();
    }

    String str;
    int code;
    /**
     * 提交数据
     */
    MyPopuwindown myPopuwindown;
    private void submitData() {
//        myPopuwindown=new MyPopuwindown(EditSecretActivity.this,R.layout.my_popuwindown);
//        myPopuwindown.showAtLocation(EditSecretActivity.this.findViewById(R.id.ll_popuwind), Gravity.CENTER,0,0);
        StyledDialog.buildLoading().show();
        OkHttpClient okhttpclient = new OkHttpClient();
        FormEncodingBuilder formEcding = new FormEncodingBuilder();
        formEcding.add("oldpwd", oldcontent);
        formEcding.add("newpwd", twocontent);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.USER_PASSWORD_RESET)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR+api_token)
                .post(formEcding.build())
                .build();
        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (e.getMessage() != null) {
                    handler.sendEmptyMessage(110);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("11111111", str);
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
                        JSONObject json= JSON.parseObject(str);
                        String codes=json.getString("code");
//                        myPopuwindown.dismiss();
                        StyledDialog.dismissLoading();
                        if (codes.equals("1")){
                            Toast.makeText(EditSecretActivity.this,json.getString("msg"),Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(EditSecretActivity.this,json.getString("msg"),Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditSecretActivity.this,"修改密码失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 110:
                    StyledDialog.dismissLoading();
                    Toast.makeText(EditSecretActivity.this, "联网超时请重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }
}
