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
import com.yiwangrencai.ywkj.view.MyPopuwindown;
import com.yiwangrencai.ywkj.view.MySexPopuwin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */

public class EduItemActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_item_education;
    }

    String id;
    String str;
    int code;
    @Override
    public void initData() {
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token= share.getString("api_token", "");
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        formEncodingBuilder.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_EDUEXP_EDIT)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(formEncodingBuilder.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                System.out.println("777777777777" + str);
                handler.sendEmptyMessage(100);
            }
        });
    }

    String edu_type;
    String school;
    String starttime;
    String endtime;
    String speciality;
    String educationId;
    String education;
    String description;
    String education_name;
    String starttime_name;
    String endtime_name;
    //String type;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    if (code == 200) {
                        JSONObject jsonObject = JSON.parseObject(str);
                        String codee = jsonObject.getString("code");
                        String message = jsonObject.getString("msg");
                        if ("1".equals(codee)){
                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                            String id = jsonObjectData.getString("id");
                            edu_type = jsonObjectData.getString("edu_type");
                            school = jsonObjectData.getString("school");
                            starttime = jsonObjectData.getString("starttime");
                            endtime = jsonObjectData.getString("endtime");
                            speciality = jsonObjectData.getString("speciality");
                            educationId = jsonObjectData.getString("education");
                            type = jsonObjectData.getString("type");
                            description = jsonObjectData.getString("description");
                            education_name = jsonObjectData.getString("education_name");
                            starttime_name = jsonObjectData.getString("starttime_name");
                            endtime_name = jsonObjectData.getString("endtime_name");
                        }else if("0".equals(codee)){
                            Toast.makeText(EduItemActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        Toast.makeText(EduItemActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();

                    }

                    if (edu_type.equals("1")) {
                        framlayout.addView(leftView);

                        tv_input_school_namee.setText(school);
                        tv_eentry_time.setText(starttime_name);
                        tv_gradution_time.setText(endtime_name);
                        tv_input_major_name.setText(speciality);
                        tv_select_education.setText(education_name);
                        if (type.equals("0")) {
                            tv_toanzhao.setText("是");
                        } else if (type.equals("1")) {
                            tv_toanzhao.setText("否");
                        }

                        tv_education_describe.setText(description);

                    } else if (edu_type.equals("2")) {
                        framlayout.addView(rightView);

                        tv_input_school_right.setText(school);
                        tv_eentry_right.setText(starttime_name);
                        tv_gradution_right.setText(endtime_name);
                        tv_input_major_right.setText(speciality);
                        tv_describe_right.setText(description);
                    }

                    break;

                case 200:
                    if (code == 200) {
                        JSONObject json = JSON.parseObject(strss);
                        String message = json.getString("msg");
                        String code = json.getString("code");
                        if ("1".equals(code)){
                            Intent intent = new Intent(EduItemActivity.this, EditResumeTwoActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else if("0".equals(code)){
                            Toast.makeText(EduItemActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(EduItemActivity.this, "删除数据失败", Toast.LENGTH_SHORT).show();
                    }
                  //  mypopu.dismiss();
                    StyledDialog.dismissLoading();
                    break;
            }
        }
    };

    private FrameLayout iv_back_basic;
    private FrameLayout framlayout;
    private TextView texteducation;
    private TextView textsave;
    private View leftView;
    private View rightView;
    private String api_token;

    @Override
    public void initView() {

        iv_back_basic = (FrameLayout) findViewById(R.id.iv_back_basic);
        texteducation = (TextView) findViewById(R.id.texteducation);
        textsave = (TextView) findViewById(R.id.textsave);
        framlayout = (FrameLayout) findViewById(R.id.framlayout);

        leftView = createLeftView();
        rightView = createRightView();

        iv_back_basic.setOnClickListener(this);
        textsave.setOnClickListener(this);
        initTimePicker();
    }

    private RelativeLayout rl_school_right;
    private RelativeLayout rl_time_right;
    private RelativeLayout rl_gradution_right;
    private RelativeLayout rl_major_right;
    private RelativeLayout rl_discrub_right;
    private LinearLayout linearLayout_delete_right;
    private TextView tv_input_school_right;
    private TextView tv_eentry_right;
    private TextView tv_gradution_right;
    private TextView tv_input_major_right;
    private TextView tv_describe_right;

    private View createRightView() {
        View view = View.inflate(this, R.layout.fram_right_item, null);
        rl_school_right = (RelativeLayout) view.findViewById(R.id.rl_school_right);
        rl_time_right = (RelativeLayout) view.findViewById(R.id.rl_time_right);
        rl_gradution_right = (RelativeLayout) view.findViewById(R.id.rl_gradution_right);
        rl_major_right = (RelativeLayout) view.findViewById(R.id.rl_major_right);
        rl_discrub_right = (RelativeLayout) view.findViewById(R.id.rl_discrub_right);
        linearLayout_delete_right = (LinearLayout) view.findViewById(R.id.linearLayout_delete_right);

        rl_school_right.setOnClickListener(this);
        rl_discrub_right.setOnClickListener(this);
        rl_time_right.setOnClickListener(this);
        rl_gradution_right.setOnClickListener(this);
        rl_major_right.setOnClickListener(this);
        rl_discrub_right.setOnClickListener(this);
        linearLayout_delete_right.setOnClickListener(this);

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
    private LinearLayout linearLayout_delete_left;

    private View createLeftView() {
        View view = View.inflate(this, R.layout.fram_left_item, null);
        rl_school_name = (RelativeLayout) view.findViewById(R.id.rl_school_name);
        rl_time_enrollment = (RelativeLayout) view.findViewById(R.id.rl_time_enrollment);
        rl_gradution_time = (RelativeLayout) view.findViewById(R.id.rl_gradution_time);
        rl_major_name = (RelativeLayout) view.findViewById(R.id.rl_major_name);
        rl_education_background = (RelativeLayout) view.findViewById(R.id.rl_education_background);
        rl_examination = (RelativeLayout) view.findViewById(R.id.rl_examination);
        rl_discrub = (RelativeLayout) view.findViewById(R.id.rl_discrub);
        linearLayout_delete_left = (LinearLayout) view.findViewById(R.id.linearLayout_delete_left);

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
        linearLayout_delete_left.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_basic://返回上一个界面
                finish();
                break;
            case R.id.textsave://保存数据
                //向服务器提交修改的数据
                saveEditData();
                break;
            case R.id.rl_school_name://学校名称
                Intent intentName = new Intent(EduItemActivity.this, NameActivity.class);
                intentName.putExtra("params", "schoolname");
                intentName.putExtra("mobile", school);
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
                Intent intentMajor = new Intent(EduItemActivity.this, NameActivity.class);
                intentMajor.putExtra("params", "majorr");
                intentMajor.putExtra("mobile", speciality);
                startActivityForResult(intentMajor, 31);
                break;
            case R.id.rl_education_background://学历
                Intent intentEducation = new Intent(EduItemActivity.this, EducationActivity.class);
                intentEducation.putExtra("education", "education");
                intentEducation.putExtra("company_name", education_name);
                startActivityForResult(intentEducation, 15);
                break;
            case R.id.rl_examination://是否统招
                //显示弹出框
                showTongzhaoPOpu();
                break;
            case R.id.rl_discrub://描述
                Intent intentDiscuss = new Intent(EduItemActivity.this, DescribeActivity.class);
                intentDiscuss.putExtra("params", "describe");
                intentDiscuss.putExtra("introduction", description);
                startActivityForResult(intentDiscuss, 60);
                break;
            case R.id.rl_school_right://培训机构
                Intent intentSchool = new Intent(EduItemActivity.this, NameActivity.class);
                intentSchool.putExtra("params", "train");
                intentSchool.putExtra("mobile",school);
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
                Intent intentProgress = new Intent(EduItemActivity.this, NameActivity.class);
                intentProgress.putExtra("params", "project");
                intentProgress.putExtra("mobile",speciality);
                startActivityForResult(intentProgress, 301);
                break;
            case R.id.rl_discrub_right://培训机构的描述
                Intent intentTrain = new Intent(EduItemActivity.this, DescribeActivity.class);
                intentTrain.putExtra("params", "describes");
                intentTrain.putExtra("introduction",description);
                startActivityForResult(intentTrain, 61);
                break;
            case R.id.linearLayout_delete_left://删除学历背景
                deleteEducationExp();
                break;
            case R.id.linearLayout_delete_right://删除培训技能
                deleteTrainSkill();//删除培训技能
                break;
        }
    }

    /**
     * 删除培训技能
     */
    private void deleteTrainSkill() {
        StyledDialog.buildLoading().show();
        OkHttpClient okhttpClient = new OkHttpClient();
        FormEncodingBuilder foemEncoding = new FormEncodingBuilder();
        foemEncoding.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_EDUEXP_DELETE)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(foemEncoding.build())
                .build();
        okhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strss = response.body().string();
                code = response.code();
                Log.i("nnnnnnnnnnnnnn", strss);
                handler.sendEmptyMessage(200);
            }
        });
    }

    /**
     * 修改后保存数据
     */
    private void saveEditData() {

        StyledDialog.buildLoading().show();
        if (edu_type.equals("1")) {
            saveData();//保存学历教育数据
        } else if (edu_type.equals("2")) {
            saveTrainData();//保存培训教育的数据
        }
    }

    /**
     * 保存培训教育的数据
     */
    private void saveTrainData() {
        OkHttpClient okhrttpEdu = new OkHttpClient();
        FormEncodingBuilder formcing = new FormEncodingBuilder();
        formcing.add("id", id);
        formcing.add("edu_type", edu_type);
        formcing.add("school", school);
        formcing.add("speciality", speciality);
        formcing.add("starttime", starttime + "-01");
        formcing.add("endtime", endtime + "-01");
        formcing.add("description", description);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_EDUEXP_UPDATE)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(formcing.build())
                .build();
        okhrttpEdu.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strss = response.body().string();
                code = response.code();
                System.out.println("jjjjjjjjjjjj"+strss);
                handler.sendEmptyMessage(200);
            }
        });
    }

    String strsss;

    /**
     * 保存数据学历教育
     */
    private void saveData() {
        OkHttpClient okhrttpEdu = new OkHttpClient();
        FormEncodingBuilder formcing = new FormEncodingBuilder();
        formcing.add("id", id);
        formcing.add("edu_type", edu_type);
        formcing.add("school", school);
        formcing.add("speciality", speciality);
        formcing.add("education", educationId);
        formcing.add("type", type);
        formcing.add("starttime", starttime );
        formcing.add("endtime", endtime );
        formcing.add("description", description);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_EDUEXP_UPDATE)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(formcing.build())
                .build();
        okhrttpEdu.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strss = response.body().string();
                code = response.code();
                System.out.println("jjjjjjjjjjjj"+strss);
                handler.sendEmptyMessage(200);
            }
        });
    }

    String strss;
    MyPopuwindown mypopu;

    /**
     * 删除教育经历
     */
    private void deleteEducationExp() {

        StyledDialog.buildLoading().show();
        OkHttpClient okhttpClient = new OkHttpClient();
        FormEncodingBuilder foemEncoding = new FormEncodingBuilder();
        foemEncoding.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_EDUEXP_DELETE)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(foemEncoding.build())
                .build();
        okhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strss = response.body().string();
                code = response.code();
                Log.i("/////////", strss);
                handler.sendEmptyMessage(200);
            }
        });
    }

    /**
     * 开启界面返回的数据
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
                    school = data.getExtras().getString("schoolname");
                    tv_input_school_namee.setText(school);
                    break;
                case 31:
                    speciality = data.getExtras().getString("major");
                    tv_input_major_name.setText(speciality);
                    break;
                case 15:
                    education_name = data.getExtras().getString("education");
                    educationId = data.getExtras().getString("educationId");
                    tv_select_education.setText(education_name);
                    break;
                case 60:
                    description = data.getExtras().getString("describe");
                    tv_education_describe.setText(description);
                    break;
                case 300:
                    school=data.getExtras().getString("train");
                    tv_input_school_right.setText(school);
                    break;
                case 301:
                   speciality= data.getExtras().getString("project");
                    tv_input_major_right.setText(speciality);
                    break;
                case 61:
                    description=data.getExtras().getString("describes");
                    tv_describe_right.setText(description);
                    break;
            }
        }
    }


    /**
     * 显示弹出框
     */

    OptionsPickerView pvOptions;
    private void showTongzhaoPOpu() {
        final List<String> options1Items = new ArrayList<>();
        options1Items.add("是");
        options1Items.add("否");

        //options1Items.add("其他");
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
                .setSelectOptions(Integer.parseInt(type), 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .build();

        pvOptions.setPicker(options1Items);//添加数据源
        pvOptions.show();
    }

    // private TimePickerView pvTime;
    String flag;
    String type;
    TimePickerView pvTime;
    String eduStartTime;
    String eduEndTime;
    String trainStartTime;
    String trainendTime;
    boolean flags = true;

    /**
     * 选择时间
     */
    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 1, 1);
        pvTime = new TimePickerView.Builder(EduItemActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (flag.equals("start")) {
                    starttime = getTime(date.toString());
                    tv_eentry_time.setText(starttime);
                    starttime=starttime+"-01";
                } else if (flag.equals("end")) {
                    endtime = getTime(date.toString());
                    tv_gradution_time.setText(endtime);
                    endtime=endtime+"-01";
                } else if (flag.equals("trainstart")) {
                    starttime = getTime(date.toString());
                    tv_eentry_right.setText(starttime);
                    starttime=starttime+"-01";
                } else if (flag.equals("trainend")) {
                    endtime = getTime(date.toString());
                    tv_gradution_right.setText(endtime);
                    endtime=endtime+"-01";
                }

            }
        }).setType(TimePickerView.Type.YEAR_MONTH)
                .setTitleText("选择时间")
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。

    }

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