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
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.wxapi.WXEntryActivity;
import com.yiwangrencai.ywkj.adapter.MyLookMeAdapter;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.UiUtils;
import com.yiwangrencai.ywkj.view.RecycleViewDivider;
import com.yiwangrencai.ywkj.view.SimpleDividerItemDecoration;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/5.
 */

public class LookMineActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_look_mine;
    }

    @Override
    public void initData() {
        gainDataFromService();//从服务器获取数据
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    switchView();//切换界面
                    break;
                case 110:
                    break;
                case 130:
                    parsLookMeLoadMoreData();//解析获取更多的数据
                    break;
                case 300:
                    parsRefreshNewData();//解析下拉刷新获取的数据
                    break;
                case ONE_PAGE:
                    parsOnePageData();//解析当前页是第一页时的数据
                    break;
            }
        }
    };


    /**
     * 解析下拉刷新获取的数据
     */
    private void parsRefreshNewData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            if ("1".equals(codes)) {
                JSONArray jsonArray = json.getJSONArray("data");
                listLookMe.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    CompanyJobBean companyJobBean = new CompanyJobBean();
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    companyJobBean.setCom_id(jsonData.getString("com_id"));
                    companyJobBean.setEcom_id(jsonData.getString("ecom_id"));
                    companyJobBean.setCompany_name(jsonData.getString("company_name"));
                    companyJobBean.setTime(jsonData.getString("time"));
                    companyJobBean.setIndustry_name(jsonData.getString("industry_name"));
                    companyJobBean.setRegion_name(jsonData.getString("region_name"));
                    listLookMe.add(companyJobBean);
                }
                adapter.notifyDataSetChanged();
                xrefreshview.stopRefresh();
                xrefreshview.setLoadComplete(false);
            } else {

            }
        } else {

        }
    }

    /**
     * 解析获取更多的数据
     */
    private void parsLookMeLoadMoreData() {
        xrefreshview.stopLoadMore();
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            if (codes.equals("1")) {
                JSONArray jsonArray = json.getJSONArray("data");
                for (int i = 0; i < jsonArray.size(); i++) {
                    CompanyJobBean companyJobBean = new CompanyJobBean();
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    companyJobBean.setCom_id(jsonData.getString("com_id"));
                    companyJobBean.setEcom_id(jsonData.getString("ecom_id"));
                    companyJobBean.setCompany_name(jsonData.getString("company_name"));
                    companyJobBean.setTime(jsonData.getString("time"));
                    companyJobBean.setIndustry_name(jsonData.getString("industry_name"));
                    companyJobBean.setRegion_name(jsonData.getString("region_name"));
                    listLookMe.add(companyJobBean);
                }
                adapter.notifyDataSetChanged();

            } else {

            }
        } else {

        }
    }

    /**
     * 根据返回的数据切换不同的界面
     */
    String current_page;
    String next_page_url;
    String prev_page_url;

    private void switchView() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String msg = json.getString("msg");

            if (codes.equals("1")) {
                JSONArray jsonArray = json.getJSONArray("data");

                if (jsonArray.size() == 0) {
                    frameLayout.removeView(loadingView);
                    frameLayout.addView(noEmptyLookView);
                } else {
                    frameLayout.removeView(loadingView);
                    frameLayout.addView(successView);
                    if (jsonArray.size() < 15 && current_page.equals("1")) {
                        xrefreshview.setPullLoadEnable(false);
                    }
                    parsLookMineData(jsonArray);
                }

            } else if (codes.equals("0")) {
            //Todo
            } else if (codes.equals("-1")) {
                Intent intent = new Intent(LookMineActivity.this, WXEntryActivity.class);
                intent.putExtra("register", "loginMine");
                startActivity(intent);
                finish();
            }
            if (msg != null && !"".equals(msg)) {
                Toast.makeText(LookMineActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

        } else {

        }
    }

    /**
     * 解析看过我的数据
     *
     * @param jsonArray
     */
    MyLookMeAdapter adapter;
    private List<CompanyJobBean> listLookMe = new ArrayList<>();

    private void parsLookMineData(JSONArray jsonArray) {
        listLookMe.clear();
        for (int i = 0; i < jsonArray.size(); i++) {
            CompanyJobBean companyJobBean = new CompanyJobBean();
            JSONObject jsonData = jsonArray.getJSONObject(i);
            companyJobBean.setCom_id(jsonData.getString("com_id"));
            companyJobBean.setEcom_id(jsonData.getString("ecom_id"));
            companyJobBean.setCompany_name(jsonData.getString("company_name"));
            companyJobBean.setTime(jsonData.getString("time"));
            companyJobBean.setIndustry_name(jsonData.getString("industry_name"));
            companyJobBean.setRegion_name(jsonData.getString("region_name"));
            listLookMe.add(companyJobBean);
        }
        adapter = new MyLookMeAdapter(listLookMe);
        recycler_view_test_rv.setAdapter(adapter);

        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                refreshNewData();//下拉刷新获取数据
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                loadMoreData();//上拉获取新的数据
            }
        });

        adapter.setmOnItemClickListener(new MyLookMeAdapter.OnRecyclerViewOnItemClickListener() {
            @Override
            public void onItenClick(View view, CompanyJobBean data) {
                Intent intent = new Intent(LookMineActivity.this, CompanydetailActivityTwo.class);
                intent.putExtra("id", data.getEcom_id());
                intent.putExtra("com_id", data.getEcom_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

    }

    /**
     * 上垃加载更多数据
     */
    private void loadMoreData() {
        if (next_page_url == null) {
            xrefreshview.setLoadComplete(true);
            Toast.makeText(LookMineActivity.this, "已经没有更多数据了", Toast.LENGTH_SHORT).show();
        } else {
            loadMoreDataAgain();//加载更多的数据
        }
    }

    /**
     * 向服务器获取更多的数据
     */
    private void loadMoreDataAgain() {
        // int currentpage = Integer.parseInt(current_page) + 1;
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        //formEncoding.add("page", currentpage + "");
        Request request = new Request.Builder()
                .url(next_page_url)
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
                handler.sendEmptyMessage(130);
            }
        });
    }

    /**
     * 下拉刷新获取数据
     */
    private void refreshNewData() {
        if ("1".equals(current_page)) {
            refreshOnePage();//刷新当前页是第一的时候的数据
        } else {
            if (prev_page_url == null) {
                xrefreshview.stopRefresh();
            } else {
                refreshNewDataService();//下拉刷新获取数据
            }
        }
    }

    /**
     * 刷新当前页是第一的时候的数据
     */
    private static final int ONE_PAGE = 2;
    private void refreshOnePage() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("page", "1");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.USER_BROWSE_COM)
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
                Log.i("ssssssss", str);
                handler.sendEmptyMessage(ONE_PAGE);
            }
        });
    }

    /**
     * 解析当前页是第一页的时候的数据
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
                listLookMe.clear();

                for (int i = 0; i < jsonArray.size(); i++) {
                    CompanyJobBean companyJobBean = new CompanyJobBean();
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    companyJobBean.setCom_id(jsonData.getString("com_id"));
                    companyJobBean.setEcom_id(jsonData.getString("ecom_id"));
                    companyJobBean.setCompany_name(jsonData.getString("company_name"));
                    companyJobBean.setTime(jsonData.getString("time"));
                    companyJobBean.setIndustry_name(jsonData.getString("industry_name"));
                    companyJobBean.setRegion_name(jsonData.getString("region_name"));
                    listLookMe.add(companyJobBean);
                }

                if (next_page_url == null || "".equals(next_page_url)) {
                    xrefreshview.setLoadComplete(true);
                    xrefreshview.setPullLoadEnable(false);
                }
                xrefreshview.stopRefresh();
                adapter.notifyDataSetChanged();
                if (msg != null && !"".equals(msg)) {
                   Toast.makeText(LookMineActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            } else if ("-1".equals(codes)) {

            }

        } else {

        }
    }

    /**
     * 下拉刷新获取数据
     */
    private void refreshNewDataService() {

        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formencoding = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(prev_page_url)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formencoding.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("!!!!!!", str);
                handler.sendEmptyMessage(300);
            }
        });
    }

    /**
     * 从服务器获取数据
     */
    String str;
    int code;
    String api_token;

    private void gainDataFromService() {
        SharedPreferences shared = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = shared.getString("api_token", "");
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.USER_BROWSE_COM)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
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
                Log.i("1111111111111", str);
                handler.sendEmptyMessage(100);
            }
        });
    }

    private FrameLayout fram_job_back;
    private FrameLayout frameLayout;
    private View loadingView;
    private View emptyView;
    private View successView;
    private View noEmptyLookView;

    @Override
    public void initView() {
        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        loadingView = createLoadingView();
        emptyView = createEmptyView();
        successView = createSuccessView();
        noEmptyLookView = createNoEmptyLookView();

        frameLayout.addView(loadingView);
        fram_job_back.setOnClickListener(this);
    }

    /**
     * 创建还没有公司浏览您的简历
     *
     * @return
     */
    private View createNoEmptyLookView() {
        View view = View.inflate(LookMineActivity.this, R.layout.empty_resume_view, null);
        TextView textview = (TextView) view.findViewById(R.id.textview);
        textview.setText("还没有公司查看您的简历");
        return view;
    }

    /**
     * 创建加载成功界面
     *
     * @return
     */
    private XRefreshView xrefreshview;
    private RecyclerView recycler_view_test_rv;

    private View createSuccessView() {
        View view = View.inflate(LookMineActivity.this, R.layout.fram_listview_job, null);
        xrefreshview = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);

        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_test_rv.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL,1,0xffdfdfdf));
        xrefreshview.setPullLoadEnable(true);
        return view;
    }

    /**
     * 创建空界面
     *
     * @return
     */
    private View createEmptyView() {
        View view = View.inflate(LookMineActivity.this, R.layout.empty_view, null);
        TextView textview = (TextView) view.findViewById(R.id.textview_click);
        textview.setOnClickListener(this);
        return view;
    }

    /**
     * 创建正在加载的页面
     *
     * @return
     */
    private View createLoadingView() {
        View view = View.inflate(LookMineActivity.this, R.layout.loading_view, null);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back:
                finish();
                break;
            case R.id.textview:
                gainDataFromService();
                break;
        }
    }
}
