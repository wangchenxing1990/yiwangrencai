package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.hss01248.dialog.StyledDialog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.MyPopuwindown;
import com.yiwangrencai.ywkj.view.MySexPopuwin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */

public class AddSkillActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_add_skill;
    }

    @Override
    public void initData() {

    }

    private RelativeLayout rl_skill_name;
    private RelativeLayout rl_skill_degree;
    private TextView tv_input_skill_name;
    private TextView tv_skill_level;
    private TextView tv_add_save;
    private TextView title_skill;
    private TextView textskillname;
    private TextView texttimeskill;
    private FrameLayout fram_job_back;
    private String params;

    @Override
    public void initView() {


        Intent intent = getIntent();
        params = intent.getStringExtra("params");
        rl_skill_name = (RelativeLayout) findViewById(R.id.rl_skill_name);
        rl_skill_degree = (RelativeLayout) findViewById(R.id.rl_skill_degree);

        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        tv_input_skill_name = (TextView) findViewById(R.id.tv_input_skill_name);
        tv_skill_level = (TextView) findViewById(R.id.tv_skill_level);
        tv_add_save = (TextView) findViewById(R.id.tv_add_save);
        title_skill = (TextView) findViewById(R.id.title_skill);
        textskillname = (TextView) findViewById(R.id.textskillname);
        texttimeskill = (TextView) findViewById(R.id.texttimeskill);

        rl_skill_name.setOnClickListener(this);
        rl_skill_degree.setOnClickListener(this);
        tv_add_save.setOnClickListener(this);
        fram_job_back.setOnClickListener(this);

        if (params.equals("skill")) {
            title_skill.setText("技能专长");
        } else if (params.equals("book")) {
            title_skill.setText("证书");
            textskillname.setText("证书名称");
            tv_input_skill_name.setText("请输入证书名称");
            texttimeskill.setText("获得时间");
            tv_skill_level.setText("请选择获得时间");
        } else if (params.equals("other")) {
            title_skill.setText("其他信息");
            textskillname.setText("主题");
            tv_input_skill_name.setText("请选择主题");
            texttimeskill.setText("内容描述");
            tv_skill_level.setText("请输入内容描述");
        }
    }


    String flag;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back://返回上一个界面
                finish();
                break;
            case R.id.tv_add_save://保存数据
                saveDataToServicer();//保存数据到服务器
                break;
            case R.id.rl_skill_name:
                if (params.equals("skill")) {
                    Intent intent = new Intent(AddSkillActivity.this, NameActivity.class);
                    intent.putExtra("params", "skill");
                    intent.putExtra("mobile", skill);
                    startActivityForResult(intent, 100);
                } else if (params.equals("book")) {
                    Intent intent = new Intent(AddSkillActivity.this, NameActivity.class);
                    intent.putExtra("params", "book");
                    intent.putExtra("mobile", other);
                    startActivityForResult(intent, 400);
                } else if (params.equals("other")) {
                    Intent intent = new Intent(AddSkillActivity.this, EducationActivity.class);
                    intent.putExtra("education", "other");
                    intent.putExtra("mobile", otherMessage);
                    intent.putExtra("company_name", otherMessage);
                    startActivityForResult(intent, 200);
                }

                break;
            case R.id.rl_skill_degree:

                if (params.equals("book")) {
                    initTimePicker();
                } else if (params.equals("skill")) {
                    showPopuWind();//显示熟练程度
                } else if (params.equals("other")) {
                    Intent intent = new Intent(AddSkillActivity.this, DescribeActivity.class);
                    intent.putExtra("params", "other");
                    intent.putExtra("introduction", otherMessages);
                    startActivityForResult(intent, 300);
                }
                break;
        }
    }

    String api_token;
    String id;
    MyPopuwindown myStatePopu;

    /**
     * 保存数据到服务器
     */
    private void saveDataToServicer() {
        SharedPreferences sharedPreferencess = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = sharedPreferencess.getString("api_token", "");

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        id = sharedPreferences.getString("resume_id", "");
        if (params.equals("skill")) {
            skillSaveDataService();//添加技能专长保存数据
        } else if (params.equals("book")) {
            bookSaveDataService();//保存证书数据到服务器
        } else if (params.equals("other")) {
            otherSaveDataService();//保存其他信息到服务器
        }

    }

    /**
     * 保存其他信息到服务器
     */
    private void otherSaveDataService() {


        if (otherMessage==null ||"".equals(otherMessage)){
            Toast.makeText(AddSkillActivity.this,"请输入主题",Toast.LENGTH_SHORT).show();
            return;
        }
        if (otherMessages==null ||"".equals(otherMessages)){
            Toast.makeText(AddSkillActivity.this,"请输入内容描述",Toast.LENGTH_SHORT).show();
            return;
        }
        StyledDialog.buildLoading().show();
        OkHttpClient okhttpclient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        form.add("resume_id", id);
        form.add("title", otherMessage);
        form.add("content", otherMessages);

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_OTHER_CREATE)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(form.build())
                .build();
        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strskill = response.body().string();
                code = response.code();
                Log.i("236523652365000----+++", strskill);
                handler.sendEmptyMessage(100);
            }
        });

    }

    /**
     * 保存证书数据到服务器
     */
    private void bookSaveDataService() {
        if (other==null ||"".equals(other)){
            Toast.makeText(AddSkillActivity.this,"请输入证书名",Toast.LENGTH_SHORT).show();
            return;
        }
        if (dataTime==null ||"".equals(dataTime)){
            Toast.makeText(AddSkillActivity.this,"请输入获取证书的时间",Toast.LENGTH_SHORT).show();
            return;
        }
        StyledDialog.buildLoading().show();
        OkHttpClient okhttpclient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        form.add("resume_id", id);
        form.add("certificate_name", other);
        form.add("gettime", dataTime + "-01-01");

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_CER_CREATE)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(form.build())
                .build();
        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strskill = response.body().string();
                code = response.code();
                Log.i("5566556565664646+++", strskill);
                handler.sendEmptyMessage(100);
            }
        });
    }

    String strskill;
    String strbook;
    int code;

    /**
     * 添加技能专长保存数据
     */
    private void skillSaveDataService() {

        if (skill==null ||"".equals(skill)){
            Toast.makeText(AddSkillActivity.this,"请输入技能名称",Toast.LENGTH_SHORT).show();
            return;
        }
        if (flag==null ||"".equals(flag)){
            Toast.makeText(AddSkillActivity.this,"请输入熟练程度",Toast.LENGTH_SHORT).show();
            return;
        }
        StyledDialog.buildLoading().show();
        OkHttpClient okhttpclient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        form.add("resume_id", id);
        form.add("skillname", skill);
        form.add("degree", flag);

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_SKILL_CREATE)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(form.build())
                .build();
        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strskill = response.body().string();
                code = response.code();
                Log.i("5566556565664646+++", strskill);
                handler.sendEmptyMessage(100);
            }
        });
    }



    /**
     * 显示时间
     */
    OptionsPickerView pvBookTime;
    String dataTime;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initTimePicker() {
        final List<String> list=new ArrayList<>();
        for (int i=1960;i<2020;i++){
            list.add(i+"年");
        }
        pvBookTime = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
              String  dataTimes = list.get(options1);
                if (params.equals("book")) {
                    tv_skill_level.setText(dataTimes);
                }
                dataTime=dataTimes.split("年")[0];
               // tvOptions.setText(tx);
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("获得时间")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFffffff)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
               // .setLabels("省", "市", "区")//设置选择的三级单位
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(57, 1, 1)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .build();

        pvBookTime.setPicker(list);//添加数据源
        pvBookTime.show();

    }

    /**
     * 显示熟练程度
     */
    OptionsPickerView pvOptions;

    private void showPopuWind() {
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
                    flag = "1";
                } else if (options1 == 1) {
                    flag = "2";
                } else if (options1 == 2) {
                    flag = "3";
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
//              .setLabels("省", "市", "区")//设置选择的三级单位
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .build();

        pvOptions.setPicker(options1Items);//添加数据源
        pvOptions.show();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    if (code == 200) {
                        JSONObject json = JSON.parseObject(strskill);
                        String str = json.getString("msg");
                        String code = json.getString("code");
                        if ("1".equals(code)){
                            Toast.makeText(AddSkillActivity.this, str, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddSkillActivity.this, EditResumeTwoActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else if("0".equals(code)){
                            Toast.makeText(AddSkillActivity.this, str, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(AddSkillActivity.this, "添加技能数据失败", Toast.LENGTH_SHORT).show();
                    }
                     StyledDialog.dismissLoading();
                    break;
            }
        }
    };

    String skill;
    String other;
    String otherMessage="";
    String otherMessages;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            switch (requestCode) {
                case 100:
                    skill = data.getExtras().getString("skill");
                    if (skill==null||"".equals(skill)){

                    }else{
                        tv_input_skill_name.setText(skill);
                    }

                    break;
                case 400:
                    other = data.getExtras().getString("book");
                    if (other==null||"".equals(other)){

                    }else{
                        tv_input_skill_name.setText(other);
                    }

                break;
                case 200:
                    otherMessage = data.getExtras().getString("other");
                    if (otherMessage==null||"".equals(otherMessage)){

                    }else{
                        tv_input_skill_name.setText(otherMessage);
                    }

                    break;
                case 300:
                    otherMessages = data.getExtras().getString("other");
                    if (otherMessages==null||"".equals(otherMessages)){

                    }else{
                        tv_skill_level.setText(otherMessages);
                    }

                    break;

            }
        }
    }

    /**
     * 转化获取的时间格式
     *
     * @param data
     * @return
     */
    private String getTime(String data) {
        String[] datas = data.split(" ");
        String mouth = datas[1];
        switch (mouth) {
            case "Jan":
                mouth = "01";
                break;
            case "Feb":
                mouth = "02";
                break;
            case "Mar":
                mouth = "03";
                break;
            case "Apr":
                mouth = "04";
                break;
            case "May":
                mouth = "05";
                break;
            case "Jun":
                mouth = "06";
                break;
            case "Jul":
                mouth = "07";
                break;
            case "Aug":
                mouth = "08";
                break;
            case "Sep":
                mouth = "09";
                break;
            case "Oct":
                mouth = "10";
                break;
            case "Nov":
                mouth = "11";
                break;
            case "Dec":
                mouth = "12";
                break;
        }
        String dataa = datas[datas.length - 1] + "-" + mouth;
        return dataa;
    }

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }
}
