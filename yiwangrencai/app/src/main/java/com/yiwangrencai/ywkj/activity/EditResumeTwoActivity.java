package com.yiwangrencai.ywkj.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hss01248.dialog.StyledDialog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.wxapi.WXEntryActivity;
import com.yiwangrencai.ywkj.adapter.MyListBookAdapter;
import com.yiwangrencai.ywkj.adapter.MyListEduAdapter;
import com.yiwangrencai.ywkj.adapter.MyListLangAdapter;
import com.yiwangrencai.ywkj.adapter.MyListOtherAdapter;
import com.yiwangrencai.ywkj.adapter.MyListProgressAdapter;
import com.yiwangrencai.ywkj.adapter.MyListSkillAdapter;
import com.yiwangrencai.ywkj.adapter.MyListWorkAdapter;
import com.yiwangrencai.ywkj.bean.BookBean;
import com.yiwangrencai.ywkj.bean.EducaExperBean;
import com.yiwangrencai.ywkj.bean.LangBean;
import com.yiwangrencai.ywkj.bean.OtherBean;
import com.yiwangrencai.ywkj.bean.ProgressBean;
import com.yiwangrencai.ywkj.bean.SkillBean;
import com.yiwangrencai.ywkj.bean.WorkExperience;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.parsResumeShowData;
import com.yiwangrencai.ywkj.view.CircleTransform;
import com.yiwangrencai.ywkj.view.MyPopuwindown;
import com.yiwangrencai.ywkj.view.MySexPopuwin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import it.sephiroth.android.library.picasso.Picasso;
import okhttp3.Call;
import okhttp3.MultipartBody;

/**
 * Created by Administrator on 2017/4/27.
 */

