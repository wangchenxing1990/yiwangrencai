package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.hss01248.dialog.StyledDialog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.MyDiscussPopuwind;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class IntensionActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.job_intension_activity;
    }

    @Override
    public void initData() {

    }

    private TextView tv_next_intension;
    private RelativeLayout rl_salary_expection;
    private RelativeLayout rl_discuss_personally;
    private RelativeLayout rl_intension_job;
    private RelativeLayout rl_work_area;
    private RelativeLayout rl_place_job;
    private TextView tv_select_saraly;
    private TextView tv_select_work;
    private TextView tv_select_area_work;
    private TextView tv_view_disscuess;
    private TextView textViewIntentsion_work;
    private String id;
    private XRefreshView flowlayout;

    @Override
    public void initView() {


        tv_next_intension = (TextView) findViewById(R.id.tv_next_intension);
        rl_salary_expection = (RelativeLayout) findViewById(R.id.rl_salary_expection);
        rl_discuss_personally = (RelativeLayout) findViewById(R.id.rl_discuss_personally);
        rl_place_job = (RelativeLayout) findViewById(R.id.rl_place_job);
        rl_intension_job = (RelativeLayout) findViewById(R.id.rl_intension_job);
        rl_work_area = (RelativeLayout) findViewById(R.id.rl_work_area);

        tv_select_saraly = (TextView) findViewById(R.id.tv_select_saraly);
        tv_select_work = (TextView) findViewById(R.id.tv_select_work);
        tv_select_area_work = (TextView) findViewById(R.id.tv_select_area_work);
        tv_view_disscuess = (TextView) findViewById(R.id.tv_view_disscuess);
        textViewIntentsion_work = (TextView) findViewById(R.id.textViewIntentsion_work);

        tv_next_intension.setOnClickListener(this);
        rl_salary_expection.setOnClickListener(this);
        rl_discuss_personally.setOnClickListener(this);
        rl_place_job.setOnClickListener(this);
        rl_intension_job.setOnClickListener(this);
        rl_work_area.setOnClickListener(this);
        initChildViews();
    }

    private void initChildViews() {
        // TODO Auto-generated method stub


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next_intension://点击下一步进入下一个界面
                nextView();
                break;
            case R.id.rl_salary_expection://薪资期望
                Intent intentt = new Intent(IntensionActivity.this, EducationActivity.class);
                intentt.putExtra("education", "intensionsaraly");
                intentt.putExtra("company_name", saraly);
                startActivityForResult(intentt, 20);
                break;
            case R.id.rl_discuss_personally://显示面议

                showViewPopuwind();
                break;
            case R.id.rl_place_job://工作岗位
                Intent intentJob = new Intent(IntensionActivity.this, AllWorkActivity.class);
                //intentJob.putExtra("params", "intension");
                Bundle bundle=new Bundle();
                bundle.putStringArrayList("job_category", (ArrayList<String>) listJob);
                bundle.putStringArrayList("job_categoryId", (ArrayList<String>) listJobId);
                intentJob.putExtras(bundle);
                startActivityForResult(intentJob, 50);
                break;
            case R.id.rl_intension_job://意向工作岗位
                Intent intentIntensionJob = new Intent(IntensionActivity.this, NameActivity.class);
                intentIntensionJob.putExtra("params", "intension");
                intentIntensionJob.putExtra("mobile", work);
                startActivityForResult(intentIntensionJob, 21);
                break;
            case R.id.rl_work_area://工作区域
                Intent locationIntent = new Intent(IntensionActivity.this, LocationActivity.class);
                locationIntent.putExtra("params", "");
//                locationIntent.putExtra("jobareaaId", listAreaId);
//                locationIntent.putExtra("jobareaaName", listArea);
                Bundle bundleName=new Bundle();
                bundleName.putStringArrayList("jobareaaId", (ArrayList<String>) listAreaId);
                bundleName.putStringArrayList("jobareaaName", (ArrayList<String>) listArea);
                locationIntent.putExtras(bundleName);
                locationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(locationIntent, 9);
                break;
        }
    }

    private void saveDiscuss() {
        SharedPreferences shares = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = shares.edit();
        if (flag == 1) {
            editor.putString("discuss", "1");
            tv_view_disscuess.setText("显示面议");
        } else if (flag == 2) {
            editor.putString("discuss", "0");
            tv_view_disscuess.setText("不显示面议");
        }

        editor.commit();

    }


    private int flag;

    /**
     * 显示是否面议
     */
    private void showViewPopuwind() {
        final List<String> cardState = new ArrayList<>();
        cardState.add("不显示面议");
        cardState.add("显示面议");

        OptionsPickerView pvOptionWork = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String tx = cardState.get(options1);
                tv_view_disscuess.setText(tx);
                discuss = options1 + "";

                System.out.print("asdonosanfmoasmalk" + tx);

            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("显示面议")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFffffff)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvOptionWork.show();
        pvOptionWork.setPicker(cardState);//添加数据源

    }

    String saraly="";
    String work="";
    String areaLocation = "";
    String jobsort;
    String jobarea;
    String discuss = "1";

    private void nextView() {

        if (saralyId == null || "".equals(saralyId)) {
            Toast.makeText(IntensionActivity.this, "请输入期望薪资", Toast.LENGTH_SHORT).show();
            return;
        }

        if (discuss == null || "".equals(discuss)) {
            Toast.makeText(IntensionActivity.this, "请选择是否面议", Toast.LENGTH_SHORT).show();
            return;
        }

        if (work == null || "".equals(work)) {
            Toast.makeText(IntensionActivity.this, "请输入意向工作岗位", Toast.LENGTH_SHORT).show();
            return;
        }

        if (jobsort == null || "".equals(jobsort)) {
            Toast.makeText(IntensionActivity.this, "请选择工作岗位", Toast.LENGTH_SHORT).show();
            return;
        }

        if (jobarea == null || "".equals(jobarea)) {
            Toast.makeText(IntensionActivity.this, "请选择工作地区", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadingData();
    }

    private String str;
    private MyPopuwindown myPopuwindown;

    private void uploadingData() {
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        String api_token = share.getString("api_token", "");
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        id = sharedPreferences.getString("resume_id", "");

        StyledDialog.buildLoading().show();

        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("expectedsalary", saralyId);
        formEncoding.add("id", id);
        formEncoding.add("isexpectedsalary", discuss);
        formEncoding.add("intentionjobs", work);
        formEncoding.add("jobsort", jobsort);
        formEncoding.add("jobarea", jobarea);

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_INTENT)
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
                System.out.println("11111111222222" + str);
                handler.sendEmptyMessage(1000);
            }
        });
    }

    String text = "";
    String textLocation;
    List<String> listArea=new ArrayList<>();
    String saralyId;
    List<String> listJob=new ArrayList<>();
    List<String> listJobId=new ArrayList<>();
    List<String> listAreaId=new ArrayList<>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
        } else {
            switch (requestCode) {
                case 20:
                    saraly = data.getExtras().getString("intensionsaraly");
                    saralyId = data.getExtras().getString("intensionsaralyId");
                    tv_select_saraly.setText(saraly);

                    break;
                case 21:

                    work = data.getExtras().getString("intension");
                    Log.i("work", work);
                    textViewIntentsion_work.setText(work);

                    break;
                case 9:

                    listArea = (List<String>) data.getExtras().getSerializable("listArea");
                    listAreaId = (List<String>) data.getExtras().getSerializable("listAreaId");
                    areaLocation = "";
                    jobarea = "";
                    for (int i = 0; i < listArea.size(); i++) {
                        Log.i("aaaaaa", listArea.get(i));
                        if (i < listArea.size()) {
                            areaLocation += listArea.get(i) + ",";
                            jobarea += listAreaId.get(i) + ",";
                        } else {
                            areaLocation += listArea.get(i);
                            jobarea += listAreaId.get(i);
                        }
                    }

                    tv_select_area_work.setText(areaLocation);

                    break;
                case 50://工作岗位
                   listJob = (List<String>) data.getExtras().getSerializable("listItem");
                     listJobId = (List<String>) data.getExtras().getSerializable("cid");
                    text = "";
                    jobsort = "";
                    for (int i = 0; i < listJob.size(); i++) {
                        if (listJob.size() == 1 || i == listJob.size()-1) {
                            text += listJob.get(i);
                            jobsort += listJobId.get(i);
                        } else {
                            text += listJob.get(i) + ",";
                            jobsort += listJobId.get(i) + ",";
                        }

                    }
                    tv_select_work.setText(text);
                    break;

            }
        }
    }

    private int code;
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
                            Intent intent = new Intent(IntensionActivity.this, EduBackGroundActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            StyledDialog.dismissLoading();
                            Toast.makeText(IntensionActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        StyledDialog.dismissLoading();
                        Toast.makeText(IntensionActivity.this, "联网超时,请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 1200:

                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(IntensionActivity.this,MainActivity.class);
        intent.putExtra("register","register");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
