package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
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
//import com.baidu.platform.comapi.map.F;
//import com.squareup.okhttp.Address;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
//import com.tencent.mm.opensdk.modelbiz.AddCardToWXCardPackage;
import com.yiwangrencai.R;


import com.yiwangrencai.ywkj.adapter.MyReceiveResumeAdapter;
import com.yiwangrencai.ywkj.adapter.MyRecyclerViewReceiveResumeAdapter;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.RecycleViewDivider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;

/**
 * Created by Administrator on 2017/5/10.
 */
public class CompanydetailActivity extends BaseActiviyt implements View.OnClickListener {


    @Override
    public int getLayoutId() {
        return R.layout.activity_company_detail;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    parsData();//解析数据
                    break;
                case 110:
                    Toast.makeText(CompanydetailActivity.this, "联网超时，请重试", Toast.LENGTH_SHORT).show();
                    break;
                case 120:
                    parsCompanyJob();//解析公司所有职位的数据
                    break;
                case 130:
                    parsRefreshCompanyJob();//解析下拉刷新获取的数据
                    break;
                case 300:
                    parsLoadNewData();//上垃加载更多的数据
                    break;
                case 150:
                    parsRefreshNewData();//解析下拉刷新的数据
                    break;
                case ONE_PAGE:
                    parsOnePageData();//解析当前数据是第一个页面的数据
                    break;


            }
        }
    };


    /**
     * 解析下拉刷新的数据n
     */
    private void parsRefreshNewData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codes = json.getString("code");
            if ("1".equals(codes)) {
                JSONArray data = json.getJSONArray("data");
                allJobList.clear();
                for (int i = 0; i < data.size(); i++) {
                    CompanyJobBean companyBean = new CompanyJobBean();
                    JSONObject jsondata = data.getJSONObject(i);
                    companyBean.setId(jsondata.getString("id"));
                    companyBean.setCom_id(jsondata.getString("com_id"));
                    companyBean.setJob_title(jsondata.getString("job_title"));
                    companyBean.setUpdatetime(jsondata.getString("updatetime"));
                    companyBean.setJob_status(jsondata.getString("job_status"));
                    companyBean.setLocation_name(jsondata.getString("location_name"));
                    companyBean.setSalary(jsondata.getString("salary"));
                    companyBean.setEducation_name(jsondata.getString("education_name"));
                    companyBean.setWork_year_name(jsondata.getString("work_year_name"));
                    companyBean.setCompany_name(jsondata.getString("company_name"));
                    companyBean.setJob_status_name(jsondata.getString("job_status_name"));
                    companyBean.setIs_urgent(jsondata.getString("is_urgent"));

                    allJobList.add(companyBean);

                }
                recyclerViewReceiveResumeAdapter.notifyDataSetChanged();
                xrefreshview.stopRefresh();
                xrefreshview.setLoadComplete(false);
            } else {

            }
        } else {

        }
    }

    /**
     * 解析下拉刷新获取的数据
     */
    private void parsRefreshCompanyJob() {
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(prev_page_url)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncoding.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("112124124124", str);
                handler.sendEmptyMessage(150);
            }
        });

    }

    /**
     * 上垃加载更多的数据
     */
    private void parsLoadNewData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            if (codes.equals("1")) {
                JSONArray data = json.getJSONArray("data");

                for (int i = 0; i < data.size(); i++) {
                    CompanyJobBean companyBean = new CompanyJobBean();
                    JSONObject jsondata = data.getJSONObject(i);

                    companyBean.setId(jsondata.getString("id"));
                    companyBean.setCom_id(jsondata.getString("com_id"));
                    companyBean.setJob_title(jsondata.getString("job_title"));
                    companyBean.setUpdatetime(jsondata.getString("updatetime"));
                    companyBean.setJob_status(jsondata.getString("job_status"));
                    companyBean.setLocation_name(jsondata.getString("location_name"));
                    companyBean.setSalary(jsondata.getString("salary"));
                    companyBean.setEducation_name(jsondata.getString("education_name"));
                    companyBean.setWork_year_name(jsondata.getString("work_year_name"));
                    companyBean.setCompany_name(jsondata.getString("company_name"));
                    companyBean.setJob_status_name(jsondata.getString("job_status_name"));
                    companyBean.setIs_urgent(jsondata.getString("is_urgent"));

                    allJobList.add(companyBean);

                }
                recyclerViewReceiveResumeAdapter.notifyDataSetChanged();
                xrefreshview.stopLoadMore();
            } else {
                Toast.makeText(CompanydetailActivity.this, json.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

    String prev_page_url;
    String next_page_url;
    String current_page;

    /**
     * 解析数据公司所有职位
     */
    private void parsCompanyJob() {
        if (code == 200) {
            JSONObject jsons = JSON.parseObject(str);
            String codes = jsons.getString("code");
            current_page = jsons.getString("current_page");
            next_page_url = jsons.getString("next_page_url");
            prev_page_url = jsons.getString("prev_page_url");
            String msg = jsons.getString("msg");
            String to = jsons.getString("to");
            if (codes.equals("1")) {
                JSONArray data = jsons.getJSONArray("data");
                if (next_page_url == null || "".equals(next_page_url)) {
                    xrefreshview.setLoadComplete(true);
                    xrefreshview.setPullLoadEnable(false);
                    Log.i("a=========a", "111111"+next_page_url);
                }
                Log.i("a=========a", "2222222"+next_page_url);
                parsArrayJob(data);
            } else {
                Toast.makeText(CompanydetailActivity.this, jsons.getString("msg"), Toast.LENGTH_SHORT).show();
            }
            if (msg!=null&&!"".equals(msg)){
                Toast.makeText(CompanydetailActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CompanydetailActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    private List<CompanyJobBean> allJobList = new ArrayList<>();

    MyReceiveResumeAdapter recyclerViewReceiveResumeAdapter;

    /**
     * 解析所有公司职位的数据
     *
     * @param jsonArray
     */
    private void parsArrayJob(JSONArray jsonArray) {
        allJobList.clear();
        for (int i = 0; i < jsonArray.size(); i++) {
            CompanyJobBean companyBean = new CompanyJobBean();
            JSONObject jsondata = jsonArray.getJSONObject(i);

            companyBean.setId(jsondata.getString("id"));
            companyBean.setCom_id(jsondata.getString("com_id"));
            companyBean.setJob_title(jsondata.getString("job_title"));
            companyBean.setUpdatetime(jsondata.getString("updatetime"));
            companyBean.setJob_status(jsondata.getString("job_status"));
            companyBean.setLocation_name(jsondata.getString("location_name"));
            companyBean.setSalary(jsondata.getString("salary"));
            companyBean.setEducation_name(jsondata.getString("education_name"));
            companyBean.setWork_year_name(jsondata.getString("work_year_name"));
            companyBean.setCompany_name(jsondata.getString("company_name"));
            companyBean.setJob_status_name(jsondata.getString("job_status_name"));
            companyBean.setIs_urgent(jsondata.getString("is_urgent"));

            allJobList.add(companyBean);

        }

        // Log.i("aaaaaaaa", allJobList.get(0).getCompany_name());
        recyclerViewReceiveResumeAdapter = new MyReceiveResumeAdapter(allJobList, this);
        recycler_view_test_rv.setAdapter(recyclerViewReceiveResumeAdapter);
        recycler_view_test_rv.addItemDecoration(new RecycleViewDivider(CompanydetailActivity.this, LinearLayoutManager.HORIZONTAL));
        xrefreshview.setPullLoadEnable(true);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);

        recyclerViewReceiveResumeAdapter.setCustomLoadMoreView(new XRefreshViewFooter(CompanydetailActivity.this));

        recyclerViewReceiveResumeAdapter.setOnItemClickListener(new MyReceiveResumeAdapter.OnRecyclerViewOnItemClickListener() {
            @Override
            public void onItemClick(View v, CompanyJobBean data) {
                Intent intent = new Intent(CompanydetailActivity.this, JobInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", data.getId());
                startActivity(intent);
            }
        });
    }

    /**
     * 上垃加载更多数据
     */
    private void downLoadMoreData() {
        if (next_page_url == null) {
            xrefreshview.setLoadComplete(true);
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("Activity", MODE_PRIVATE);
            String api_token = sharedPreferences.getString("api_token", "");
            if (next_page_url == null) {
                xrefreshview.setLoadComplete(true);
            } else {
                OkHttpClient okHttpClient = new OkHttpClient();
                FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
                Request request = new Request.Builder()
                        .url(next_page_url)
                        .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                        .addHeader(ContentUrl.ACCEPT, ContentUrl.BEAR + api_token)
                        .post(formEncodingBuilder.build())
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        str = response.body().string();
                        code = response.code();
                        Log.i("::::::", str);
                        handler.sendEmptyMessage(300);
                    }
                });

            }
        }
    }

    /**
     * 下拉刷新更多数据
     */
    private void refreshMoreData() {
        if ("1".equals(current_page)) {
            refreshOnePageData();//刷新当前页面是第一个页面的数据
        } else {
            if (prev_page_url == null) {
                xrefreshview.stopRefresh();
            } else {
                upLoadMore();//下拉加载更多
            }
        }
    }

    /**
     * 刷新当前页面是第一个页面的数据
     */
    private static final int ONE_PAGE = 1;
    private void refreshOnePageData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("page", "1");
        formEncoding.add("com_id", idid);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.COMPANY_JOB)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR )
                .post(formEncoding.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("aassssss", str);
                handler.sendEmptyMessage(ONE_PAGE);
            }
        });

    }

    /**
     * 解析当前数据是第一个页面的数据
     */
    private void parsOnePageData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            String msg = json.getString("msg");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            if ("1".equals(codes)) {
                JSONArray jsonArray = json.getJSONArray("data");
                allJobList.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    CompanyJobBean companyBean = new CompanyJobBean();
                    JSONObject jsondata = jsonArray.getJSONObject(i);

                    companyBean.setId(jsondata.getString("id"));
                    companyBean.setCom_id(jsondata.getString("com_id"));
                    companyBean.setJob_title(jsondata.getString("job_title"));
                    companyBean.setUpdatetime(jsondata.getString("updatetime"));
                    companyBean.setJob_status(jsondata.getString("job_status"));
                    companyBean.setLocation_name(jsondata.getString("location_name"));
                    companyBean.setSalary(jsondata.getString("salary"));
                    companyBean.setEducation_name(jsondata.getString("education_name"));
                    companyBean.setWork_year_name(jsondata.getString("work_year_name"));
                    companyBean.setCompany_name(jsondata.getString("company_name"));
                    companyBean.setJob_status_name(jsondata.getString("job_status_name"));
                    companyBean.setIs_urgent(jsondata.getString("is_urgent"));

                    allJobList.add(companyBean);

                }
                if (next_page_url==null||"".equals(next_page_url)){
                    xrefreshview.setLoadComplete(true);
                    xrefreshview.setPullLoadEnable(false);
                }
                xrefreshview.stopRefresh();
                recyclerViewReceiveResumeAdapter.notifyDataSetChanged();
            } else if ("-1".equals(codes)) {

            }

            if (msg!=null&&!"".equals(msg)){
                Toast.makeText(CompanydetailActivity.this,msg,Toast.LENGTH_SHORT).show();
            }


        } else {

        }
    }

    /**
     * 下拉加载更多
     */
    private void upLoadMore() {
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();

        Request request = new Request.Builder()
                .url(prev_page_url)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR)
                .post(form.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("ggggg", str);
                handler.sendEmptyMessage(130);
            }
        });
    }

    private void parsData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            if (codes.equals("1")) {
                JSONObject jsonData = json.getJSONObject("data");
                parsdatadata(jsonData);//解析data数据
            } else {
                Toast.makeText(CompanydetailActivity.this, json.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CompanydetailActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    String idid;
    String uid;
    String company_name;
    String brands;
    String temptation;
    /**
     * 解析data数据
     *
     * @param jsonData
     */
    String latitude;
    String longitude;
    String coordinate_address;
    String address;
    private void parsdatadata(JSONObject jsonData) {
        idid = jsonData.getString("id");
        uid = jsonData.getString("uid");
        company_name = jsonData.getString("company_name");
        brands = jsonData.getString("brands");
        temptation = jsonData.getString("temptation");
        String profile = jsonData.getString("profile");
        String contacts = jsonData.getString("contacts");
        String mobile = jsonData.getString("mobile");
        String phone = jsonData.getString("phone");
        String fax = jsonData.getString("fax");
        String email = jsonData.getString("email");
        String qq = jsonData.getString("qq");
        String website = jsonData.getString("website");
        address = jsonData.getString("address");
        String lines = jsonData.getString("lines");
        longitude = jsonData.getString("longitude");
        latitude = jsonData.getString("latitude");
        coordinate_address = jsonData.getString("coordinate_address");
        String logo = jsonData.getString("logo");
        String micro_like = jsonData.getString("micro_like");
        String famous = jsonData.getString("famous");
        String cert_status = jsonData.getString("cert_status");
        String industry_name = jsonData.getString("industry_name");
        String comkind_name = jsonData.getString("comkind_name");
        String region_name = jsonData.getString("region_name");
        String employee_num_name = jsonData.getString("employee_num_name");
        String established_name = jsonData.getString("established_name");

        textcompanyname.setText(company_name);
        texthone.setText(brands);
        textlocation.setText(region_name);
        textscancel.setText(employee_num_name);
        textindustry.setText(comkind_name);
        texthangye.setText(industry_name);
        textaddress.setText(address);
      //  textbycarroad.setText(website);
        textprofile.setText(profile);
        text_estanted_time.setText(established_name);

        String[] welfare = temptation.split(",");
        List<String> listWelfare = new ArrayList<>();
        for (int i = 0; i < welfare.length; i++) {
            if (welfare[i]!=null&&!"".equals(welfare[i])){
                listWelfare.add(welfare[i]);
            }

        }
        if (listWelfare.size()==0){

        }else{
            text_welfare.setTags(listWelfare);
        }

    }

    String com_id;
    String api_token;
    int code;

    @Override
    public void initData() {
        Intent intent = getIntent();
        com_id = intent.getStringExtra("id");
        SharedPreferences shares = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = shares.getString("api_token", "");
        if (com_id == null || "".equals(com_id)) {
            //  Log.i("com_id",com_id);
        } else {

            OkHttpClient okHttpClient = new OkHttpClient();
            FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
            formEncodingBuilder.add("id", com_id);

            Request request = new Request.Builder()
                    .url(ContentUrl.BASE_URL + ContentUrl.COMPANY_BASIC_SHOW)
                    .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                    .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR+api_token)
                    .post(formEncodingBuilder.build())
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    str = response.body().string();
                    code = response.code();
                    Log.i("22222222str", str);
                    Log.i("22222222code", code + "");
                    handler.sendEmptyMessage(100);
                }
            });
        }
    }

    FrameLayout iv_back_basic;
    FrameLayout company_brief;
    FrameLayout company_job_all;
    LinearLayout linear_company_brief;
    LinearLayout linear_job_invite;
    RelativeLayout relativelocation;
    TextView textcompanyname;
    TextView texthone;
    TextView textlocation;
    TextView textscancel;
    TextView textindustry;
    TextView texthangye;
    TextView textaddress;
  //  TextView textbycarroad;
    TextView textprofile;
    TextView textbfief;
    TextView textcompanyjob;
    View view_left;
    View view_right;

    @Override
    public void initView() {
        iv_back_basic = (FrameLayout) findViewById(R.id.iv_back_basic);
        linear_company_brief = (LinearLayout) findViewById(R.id.linear_company_brief);
        linear_job_invite = (LinearLayout) findViewById(R.id.linear_job_invite);

        view_left = (View) findViewById(R.id.view_left);
        view_right = (View) findViewById(R.id.view_right);

        textbfief = (TextView) findViewById(R.id.textbfief);
        textcompanyjob = (TextView) findViewById(R.id.textcompanyjob);

        company_brief = (FrameLayout) findViewById(R.id.company_brief);
        company_job_all = (FrameLayout) findViewById(R.id.company_job_all);

        View briefView = creteBriefView();
        company_brief.addView(briefView);
        View companyJob = createCompanyJob();
        company_job_all.addView(companyJob);

        iv_back_basic.setOnClickListener(this);
        linear_company_brief.setOnClickListener(this);
        linear_job_invite.setOnClickListener(this);
        relativelocation.setOnClickListener(this);

    }

    private RecyclerView recycler_view_test_rv;
    private XRefreshView xrefreshview;

    private View createCompanyJob() {
        View view = View.inflate(CompanydetailActivity.this, R.layout.fram_listview, null);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);
        xrefreshview = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(this));

        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                refreshMoreData();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                downLoadMoreData();
            }
        });

        return view;
    }

    private TextView text_estanted_time;
    private TagContainerLayout text_welfare;

    private View creteBriefView() {
        View view = View.inflate(CompanydetailActivity.this, R.layout.framlayout_company_frief, null);

        relativelocation = (RelativeLayout) view.findViewById(R.id.relativelocation);
        textcompanyname = (TextView) view.findViewById(R.id.textcompanyname);
        texthone = (TextView) view.findViewById(R.id.texthone);
        textlocation = (TextView) view.findViewById(R.id.textlocation);
        textscancel = (TextView) view.findViewById(R.id.textscancel);
        textindustry = (TextView) view.findViewById(R.id.textindustry);
        texthangye = (TextView) view.findViewById(R.id.texthangye);
        textaddress = (TextView) view.findViewById(R.id.textaddress);
       // textbycarroad = (TextView) view.findViewById(R.id.textbycarroad);
        textprofile = (TextView) view.findViewById(R.id.textprofile);

        text_estanted_time = (TextView) view.findViewById(R.id.text_times);
        text_welfare = (TagContainerLayout) view.findViewById(R.id.text_welfare);

        relativelocation.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_basic://返回
                finish();
                break;
            case R.id.linear_company_brief://企业简介
                // Toast.makeText(this, "点击了企业简介", Toast.LENGTH_SHORT).show();
                textbfief.setTextColor(0xff42bfb6);
                textcompanyjob.setTextColor(0xffaaaaaa);
                company_brief.setVisibility(View.VISIBLE);
                company_job_all.setVisibility(View.GONE);
                view_left.setBackgroundColor(0xff42bfb6);
                view_right.setBackgroundColor(0xffefefef);
                break;
            case R.id.linear_job_invite://在招职位
                //Toast.makeText(this, "点击了企业简介", Toast.LENGTH_SHORT).show();
                gainCompanyWork();
                textbfief.setTextColor(0xffaaaaaa);
                textcompanyjob.setTextColor(0xff42bfb6);
                company_brief.setVisibility(View.GONE);
                company_job_all.setVisibility(View.VISIBLE);
                view_left.setBackgroundColor(0xffefefef);
                view_right.setBackgroundColor(0xff42bfb6);
                break;

            case R.id.relativelocation://显示地图
               // Toast.makeText(CompanydetailActivity.this, "11122222", Toast.LENGTH_SHORT).show();
                Intent intentAdd = new Intent(CompanydetailActivity.this, AddressActivity.class);
                intentAdd.putExtra("address", address);
                intentAdd.putExtra("latitude", latitude);
                intentAdd.putExtra("longitude", longitude);
                intentAdd.putExtra("coordinate_address", coordinate_address);
                startActivity(intentAdd);

                break;
        }
    }

    String str;

    /**
     * 获取公司所有职位
     */
    private void gainCompanyWork() {
        OkHttpClient okhttpclient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("com_id", idid);
        formEncoding.add("page", "1");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.COMPANY_JOB)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR)
                .post(formEncoding.build())
                .build();
        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (e.getMessage() != null) {
                    handler.sendEmptyMessage(110);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("111111114", str);
                handler.sendEmptyMessage(120);
            }
        });
    }
}
