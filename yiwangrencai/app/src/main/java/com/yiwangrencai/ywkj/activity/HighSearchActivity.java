package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiwangrencai.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */

public class HighSearchActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.high_search_activity;
    }

    @Override
    public void initData() {

    }

    private RelativeLayout searchInputWork;
    private RelativeLayout searchSelectIndustry;
    private RelativeLayout searchSelectArea;
    private RelativeLayout searchSelectTime;
    private TextView textViewWork;
    private TextView textviewsearchhigh;
    private TextView textindustry;
    private TextView textArea;
    private TextView textissuetime;
    private FrameLayout iv_back_basic;
    private EditText edit_left;
    private EditText edit_right;

    @Override
    public void initView() {
        searchInputWork = (RelativeLayout) findViewById(R.id.searchInputWork);
        searchSelectIndustry = (RelativeLayout) findViewById(R.id.searchSelectIndustry);
        searchSelectArea = (RelativeLayout) findViewById(R.id.searchSelectArea);
        searchSelectTime = (RelativeLayout) findViewById(R.id.searchSelectTime);
        textViewWork = (TextView) findViewById(R.id.textViewWork);
        textindustry = (TextView) findViewById(R.id.textindustry);
        textissuetime = (TextView) findViewById(R.id.textissuetime);
        textArea = (TextView) findViewById(R.id.textArea);
        edit_left = (EditText) findViewById(R.id.edit_left);
        edit_right = (EditText) findViewById(R.id.edit_right);

        iv_back_basic = (FrameLayout) findViewById(R.id.iv_back_basic);
        textviewsearchhigh = (TextView) findViewById(R.id.textviewsearchhigh);
        searchInputWork.setOnClickListener(this);
        searchSelectIndustry.setOnClickListener(this);
        searchSelectArea.setOnClickListener(this);
        searchSelectTime.setOnClickListener(this);
        iv_back_basic.setOnClickListener(this);
        textviewsearchhigh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_basic://返回
                finish();
                break;
            case R.id.searchInputWork://职位
                Intent intentWork = new Intent(HighSearchActivity.this, NameActivity.class);
                intentWork.putExtra("params", "search");
                intentWork.putExtra("mobile", work);
                startActivityForResult(intentWork, 70);
                break;
            case R.id.searchSelectIndustry://行业
                Intent intent = new Intent(HighSearchActivity.this, EducationActivity.class);
                intent.putExtra("education", "industry_more");
                intent.putExtra("company_name", "");
                Bundle bundle=new Bundle();
                bundle.putStringArrayList("list", (ArrayList<String>) industrys);
                bundle.putStringArrayList("listId", (ArrayList<String>) industryIds);
                intent.putExtras(bundle);
                startActivityForResult(intent, 62);
                break;
            case R.id.searchSelectArea://地区
                Intent intentArea = new Intent(HighSearchActivity.this, LocationActivity.class);
                intentArea.putExtra("params", "");
                Bundle bundles=new Bundle();
                bundles.putStringArrayList("jobareaaName", (ArrayList<String>) listArea);
                bundles.putStringArrayList("jobareaaId", (ArrayList<String>) listAreaId);
                intentArea.putExtras(bundles);
                startActivityForResult(intentArea, 9);
                break;
            case R.id.searchSelectTime://发布时间
                Intent intentTime = new Intent(HighSearchActivity.this, EducationActivity.class);
                intentTime.putExtra("education", "time");
                intentTime.putExtra("company_name", issuedata);
                startActivityForResult(intentTime, 00);
                break;
            case R.id.textviewsearchhigh://搜索按钮
                // searchJob();//搜索职位
                String salarylittle = edit_left.getText().toString().trim();
                String salaryhigh = edit_right.getText().toString().trim();
                Intent intentSearch = new Intent(HighSearchActivity.this, SearchResultActivity.class);
                intentSearch.putExtra("keyword", work);
                intentSearch.putExtra("industryname", industry);
                intentSearch.putExtra("location", location);
                intentSearch.putExtra("salarylittle", salarylittle);
                intentSearch.putExtra("salaryhigh", salaryhigh);
                intentSearch.putExtra("release_date", issuedata);
                intentSearch.putExtra("industry", industryId);
                intentSearch.putExtra("locationId", locationId);
                intentSearch.putExtra("release_dateId", issuedataId);
                intentSearch.putExtra("params", "highSearch");
                startActivity(intentSearch);
                break;
        }

    }

    String work;
    String industry;
    String industryId;
    String location;
    String locationId="";
    String issuedata="";
    String issuedataId;
    List<String> industrys=new ArrayList<>();
    List<String>  industryIds=new ArrayList<>();
    private  List<String> listArea=new ArrayList();
    private  List<String> listAreaId=new ArrayList();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            switch (requestCode) {
                case 70:
                    work = data.getExtras().getString("search");
                    textViewWork.setText(work);
                    break;
                case 62:
                    industry="";
                    industryId="";

                    industrys = (List<String>) data.getExtras().getSerializable("list");
                    industryIds = (List<String>) data.getExtras().getSerializable("listId");
                    if (industrys.size()==0){
                        textindustry.setText("请选择行业");
                    }else {
                        for (int i = 0; i < industrys.size(); i++) {
                            if ( i == industryIds.size()-1) {
                                industry += industrys.get(i);
                                industryId += industryIds.get(i);
                            } else {
                                industry += industrys.get(i) + ",";
                                industryId += industryIds.get(i) + ",";
                            }
                        }
                        textindustry.setText(industry);
                    }
                    break;
                case 9:
                    location = "";
                    locationId = "";
                    listArea=data.getExtras().getStringArrayList("listArea");
                    listAreaId=data.getExtras().getStringArrayList("listAreaId");
                    for (int i=0;i<listArea.size();i++){
                        if ( i == listArea.size()-1) {
                            location += listArea.get(i);
                            locationId += listAreaId.get(i);
                        } else {
                            location += listArea.get(i) + ",";
                            locationId += listAreaId.get(i) + ",";
                        }
                    }
                    textArea.setText(location);
                    break;
                case 00:
                    issuedata = data.getExtras().getString("issuedata");
                    issuedataId = data.getExtras().getString("issuedataId");
                    textissuetime.setText(issuedata);
                    break;
            }
        }
    }
}
