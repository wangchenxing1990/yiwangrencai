package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import java.io.IOException;

/**
 * Created by Administrator on 2017/5/6.
 */

public class AddOtherItemActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_other_item;
    }

    private String id;

    @Override
    public void initData() {
        id = getIntent().getStringExtra("id");
        gainDataOther();//获取数据
    }

    String str;
    int code;

    /**
     * 获取数据
     */
    String api_token;
    private void gainDataOther() {
        SharedPreferences share=getSharedPreferences("Activity",MODE_PRIVATE);
        api_token=share.getString("api_token","");
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formcing = new FormEncodingBuilder();
        formcing.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL+ContentUrl.RESUME_OTHER_EDIT)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR+api_token)
                .post(formcing.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("7789///**********",str);
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
                    if (code==200){
                        JSONObject json= JSON.parseObject(str);
                        String codes=json.getString("code");
                        String mess=json.getString("msg");
                        if(codes.equals("1")){
                            paraseJsonData(json.getJSONObject("data"));
                        }else{
                            Toast.makeText(AddOtherItemActivity.this,mess,Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(AddOtherItemActivity.this,"获取数据失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 300:
                    if (code == 200) {
                        JSONObject json = JSON.parseObject(strss);
                        String codess = json.getString("code");
                        if (codess.equals("1")) {
                            String jsonData = json.getString("msg");
                            Toast.makeText(AddOtherItemActivity.this, jsonData, Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(AddOtherItemActivity.this,EditResumeTwoActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AddOtherItemActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(AddOtherItemActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
//                    myPopuwStatu.dismiss();
                    StyledDialog.dismissLoading();
                    break;
            }
        }
    };

    String title;
    String content;
    /**
     * 解析数据
     * @param data
     */
    private void paraseJsonData(JSONObject data) {
        title=data.getString("title");
        content=data.getString("content");

        tv_input_skill_name.setText(title);
        tv_skill_level.setText(content);
    }
    TextView tv_input_skill_name;
    TextView tv_skill_level;
    @Override
    public void initView() {
        FrameLayout fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        TextView tv_add_save = (TextView) findViewById(R.id.tv_add_save);
        RelativeLayout rl_skill_name = (RelativeLayout) findViewById(R.id.rl_skill_name);
        RelativeLayout rl_skill_degree = (RelativeLayout) findViewById(R.id.rl_skill_degree);
        LinearLayout linearLayout_delete = (LinearLayout) findViewById(R.id.linearLayout_delete);

        tv_input_skill_name=(TextView)findViewById(R.id.tv_input_skill_name);
        tv_skill_level=(TextView)findViewById(R.id.tv_skill_level);
        fram_job_back.setOnClickListener(this);
        tv_add_save.setOnClickListener(this);
        rl_skill_name.setOnClickListener(this);
        rl_skill_degree.setOnClickListener(this);
        linearLayout_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back://返回
                finish();
                break;
            case R.id.tv_add_save://保存
                saveUpdataOther();//保存数据
                break;
            case R.id.rl_skill_name://主题
                Intent intent=new Intent(AddOtherItemActivity.this,EducationActivity.class);
                intent.putExtra("education","other");
                intent.putExtra("company_name",title);
                startActivityForResult(intent,60);

                break;
            case R.id.rl_skill_degree://描述
                Intent intentDes=new Intent(AddOtherItemActivity.this,DescribeActivity.class);
                intentDes.putExtra("params","other");
                intentDes.putExtra("introduction",content);
                startActivityForResult(intentDes,300);
                break;
            case R.id.linearLayout_delete://删除
                deleteOtherData();//删除
                break;
        }
    }

    /**
     * 保存数据
     */
    private void saveUpdataOther() {
//        myPopuwStatu=new MyPopuwindown(AddOtherItemActivity.this,R.layout.my_popuwindown);
//        myPopuwStatu.showAtLocation(AddOtherItemActivity.this.findViewById(R.id.ll_popuwind), Gravity.CENTER,0,0);
        StyledDialog.buildLoading().show();
        OkHttpClient okhttpclient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);
        formEncoding.add("title", title);
        formEncoding.add("content", content);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_OTHER_UPDATE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR+api_token)
                .post(formEncoding.build())
                .build();
        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strss = response.body().string();
                code = response.code();
                System.out.print("?????????" + strss);
                handler.sendEmptyMessage(300);
            }
        });
    }
    MyPopuwindown myPopuwStatu;
    String strss;
    /**
     * 删除其他信息的条目数据
     */
    private void deleteOtherData() {
        StyledDialog.buildLoading().show();
        OkHttpClient okhttpclient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_OTHER_DELETE
                )
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR+api_token)
                .post(formEncoding.build())
                .build();
        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strss = response.body().string();
                code = response.code();
                System.out.print("?????????" + strss);
                handler.sendEmptyMessage(300);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null){

        }else{
            switch (requestCode){
                case 300:
                    content=data.getExtras().getString("other");
                    tv_skill_level.setText(content);
                    break;

                case 60:
                    title=data.getExtras().getString("other");
                    tv_input_skill_name.setText(title);
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
