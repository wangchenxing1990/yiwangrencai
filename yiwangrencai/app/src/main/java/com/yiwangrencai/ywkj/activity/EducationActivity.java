package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.adapter.MyEducationAdapter;
import com.yiwangrencai.ywkj.adapter.MyEducationAdapterTwo;
import com.yiwangrencai.ywkj.adapter.MyLangOptionAdapter;
import com.yiwangrencai.ywkj.bean.LangOptionBean;
import com.yiwangrencai.ywkj.tools.InitDatas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * Created by Administrator on 2017/4/24.
 */

public class EducationActivity extends BaseActiviyt implements View.OnClickListener {
    private ListView education;
    private TextView tv_province;
    private FrameLayout backEducation;
    private String edusuffer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_area_code;
    }

    @Override
    public void initData() {

    }

    List<String> educationData = new ArrayList<>();
    List<String> educationId = new ArrayList<>();
    List<String> expersionDatas = new ArrayList<>();
    List<String> expersionDataID = new ArrayList<>();
    List<String> salaryDatas = new ArrayList<String>();
    List<String> salaryDataID = new ArrayList<String>();
    List<String> natrueBusiness = new ArrayList<>();
    List<String> natrueBusinessID = new ArrayList<>();
    List<String> companyScale = new ArrayList<>();
    List<String> companyScaleID = new ArrayList<>();
    List<String> educationDataBack = new ArrayList<>();
    List<String> educationIdBack = new ArrayList<>();
    List<String> langueDatas = new ArrayList<>();
    List<String> langDatasId = new ArrayList<>();
    List<String> otherDatas = new ArrayList<>();
    List<String> industryDatas = new ArrayList<>();
    List<String> industryDataID = new ArrayList<>();
    List<String> issueTimeDatas = new ArrayList<>();
    List<String> issueTimeDataId = new ArrayList<>();
    List<String> ageDatas = new ArrayList<>();
    List<String> ageDatasId = new ArrayList<>();
    List<String> partJob = new ArrayList<>();
    List<String> partJobId = new ArrayList<>();
    List<LangOptionBean> optionList;
    private MyEducationAdapter adapter;
    TagContainerLayout textSelectWork;
    TextView text_save;
    String params;
    String save;
    String industry1;
    String intention;
    String intention_name;
    String company_name;

    @Override
    public void initView() {
        Intent intent = getIntent();
        edusuffer = intent.getStringExtra("education");
        params = intent.getStringExtra("params");
        save = intent.getStringExtra("save");
        industry1 = intent.getStringExtra("industry1");
        intention_name = intent.getStringExtra("partJob");
        intention = intent.getStringExtra("partJobId");
        company_name = intent.getStringExtra("company_name");



        education = (ListView) findViewById(R.id.list_view);
        tv_province = (TextView) findViewById(R.id.tv_province);
        text_save = (TextView) findViewById(R.id.text_save);
        backEducation = (FrameLayout) findViewById(R.id.iv_back_location);
        textSelectWork = (TagContainerLayout) findViewById(R.id.textSelectWork);

        backEducation.setOnClickListener(this);
        text_save.setOnClickListener(this);

        if (edusuffer.equals("education")) {
            educationData = InitDatas.edcationData();
            educationId = InitDatas.edcationDataId();
            tv_province.setText("学历");
            text_save.setVisibility(View.INVISIBLE);
            education.setAdapter(new MyEducationAdapterTwo(educationData,company_name));
        } else if (edusuffer.equals("suffer")) {

            expersionDatas = InitDatas.expersionData();
            expersionDataID = InitDatas.expersionDataID();
            tv_province.setText("相关工作经验");
            text_save.setVisibility(View.INVISIBLE);
            education.setAdapter(new MyEducationAdapterTwo(expersionDatas,company_name));

        } else if (edusuffer.equals("intensionsaraly")) {

            salaryDatas = InitDatas.saralyDatas();
            salaryDataID = InitDatas.saralyDataID();
            tv_province.setText("期望薪资");
            text_save.setVisibility(View.INVISIBLE);
            education.setAdapter(new MyEducationAdapterTwo(salaryDatas,company_name));
        } else if (edusuffer.equals("educationBackground")) {
            educationIdBack = InitDatas.edcationDataId();
            educationDataBack = InitDatas.edcationData();
            text_save.setVisibility(View.INVISIBLE);
            tv_province.setText("学历");
            education.setAdapter(new MyEducationAdapterTwo(educationDataBack,company_name));

        } else if (edusuffer.equals("natrueBusiness")) {
            natrueBusiness = InitDatas.natrueBusiness();
            natrueBusinessID = InitDatas.natrueBusinessId();
            text_save.setVisibility(View.INVISIBLE);
            tv_province.setText("公司性质");
            education.setAdapter(new MyEducationAdapterTwo(natrueBusiness,company_name));
        } else if (edusuffer.equals("companyScale")) {
            companyScale = InitDatas.companyScale();
            companyScaleID = InitDatas.companyScaleID();
            tv_province.setText("公司规模");
            text_save.setVisibility(View.INVISIBLE);
            education.setAdapter(new MyEducationAdapterTwo(companyScale,company_name));
        } else if (edusuffer.equals("base_info")) {
            educationData = InitDatas.edcationData();
            educationId = InitDatas.edcationDataId();
            tv_province.setText("学历");
            text_save.setVisibility(View.INVISIBLE);
            education.setAdapter(new MyEducationAdapterTwo(educationData,company_name));
        } else if (edusuffer.equals("base_info_suffer")) {
            expersionDatas = InitDatas.expersionData();
            expersionDataID = InitDatas.expersionDataID();
            tv_province.setText("相关工作经验");
            text_save.setVisibility(View.INVISIBLE);
            education.setAdapter(new MyEducationAdapterTwo(expersionDatas,company_name));
        } else if (edusuffer.equals("langue")) {
            langueDatas = InitDatas.langData();
            langDatasId = InitDatas.langDataID();
            tv_province.setText("语种");
            education.setAdapter(new MyEducationAdapterTwo(langueDatas,company_name));
        } else if (edusuffer.equals("langOption")) {
            Bundle optionlist = intent.getExtras();
            optionList = (List<LangOptionBean>) optionlist.getSerializable("optionlist");
            tv_province.setText("等级");
            education.setAdapter(new MyLangOptionAdapter(optionList));
        } else if (edusuffer.equals("other")) {
            otherDatas = InitDatas.otherData();
            tv_province.setText("主题");
            education.setAdapter(new MyEducationAdapterTwo(otherDatas,company_name));
        } else if (edusuffer.equals("industry")) {
            industryDatas = InitDatas.industryData();
            industryDataID = InitDatas.industryDataId();
            tv_province.setText("行业");

            education.setAdapter(new MyEducationAdapterTwo(industryDatas,company_name));
            if ("experiencesss".equals(params)) {
                textSelectWork.setVisibility(View.INVISIBLE);
                text_save.setVisibility(View.INVISIBLE);
            }else{
                text_save.setVisibility(View.INVISIBLE);
                textSelectWork.setVisibility(View.INVISIBLE);
            }

        } else if(edusuffer.equals("industry_more")){
            list=intent.getExtras().getStringArrayList("list");
            listId=intent.getExtras().getStringArrayList("listId");
            industryDatas = InitDatas.industryData();
            industryDataID = InitDatas.industryDataId();
            text_save.setVisibility(View.VISIBLE);
            textSelectWork.setVisibility(View.VISIBLE);
             education.setAdapter(new MyEducationAdapter(industryDatas));
            textSelectWork.setTags(list);
             textSelectWork.setOnTagClickListener(new TagView.OnTagClickListener() {
                    @Override
                    public void onTagClick(int position, String text) {
                        list.remove(position);
                        listId.remove(position);
                        textSelectWork.setTags(list);
                    }

                    @Override
                    public void onTagLongClick(int position, String text) {

                    }

                    @Override
                    public void onTagCrossClick(int position) {
                        list.remove(position);
                        listId.remove(position);
                        textSelectWork.setTags(list);
                    }
                });

        }else if (edusuffer.equals("time")) {
            issueTimeDatas = InitDatas.issueTimeData();
            issueTimeDataId = InitDatas.issueTimeDataId();
            tv_province.setText("发布时间");
            text_save.setVisibility(View.INVISIBLE);
            education.setAdapter(new MyEducationAdapterTwo(issueTimeDatas,company_name));
        } else if (edusuffer.equals("age")) {
            ageDatas = InitDatas.ageDatas();
            ageDatasId = InitDatas.ageDatasId();
            tv_province.setText("选择年龄");
             text_save.setVisibility(View.INVISIBLE);
            education.setAdapter(new MyEducationAdapterTwo(ageDatas,company_name));
        }else if(edusuffer.equals("partJob")){

            partJob=InitDatas.partJobDatas();
            partJobId=InitDatas.partJobDatasId();
            tv_province.setText("兼职类别");
            text_save.setVisibility(View.VISIBLE);
            education.setAdapter(new MyEducationAdapter(partJob));
            textSelectWork.setVisibility(View.VISIBLE);
            if (intention_name!=null&&!"".equals(intention_name)){
                String [] str=intention_name.split(",");
                String [] strId=intention.split(",");
                for (int i=0;i<str.length;i++){
                    list.add(str[i]);
                    listId.add(strId[i]);
                }
                textSelectWork.setTags(list);
            }
            textSelectWork.setOnTagClickListener(new TagView.OnTagClickListener() {
                @Override
                public void onTagClick(int position, String text) {
                    list.remove(position);
                    listId.remove(position);
                    textSelectWork.setTags(list);
                }

                @Override
                public void onTagLongClick(int position, String text) {

                }

                @Override
                public void onTagCrossClick(int position) {
                    list.remove(position);
                    listId.remove(position);
                    textSelectWork.setTags(list);
                }
            });

        }

        //条目点击事件
        education.setOnItemClickListener(myOnItemClickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_location:
                finish();
                break;
            case R.id.text_save://保存
                 if ("partJob".equals(edusuffer)){
                     Intent intent = new Intent();
                     Bundle bundle = new Bundle();
                     bundle.putSerializable("list", (Serializable) list);
                     bundle.putSerializable("listId", (Serializable) listId);
                     intent.putExtras(bundle);
                     EducationActivity.this.setResult(62, intent);
                 }else if("industry_more".equals(edusuffer)){
                     Intent intent = new Intent();
                     Bundle bundle = new Bundle();
                     bundle.putSerializable("list", (Serializable) list);
                     bundle.putSerializable("listId", (Serializable) listId);
                     intent.putExtras(bundle);
                     EducationActivity.this.setResult(62, intent);
                 }else{
                     Intent intent = new Intent();
                     Bundle bundle = new Bundle();
                     bundle.putSerializable("list", (Serializable) list);
                     bundle.putSerializable("listId", (Serializable) listId);
                     intent.putExtras(bundle);
                     EducationActivity.this.setResult(62, intent);
                 }

                finish();
                break;
        }
    }

    List<String> industry = new LinkedList<>();
    List<String> list = new ArrayList<>();
    List<String> listId = new ArrayList<>();
    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (edusuffer.equals("education")) {
                Intent intent = new Intent();
                intent.putExtra("education", educationData.get(position));
                intent.putExtra("educationId", educationId.get(position));
                EducationActivity.this.setResult(15, intent);
                finish();
            } else if (edusuffer.equals("suffer")) {
                Intent intent = new Intent();
                intent.putExtra("suffer", expersionDatas.get(position));
                intent.putExtra("sufferId", expersionDataID.get(position));
                EducationActivity.this.setResult(16, intent);
                finish();
            } else if (edusuffer.equals("intensionsaraly")) {
                Intent intent = new Intent();
                intent.putExtra("intensionsaraly", salaryDatas.get(position));
                intent.putExtra("intensionsaralyId", salaryDataID.get(position));
                EducationActivity.this.setResult(20, intent);
                finish();
            } else if (edusuffer.equals("educationBackground")) {
                Intent intent = new Intent();
                intent.putExtra("educationBackground", educationDataBack.get(position));
                intent.putExtra("educationBackgroundId", educationIdBack.get(position));
                EducationActivity.this.setResult(32, intent);
                finish();
            } else if (edusuffer.equals("natrueBusiness")) {
                Intent intent = new Intent();
                intent.putExtra("natrueBusiness", natrueBusiness.get(position));
                intent.putExtra("natrueBusinessId", natrueBusinessID.get(position));
                EducationActivity.this.setResult(42, intent);
                finish();
            } else if (edusuffer.equals("companyScale")) {
                Intent intent = new Intent();
                intent.putExtra("companyScale", companyScale.get(position));
                intent.putExtra("companyScaleId", companyScaleID.get(position));
                EducationActivity.this.setResult(43, intent);
                finish();
            } else if (edusuffer.equals("base_info")) {
                Intent intent = new Intent();
                intent.putExtra("education", educationData.get(position));
                intent.putExtra("educationId", educationId.get(position));
                EducationActivity.this.setResult(102, intent);
                finish();
            } else if (edusuffer.equals("base_info_suffer")) {
                Intent intent = new Intent();
                intent.putExtra("suffer", expersionDatas.get(position));
                intent.putExtra("sufferId", expersionDataID.get(position));
                EducationActivity.this.setResult(103, intent);
                finish();
            } else if (edusuffer.equals("langue")) {
                Intent intent = new Intent();
                intent.putExtra("languedata", langueDatas.get(position));
                intent.putExtra("languedataId", langDatasId.get(position));
                EducationActivity.this.setResult(20, intent);
                finish();
            } else if (edusuffer.equals("langOption")) {
                Intent intent = new Intent();
                intent.putExtra("langoption", optionList.get(position).getOpt_name());
                intent.putExtra("langoptionId", optionList.get(position).getOpt_id());
                EducationActivity.this.setResult(60, intent);
                finish();
            } else if (edusuffer.equals("other")) {
                if (otherDatas.size() == position + 1) {
                    Intent intent = new Intent(EducationActivity.this, NameActivity.class);
                    intent.putExtra("params", "other");
                    /// intent.putExtra("moble",otherDatas.get);
                    startActivityForResult(intent, 200);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("other", otherDatas.get(position));
                    //intent.putExtra("langoptionId", optionList.get(position).getOpt_id());
                    EducationActivity.this.setResult(60, intent);
                    finish();
                }
            } else if ("industry".equals(edusuffer)) {

                if ("experiencesss".equals(params)){
                    Intent intent=new Intent();
                    intent.putExtra("list",industryDatas.get(position));
                    intent.putExtra("listId",industryDataID.get(position));
                    EducationActivity.this.setResult(62,intent);
                    finish();
                }else{
                    Intent intent=new Intent();
                    intent.putExtra("list",industryDatas.get(position));
                    intent.putExtra("listId",industryDataID.get(position));
                    EducationActivity.this.setResult(62,intent);
                    finish();
                }

            }else if("industry_more".equals(edusuffer)){
                if (list.size() < 5) {
                        if (list.contains(industryDatas.get(position))) {
                            Toast.makeText(EducationActivity.this, "不能重复添加", Toast.LENGTH_SHORT).show();
                        } else {
                            list.add(industryDatas.get(position));
                            listId.add(industryDataID.get(position));
                        }
                    } else {
                        Toast.makeText(EducationActivity.this, "最多只能添加5个", Toast.LENGTH_SHORT).show();
                    }

                textSelectWork.setTags(list);
            } else if ("time".equals(edusuffer)) {
                Intent intent = new Intent();
                intent.putExtra("issuedata", issueTimeDatas.get(position));
                intent.putExtra("issuedataId", issueTimeDataId.get(position));
                EducationActivity.this.setResult(00, intent);
                finish();
            } else if ("age".equals(edusuffer)) {
                Intent intent = new Intent();
                intent.putExtra("ageDatas", ageDatas.get(position));
                intent.putExtra("ageDatasId", ageDatasId.get(position));
                EducationActivity.this.setResult(19, intent);
                finish();
            }else if("partJob".equals(edusuffer)){
                if (list.size()<5){
                    if (list.contains(partJob.get(position))){
                        Toast.makeText(EducationActivity.this,"不能重复添加",Toast.LENGTH_SHORT).show();
                    }else{
                        list.add(partJob.get(position));
                        listId.add(partJobId.get(position));
                    }
                }else{
                    Toast.makeText(EducationActivity.this,"最多只能添加5个",Toast.LENGTH_SHORT).show();
                }
                textSelectWork.setTags(list);
            }
        }

//
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            switch (requestCode) {
                case 200:
                    String other = data.getExtras().getString("other");
                    Intent intent = new Intent();
                    intent.putExtra("other", other);
                    EducationActivity.this.setResult(200, intent);
                    finish();
                    break;
            }
        }
    }
}
