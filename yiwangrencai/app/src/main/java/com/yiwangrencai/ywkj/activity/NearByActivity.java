package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.adapter.MyNearWorkAdapter;
import com.yiwangrencai.ywkj.bean.NearJobBean;
import com.yiwangrencai.ywkj.bean.NearWorkBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2017/5/16.
 */

public class NearByActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_nearby_job;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100://获取数据成功
                    //   myPopuwindown.dismiss();
                    parsGainDataFromService();//解析数据
                    break;
                case 110://联网失败

                    break;
                case 120:
                    Toast.makeText(NearByActivity.this, "下拉刷新获取数据失败", Toast.LENGTH_SHORT).show();
                    break;
                case 130://下拉刷新
                    parsRefreshData();//解析下拉刷新的数据
                    break;
                case 150://上拉加载数据
                    parsLoadMoreData();//解析上垃加载的数据
                    break;
                case ONE_PAGE:
                    parsOnePageData();//解析当时第一个页面时下拉刷新的数据
                    break;
            }
        }
    };


    String api_token;
    String latitude;
    String longitude;

    @Override
    public void initData() {
        SharedPreferences shares = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = shares.getString("api_token", "");

        SharedPreferences share = getSharedPreferences("data", MODE_PRIVATE);
        latitude = share.getString("latitude", "");
        longitude = share.getString("lontitude", "");
        Log.i("fujinlatitude", latitude);
        Log.i("fujinlontitude", longitude);


    }


    /**
     * 从服务器获取数据
     */
    String str;
    int code;

    private void gainDataFromService() {

        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("latitude", latitude);
        formEncoding.add("longitude", longitude);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.USER_NEAR_JOB)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .post(formEncoding.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
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
                Log.i("从服务器获取的附近工作的数据", str);
                handler.sendEmptyMessage(100);
            }
        });
    }

    /**
     * 解析从服务器获取的数据
     */
    private String current_page;
    private String next_page_url;
    private String prev_page_url;
    private List<NearWorkBean> listData = new ArrayList<NearWorkBean>();
    private MyNearWorkAdapter adapter;

    private void parsGainDataFromService() {
        if (code == 200) {
            JSONObject jsonObject = JSON.parseObject(str);
            current_page = jsonObject.getString("current_page");
            next_page_url = jsonObject.getString("next_page_url");
            prev_page_url = jsonObject.getString("prev_page_url");
            String codes = jsonObject.getString("code");
            if ("1".equals(codes)) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray.size() != 0) {
                    framLayout.removeView(loadingView);
                    framLayout.addView(successView);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        NearWorkBean nearWorkBean = new NearWorkBean();
                        nearWorkBean.setId(jsonData.getString("id"));
                        nearWorkBean.setJob_title(jsonData.getString("job_title"));
                        nearWorkBean.setCom_id(jsonData.getString("com_id"));
                        nearWorkBean.setEducation(jsonData.getString("education"));
                        nearWorkBean.setWork_year(jsonData.getString("work_year"));
                        nearWorkBean.setDistance(jsonData.getString("distance"));
                        nearWorkBean.setSalary(jsonData.getString("salary"));
                        nearWorkBean.setId(jsonData.getString("id"));
                        nearWorkBean.setCompany_name(jsonData.getString("company_name"));
                        listData.add(nearWorkBean);
                    }
                    if (next_page_url == null || "".equals(next_page_url)) {
                        xrefreshview.setLoadComplete(true);
                        xrefreshview.setPullLoadEnable(false);
                    }

                    adapter = new MyNearWorkAdapter(NearByActivity.this, listData);
                    recycler_view_test_rv.setAdapter(adapter);

                    xrefreshview.setAutoLoadMore(false);
                    xrefreshview.setPinnedTime(1000);
                    xrefreshview.setMoveForHorizontal(true);

                    adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
                    //条目的点击事件
                    adapter.setOnItemClickListener(new MyNearWorkAdapter.OnNearRecyelerViewClickListener() {
                        @Override
                        public void onItemClick(View view, NearWorkBean data) {
                            //   Toast.makeText(NearByActivity.this, "点击了", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NearByActivity.this, JobInfoActivity.class);
                            intent.putExtra("id", data.getId());
                            startActivity(intent);
                        }
                    });

                    //上垃加载和下拉刷新
                    xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
                        @Override
                        public void onRefresh(boolean isPullDown) {
                            super.onLoadMore(isPullDown);
                            onRefreshData();//下拉获取数据
                        }

                        @Override
                        public void onLoadMore(boolean isSilence) {
                            super.onLoadMore(isSilence);
                            onLoadMoreData();//上垃加载获取更多数据
                        }
                    });

                } else {
                    framLayout.removeView(loadingView);
                    framLayout.addView(emptyView);
                    Toast.makeText(NearByActivity.this, "附近没有您要找的工作", Toast.LENGTH_SHORT).show();
                }
            } else {
                framLayout.removeView(loadingView);
                framLayout.addView(emptyView);
                Toast.makeText(NearByActivity.this, "联网获取数据失败", Toast.LENGTH_SHORT).show();
            }

        } else {
            framLayout.removeView(loadingView);
            framLayout.addView(emptyView);
            Toast.makeText(NearByActivity.this, "联网获取数据失败", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 上垃加载获取更多数据
     */
    private void onLoadMoreData() {
        if (next_page_url == null || "".equals(next_page_url)) {
            xrefreshview.setLoadComplete(true);
        } else {
            OkHttpClient okHttp = new OkHttpClient();
            FormEncodingBuilder formEncoding = new FormEncodingBuilder();
            formEncoding.add("latitude", latitude);
            formEncoding.add("longitude", longitude);
            Request request = new Request.Builder()
                    .url(next_page_url)
                    .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                    .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                    .post(formEncoding.build())
                    .build();
            okHttp.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    if (e.getMessage() != null) {
                        handler.sendEmptyMessage(140);
                    }
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    str = response.body().string();
                    code = response.code();
                    Log.i("上垃加载获取的数据", str);
                    handler.sendEmptyMessage(150);
                }
            });
        }
    }

    /**
     * 解析上垃加载的数据
     */
    private void parsLoadMoreData() {
        if (code == 200) {
            JSONObject jsonObject = JSON.parseObject(str);
            current_page = jsonObject.getString("current_page");
            prev_page_url = jsonObject.getString("prev_page_url");
            next_page_url = jsonObject.getString("next_page_url");
            String codes = jsonObject.getString("code");

            if ("1".equals(codes)) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    NearWorkBean nearWorkBean = new NearWorkBean();
                    nearWorkBean.setId(jsonData.getString("id"));
                    nearWorkBean.setJob_title(jsonData.getString("job_title"));
                    nearWorkBean.setCom_id(jsonData.getString("com_id"));
                    nearWorkBean.setEducation(jsonData.getString("education"));
                    nearWorkBean.setWork_year(jsonData.getString("work_year"));
                    nearWorkBean.setDistance(jsonData.getString("distance"));
                    nearWorkBean.setSalary(jsonData.getString("salary"));
                    nearWorkBean.setId(jsonData.getString("id"));
                    nearWorkBean.setCompany_name(jsonData.getString("company_name"));
                    listData.add(nearWorkBean);
                }

                if (next_page_url == null || "".equals(next_page_url)) {
                    xrefreshview.setLoadComplete(true);
                    xrefreshview.setPullLoadEnable(false);
                }

                adapter.notifyDataSetChanged();
                xrefreshview.setLoadComplete(false);

            } else {
                Toast.makeText(NearByActivity.this, "上垃加载获取数据失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(NearByActivity.this, "上垃加载获取数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 下拉刷新
     */
    private void onRefreshData() {
        if ("1".equals(current_page)) {
            refreshOnePage();//当是第一个界面的时候下拉刷新的数据
        } else {
            if (prev_page_url == null || "".equals(prev_page_url)) {
                xrefreshview.stopRefresh();
                xrefreshview.setLoadComplete(false);
                xrefreshview.setLoadComplete(false);
            } else {
                OkHttpClient okHttpClient = new OkHttpClient();
                FormEncodingBuilder formRefrsh = new FormEncodingBuilder();
                formRefrsh.add("latitude", latitude);
                formRefrsh.add("longitude", longitude);
                Request request = new Request.Builder()
                        .url(prev_page_url)
                        .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                        .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                        .post(formRefrsh.build())
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        if (e.getMessage() != null) {
                            handler.sendEmptyMessage(120);
                        }
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        str = response.body().string();
                        code = response.code();
                        Log.i("下拉刷新获取的数据", str);
                        handler.sendEmptyMessage(130);
                    }
                });
            }
        }
    }

    /**
     * 当是第一个界面的时候下拉刷新的数据
     */
    private static final int ONE_PAGE = 1;

    private void refreshOnePage() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("latitude", latitude);
        formEncoding.add("longitude", longitude);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.USER_NEAR_JOB)
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
                str = response.body().string();
                code = response.code();
                Log.i("assssss", str);
                handler.sendEmptyMessage(ONE_PAGE);
            }
        });

    }

    /**
     * 解析当时第一个页面时下拉刷新的数据
     */
    private void parsOnePageData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            String msg = json.getString("msg");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            if ("1".equals(codes)){
                listData.clear();
                JSONArray jsonArray = json.getJSONArray("data");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    NearWorkBean nearWorkBean = new NearWorkBean();
                    nearWorkBean.setId(jsonData.getString("id"));
                    nearWorkBean.setJob_title(jsonData.getString("job_title"));
                    nearWorkBean.setCom_id(jsonData.getString("com_id"));
                    nearWorkBean.setEducation(jsonData.getString("education"));
                    nearWorkBean.setWork_year(jsonData.getString("work_year"));
                    nearWorkBean.setDistance(jsonData.getString("distance"));
                    nearWorkBean.setSalary(jsonData.getString("salary"));
                    nearWorkBean.setId(jsonData.getString("id"));
                    nearWorkBean.setCompany_name(jsonData.getString("company_name"));
                    listData.add(nearWorkBean);
                }
               if (next_page_url==null||"".equals(next_page_url)){
                   xrefreshview.setLoadComplete(true);
                   xrefreshview.setPullLoadEnable(false);
               }

                xrefreshview.stopRefresh();
                adapter.notifyDataSetChanged();
            }else if("-1".equals(codes)){

            }

            if (msg!=null&&!"".equals(msg)){
                Toast.makeText(NearByActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

    /**
     * 解析下拉刷新的数据
     */
    private void parsRefreshData() {


        if (code == 200) {
            JSONObject jsonObject = JSON.parseObject(str);
            current_page = jsonObject.getString("current_page");
            next_page_url = jsonObject.getString("next_page_url");
            prev_page_url = jsonObject.getString("prev_page_url");
            String codes = jsonObject.getString("code");

            if ("1".equals(codes)) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                listData.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    NearWorkBean nearWorkBean = new NearWorkBean();
                    nearWorkBean.setId(jsonData.getString("id"));
                    nearWorkBean.setJob_title(jsonData.getString("job_title"));
                    nearWorkBean.setCom_id(jsonData.getString("com_id"));
                    nearWorkBean.setEducation(jsonData.getString("education"));
                    nearWorkBean.setWork_year(jsonData.getString("work_year"));
                    nearWorkBean.setDistance(jsonData.getString("distance"));
                    nearWorkBean.setSalary(jsonData.getString("salary"));
                    nearWorkBean.setId(jsonData.getString("id"));
                    nearWorkBean.setCompany_name(jsonData.getString("company_name"));
                    listData.add(nearWorkBean);
                }

                if (next_page_url == null || "".equals(next_page_url)) {
                    xrefreshview.setLoadComplete(true);
                    xrefreshview.setPullLoadEnable(false);
                }

                adapter.notifyDataSetChanged();
                xrefreshview.stopRefresh();
                xrefreshview.setPullLoadEnable(true);

            } else {
                Toast.makeText(NearByActivity.this, "下拉刷新获取数据失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(NearByActivity.this, "下拉刷新获取数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    private FrameLayout iv_back_basic;
    private FrameLayout framLayout;
    private View loadingView;
    private View emptyView;
    private View successView;

    @Override
    public void initView() {

        iv_back_basic = (FrameLayout) findViewById(R.id.iv_back_basic);
        framLayout = (FrameLayout) findViewById(R.id.framLayout);

        loadingView = createLoadingView();
        emptyView = createEmptyView();
        successView = createSuccessView();
        iv_back_basic.setOnClickListener(this);

        framLayout.addView(loadingView);
        gainDataFromService();//从服务器获取数据

    }

    /**
     * 创建加载数据成功的界面
     *
     * @return
     */
    private XRefreshView xrefreshview;
    private RecyclerView recycler_view_test_rv;

    private View createSuccessView() {
        View view = View.inflate(NearByActivity.this, R.layout.fram_listview, null);
        xrefreshview = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);

        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(this));
        xrefreshview.setPullLoadEnable(true);

        return view;
    }

    /**
     * 创建加载为空的界面
     *
     * @return
     */
    private View createEmptyView() {
        View view = View.inflate(NearByActivity.this, R.layout.empty_view, null);
        return view;
    }

    /**
     * 创建正在加载的界面
     *
     * @return
     */
    private View createLoadingView() {
        View view = View.inflate(NearByActivity.this, R.layout.loading_view, null);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_basic://返回
                finish();
                break;
        }
    }
}
