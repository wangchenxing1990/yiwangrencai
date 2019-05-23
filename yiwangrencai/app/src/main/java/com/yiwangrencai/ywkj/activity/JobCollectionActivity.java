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
import com.yiwangrencai.ywkj.adapter.MyJobCollectionAdapter;
import com.yiwangrencai.ywkj.adapter.MyRecyclerAdapter;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.bean.HomePager;
import com.yiwangrencai.ywkj.bean.InterViewBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.UiUtils;
import com.yiwangrencai.ywkj.view.RecycleViewDivider;
import com.yiwangrencai.ywkj.view.SwipeItemLayout;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/5.
 */

public class JobCollectionActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_job_collection;
    }

    @Override
    public void initData() {
        gainDataFromService();
    }

    private FrameLayout fram_job_back;
    private FrameLayout frameLayout;
    private View loadingView;
    private View emptyView;
    private View successView;
    private View noCollectView;

    @Override
    public void initView() {
        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        loadingView = createLoadingView();
        emptyView = createEmptyView();
        successView = createSucessView();
        noCollectView = createNoCollectionView();

        frameLayout.addView(loadingView);
        fram_job_back.setOnClickListener(this);

    }

    /**
     * 创建没有收藏简历的界面
     *
     * @return
     */
    private View createNoCollectionView() {
        View view = View.inflate(JobCollectionActivity.this, R.layout.empty_resume_view, null);
        TextView textview= (TextView) view.findViewById(R.id.textview);
        textview.setText("你还没有收藏职位");
        return view;
    }

    /**
     * 创建加载成功的界面
     *
     * @return
     */
    private XRefreshView xrefreshview;
    private RecyclerView recycler_view_test_rv;

    private View createSucessView() {

        View view = View.inflate(JobCollectionActivity.this, R.layout.fram_listview_job, null);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);
        xrefreshview = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv.setBackgroundColor(0xffffffff);
        recycler_view_test_rv.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL,1,0xffdfdfdf));

        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_test_rv.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        xrefreshview.setPullLoadEnable(true);
        return view;
    }

    /**
     * 创建网络出错的空界面
     *
     * @return
     */
    private TextView textview_click;

    private View createEmptyView() {
        View view = View.inflate(JobCollectionActivity.this, R.layout.empty_view, null);
        textview_click = (TextView) view.findViewById(R.id.textview_click);
        textview_click.setOnClickListener(this);
        return view;
    }

    /**
     * 创建正在加载的界面
     *
     * @return
     */
    private View createLoadingView() {
        View view = View.inflate(JobCollectionActivity.this, R.layout.loading_view, null);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back:
                finish();
                break;
            case R.id.textview_click:
                frameLayout.removeView(emptyView);
                frameLayout.addView(loadingView);
                gainDataFromService();//从服务器获取数据
                break;
        }
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
                .url(ContentUrl.BASE_URL + ContentUrl.FAVORITES)
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
                Log.i("aaaaaaaa", str);
                Log.i("vvvvvvvv", code + "");
                handler.sendEmptyMessage(100);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    switchView();//切换不同的view界面
                    break;
                case 110:
                    frameLayout.removeView(loadingView);
                    frameLayout.addView(emptyView);
                    break;
                case 130:
                    loadMoreResult();
                    break;
                case 300:
                    parsRefreshNewData();//解析下拉刷新获取的数据
                    break;
                case ONE_PAGE:
                    parsOnePageData();//解析当前也为第一页的时候下拉刷新的数据
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
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            current_page = json.getString("current_page");
            String codes = json.getString("code");
            if ("1".equals(codes)) {
                JSONArray jsonArray = json.getJSONArray("data");
                listCollection.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    CompanyJobBean companyJobBean = new CompanyJobBean();
                    JSONObject jsondata = jsonArray.getJSONObject(i);
                    companyJobBean.setJob_id(jsondata.getString("job_id"));
                    companyJobBean.setJob_title(jsondata.getString("job_title"));
                    companyJobBean.setLocation_name(jsondata.getString("location_name"));
                    companyJobBean.setWork_year_name(jsondata.getString("work_year_name"));
                    companyJobBean.setEducation_name(jsondata.getString("education_name"));
                    companyJobBean.setSalary(jsondata.getString("salary"));
                    companyJobBean.setCompany_name(jsondata.getString("company_name"));
                    companyJobBean.setIs_urgent(jsondata.getString("is_urgent"));
                    companyJobBean.setUpdatetime(jsondata.getString("updatetime"));
                    companyJobBean.setPart_status(jsondata.getString("part_status"));

                    listCollection.add(companyJobBean);
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
     * 加载更多的返回结果
     */
    private void loadMoreResult() {
        xrefreshview.stopLoadMore();
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            current_page = json.getString("current_page");
            if (codes.equals("1")) {
                JSONArray jsonArray = json.getJSONArray("data");
                for (int i = 0; i < jsonArray.size(); i++) {
                    CompanyJobBean companyJobBean = new CompanyJobBean();
                    JSONObject jsondata = jsonArray.getJSONObject(i);
                    companyJobBean.setJob_id(jsondata.getString("job_id"));
                    companyJobBean.setJob_title(jsondata.getString("job_title"));
                    companyJobBean.setLocation_name(jsondata.getString("location_name"));
                    companyJobBean.setWork_year_name(jsondata.getString("work_year_name"));
                    companyJobBean.setEducation_name(jsondata.getString("education_name"));
                    companyJobBean.setSalary(jsondata.getString("salary"));
                    companyJobBean.setCompany_name(jsondata.getString("company_name"));
                    companyJobBean.setIs_urgent(jsondata.getString("is_urgent"));
                    companyJobBean.setUpdatetime(jsondata.getString("updatetime"));
                    companyJobBean.setPart_status(jsondata.getString("part_status"));

                    listCollection.add(companyJobBean);
                }
                adapter.notifyDataSetChanged();
            } else {

            }
        } else {

        }
    }

    /**
     * 切换不同的view界面
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
                JSONArray data = json.getJSONArray("data");
                if (data.size() == 0) {//没有收藏职位的界面
                    frameLayout.removeView(loadingView);
                    frameLayout.addView(noCollectView);
                } else {//有收藏职位的界面
                    frameLayout.removeView(loadingView);
                    frameLayout.addView(successView);
                    if (data.size() < 15 && current_page.equals("1")) {
                        xrefreshview.setPullLoadEnable(false);

                    }
                    //解析数据
                    parsCollectionData(data);
                }
            } else if (codes.equals("0")) {
                //TODO
            } else if (codes.equals("-1")) {
                Intent intent=new Intent(JobCollectionActivity.this, WXEntryActivity.class);
                intent.putExtra("register", "loginMine");
                startActivity(intent);
                finish();
            }
            if (msg!=null&&!"".equals(msg)){
                Toast.makeText(JobCollectionActivity.this,msg,Toast.LENGTH_SHORT).show();
            }

        } else {
            frameLayout.removeView(loadingView);
            frameLayout.addView(emptyView);
            Toast.makeText(JobCollectionActivity.this, "联网超时", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析收藏职位的简历
     */
    MyJobCollectionAdapter adapter;
    private List<CompanyJobBean> listCollection = new ArrayList();

    private void parsCollectionData(JSONArray data) {

        for (int i = 0; i < data.size(); i++) {

            CompanyJobBean companyJobBean = new CompanyJobBean();
            JSONObject jsondata = data.getJSONObject(i);
            companyJobBean.setJob_id(jsondata.getString("job_id"));
            companyJobBean.setJob_title(jsondata.getString("job_title"));
            companyJobBean.setLocation_name(jsondata.getString("location_name"));
            companyJobBean.setWork_year_name(jsondata.getString("work_year_name"));
            companyJobBean.setEducation_name(jsondata.getString("education_name"));
            companyJobBean.setSalary(jsondata.getString("salary"));
            companyJobBean.setCompany_name(jsondata.getString("company_name"));
            companyJobBean.setIs_urgent(jsondata.getString("is_urgent"));
            companyJobBean.setUpdatetime(jsondata.getString("updatetime"));
            companyJobBean.setPart_status(jsondata.getString("part_status"));
            listCollection.add(companyJobBean);

        }

        adapter = new MyJobCollectionAdapter(listCollection,JobCollectionActivity.this);
        recycler_view_test_rv.setAdapter(adapter);

        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);

        adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));

        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {//下拉刷新
                super.onRefresh(isPullDown);
                pullToRefreshData();//下拉刷新
            }

            @Override
            public void onLoadMore(boolean isSilence) {//上垃加载
                super.onLoadMore(isSilence);
                loadMoreData();//上垃加载更多

            }
        });
    }

    /**
     * 上垃加载更多
     */
    private void loadMoreData() {
        if (next_page_url == null) {
            xrefreshview.setLoadComplete(true);
        } else {
            loadMoreDataAgain();
        }
    }

    /**
     * 下拉刷新数据
     */
    private void pullToRefreshData() {
        if ("1".equals(current_page)) {
            refreshOnePageData();//当前页为第一页的时候的下拉刷新
        } else {
            if (prev_page_url == null) {
                xrefreshview.stopRefresh();
            } else {
                refreshDataFromService();//下拉刷新获取新数据
            }
        }
    }

    /**
     * 当前页为第一页的时候的下拉刷新
     */
    private static final int ONE_PAGE = 2;

    private void refreshOnePageData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncod = new FormEncodingBuilder();
        formEncod.add("page", "1");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.FAVORITES)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncod.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("iiiiooooo", str);
                handler.sendEmptyMessage(ONE_PAGE);
            }
        });
    }

    /**
     * 解析当前也为第一页的时候下拉刷新的数据
     */
    private void parsOnePageData() {
        if (code == 200) {
            JSONObject jsonObject1 = JSON.parseObject(str);
            String codes = jsonObject1.getString("code");
            String msg = jsonObject1.getString("msg");
            current_page = jsonObject1.getString("current_page");
            next_page_url = jsonObject1.getString("next_page_url");
            prev_page_url = jsonObject1.getString("prev_page_url");
            if ("1".equals(codes)) {
                JSONArray data = jsonObject1.getJSONArray("data");
                listCollection.clear();
                for (int i = 0; i < data.size(); i++) {

                    CompanyJobBean companyJobBean = new CompanyJobBean();
                    JSONObject jsondata = data.getJSONObject(i);
                    companyJobBean.setJob_id(jsondata.getString("job_id"));
                    companyJobBean.setJob_title(jsondata.getString("job_title"));
                    companyJobBean.setLocation_name(jsondata.getString("location_name"));
                    companyJobBean.setWork_year_name(jsondata.getString("work_year_name"));
                    companyJobBean.setEducation_name(jsondata.getString("education_name"));
                    companyJobBean.setSalary(jsondata.getString("salary"));
                    companyJobBean.setCompany_name(jsondata.getString("company_name"));
                    companyJobBean.setIs_urgent(jsondata.getString("is_urgent"));
                    companyJobBean.setUpdatetime(jsondata.getString("updatetime"));
                    companyJobBean.setPart_status(jsondata.getString("part_status"));
                    listCollection.add(companyJobBean);

                }

                if (next_page_url == null || "".equals(next_page_url)) {
                    xrefreshview.setLoadComplete(true);
                    xrefreshview.setPullLoadEnable(false);
                }
                xrefreshview.stopRefresh();
                adapter.notifyDataSetChanged();

            } else if ("-1".equals(codes)) {

            }
            if (msg != null && !"".equals(msg)) {
                Toast.makeText(JobCollectionActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

        } else {

        }
    }

    /**
     * 下拉刷新获取新数据
     */
    private void refreshDataFromService() {
        SharedPreferences shares = getSharedPreferences("Activity", MODE_PRIVATE);
        String api_token = shares.getString("api_token", "");
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
                Log.i("aaaaaaa", str);
                handler.sendEmptyMessage(300);
            }
        });
    }

    /**
     * 加载更多数据
     */
    private void loadMoreDataAgain() {
        if (next_page_url == null) {
            xrefreshview.setLoadComplete(true);
        } else {
            int in = Integer.parseInt(current_page) + 1;
            OkHttpClient okhttp = new OkHttpClient();
            FormEncodingBuilder form = new FormEncodingBuilder();
            form.add("page", String.valueOf(in));
            final Request request = new Request.Builder()
                    .url(next_page_url)
                    .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                    .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + UiUtils.getApiToken())
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
                    Log.i("11111", str);
                    handler.sendEmptyMessage(130);
                }
            });
        }
    }
}
