package com.yiwangrencai.ywkj.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
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
import com.yiwangrencai.ywkj.view.MySexPopuwin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/6.
 */

public class AddSkillItemActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_skill_item;
    }

    private String id;
    private String api_token;

    @Override
    public void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        SharedPreferences share=getSharedPreferences("Activity",MODE_PRIVATE);
        api_token = share.getString("api_token","");

        //获取数据
        gainSillItemData();
    }

    String str;
    int code;

    /**
     * 获取条目的数据
     */
    private void gainSillItemData() {
        OkHttpClient okhttpclient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_SKILL_EDIT)
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
                str = response.body().string();
                code = response.code();
                System.out.println("........." + str);
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
                        JSONObject json = JSON.parseObject(str);
                        String codes = json.getString("code");
                        if (codes.equals("1")) {
                            JSONObject jsonData = json.getJSONObject("data");
                            parasenJsonData(jsonData);
                        } else {
                            Toast.makeText(AddSkillItemActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(AddSkillItemActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 300:
                    if (code == 200) {
                        JSONObject json = JSON.parseObject(strss);
                        String codes = json.getString("code");
                        if (codes.equals("1")) {
                            String jsonData = json.getString("msg");
                            Toast.makeText(AddSkillItemActivity.this, jsonData, Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(AddSkillItemActivity.this,EditResumeTwoActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AddSkillItemActivity.this, "技能更新失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(AddSkillItemActivity.this, "技能更新失败", Toast.LENGTH_SHORT).show();
                    }

                    StyledDialog.dismissLoading();
                    break;

            }
        }
    };

    private String skillname;
    private String degree;

    /**
     * 解析数据
     *
     * @param jsonData
     */
    private void parasenJsonData(JSONObject jsonData) {
        skillname = jsonData.getString("skillname");
        degree = jsonData.getString("degree");

        tv_input_skill_name.setText(skillname);

        if ("1".equals(degree)) {
            tv_skill_level.setText("入门");
        } else if ("2".equals(degree)) {
            tv_skill_level.setText("熟练");
        } else if ("3".equals(degree)) {
            tv_skill_level.setText("精通");
        }

    }

    private TextView tv_input_skill_name;
    private TextView tv_skill_level;

    @Override
    public void initView() {
        FrameLayout fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        TextView tv_add_save = (TextView) findViewById(R.id.tv_add_save);

        RelativeLayout rl_skill_name = (RelativeLayout) findViewById(R.id.rl_skill_name);
        RelativeLayout rl_skill_degree = (RelativeLayout) findViewById(R.id.rl_skill_degree);
        LinearLayout linearLayout_delete = (LinearLayout) findViewById(R.id.linearLayout_delete);

        tv_input_skill_name = (TextView) findViewById(R.id.tv_input_skill_name);
        tv_skill_level = (TextView) findViewById(R.id.tv_skill_level);

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
                saveDataToService();//保存数据到服务器
                break;
            case R.id.rl_skill_name://技能名称
                Intent intent = new Intent(AddSkillItemActivity.this, NameActivity.class);
                intent.putExtra("params", "skill");
                intent.putExtra("mobile", skillname);
                startActivityForResult(intent, 301);
                break;
            case R.id.rl_skill_degree://熟练程度
                showDegreePopuWind();//熟练程度
                break;
            case R.id.linearLayout_delete://删除
                deleteDataSkill();//删除技能数据
                break;
        }
    }

    /**
     * 删除技能条目的数据
     */
    private void deleteDataSkill() {
       StyledDialog.buildLoading().show();
        OkHttpClient okhttpclient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_SKILL_DELETE)
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
                System.out.print("！！！！！！！" + strss);
                handler.sendEmptyMessage(300);
            }
        });
    }

    String strss;
    MyPopuwindown myPopuwStatu;
    /**
     * 保存数据到服务器
     */
    private void saveDataToService() {
        StyledDialog.buildLoading().show();
        OkHttpClient okhttpclient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);
        formEncoding.add("skillname", skillname);
        formEncoding.add("degree", degree);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_SKILL_UPDATE)
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


    /**
     * 显示熟练程度
     */
    OptionsPickerView pvOptions;
    String flag;
    private void showDegreePopuWind() {
        final List<String> options1Items = new ArrayList<>();
        options1Items.add("入门");
        options1Items.add("熟练");
        options1Items.add("精通");
//        options1Items.add("其他");
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);
                tv_skill_level.setText(tx);
                if (options1 == 0) {
                    degree = "1";
                } else if (options1 == 1) {
                    degree = "2";
                } else if (options1 == 2) {
                    degree = "3";
                }
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("熟练程度")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFffffff)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
//                .setLabels("省", "市", "区")//设置选择的三级单位
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .build();

        pvOptions.setPicker(options1Items);//添加数据源
        pvOptions.show();
    }

    /**
     * 开启一个界面得到的返回值
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            switch (requestCode) {
                case 301:
                    skillname = data.getExtras().getString("skill");
                    tv_input_skill_name.setText(skillname);
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
