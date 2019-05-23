package com.yiwangrencai.ywkj.subPageView;

import android.content.Context;
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
import com.yiwangrencai.ywkj.activity.BaseApplication;
import com.yiwangrencai.ywkj.activity.WebViewActivity;
import com.yiwangrencai.ywkj.adapter.MyResumeGuideAdapter;
import com.yiwangrencai.ywkj.bean.CarerBean;
import com.yiwangrencai.ywkj.content.ContentUrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 职场八卦界面
 * Created by Administrator on 2017/4/17.
 */
public class CarerGossipPage extends BasePager {
    private String category;
    private Context context;

    public CarerGossipPage(Context context, String category) {
        super();
        this.category = category;
        this.context = context;
    }

    private View loadingView;
    private View emptyView;
    private View successView;
    FrameLayout framLayout;

    @Override
    public View initView() {
        framLayout = new FrameLayout(context);
        loadingView = createLoadingView();
        emptyView = createEmptyView();
        successView = createSuccessView();
        framLayout.removeAllViews();
        framLayout.addView(loadingView);
        return framLayout;
    }

    /**
     * 创建加载成功的界面
     *
     * @return
     */
    private XRefreshView xrefreshview;
    private RecyclerView recycler_view_test_rv;

    private View createSuccessView() {
        View view = View.inflate(context, R.layout.fram_listview, null);
        xrefreshview = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);
        recycler_view_test_rv.setBackgroundColor(0xffefefef);
        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    /**
     * 创建加载为空的界面
     *
     * @return
     */
    private View createEmptyView() {
        View view = View.inflate(context, R.layout.empty_view, null);
        return view;
    }

    /**
     * 创建正在加载界面
     *
     * @return
     */
    private View createLoadingView() {
        View view = View.inflate(context, R.layout.loading_view, null);
        return view;
    }

