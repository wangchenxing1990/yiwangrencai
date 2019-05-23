package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.yiwangrencai.ywkj.bean.WorkExperience;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.UiUtils;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */
public class WorkExpActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.work_expersion_activity;
    }

    @Override
    public void initData() {

    }

    private TextView tv_next_skip_work;
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
    private TextView tv_input_work_names;
    private TextView tv_select_natrue_business;
    private TextView tv_select_company_scale;
    private TextView tv_suoshu_hangye;
    private TextView tv_entry_time;
    private TextView tv_leave_time;
    private TextView tv_describe_work;
    private LinearLayout ll_popuwinddd;
    private LinearLayout linearLayout;
    private LinearLayout linearLayout_delete;
    private FrameLayout iv_back_basic;
    private Intent intent;
    String id;
    String params="";
    String api_token;
    String industry_name;
    String endtime_name;
    String starttime_name;
    String starttime;
    String endtime;
    private WorkExperience listWorkExpp;

    @Override
    public void initView() {
        intent = getIntent();
        params = intent.getStringExtra("params");
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        id = sharedPreferences.getString("resume_id", "");
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");

        tv_next_skip_work = (TextView) findViewById(R.id.tv_next_skip_work);
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
        ll_popuwinddd = (LinearLayout) findViewById(R.id.ll_popuwinddd);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout_delete = (LinearLayout) findViewById(R.id.linearLayout_delete);

        tv_input_company_name = (TextView) findViewById(R.id.tv_input_company_name);
        tv_input_work_names = (TextView) findViewById(R.id.tv_input_work_names);
        tv_select_natrue_business = (TextView) findViewById(R.id.tv_select_natrue_business);
        tv_select_company_scale = (TextView) findViewById(R.id.tv_select_company_scale);
        tv_suoshu_hangye = (TextView) findViewById(R.id.tv_suoshu_hangye);
        tv_entry_time = (TextView) findViewById(R.id.tv_entry_time);
        tv_leave_time = (TextView) findViewById(R.id.tv_leave_time);
        tv_describe_work = (TextView) findViewById(R.id.tv_describe_work);

        if (!params.isEmpty() && params.equals("experience")) {
            iv_back_basic.setVisibility(View.VISIBLE);
            tv_next_skip_work.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            tv_next_basic.setText("保存");
        } else if (params.equals("experienceItem")) {
            iv_back_basic.setVisibility(View.VISIBLE);
            tv_next_skip_work.setVisibility(View.INVISIBLE);

            linearLayout.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.GONE);
            tv_next_basic.setText("保存");
            linearLayout_delete.setVisibility(View.VISIBLE);

            Bundle bundle = intent.getExtras();
            listWorkExpp = (WorkExperience) bundle.getSerializable("experitem");

            company = listWorkExpp.getCompany();
            workName = listWorkExpp.getPost();
            natrueBusinessId = listWorkExpp.getComkind();
            companyScaleId = listWorkExpp.getScale();
            starttime = listWorkExpp.getStarttime();
            endtime = listWorkExpp.getEndtime();
            industry = listWorkExpp.getIndustry();
            describe = listWorkExpp.getContent();

            companyScale=listWorkExpp.getScale_name();
            natrueBusiness=listWorkExpp.getComkind_name();
            tv_input_company_name.setText(listWorkExpp.getCompany());
            tv_suoshu_hangye.setText(listWorkExpp.getIndustry_name());
            tv_select_natrue_business.setText(natrueBusiness);
            tv_select_company_scale.setText(companyScale);
            tv_entry_time.setText(listWorkExpp.getStarttime_name());
            tv_leave_time.setText(listWorkExpp.getEndtime_name());
            tv_input_work_names.setText(listWorkExpp.getPost());
            tv_describe_work.setText(listWorkExpp.getContent());

        }

        iv_back_basic.setOnClickListener(this);
        tv_next_skip_work.setOnClickListener(this);
        tv_next_basic.setOnClickListener(this);

        rl_company_name.setOnClickListener(this);
        rl_industry_involved.setOnClickListener(this);
        rl_natrue_business.setOnClickListener(this);
        rl_scale_company.setOnClickListener(this);

        rl_entry_time.setOnClickListener(this);
        rl_dimission.setOnClickListener(this);
        rl_work_name.setOnClickListener(this);
        rl_work_discriub.setOnClickListener(this);
        linearLayout_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_basic:
                finish();
                break;
            case R.id.tv_next_skip_work://跳转到下一个界面
                openResumeFragment();
                break;
            case R.id.tv_next_basic://完成
                nextView();
                break;
            case R.id.rl_company_name://公司名称
                if (params.equals("experienceItem")) {
                    Intent companyName = new Intent(WorkExpActivity.this, NameActivity.class);
                    companyName.putExtra("params", "companyName");
                    companyName.putExtra("mobile", listWorkExpp.getCompany());
                    startActivityForResult(companyName, 40);
                } else {
                    Intent companyName = new Intent(WorkExpActivity.this, NameActivity.class);
                    companyName.putExtra("params", "companyName");
                    companyName.putExtra("mobile", company);
                    startActivityForResult(companyName, 40);
                }
                break;
            case R.id.rl_industry_involved://所属行业
                if (params.equals("experienceItem")) {
                    Intent intentJob = new Intent(WorkExpActivity.this, EducationActivity.class);
                    intentJob.putExtra("education", "industry");
                    intentJob.putExtra("company_name", industryId);
                    startActivityForResult(intentJob, 62);
                } else if(params.equals("experience")){
                    Intent intentJob = new Intent(WorkExpActivity.this, EducationActivity.class);
                    intentJob.putExtra("education", "industry");
                    intentJob.putExtra("company_name", industryId);
                    startActivityForResult(intentJob, 62);
                } else{
                    Intent intentJob = new Intent(WorkExpActivity.this, EducationActivity.class);
                    intentJob.putExtra("education", "industry");
                    intentJob.putExtra("params", "experiencesss");
                    intentJob.putExtra("company_name", industryId);
                    startActivityForResult(intentJob, 62);
                }
                break;
            case R.id.rl_natrue_business://公司性质
                Intent intent = new Intent(WorkExpActivity.this, EducationActivity.class);
                intent.putExtra("education", "natrueBusiness");
                intent.putExtra("company_name", natrueBusiness);
                startActivityForResult(intent, 42);
                break;
            case R.id.rl_scale_company://公司规模
                Intent intentScale = new Intent(WorkExpActivity.this, EducationActivity.class);
                intentScale.putExtra("education", "companyScale");
