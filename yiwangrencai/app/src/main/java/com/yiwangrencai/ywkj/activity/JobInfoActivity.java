package com.yiwangrencai.ywkj.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.ContextObjectDeserializer;

import com.hss01248.dialog.StyledDialog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.wxapi.WXEntryActivity;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.jmessage.ChatActivity;
import com.yiwangrencai.ywkj.tools.UiUtils;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.api.BasicCallback;
import co.lujun.androidtagview.TagContainerLayout;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Administrator on 2017/4/10.
 */

public class JobInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout iv_back_basic;
    String id;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    parsData();//解析数据
                    break;
                case 110:
                    StyledDialog.dismissLoading();
                    Toast.makeText(JobInfoActivity.this, "联网超时,请重试", Toast.LENGTH_SHORT).show();
                    break;
                case 120:
                    parscollectionData();//判断收藏是否成功
                    break;
                case 130:
                    parsSubmitResumeData();//判断投递简历是否成功
                    break;
                case ACCEPT_NOTIFICATION:
                    parAcceptData();//解析接受的返回数据
                    break;
                case REJECT_NOTIFICATION:
                    parRejectData();//解析拒绝的返回数据
                    break;
                case CANCEL_COLLECTION:
                    parsCancelData();//解析取消收藏的数据
                    break;
                case NUMBER_IPONE:
                    parsNumberPhone();//解析联系人的手机号码的数据
                    break;
            }
        }
    };

    private void parsNumberPhone() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(string);
            String codes = json.getString("code");
            String message = json.getString("msg");
            if ("1".equals(codes)) {
                JSONObject jsonPhone = json.getJSONObject("data");
                String phone = jsonPhone.getString("phone");
                String mobile = jsonPhone.getString("mobile");
                if (phone != null && !"".equals(phone)) {
                    callPhone(phone);
                } else if (mobile != null && !"".equals(mobile)) {
                    callPhone(mobile);
                } else if ((phone == null || "".equals(phone)) && (mobile == null || "".equals(mobile))) {
                    Toast.makeText(this, "公司只能投递简历", Toast.LENGTH_SHORT);
                }
            } else {

            }

            if (message != null && !"".equals(message)) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 解析 取消收藏的数据
     */
    private void parsCancelData() {
        StyledDialog.dismissLoading();
        if (code == 200) {
            JSONObject jsonObject = JSON.parseObject(str);
            String codes = jsonObject.getString("code");
            if ("1".equals(codes)) {
                tv_job_collection.setText("收藏");
                fav = "0";
                Toast.makeText(JobInfoActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(JobInfoActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(JobInfoActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析拒绝的返回数据
     */
    private void parRejectData() {
        StyledDialog.dismissLoading();
        if (code == 200) {
            JSONObject jsonObject = JSON.parseObject(str);
            String codes = jsonObject.getString("code");
            if ("1".equals(codes)) {
                Toast.makeText(JobInfoActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(JobInfoActivity.this, "拒绝面试通知失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(JobInfoActivity.this, "拒绝面试通知失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析接受返回的数据
     */
    private void parAcceptData() {
        StyledDialog.dismissLoading();
        if (code == 200) {
            JSONObject jsonObject = JSON.parseObject(str);
            String codes = jsonObject.getString("code");
            if ("1".equals(codes)) {
                Toast.makeText(JobInfoActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(JobInfoActivity.this, "接收面试通知失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(JobInfoActivity.this, "接收面试通知失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断投递简历是否成功
     */
    private void parsSubmitResumeData() {
        if (code == 200) {
            JSONObject jsondata = JSON.parseObject(str);
            String codess = jsondata.getString("code");
            if (codess.equals("1")) {
                StyledDialog.dismissLoading();
                Toast.makeText(JobInfoActivity.this, jsondata.getString("msg"), Toast.LENGTH_SHORT).show();

            } else {
                StyledDialog.dismissLoading();
                Toast.makeText(JobInfoActivity.this, jsondata.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } else {
            StyledDialog.dismissLoading();
            Toast.makeText(JobInfoActivity.this, "投递简历失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void callPhone(String number) {
        Intent intentt = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + number);
        intentt.setData(data);
        intentt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUESTCODE);

            return;
        } else {
            startActivity(intentt);
        }
    }

    /**
     * 判断收藏是否成功
     */
    private void parscollectionData() {
        StyledDialog.dismissLoading();
        if (code == 200) {
            JSONObject jsondata = JSON.parseObject(str);
            String codess = jsondata.getString("code");
            if (codess.equals("1")) {
                tv_job_collection.setText("已收藏");
                fav = "1";
                Toast.makeText(JobInfoActivity.this, jsondata.getString("msg"), Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(JobInfoActivity.this, jsondata.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(JobInfoActivity.this, "职位收藏失败", Toast.LENGTH_SHORT).show();
        }

    }

    String job_title;
    String salary;
    String language_name;
    String work_year_name;
    String education_name;
    String recruiting_num;
    String age;
    String gender;
    String location_name;
    String last_login_time;
    String rates;
    String average_time;
    String description;
    String company_name;
    String address;
    String employee_num_name;
    String comkind_name;
    String logo;
    String industry_name;
    String com_id;
    String uid;
    String job_id;
    String phone;
    String latitude;
    String longitude;
    String coordinate_address;
    String brands;
    String cert_status;
    String com_idd;
    String welfare;
    String fav;
    /**
     * 解析数据
     */
    private List<String> fareList = new ArrayList<>();

    private void parsData() {
        if (code == 200) {
            if (str == null) {

            } else {
                JSONObject jsons = JSON.parseObject(str);
                String codes = jsons.getString("code");
                String message = jsons.getString("msg");
                if (codes.equals("1")) {
                    progress_loading.setVisibility(View.INVISIBLE);
                    linearLayout_guider.setVisibility(View.VISIBLE);
                    srollviewtop.setVisibility(View.VISIBLE);
                    view_above.setVisibility(View.VISIBLE);
                    JSONObject jsondata = jsons.getJSONObject("data");

                    job_id = jsondata.getString("id");
                    com_id = jsondata.getString("com_id");
                    uid = jsondata.getString("uid");
                    job_title = jsondata.getString("job_title");
                    String department = jsondata.getString("department");
                    recruiting_num = jsondata.getString("recruiting_num");
                    description = jsondata.getString("description");
                    gender = jsondata.getString("gender");
                    welfare = jsondata.getString("welfare");
                    String contact_info = jsondata.getString("contact_info");
                    String job_status = jsondata.getString("job_status");
                    String part_status = jsondata.getString("part_status");
                    String pageviews = jsondata.getString("pageviews");
                    String xs_status = jsondata.getString("xs_status");
                    String send = jsondata.getString("send");
                    fav = jsondata.getString("fav");
                    industry_name = jsondata.getString("industry_name");
                    String job_category_name = jsondata.getString("job_category_name");
                    location_name = jsondata.getString("location_name");
                    education_name = jsondata.getString("education_name");
                    work_year_name = jsondata.getString("work_year_name");
                    language_name = jsondata.getString("language_name");
                    salary = jsondata.getString("salary");
                    age = jsondata.getString("age");
                    String is_urgent = jsondata.getString("is_urgent");
                    String contact = jsondata.getString("contact");

                    JSONObject jsonreply = jsondata.getJSONObject("reply");
                    rates = jsonreply.getString("rates");
                    average_time = jsonreply.getString("average_time");

                    JSONObject jsoncompanyuser = jsondata.getJSONObject("company_user");

                    last_login_time = jsoncompanyuser.getString("last_login_time");
                    JSONObject jsoncompany = jsondata.getJSONObject("company_basic");
                    com_idd = jsoncompany.getString("id");
                    company_name = jsoncompany.getString("company_name");
                    address = jsoncompany.getString("address");
                    employee_num_name = jsoncompany.getString("employee_num_name");
                    comkind_name = jsoncompany.getString("comkind_name");
                    logo = jsoncompany.getString("logo");
                    phone = jsoncompany.getString("phone");
                    latitude = jsoncompany.getString("latitude");
                    longitude = jsoncompany.getString("longitude");
                    brands = jsoncompany.getString("brands");
                    brands = jsoncompany.getString("brands");
                    cert_status = jsoncompany.getString("cert_status");
                    coordinate_address = jsoncompany.getString("coordinate_address");

                    String[] welfares = welfare.split(",");
                    for (int i = 0; i < welfares.length; i++) {
                        if (welfares[i] != null && !"".equals(welfares[i])) {
                            fareList.add(welfares[i]);
                        }
                    }
                    //展示数据
                    displayData();
                }else if("0".equals(codes)){
                    text_empty.setVisibility(View.VISIBLE);
                    text_empty.setText(message);
                    tv_job_collection.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            Toast.makeText(JobInfoActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 展示数据
     */
    private void displayData() {
        text_job_title.setText(job_title);
        text_job_salary.setText(salary + "/月");
        text_job_experience.setText(work_year_name);
        text_job_education.setText(education_name);

        if ("0".equals(recruiting_num)) {
            text_job_people.setText("人数若干");
        } else {
            text_job_people.setText(recruiting_num + "人");
        }

        text_job_age.setText(age);

        text_job_language.setText(language_name);
        textLocation.setText(location_name);
        textviewname.setText(company_name);
        textviewtime.setText(last_login_time);
        text_job_reply.setText("低于" + rates + "%");
        text_job_today.setText(average_time);
        text_job_describue.setText(description);
        text_job_industry.setText(employee_num_name + " | " + comkind_name);
        text_job_address.setText(address);

        if (cert_status.equals("1")) {
            textview.setText("已认证");
        } else {
            textview.setVisibility(View.INVISIBLE);
        }

        if ("0".equals(fav)) {
            tv_job_collection.setText("收藏");
        } else if ("1".equals(fav)) {
            tv_job_collection.setText("已收藏");
        }

        if (!brands.isEmpty()) {
            textviewnamee.setText(brands);
        } else {
            textviewnamee.setVisibility(View.GONE);
        }

        if (gender.equals("0")) {
            text_job_sex.setText("男女不限");
        } else if (gender.equals("1")) {
            text_job_sex.setText("男生");
        } else if (gender.equals("2")) {
            text_job_sex.setText("女生");
        }

        if (fareList.size() == 0) {

        } else {
            scrollview.setTags(fareList);
        }


        Picasso.with(JobInfoActivity.this)
                .load(ContentUrl.BASE_ICON_URL + logo)
                .placeholder(R.mipmap.cell_com_default2x)
                .error(R.mipmap.cell_com_default2x)
                .into(imageviewavater);
    }

    String resume_id;
    String chat_pwd;
    String codess;
    String register="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_info_activity);
        SharedPreferences shared = getSharedPreferences("data", MODE_PRIVATE);
        resume_id = shared.getString("resume_id", "");
        chat_pwd = shared.getString("chat_pwd", "");
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        notificationId = intent.getStringExtra("ids");
        statues = intent.getStringExtra("statues");
        codess = intent.getStringExtra("params");
        register = intent.getStringExtra("register");

        initView();//初始化界面
        initData();//初始化数据

    }

    TextView text_job_title;
    TextView text_job_salary;
    TextView text_job_experience;
    TextView text_job_education;
    TextView text_job_people;
    TextView text_job_age;
    TextView text_job_sex;
    TextView text_job_language;
    TagContainerLayout scrollview;
    TextView textLocation;
    TextView text_job_address;
    TextView textviewtime;
    TextView textviewname;
    TextView text_job_industry;
    TextView text_job_reply;
    TextView text_job_today;
    TextView text_job_describue;
    TextView textviewnamee;
    TextView textview;
    TextView tv_job_collection;
    TextView tv_job_reject;
    TextView tv_job_accept;
    TextView message_chart;
    ImageView imageviewavater;
    RelativeLayout progress_loading;
    LinearLayout linearLayout_guider;
    ScrollView srollviewtop;
    LinearLayout linear_chat;
    View view_above;
    private ProgressBar progress;
    private TextView text_empty;

    /**
     * 初始化界面
     */
    private void initView() {

        iv_back_basic = (FrameLayout) findViewById(R.id.iv_back_basic);
        RelativeLayout linear_call = (RelativeLayout) findViewById(R.id.linear_call);
        LinearLayout text_submit_resume = (LinearLayout) findViewById(R.id.text_submit_resume);
        RelativeLayout relative_location = (RelativeLayout) findViewById(R.id.relative_location);
        RelativeLayout relative_company = (RelativeLayout) findViewById(R.id.relative_company);
        tv_job_collection = (TextView) findViewById(R.id.tv_job_collection);
        srollviewtop = (ScrollView) findViewById(R.id.srollviewtop);

        text_job_title = (TextView) findViewById(R.id.text_job_title);
        text_job_salary = (TextView) findViewById(R.id.text_job_salary);
        text_job_experience = (TextView) findViewById(R.id.text_job_experience);
        text_job_education = (TextView) findViewById(R.id.text_job_education);
        text_job_people = (TextView) findViewById(R.id.text_job_people);
        text_job_age = (TextView) findViewById(R.id.text_job_age);
        text_job_sex = (TextView) findViewById(R.id.text_job_sex);
        text_job_language = (TextView) findViewById(R.id.text_job_language);
        scrollview = (TagContainerLayout) findViewById(R.id.textwelfare);
        textLocation = (TextView) findViewById(R.id.textLocation);
        text_job_address = (TextView) findViewById(R.id.text_job_address);
        textviewtime = (TextView) findViewById(R.id.textviewtime);
        textviewname = (TextView) findViewById(R.id.textviewname);
        text_job_industry = (TextView) findViewById(R.id.text_job_industry);
        text_job_reply = (TextView) findViewById(R.id.text_job_reply);
        text_job_today = (TextView) findViewById(R.id.text_job_today);
        text_job_describue = (TextView) findViewById(R.id.text_job_describue);
        textviewnamee = (TextView) findViewById(R.id.textviewnamee);
        textview = (TextView) findViewById(R.id.textview);
        tv_job_reject = (TextView) findViewById(R.id.tv_job_reject);
        tv_job_accept = (TextView) findViewById(R.id.tv_job_accept);
        imageviewavater = (ImageView) findViewById(R.id.imageviewavater);
        progress_loading = (RelativeLayout) findViewById(R.id.progress_loading);
        linearLayout_guider = (LinearLayout) findViewById(R.id.linearLayout_guider);
        linear_chat = (LinearLayout) findViewById(R.id.linear_chat);
        view_above = (View) findViewById(R.id.view_above);
        progress = (ProgressBar) findViewById(R.id.progress);
        text_empty = (TextView) findViewById(R.id.text_empty);

        iv_back_basic.setOnClickListener(this);
        tv_job_collection.setOnClickListener(this);
        linear_call.setOnClickListener(this);
        text_submit_resume.setOnClickListener(this);
        relative_location.setOnClickListener(this);
        relative_company.setOnClickListener(this);
        tv_job_reject.setOnClickListener(this);
        tv_job_accept.setOnClickListener(this);
        linear_chat.setOnClickListener(this);
        progress_loading.setVisibility(View.VISIBLE);
        text_empty.setVisibility(View.INVISIBLE);
        linearLayout_guider.setVisibility(View.INVISIBLE);
        srollviewtop.setVisibility(View.INVISIBLE);

        if (notificationId != null && !"".equals(notificationId)) {
            if (statues != null && statues.equals("2")) {
                tv_job_reject.setVisibility(View.VISIBLE);
                tv_job_accept.setVisibility(View.GONE);
                tv_job_reject.setClickable(false);
                tv_job_reject.setText("已接受");
            } else if (statues != null && statues.equals("3")) {
                tv_job_reject.setVisibility(View.VISIBLE);
                tv_job_accept.setVisibility(View.GONE);
                tv_job_reject.setClickable(false);
                tv_job_reject.setText("已拒绝");
            } else {
                tv_job_reject.setVisibility(View.VISIBLE);
                tv_job_accept.setVisibility(View.VISIBLE);
            }

        } else {
            tv_job_reject.setVisibility(View.INVISIBLE);
            tv_job_accept.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 初始化数据
     */
    String str;
    int code;
    String api_token;

    private void initData() {

        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");

        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        form.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.COMPANY_JOB_SHOW)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(form.build())
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
        Log.i("0000000000", str);
        handler.sendEmptyMessage(100);

    }
});
        }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_basic:
                if (notificationId != null && !"".equals(notificationId)) {
                    Intent intent = new Intent(JobInfoActivity.this, InterviewActivity.class);
                    intent.putExtra("register",register);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if (codess != null && "codess".equals(codess)) {
                    Intent intent = new Intent(JobInfoActivity.this, MainActivity.class);
                    intent.putExtra("register",register);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else if("message".equals(register)){
                    Intent intent = new Intent(JobInfoActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("register",register);
                    startActivity(intent);
                    finish();
                }else if("interView".equals(register)){
                    Intent intent = new Intent(JobInfoActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("interView",register);
                    startActivity(intent);
                    finish();
                }else{
                    finish();
                }

                break;
            case R.id.tv_job_reject://拒绝  *查看，接受，拒绝面试：interview/update
                rejestInterview();//拒绝接受面试邀请
                break;
            case R.id.tv_job_accept://接受
                acceptInterview();//接受面试
                break;
            case R.id.tv_job_collection://收藏
                if ("0".equals(fav)) {
                    colectionJob();//收藏职位
                } else if ("1".equals(fav)) {
                    cancelCollectionJob();//取消收藏
                }

                break;
            case R.id.relative_location://地址
                Intent intentAdd = new Intent(JobInfoActivity.this, AddressActivity.class);
                intentAdd.putExtra("address", address);
                intentAdd.putExtra("latitude", latitude);
                intentAdd.putExtra("longitude", longitude);
                intentAdd.putExtra("coordinate_address", coordinate_address);
                startActivity(intentAdd);
                break;
            case R.id.relative_company://企业详情
                //TODO
                Intent intent = new Intent(JobInfoActivity.this, CompanydetailActivityTwo.class);
                intent.putExtra("id", com_idd);
                intent.putExtra("com_id", com_id);
                startActivity(intent);
                break;
            case R.id.linear_call://打电话
                gainContextPeople();//获取联系人电话
                break;
            case R.id.text_submit_resume://投递简历
                if (api_token == null || "".equals(api_token)) {
                    Intent intent1 = new Intent(JobInfoActivity.this, WXEntryActivity.class);
                    startActivity(intent1);
                } else {
                    if (resume_id == null || "".equals(resume_id)) {
                        Toast.makeText(JobInfoActivity.this, "您还没有创建简历", Toast.LENGTH_SHORT).show();
                    } else {
                        submitResume();//投递简历
                    }
                }
                break;
            case R.id.linear_chat://点击进入聊天界面
                entryChartActivity();//
                break;
        }
    }

    private void entryChartActivity() {
        if (api_token == null || "".equals(api_token)) {
            Intent intent = new Intent(JobInfoActivity.this, WXEntryActivity.class);
            startActivity(intent);
        } else {
            if (resume_id == null || "".equals(resume_id)) {
                Toast.makeText(JobInfoActivity.this, "您还没有创建简历", Toast.LENGTH_SHORT).show();
            } else if (chat_pwd == null || "".equals(chat_pwd)) {
                Toast.makeText(JobInfoActivity.this, "聊天初始化数据失败,请重新登录", Toast.LENGTH_SHORT).show();
            } else {

                OkHttpClient okHttpClient = new OkHttpClient();
                FormEncodingBuilder formEncoding = new FormEncodingBuilder();
                formEncoding.add("id", uid);
                formEncoding.add("type", "1");

                final Request request = new Request.Builder()
                        .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                        .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                        .url(ContentUrl.BASE_URL + "register_chat")
                        .post(formEncoding.build())
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        //loginHuanXin();
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Log.i("aaaaaaaaaaaaaa", response.body().string());
                        loginHuanXin();
                    }
                });
            }
            // loginHuanXin();
        }
    }

    private void loginHuanXin() {
        SharedPreferences shared = getSharedPreferences("data", MODE_PRIVATE);
        String uids = shared.getString("uid", "");

        Log.i("++++++++", "com_id::" + com_id + " job_id::" + job_id + "  resume_id::" + resume_id);
        Intent intent = new Intent(JobInfoActivity.this, ChatActivity.class);
        intent.putExtra("targetId", "company_" + uid);
        intent.putExtra("conv_title", company_name);
        intent.putExtra("id", com_idd);
        intent.putExtra("com_id", com_idd);
        intent.putExtra("job_id", job_id);
        intent.putExtra("api_token", api_token);
        intent.putExtra("resume_id", resume_id);
        intent.putExtra("job_title", job_title);
        intent.putExtra("targetAppKey", "7daab1fe14f8955e18ef60e3");
        startActivity(intent);
    }

    private String string;
    private static final int NUMBER_IPONE = 4;
    private void gainContextPeople() {
        if (id == null || "".equals(id)) {

        } else {
            OkHttpClient okHttpClient = new OkHttpClient();
            FormEncodingBuilder formEncoding = new FormEncodingBuilder();
            formEncoding.add("id", id);
            Request request = new Request.Builder()
                    .url(ContentUrl.BASE_URL + "company_job/get_contact")
                    .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                    .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                    .post(formEncoding.build())
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    string = response.body().string();
                    Log.i("获取的联系人的手机号码", string);
                    handler.sendEmptyMessage(NUMBER_IPONE);
                }
            });
        }
    }

    /**
     * 取消收藏
     */
    private static final int CANCEL_COLLECTION = 3;

    private void cancelCollectionJob() {
        StyledDialog.buildLoading().show();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formcode = new FormEncodingBuilder();
        formcode.add("com_id", com_idd);
        formcode.add("job_id", job_id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.FAVORITES_DELETE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formcode.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("取消收藏", str);
                handler.sendEmptyMessage(CANCEL_COLLECTION);
            }
        });
    }

    /**
     * 拒绝接受面试邀请
     */
    private static final int REJECT_NOTIFICATION = 2;

    private void rejestInterview() {

        StyledDialog.buildLoading().show();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formcode = new FormEncodingBuilder();
        formcode.add("id", notificationId);
        formcode.add("status", "3");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.INTERVIEW_UPDATE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formcode.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("接拒绝面试邀请", str);
                handler.sendEmptyMessage(REJECT_NOTIFICATION);
            }
        });
    }

    /**
     * 接受面试
     */
    private static final int ACCEPT_NOTIFICATION = 0;
    String notificationId;
    String statues;

    private void acceptInterview() {
        StyledDialog.buildLoading().show();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formcode = new FormEncodingBuilder();
        formcode.add("id", notificationId);
        formcode.add("status", "2");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.INTERVIEW_UPDATE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formcode.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("接受面试邀请", str);
                handler.sendEmptyMessage(ACCEPT_NOTIFICATION);
            }
        });
    }

    private final static int REQUESTCODE = 0;

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUESTCODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    // AskForPermission();
                }
            }
        }
    }

    private void AskForPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permission!");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                startActivity(intent);
            }
        });
        builder.create().show();
    }

    /**
     * 投递简历
     */
    private void submitResume() {

        if (api_token == null || "".equals(api_token)) {
            Intent intent = new Intent(JobInfoActivity.this, WXEntryActivity.class);
            startActivity(intent);
        } else {
            StyledDialog.buildLoading().show();
            OkHttpClient okhttpclients = new OkHttpClient();
            FormEncodingBuilder formEcdings = new FormEncodingBuilder();
            formEcdings.add("job_id", job_id);
            Request request = new Request.Builder()
                    .url(ContentUrl.BASE_URL + ContentUrl.RESUME_SEND_CREATE)
                    .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                    .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                    .post(formEcdings.build())
                    .build();

            okhttpclients.newCall(request).enqueue(new Callback() {
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
                    Log.i("22222222", str);
                    Log.i("22222222", code + "");
                    handler.sendEmptyMessage(130);
                }
            });
        }

    }


    /**
     * 收藏职位
     */
    private void colectionJob() {

        if (api_token == null || "".equals(api_token)) {
            Intent intent = new Intent(JobInfoActivity.this, WXEntryActivity.class);
            startActivity(intent);
        } else {

            StyledDialog.buildLoading().show();
            OkHttpClient okhttpClient = new OkHttpClient();
            FormEncodingBuilder formEcding = new FormEncodingBuilder();
            formEcding.add("com_id", com_idd);
            formEcding.add("job_id", job_id);
            Request request = new Request.Builder()
                    .url(ContentUrl.BASE_URL + ContentUrl.FAVORITES_CREATE)
                    .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                    .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                    .post(formEcding.build())
                    .build();
            okhttpClient.newCall(request).enqueue(new Callback() {
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
                    Log.i("11111111", str);
                    handler.sendEmptyMessage(120);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (codess != null && "codess".equals(codess)) {
            Intent intent = new Intent(JobInfoActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("register",register);
            startActivity(intent);
        } else if("message".equals(register)){
            Intent intent = new Intent(JobInfoActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("register","message");
            startActivity(intent);
            finish();
        }else{
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }
}
