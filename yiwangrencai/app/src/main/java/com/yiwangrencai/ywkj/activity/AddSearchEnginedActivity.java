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
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */

public class AddSearchEnginedActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_add_searcher;
    }

    @Override
    public void initData() {

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
    String jobarea = "";
    String jobareaId = "";
    String industry = "";
    String industryId = "";
    String jobsort = "";
    String cids = "";
    String sex = "";
    String education = "";
    String educationId = "";
    String word_year = "";
    String suffer = "";
    String key_word = "";
    String keyword_type = "1";
    String release_date = "";
    String issuedata = "";
    String search_name = "";
    String ageDatas = "";
    String ageDatasId = "";

    @Override
    public void initView() {
        Intent intent = getIntent();
        String params = intent.getStringExtra("params");

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

        if (params.equals("save")) {
            tv_searech.setVisibility(View.INVISIBLE);
            linear_delete.setVisibility(View.INVISIBLE);
        } else if (params.equals("item")) {
            tv_searech.setVisibility(View.VISIBLE);
            linear_delete.setVisibility(View.VISIBLE);
        }

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

    }

    private List<String> jobareaList=new ArrayList();
    private List<String> jobareaListId=new ArrayList();
    private List<String> listts=new ArrayList<String>();
    private List<String> listIds=new ArrayList<String>();
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back://返回按钮
                finish();
                break;
            case R.id.tv_searech://搜索按钮
                Intent intent = new Intent(AddSearchEnginedActivity.this, SearchResultActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_add_save://保存按钮
                submitDataToService();//向服务器提交数据
                break;
            case R.id.relativeWorkArea://工作地区
                Intent intentArer = new Intent(AddSearchEnginedActivity.this, LocationActivity.class);
                intentArer.putExtra("paramss", "areas");
                intentArer.putExtra("params", "");
                Bundle bundle=new Bundle();
                bundle.putStringArrayList("jobareaaName", (ArrayList<String>) jobareaList);
                bundle.putStringArrayList("jobareaaId", (ArrayList<String>) jobareaListId);
                intentArer.putExtras(bundle);
                startActivityForResult(intentArer, 9);
                break;
            case R.id.relativeWorkPost://工作岗位
                Intent intentPost = new Intent(AddSearchEnginedActivity.this, AllWorkActivity.class);
                Bundle bundlePost=new Bundle();
                bundlePost.putStringArrayList("job_category", (ArrayList<String>) listItem);
                bundlePost.putStringArrayList("job_categoryId", (ArrayList<String>) cid);
                intentPost.putExtras(bundlePost);
                startActivityForResult(intentPost, 50);
                break;
            case R.id.relativeIndustryCatgory://行业类别
                Intent intentIndustry = new Intent(AddSearchEnginedActivity.this, EducationActivity.class);
                intentIndustry.putExtra("education", "industry_more");
                intentIndustry.putExtra("company_name", "");
                Bundle bundleIndustry=new Bundle();
                bundleIndustry.putStringArrayList("list", (ArrayList<String>) listts);
                bundleIndustry.putStringArrayList("listId", (ArrayList<String>) listIds);
                intentIndustry.putExtras(bundleIndustry);
                startActivityForResult(intentIndustry, 62);
                break;
            case R.id.relativeSex://性别
                initTimePicker();
                break;
            case R.id.relativeEducation://学历
                Intent intentEducation = new Intent(AddSearchEnginedActivity.this, EducationActivity.class);
                intentEducation.putExtra("education", "education");
                intentEducation.putExtra("company_name",educationId);
                startActivityForResult(intentEducation, 15);
                break;
            case R.id.relativeage:
                Intent intentAge = new Intent(AddSearchEnginedActivity.this, EducationActivity.class);
                intentAge.putExtra("education", "age");
                intentAge.putExtra("company_name", ageDatas);
                startActivityForResult(intentAge, 19);
                break;
            case R.id.relativeWorkExp://工作经验
                Intent intentExp = new Intent(AddSearchEnginedActivity.this, EducationActivity.class);
                intentExp.putExtra("education", "suffer");
                intentExp.putExtra("company_name",suffer);
                startActivityForResult(intentExp, 16);

                break;
            case R.id.relative_keyWord://关键字
                Intent intentKw = new Intent(AddSearchEnginedActivity.this, NameActivity.class);
                intentKw.putExtra("params", "kw");
                intentKw.putExtra("mobile", key_word);
                startActivityForResult(intentKw, 17);
                break;
            case R.id.relative_keywork_type://关键字类型
                initKeyWord();
                break;
            case R.id.relativeTime://发布日期
                Intent intentTime = new Intent(AddSearchEnginedActivity.this, EducationActivity.class);
                intentTime.putExtra("education", "time");
                intentTime.putExtra("company_name", issuedata);
                startActivityForResult(intentTime, 00);
                break;
            case R.id.ralativeSearcherName://搜索器名
                Intent intentSearch = new Intent(AddSearchEnginedActivity.this, NameActivity.class);
                intentSearch.putExtra("params", "searchername");
                intentSearch.putExtra("company_name", search_name);
                startActivityForResult(intentSearch, 18);
                break;

            case R.id.linear_delete://删除
                break;

        }
    }


    private List<String> listItem=new ArrayList<>();
    private List<String> cid=new ArrayList<>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            switch (requestCode) {
                case 9://工作地区
                     jobareaList = (List<String>) data.getExtras().getSerializable("listArea");
                     jobareaListId = (List<String>) data.getExtras().getSerializable("listAreaId");
                    if (jobareaList.size() == 0) {
                        textSelectArea.setText("请输入工作地区");
                    } else {
                        jobarea = "";
                        jobareaId = "";
                        for (int i = 0; i < jobareaList.size(); i++) {
                            if (jobareaList.size() == 0 || i == jobareaList.size() - 1) {
                                jobarea += jobareaList.get(i);
                                jobareaId += jobareaListId.get(i);
                            } else {
                                jobarea += jobareaList.get(i) + ",";
                                jobareaId += jobareaListId.get(i) + ",";
                            }
                        }
                        textSelectArea.setText(jobarea);
                    }

                    break;
                case 50://工作岗位
                    listItem = (List<String>) data.getExtras().getSerializable("listItem");
                     cid = (List<String>) data.getExtras().getSerializable("cid");
                    if (listItem.size() == 0) {

                    } else {
                        jobsort = "";
                        cids = "";
                        for (int i = 0; i < listItem.size(); i++) {
                            if (listItem.size() == 0 || i == listItem.size() - 1) {
                                jobsort += listItem.get(i);
                                cids += cid.get(i);
                            } else {
                                jobsort += listItem.get(i) + ",";
                                cids += cid.get(i) + ",";
                            }

                        }
                    }
                    textWorkPost.setText(jobsort);

                    break;
                case 62://行业类别
                    listts = (List<String>) data.getExtras().getSerializable("list");
                    listIds = (List<String>) data.getExtras().getSerializable("listId");
                    if (listts.size() == 0) {
                        textIndustryType.setText("请选择行业类别");
                    } else {
                        industry = "";
                        industryId = "";
                        for (int i = 0; i < listts.size(); i++) {
                            if (listts.size() == 0 || i == listts.size() - 1) {
                                industry += listts.get(i);
                                industryId += listIds.get(i);
                            } else {
                                industry += listts.get(i) + ",";
                                industryId += listIds.get(i) + ",";
                            }

                        }
                        textIndustryType.setText(industry);
                    }

                    break;
                case 15://学历
                    educationId = data.getExtras().getString("education");
                    education = data.getExtras().getString("educationId");
                    if (education==null||"".equals(education)){

                    }else{
                        textEducation.setText(educationId);
                    }

                    break;
                case 16://工作经验
                    word_year = data.getExtras().getString("sufferId");
                    suffer = data.getExtras().getString("suffer");
                    if (suffer==null||"".equals(suffer)){

                    }else{
                        textExp.setText(suffer);
                    }

                    break;
                case 17://关键字
                    key_word = data.getExtras().getString("kw");
                    if (key_word==null||"".equals(key_word)){

                    }else{
                        textkeyword.setText(key_word);
                    }

                    break;
                case 00://发布日期
                    release_date = data.getExtras().getString("issuedataId");
                    issuedata = data.getExtras().getString("issuedata");
                    if (issuedata==null||"".equals(issuedata)){

                    }else{
                        texttime.setText(issuedata);
                    }

                    break;
                case 18:
                    search_name = data.getExtras().getString("searchname");
                    if (search_name==null||"".equals(search_name)){

                    }else{
                        textsearcherName.setText(search_name);
                    }
                    break;
                case 19:
                    ageDatas = data.getExtras().getString("ageDatas");
                    ageDatasId = data.getExtras().getString("ageDatasId");
                    if (ageDatasId==null||"".equals(ageDatasId)){

                    }else{
                        textage.setText(ageDatas);
                    }
                    break;
            }
        }
    }

    /**
     * 向服务器提交数据
     */
    private void submitDataToService() {
        FormEncodingBuilder formEncod = new FormEncodingBuilder();

        if (jobarea != null && !"".equals(jobarea)) {

            formEncod.add("jobarea", jobareaId);
        } else {
            formEncod.add("jobarea", "");
        }

        if (jobsort != null && !"".equals(jobsort)) {
            formEncod.add("jobsort", cids);
        } else {
            formEncod.add("jobsort", "");
        }
        if (industry != null && !"".equals(industry)) {
            formEncod.add("industry", industryId);
        } else {
            formEncod.add("industry", "");
        }

        if (sex != null && !"".equals(sex)) {
            formEncod.add("sex", sex);
        } else {
            formEncod.add("sex", "");
        }

        if (educationId != null && !"".equals(educationId)) {
            formEncod.add("education", education);
        } else {
            formEncod.add("education", "");
        }

        if (ageDatasId != null && !"".equals(ageDatasId)) {
            formEncod.add("age", ageDatasId);
        } else {
            formEncod.add("age", "");
        }

        if (word_year != null && !"".equals(word_year)) {
            formEncod.add("word_year", word_year);
        } else {
            formEncod.add("word_year", "");
        }

        if (key_word != null && !"".equals(key_word)) {
            formEncod.add("keyword", key_word);
            Log.i("key_word", key_word);
        } else {
            formEncod.add("keyword", "");
        }

        if (keyword_type != null && !"".equals(keyword_type)) {
            formEncod.add("keyword_type", keyword_type);
            Log.i("keyword_type", keyword_type);
        } else {
            formEncod.add("keyword_type", "");
        }

        if (release_date != null && !"".equals(release_date)) {
            formEncod.add("release_date", release_date);
        } else {
            formEncod.add("release_date", "");
        }

        if (search_name == null || "".equals(search_name)) {
            Toast.makeText(AddSearchEnginedActivity.this, "搜索器名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        formEncod.add("search_name", search_name);

        sendSearcherData(formEncod);//发送搜索器数据到服务器
    }

    /**
     * 保存数据到服务器
     */
    private String str;
    private int code;
    private MyPopuwindown myPopuwindown;

    private void sendSearcherData(FormEncodingBuilder formEncod) {
//        myPopuwindown = new MyPopuwindown(AddSearchEnginedActivity.this, R.layout.my_popuwindown);
//        myPopuwindown.showAtLocation(AddSearchEnginedActivity.this.findViewById(R.id.ll_popuwind), Gravity.CENTER, 0, 0);

        StyledDialog.buildLoading().show();
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        String api_token = share.getString("api_token", "");
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.SEARCH_ENGINE_CREATE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncod.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("保存搜索器的名臣的数据", str);
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
                    if (code == 200) {
                        JSONObject json = JSON.parseObject(str);
                        String codes = json.getString("code");
                        if ("1".equals(codes)) {
                            Intent intent = new Intent(AddSearchEnginedActivity.this, SearchEnginedActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AddSearchEnginedActivity.this, "添加搜索器失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddSearchEnginedActivity.this, "添加搜索器失败", Toast.LENGTH_SHORT).show();
                    }
//                    myPopuwindown.dismiss();
                    StyledDialog.dismissLoading();
                    break;
            }
        }
    };

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
                .setSelectOptions(1, 1, 1)  //设置默认选中项
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