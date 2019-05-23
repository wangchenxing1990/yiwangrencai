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
import com.yiwangrencai.ywkj.fragment.HomeFragent;
import com.yiwangrencai.ywkj.tools.UiUtils;
import com.yiwangrencai.ywkj.view.MyPopuwindown;
import com.yiwangrencai.ywkj.view.MySexPopuwin;
import com.yiwangrencai.ywkj.view.MyStatePopu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;

/**
 * Created by Administrator on 2017/4/28.
 */

public class BaseInfoActiovity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.base_edit_resume_activity;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100://解析数据
                    parsEditBaseInfo();//解析基本资料的数据
                    break;
                case 120:
                    parsSaveEditData();//解析保存编辑数据
                    break;
            }
        }
    };

    /**
     * 解析保存编辑数据的成功
     */
    private void parsSaveEditData() {
        if (code ==200){
            JSONObject json=JSON.parseObject(strs);
            String codes=json.getString("code");
             if ("1".equals(codes)){
                 Toast.makeText(BaseInfoActiovity.this,"数据保存成功",Toast.LENGTH_SHORT).show();
                 SharedPreferences shared=getSharedPreferences("data",MODE_PRIVATE);
                 SharedPreferences.Editor editor=shared.edit();
                 editor.putString("job_status",job_status);
                 editor.commit();
                 Intent intent=new Intent(BaseInfoActiovity.this,EditResumeTwoActivity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(intent);
             }else{
                 Toast.makeText(BaseInfoActiovity.this,"数据保存失败",Toast.LENGTH_SHORT).show();
             }
        }else {
            Toast.makeText(BaseInfoActiovity.this,"数据保存失败",Toast.LENGTH_SHORT).show();
        }
        StyledDialog.dismissLoading();
    }

    /**
     * 解析基本资料数据
     */
    private void parsEditBaseInfo() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            if ("1".equals(codes)) {
                JSONObject jsonObject = json.getJSONObject("data");
                name = jsonObject.getString("name");
                sex = jsonObject.getString("sex");
                birthday = jsonObject.getString("birthday");
                education = jsonObject.getString("education");
                work_year = jsonObject.getString("work_year");
                homeaddress = jsonObject.getString("homeaddress");
                job_status = jsonObject.getString("job_status");
                census = jsonObject.getString("census");
                marital = jsonObject.getString("marital");
                height = jsonObject.getString("height");
                address = jsonObject.getString("address");
                birthday_name = jsonObject.getString("birthday_name");
                work_year_name = jsonObject.getString("work_year_name");
                education_name = jsonObject.getString("education_name");
                homeaddress_name = jsonObject.getString("homeaddress_name");
                census_name = jsonObject.getString("census_name");

                tv_select_birthday.setText(birthday_name);
                tv_input_name.setText(name);
                tv_input_address.setText(address);
                tv_input_education.setText(education_name);
                tv_input_work_suffer.setText(work_year_name);
                tv_select_location.setText(homeaddress_name);

                tv_input_high.setText(height);
                tv_input_address.setText(address);

                if (census_name.equals("")){
                    tv_input_cesus_name.setText("请选择户籍所在地");
                }else{
                    tv_input_cesus_name.setText(census_name);
                }

                showWorkStatuePopuwin();//求职状态
                showSexPopuwind();//性别
                //初始状态
                showSexView();
                //显示婚姻
                showMartail();//显示婚姻

                if (job_status.equals("1")){
                    tv_work_state_basic.setText("不在职，正在找工作");
                }else if(job_status.equals("2")){
                    tv_work_state_basic.setText("在职，打算近期换工作");
                }else if(job_status.equals("3")){
                    tv_work_state_basic.setText("在职，有更好的机会才考虑");
                }else if(job_status.equals("4")){
                    tv_work_state_basic.setText("不考虑换工作");
                }

            } else {
                Toast.makeText(BaseInfoActiovity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(BaseInfoActiovity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 显示婚姻
     */
    private void showMartail() {
        if ("0".equals(marital)){
            tv_marital.setText("未婚");
        }else if("1".equals(marital)){
            tv_marital.setText("已婚");
        }else if("2".equals(marital)){
            tv_marital.setText("离婚");
        }else if("3".equals(marital)){
            tv_marital.setText("保密");
        }
    }

    String api_token;
    int code;
    String str;

    @Override
    public void initData() {
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        OkHttpClient okHttp = new OkHttpClient();
        FormEncodingBuilder formEncod = new FormEncodingBuilder();
        formEncod.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_DATA_EDIT)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncod.build())
                .build();
        okHttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (e.getMessage() != null) {
                    handler.sendEmptyMessage(110);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                code = response.code();
                str = response.body().string();
                Log.i("获取编辑基本信息的数据", str);
                handler.sendEmptyMessage(100);
            }
        });

    }

    private FrameLayout iv_back_basic;
    private TextView tv_next_basic;
    private RelativeLayout rl_name;
    private RelativeLayout rl_sex;
    private RelativeLayout rl_birthday;
    private RelativeLayout rl_education;
    private RelativeLayout rl_work_suffer;
    private RelativeLayout rl_location;
    private RelativeLayout rl_work_state;
    private RelativeLayout rl_address;
    private RelativeLayout rl_cesus_name;
    private RelativeLayout rl_high;
    private RelativeLayout rl_marital;
    private String name;
    String height;
    TextView tv_input_name;
    TextView tv_input_high;
    TextView tv_sex_input;
    TextView tv_input_education;
    TextView tv_input_work_suffer;
    TextView tv_work_state_basic;
    TextView tv_input_address;
    TextView tv_input_cesus_name;
    TextView tv_select_birthday;
    TextView tv_marital;
    TextView tv_select_location;
    String id;
    String sex;
    String education_name;
    String education;
    String work_year_name;
    String work_year;
    String job_status;
    String address;
    String homeaddress_name;
    String homeaddress;
    String census_name;
    String census;
    String birthday;
    String birthday_name;
    String marital;


    @Override
    public void initView() {

        iv_back_basic = (FrameLayout) findViewById(R.id.iv_back_basic);
        tv_next_basic = (TextView) findViewById(R.id.tv_next_basic);
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        rl_birthday = (RelativeLayout) findViewById(R.id.rl_birthday);
        rl_education = (RelativeLayout) findViewById(R.id.rl_education);
        rl_work_suffer = (RelativeLayout) findViewById(R.id.rl_work_suffer);
        rl_location = (RelativeLayout) findViewById(R.id.rl_location);
        rl_work_state = (RelativeLayout) findViewById(R.id.rl_work_state);
        rl_address = (RelativeLayout) findViewById(R.id.rl_address);
        rl_cesus_name = (RelativeLayout) findViewById(R.id.rl_cesus_name);
        rl_marital = (RelativeLayout) findViewById(R.id.rl_marital);
        rl_high = (RelativeLayout) findViewById(R.id.rl_high);

        tv_input_name = (TextView) findViewById(R.id.tv_input_name);
        tv_sex_input = (TextView) findViewById(R.id.tv_sex_input);
        tv_select_birthday = (TextView) findViewById(R.id.tv_select_birthday);
        tv_input_education = (TextView) findViewById(R.id.tv_input_education);
        tv_input_work_suffer = (TextView) findViewById(R.id.tv_input_work_suffer);
        tv_select_location = (TextView) findViewById(R.id.tv_select_location);
        tv_work_state_basic = (TextView) findViewById(R.id.tv_work_state_basic);
        tv_input_address = (TextView) findViewById(R.id.tv_input_address);
        tv_input_cesus_name = (TextView) findViewById(R.id.tv_input_cesus_name);
        tv_marital = (TextView) findViewById(R.id.tv_marital);
        tv_input_high = (TextView) findViewById(R.id.tv_input_high);


        iv_back_basic.setOnClickListener(this);
        tv_next_basic.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_birthday.setOnClickListener(this);
        rl_education.setOnClickListener(this);
        rl_work_suffer.setOnClickListener(this);
        rl_location.setOnClickListener(this);
        rl_work_state.setOnClickListener(this);
        rl_address.setOnClickListener(this);
        rl_cesus_name.setOnClickListener(this);
        rl_marital.setOnClickListener(this);
        rl_high.setOnClickListener(this);

        initTimePicker();//生日
    }

    Boolean flag;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_basic://返回上一页
                finish();
                break;
            case R.id.tv_next_basic://保存数据
                saveDatatoService();//保存数据到服务器
                break;
            case R.id.rl_name://姓名
                Intent intent = new Intent(this, NameActivity.class);
                intent.putExtra("params", "base_name");
                intent.putExtra("mobile", name);
                startActivityForResult(intent, 100);
                break;
            case R.id.rl_sex://性别
                pvOptions.show();
                break;
            case R.id.rl_birthday://生日
                pvTime.show();
                break;
            case R.id.rl_education://学历

                Intent intentEdu = new Intent(this, EducationActivity.class);
                intentEdu.putExtra("education", "base_info");
                intentEdu.putExtra("save", "save");
                intentEdu.putExtra("educationId", education);
                intentEdu.putExtra("company_name", education_name);
                startActivityForResult(intentEdu, 102);

                break;
            case R.id.rl_work_suffer://工作经验
                Intent intentSuffer = new Intent(this, EducationActivity.class);
                intentSuffer.putExtra("education", "base_info_suffer");
                intentSuffer.putExtra("save", "save");
                intentSuffer.putExtra("company_name", work_year_name);
                startActivityForResult(intentSuffer, 103);
                break;
            case R.id.rl_location://现居住地
                Intent intentLocation = new Intent(this, LocationActivity.class);
                intentLocation.putExtra("params", "area");
                intentLocation.putExtra("paramss", "areasss");
                intentLocation.putExtra("paramss_name", "cesus");
                intentLocation.putExtra("style", "current_address");
                startActivityForResult(intentLocation, 9);
                flag = true;
                break;
            case R.id.rl_work_state://求职状态
                //显示求职状态的选择状态框
                jobStatue.show();
                break;
            case R.id.rl_address://详细地址
                Intent intentDetailAddress = new Intent(this, DescribeActivity.class);
                intentDetailAddress.putExtra("params", "base_info_address");
                intentDetailAddress.putExtra("introduction", address);
                startActivityForResult(intentDetailAddress, 104);
                break;
            case R.id.rl_cesus_name://户籍

                flag = false;
                Intent intentCensus = new Intent(this, LocationActivity.class);
                intentCensus.putExtra("params", "area");
                intentCensus.putExtra("paramss", "cesus");
                intentCensus.putExtra("paramss_name", "cesus");
                startActivityForResult(intentCensus, 9);

                break;
            case R.id.rl_marital://婚姻状况
                maritalStatue();//婚姻状况
                break;
            case R.id.rl_high://身高
                Intent intentHigh = new Intent(this, NameActivity.class);
                intentHigh.putExtra("params", "height");
                intentHigh.putExtra("mobile", height);
                startActivityForResult(intentHigh, 101);
                break;
        }
    }

    /**
     * 婚姻状况
     */
    OptionsPickerView maritalStatue;

    private void maritalStatue() {
        final List<String> listStatue = new ArrayList<>();
        listStatue.add("未婚");
        listStatue.add("已婚");
        listStatue.add("离婚");
        listStatue.add("保密");
        int maritals=Integer.parseInt(marital);
        maritalStatue = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = listStatue.get(options1);
                tv_marital.setText(tx);

                if (options1 == 0) {
                    marital = "0";
                } else if (options1 == 1) {
                    marital = "1";
                } else if (options1 == 2) {
                    marital = "2";
                } else if (options1 == 3) {
                    marital = "3";
                }

            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("婚姻状况")//标题
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
                .setSelectOptions(maritals, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                // .isDialog(true)//是否显示为对话框样式
                .build();

        maritalStatue.setPicker(listStatue);//添加数据源
        maritalStatue.show();
    }

    /**
     * 保存数据到服务器
     */
    private MyPopuwindown myPopuwindown;
    String strs;
    private void saveDatatoService() {
        if (name==null||"".equals(name)){
            Toast.makeText(BaseInfoActiovity.this,"姓名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        if (sex==null||"".equals(sex)){
            Toast.makeText(BaseInfoActiovity.this,"请选择性别",Toast.LENGTH_SHORT).show();
            return;
        }

        if (birthday==null||"".equals(birthday)){
            Toast.makeText(BaseInfoActiovity.this,"请选择生日",Toast.LENGTH_SHORT).show();
            return;
        }

        if (education==null||"".equals(education)){
            Toast.makeText(BaseInfoActiovity.this,"请选择学历",Toast.LENGTH_SHORT).show();
            return;
        }

        if (work_year==null||"".equals(work_year)){
            Toast.makeText(BaseInfoActiovity.this,"请选择工作经验",Toast.LENGTH_SHORT).show();
            return;
        }

        if (homeaddress==null||"".equals(homeaddress)){
            Toast.makeText(BaseInfoActiovity.this,"请选择现居住地",Toast.LENGTH_SHORT).show();
            return;
        }
        StyledDialog.buildLoading().show();
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);
        formEncoding.add("name", name);
        formEncoding.add("sex", sex);
        formEncoding.add("birthday", birthday);
        formEncoding.add("education", education);
        formEncoding.add("work_year", work_year);
        formEncoding.add("homeaddress", homeaddress);
        formEncoding.add("job_status", job_status);
        formEncoding.add("census", census);
        formEncoding.add("marital", marital);
        formEncoding.add("height", height);
        formEncoding.add("address", address);

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_DATA_UPDATE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncoding.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strs= response.body().string();
                 code = response.code();
                Log.i("更新基本资料", strs);
                handler.sendEmptyMessage(120);
            }
        });
    }

    /**
     * 显示选择的工作状态
     */
    private void showSelectWorkState() {
        if ("1".equals(job_status)) {
            tv_work_state_basic.setText("不在职，正在找工作");
        } else if ("2".equals(job_status)) {
            tv_work_state_basic.setText("在职，打算近期换工作");
        } else if ("3".equals(job_status)) {
            tv_work_state_basic.setText("在职，有更好的机会才考虑");
        } else if ("4".equals(job_status)) {
            tv_work_state_basic.setText("不考虑换工作");
        }
    }


    MyStatePopu myStatePopu;

    /**
     * 显示求职状态的选择状态框
     */
    OptionsPickerView jobStatue;

    private void showWorkStatuePopuwin() {
        final List<String> listStatue = new ArrayList<>();
        listStatue.add("不在职,正在找工作");
        listStatue.add("在职,打算近期换工作");
        listStatue.add("在职,有更好的机会才考虑");
        listStatue.add("不考虑换工作");
        jobStatue = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = listStatue.get(options1);
                tv_work_state_basic.setText(tx);

                if (options1 == 0) {
                    job_status = "1";
                } else if (options1 == 1) {
                    job_status = "2";
                } else if (options1 == 2) {
                    job_status = "3";
                } else if (options1 == 3) {
                    job_status = "4";
                }

            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("求职状态")//标题
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
                .setSelectOptions(Integer.parseInt(job_status)-1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                // .isDialog(true)//是否显示为对话框样式
                .build();

        jobStatue.setPicker(listStatue);//添加数据源

    }

    /**
     * 显示性别的弹窗
     */
    OptionsPickerView pvOptions;

    private void showSexPopuwind() {

        final List<String> options1Items = new ArrayList<>();
        options1Items.add("保密");
        options1Items.add("男");
        options1Items.add("女");
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);
                tv_sex_input.setText(tx);
                if (options1 == 0) {
                    sex = "0";
                } else if (options1 == 1) {
                    sex = "1";
                } else if (options1 == 2) {
                    sex = "2";
                }

            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("性别")//标题
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
                .setSelectOptions(Integer.parseInt(sex), 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                // .isDialog(true)//是否显示为对话框样式
                .build();

        pvOptions.setPicker(options1Items);//添加数据源

    }

    private TimePickerView pvTime;
    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 1, 1);
        pvTime= new TimePickerView.Builder(BaseInfoActiovity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
//                tvTime.setText(getTime(date));
                birthday_name = UiUtils.getTime(date.toString());
                tv_select_birthday.setText(birthday_name);
                birthday = birthday_name + "-01";

            }
        }).setType(TimePickerView.Type.YEAR_MONTH)
                .setTitleText("生日")
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            switch (requestCode) {
                case 100:
                    name = data.getExtras().getString("base_name");
                    tv_input_name.setText(name);
                    Log.i("base_namebase_name",name);
                    break;
                case 101:
                    height = data.getExtras().getString("height");
                    tv_input_high.setText(height);
                    break;
                case 102:
                    education_name = data.getExtras().getString("education");
                    education = data.getExtras().getString("educationId");
                    tv_input_education.setText(education_name);
                    break;
                case 103:
                    work_year_name = data.getExtras().getString("suffer");
                    work_year = data.getExtras().getString("sufferId");
                    tv_input_work_suffer.setText(work_year_name);
                    break;
                case 104:
                    address = data.getExtras().getString("describe");
                    tv_input_address.setText(address);
                    break;
                case 9:
                    if (flag) {
                        homeaddress_name = data.getExtras().getString("location");
                        homeaddress = data.getExtras().getString("locationId");
                        tv_select_location.setText(homeaddress_name);
                    } else {

                        census_name = data.getExtras().getString("location");
                        census = data.getExtras().getString("locationId");
                        tv_input_cesus_name.setText(census_name);
                    }

                    break;
            }
        }
    }

    /**
     * 显示性别
     */
    private void showSexView() {

        if ("1".equals(sex)) {
            tv_sex_input.setText("男");
        } else if ("2".equals(sex)) {
            tv_sex_input.setText("女");
        } else if ("0".equals(sex)) {
            tv_sex_input.setText("保密");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }
}