    String api_token;
    String str;
    int code;
    @Override
    public void initData() {
        SharedPreferences share = context.getSharedPreferences("Activity", Context.MODE_PRIVATE);
        api_token = share.getString("api_token", "");
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("category", category);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.NEWS)
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
                Log.i("职场八卦", str);
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
                    switchView();//根据请求返回的数据切换不同的界面
                    break;
                case 120:
                    parsLoadMoreData();//解析上垃加载更多的数据
                    break;
                case 130:
                    parsRefreshMoreData();//解析下拉刷新更多的数据
                    break;
                case ONE_PAGE:
                    parsOnePageData();//解析当前页面是第一个页面时的数据
                    break;
            }
        }
    };

    /**
     * 根据返回的数据切换不同的界面
     */
    MyResumeGuideAdapter adapter;
    private List<CarerBean> list = new ArrayList<>();
    private String current_page;
    private String next_page_url;
    private String prev_page_url;

    private void switchView() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codes = json.getString("code");
            String msg = json.getString("msg");
            if ("1".equals(codes)) {
                framLayout.removeView(loadingView);
                framLayout.removeAllViews();
                framLayout.addView(successView);
                JSONArray jsonArray = json.getJSONArray("data");
                if (next_page_url == null) {
                    xrefreshview.setPullLoadEnable(false);
                }
                list.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    CarerBean carerBean = new CarerBean();
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    carerBean.setId(jsonData.getString("id"));
                    carerBean.setTitle(jsonData.getString("title"));
                    carerBean.setCreated_at(jsonData.getString("created_at"));
                    carerBean.setReadnum(jsonData.getString("readnum"));
                    carerBean.setImg(jsonData.getString("img"));
                    list.add(carerBean);
                }

                adapter = new MyResumeGuideAdapter(context, list);
                recycler_view_test_rv.setAdapter(adapter);

                xrefreshview.setAutoLoadMore(false);
                xrefreshview.setPinnedTime(1000);
                xrefreshview.setMoveForHorizontal(true);

                adapter.setCustomLoadMoreView(new XRefreshViewFooter(context));

                //上拉刷新与加载
                xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
                    @Override
                    public void onRefresh(boolean isPullDown) {
                        super.onRefresh(isPullDown);
                        refreshMoreNewData();//下拉刷新更多数据
                    }

                    @Override
                    public void onLoadMore(boolean isSilence) {
                        super.onLoadMore(isSilence);
                        //上垃加载
                        loadMoreNewData();//上垃加载更多数据
                    }
                });

                adapter.setOnItemClickListener(new MyResumeGuideAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, CarerBean data) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("id", data.getId());
                        context.startActivity(intent);
                    }
                });

            } else if ("-1".equals(codes)) {

            } else if ("0".equals(codes)) {

            }

            if (msg != null && !"".equals(msg)) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

    /**
     * 上垃加载更多数据
     */
    private void loadMoreNewData() {
        if (next_page_url.isEmpty()) {
            xrefreshview.setLoadComplete(true);
        } else {
            OkHttpClient okHttpClient = new OkHttpClient();
            FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
            Request request = new Request.Builder()
                    .url(next_page_url)
                    .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                    .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
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
                    handler.sendEmptyMessage(120);

                }
            });
        }
    }

    /**
     * 解析上啦加载更多的数据
     */
    private void parsLoadMoreData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codes = json.getString("code");
            if ("1".equals(codes)) {
                framLayout.removeView(loadingView);
                framLayout.addView(successView);
                JSONArray jsonArray = json.getJSONArray("data");
                if (next_page_url == null) {
                    xrefreshview.setPullLoadEnable(false);
                }
                //list.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    CarerBean carerBean = new CarerBean();
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    carerBean.setId(jsonData.getString("id"));
                    carerBean.setTitle(jsonData.getString("title"));
                    carerBean.setCreated_at(jsonData.getString("created_at"));
                    carerBean.setReadnum(jsonData.getString("readnum"));
                    carerBean.setImg(jsonData.getString("img"));
                    list.add(carerBean);
                }
                xrefreshview.setLoadComplete(false);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(context, "上垃加载数据失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "上垃加载数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 下拉刷新获取更多数据
     */
    private void refreshMoreNewData() {
        if ("1".equals(current_page)) {
            refreshOnePageData();
        } else {
            if (prev_page_url == null) {
                xrefreshview.stopRefresh();
            } else {
                OkHttpClient okhttp = new OkHttpClient();
                FormEncodingBuilder formcoding = new FormEncodingBuilder();
                Request requesrt = new Request.Builder()
                        .url(prev_page_url)
                        .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                        .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                        .post(formcoding.build())
                        .build();
                okhttp.newCall(requesrt).enqueue(new Callback() {
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
        }
    }

    /**
     * 下拉刷新当前页面是第一个页面时的数据
     */
    private static final int ONE_PAGE=1;
    private void refreshOnePageData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncod = new FormEncodingBuilder();
        formEncod.add("category", category);
        formEncod.add("page", "1");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.NEWS)
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
                Log.i("strstrstrstrstrstr", str);
                handler.sendEmptyMessage(ONE_PAGE);
            }
        });
    }

    /**
     * 解析当前页面是第一个页面时下拉刷新获取的数据
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
                list.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    CarerBean carerBean = new CarerBean();
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    carerBean.setId(jsonData.getString("id"));
                    carerBean.setTitle(jsonData.getString("title"));
                    carerBean.setCreated_at(jsonData.getString("created_at"));
                    carerBean.setReadnum(jsonData.getString("readnum"));
                    carerBean.setImg(jsonData.getString("img"));
                    list.add(carerBean);
                }
                if (next_page_url==null||"".equals(next_page_url)){
                    xrefreshview.setLoadComplete(true);
                    xrefreshview.setPullLoadEnable(false);
                }

                xrefreshview.stopRefresh();
                adapter.notifyDataSetChanged();

            } else if ("-1".equals(codes)) {

            }

            if (msg != null && !"".equals(msg)) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }
    /**
     * 解析下拉刷新获取的数据
     */
    private void parsRefreshMoreData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codes = json.getString("code");
            if ("1".equals(codes)) {
                framLayout.removeView(loadingView);
                framLayout.addView(successView);
                JSONArray jsonArray = json.getJSONArray("data");
                if (next_page_url == null) {
                    xrefreshview.setPullLoadEnable(false);
                }
                list.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    CarerBean carerBean = new CarerBean();
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    carerBean.setId(jsonData.getString("id"));
                    carerBean.setTitle(jsonData.getString("title"));
                    carerBean.setCreated_at(jsonData.getString("created_at"));
                    carerBean.setReadnum(jsonData.getString("readnum"));
                    carerBean.setImg(jsonData.getString("img"));
                    list.add(carerBean);
                }
                adapter.notifyDataSetChanged();
                xrefreshview.stopRefresh();
                xrefreshview.setLoadComplete(false);
            } else {
                Toast.makeText(context, "下拉加载数据失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "下拉加载数据失败", Toast.LENGTH_SHORT).show();
        }
    }
}
