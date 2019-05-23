package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 */

public class EducationTrainActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.train_education_activity;
    }

    @Override
    public void initData() {
        initTimePicker();
    }

    private FrameLayout framlayout_right;
    private FrameLayout framlayout_left;
    private TextView texteducation;
    private TextView textskilltrain;

    @Override
    public void initView() {
        FrameLayout iv_back_basic = (FrameLayout) findViewById(R.id.iv_back_basic);
        framlayout_left = (FrameLayout) findViewById(R.id.framlayout_left);
        framlayout_right = (FrameLayout) findViewById(R.id.framlayout_right);
        texteducation = (TextView) findViewById(R.id.texteducation);
        textskilltrain = (TextView) findViewById(R.id.textskilltrain);
        TextView textsave = (TextView) findViewById(R.id.textsave);

        //创建学历背景界面
        View viewLeft = createLeftView();
        framlayout_left.addView(viewLeft);

        //创建技能培训界面
        View viewRight = createViewRight();
        framlayout_right.addView(viewRight);

        iv_back_basic.setOnClickListener(this);
        texteducation.setOnClickListener(this);
        textskilltrain.setOnClickListener(this);
        textsave.setOnClickListener(this);

    }

    private RelativeLayout rl_school_right;
    private RelativeLayout rl_time_right;
    private RelativeLayout rl_gradution_right;
    private RelativeLayout rl_major_right;
    private RelativeLayout rl_discrub_right;
    private TextView tv_input_school_right;
    private TextView tv_eentry_right;
    private TextView tv_gradution_right;
    private TextView tv_input_major_right;
    private TextView tv_describe_right;

    /**
     * 创建技能培训界面
     *
     * @return
     */
    private View createViewRight() {
        View view = View.inflate(this, R.layout.fram_right, null);
        rl_school_right = (RelativeLayout) view.findViewById(R.id.rl_school_right);
        rl_time_right = (RelativeLayout) view.findViewById(R.id.rl_time_right);
        rl_gradution_right = (RelativeLayout) view.findViewById(R.id.rl_gradution_right);
        rl_major_right = (RelativeLayout) view.findViewById(R.id.rl_major_right);
        rl_discrub_right = (RelativeLayout) view.findViewById(R.id.rl_discrub_right);

        rl_school_right.setOnClickListener(this);
        rl_discrub_right.setOnClickListener(this);
        rl_time_right.setOnClickListener(this);
        rl_gradution_right.setOnClickListener(this);
        rl_major_right.setOnClickListener(this);
        rl_discrub_right.setOnClickListener(this);

        tv_input_school_right = (TextView) view.findViewById(R.id.tv_input_school_right);
        tv_eentry_right = (TextView) view.findViewById(R.id.tv_eentry_right);
        tv_gradution_right = (TextView) view.findViewById(R.id.tv_gradution_right);
        tv_input_major_right = (TextView) view.findViewById(R.id.tv_input_major_right);
        tv_describe_right = (TextView) view.findViewById(R.id.tv_describe_right);

        return view;
    }

    private RelativeLayout rl_school_name;
    private RelativeLayout rl_time_enrollment;
    private RelativeLayout rl_gradution_time;
    private RelativeLayout rl_major_name;
    private RelativeLayout rl_education_background;
    private RelativeLayout rl_examination;
    private RelativeLayout rl_discrub;
    private TextView tv_input_school_namee;
    private TextView tv_eentry_time;
    private TextView tv_gradution_time;
    private TextView tv_input_major_name;
    private TextView tv_select_education;
    private TextView tv_toanzhao;
    private TextView tv_education_describe;

    /**
     * 创建学历背景界面
     *
     * @return
     */
    private View createLeftView() {
        View view = View.inflate(this, R.layout.fram_left, null);
        rl_school_name = (RelativeLayout) view.findViewById(R.id.rl_school_name);
        rl_time_enrollment = (RelativeLayout) view.findViewById(R.id.rl_time_enrollment);
        rl_gradution_time = (RelativeLayout) view.findViewById(R.id.rl_gradution_time);
        rl_major_name = (RelativeLayout) view.findViewById(R.id.rl_major_name);
        rl_education_background = (RelativeLayout) view.findViewById(R.id.rl_education_background);
        rl_examination = (RelativeLayout) view.findViewById(R.id.rl_examination);
        rl_discrub = (RelativeLayout) view.findViewById(R.id.rl_discrub);

        tv_input_school_namee = (TextView) view.findViewById(R.id.tv_input_school_namee);
        tv_eentry_time = (TextView) view.findViewById(R.id.tv_eentry_time);
        tv_gradution_time = (TextView) view.findViewById(R.id.tv_gradution_time);
        tv_input_major_name = (TextView) view.findViewById(R.id.tv_input_major_name);
        tv_select_education = (TextView) view.findViewById(R.id.tv_select_education);
        tv_toanzhao = (TextView) view.findViewById(R.id.tv_toanzhao);
        tv_education_describe = (TextView) view.findViewById(R.id.tv_education_describe);

        rl_school_name.setOnClickListener(this);
        rl_time_enrollment.setOnClickListener(this);
        rl_gradution_time.setOnClickListener(this);
        rl_major_name.setOnClickListener(this);
        rl_education_background.setOnClickListener(this);
        rl_examination.setOnClickListener(this);
        rl_discrub.setOnClickListener(this);

        return view;
    }

    String flag;
    String type = "0";

    boolean flags = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_basic://返回上一个界面
                finish();
                break;
            case R.id.texteducation://学历背景
                flags = true;
                framlayout_left.setVisibility(View.VISIBLE);
                framlayout_right.setVisibility(View.GONE);
                textskilltrain.setTextColor(0xffffffff);
                texteducation.setTextColor(getResources().getColor(R.color.text_color_green));
                texteducation.setBackgroundResource(R.drawable.shape_white_left);
                textskilltrain.setBackgroundResource(R.drawable.shape_green_right);
                break;
            case R.id.textskilltrain://技能培训
                flags = false;
                framlayout_left.setVisibility(View.GONE);
                framlayout_right.setVisibility(View.VISIBLE);
                texteducation.setTextColor(0xffffffff);
                texteducation.setBackgroundResource(R.drawable.shape_green_left);
                textskilltrain.setTextColor(getResources().getColor(R.color.text_color_green));
                textskilltrain.setBackgroundResource(R.drawable.shape_white_right);
                break;
            case R.id.textsave://保存数据
                //向服务器提交修改的数据
                saveEditData();
                break;
            case R.id.rl_school_name://学校名称
                Intent intentName = new Intent(EducationTrainActivity.this, NameActivity.class);
                intentName.putExtra("params", "schoolname");
                startActivityForResult(intentName, 30);
                break;
            case R.id.rl_time_enrollment://入学时间
                pvTime.show();
                flag = "start";
                break;
            case R.id.rl_gradution_time://毕业时间
                pvTime.show();
                flag = "end";
                break;
            case R.id.rl_major_name://专业
                Intent intentMajor = new Intent(EducationTrainActivity.this, NameActivity.class);
                intentMajor.putExtra("params", "majorr");
                startActivityForResult(intentMajor, 31);
                break;
            case R.id.rl_education_background://学历
                Intent intentEducation = new Intent(EducationTrainActivity.this, EducationActivity.class);
                intentEducation.putExtra("education", "education");
                intentEducation.putExtra("company_name", educationData);
                startActivityForResult(intentEducation, 15);
                break;
            case R.id.rl_examination://是否统招
                //显示弹出框
                showTongzhaoPOpu();
                break;
            case R.id.rl_discrub://描述
                Intent intentDiscuss = new Intent(EducationTrainActivity.this, DescribeActivity.class);
                intentDiscuss.putExtra("params", "describe");
                startActivityForResult(intentDiscuss, 60);
                break;
            case R.id.rl_school_right://培训机构
                Intent intentSchool = new Intent(EducationTrainActivity.this, NameActivity.class);
                intentSchool.putExtra("params", "train");
                intentSchool.putExtra("mobile", train);
                startActivityForResult(intentSchool, 300);
                break;
            case R.id.rl_time_right://培训开始的时间
                pvTime.show();
                flag = "trainstart";
                break;
            case R.id.rl_gradution_right://培训结束的时间
                pvTime.show();
                flag = "trainend";
                break;
            case R.id.rl_major_right://培训的项目
                Intent intentProgress = new Intent(EducationTrainActivity.this, NameActivity.class);
                intentProgress.putExtra("params", "project");
                intentProgress.putExtra("mobile", project);
                startActivityForResult(intentProgress, 301);
                break;
            case R.id.rl_discrub_right://培训机构的描述
                Intent intentTrain = new Intent(EducationTrainActivity.this, DescribeActivity.class);
                intentTrain.putExtra("params", "describes");
                intentTrain.putExtra("introduction",content);
                startActivityForResult(intentTrain, 61);
                break;
        }
    }

    String str;
    MyPopuwindown mypopuwin;

    /**
     * 向服务器提交修改的数据
     */
    private void saveEditData() {
        if (flags) {
            if (schoolname == null || "".equals(schoolname)) {
                Toast.makeText(EducationTrainActivity.this, "请输入学校名称", Toast.LENGTH_SHORT).show();
                return;
            }
            if (eduStartTime == null || "".equals(eduStartTime)) {
                Toast.makeText(EducationTrainActivity.this, "请选择入学时间", Toast.LENGTH_SHORT).show();
                return;
            }
            if (eduEndTime == null || "".equals(eduEndTime)) {
                Toast.makeText(EducationTrainActivity.this, "请选择毕业时间", Toast.LENGTH_SHORT).show();
                return;
            }
            if (major == null || "".equals(major)) {
                Toast.makeText(EducationTrainActivity.this, "请输入专业名称", Toast.LENGTH_SHORT).show();
                return;
            }
            if (educationDataId == null || "".equals(educationDataId)) {
                Toast.makeText(EducationTrainActivity.this, "请选择学历", Toast.LENGTH_SHORT).show();
                return;
            }
            if (type == null || "".equals(type)) {
                Toast.makeText(EducationTrainActivity.this, "请选择是否统招", Toast.LENGTH_SHORT).show();
                return;
            }

        } else {
            if (train == null || "".equals(train)) {
                Toast.makeText(EducationTrainActivity.this, "请输入培训机构的名称", Toast.LENGTH_SHORT).show();
                return;
            }

            if (trainStartTime == null || "".equals(trainStartTime)) {
                Toast.makeText(EducationTrainActivity.this, "请选择培训开始的时间", Toast.LENGTH_SHORT).show();
                return;
            }
            if (trainendTime == null || "".equals(trainendTime)) {
                Toast.makeText(EducationTrainActivity.this, "请选择培训结束的时间", Toast.LENGTH_SHORT).show();
                return;
            }
            if (project == null || "".equals(project)) {
                Toast.makeText(EducationTrainActivity.this, "请输入培训的项目", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        StyledDialog.buildLoading().show();
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        String api_token = share.getString("api_token", "");
        SharedPreferences shares = getSharedPreferences("data", MODE_PRIVATE);
        String id = shares.getString("resume_id", "");
        OkHttpClient okhttpClient = new OkHttpClient();
        FormEncodingBuilder fermEncoding = new FormEncodingBuilder();
        fermEncoding.add("resume_id", id);
        if (flags) {
            fermEncoding.add("edu_type", "1");
            fermEncoding.add("school", schoolname);
            fermEncoding.add("speciality", major);
            fermEncoding.add("education", educationDataId);
            fermEncoding.add("type", type);
            fermEncoding.add("starttime", eduStartTime);
            fermEncoding.add("endtime", eduEndTime);
            if (describe == null || "".equals(describe)) {
                fermEncoding.add("description", "");
            } else {
                fermEncoding.add("description", describe);
            }


        } else {
            fermEncoding.add("edu_type", "2");
            fermEncoding.add("school", train);
            fermEncoding.add("speciality", project);
            fermEncoding.add("starttime", trainStartTime);
            fermEncoding.add("endtime", trainendTime);
            if (content == null || "".equals(content)) {
                fermEncoding.add("description", "");
            } else {
                fermEncoding.add("description", content);
            }

        }

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_EDUEXP)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(fermEncoding.build()).build();

        okhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                handler.sendEmptyMessage(1000);
                System.out.println("1212121212112" + str);
            }
        });

    }

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
                            Toast.makeText(EducationTrainActivity.this, message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EducationTrainActivity.this, EditResumeTwoActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else if("0".equals(code)){
                            Toast.makeText(EducationTrainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                        StyledDialog.dismissLoading();
                    } else {
                        Toast.makeText(EducationTrainActivity.this, "添加教育背景失败", Toast.LENGTH_SHORT).show();
                        StyledDialog.dismissLoading();
                    }
                    break;
            }
        }
    };


    /**
     * 显示弹出框
     */
    OptionsPickerView pvOptions;

    private void showTongzhaoPOpu() {
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
                .isDialog(false)//是否显示为对话框样式
                .build();

        pvOptions.setPicker(options1Items);//添加数据源
        pvOptions.show();
    }

    TimePickerView pvTime;
    String eduStartTime;
    String eduEndTime = "0000-00-00";
    String trainStartTime;
    String trainendTime = "0000-00-00";

    /**
     * 选择时间
     */
    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 1, 1);
        pvTime = new TimePickerView.Builder(EducationTrainActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (flag.equals("start")) {
                    eduStartTime = getTime(date.toString());
                    tv_eentry_time.setText(eduStartTime);
                    eduStartTime = eduStartTime + "-01";
                } else if (flag.equals("end")) {
                    eduEndTime = getTime(date.toString());
                    tv_gradution_time.setText(eduEndTime);
                    eduEndTime = eduEndTime + "-01";
                } else if (flag.equals("trainstart")) {
                    trainStartTime = getTime(date.toString());
                    tv_eentry_right.setText(trainStartTime);
                    trainStartTime = trainStartTime + "-01";
                } else if (flag.equals("trainend")) {
                    trainendTime = getTime(date.toString());
                    tv_gradution_right.setText(trainendTime);
                    trainendTime = trainendTime + "-01";
                }

            }
        }).setType(TimePickerView.Type.YEAR_MONTH)
                .setTitleText("选择时间")
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。

    }

    String schoolname="";
    String major="";
    String describe="";
    String educationData="";
    String educationDataId="";
    String content="";
    String project="";
    String train="";

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            switch (resultCode) {
                case 30://学校名称
                    schoolname = data.getExtras().getString("schoolname");
                    Log.i("99999999..", schoolname);
                    if (schoolname == null || "".equals(schoolname)) {

                    } else {
                        tv_input_school_namee.setText(schoolname);
                    }

                    break;
                case 31://专业名称
                    major = data.getExtras().getString("major");
                    if (major == null || "".equals(major)) {

                    } else {
                        tv_input_major_name.setText(major);
                    }

                    break;
                case 60://描述
                    describe = data.getExtras().getString("describe");
                    if (describe == null || "".equals(describe)) {

                    } else {
                        tv_education_describe.setText(describe);
                    }

                    break;
                case 15:
                    educationData = data.getExtras().getString("education");
                    educationDataId = data.getExtras().getString("educationId");
                    if (educationData == null || "".equals(educationData)) {

                    } else {
                        tv_select_education.setText(educationData);
                    }

                    break;
                case 61:
                    content = data.getExtras().getString("describes");
                    if (content == null || "".equals(content)) {

                    } else {
                        tv_describe_right.setText(content);
                    }

                    break;
                case 300:
                    train = data.getExtras().getString("train");
                    if (train == null || "".equals(train)) {

                    } else {
                        tv_input_school_right.setText(train);
                    }

                    break;
                case 301:
                    project = data.getExtras().getString("project");
                    if (project == null || "".equals(project)) {

                    } else {
                        tv_input_major_right.setText(project);
                    }

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
