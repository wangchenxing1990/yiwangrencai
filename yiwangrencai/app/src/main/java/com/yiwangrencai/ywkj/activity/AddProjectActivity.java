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
import com.bigkoo.pickerview.TimePickerView;
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
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/3.
 */
public class AddProjectActivity extends BaseActiviyt implements View.OnClickListener {
    private FrameLayout fram_job_back;
    private TextView tv_add_save;
    private RelativeLayout rl_project_name;
    private RelativeLayout rl_time_start;
    private RelativeLayout rl_time_end;
    private RelativeLayout rl_project_work;
    private RelativeLayout rl_discrub_project;

    @Override
    public int getLayoutId() {
        return R.layout.add_project_activity;
    }

    @Override
    public void initData() {

    }

    private TextView tv_input_project_name;
    private TextView tv_project_start;
    private TextView tv_project_end;
    private TextView tv_input_project_work;
    private TextView tv_describe_project;

    @Override
    public void initView() {

        initTimePicker();
        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        tv_add_save = (TextView) findViewById(R.id.tv_add_save);

        rl_project_name = (RelativeLayout) findViewById(R.id.rl_project_name);
        rl_time_start = (RelativeLayout) findViewById(R.id.rl_time_start);
        rl_time_end = (RelativeLayout) findViewById(R.id.rl_time_end);
        rl_project_work = (RelativeLayout) findViewById(R.id.rl_project_work);
        rl_discrub_project = (RelativeLayout) findViewById(R.id.rl_discrub_project);

        tv_input_project_name = (TextView) findViewById(R.id.tv_input_project_name);
        tv_project_start = (TextView) findViewById(R.id.tv_project_start);
        tv_project_end = (TextView) findViewById(R.id.tv_project_end);
        tv_input_project_work = (TextView) findViewById(R.id.tv_input_project_work);
        tv_describe_project = (TextView) findViewById(R.id.tv_describe_project);
        fram_job_back.setOnClickListener(this);
        tv_add_save.setOnClickListener(this);

        rl_project_name.setOnClickListener(this);
        rl_time_start.setOnClickListener(this);
        rl_time_end.setOnClickListener(this);
        rl_project_work.setOnClickListener(this);
        rl_discrub_project.setOnClickListener(this);

    }

    String flag;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back://返回上一个界面
                finish();
                break;
            case R.id.tv_add_save://保存数据
                //保存数据
                saveData();
                break;
            case R.id.rl_project_name://项目名称
                Intent projectIntent = new Intent(AddProjectActivity.this, NameActivity.class);
                projectIntent.putExtra("params", "projectName");
                projectIntent.putExtra("mobile", projectName);
                startActivityForResult(projectIntent, 300);
                break;
            case R.id.rl_time_start://项目的开始时间
                pvTime.show();
                flag = "start";
                break;
            case R.id.rl_time_end://项目的结束时间
                pvTime.show();
                flag = "end";
                break;
            case R.id.rl_project_work://项目中担任的职位
                Intent projectIntents = new Intent(AddProjectActivity.this, NameActivity.class);
                projectIntents.putExtra("params", "projectwork");
                projectIntents.putExtra("mobile", projectWork);
                startActivityForResult(projectIntents, 301);
                break;
            case R.id.rl_discrub_project://项目描述
                Intent discribeProject = new Intent(AddProjectActivity.this, DescribeActivity.class);
                discribeProject.putExtra("params", "projectDiscrube");
                discribeProject.putExtra("introduction", projectDiscrube);
                startActivityForResult(discribeProject, 302);
                break;
        }
    }

    MyPopuwindown mywiond;

    /**
     * 保存数据
     */
    private void saveData() {

        if (projectName==null){
            Toast.makeText(AddProjectActivity.this,"请输入项目名称",Toast.LENGTH_SHORT).show();
            return;
        }
        if (projectStartTime==null || "".equals(projectStartTime)){
            Toast.makeText(AddProjectActivity.this,"请输入开始时间",Toast.LENGTH_SHORT).show();
            return;
        }

        if (projectEndTime==null || "".equals(projectEndTime)){
            Toast.makeText(AddProjectActivity.this,"请输入结束时间",Toast.LENGTH_SHORT).show();
            return;
        }

        if (projectWork==null || "".equals(projectWork)){
            Toast.makeText(AddProjectActivity.this,"请输入担任职位",Toast.LENGTH_SHORT).show();
            return;
        }

        StyledDialog.buildLoading().show();
        SharedPreferences share = getSharedPreferences("data", MODE_PRIVATE);
        String id = share.getString("resume_id", "");
        SharedPreferences shares = getSharedPreferences("Activity", MODE_PRIVATE);
        String api_token = shares.getString("api_token", "");
        OkHttpClient okhttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("resume_id", id);
        formEncoding.add("project_name", projectName);
        formEncoding.add("starttime", projectStartTime);
        formEncoding.add("endtime", projectEndTime);
        formEncoding.add("post", projectWork);
        if(projectDiscrube==null || "".equals(projectDiscrube)){
            formEncoding.add("content", "");
        }else{
            formEncoding.add("content", projectDiscrube);
        }


        Request request = new Request.Builder().url(ContentUrl.BASE_URL + ContentUrl.RESUME_PROEXP)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(formEncoding.build()).build();
        okhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                handler.sendEmptyMessage(1000);
                System.out.println("147852587412558" + str);
            }
        });
    }

    String str;
    int code;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    if (code == 200) {
                        JSONObject json = JSON.parseObject(str);
                        String message = json.getString("msg");
                        String code = json.getString("code");
                        if ("1".equals(code)){
                            Toast.makeText(AddProjectActivity.this, message, Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(AddProjectActivity.this,EditResumeTwoActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else if("0".equals(code)){
                            Toast.makeText(AddProjectActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(AddProjectActivity.this, "添加项目经验失败", Toast.LENGTH_SHORT).show();

                    }
                    StyledDialog.dismissLoading();
                    break;
            }
        }
    };
    String projectName;
    String projectStartTime;
    String projectEndTime="0000-00-00";
    String projectWork;
    String projectDiscrube;

    /**
     * 开启界面返回的结果数据
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
                case 300:
                    projectName = data.getExtras().getString("projectName");
                    if (projectName==null||"".equals(projectName)){

                    }else{
                        tv_input_project_name.setText(projectName);
                    }

                    break;
                case 301:
                    projectWork = data.getExtras().getString("projectwork");
                    if (projectWork==null||"".equals(projectWork)){

                    }else{
                        tv_input_project_work.setText(projectWork);
                    }

                    break;
                case 302:
                    projectDiscrube = data.getExtras().getString("describes");
                    if (projectDiscrube==null||"".equals(projectDiscrube)){

                    }else{
                        tv_describe_project.setText(projectDiscrube);
                    }

                    break;
            }
        }
    }

    private TimePickerView pvTime;

    /**
     * 选择时间
     */
    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 1, 1);
        pvTime = new TimePickerView.Builder(AddProjectActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (flag.equals("start")) {
                    projectStartTime = UiUtils.getTime(date.toString());
                    tv_project_start.setText(projectStartTime);
                    projectStartTime=projectStartTime+"-01";
                } else if (flag.equals("end")) {
                    projectEndTime = UiUtils.getTime(date.toString());
                    tv_project_end.setText(projectEndTime);
                    projectEndTime=projectEndTime+"-01";
                }

            }
        }).setType(TimePickerView.Type.YEAR_MONTH)
                .setTitleText("选择时间")
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。

    }

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }
}
