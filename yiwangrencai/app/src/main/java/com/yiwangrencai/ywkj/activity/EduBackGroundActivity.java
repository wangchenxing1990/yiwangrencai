package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.UnicodeSetSpanner;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
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
import com.yiwangrencai.ywkj.jmessage.CropImageView;
import com.yiwangrencai.ywkj.tools.UiUtils;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */
public class EduBackGroundActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.education_background_activity;
    }

    @Override
    public void initData() {

    }

    private TextView skipNext;
    private TextView tv_next_basic;
    private RelativeLayout schoolName;
    private RelativeLayout rl_time_enrollment;
    private RelativeLayout rl_gradution_time;
    private RelativeLayout rl_major_name;
    private RelativeLayout rl_education_background;
    private RelativeLayout rl_discrub;
    private RelativeLayout rl_examination;
    private TextView tv_input_school_name;
    private TextView tv_input_major_name;
    private TextView tv_select_education;
    private TextView tv_eentry_time;
    private TextView tv_gradution_time;
    private TextView tv_toanzhao;
    private TextView tv_education_describe;
    private TextView tv_next_intension;
    private LinearLayout ll_popuwind;
    String id;

    @Override
    public void initView() {

        skipNext = (TextView) findViewById(R.id.tv_next_skip);
        tv_next_basic = (TextView) findViewById(R.id.tv_next_basic);

        schoolName = (RelativeLayout) findViewById(R.id.rl_school_name);
        rl_time_enrollment = (RelativeLayout) findViewById(R.id.rl_time_enrollment);
        rl_gradution_time = (RelativeLayout) findViewById(R.id.rl_gradution_time);
        rl_major_name = (RelativeLayout) findViewById(R.id.rl_major_name);
        rl_examination = (RelativeLayout) findViewById(R.id.rl_examination);
        rl_education_background = (RelativeLayout) findViewById(R.id.rl_education_background);
        rl_discrub = (RelativeLayout) findViewById(R.id.rl_discrub);
        ll_popuwind = (LinearLayout) findViewById(R.id.ll_popuwind);
        tv_eentry_time = (TextView) findViewById(R.id.tv_eentry_time);
        tv_gradution_time = (TextView) findViewById(R.id.tv_gradution_time);
        tv_education_describe = (TextView) findViewById(R.id.tv_education_describe);

        tv_input_school_name = (TextView) findViewById(R.id.tv_input_school_name);
        tv_input_major_name = (TextView) findViewById(R.id.tv_input_major_name);
        tv_select_education = (TextView) findViewById(R.id.tv_select_education);
        tv_toanzhao = (TextView) findViewById(R.id.tv_toanzhao);

        skipNext.setOnClickListener(this);
        tv_next_basic.setOnClickListener(this);

        schoolName.setOnClickListener(this);
        rl_time_enrollment.setOnClickListener(this);
        rl_gradution_time.setOnClickListener(this);
        rl_major_name.setOnClickListener(this);
        rl_education_background.setOnClickListener(this);
        rl_discrub.setOnClickListener(this);
        rl_examination.setOnClickListener(this);


    }

    private String flags;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next_skip://跳过本界面
                Intent workExpersion = new Intent(EduBackGroundActivity.this, WorkExpActivity.class);
                workExpersion.putExtra("params", "experience");
                startActivity(workExpersion);
                break;
            case R.id.tv_next_basic://点击下一步进入下一个界面
                nextView();
                break;
            case R.id.rl_school_name://学校名称
                Intent intentSchool = new Intent(EduBackGroundActivity.this, NameActivity.class);
                intentSchool.putExtra("params", "schoolname");
                intentSchool.putExtra("mobile", schoolNames);
                startActivityForResult(intentSchool, 30);
                break;
            case R.id.rl_time_enrollment://入学时间
                initTimePicker("1");
                break;
            case R.id.rl_gradution_time://毕业时间
                initTimePicker("2");
                break;
            case R.id.rl_major_name://专业名称
                Intent intentmajor = new Intent(EduBackGroundActivity.this, NameActivity.class);
                intentmajor.putExtra("params", "majorr");
                intentmajor.putExtra("mobile", major);
                startActivityForResult(intentmajor, 31);
                break;
            case R.id.rl_education_background://教育背景学历
                Intent intentEdu = new Intent(EduBackGroundActivity.this, EducationActivity.class);
                intentEdu.putExtra("education", "educationBackground");
                intentEdu.putExtra("company_name", educations);
                startActivityForResult(intentEdu, 32);
                break;
            case R.id.rl_examination://是否统招
                showPopuwindTong();
                break;
            case R.id.rl_discrub://描述
                Intent intentDescribe = new Intent(EduBackGroundActivity.this, DescribeActivity.class);
                intentDescribe.putExtra("params", "describe");
                intentDescribe.putExtra("introduction", describe);
                startActivityForResult(intentDescribe, 60);
                break;
        }
    }

    /**
     * 显示是否统招
     */
    OptionsPickerView pvOptions;

    private void showPopuwindTong() {
        final List<String> options1Items = new ArrayList<>();
        options1Items.add("是");
        options1Items.add("否");
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);
                tv_toanzhao.setText(tx);
                if (options1 == 0) {
                    type = "0";
                } else if (options1 == 1) {
                    type = "1";
                }
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市选择")//标题
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
                .setSelectOptions(0, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                //  .isDialog(true)//是否显示为对话框样式
                .build();

        pvOptions.setPicker(options1Items);//添加数据源
        pvOptions.show();
    }

    private String schoolNames="";
    private String major="";
    private String educations="";
    private String educationid;

    private String starttime;
    private String finishtime="0000-00-00";
    private String describe="";
    private String tong;

    private void nextView() {

        if (schoolNames == null || "".equals(schoolNames)) {
            Toast.makeText(EduBackGroundActivity.this, "请输入学校名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (starttime == null || "".equals(starttime)) {
            Toast.makeText(EduBackGroundActivity.this, "请选择入学时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (finishtime == null || "".equals(finishtime)) {
            Toast.makeText(EduBackGroundActivity.this, "请选择毕业时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (major == null || "".equals(major)) {
            Toast.makeText(EduBackGroundActivity.this, "请输入专业名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (educations == null || "".equals(educations)) {
            Toast.makeText(EduBackGroundActivity.this, "请选择学历", Toast.LENGTH_SHORT).show();
            return;
        }

        if (type == null || "".equals(type)) {
            Toast.makeText(EduBackGroundActivity.this, "请选择选择是否统招", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadingData();
    }

    private String str;
    private String type = "0";

    private void uploadingData() {

        StyledDialog.buildLoading().show();
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        id = sharedPreferences.getString("resume_id", "");
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        String api_token = share.getString("api_token", "");

        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("edu_type", "1");
        formEncoding.add("resume_id", id);
        formEncoding.add("school", schoolNames);
        formEncoding.add("speciality", major);
        formEncoding.add("education", educationid);
        formEncoding.add("type", type);
        formEncoding.add("starttime", starttime );
        formEncoding.add("endtime", finishtime );
        if (describe == null || "".equals(describe)) {
            formEncoding.add("description", "");
        } else {
            formEncoding.add("description", describe);
        }


        Request request = new Request.Builder().url(ContentUrl.BASE_URL + ContentUrl.RESUME_EDUEXP).addHeader("Accept", "application/json").addHeader("Authorization", "Bearer " + api_token).post(formEncoding.build()).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (e.getMessage() != null) {
                    handler.sendEmptyMessage(1200);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                code = response.code();
                str = response.body().string();
                System.out.println("3333333333311111111" + str);
                handler.sendEmptyMessage(1000);
            }
        });
    }

    /**
     * 开启activity返回来的结果
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
                case 30:
                    schoolNames = data.getExtras().getString("schoolname");
                    tv_input_school_name.setText(schoolNames);
                    break;
                case 31:
                    major = data.getExtras().getString("major");
                    tv_input_major_name.setText(major);
                    break;
                case 32:
                    educations = data.getExtras().getString("educationBackground");
                    educationid = data.getExtras().getString("educationBackgroundId");
                    tv_select_education.setText(educations);
                    break;
                case 60:
                    describe = data.getExtras().getString("describe");
                    tv_education_describe.setText(describe);
                    break;
            }

        }
    }

    int code;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    if (code == 200) {
                        JSONObject jsonObject = JSON.parseObject(str);
                        String codes = jsonObject.getString("code");
                        if ("1".equals(codes)) {
                          StyledDialog.dismissLoading();
                            Intent workExpersion = new Intent(EduBackGroundActivity.this, WorkExpActivity.class);
                            workExpersion.putExtra("params", "experience");
                            startActivity(workExpersion);
                            finish();
                        } else if("0".equals(codes)){
                            StyledDialog.dismissLoading();
                            Toast.makeText(EduBackGroundActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        StyledDialog.dismissLoading();
                        Toast.makeText(EduBackGroundActivity.this, "保存数据失败", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 1200:
                    StyledDialog.dismissLoading();
                    Toast.makeText(EduBackGroundActivity.this, "联网超时,请重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private TimePickerView pvTime;

    private void initTimePicker(final String flags) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 1, 1);

        pvTime = new TimePickerView.Builder(EduBackGroundActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String time = UiUtils.getTime(date.toString());
                if (flags.equals("1")) {
                    tv_eentry_time.setText(time);
                    starttime = time+"-01";
                } else if (flags.equals("2")) {
                    tv_gradution_time.setText(time);
                    finishtime = time+"-01";
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

