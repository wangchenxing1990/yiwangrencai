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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.hss01248.dialog.StyledDialog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.LoginOffPopuWindow;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/23.
 */

public class EditSearchEnginedActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_add_searcher;
    }

    String id;
    String api_token;
    String str;
    int code;

    @Override
    public void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formEncod = new FormEncodingBuilder();
        formEncod.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.SEARCH_ENGINE_EDIT)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncod.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("编辑搜索器的数据", str);
                handler.sendEmptyMessage(100);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    parsEditSearcherData();//解析获取到的数据
                    break;
                case 120:
                    if (code == 200) {
                        JSONObject json = JSON.parseObject(strs);
                        String codes = json.getString("code");
                        if ("1".equals(codes)) {
                            //Toast.makeText(EditSearchEnginedActivity.this, json.getString("msg"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditSearchEnginedActivity.this, SearchEnginedActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(EditSearchEnginedActivity.this, json.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditSearchEnginedActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                    StyledDialog.dismissLoading();
                    break;
                case 130:
                    if (code == 200) {
                        JSONObject json = JSON.parseObject(strss);
                        String codes = json.getString("code");
                        if ("1".equals(codes)) {
                            Toast.makeText(EditSearchEnginedActivity.this, json.getString("msg"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditSearchEnginedActivity.this, SearchEnginedActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(EditSearchEnginedActivity.this, json.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditSearchEnginedActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                    StyledDialog.dismissLoading();
                    break;
            }
        }
    };

    /**
     * 解析获取到的数据
     */
    String sex="";
    String age="";
    String education="";
    String work_year="";
    String keyword="";
    String keyword_type="";
    String release_date="";
    String search_name="";
    String jobsort_name="";
    String jobarea_name="";
    String industry_name="";
    String education_name="";
    String work_year_name="";
    String release_date_name="";
    JSONArray jobsort;
    JSONArray jobarea;
    JSONArray industry;
    String industryId = "";
    String jobsortId = "";
    String jobareaId = "";

    private void parsEditSearcherData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            if ("1".equals(codes)) {
                JSONObject jsonData = json.getJSONObject("data");
                sex = jsonData.getString("sex");
                age = jsonData.getString("age");
                education = jsonData.getString("education");
                work_year = jsonData.getString("work_year");
                keyword = jsonData.getString("keyword");
                keyword_type = jsonData.getString("keyword_type");
                release_date = jsonData.getString("release_date");
                search_name = jsonData.getString("search_name");
                jobsort_name = jsonData.getString("jobsort_name");
                jobsort = jsonData.getJSONArray("jobsort");
                jobarea_name = jsonData.getString("jobarea_name");
                jobarea = jsonData.getJSONArray("jobarea");
                industry_name = jsonData.getString("industry_name");
                industry = jsonData.getJSONArray("industry");
                education_name = jsonData.getString("education_name");
                work_year_name = jsonData.getString("work_year_name");
                release_date_name = jsonData.getString("release_date_name");

                String[] industryName = industry_name.split(",");
                for (int i = 0; i < industry.size(); i++) {
                    listts.add(industryName[i]);
                    listIds.add(industry.get(i) + "");
                }
                for (int i = 0; i < industry.size(); i++) {
                    if (industry.size() == 1 || i == industry.size() - 1) {
                        industryId += industry.get(i);
                    } else {
                        industryId += industry.get(i) + ",";
                    }
                }

                String[] jobSortName = jobsort_name.split(",");
                if (jobsort.size() != 0) {
                    for (int i = 0; i < jobSortName.length; i++) {
                        listItem.add(jobSortName[i]);
                        cid.add(jobsort.get(i) + "");
                    }
                }


                for (int i = 0; i < jobsort.size(); i++) {
                    if (industry.size() == 1 || i == industry.size() - 1) {
                        jobsortId += jobsort.get(i);
                    } else {
                        jobsortId += jobsort.get(i) + ",";
                    }
                }

                if (keyword == null || keyword.isEmpty()) {
                    textkeyword.setText("请输入关键字");
                } else {
                    textkeyword.setText(keyword);
                }

                String[] jobAreaName = jobarea_name.split(",");
                if (jobarea.size() != 0) {
                    for (int i = 0; i < jobAreaName.length; i++) {
                        jobareaList.add(jobAreaName[i]);
                        jobareaListId.add(jobarea.get(i) + "");
                    }
                }

                for (int i = 0; i < jobarea.size(); i++) {
                    if (industry.size() == 1 || i == industry.size() - 1) {
                        jobareaId += jobarea.get(i);
                    } else {
                        jobareaId += jobarea.get(i) + ",";
                    }
                }

                if (jobarea_name != null && !jobarea_name.isEmpty()) {
                    textSelectArea.setText(jobarea_name);
                }
                if (jobsort_name != null && !jobsort_name.isEmpty()) {
                    textWorkPost.setText(jobsort_name);
                }
                if (industry_name != null && !industry_name.isEmpty()) {
                    textIndustryType.setText(industry_name);
                }

                textEducation.setText(education_name);
                textExp.setText(work_year_name);
                if (release_date_name!=null&&!release_date_name.isEmpty()){
                    texttime.setText(release_date_name);
                }

                textsearcherName.setText(search_name);

                if ("0".equals(sex)) {
                    textSex.setText("性别不限");
                } else if ("1".equals(sex)) {
                    textSex.setText("男");
                } else if ("2".equals(sex)) {
                    textSex.setText("女");
                }

                if ("0".equals(age)) {
                    textage.setText("年龄不限");
                } else {
                    textage.setText(age);
                }

                if ("1".equals(keyword_type)) {
                    textkeywordtype.setText("全文");
                } else if ("2".equals(keyword_type)) {
                    textkeywordtype.setText("职位");
                } else if ("3".equals(keyword_type)) {
                    textkeywordtype.setText("公司");
                }

            } else {

            }
        } else {

        }
    }

    private FrameLayout fram_job_back;
    private TextView textSelectArea;
    private TextView textWorkPost;
    private TextView textIndustryType;
    private TextView textSex;
    private TextView textEducation;
    private TextView textExp;
    private TextView textkeyword;
    private TextView textkeywordtype;
    private TextView texttime;
    private TextView textsearcherName;
    private TextView textage;

    @Override
    public void initView() {
        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        TextView tv_searech = (TextView) findViewById(R.id.tv_searech);
        TextView tv_add_save = (TextView) findViewById(R.id.tv_add_save);
        LinearLayout linear_delete = (LinearLayout) findViewById(R.id.linear_delete);
        RelativeLayout relativeWorkArea = (RelativeLayout) findViewById(R.id.relativeWorkArea);
        RelativeLayout relativeWorkPost = (RelativeLayout) findViewById(R.id.relativeWorkPost);
        RelativeLayout relativeIndustryCatgory = (RelativeLayout) findViewById(R.id.relativeIndustryCatgory);
        RelativeLayout relativeSex = (RelativeLayout) findViewById(R.id.relativeSex);
        RelativeLayout relativeEducation = (RelativeLayout) findViewById(R.id.relativeEducation);
        RelativeLayout relativeWorkExp = (RelativeLayout) findViewById(R.id.relativeWorkExp);
        RelativeLayout relative_keyWord = (RelativeLayout) findViewById(R.id.relative_keyWord);
        RelativeLayout relative_keywork_type = (RelativeLayout) findViewById(R.id.relative_keywork_type);
        RelativeLayout relativeTime = (RelativeLayout) findViewById(R.id.relativeTime);
        RelativeLayout ralativeSearcherName = (RelativeLayout) findViewById(R.id.ralativeSearcherName);
        RelativeLayout relativeage = (RelativeLayout) findViewById(R.id.relativeage);

        textSelectArea = (TextView) findViewById(R.id.textSelectArea);
        textWorkPost = (TextView) findViewById(R.id.textWorkPost);
        textIndustryType = (TextView) findViewById(R.id.textIndustryType);
        textSex = (TextView) findViewById(R.id.textSex);
        textEducation = (TextView) findViewById(R.id.textEducation);
        textExp = (TextView) findViewById(R.id.textExp);
        textkeyword = (TextView) findViewById(R.id.textkeyword);
        textkeywordtype = (TextView) findViewById(R.id.textkeywordtype);
        texttime = (TextView) findViewById(R.id.texttime);
        textsearcherName = (TextView) findViewById(R.id.textsearcherName);
        textage = (TextView) findViewById(R.id.textage);

        fram_job_back.setOnClickListener(this);
        tv_searech.setOnClickListener(this);
        tv_add_save.setOnClickListener(this);

        relativeWorkArea.setOnClickListener(this);
        relativeWorkPost.setOnClickListener(this);
        relativeIndustryCatgory.setOnClickListener(this);
        relativeSex.setOnClickListener(this);
        relativeage.setOnClickListener(this);
        relativeWorkExp.setOnClickListener(this);
        relativeEducation.setOnClickListener(this);
        relative_keyWord.setOnClickListener(this);
        relative_keywork_type.setOnClickListener(this);
        relativeTime.setOnClickListener(this);
        ralativeSearcherName.setOnClickListener(this);
        linear_delete.setOnClickListener(this);
    }

    private List<String> listItem = new ArrayList<>();
    private List<String> cid = new ArrayList<>();
    private List<String> listts = new ArrayList<>();
    private List<String> listIds = new ArrayList<>();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back://返回上一页
                finish();
                break;
            case R.id.tv_searech://搜索按钮

                Intent intent = new Intent(EditSearchEnginedActivity.this, SearchResultActivity.class);
                intent.putExtra("keyword", keyword);
                intent.putExtra("sex", sex);
                intent.putExtra("jobarea", jobareaId);
                intent.putExtra("industry", industryId);
                intent.putExtra("jobsort", jobsortId);
                intent.putExtra("education", education);
                intent.putExtra("work_year", work_year);
                intent.putExtra("keyword_type", keyword_type);
                intent.putExtra("age", age);
                intent.putExtra("release_dateId", release_date);
                intent.putExtra("search_name", search_name);
                intent.putExtra("params", "highSearch");
                startActivity(intent);
                break;
            case R.id.tv_add_save://保存按钮
                submitDataToService();//向服务器提交数据
                break;
            case R.id.relativeWorkArea://工作地区
                Intent intentArer = new Intent(EditSearchEnginedActivity.this, LocationActivity.class);
                intentArer.putExtra("params", "");
                intentArer.putExtra("paramss", "");
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("jobareaaName", (ArrayList<String>) jobareaList);
                bundle.putStringArrayList("jobareaaId", (ArrayList<String>) jobareaListId);
                intentArer.putExtras(bundle);
                startActivityForResult(intentArer, 9);
                break;
            case R.id.relativeWorkPost://工作岗位
                Intent intentPost = new Intent(EditSearchEnginedActivity.this, AllWorkActivity.class);
                Bundle bundlePost = new Bundle();
                bundlePost.putStringArrayList("job_category", (ArrayList<String>) listItem);
                bundlePost.putStringArrayList("job_categoryId", (ArrayList<String>) cid);
                intentPost.putExtras(bundlePost);
                startActivityForResult(intentPost, 50);
                break;
            case R.id.relativeIndustryCatgory://行业类别
                Intent intentIndustry = new Intent(EditSearchEnginedActivity.this, EducationActivity.class);
                intentIndustry.putExtra("education", "industry_more");
                intentIndustry.putExtra("company_name", "");
                Bundle bundleIndustry = new Bundle();
                bundleIndustry.putStringArrayList("list", (ArrayList<String>) listts);
                bundleIndustry.putStringArrayList("listId", (ArrayList<String>) listIds);
                intentIndustry.putExtras(bundleIndustry);
                startActivityForResult(intentIndustry, 62);
                break;
            case R.id.relativeSex://性别
                initTimePicker();
                break;
            case R.id.relativeEducation://学历
                Intent intentEducation = new Intent(EditSearchEnginedActivity.this, EducationActivity.class);
                intentEducation.putExtra("education", "education");
                intentEducation.putExtra("company_name", education_name);
                startActivityForResult(intentEducation, 15);
                break;
            case R.id.relativeage:
                Intent intentAge = new Intent(EditSearchEnginedActivity.this, EducationActivity.class);
                intentAge.putExtra("education", "age");
                intentAge.putExtra("company_name", age);
                startActivityForResult(intentAge, 19);
                break;
            case R.id.relativeWorkExp://工作经验
                Intent intentExp = new Intent(EditSearchEnginedActivity.this, EducationActivity.class);
                intentExp.putExtra("education", "suffer");
                intentExp.putExtra("company_name", work_year_name);
                startActivityForResult(intentExp, 16);

                break;
            case R.id.relative_keyWord://关键字
                Intent intentKw = new Intent(EditSearchEnginedActivity.this, NameActivity.class);
                intentKw.putExtra("params", "kw");
                intentKw.putExtra("mobile", keyword);
                startActivityForResult(intentKw, 17);
                break;
            case R.id.relative_keywork_type://关键字类型
                initKeyWord();
                break;
            case R.id.relativeTime://发布日期
                Intent intentTime = new Intent(EditSearchEnginedActivity.this, EducationActivity.class);
                intentTime.putExtra("education", "time");
                intentTime.putExtra("company_name", release_date_name);
                startActivityForResult(intentTime, 00);
                break;
            case R.id.ralativeSearcherName://搜索器名
                Intent intentSearch = new Intent(EditSearchEnginedActivity.this, NameActivity.class);
                intentSearch.putExtra("params", "searchername");
                intentSearch.putExtra("mobile", search_name);
                startActivityForResult(intentSearch, 18);
                break;

            case R.id.linear_delete://删除
                showSureDeletePopuwind();//显示是否确认删除
                break;
            case R.id.tv_off_cancel:
                loginOffWindow.dismiss();
                break;
            case R.id.tv_off_sure:
                loginOffWindow.dismiss();
                deteleSearcher();//删除搜索器
                break;
        }
    }


    /**
     * 向服务器提交数据
     */
    String strss;

    private void submitDataToService() {
        StyledDialog.buildLoading().show();
        Log.i("jobareaIdjobareaIdjobareaId", jobareaId);
        Log.i("jobsortIdjobsortIdjobsortId", jobsortId);
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);
        formEncoding.add("jobarea", jobareaId);
        formEncoding.add("jobsort", jobsortId);
        formEncoding.add("industry", industryId);
        formEncoding.add("sex", sex);
        formEncoding.add("education", education);
        formEncoding.add("age", age);
        formEncoding.add("work_year", work_year);
        formEncoding.add("release_date", release_date);
        formEncoding.add("keyword", keyword);
        formEncoding.add("keyword_type", keyword_type);
        formEncoding.add("search_name", search_name);

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.SEARCH_ENGINE_UPDATE)
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
                strss = response.body().string();
                code = response.code();
                Log.i("更新搜索器数据", strss);
                handler.sendEmptyMessage(130);
            }
        });
    }

    private List<String> jobareaList = new ArrayList();
    private List<String> jobareaListId = new ArrayList();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            switch (requestCode) {
                case 9://工作地区
                    jobareaList = (List<String>) data.getExtras().getSerializable("listArea");
                    jobareaListId = (List<String>) data.getExtras().getSerializable("listAreaId");
                    jobarea_name = "";
                    jobareaId = "";
                    for (int i = 0; i < jobareaList.size(); i++) {

                        if (jobareaList.size() == 0 || i == jobareaList.size() - 1) {
                            jobarea_name += jobareaList.get(i);
                            jobareaId += jobareaListId.get(i);
                        } else {
                            jobareaId += jobareaListId.get(i) + ",";
                            jobarea_name += jobareaList.get(i) + ",";
                        }
                    }

                    textSelectArea.setText(jobarea_name);

                    break;
                case 50://工作岗位
                    listItem = (List<String>) data.getExtras().getSerializable("listItem");
                    cid = (List<String>) data.getExtras().getSerializable("cid");
                    jobsort_name = "";
                    jobsortId = "";
                    for (int i = 0; i < listItem.size(); i++) {
                        if (listItem.size() == 1 || i == listItem.size() - 1) {
                            jobsort_name += listItem.get(i);
                            jobsortId += cid.get(i);
                        } else {
                            jobsortId += cid.get(i) + ",";
                            jobsort_name += listItem.get(i) + ",";
                        }

                    }
                    textWorkPost.setText(jobsort_name);

                    break;
                case 62://行业类别
                    listts = (List<String>) data.getExtras().getSerializable("list");
                    listIds = (List<String>) data.getExtras().getSerializable("listId");
                    industry_name = "";
                    industryId = "";
                    for (int i = 0; i < listts.size(); i++) {
                        //industry_name+=listIndustry.get(i)+",";
                        if (listts.size() == 1 || i == listts.size() - 1) {
                            industryId += listIds.get(i);
                            industry_name += listts.get(i);
                        } else {
                            industryId += listIds.get(i) + ",";
                            industry_name += listts.get(i) + ",";
                        }

                    }
                    textIndustryType.setText(industry_name);
                    break;
                case 15://学历
                    education_name = data.getExtras().getString("education");
                    education = data.getExtras().getString("educationId");
                    textEducation.setText(education_name);
                    break;
                case 16://工作经验
                    work_year = data.getExtras().getString("sufferId");
                    work_year_name = data.getExtras().getString("suffer");
                    textExp.setText(work_year_name);
                    break;
                case 17:
                    keyword = data.getExtras().getString("kw");
                    textkeyword.setText(keyword);
                    break;
                case 00:
                    release_date = data.getExtras().getString("issuedataId");
                    release_date_name = data.getExtras().getString("issuedata");
                    texttime.setText(release_date_name);
                    break;
                case 18:
                    search_name = data.getExtras().getString("searchname");
                    textsearcherName.setText(search_name);
                    break;
                case 19:
                    age = data.getExtras().getString("ageDatas");
                    if ("0".equals(age)) {
                        textage.setText("年龄不限");
                    } else {
                        textage.setText(age);
                    }

                    break;
            }
        }
    }

    /**
     * 显示是否确认删除
     */
    private LoginOffPopuWindow loginOffWindow;

    private void showSureDeletePopuwind() {
        loginOffWindow = new LoginOffPopuWindow(EditSearchEnginedActivity.this, R.layout.login_off_popuwindown);
        View view = loginOffWindow.getView();
        loginOffWindow.showAtLocation(EditSearchEnginedActivity.this.findViewById(R.id.ll_popuwind), Gravity.CENTER, 0, 0);
        TextView tv_off_cancel = (TextView) view.findViewById(R.id.tv_off_cancel);
        TextView tv_off_sure = (TextView) view.findViewById(R.id.tv_off_sure);
        TextView textview = (TextView) view.findViewById(R.id.textview);
        textview.setText("确认删除");
        tv_off_cancel.setOnClickListener(this);
        tv_off_sure.setOnClickListener(this);

    }

    /**
     * 删除搜索器
     */
    String strs;
    MyPopuwindown myPopuwind;

    private void deteleSearcher() {
        StyledDialog.buildLoading().show();
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        form.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.SEARCH_ENGINE_DELETE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(form.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strs = response.body().string();
                code = response.code();
                Log.i("删除搜索器", strs);
                handler.sendEmptyMessage(120);
            }
        });
    }

    OptionsPickerView pvOptions;

    private void initTimePicker() {
        final List<String> options1Items = new ArrayList<>();
        options1Items.add("保密");
        options1Items.add("男");
        options1Items.add("女");
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);
                if (options1 == 0) {
                    sex = "0";
                } else if (options1 == 1) {
                    sex = "1";
                } else if (options1 == 2) {
                    sex = "2";
                }

                textSex.setText(tx);
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("性别选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFffffff)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
                //  .setLabels("省", "市", "区")//设置选择的三级单位
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .build();

        pvOptions.setPicker(options1Items);//添加数据源
        pvOptions.show();
    }

    OptionsPickerView keyWordInit;

    private void initKeyWord() {
        final List<String> options1Items = new ArrayList<>();
        options1Items.add("全文");
        options1Items.add("职位");
        options1Items.add("公司");
        keyWordInit = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);
                if (options1 == 0) {
                    keyword_type = "1";
                } else if (options1 == 1) {
                    keyword_type = "2";
                } else if (options1 == 2) {
                    keyword_type = "3";
                }

                textkeywordtype.setText(tx);
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("关键字选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFffffff)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
                //  .setLabels("省", "市", "区")//设置选择的三级单位
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(Integer.parseInt(keyword_type) - 1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .build();

        keyWordInit.setPicker(options1Items);//添加数据源
        keyWordInit.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }
}
