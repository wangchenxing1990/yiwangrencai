package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.adapter.MyReceiveResumeAdapter;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.fragment.CompanyBriefFragment;
import com.yiwangrencai.ywkj.fragment.FindingWorkFragment;
import com.yiwangrencai.ywkj.view.RecycleViewDivider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;

//import com.baidu.platform.comapi.map.F;
//import com.squareup.okhttp.Address;
//import com.tencent.mm.opensdk.modelbiz.AddCardToWXCardPackage;

/**
 * Created by Administrator on 2017/5/10.
 */
public class CompanydetailActivityTwo extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail_two);
        initData();
        initView();
    }

    String company_name;
    String api_token;
    int code;
    private List<Fragment> fragments=new ArrayList();
    String com_id;
    String id;
    public void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        com_id = intent.getStringExtra("com_id");
        SharedPreferences sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("id",id);
        editor.putString("com_id",com_id);
        editor.commit();
        Log.i("com_idcom_id",com_id+"com_id");
        fragments.add(new CompanyBriefFragment());
        fragments.add(new FindingWorkFragment());
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout,fragments.get(0)).show(fragments.get(0)).commit();

    }

    FrameLayout iv_back_basic;
    FrameLayout frame_layout;
    LinearLayout linear_company_brief;
    LinearLayout linear_job_invite;
    TextView textbfief;
    TextView textcompanyjob;
    View view_left;
    View view_right;

    public void initView() {
        iv_back_basic = (FrameLayout) findViewById(R.id.iv_back_basic);
        linear_company_brief = (LinearLayout) findViewById(R.id.linear_company_brief);
        linear_job_invite = (LinearLayout) findViewById(R.id.linear_job_invite);

        view_left =  findViewById(R.id.view_left);
        view_right =  findViewById(R.id.view_right);

        textbfief = (TextView) findViewById(R.id.textbfief);
        textcompanyjob = (TextView) findViewById(R.id.textcompanyjob);
        frame_layout = (FrameLayout) findViewById(R.id.frame_layout);

        iv_back_basic.setOnClickListener(this);
        linear_company_brief.setOnClickListener(this);
        linear_job_invite.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_basic://返回
                finish();
                break;
            case R.id.linear_company_brief://企业简介
                // Toast.makeText(this, "点击了企业简介", Toast.LENGTH_SHORT).show();
                textbfief.setTextColor(getResources().getColor(R.color.foot_text_color_green));
                textcompanyjob.setTextColor(getResources().getColor(R.color.color_text));

                view_left.setBackgroundColor(getResources().getColor(R.color.foot_text_color_green));
//                view_right.setBackgroundColor(getResources().getColor(R.color.color_text));
                view_right.setVisibility(View.INVISIBLE);
                view_left.setVisibility(View.VISIBLE);

                swithFragment(0);
                break;
            case R.id.linear_job_invite://在招职位

                textbfief.setTextColor(getResources().getColor(R.color.color_text));
                textcompanyjob.setTextColor(getResources().getColor(R.color.foot_text_color_green));
                view_left.setVisibility(View.INVISIBLE);
                view_right.setVisibility(View.VISIBLE);
                view_right.setBackgroundColor(getResources().getColor(R.color.foot_text_color_green));
                swithFragment(1);
                break;


        }
    }

    int oldIndex;
    private void swithFragment(int targIndex){

        FragmentTransaction transactioln=getSupportFragmentManager().beginTransaction();
        if (fragments.get(targIndex).isAdded()){

            if (targIndex==oldIndex){
                transactioln.show(fragments.get(oldIndex));
            }else{
                transactioln.show(fragments.get(targIndex)).hide(fragments.get(oldIndex));
            }
        }else{
            transactioln.add(R.id.frame_layout,fragments.get(targIndex)).show(fragments.get(targIndex)).hide(fragments.get(oldIndex));
        }

        transactioln.commit();
        oldIndex=targIndex;
    }

}
