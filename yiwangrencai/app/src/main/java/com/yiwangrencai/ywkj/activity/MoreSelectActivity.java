package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiwangrencai.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */

public class MoreSelectActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_more_select;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        companyScaleId = intent.getStringExtra("comkindId");
        companyScale = intent.getStringExtra("comkind");
        natrueBusinessId = intent.getStringExtra("employee_numId");
        natrueBusiness = intent.getStringExtra("employee_num");
        sufferId = intent.getStringExtra("work_yearId");
        suffer = intent.getStringExtra("work_year");
        issuedataId = intent.getStringExtra("updatetimeId");
        issuedata = intent.getStringExtra("updatetime");
        industry = intent.getStringExtra("industry");
        industryId = intent.getStringExtra("industryId");

        if (industry!=null&&!"".equals(industry)) {
            for (int i = 0; i < industry.split(",").length; i++) {
                industryList.add(industry.split(",")[i]);
                industryListId.add(industryId.split(",")[i]);

            }
        }
    }

    private FrameLayout fram_job_back;
    private RelativeLayout relativeJob;
    private RelativeLayout relativeScancle;
    private RelativeLayout relativeindustry;
    private RelativeLayout relativeExp;
    private RelativeLayout relativeTime;
    private TextView textindustry;
    private TextView textcompanyscanle;
    private TextView textnatrue;
    private TextView texttime;
    private TextView textissuetime;
    private TextView textmoresure;

    @Override
    public void initView() {
        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        relativeJob = (RelativeLayout) findViewById(R.id.relativeJob);
        relativeScancle = (RelativeLayout) findViewById(R.id.relativeScancle);
        relativeindustry = (RelativeLayout) findViewById(R.id.relativeindustry);
        relativeExp = (RelativeLayout) findViewById(R.id.relativeExp);
        relativeTime = (RelativeLayout) findViewById(R.id.relativeTime);

        textmoresure = (TextView) findViewById(R.id.textmoresure);
        textindustry = (TextView) findViewById(R.id.textindustry);
        textcompanyscanle = (TextView) findViewById(R.id.textcompanyscanle);
        textnatrue = (TextView) findViewById(R.id.textnatrue);
        texttime = (TextView) findViewById(R.id.texttime);
        textissuetime = (TextView) findViewById(R.id.textissuetime);

        fram_job_back.setOnClickListener(this);
        textmoresure.setOnClickListener(this);
        relativeJob.setOnClickListener(this);
        relativeScancle.setOnClickListener(this);
        relativeindustry.setOnClickListener(this);
        relativeExp.setOnClickListener(this);
        relativeTime.setOnClickListener(this);

        if (industry != null && !"".equals(industry)) {
            textindustry.setText(industry);
        }

        if (companyScale != null && !"".equals(companyScale)) {
            textcompanyscanle.setText(companyScale);
        }

        if (natrueBusiness != null && !"".equals(natrueBusiness)) {
            textnatrue.setText(natrueBusiness);
        }

        if (suffer != null && !"".equals(suffer)) {
            texttime.setText(suffer);
        }

        if (issuedata != null && !"".equals(issuedata)) {
            textissuetime.setText(issuedata);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back://返回
                finish();
                break;
            case R.id.relativeJob://行业
                Intent intentIndustry = new Intent(MoreSelectActivity.this, EducationActivity.class);
                intentIndustry.putExtra("education", "industry_more");
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("list", (ArrayList<String>) industryList);
                bundle.putStringArrayList("listId", (ArrayList<String>) industryListId);
                intentIndustry.putExtras(bundle);
                startActivityForResult(intentIndustry, 62);
                break;
            case R.id.relativeScancle://公司规模
                Intent intentScancle = new Intent(MoreSelectActivity.this, EducationActivity.class);
                intentScancle.putExtra("education", "companyScale");
                intentScancle.putExtra("company_name", companyScale);
                startActivityForResult(intentScancle, 43);
                break;
            case R.id.relativeindustry://公司性质
                Intent intentnartrue = new Intent(MoreSelectActivity.this, EducationActivity.class);
                intentnartrue.putExtra("education", "natrueBusiness");
                intentnartrue.putExtra("company_name", natrueBusiness);
                startActivityForResult(intentnartrue, 42);
                break;
            case R.id.relativeExp://工作经验
                Intent intentExp = new Intent(MoreSelectActivity.this, EducationActivity.class);
                intentExp.putExtra("education", "base_info_suffer");
                intentExp.putExtra("company_name", suffer);
                startActivityForResult(intentExp, 103);
                break;
            case R.id.relativeTime://发布时间
                Intent intentIssueTime = new Intent(MoreSelectActivity.this, EducationActivity.class);
                intentIssueTime.putExtra("education", "time");
                intentIssueTime.putExtra("company_name", issuedata);
                startActivityForResult(intentIssueTime, 00);
                break;
            case R.id.textmoresure:
                Intent intent = new Intent(MoreSelectActivity.this, SearchResultActivity.class);
                intent.putExtra("industryId", industryId);
                intent.putExtra("industry", industry);
                intent.putExtra("companyScaleId", companyScaleId);
                intent.putExtra("companyScale", companyScale);
                intent.putExtra("natrueBusinessId", natrueBusinessId);
                intent.putExtra("natrueBusiness", natrueBusiness);
                intent.putExtra("sufferId", sufferId);
                intent.putExtra("suffer", suffer);
                intent.putExtra("issuedataId", issuedataId);
                intent.putExtra("issuedata", issuedata);
                MoreSelectActivity.this.setResult(60, intent);
                finish();
                break;
        }
    }

    /**
     * 点击进入下一个界面返回的值
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    String industry;
    String industryId;
    String companyScale = "";
    String companyScaleId;
    String natrueBusiness = "";
    String natrueBusinessId;
    String suffer = "";
    String sufferId;
    String issuedata = "";
    String issuedataId;
    List<String> industryList = new ArrayList();
    List<String> industryListId = new ArrayList();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            switch (requestCode) {
                case 62://行业
                    industryList = data.getExtras().getStringArrayList("list");
                    industryListId = data.getExtras().getStringArrayList("listId");
                    industry = "";
                    industryId = "";
                    Log.i("industryList", industryList + "");
                    Log.i("industryListId", industryListId + "");
                    for (int i = 0; i < industryList.size(); i++) {
                        if (industryList.size() == 1) {
                            industry = industryList.get(i);
                            industryId = industryListId.get(i);
                        } else {
                            if (i == industryList.size() - 1) {
                                industry += industryList.get(i);
                                industryId += industryListId.get(i);
                            } else {
                                industry += industryList.get(i) + ",";
                                industryId += industryListId.get(i) + ",";
                            }
                        }
                    }
                    Log.i("industry", industry + "");
                    Log.i("industryId", industryId + "");
                    textindustry.setText(industry);
                    break;
                case 43://公司规模
                    companyScale = data.getExtras().getString("companyScale");
                    companyScaleId = data.getExtras().getString("companyScaleId");
                    textcompanyscanle.setText(companyScale);
                    break;
                case 42://公司性质
                    natrueBusiness = data.getExtras().getString("natrueBusiness");
                    natrueBusinessId = data.getExtras().getString("natrueBusinessId");
                    textnatrue.setText(natrueBusiness);
                    break;
                case 103://工作经验
                    suffer = data.getExtras().getString("suffer");
                    sufferId = data.getExtras().getString("sufferId");
                    texttime.setText(suffer);
                    break;
                case 00://发布时间
                    issuedata = data.getExtras().getString("issuedata");
                    issuedataId = data.getExtras().getString("issuedataId");
                    textissuetime.setText(issuedata);
                    break;
            }
        }
    }
}
