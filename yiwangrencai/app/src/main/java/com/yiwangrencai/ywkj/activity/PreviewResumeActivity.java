package com.yiwangrencai.ywkj.activity;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.adapter.MyPreBookAdapter;
import com.yiwangrencai.ywkj.adapter.MyPreEduAdapter;
import com.yiwangrencai.ywkj.adapter.MyPreLangAdapter;
import com.yiwangrencai.ywkj.adapter.MyPreOtherAdapter;
import com.yiwangrencai.ywkj.adapter.MyPreProExpAdapter;
import com.yiwangrencai.ywkj.adapter.MyPreSkillAdapter;
import com.yiwangrencai.ywkj.adapter.MyWorkPreViewAdapter;
import com.yiwangrencai.ywkj.bean.BookBean;
import com.yiwangrencai.ywkj.bean.EducaExperBean;
import com.yiwangrencai.ywkj.bean.LangBean;
import com.yiwangrencai.ywkj.bean.OtherBean;
import com.yiwangrencai.ywkj.bean.ProgressBean;
import com.yiwangrencai.ywkj.bean.SkillBean;
import com.yiwangrencai.ywkj.bean.WorkExperience;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.UiUtils;
import com.yiwangrencai.ywkj.tools.parsResumeShowData;

import java.io.IOException;
import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Administrator on 2017/5/8.
 */