public class EditResumeTwoActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.edit_resume_two_activiyt;
    }

    private String resume_name;
    private String language;
    private String name;
    private String sex;
    private String expectedsalary;
    private String isexpectedsalary;
    private String intentionjobs;
    private String introduction;
    private String mobile;
    private String email;
    private String qq;
    private String avatar;
    private String job_status;
    private String resume_status;
    private String bkresume_status;
    private String marital;
    private String height;
    private String birthday_name;
    private String homeaddress_name;
    private String work_year_name;
    private String jobsort_name;
    private String jobarea_name;
    private String education_name;
    private String census_name;
    private String age;
    private String expectedsalary_name;
    private String address;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    if (code == 200) {
                        JSONObject jsonObject1 = JSON.parseObject(str);
                        String codes = jsonObject1.getString("code");
                        String msgs = jsonObject1.getString("msg");
                        if ("1".equals(codes)) {
                            progress_liading.setVisibility(View.INVISIBLE);
                            scrollview.setVisibility(View.VISIBLE);
                            System.out.println("123456789   ====" + codes);
                            String jsonObject2 = jsonObject1.getString("data");
                            JSONObject jsonObject = JSON.parseObject(jsonObject2);
                            resume_name = jsonObject.getString("resume_name");
                            language = jsonObject.getString("language");
                            name = jsonObject.getString("name");
                            System.out.println("123456789  " + name);

                            sex = jsonObject.getString("sex");
                            expectedsalary = jsonObject.getString("expectedsalary");
                            isexpectedsalary = jsonObject.getString("isexpectedsalary");
                            intentionjobs = jsonObject.getString("intentionjobs");
                            introduction = jsonObject.getString("introduction");
                            mobile = jsonObject.getString("mobile");
                            email = jsonObject.getString("email");
                            qq = jsonObject.getString("qq");
                            avatar = jsonObject.getString("avatar");
                            address = jsonObject.getString("address");
                            job_status = jsonObject.getString("job_status");
                            resume_status = jsonObject.getString("resume_status");
                            bkresume_status = jsonObject.getString("bkresume_status");

                            marital = jsonObject.getString("marital");
                            height = jsonObject.getString("height");
                            birthday_name = jsonObject.getString("birthday_name");
                            homeaddress_name = jsonObject.getString("homeaddress_name");
                            expectedsalary_name = jsonObject.getString("expectedsalary_name");
                            work_year_name = jsonObject.getString("work_year_name");
                            jobsort_name = jsonObject.getString("jobsort_name");
                            jobarea_name = jsonObject.getString("jobarea_name");
                            education_name = jsonObject.getString("education_name");
                            census_name = jsonObject.getString("census_name");
                            age = jsonObject.getString("age");
                            //解析工作经验数据
                            listWorkExp = parsResumeShowData.parsenArrayData(jsonObject.getJSONArray("resume_workexp"));

                            //解析教育背景的数据
                            educationList = parsResumeShowData.parenEducationData(jsonObject.getJSONArray("resume_eduexp"));

                            //解析项目经历的数据
                            progressList = parsResumeShowData.parsenProgress(jsonObject.getJSONArray("resume_proexp"));
                            //解析语言技能的数据
                            langList = parsResumeShowData.parsenLangArray(jsonObject.getJSONArray("resume_lang"));
                            //解析技能专长
                            skillList = parsResumeShowData.parsenSkillArray(jsonObject.getJSONArray("resume_skill"));
                            //解析证书的数据
                            bookList = parsResumeShowData.parsenBookArray(jsonObject.getJSONArray("resume_cer"));
                            //解析其他信息的数据
                            otherList = parsResumeShowData.parsenOtherArray(jsonObject.getJSONArray("resume_other"));

                            SharedPreferences shares = getSharedPreferences("data", MODE_PRIVATE);
                            SharedPreferences.Editor edit = shares.edit();
                            edit.putString("avatar", avatar);
                            edit.commit();

                            editTwoResumeName.setText(name);

                            textTwoResumeAge.setText(age);
                            textTwoResumeEducation.setText(education_name + " | " + work_year_name + "工作经验");
                            textTwoResumeArea.setText(homeaddress_name);
                            tv_search_work.setText(job_status);
                            textTwoResumePhone.setText(mobile);
                            textTwoResumeEmail.setText(email);

                            if (expectedsalary == null || "".equals(expectedsalary) || "0".equals(expectedsalary)) {
                                textTwoResumeSaraly.setText("请选择期望的薪资");
                            } else {
                                textTwoResumeSaraly.setText(expectedsalary_name);
                            }

                            if (jobsort_name == null || "".equals(jobsort_name)) {
                                textTwoResumeJob.setText("请选择工作岗位");
                            } else {
                                textTwoResumeJob.setText(jobsort_name);
                            }

                            if (intentionjobs == null || "".equals(intentionjobs)) {
                                textTwoResumeIntensionJob.setText("请选择意向工作岗位");
                            } else {
                                textTwoResumeIntensionJob.setText(intentionjobs);
                            }

                            if (jobarea_name == null || "".equals(jobarea_name)) {
                                textTwoResumeJobArea.setText("请选择工作的区域");
                            } else {
                                textTwoResumeJobArea.setText(jobarea_name);
                            }

                            if (introduction.isEmpty()) {
                                relative_induction.setVisibility(View.GONE);
                                relative_add_introduce.setVisibility(View.VISIBLE);

                            } else {
                                relative_induction.setVisibility(View.VISIBLE);
                                relative_add_introduce.setVisibility(View.GONE);
                                tv_introduction.setText(introduction);
                            }

                            if ("1".equals(sex)) {
                                textTwoResumeSex.setText("男");
                            } else if ("2".equals(sex)) {
                                textTwoResumeSex.setText("女");
                            } else if ("0".equals(sex)) {
                                textTwoResumeSex.setText("保密");
                            }

                            if ("1".equals(job_status)) {
                                tv_search_work.setText("不在职，正在找工作");
                            } else if ("2".equals(job_status)) {
                                tv_search_work.setText("在职，打算近期换工作");
                            } else if ("3".equals(job_status)) {
                                tv_search_work.setText("在职，有更好的机会才考虑");
                            } else if ("4".equals(job_status)) {
                                tv_search_work.setText("不考虑换工作");
                            }

                            if (isexpectedsalary == null || "".equals(isexpectedsalary)) {
                                textTwoResumeView.setText("请选择显示面议");
                            } else {
                                if ("1".equals(isexpectedsalary)) {
                                    textTwoResumeView.setText("显示面议");
                                } else if ("0".equals(isexpectedsalary)) {
                                    textTwoResumeView.setText("不显示面议");
                                }
                            }

                            if ("0".equals(chkphoto_open)) {
                                textviewopen.setText("公开");
                            } else if ("1".equals(chkphoto_open)) {
                                textviewopen.setText("隐藏");
                            }

                            listview.setAdapter(new MyListWorkAdapter(listWorkExp));
                            listvieweducation.setAdapter(new MyListEduAdapter(educationList));
                            listviewprogress.setAdapter(new MyListProgressAdapter(progressList));
                            listviewlangue.setAdapter(new MyListLangAdapter(langList));
                            listviewskill.setAdapter(new MyListSkillAdapter(skillList));
                            listviewbook.setAdapter(new MyListBookAdapter(bookList));
                            listviewothermessage.setAdapter(new MyListOtherAdapter(otherList));

                            if ("".equals(avatar) || avatar == null) {
                                Picasso.with(EditResumeTwoActivity.this)
                                        .load(R.mipmap.avatar_m2x)
                                        .error(R.mipmap.avatar_m2x)
                                        .transform(new CircleTransform())
                                        .into(imageview);
                            } else {
                                Picasso.with(EditResumeTwoActivity.this)
                                        .load(ContentUrl.BASE_ICON_URL + avatar)
                                        .error(R.mipmap.avatar_m2x)
                                        .transform(new CircleTransform())
                                        .into(imageview);
                            }

                            scrollview.smoothScrollTo(0, 0);

                        } else if ("-1".equals(codes)) {
                            Intent intent = new Intent(EditResumeTwoActivity.this, WXEntryActivity.class);
                            intent.putExtra("register", "register");
                            startActivity(intent);
                            finish();
                        }

                        if (msgs != null && !"".equals(msgs)) {
                            Toast.makeText(EditResumeTwoActivity.this, msgs, Toast.LENGTH_SHORT).show();
                        }
                    } else {

                    }
                    break;
                case 333:
                    if (code == 200) {
                        JSONObject jsons = JSON.parseObject(str);
                        String codess = jsons.getString("code");
                        if ("1".equals(codess)) {

                            if ("0".equals(chkphoto_open)) {
                                textviewopen.setText("公开");
                            } else if ("1".equals(chkphoto_open)) {
                                textviewopen.setText("隐藏");
                            }

                            SharedPreferences share = getSharedPreferences("data", MODE_PRIVATE);
                            SharedPreferences.Editor edit = share.edit();
                            edit.putString("chkphoto_open", chkphoto_open);
                            edit.commit();
                            sexPopuWind.dismiss();
                            StyledDialog.dismissLoading();
                            Toast.makeText(EditResumeTwoActivity.this, jsons.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else {
                            sexPopuWind.dismiss();
                            Toast.makeText(EditResumeTwoActivity.this, "头像操作失败", Toast.LENGTH_SHORT).show();
                            if ("0".equals(chkphoto_open)) {
                                chkphoto_open = "1";
                            } else if ("1".equals(chkphoto_open)) {
                                chkphoto_open = "0";
                            }
                        }
                    } else {
                        if ("0".equals(chkphoto_open)) {
                            chkphoto_open = "1";
                        } else if ("1".equals(chkphoto_open)) {
                            chkphoto_open = "0";
                        }
                        sexPopuWind.dismiss();
                        Toast.makeText(EditResumeTwoActivity.this, "头像操作失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 133:
                    if (code == 200) {
                        StyledDialog.dismissLoading();
                        JSONObject json = JSON.parseObject(str);
                        String codess = json.getString("code");
                        if ("1".equals(codess)) {

                            Toast.makeText(EditResumeTwoActivity.this, json.getString("msg"), Toast.LENGTH_SHORT).show();
                            SharedPreferences shares = getSharedPreferences("data", MODE_PRIVATE);
                            SharedPreferences.Editor edit = shares.edit();
                            JSONObject jsons = json.getJSONObject("data");
                            avatar = jsons.getString("avatar");
                            Log.i("ttttttt", jsons.getString("avatar"));
                            edit.putString("avatar", avatar);
                            edit.commit();
                            updataJGData();

                            Picasso.with(EditResumeTwoActivity.this).load(ContentUrl.BASE_ICON_URL + avatar).transform(new CircleTransform()).into(imageview);

                        } else {
                            Toast.makeText(EditResumeTwoActivity.this, "头像更新失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        StyledDialog.dismissLoading();
                        Toast.makeText(EditResumeTwoActivity.this, "头像更新失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void updataJGData() {
        cn.jpush.im.android.api.model.UserInfo userInfo = JMessageClient.getMyInfo();
        Log.i("从本地获取当前登录用户的信息", "1111111" + userInfo);
        if (userInfo != null) {
            userInfo.setAddress(avatar);
            JMessageClient.updateMyInfo(cn.jpush.im.android.api.model.UserInfo.Field.address, userInfo, new BasicCallback() {

                @Override
                public void gotResult(int i, String s) {
                    Log.i("更新头像", "i：" + i + "  s;" + s);
                }
            });

        }
    }

    private List<OtherBean> otherList = new ArrayList<>();
    private List<BookBean> bookList = new ArrayList();
    private List<SkillBean> skillList = new ArrayList<>();
    private List<LangBean> langList = new ArrayList();
    private List<ProgressBean> progressList = new ArrayList();
    private List<EducaExperBean> educationList = new ArrayList();
    private List<WorkExperience> listWorkExp = new ArrayList();
    private String api_token;
    private String resume_id;

    @Override
    public void initData() {

        SharedPreferences sharedPreferencess = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = sharedPreferencess.getString("api_token", "");

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        resume_id = sharedPreferences.getString("resume_id", "");

        chkphoto_open = sharedPreferences.getString("chkphoto_open", "");

        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        formEncodingBuilder.add("id", resume_id);
        Request request = new Request
                .Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_SHOW)
                .post(formEncodingBuilder.build())
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                System.out.println("55555555++++++" + str);
                System.out.println("55555555code" + code);
                handler.sendEmptyMessage(1000);
            }
        });

    }

    private TextView editTwoResumeName;
    private TextView textTwoResumeSex;
    private TextView textTwoResumeAge;
    private TextView textTwoResumeEducation;
    private TextView tv_work_experience;
    private TextView tv_search_work;
    private TextView textTwoResumeArea;
    private TextView textTwoResumePhone;
    private TextView textTwoResumeEmail;
    private TextView textTwoResumeSaraly;
    private TextView textTwoResumeView;
    private TextView textTwoResumeIntensionJob;
    private TextView textTwoResumeJob;
    private TextView textTwoResumeJobArea;
    private TextView tv_introduction;
    private TextView textviewopen;
    private FrameLayout fram_decribe_back;
    private RelativeLayout rl_next_view;
    private RelativeLayout relationEditEmail;
    private RelativeLayout relative_intent_saraly;
    private RelativeLayout relative_view_show;
    private RelativeLayout relative_intent_work;
    private RelativeLayout relative_work;
    private RelativeLayout relative_work_area;
    private RelativeLayout relative_add_introduce;
    private RelativeLayout relative_induction;
    private RelativeLayout relative_work_experience;
    private RelativeLayout relative_education_background;
    private RelativeLayout relative_add_project;
    private RelativeLayout relative_add_langue;
    private RelativeLayout relative_add_skill;
    private RelativeLayout bookAdd;
    private RelativeLayout relative_add_book;
    private RelativeLayout relative_add_other;
    private RelativeLayout relationEdiPhone;
    private RelativeLayout recycler_photo;
    private RelativeLayout relative_resume_conceal;
    private RelativeLayout progress_liading;
    private ListView listview;
    private ListView listvieweducation;
    private ListView listviewprogress;
    private ListView listviewlangue;
    private ListView listviewskill;
    private ListView listviewbook;
    private ListView listviewothermessage;
    private ImageView imageview;
    private ScrollView scrollview;

    @Override
    public void initView() {

        fram_decribe_back = (FrameLayout) findViewById(R.id.fram_decribe_back);
        rl_next_view = (RelativeLayout) findViewById(R.id.rl_next_view);
        editTwoResumeName = (TextView) findViewById(R.id.editTwoResumeName);
        textTwoResumeSex = (TextView) findViewById(R.id.textTwoResumeSex);
        textTwoResumeAge = (TextView) findViewById(R.id.textTwoResumeAge);
        textTwoResumeEducation = (TextView) findViewById(R.id.textTwoResumeEducation);
        //   tv_work_experience = (TextView) findViewById(R.id.tv_work_experience);
        textTwoResumeArea = (TextView) findViewById(R.id.textTwoResumeArea);
        tv_search_work = (TextView) findViewById(R.id.tv_search_work);
        textTwoResumePhone = (TextView) findViewById(R.id.textTwoResumePhone);
        textTwoResumeEmail = (TextView) findViewById(R.id.textTwoResumeEmail);
        textviewopen = (TextView) findViewById(R.id.textviewopen);
        relationEditEmail = (RelativeLayout) findViewById(R.id.relationEditEmail);
        relationEdiPhone = (RelativeLayout) findViewById(R.id.relationEdiPhone);
        recycler_photo = (RelativeLayout) findViewById(R.id.recycler_photo);
        relative_resume_conceal = (RelativeLayout) findViewById(R.id.relative_resume_conceal);

        imageview = (ImageView) findViewById(R.id.imageview);

        relative_intent_saraly = (RelativeLayout) findViewById(R.id.relative_intent_saraly);
        relative_view_show = (RelativeLayout) findViewById(R.id.relative_view_show);
        relative_intent_work = (RelativeLayout) findViewById(R.id.relative_intent_work);
        relative_work = (RelativeLayout) findViewById(R.id.relative_work);
        relative_work_area = (RelativeLayout) findViewById(R.id.relative_work_area);
        relative_add_introduce = (RelativeLayout) findViewById(R.id.relative_add_introduce);
        relative_induction = (RelativeLayout) findViewById(R.id.relative_induction);
        relative_work_experience = (RelativeLayout) findViewById(R.id.relative_work_experience);
        relative_education_background = (RelativeLayout) findViewById(R.id.relative_education_background);
        relative_add_project = (RelativeLayout) findViewById(R.id.relative_add_project);
        relative_add_langue = (RelativeLayout) findViewById(R.id.relative_add_langue);
        relative_add_skill = (RelativeLayout) findViewById(R.id.relative_add_skill);
        relative_add_book = (RelativeLayout) findViewById(R.id.relative_add_book);
        relative_add_other = (RelativeLayout) findViewById(R.id.relative_add_other);

        listview = (ListView) findViewById(R.id.listview);
        listvieweducation = (ListView) findViewById(R.id.listvieweducation);
        listviewprogress = (ListView) findViewById(R.id.listviewprogress);

        listviewlangue = (ListView) findViewById(R.id.listviewlangue);
        listviewskill = (ListView) findViewById(R.id.listviewskill);
        listviewbook = (ListView) findViewById(R.id.listviewbook);
        listviewothermessage = (ListView) findViewById(R.id.listviewothermessage);
        scrollview = (ScrollView) findViewById(R.id.scrollview);

        textTwoResumeSaraly = (TextView) findViewById(R.id.textTwoResumeSaraly);
        textTwoResumeView = (TextView) findViewById(R.id.textTwoResumeView);
        textTwoResumeIntensionJob = (TextView) findViewById(R.id.textTwoResumeIntensionJob);
        textTwoResumeJob = (TextView) findViewById(R.id.textTwoResumeJob);
        textTwoResumeJobArea = (TextView) findViewById(R.id.textTwoResumeJobArea);
        tv_introduction = (TextView) findViewById(R.id.tv_introduction);
        progress_liading = (RelativeLayout) findViewById(R.id.progress_liading_resume);

        progress_liading.setVisibility(View.VISIBLE);
        scrollview.setVisibility(View.INVISIBLE);

        fram_decribe_back.setOnClickListener(this);
        rl_next_view.setOnClickListener(this);
        recycler_photo.setOnClickListener(this);
        relationEditEmail.setOnClickListener(this);
        relationEdiPhone.setOnClickListener(this);
        relative_intent_saraly.setOnClickListener(this);
        relative_view_show.setOnClickListener(this);
        relative_intent_work.setOnClickListener(this);
        relative_work.setOnClickListener(this);
        relative_work_area.setOnClickListener(this);
        relative_add_introduce.setOnClickListener(this);
        relative_induction.setOnClickListener(this);
        relative_work_experience.setOnClickListener(this);
        relative_education_background.setOnClickListener(this);
        relative_add_project.setOnClickListener(this);
        relative_add_langue.setOnClickListener(this);
        relative_add_skill.setOnClickListener(this);
        relative_add_book.setOnClickListener(this);
        relative_add_other.setOnClickListener(this);
        relative_resume_conceal.setOnClickListener(this);
        System.out.println("000000000000" + name);

        editTwoResumeName.setText(name + " | ");
        textTwoResumeSex.setText(sex + " | ");
        //textTwoResumeAge.setText("");
        textTwoResumeEducation.setText(education_name);
        textTwoResumeArea.setText(homeaddress_name + " | ");
        tv_search_work.setText(job_status);
        textTwoResumePhone.setText(mobile);
        textTwoResumeEmail.setText(email);

        textTwoResumeSaraly.setText(expectedsalary_name);
        textTwoResumeView.setText(isexpectedsalary);
        textTwoResumeIntensionJob.setText(intentionjobs);
        textTwoResumeJob.setText(jobsort_name);
        textTwoResumeJobArea.setText(jobarea_name);


        //工作经历的条目点击事件
        listview.setOnItemClickListener(onMyListViewItemClient);
        //教育背景的条目点击事件
        listvieweducation.setOnItemClickListener(onMyEducationItemClient);
        //项目经历的条目点击事件
        listviewprogress.setOnItemClickListener(onMyProgressItemClient);
        //添加语言能力的条目点击事件
        listviewlangue.setOnItemClickListener(onMyLanguageItemClient);
        //添加技能专长的条目的点击事件
        listviewskill.setOnItemClickListener(onMySkillItemClient);
        //添加证书的条目的点击事件
        listviewbook.setOnItemClickListener(onMyBookItemClient);
        //添加其他信息的条目的点击事件
        listviewothermessage.setOnItemClickListener(onMyOtherItemClient);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_decribe_back:
                Intent intentt = new Intent(EditResumeTwoActivity.this, MainActivity.class);
                intentt.putExtra("register", "register");
                intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentt);
                finish();
                break;
            case R.id.rl_next_view://基本资料
                openBaseInfoActivity();
                break;
            case R.id.recycler_photo:
                showPopuwindowPhoto();//弹出修改头像的对话框
                break;
            case R.id.relative_resume_conceal://在简历中隐藏或公开头像
                showPopuwindHeadPortraits();//在简历中隐藏或公开头像
                break;
            case R.id.relationEdiPhone:
                Intent intentPhone = new Intent(EditResumeTwoActivity.this, EditEmailActivity.class);
                intentPhone.putExtra("mobile", mobile);
                intentPhone.putExtra("email", email);
                intentPhone.putExtra("qq", qq);
                startActivity(intentPhone);
                break;
            case R.id.relationEditEmail://修改邮箱
                Intent intentEmail = new Intent(EditResumeTwoActivity.this, EditEmailActivity.class);
                intentEmail.putExtra("mobile", mobile);
                intentEmail.putExtra("email", email);
                intentEmail.putExtra("qq", qq);
                startActivity(intentEmail);
                break;
            case R.id.relative_intent_saraly://薪资期望
                openEditIntensionActivity();
                break;
            case R.id.relative_view_show://显示面议
                openEditIntensionActivity();
                break;
            case R.id.relative_intent_work://意向工作岗位
                openEditIntensionActivity();
                break;
            case R.id.relative_work://工作岗位
                openEditIntensionActivity();
                break;
            case R.id.relative_work_area://工作地区
                openEditIntensionActivity();
                break;
            case R.id.relative_add_introduce:
                Intent intent = new Intent(EditResumeTwoActivity.this, DescribeActivity.class);
                intent.putExtra("params", "self_assessment");
                startActivity(intent);
                break;
            case R.id.relative_induction:
                Intent intentIn = new Intent(EditResumeTwoActivity.this, DescribeActivity.class);
                intentIn.putExtra("params", "self_assessment");
                intentIn.putExtra("introduction", introduction);
                startActivity(intentIn);
                break;
            case R.id.relative_work_experience://添加工作经历
                Intent intentWorkExper = new Intent(EditResumeTwoActivity.this, AddWorkExpActivity.class);
                intentWorkExper.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentWorkExper);
                break;
            case R.id.relative_education_background://添加教育背景
                Intent intentEdu = new Intent(EditResumeTwoActivity.this, EducationTrainActivity.class);
                intentEdu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentEdu);
                break;
            case R.id.relative_add_project://添加项目经历
                Intent addProject = new Intent(EditResumeTwoActivity.this, AddProjectActivity.class);
                addProject.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(addProject);
                break;
            case R.id.relative_add_langue://添加语言能力
                Intent langueAdd = new Intent(EditResumeTwoActivity.this, AddLangActivity.class);
                startActivity(langueAdd);
                break;
            case R.id.relative_add_skill://天假技能专长
                Intent skillAdd = new Intent(EditResumeTwoActivity.this, AddSkillActivity.class);
                skillAdd.putExtra("params", "skill");
                startActivity(skillAdd);
                break;
            case R.id.relative_add_book://添加证书
                Intent bookAdd = new Intent(EditResumeTwoActivity.this, AddSkillActivity.class);
                bookAdd.putExtra("params", "book");
                startActivity(bookAdd);

                break;
            case R.id.relative_add_other://添加其他信息
                Intent otherAdd = new Intent(EditResumeTwoActivity.this, AddSkillActivity.class);
                otherAdd.putExtra("params", "other");
                startActivity(otherAdd);
                break;
            case R.id.text_take_photo://点击拍摄照片
                myPopuwindown.dismiss();
                openTakePhoto();//打开相机进行拍照
                break;
            case R.id.text_pictuor://点击相册
                myPopuwindown.dismiss();
                openPhotoAlbum();//打开相册
                break;
            case R.id.text_cancle://点击取消
                myPopuwindown.dismiss();
                break;
            case R.id.text_open_photo://点击隐藏或公开

                if ("0".equals(chkphoto_open)) {
                    chkphoto_open = "1";
                } else if ("1".equals(chkphoto_open)) {
                    chkphoto_open = "0";
                }

                openResumePhoto();
                break;
            case R.id.text_cancle_open:
                sexPopuWind.dismiss();
                break;
        }
    }

    /**
     * 公开或隐藏简历头像
     */
    String chkphoto_open = "0";
    String str;
    int code;
    MyPopuwindown myLoading;

    private void openResumePhoto() {
        StyledDialog.buildLoading().show();
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder foem = new FormEncodingBuilder();
        foem.add("chkphoto_open", chkphoto_open);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_CHKPHOTO_OPEN)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(foem.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("aaaaaaaaaaa", str);
                handler.sendEmptyMessage(333);
            }
        });
    }

    /**
     * 在简历中隐藏或公开头像
     */
    MySexPopuwin sexPopuWind;

    private void showPopuwindHeadPortraits() {

        sexPopuWind = new MySexPopuwin(EditResumeTwoActivity.this, R.layout.popuwindown_head_open);
        View view = sexPopuWind.getView();
        ColorDrawable dw = new ColorDrawable(0xaa000000);
        sexPopuWind.setBackgroundDrawable(dw);
        sexPopuWind.showAtLocation(EditResumeTwoActivity.this.findViewById(R.id.ll_popuwindd), Gravity.BOTTOM, 0, 0);
        sexPopuWind.setOutsideTouchable(false);
        TextView text_open_photo = (TextView) view.findViewById(R.id.text_open_photo);
        TextView text_cancle_open = (TextView) view.findViewById(R.id.text_cancle_open);

        if ("0".equals(chkphoto_open)) {
            text_open_photo.setText("隐藏");
        } else if ("1".equals(chkphoto_open)) {
            text_open_photo.setText("公开");
        }

        text_open_photo.setOnClickListener(this);
        text_cancle_open.setOnClickListener(this);
    }

    /**
     * 打开相册进行选择图片
     */
    private void openPhotoAlbum() {
        if (ContextCompat.checkSelfPermission(EditResumeTwoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditResumeTwoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }

    /**
     * 启动相册
     */
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 12);
    }

    /**
     * 打开相机进行拍照
     */
    private Uri imageUrl;
    File outputImage = null;

    private void openTakePhoto() {
        outputImage = new File(getExternalCacheDir(), "output_image.jpg");

        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            imageUrl = FileProvider.getUriForFile(EditResumeTwoActivity.this, "com.yiwangrencai.fileprovider", outputImage);
        } else {
            imageUrl = Uri.fromFile(outputImage);
        }
        Log.i("imageUrl", imageUrl + "");
        //启动相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 10);
    }

    private String path;
    Bitmap bitmap = null;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (data == null) {
                    startPhotoZoom(imageUrl);
                } else {
                    Uri uri = data.getData();
                    startPhotoZoom(uri);
                }
                break;
            case 12:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);//4.4及以上系统使用这个方法处理图片
                    } else {
                        handleImageBeforeKitKat(data);//4.4以下系统使用这个方法处理图片
                    }
                }
                break;
            case 14:
                Bitmap btmaip = null;
                if (data == null) {
                    try {
                        if (imageUrl!=null){
                            btmaip = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUrl));
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    btmaip = data.getExtras().getParcelable("data");
                }

                File fImage = new File(getExternalCacheDir(), "out_exearl.jpg");
                FileOutputStream iStream = null;
                if (fImage.exists()) {
                    fImage.delete();
                }
                try {
                    fImage.createNewFile();
                    iStream = new FileOutputStream(fImage);
                    if (btmaip == null) {

                    } else {

                        btmaip.compress(Bitmap.CompressFormat.JPEG, 50, iStream);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (iStream == null) {

                        } else {
                            iStream.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                submitTakePhoto(fImage);
                break;
        }
    }


    /**
     * 安卓7.0裁剪根据文件路径获取uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 上传头像
     */
    private void submitTakePhoto(File file) {
        StyledDialog.buildLoading().show();
        okhttp3.OkHttpClient okhttp = new okhttp3.OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        builder.addFormDataPart("file", file.getName(), okhttp3.RequestBody.create(okhttp3.MediaType.parse("image/*"), file));

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_AVATAR)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(builder.build())
                .build();

        okhttp.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e.getMessage() != null) {
                    Log.i("vvvvvv", e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("aaaaaaaa", str);
                handler.sendEmptyMessage(133);
            }
        });

    }

    /**
     * 4.4以下系统使用这个方法处理图片
     *
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        startPhotoZoom(uri);
    }

    /**
     * 收缩图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Log.i("log", uri + "");

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 180);
        intent.putExtra("outputY", 180);

        intent.putExtra("return-data", true);
        startActivityForResult(intent, 14);

    }

    /**
     * 4.4及以上系统使用这个方法处理图片
     *
     * @param data
     */
    String imagePath = null;

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {

        Uri uri = data.getData();
        Log.d("intent.getData :", "" + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            Log.d("getDocumentId(uri) :", "" + docId);
            Log.d("uri.getAuthority() :", "" + uri.getAuthority());
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                Log.e("media :", "" + imagePath);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
                Log.e("downloads :", "" + imagePath);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
            Log.e("content", "" + imagePath);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
            Log.e("file", "" + imagePath);
        }
        startPhotoZoom(uri);
    }

    /**
     * 展示图片
     *
     * @param imagePath
     */
    private void displayImage(String imagePath) {
        if (imagePath == null || imagePath.equals("")) {
            Picasso.with(EditResumeTwoActivity.this)
                    .load(R.mipmap.avatar_m2x)
                    .transform(new CircleTransform())
                    .into(imageview);
        } else {
            Picasso.with(EditResumeTwoActivity.this)
                    .load(imagePath)
                    .transform(new CircleTransform())
                    .into(imageview);
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, selection, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

            cursor.close();
        }
        return path;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "需要设置权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 弹出修改头像的对话框
     */
    MyPopuwindown myPopuwindown;

    private void showPopuwindowPhoto() {
        myPopuwindown = new MyPopuwindown(EditResumeTwoActivity.this, R.layout.popuwindown_photo);
        View view = myPopuwindown.getView();
        ColorDrawable dw = new ColorDrawable(0xaa000000);
        myPopuwindown.setBackgroundDrawable(dw);
        myPopuwindown.showAtLocation(EditResumeTwoActivity.this.findViewById(R.id.ll_popuwindd), Gravity.BOTTOM, 0, 0);
        myPopuwindown.setOutsideTouchable(false);
        TextView text_take_photo = (TextView) view.findViewById(R.id.text_take_photo);
        TextView text_pictuor = (TextView) view.findViewById(R.id.text_pictuor);
        TextView text_cancle = (TextView) view.findViewById(R.id.text_cancle);


        text_take_photo.setOnClickListener(this);
        text_pictuor.setOnClickListener(this);
        text_cancle.setOnClickListener(this);
    }

    /**
     * 打开基本资料界面
     */
    private void openBaseInfoActivity() {
        Intent baseInfoIntent = new Intent(EditResumeTwoActivity.this, BaseInfoActiovity.class);
        baseInfoIntent.putExtra("id", resume_id);
        startActivity(baseInfoIntent);
    }

    /**
     * 求职意向
     */
    private void openEditIntensionActivity() {

        Intent intent = new Intent(EditResumeTwoActivity.this, EditIntensionActivity.class);
        intent.putExtra("id", resume_id);
        startActivity(intent);

    }

    /**
     * 工作经历的条目点击事件
     */
    private AdapterView.OnItemClickListener onMyListViewItemClient = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(EditResumeTwoActivity.this, WorkExpActivity.class);
            intent.putExtra("params", "experienceItem");
            Bundle bundle = new Bundle();
            bundle.putSerializable("experitem", listWorkExp.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    /**
     * 教育背景的条目点击事件
     */
    private AdapterView.OnItemClickListener onMyEducationItemClient = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(EditResumeTwoActivity.this, EduItemActivity.class);
            intent.putExtra("id", educationList.get(position).getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
    };
    /**
     * 项目经历条目的点击事件
     */
    private AdapterView.OnItemClickListener onMyProgressItemClient = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(EditResumeTwoActivity.this, ProgresssItemActivity.class);
            intent.putExtra("id", progressList.get(position).getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };
    /**
     * 语言能力的条目的点击事件
     */
    private AdapterView.OnItemClickListener onMyLanguageItemClient = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(EditResumeTwoActivity.this, LangaugeItemActivity.class);
            intent.putExtra("id", langList.get(position).getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

    /**
     * 技能专长的条目点击事件
     */
    private AdapterView.OnItemClickListener onMySkillItemClient = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(EditResumeTwoActivity.this, AddSkillItemActivity.class);
            intent.putExtra("id", skillList.get(position).getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };
    /**
     * 证书的条目的点击事件
     */
    private AdapterView.OnItemClickListener onMyBookItemClient = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(EditResumeTwoActivity.this, AddBookItemActivity.class);
            intent.putExtra("id", bookList.get(position).getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

    /**
     * 其他信息的条目点击事件
     */
    private AdapterView.OnItemClickListener onMyOtherItemClient = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(EditResumeTwoActivity.this, AddOtherItemActivity.class);
            intent.putExtra("id", otherList.get(position).getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }
}
