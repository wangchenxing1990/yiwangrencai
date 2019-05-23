package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
 * Created by Administrator on 2017/5/27.
 */

public class AddWorkExpActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_add_work_experience;
    }

    @Override
    public void initData() {

    }

    private FrameLayout iv_back_basic;
    private TextView tv_next_basic;
    private RelativeLayout rl_company_name;
    private RelativeLayout rl_industry_involved;
    private RelativeLayout rl_natrue_business;
    private RelativeLayout rl_scale_company;
    private RelativeLayout rl_entry_time;
    private RelativeLayout rl_dimission;
    private RelativeLayout rl_work_name;
    private RelativeLayout rl_work_discriub;

    private TextView tv_input_company_name;
    private TextView tv_suoshu_hangye;
    private TextView tv_select_natrue_business;
    private TextView tv_select_company_scale;
    private TextView tv_entry_time;
    private TextView tv_leave_time;
    private TextView tv_input_work_names;
    private TextView tv_describe_work;

    String id;
    String api_token;

    @Override
    public void initView() {
        SharedPreferences share = getSharedPreferences("data", MODE_PRIVATE);
        id = share.getString("resume_id", "");
        SharedPreferences shared = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = shared.getString("api_token", "");
        tv_next_basic = (TextView) findViewById(R.id.tv_next_basic);
        iv_back_basic = (FrameLayout) findViewById(R.id.iv_back_basic);

        rl_company_name = (RelativeLayout) findViewById(R.id.rl_company_name);
        rl_industry_involved = (RelativeLayout) findViewById(R.id.rl_industry_involved);
        rl_natrue_business = (RelativeLayout) findViewById(R.id.rl_natrue_business);
        rl_scale_company = (RelativeLayout) findViewById(R.id.rl_scale_company);
        rl_entry_time = (RelativeLayout) findViewById(R.id.rl_entry_time);
        rl_dimission = (RelativeLayout) findViewById(R.id.rl_dimission);
        rl_work_name = (RelativeLayout) findViewById(R.id.rl_work_name);
        rl_work_discriub = (RelativeLayout) findViewById(R.id.rl_work_discriub);

        tv_input_company_name = (TextView) findViewById(R.id.tv_input_company_name);
        tv_suoshu_hangye = (TextView) findViewById(R.id.tv_suoshu_hangye);
        tv_select_natrue_business = (TextView) findViewById(R.id.tv_select_natrue_business);
        tv_select_company_scale = (TextView) findViewById(R.id.tv_select_company_scale);
        tv_entry_time = (TextView) findViewById(R.id.tv_entry_time);
        tv_leave_time = (TextView) findViewById(R.id.tv_leave_time);
        tv_input_work_names = (TextView) findViewById(R.id.tv_input_work_names);
        tv_describe_work = (TextView) findViewById(R.id.tv_describe_work);


        iv_back_basic.setOnClickListener(this);
        tv_next_basic.setOnClickListener(this);
        rl_company_name.setOnClickListener(this);
        rl_industry_involved.setOnClickListener(this);
        rl_natrue_business.setOnClickListener(this);
        rl_scale_company.setOnClickListener(this);
        rl_entry_time.setOnClickListener(this);
        rl_dimission.setOnClickListener(this);
        rl_work_name.setOnClickListener(this);
        rl_work_discriub.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_basic://返回
                finish();
                break;
            case R.id.tv_next_basic://保存数据
                saveDataToService();//保存数据到服务器
                break;
            case R.id.rl_company_name://公司名称
                Intent companyIntent = new Intent(AddWorkExpActivity.this, NameActivity.class);
                companyIntent.putExtra("params", "companyName");
                companyIntent.putExtra("mobile", companyName);

                startActivityForResult(companyIntent, 40);
                break;
            case R.id.rl_industry_involved://所属行业
                Intent intentJob = new Intent(AddWorkExpActivity.this, EducationActivity.class);
                intentJob.putExtra("education", "industry");
                intentJob.putExtra("params","experiencesss");
                intentJob.putExtra("company_name",industryid);
                startActivityForResult(intentJob, 62);
                break;
            case R.id.rl_natrue_business://公司性质
                Intent intent = new Intent(AddWorkExpActivity.this, EducationActivity.class);
                intent.putExtra("education", "natrueBusiness");
                intent.putExtra("company_name", natrueBusiness);
                startActivityForResult(intent, 42);
                break;
            case R.id.rl_scale_company://公司规模
                Intent intentScale = new Intent(AddWorkExpActivity.this, EducationActivity.class);
                intentScale.putExtra("education", "companyScale");
                intentScale.putExtra("company_name", companyScale);
                startActivityForResult(intentScale, 43);
                break;
            case R.id.rl_entry_time://入职时间
                initTimePicker("1");
                break;
            case R.id.rl_dimission://离职时间
                initTimePicker("2");
                break;
            case R.id.rl_work_name://职位名称
                Intent workNames = new Intent(AddWorkExpActivity.this, NameActivity.class);
                workNames.putExtra("params", "workName");
                workNames.putExtra("mobile", post);
                startActivityForResult(workNames, 41);
                break;
            case R.id.rl_work_discriub://工作描述
                Intent intentdescribe = new Intent(AddWorkExpActivity.this, DescribeActivity.class);
                intentdescribe.putExtra("params", "describework");
                intentdescribe.putExtra("introduction", content);
                startActivityForResult(intentdescribe, 61);
                break;
        }
    }

    /**
     * 保存数据到服务器
     */
    String strss;
    int code;
    MyPopuwindown mypopuwind;

    private void saveDataToService() {
        if (companyName == null || "".equals(companyName)) {
            Toast.makeText(AddWorkExpActivity.this, "请输入公司名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (industry == null || "".equals(industry)) {
            Toast.makeText(AddWorkExpActivity.this, "请选择所属行业", Toast.LENGTH_SHORT).show();
            return;
        }
        if (comkind == null || "".equals(comkind)) {
            Toast.makeText(AddWorkExpActivity.this, "请选择公司类型", Toast.LENGTH_SHORT).show();
            return;
        }
        if (scale == null || "".equals(scale)) {
            Toast.makeText(AddWorkExpActivity.this, "请选择公司规模", Toast.LENGTH_SHORT).show();
            return;
        }
        if (starttime == null || "".equals(starttime)) {
            Toast.makeText(AddWorkExpActivity.this, "请选择入职时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (endtime == null || "".equals(endtime)) {
            Toast.makeText(AddWorkExpActivity.this, "请选择离职时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (post == null || "".equals(post)) {
            Toast.makeText(AddWorkExpActivity.this, "请输入值为名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (content == null || "".equals(content)) {
            Toast.makeText(AddWorkExpActivity.this, "请输入工作描述", Toast.LENGTH_SHORT).show();
            return;
        }

        StyledDialog.buildLoading().show();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        formEncodingBuilder.add("resume_id", id);
        formEncodingBuilder.add("company", companyName);
        formEncodingBuilder.add("industry", industry);
        formEncodingBuilder.add("comkind", comkind);
        formEncodingBuilder.add("scale", scale);
        formEncodingBuilder.add("starttime", starttime + "-01");
        formEncodingBuilder.add("endtime", endtime + "-01");
        formEncodingBuilder.add("post", post);
        formEncodingBuilder.add("content", content);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_WORKEXP_CREATE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncodingBuilder.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (e.getMessage() != null) {
                    handler.sendEmptyMessage(110);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                strss = response.body().string();
                code = response.code();
                Log.i("aaaaaaaaaa", strss);
                handler.sendEmptyMessage(100);
            }
        });
    }

    String companyName="";
    String industry;
    String comkind;
    String scale;
    String post;
    String content;
    String starttime;
    String endtime;
    String natrueBusiness="";
    String companyScale="";
    String industryid="";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            switch (requestCode) {
                case 40:
                    companyName = data.getExtras().getString("companyName");
                    if (companyName==null||"".equals(companyName)){

                    }else{
                        tv_input_company_name.setText(companyName);
                    }

                    break;
                case 62:
                    industryid = data.getExtras().getString("list");
                    industry = data.getExtras().getString("listId");
                    if (industryid==null||"".equals(industryid)){

                    }else{
                        tv_suoshu_hangye.setText(industryid);
                    }

                    break;
                case 42:
                    natrueBusiness = data.getExtras().getString("natrueBusiness");
                    comkind = data.getExtras().getString("natrueBusinessId");
                    if(natrueBusiness==null||"".equals(natrueBusiness)){

                    }else{
                        tv_select_natrue_business.setText(natrueBusiness);
                    }

                    break;
                case 43:
                    companyScale= data.getExtras().getString("companyScale");
                    scale = data.getExtras().getString("companyScaleId");
                    if (companyScale==null||"".equals(companyScale)){

                    }else{
                        tv_select_company_scale.setText(companyScale);
                    }

                    break;
                case 41:
                    post = data.getExtras().getString("workName");
                    if (post==null||"".equals(post)){

                    }else{
                        tv_input_work_names.setText(post);
                    }

                    break;
                case 61:
                    content = data.getExtras().getString("describe");
                    if (content==null||"".equals(content)){

                    }else{
                        tv_describe_work.setText(content);
                    }
                    break;
            }
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    if (code == 200) {
                        JSONObject josn = JSON.parseObject(strss);
                        String codes = josn.getString("code");
                        if ("1".equals(codes)) {
                            StyledDialog.dismissLoading();
                            Intent intent = new Intent(AddWorkExpActivity.this, EditResumeTwoActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            StyledDialog.dismissLoading();
                            Toast.makeText(AddWorkExpActivity.this, "工作经历添加成功", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        StyledDialog.dismissLoading();
                        Toast.makeText(AddWorkExpActivity.this, "工作经历添加失败", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 110:
                    Toast.makeText(AddWorkExpActivity.this, "工作经历添加失败", Toast.LENGTH_SHORT).show();
                    StyledDialog.dismissLoading();
                    break;
            }
        }
    };
    TimePickerView pvTime;

    private void initTimePicker(final String flags) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1960, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 1, 1);
        pvTime = new TimePickerView.Builder(AddWorkExpActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String time = UiUtils.getTime(date.toString());
                if (flags.equals("1")) {
                    tv_entry_time.setText(time);
                    starttime = time;
                } else if (flags.equals("2")) {
                    tv_leave_time.setText(time);
                    endtime = time;
                }
            }
        }).setType(TimePickerView.Type.YEAR_MONTH)
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }
}
