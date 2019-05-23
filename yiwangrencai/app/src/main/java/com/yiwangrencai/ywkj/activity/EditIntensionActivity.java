package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/29.
 */

public class EditIntensionActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.edit_intension_activity;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    JSONObject json = JSON.parseObject(str);
                    String code = json.getString("code");
                    if ("1".equals(code)) {
                        Intent intent = new Intent(EditIntensionActivity.this, EditResumeTwoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    StyledDialog.dismissLoading();
                    break;
                case 2000:
                    Toast.makeText(EditIntensionActivity.this, "简历意向更新失败", Toast.LENGTH_SHORT).show();
                    break;
                case 100:
                    parsGainDataFromService();//解析获取到的数据
                    break;
            }
        }
    };

    /**
     * 解析获取到的数据
     */
    String jobsortStr = "";
    String jobareaStr = "";
    private List<String> list = new ArrayList<>();
    private List<String> listarea = new ArrayList<>();

    private void parsGainDataFromService() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            if ("1".equals(codes)) {
                JSONObject jsonObject = json.getJSONObject("data");
                expectedsalary = jsonObject.getString("expectedsalary");
                isexpectedsalary = jsonObject.getString("isexpectedsalary");
                intentionjobs = jsonObject.getString("intentionjobs");
                birthday_name = jsonObject.getString("birthday_name");
                expectedsalary_name = jsonObject.getString("expectedsalary_name");
                jobsort = jsonObject.getJSONArray("jobsort");
                jobsort_names = jsonObject.getString("jobsort_name");
                jobarea = jsonObject.getJSONArray("jobarea");
                jobarea_name = jsonObject.getString("jobarea_name");

                if (jobarea != null && jobarea.size() != 0) {
                    for (int i = 0; i < jobarea.size(); i++) {
                        jobareaa_name.add(jobarea_name.split(",")[i]);
                        jobareaa.add(jobarea.get(i) + "");
                    }
                }

                String[] jobsort_name = jobsort_names.split(",");
                showViewDiscuss();

                if (intentionjobs == null || "".equals(intentionjobs)) {
                    textViewIntentsion_work.setText("请选择想要的工作");
                } else {
                    textViewIntentsion_work.setText(intentionjobs);
                }

                if (jobsort_names == null || "".equals(jobsort_names)) {
                    tv_select_work.setText("请选择工作岗位");
                } else {
                    tv_select_work.setText(jobsort_names);
                }

                if (jobarea_name == null || "".equals(jobarea_name)) {
                    tv_select_area_work.setText("请选择工作地区");
                } else {
                    tv_select_area_work.setText(jobarea_name);
                }

                for (int i = 0; i < jobsort.size(); i++) {
                    jobsortt_name.add(jobsort_name[i]);
                    jobsortt.add(jobsort.get(i) + "");
                    if (i == jobsort.size() - 1) {
                        jobsortStr += jobsort.get(i);
                    } else {
                        jobsortStr += jobsort.get(i) + ",";
                    }
                }

                for (int i = 0; i < jobarea.size(); i++) {
                    listarea.add(jobarea.get(i) + "");
                    Log.i("截取的字符串222", listarea.get(i) + "");

                    if (i == jobarea.size() - 1) {
                        jobareaStr += jobarea.get(i);
                    } else {
                        jobareaStr += jobarea.get(i) + ",";
                    }
                }

                if (expectedsalary == null || "0".equals(expectedsalary) || "".equals(expectedsalary)) {
                    tv_select_saraly.setText("请选择期望的薪资");
                } else {
                    tv_select_saraly.setText(expectedsalary_name);
                }

            } else {
                Toast.makeText(EditIntensionActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(EditIntensionActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    String id;
    String api_token;
    int code;

    @Override
    public void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");

        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_INTENT_EDIT)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncoding.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
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
                Log.i("获取编辑建立意向的数据", str);
                handler.sendEmptyMessage(100);
            }
        });


    }

    private FrameLayout iv_back_basic;
    private TextView tv_next_intension;
    private RelativeLayout rl_salary_expection;
    private RelativeLayout rl_discuss_personally;
    private RelativeLayout rl_intension_job;
    private RelativeLayout rl_place_job;
    private RelativeLayout rl_work_area;
    private TextView tv_select_saraly;
    private TextView tv_view_disscuess;
    private TextView textViewIntentsion_work;
    private TextView tv_select_work;
    private TextView tv_select_area_work;
    private String expectedsalary_name="";
    private String intensionsaralyId;
    private String isexpectedsalary="";
    private String intentionjobs="";
    private String expectedsalary="";
    private String birthday_name;
    private JSONArray jobsort;
    private JSONArray jobarea;
    String jobsort_names="";
    String jobarea_name="";

    @Override
    public void initView() {

        iv_back_basic = (FrameLayout) findViewById(R.id.iv_back_basic);
        tv_next_intension = (TextView) findViewById(R.id.tv_next_intension);
        rl_salary_expection = (RelativeLayout) findViewById(R.id.rl_salary_expection);
        rl_discuss_personally = (RelativeLayout) findViewById(R.id.rl_discuss_personally);
        rl_intension_job = (RelativeLayout) findViewById(R.id.rl_intension_job);
        rl_place_job = (RelativeLayout) findViewById(R.id.rl_place_job);
        rl_work_area = (RelativeLayout) findViewById(R.id.rl_work_area);

        tv_select_saraly = (TextView) findViewById(R.id.tv_select_saraly);
        tv_view_disscuess = (TextView) findViewById(R.id.tv_view_disscuess);
        textViewIntentsion_work = (TextView) findViewById(R.id.textViewIntentsion_work);
        tv_select_work = (TextView) findViewById(R.id.tv_select_work);
        tv_select_area_work = (TextView) findViewById(R.id.tv_select_area_work);

        iv_back_basic.setOnClickListener(this);
        tv_next_intension.setOnClickListener(this);
        rl_salary_expection.setOnClickListener(this);
        rl_discuss_personally.setOnClickListener(this);
        rl_intension_job.setOnClickListener(this);
        rl_place_job.setOnClickListener(this);
        rl_work_area.setOnClickListener(this);
        showViewPopywind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_basic:
                finish();
                break;
            case R.id.tv_next_intension://保存
                //保存修改的数据
                updataEditJobINtension();
                break;
            case R.id.rl_salary_expection://期望薪资
                Intent intent = new Intent(EditIntensionActivity.this, EducationActivity.class);
                intent.putExtra("education", "intensionsaraly");
                intent.putExtra("company_name", expectedsalary_name);
                startActivityForResult(intent, 20);

                break;
            case R.id.rl_discuss_personally://显示面议
                //显示面议

                pvOptions.show();
                break;
            case R.id.rl_intension_job://意向工作岗位
                Intent intentJob = new Intent(EditIntensionActivity.this, NameActivity.class);
                intentJob.putExtra("mobile", intentionjobs);
                intentJob.putExtra("params", "intension");
                startActivityForResult(intentJob, 21);

                break;
            case R.id.rl_place_job://工作岗位
                Intent work = new Intent(EditIntensionActivity.this, AllWorkActivity.class);
                // work.putExtra()
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("job_category", (ArrayList<String>) jobsortt_name);
                bundle.putStringArrayList("job_categoryId", (ArrayList<String>) jobsortt);
                work.putExtras(bundle);
                startActivityForResult(work, 50);
                break;
            case R.id.rl_work_area://工作地区
                Intent intentArea = new Intent(EditIntensionActivity.this, LocationActivity.class);
                intentArea.putExtra("params", "");
                Bundle bundleName=new Bundle();
                bundleName.putStringArrayList("jobareaaId", (ArrayList<String>) jobareaa);
                bundleName.putStringArrayList("jobareaaName", (ArrayList<String>) jobareaa_name);
                intentArea.putExtras(bundleName);
                startActivityForResult(intentArea, 9);

                break;
        }
    }

    String str;

    /**
     * 向服务器保存修改的数据
     */
    private void updataEditJobINtension() {


        if (expectedsalary == null || expectedsalary.isEmpty()) {
            Toast.makeText(EditIntensionActivity.this, "请选择期望薪资", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isexpectedsalary == null || isexpectedsalary.isEmpty()) {
            Toast.makeText(EditIntensionActivity.this, "请选择是否显示面议", Toast.LENGTH_SHORT).show();
            return;
        }

        if (intentionjobs == null || intentionjobs.isEmpty()) {
            Toast.makeText(EditIntensionActivity.this, "请选择意向工作岗位", Toast.LENGTH_SHORT).show();
            return;
        }
        if (jobsortStr == null || jobsortStr.isEmpty()) {
            Toast.makeText(EditIntensionActivity.this, "请选择工作岗位", Toast.LENGTH_SHORT).show();
            return;
        }

        if (jobareaStr == null || jobareaStr.isEmpty()) {
            Toast.makeText(EditIntensionActivity.this, "请选择工作地区", Toast.LENGTH_SHORT).show();
            return;
        }

        StyledDialog.buildLoading().show();
        SharedPreferences shares = getSharedPreferences("Activity", MODE_PRIVATE);
        String api_token = shares.getString("api_token", "");
        SharedPreferences share = getSharedPreferences("data", MODE_PRIVATE);
        String id = share.getString("resume_id", "");
        OkHttpClient okhtpClient = new OkHttpClient();
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        formEncodingBuilder.add("id", id);
        formEncodingBuilder.add("expectedsalary", expectedsalary);
        formEncodingBuilder.add("isexpectedsalary", isexpectedsalary);
        formEncodingBuilder.add("intentionjobs", intentionjobs);
        formEncodingBuilder.add("jobsort", jobsortStr);
        formEncodingBuilder.add("jobarea", jobareaStr);

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_INTENT)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(formEncodingBuilder.build())
                .build();
        okhtpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(2000);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                System.out.println("6666666622222" + str);
                handler.sendEmptyMessage(1000);

            }
        });
    }


    /**
     * 显示面议
     */
    private void showViewDiscuss() {
        //tv_view_disscuess.setText(isexpectedsalary);
        if (isexpectedsalary == null || "".equals(isexpectedsalary)) {
            tv_view_disscuess.setText("请选择面议");
        } else {
            if (isexpectedsalary.equals("1")) {
                tv_view_disscuess.setText("显示面议");
            } else if (isexpectedsalary.equals("0")) {
                tv_view_disscuess.setText("不显示面议");
            }
        }

    }


    /**
     * 显示面议
     */
    private OptionsPickerView pvOptions;

    private void showViewPopywind() {
        final List<String> options1Items = new ArrayList<>();
        options1Items.add("显示面议");
        options1Items.add("不显示面议");
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);
                tv_view_disscuess.setText(tx);
                if (options1 == 0) {
                    isexpectedsalary = "1";
                } else if (options1 == 1) {
                    isexpectedsalary = "0";
                }
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("显示面议")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFffffff)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
                //.setLabels("省", "市", "区")//设置选择的三级单位
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                // .isDialog(true)//是否显示为对话框样式
                .build();

        pvOptions.setPicker(options1Items);//添加数据源

    }

    List<String> jobsortt_name = new ArrayList<>();
    List<String> jobsortt = new ArrayList<>();
    List<String> jobareaa_name = new ArrayList<>();
    List<String> jobareaa = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
        } else {
            switch (requestCode) {
                case 20://期望薪资
                    expectedsalary_name = data.getExtras().getString("intensionsaraly");
                    expectedsalary = data.getExtras().getString("intensionsaralyId");
                    tv_select_saraly.setText(expectedsalary_name);
                    break;
                case 21://意向工作
                    intentionjobs = data.getExtras().getString("intension");
                    textViewIntentsion_work.setText(intentionjobs);
                    break;
                case 50://工作岗位
                    jobsortt_name = (List<String>) data.getExtras().getSerializable("listItem");
                    jobsortt = (List<String>) data.getExtras().getSerializable("cid");
                    if (jobsortt.size() == 0) {
                    } else {
                        jobsort_names = "";
                        jobsortStr = "";
                        for (int i = 0; i < jobsortt_name.size(); i++) {
                            if (i == jobsortt_name.size() - 1) {
                                jobsort_names += jobsortt_name.get(i);
                                jobsortStr += jobsortt.get(i);
                            } else {
                                jobsort_names += jobsortt_name.get(i) + ",";
                                jobsortStr += jobsortt.get(i) + ",";
                            }
                        }
                    }
                    tv_select_work.setText(jobsort_names);
                    break;
                case 9://工作地区
                    jobareaa_name = (List<String>) data.getExtras().getSerializable("listArea");
                    jobareaa = (List<String>) data.getExtras().getSerializable("listAreaId");
                    if (jobareaa.size() == 0) {

                    } else {
                        jobarea_name = "";
                        jobareaStr = "";
                        for (int i = 0; i < jobareaa_name.size(); i++) {

                            if (i == jobareaa_name.size() - 1) {
                                jobarea_name += jobareaa_name.get(i);
                                jobareaStr += jobareaa.get(i);
                            } else {
                                jobarea_name += jobareaa_name.get(i) + ",";
                                jobareaStr += jobareaa.get(i) + ",";
                            }
                        }
                    }
                    tv_select_area_work.setText(jobarea_name);
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
