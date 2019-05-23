package com.yiwangrencai.ywkj.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.yiwangrencai.ywkj.adapter.MyJobBrowseAdapter;
import com.yiwangrencai.ywkj.adapter.MyLookMeAdapter;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.fragment.CompanyFragment;
import com.yiwangrencai.ywkj.fragment.JobFragment;
import com.yiwangrencai.ywkj.view.RecycleViewDivider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/15.
 */

public class BrowserActivityTwo extends FragmentActivity implements View.OnClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broesing_history_two);
        initData();
        initView();
    }


    String api_token;
    List<Fragment> fragments = new ArrayList<>();

    public void initData() {
        fragments.add(new JobFragment());
        fragments.add(new CompanyFragment());

        SharedPreferences share = getSharedPreferences("Activity", Context.MODE_PRIVATE);
        api_token = share.getString("api_token", "");

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_layout,fragments.get(0))
                .show(fragments.get(0)).commit();
    }


    FrameLayout frame_layout;
    LinearLayout linear_job_browse;
    LinearLayout linear_company_browse;
    TextView textjobbrowse;
    TextView textcompanybrowse;
    View view_left;
    View view_right;
    View loadingView;
    View successView;

    public void initView() {

        FrameLayout fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        linear_job_browse = (LinearLayout) findViewById(R.id.linear_job_browse);
        linear_company_browse = (LinearLayout) findViewById(R.id.linear_company_browse);
        textjobbrowse = (TextView) findViewById(R.id.textjobbrowse);
        textcompanybrowse = (TextView) findViewById(R.id.textcompanybrowse);

        view_left = findViewById(R.id.line_left);
        view_right = findViewById(R.id.line_right);

        fram_job_back.setOnClickListener(this);
        linear_job_browse.setOnClickListener(this);
        linear_company_browse.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back:
                finish();
                break;
            case R.id.linear_job_browse:
                textjobbrowse.setTextColor(getResources().getColor(R.color.foot_text_color_green));
                textcompanybrowse.setTextColor(getResources().getColor(R.color.home_text_color));
                view_left.setBackgroundColor(getResources().getColor(R.color.foot_text_color_green));
                view_right.setBackgroundColor(getResources().getColor(R.color.view_line_f0));
                switchFragment(0);
                break;
            case R.id.linear_company_browse:
                textjobbrowse.setTextColor(getResources().getColor(R.color.home_text_color));
                textcompanybrowse.setTextColor(getResources().getColor(R.color.foot_text_color_green));
                view_left.setBackgroundColor(getResources().getColor(R.color.view_line_f0));
                view_right.setBackgroundColor(getResources().getColor(R.color.foot_text_color_green));
                switchFragment(1);
                break;
        }
    }

    int oldIndex;
    private void switchFragment(int targIndex) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragments.get(targIndex).isAdded()) {
          if (targIndex!=oldIndex) {
              transaction.hide(fragments.get(oldIndex)).show(fragments.get(targIndex));
          }
        } else {
            transaction.add(R.id.frame_layout, fragments.get(targIndex)).show(fragments.get(targIndex)).hide(fragments.get(oldIndex));
        }
        transaction.commit();
        oldIndex=targIndex;
    }

}