//                if ("experience".equals(params)){
//                    intentScale.putExtra("company_name", listWorkExpp.getScale_name());
//                }else{
                    intentScale.putExtra("company_name", companyScale);
              //  }

                startActivityForResult(intentScale, 43);
                break;
            case R.id.rl_entry_time://入职时间
                initTimePicker("1");
                break;
            case R.id.rl_dimission://离职时间
                initTimePicker("2");
                break;
            case R.id.rl_work_name://职位名称
                if (params.equals("experienceItem")) {
                    Intent workNames = new Intent(WorkExpActivity.this, NameActivity.class);
                    workNames.putExtra("params", "workName");
                    workNames.putExtra("mobile", listWorkExpp.getPost());
                    startActivityForResult(workNames, 41);
                } else {

                    Intent workNames = new Intent(WorkExpActivity.this, NameActivity.class);
                    workNames.putExtra("params", "workName");
                    workNames.putExtra("mobile", workName);
                    startActivityForResult(workNames, 41);

                }
                break;
            case R.id.rl_work_discriub://工作描述
                Intent intentdescribe = new Intent(WorkExpActivity.this, DescribeActivity.class);
                intentdescribe.putExtra("params", "describework");
                intentdescribe.putExtra("introduction", describe);
                startActivityForResult(intentdescribe, 61);
                break;
            case R.id.linearLayout_delete:
                deleteWorkExperience();//删除工作经历
                break;

        }
    }

    int code;
    String strdelete;
    MyPopuwindown myPopustatee;

    /**
     * 删除工作经历
     */
    private void deleteWorkExperience() {

        StyledDialog.buildLoading().show();
        OkHttpClient okhhhtp = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        form.add("id", listWorkExpp.getId());
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_WORKEXP_DELETE)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(form.build())
                .build();
        okhhhtp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strdelete = response.body().string();
                code = response.code();
                System.out.println("aaaaaaaaaaaaa" + str);
                handler.sendEmptyMessage(100);
            }
        });
    }

    /**
     * 打开简历fragment
     */
    private void openResumeFragment() {
        if (params.equals("experience")) {
            Intent intent = new Intent(WorkExpActivity.this, MainActivity.class);
            intent.putExtra("register", "register");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(WorkExpActivity.this, MainActivity.class);
            intent.putExtra("register", "register");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    String natrueBusiness="";
    String natrueBusinessId;
    String companyScale="";
    String companyScaleId;
    String entry;
    String gradution;
    String describe;
    String industry = "";

    private void nextView() {
        if (params.equals("experienceItem")) {//更新工作经历
            updataWorkExperience();
        } else {

            if (company == null || "".equals(company)) {
                Toast.makeText(WorkExpActivity.this, "请输入公司的名称", Toast.LENGTH_SHORT).show();
                return;
            }

            if (industry == null || "".equals(industry)) {
                Toast.makeText(WorkExpActivity.this, "请选择所属行业", Toast.LENGTH_SHORT).show();
                return;
            }

            if (natrueBusiness == null || "".equals(natrueBusiness)) {
                Toast.makeText(WorkExpActivity.this, "请输入公司的性质", Toast.LENGTH_SHORT).show();
                return;
            }

            if (companyScale == null || "".equals(companyScale)) {
                Toast.makeText(WorkExpActivity.this, "请输入公司的规模", Toast.LENGTH_SHORT).show();
                return;
            }

            if (starttime == null || "".equals(starttime)) {
                Toast.makeText(WorkExpActivity.this, "请选择入职时间", Toast.LENGTH_SHORT).show();
                return;
            }

            if (endtime == null || "".equals(endtime)) {
                Toast.makeText(WorkExpActivity.this, "请选择离职时间", Toast.LENGTH_SHORT).show();
                return;
            }

            if (workName == null || "".equals(workName)) {
                Toast.makeText(WorkExpActivity.this, "请输入工作的名称", Toast.LENGTH_SHORT).show();
                return;
            }

            uploadingData();
        }
    }

    /**
     * 更新工作经历
     */
    private void updataWorkExperience() {
        StyledDialog.buildLoading().show();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", listWorkExpp.getId());
        formEncoding.add("company", company);
        formEncoding.add("industry", industry);
        formEncoding.add("comkind", natrueBusinessId);
        formEncoding.add("scale", companyScaleId);
        formEncoding.add("starttime", starttime);
        formEncoding.add("endtime", endtime);
        formEncoding.add("post", workName);
        formEncoding.add("content", describe);

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_WORKEXP_UPDATE)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(formEncoding.build())
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                if (e.getMessage() != null) {
                    handler.sendEmptyMessage(1200);
                }

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strdelete = response.body().string();
                code = response.code();
                System.out.println("4444444444411111ssssss" + strdelete);
                handler.sendEmptyMessage(100);
            }
        });

    }

    private String str;

    private void uploadingData() {
        StyledDialog.buildLoading().show();

        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("resume_id", id);
        formEncoding.add("company", company);
        formEncoding.add("industry", industry);

        formEncoding.add("comkind", natrueBusinessId);
        formEncoding.add("scale", companyScaleId);
        formEncoding.add("starttime", starttime);
        formEncoding.add("endtime", endtime);
        formEncoding.add("post", workName);

        if (describe == null || "".equals(describe)) {
            formEncoding.add("content", "");
        } else {
            formEncoding.add("content", describe);
        }

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_WORKEXP)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(formEncoding.build())
                .build();

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
                System.out.println("4444444444411111aaaaaaaaa" + str);
                handler.sendEmptyMessage(1000);
            }
        });

    }

    /**
     * 开启一个activity返回来的数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private String company="";
    private String workName="";
    String industryId="";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
        } else {
            switch (requestCode) {
                case 40:

                    company = data.getExtras().getString("companyName");
                    tv_input_company_name.setText(company);
                    SharedPreferences share = getSharedPreferences("data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = share.edit();
                    editor.putString("company", company);
                    editor.commit();
                    break;
                case 41:
                    workName = data.getExtras().getString("workName");
                    tv_input_work_names.setText(workName);
                    SharedPreferences sharee = getSharedPreferences("data", MODE_PRIVATE);
                    SharedPreferences.Editor editorr = sharee.edit();
                    editorr.putString("workName", workName);
                    editorr.commit();
                    break;
                case 42:

                    natrueBusiness = data.getExtras().getString("natrueBusiness");
                    natrueBusinessId = data.getExtras().getString("natrueBusinessId");
                    tv_select_natrue_business.setText(natrueBusiness);

                    break;
                case 43:
                    companyScale = data.getExtras().getString("companyScale");
                    companyScaleId = data.getExtras().getString("companyScaleId");
                    tv_select_company_scale.setText(companyScale);
                    break;
                case 62:
                    if (params.equals("experienceItem")) {

                        industry = data.getExtras().getString("listId");
                         industryId = data.getExtras().getString("list");
                        tv_suoshu_hangye.setText(industryId);

                    } else if(params.equals("experience")){

                        industry = data.getExtras().getString("listId");
                        industryId = data.getExtras().getString("list");
                        tv_suoshu_hangye.setText(industryId);

                    } else{
                        List<String> list = (List<String>) data.getExtras().getSerializable("list");
                        List<String> listId = (List<String>) data.getExtras().getSerializable("listId");
                        String industryid = "";
                        industry = "";
                        for (int i = 0; i < list.size(); i++) {
                            if (list.size()==1||i==list.size()-1) {
                                industryid += list.get(i);
                                industry += listId.get(i);
                            } else {
                                industryid += list.get(i) + ",";
                                industry += listId.get(i) + ",";
                            }

                        }
                        Log.i("industry",industry+"");
                        Log.i("industryid",industryid+"");
                        tv_suoshu_hangye.setText(industryid);
                    }
                    break;
                case 61:

                    describe = data.getExtras().getString("describe");
                    tv_describe_work.setText(describe);

                    break;
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    JSONObject json = JSON.parseObject(str);
                    String codes = json.getString("code");
                    String message = json.getString("msg");
                    StyledDialog.dismissLoading();
                    if ("1".equals(codes)) {
                        openResumeFragment();
                    }else if("0".equals(codes)){
                        Toast.makeText(WorkExpActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 100:
                    if (code == 200) {
                        JSONObject jsons = JSON.parseObject(strdelete);
                        String msgg = jsons.getString("msg");
                        String code=jsons.getString("code");
                        if ("1".equals(code)){
                            Intent intent = new Intent(WorkExpActivity.this, EditResumeTwoActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(WorkExpActivity.this, msgg, Toast.LENGTH_SHORT).show();
                        }


                    } else {
                      //  Toast.makeText(WorkExpActivity.this, "删除工作经历失败", Toast.LENGTH_SHORT).show();
                    }
                    StyledDialog.dismissLoading();
                    break;
                case 1200:
//                    myPopustatee.dismiss();
                    StyledDialog.dismissLoading();
                    Toast.makeText(WorkExpActivity.this, "保存数据失败,请重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    TimePickerView pvTime;

    private void initTimePicker(final String flags) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 1, 1);
        pvTime = new TimePickerView.Builder(WorkExpActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String time = UiUtils.getTime(date.toString());
                if (flags.equals("1")) {
                    tv_entry_time.setText(time);
                    starttime = time + "-01";
                } else if (flags.equals("2")) {
                    tv_leave_time.setText(time);
                    endtime = time + "-01";
                }
            }
        }).setType(TimePickerView.Type.YEAR_MONTH)
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent=new Intent(WorkExpActivity.this,MainActivity.class);
//        intent.putExtra("register","register");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }
}
