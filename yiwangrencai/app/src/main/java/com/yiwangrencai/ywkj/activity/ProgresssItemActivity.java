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
 * Created by Administrator on 2017/5/5.
 */

public class ProgresssItemActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_item_progress;
    }

    private String id;
    private String api_token;

    @Override
    public void initData() {
        //获取api_token
        SharedPreferences share=getSharedPreferences("Activity",MODE_PRIVATE);
        api_token = share.getString("api_token","");
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        //获取服务器数据
        gainDataFromService();

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    JSONObject json = JSON.parseObject(strdata);
                    String scode = json.getString("code");
                    if (scode.equals("1")) {
                        JSONObject jsonData = json.getJSONObject("data");
                        parasJsonData(jsonData);//解析数据
                    }
                    break;
                case 300:
                    if (code == 200) {
                        JSONObject jsons = JSON.parseObject(strsss);
                        String code = jsons.getString("code");
                        if (code.equals("1")) {

                            Toast.makeText(ProgresssItemActivity.this, jsons.getString("msg"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProgresssItemActivity.this, EditResumeTwoActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ProgresssItemActivity.this, "更新数据失败请重试", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ProgresssItemActivity.this, "更新数据失败请重试" + code, Toast.LENGTH_SHORT).show();
                    }
                    StyledDialog.dismissLoading();
                    break;
            }
        }
    };

    //String id;
    String project_name;
    String starttime;
    String endtime;
    String post;
    String content;
    String starttime_name;
    String endtime_name;

    /**
     * 解析数据
     *
     * @param jsonData
     */
    private void parasJsonData(JSONObject jsonData) {
        id = jsonData.getString("id");
        project_name = jsonData.getString("project_name");
        starttime = jsonData.getString("starttime");
        endtime = jsonData.getString("endtime");
        post = jsonData.getString("post");
        content = jsonData.getString("content");
        starttime_name = jsonData.getString("starttime_name");
        endtime_name = jsonData.getString("endtime_name");

        tv_input_company_name.setText(project_name);
        tv_entry_time.setText(starttime_name);
        tv_leave_time.setText(endtime_name);
        tv_input_work_names.setText(post);
        tv_describe_work.setText(content);

    }

    String strdata;
    int code;

    /**
     * 从服务器获取数据
     */
    private void gainDataFromService() {
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_PROEXP_EDIT)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(formEncoding.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strdata = response.body().string();
                code = response.code();
                Log.i("44545454545", strdata);
                handler.sendEmptyMessage(100);
            }
        });


    }


    private TextView tv_input_company_name;
    private TextView tv_entry_time;
    private TextView tv_leave_time;
    private TextView tv_input_work_names;
    private TextView tv_describe_work;

    @Override
    public void initView() {

        initTimePicker();
        FrameLayout iv_back_basic = (FrameLayout) findViewById(R.id.iv_back_basic);
        TextView tv_next_basic = (TextView) findViewById(R.id.tv_next_basic);
        RelativeLayout rl_company_name = (RelativeLayout) findViewById(R.id.rl_company_name);
        RelativeLayout rl_entry_time = (RelativeLayout) findViewById(R.id.rl_entry_time);
        RelativeLayout rl_dimission = (RelativeLayout) findViewById(R.id.rl_dimission);
        RelativeLayout rl_work_name = (RelativeLayout) findViewById(R.id.rl_work_name);
        RelativeLayout rl_work_discriub = (RelativeLayout) findViewById(R.id.rl_work_discriub);
        LinearLayout linearLayout_delete = (LinearLayout) findViewById(R.id.linearLayout_delete);

        tv_input_company_name = (TextView) findViewById(R.id.tv_input_company_name);
        tv_entry_time = (TextView) findViewById(R.id.tv_entry_time);
        tv_leave_time = (TextView) findViewById(R.id.tv_leave_time);
        tv_input_work_names = (TextView) findViewById(R.id.tv_input_work_names);
        tv_describe_work = (TextView) findViewById(R.id.tv_describe_work);

        iv_back_basic.setOnClickListener(this);
        tv_next_basic.setOnClickListener(this);
        tv_next_basic.setOnClickListener(this);

        rl_company_name.setOnClickListener(this);
        rl_entry_time.setOnClickListener(this);
        rl_dimission.setOnClickListener(this);
        rl_work_name.setOnClickListener(this);
        rl_work_discriub.setOnClickListener(this);
        linearLayout_delete.setOnClickListener(this);

    }

    String flag;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_basic://返回
                finish();
                break;
            case R.id.tv_next_basic://保存
                //Toast.makeText(ProgresssItemActivity.this, "0000000000", Toast.LENGTH_SHORT).show();
                updateDataservicess();//更新数据
                break;
            case R.id.rl_company_name://公司名称
                Intent companyName = new Intent(ProgresssItemActivity.this, NameActivity.class);
                companyName.putExtra("params", "projectName");
                companyName.putExtra("mobile", project_name);
                startActivityForResult(companyName, 300);
                break;
            case R.id.rl_entry_time://入职时间
                pvTime.show();
                flag = "start";
                break;
            case R.id.rl_dimission://离职时间
                pvTime.show();
                flag = "end";
                break;
            case R.id.rl_work_name://担任职位
                Intent workIntent = new Intent(ProgresssItemActivity.this, NameActivity.class);
                workIntent.putExtra("params", "projectwork");
                workIntent.putExtra("mobile", post);
                startActivityForResult(workIntent, 301);
                break;
            case R.id.rl_work_discriub://工作描述
                Intent intentDecribe = new Intent(ProgresssItemActivity.this, DescribeActivity.class);
                intentDecribe.putExtra("params", "projectDiscrube");
                intentDecribe.putExtra("introduction", content);
                startActivityForResult(intentDecribe, 302);
                break;
            case R.id.linearLayout_delete://删除
                deleteProjectData();//删除数据
                break;
        }
    }


    /**
     * 删除数据
     */
    private void deleteProjectData() {

        StyledDialog.buildLoading().show();
        OkHttpClient okhttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_PROEXP_DELETE)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(formEncoding.build())
                .build();
        okhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strsss = response.body().string();
                code = response.code();
                handler.sendEmptyMessage(300);
            }
        });
    }

    String strsss;

    private void updateDataservicess() {

        StyledDialog.buildLoading().show();
        OkHttpClient okhttpp = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);
        formEncoding.add("project_name", project_name);
        formEncoding.add("starttime", starttime );
        formEncoding.add("endtime", endtime );
        formEncoding.add("post", post);
        formEncoding.add("content", content);

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_PROEXP_UPDATE)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(formEncoding.build())
                .build();
        okhttpp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strsss = response.body().string();
                int code = response.code();
                System.out.print("更新项目数据成功" + code);
                handler.sendEmptyMessage(300);
            }
        });


    }

    /**
     * 开启一个界面返回的数据
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
                    project_name = data.getExtras().getString("projectName");
                    tv_input_company_name.setText(project_name);
                    break;
                case 301:
                    post = data.getExtras().getString("projectwork");
                    tv_input_work_names.setText(post);
                    break;
                case 302:
                    content = data.getExtras().getString("describes");
                    tv_describe_work.setText(content);
                    break;
            }
        }
    }

    TimePickerView pvTime;

    /**
     * 显示时间
     */
    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1950, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 1, 1);
        pvTime = new TimePickerView.Builder(ProgresssItemActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
        //      tvTime.setText(getTime(date));
                if (flag == "start") {
                    starttime = UiUtils.getTime(date.toString());
                    tv_entry_time.setText(starttime);
                    starttime=starttime+"-01";
                } else if (flag == "end") {
                    endtime = UiUtils.getTime(date.toString());
                    tv_leave_time.setText(endtime);
                    endtime=endtime+"-01";
                }
            }
        }).setType(TimePickerView.Type.YEAR_MONTH)
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
