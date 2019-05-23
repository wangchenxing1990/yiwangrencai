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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.ContextObjectDeserializer;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.wxapi.WXEntryActivity;
import com.yiwangrencai.ywkj.adapter.MyMoreRemcomAdapter;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.content.ContentUrl;

import org.apache.http.params.CoreConnectionPNames;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class MoreJobRecomActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_more_job_recommen;
    }

    /**
     * handler进行数据解析
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    switchView();//根据获取的数据切换不同的界面
                    break;
                case 110:
                    frameLayout.removeView(loadingView);
                    frameLayout.addView(emptyView);
                    Toast.makeText(MoreJobRecomActivity.this, "联网超时获取数据失败", Toast.LENGTH_SHORT).show();
                    break;
                case 120:
                    parLoadMoreData();//解析获取加载更多的数据
                    break;
                case 130:
                    parsRefreshMoreData();//解析下拉刷新获取的更多的数据
                    break;
            }
        }
    };


    /**
     * 根据获取的数据切换不同的界面并解析数据
     */
    String current_page;
    String next_page_url;
    String prev_page_url;

    private void switchView() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codes = json.getString("code");

            if ("1".equals(codes)) {

                JSONArray jsonArray = json.getJSONArray("data");
                if (jsonArray.size() == 0) {
                    frameLayout.removeView(loadingView);
                    frameLayout.addView(emptyViewData);
                } else {
                    frameLayout.removeView(loadingView);
                    frameLayout.addView(successView);
                    parsJsonArrayData(jsonArray);
                }

                //解析jsonArray的数据
            } else if ("-1".equals(codes)) {
                frameLayout.removeView(loadingView);

                Intent intent = new Intent(MoreJobRecomActivity.this, WXEntryActivity.class);
                startActivity(intent);
                finish();
            } else if ("0".equals(codes)) {
                frameLayout.removeView(loadingView);
                //todo
            }
        } else {
            frameLayout.removeView(loadingView);
            frameLayout.addView(emptyView);
        }
    }

    /**
     * 解析更多里面的数据
     *
     * @param jsonArray
     */
    private MyMoreRemcomAdapter adapter;
    private List<CompanyJobBean> listJob = new ArrayList<>();

    private void parsJsonArrayData(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.size(); i++) {
            CompanyJobBean companyJobBean = new CompanyJobBean();
            JSONObject jsonData = jsonArray.getJSONObject(i);
            companyJobBean.setId(jsonData.getString("id"));
            companyJobBean.setCom_id(jsonData.getString("com_id"));
            companyJobBean.setJob_title(jsonData.getString("job_title"));
            companyJobBean.setLocation_name(jsonData.getString("location_name"));
            companyJobBean.setSalary(jsonData.getString("salary"));
            companyJobBean.setCompany_name(jsonData.getString("company_name"));
            listJob.add(companyJobBean);
        }

        adapter = new MyMoreRemcomAdapter(listJob);
        recycler_view_test_rv.setAdapter(adapter);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(MoreJobRecomActivity.this));

        if ("1".equals(current_page) && jsonArray.size() < 15) {
            xrefreshview.setPullLoadEnable(false);
            Log.i("是否已经执行", current_page);
        }


        //上垃加载和下拉刷新
        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                refreshMoreData();//下拉刷新更多数据
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                loadMoreData();//上拉加载更多数据
            }
        });

        //条目的点击事件
        adapter.setOnItemClickListener(new MyMoreRemcomAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, CompanyJobBean data) {
               // Toast.makeText(MoreJobRecomActivity.this, "点击的是第" + data.getJob_title(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MoreJobRecomActivity.this, JobInfoActivity.class);
                intent.putExtra("id", data.getId());
                startActivity(intent);
            }
        });
    }

    /**
     * 下拉刷新更多数据
     */
    private void refreshMoreData() {
        if (prev_page_url == null||"".equals(prev_page_url)) {
            xrefreshview.stopRefresh();
        } else {
            refreshMoreDataFromService();//下拉刷新从服务器获取更多数据
        }
    }

    /**
     * 下拉刷新从服务器获取更多数据
     */
    private void refreshMoreDataFromService() {
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(prev_page_url)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncodingBuilder.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("下拉刷新获取更多数据", str);
                handler.sendEmptyMessage(130);
            }
        });
    }

    /**
     * 解析下拉刷新获取的更多的数据
     */
    private void parsRefreshMoreData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            current_page = json.getString("current_page");
            String codes = json.getString("code");
            if ("1".equals(codes)) {
                JSONArray jsonData = json.getJSONArray("data");
                listJob.clear();
                for (int i = 0; i < jsonData.size(); i++) {
                    CompanyJobBean companyJobBean = new CompanyJobBean();
                    JSONObject jsonDatas = jsonData.getJSONObject(i);
                    companyJobBean.setId(jsonDatas.getString("id"));
                    companyJobBean.setCom_id(jsonDatas.getString("com_id"));
                    companyJobBean.setJob_title(jsonDatas.getString("job_title"));
                    companyJobBean.setLocation_name(jsonDatas.getString("location_name"));
                    companyJobBean.setSalary(jsonDatas.getString("salary"));
                    companyJobBean.setCompany_name(jsonDatas.getString("company_name"));
                    listJob.add(companyJobBean);
                }
                adapter.notifyDataSetChanged();
                xrefreshview.stopRefresh();
                xrefreshview.setLoadComplete(false);
            } else {
                Toast.makeText(MoreJobRecomActivity.this, "下拉刷新获取数据失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MoreJobRecomActivity.this, "下拉刷新获取数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 上拉加载更多数据
     */
    private void loadMoreData() {
        if (next_page_url == null||"".equals(next_page_url)) {
            xrefreshview.setLoadComplete(true);
        } else {
            loadMoredateFromservice();//从服务器获取更多数据
        }
    }

    /**
     * 从服务器获取更多数据
     */
    private void loadMoredateFromservice() {
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formcoding = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(next_page_url)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formcoding.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("上垃加载跟多的数据", str);
                handler.sendEmptyMessage(120);
            }
        });
    }

    /**
     * 解析上垃加载获取的更多的数据
     */
    private void parLoadMoreData() {
        if (code == 200) {
            JSONObject jsonMore = JSON.parseObject(str);
            current_page = jsonMore.getString("next_page_url");
            next_page_url = jsonMore.getString("next_page_url");
            prev_page_url = jsonMore.getString("prev_page_url");
            String codes = jsonMore.getString("code");

            if ("1".equals(codes)) {
                JSONArray data = jsonMore.getJSONArray("data");

                for (int i = 0; i < data.size(); i++) {
                    CompanyJobBean companyJobBean = new CompanyJobBean();
                    JSONObject jsonData = data.getJSONObject(i);
                    companyJobBean.setId(jsonData.getString("id"));
                    companyJobBean.setCom_id(jsonData.getString("com_id"));
                    companyJobBean.setJob_title(jsonData.getString("job_title"));
                    companyJobBean.setLocation_name(jsonData.getString("location_name"));
                    companyJobBean.setSalary(jsonData.getString("salary"));
                    companyJobBean.setCompany_name(jsonData.getString("company_name"));
                    listJob.add(companyJobBean);

                }

                adapter.setData(listJob,true);
                xrefreshview.setLoadComplete(true);
                xrefreshview.stopRefresh();
            //    adapter.notifyDataSetChanged();

            } else {
                Toast.makeText(MoreJobRecomActivity.this, "数据加载失败", Toast.LENGTH_SHORT).show();
            }

        } else {

        }
    }

    /**
     * 从服务器段获取数据
     */
    String str;
    int code;
    String api_token;

    @Override
    public void initData() {
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.USER_COMMEND)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncoding.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
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
                Log.i("更多推荐职位的数据", str);
                handler.sendEmptyMessage(100);
            }
        });
    }

    private FrameLayout fram_job_back;
    private FrameLayout frameLayout;
    private View loadingView;
    private View emptyView;
    private View successView;
    private View emptyViewData;
    @Override
    public void initView() {
        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        loadingView = createLoadingView();//创建正在加载的界面
        emptyView = createEmptyView();//创建加载为空的界面
        successView = createSuccessView();//创建加载成功的界面
        emptyViewData=creatreEmptyViewData();//创建获取的数据为空的界面
        frameLayout.addView(loadingView);//添加初始界面
        fram_job_back.setOnClickListener(this);
    }

    /**
     * 创建加载数据为空的界面
     * @return
     */
    private TextView textview_click;
    private View creatreEmptyViewData() {
        View view=View.inflate(this,R.layout.empty_view,null);
        textview_click= (TextView) view.findViewById(R.id.textview_click);
        textview_click.setText("没有更多的职位可以推荐给您");
        return view;
    }

    /**
     * 创建加载成功的界面
     *
     * @return
     */
    private XRefreshView xrefreshview;
    private RecyclerView recycler_view_test_rv;

    private View createSuccessView() {
        View view = View.inflate(MoreJobRecomActivity.this, R.layout.fram_listview, null);
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
        View view = View.inflate(MoreJobRecomActivity.this, R.layout.empty_view, null);
        textview_click= (TextView) view.findViewById(R.id.textview_click);
        textview_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.removeView(emptyView);
                frameLayout.addView(loadingView);
                initData();
            }
        });
        return view;
    }

    /**
     * 创建正在加载的界面
     *
     * @return
     */
    private View createLoadingView() {
        View view = View.inflate(MoreJobRecomActivity.this, R.layout.loading_view, null);
        return view;
    }

    /**
     * 控件的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back://结束本界面返回上一页
                finish();
                break;
        }
    }
}