public class PreviewResumeActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_resume;
    }

    @Override
    public void initData() {
        //从服务器获取数据
        gainDatas();
    }

    int code;
    String str;
    /**
     * 从服务器获取数据
     */
    String api_token;

    private void gainDatas() {
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        form.add("id", UiUtils.getId());
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_SHOW)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(form.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String eMessage = e.getMessage();
                System.out.println("获取的错误的信息" + eMessage);
                handler.sendEmptyMessage(101);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                System.out.println("kkkkkkkkkkkkk" + str);

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
                        JSONObject jsons = JSON.parseObject(str);
                        String codes = jsons.getString("code");

                        if (codes.equals("1")) {
                            relative_loading.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.VISIBLE);
                            JSONObject jsonData = jsons.getJSONObject("data");
                            parasenData(jsonData);//解析数据
                        } else {
                            Toast.makeText(PreviewResumeActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PreviewResumeActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };


    /**
     * 解析数据
     *
     * @param jsonData
     */
    private void parasenData(JSONObject jsonData) {
        String resume_name = jsonData.getString("resume_name");
        String name = jsonData.getString("name");
        String sex = jsonData.getString("sex");
        String intentionjobs = jsonData.getString("intentionjobs");
        String introduction = jsonData.getString("introduction");
        String mobile = jsonData.getString("mobile");
        String email = jsonData.getString("email");
        String qq = jsonData.getString("qq");
        String avatar = jsonData.getString("avatar");
        String address = jsonData.getString("address");
        String job_status = jsonData.getString("job_status");
        String resume_status = jsonData.getString("resume_status");
        String bkresume_status = jsonData.getString("bkresume_status");
        String marital = jsonData.getString("marital");
        String height = jsonData.getString("height");
        String place_name = jsonData.getString("place_name");
        String longitude = jsonData.getString("longitude");
        String latitude = jsonData.getString("latitude");
        String isexpectedsalary = jsonData.getString("isexpectedsalary");

        String expectedsalary_name = jsonData.getString("expectedsalary_name");
        String homeaddress_name = jsonData.getString("homeaddress_name");
        String work_year_name = jsonData.getString("work_year_name");
        String birthday_name = jsonData.getString("birthday_name");
        String jobsort_name = jsonData.getString("jobsort_name");
        String jobarea_name = jsonData.getString("jobarea_name");
        String education_name = jsonData.getString("education_name");
        String age = jsonData.getString("age");

        //解析工作经验数据
        List<WorkExperience> listWorkExp = parsResumeShowData.parsenArrayData(jsonData.getJSONArray("resume_workexp"));
        //解析教育背景数据
        List<EducaExperBean> educationList = parsResumeShowData.parenEducationData(jsonData.getJSONArray("resume_eduexp"));
        //解析项目经验数据
        List<ProgressBean> projectList = parsResumeShowData.parsenProgress(jsonData.getJSONArray("resume_proexp"));
        //解析语言能力数据
        List<LangBean> langList = parsResumeShowData.parsenLangArray(jsonData.getJSONArray("resume_lang"));
        //解析技能专长数据
        List<SkillBean> skillList = parsResumeShowData.parsenSkillArray(jsonData.getJSONArray("resume_skill"));
        //证书
        List<BookBean> bookList = parsResumeShowData.parsenBookArray(jsonData.getJSONArray("resume_cer"));
        //其他信息的数据
        List<OtherBean> otherList = parsResumeShowData.parsenOtherArray(jsonData.getJSONArray("resume_other"));

        if (avatar == null) {
            Picasso.with(PreviewResumeActivity.this).load(R.mipmap.avatar_m2x).into(imagepreviewresume);
        } else {
            Picasso.with(PreviewResumeActivity.this).load(ContentUrl.BASE_ICON_URL + avatar).into(imagepreviewresume);
        }

        if (educationList.size() == 0 || "".equals(educationList)) {
            text_education_exp.setVisibility(View.GONE);
            view_education.setVisibility(View.GONE);
        } else {
            text_education_exp.setVisibility(View.VISIBLE);
            view_education.setVisibility(View.VISIBLE);
        }

        if (listWorkExp.size() == 0 || "".equals(listWorkExp)) {
            text_job_exp.setVisibility(View.GONE);
            view_job.setVisibility(View.GONE);
        } else {
            text_job_exp.setVisibility(View.VISIBLE);
            view_job.setVisibility(View.VISIBLE);
        }

        if (projectList.size() == 0 || "".equals(projectList)) {
            text_program_exp.setVisibility(View.GONE);
            view_program.setVisibility(View.GONE);
        } else {
            text_program_exp.setVisibility(View.VISIBLE);
            view_program.setVisibility(View.VISIBLE);
        }

        if (langList.size() == 0 || "".equals(langList)) {
            text_language.setVisibility(View.GONE);
            view_language.setVisibility(View.GONE);
        } else {
            text_language.setVisibility(View.VISIBLE);
            view_language.setVisibility(View.VISIBLE);
        }

        if (skillList.size() == 0 || "".equals(skillList)) {
            text_skill.setVisibility(View.GONE);
            view_skill.setVisibility(View.GONE);
        } else {
            text_skill.setVisibility(View.VISIBLE);
            view_skill.setVisibility(View.VISIBLE);
        }

        if (bookList.size() == 0 || "".equals(bookList)) {
            text_book.setVisibility(View.GONE);
            view_book.setVisibility(View.GONE);
        } else {
            text_book.setVisibility(View.VISIBLE);
            view_book.setVisibility(View.VISIBLE);
        }

        if (otherList.size() == 0 || "".equals(otherList)) {
            text_other.setVisibility(View.GONE);
            view_other.setVisibility(View.GONE);
        } else {
            text_other.setVisibility(View.VISIBLE);
            view_other.setVisibility(View.VISIBLE);
        }

        textname.setText(name);
        textage.setText(age + "岁");
        if ("在校学生".equals(work_year_name)){
            textworkexp.setText(work_year_name);
        }else if("应届毕业生".equals(work_year_name)){
            textworkexp.setText(work_year_name);
        }else{
            textworkexp.setText(work_year_name + "工作经验");
        }

        texthight.setText(height + "cm");

        textloaction.setText(homeaddress_name);
        textphone.setText(mobile);
        textemail.setText(email);
        textqq.setText(qq);

        textworkintenson.setText(intentionjobs);
        textJobCategory.setText(jobsort_name);
        textplaceadd.setText(jobarea_name);
        textintruduction.setText(introduction);

        if (sex.equals("0")) {
            textsex.setText("保密");
        } else if (sex.equals("1")) {
            textsex.setText("男");
        } else if (sex.equals("2")) {
            textsex.setText("女");
        }

        if (job_status.equals("1")) {
            textworkstatue.setText("不在职，正在找工作");
        } else if (job_status.equals("2")) {
            textworkstatue.setText("在职，打算近期换工作");
        } else if (job_status.equals("3")) {
            textworkstatue.setText("在职，有更好的机会才考虑");
        } else if (job_status.equals("4")) {
            textworkstatue.setText("不考虑换工作");
        }

        if (isexpectedsalary == null || "".equals(isexpectedsalary)) {

        } else {
            if ("0".equals(isexpectedsalary)){
                textviewdiscuss.setText(expectedsalary_name);
            }else{
                textviewdiscuss.setText("面议");
            }

        }

        listviewworkexperience.setAdapter(new MyWorkPreViewAdapter(listWorkExp));
        listvieweduecationxperience.setAdapter(new MyPreEduAdapter(educationList));
        listviewprojectexperience.setAdapter(new MyPreProExpAdapter(projectList));
        listviewlanguage.setAdapter(new MyPreLangAdapter(langList));
        listviewskill.setAdapter(new MyPreSkillAdapter(skillList));
        listviewbook.setAdapter(new MyPreBookAdapter(bookList));
        listviewothermessage.setAdapter(new MyPreOtherAdapter(otherList));

        scrollview.smoothScrollTo(0,0);
    }


    private ImageView imagepreviewresume;
    private TextView textname;
    private TextView textsex;
    private TextView textage;
    private TextView textworkexp;
    private TextView texthight;
    private TextView textworkstatue;
    private TextView textloaction;
    private TextView textphone;
    private TextView textemail;
    private TextView textqq;
    private TextView textworkintenson;
    private TextView textJobCategory;
    private TextView textviewdiscuss;
    private TextView textplaceadd;
    private TextView textintruduction;
    private TextView text_education_exp;
    private TextView text_job_exp;
    private TextView text_program_exp;
    private TextView text_language;
    private TextView text_skill;
    private TextView text_book;
    private TextView text_other;
   // private TextView text_book2;
    private FrameLayout fram_decribe_back;
    private ListView listviewworkexperience;
    private ListView listvieweduecationxperience;
    private ListView listviewprojectexperience;
    private ListView listviewlanguage;
    private ListView listviewskill;
    private ListView listviewbook;
    private ListView listviewothermessage;
    private View view_education;
    private View view_job;
    private View view_program;
    private View view_language;
    private View view_skill;
    private View view_book;
    private View view_other;
    private ScrollView scrollview;
    private RelativeLayout relative_loading;


    @Override
    public void initView() {

        fram_decribe_back = (FrameLayout) findViewById(R.id.fram_decribe_back);

        imagepreviewresume = (ImageView) findViewById(R.id.imagepreviewresume);
        textname = (TextView) findViewById(R.id.textname);
        textsex = (TextView) findViewById(R.id.textsex);
        textage = (TextView) findViewById(R.id.textage);
        textworkexp = (TextView) findViewById(R.id.textworkexp);
        texthight = (TextView) findViewById(R.id.texthight);
        textworkstatue = (TextView) findViewById(R.id.textworkstatue);
        textloaction = (TextView) findViewById(R.id.textloaction);
        textphone = (TextView) findViewById(R.id.textphone);
        textemail = (TextView) findViewById(R.id.textemail);
        textqq = (TextView) findViewById(R.id.textqq);
        textworkintenson = (TextView) findViewById(R.id.textworkintenson);
        textJobCategory = (TextView) findViewById(R.id.textJobCategory);
        textviewdiscuss = (TextView) findViewById(R.id.textviewdiscuss);
        textplaceadd = (TextView) findViewById(R.id.textplaceadd);
        textintruduction = (TextView) findViewById(R.id.textintruduction);
        text_education_exp = (TextView) findViewById(R.id.text_education_exp);
        text_job_exp = (TextView) findViewById(R.id.text_job_exp);
        text_program_exp = (TextView) findViewById(R.id.text_program_exp);
        text_language = (TextView) findViewById(R.id.text_language);
        text_skill = (TextView) findViewById(R.id.text_skill);
        text_book = (TextView) findViewById(R.id.text_book);
        text_other = (TextView) findViewById(R.id.text_other);
        scrollview= (ScrollView) findViewById(R.id.scrollview);
        relative_loading= (RelativeLayout) findViewById(R.id.relative_loading);

        relative_loading.setVisibility(View.VISIBLE);
        scrollview.setVisibility(View.INVISIBLE);

        view_education = (View) findViewById(R.id.view_education);
        view_job = (View) findViewById(R.id.view_job);
        view_program = (View) findViewById(R.id.view_program);
        view_language = (View) findViewById(R.id.view_language);
        view_skill = (View) findViewById(R.id.view_skill);
        view_book = (View) findViewById(R.id.view_book);
        view_other = (View) findViewById(R.id.view_other);

        listviewworkexperience = (ListView) findViewById(R.id.listviewworkexperience);
        listvieweduecationxperience = (ListView) findViewById(R.id.listvieweduecationxperience);
        listviewprojectexperience = (ListView) findViewById(R.id.listviewprojectexperience);
        listviewlanguage = (ListView) findViewById(R.id.listviewlanguage);
        listviewskill = (ListView) findViewById(R.id.listviewskill);
        listviewbook = (ListView) findViewById(R.id.listviewbook);
        listviewothermessage = (ListView) findViewById(R.id.listviewothermessage);

        fram_decribe_back.setOnClickListener(this);
    }

    /**
     * 点击事件的方法集合
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_decribe_back://返回上一个界面
                finish();
                break;
        }
    }
}
