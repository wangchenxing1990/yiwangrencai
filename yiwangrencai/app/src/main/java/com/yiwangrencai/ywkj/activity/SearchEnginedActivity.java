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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.ContextObjectDeserializer;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
//import com.baidu.platform.comapi.map.F;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.wxapi.WXEntryActivity;
import com.yiwangrencai.ywkj.adapter.MySearchEngineAdapter;
import com.yiwangrencai.ywkj.bean.SearcherBean;
import com.yiwangrencai.ywkj.content.ContentUrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */

public class SearchEnginedActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_search_engined;
    }

    String api_token;

    @Override
    public void initData() {
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");
        gainDataFromService();//从服务器获取搜索器的数据
    }

    /**
     * 从服务器获取搜索器数据
     */
    String str;
    int code;

    private void gainDataFromService() {

        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.SEARCH_ENGINE)
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
                Log.i("获取搜索器地数据", str);
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
                    switchView();//根据获取到的数据改变界面
                    break;
                case 120:
                    parsGainLoadNewData();//解析上啦加载获取的更多数据
                    break;
                case 130:
                    parsRefreshNewDate();//解析下拉刷新获取的更多数据
                    break;
                case ONE_PAGE://解析当前页面是第一个页面时的数据
                    parsOnePageData();//解析当前页面是第一个页面时的数据
                    break;
                case ERROR_CODE:
                    Toast.makeText(SearchEnginedActivity.this, "联网超时", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    /**
     * 根据获取的数据改变界面
     */
    String current_page;
    String next_page_url;
    String prev_page_url;
    private MySearchEngineAdapter adapter;
    private List<SearcherBean> searchList = new ArrayList<>();

    private void switchView() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codes = json.getString("code");
            String msg = json.getString("msg");
            if ("1".equals(codes)) {
                JSONArray jsonArray = json.getJSONArray("data");
                if (jsonArray.size() == 0) {
                    framlayout.removeView(loadingView);
                    framlayout.addView(emptyView);
                } else {
                    framlayout.removeView(loadingView);
                    framlayout.addView(successView);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsondate = jsonArray.getJSONObject(i);
                        SearcherBean searchBean = new SearcherBean();
                        searchBean.setId(jsondate.getString("id"));
                        searchBean.setSearch_name(jsondate.getString("search_name"));
                        searchBean.setUpdated_at(jsondate.getString("updated_at"));
                        searchList.add(searchBean);
                    }
                    if (next_page_url == null) {
                        xrefreshview.setPullLoadEnable(false);
                    }
                    adapter = new MySearchEngineAdapter(SearchEnginedActivity.this, searchList);
                    recycler_view_test_rv.setAdapter(adapter);

                    xrefreshview.setAutoLoadMore(false);
                    xrefreshview.setPinnedTime(1000);
                    xrefreshview.setMoveForHorizontal(true);

                    adapter.setCustomLoadMoreView(new XRefreshViewFooter(SearchEnginedActivity.this));
                    xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
                        @Override
                        public void onRefresh(boolean isPullDown) {
                            super.onRefresh(isPullDown);
                            refreshNewData();//下拉刷新
                        }

                        @Override
                        public void onLoadMore(boolean isSilence) {
                            super.onLoadMore(isSilence);
                            loadMoreNewData();//上垃加载
                        }
                    });

                    //点击条目进入下一个界面
                    adapter.setOnItemClickListener(new MySearchEngineAdapter.OnRecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClick(View v, SearcherBean data) {
                            Intent intent = new Intent(SearchEnginedActivity.this, EditSearchEnginedActivity.class);
                            intent.putExtra("params", "item");
                            intent.putExtra("id", data.getId());
                            startActivity(intent);
                        }
                    });
                }
            } else if ("-1".equals(codes)) {
                Intent intent = new Intent(SearchEnginedActivity.this, WXEntryActivity.class);
                intent.putExtra("register", "loginMine");
                startActivity(intent);
                finish();
            } else if ("0".equals(codes)) {

            }

            if (msg != null && !"".equals(msg)) {
                Toast.makeText(SearchEnginedActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * 上垃加载
     */
    private void loadMoreNewData() {
        if (next_page_url == null) {
            xrefreshview.setLoadComplete(true);
        } else {
            OkHttpClient okhttp = new OkHttpClient();
            FormEncodingBuilder formEncod = new FormEncodingBuilder();
            Request request = new Request.Builder()
                    .url(next_page_url)
                    .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                    .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                    .post(formEncod.build())
                    .build();
            okhttp.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    str = response.body().string();
                    code = response.code();
                    Log.i("上垃加载更多数据", str);
                    handler.sendEmptyMessage(120);
                }
            });

        }
    }

    /**
     * 解析上垃加载获取的更多数据
     */
    private void parsGainLoadNewData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codes = json.getString("code");
            if ("1".equals(codes)) {
                JSONArray jaonArray = json.getJSONArray("data");
                for (int i = 0; i < jaonArray.size(); i++) {
                    JSONObject jsondate = jaonArray.getJSONObject(i);
                    SearcherBean searchBean = new SearcherBean();
                    searchBean.setId(jsondate.getString("id"));
                    searchBean.setSearch_name(jsondate.getString("search_name"));
                    searchBean.setUpdated_at(jsondate.getString("updated_at"));
                    searchList.add(searchBean);
                }
                xrefreshview.setLoadComplete(false);
                adapter.notifyDataSetChanged();

            } else {
                Toast.makeText(SearchEnginedActivity.this, "上拉刷新数据失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SearchEnginedActivity.this, "上拉刷新数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 下拉刷新
     */
    private void refreshNewData() {
        if ("1".equals(current_page)) {
            refreshOnePage();//当前页面是第一个页面的时的刷新数据
        } else {

            if (prev_page_url == null) {
                xrefreshview.stopRefresh();
            } else {
                OkHttpClient okhttp = new OkHttpClient();
                FormEncodingBuilder formcoding = new FormEncodingBuilder();
                Request request = new Request.Builder()
                        .url(prev_page_url)
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
                        Log.i("喜爱拉刷新获取更多数据", str);
                        handler.sendEmptyMessage(130);
                    }
                });
            }
        }
    }

    /**
     * 当前页面是第一个页面得时候的下拉刷新
     */
    private static final int ONE_PAGE = 1;
    private static final int ERROR_CODE = 2;

    private void refreshOnePage() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        //formEncoding.add("page", "1");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.SEARCH_ENGINE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncoding.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(ERROR_CODE);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("aaaaaaaaaa", str);
                handler.sendEmptyMessage(ONE_PAGE);
            }
        });
    }

    /**
     * 解析当前页面是第一个页面时的数据
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
                searchList.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsondate = jsonArray.getJSONObject(i);
                    SearcherBean searchBean = new SearcherBean();
                    searchBean.setId(jsondate.getString("id"));
                    searchBean.setSearch_name(jsondate.getString("search_name"));
                    searchBean.setUpdated_at(jsondate.getString("updated_at"));
                    searchList.add(searchBean);
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
                Toast.makeText(SearchEnginedActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        } else {

        }

    }

    /**
     * 解析下拉刷新获取的更多数据
     */
    private void parsRefreshNewDate() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codes = json.getString("code");
            if ("1".equals(codes)) {
                JSONArray jsonArray = json.getJSONArray("data");
                searchList.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsondate = jsonArray.getJSONObject(i);
                    SearcherBean searchBean = new SearcherBean();
                    searchBean.setId(jsondate.getString("id"));
                    searchBean.setSearch_name(jsondate.getString("search_name"));
                    searchBean.setUpdated_at(jsondate.getString("updated_at"));
                    searchList.add(searchBean);
                }
                xrefreshview.stopRefresh();
                xrefreshview.setLoadComplete(false);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(SearchEnginedActivity.this, "下拉刷新数据失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SearchEnginedActivity.this, "下拉刷新数据失败", Toast.LENGTH_SHORT).show();
        }

    }


    FrameLayout framlayout;
    View loadingView;
    View emptyView;
    View successView;

    @Override
    public void initView() {
        FrameLayout fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        TextView tv_add_save = (TextView) findViewById(R.id.tv_add_save);

        framlayout = (FrameLayout) findViewById(R.id.framlayout);
        loadingView = createLoadingView();
        emptyView = createEmptyView();
        successView = createSuccessView();

        framlayout.addView(loadingView);
        fram_job_back.setOnClickListener(this);
        tv_add_save.setOnClickListener(this);

    }

    /**
     * 创建加载成功的界面
     *
     * @return
     */
    private XRefreshView xrefreshview;
    private RecyclerView recycler_view_test_rv;

    private View createSuccessView() {
        View view = View.inflate(SearchEnginedActivity.this, R.layout.fram_listview, null);
        xrefreshview = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);

        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(this));
        xrefreshview.setPullLoadEnable(true);
        return view;
    }

    /**
     * 创建加载数据为空的界面
     *
     * @return
     */
    private View createEmptyView() {
        View view = View.inflate(SearchEnginedActivity.this, R.layout.empty_view_icon, null);
        TextView textview_click= (TextView) view.findViewById(R.id.textview_click);
        textview_click.setText("你还没有创建搜索器");
        return view;
    }

    /**
     * 创建正在加载的界面
     *
     * @return
     */
    private View createLoadingView() {
        View view = View.inflate(SearchEnginedActivity.this, R.layout.loading_view, null);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back://返回上一页
                finish();
                break;
            case R.id.tv_add_save://保存数据
                Intent intent = new Intent(SearchEnginedActivity.this, AddSearchEnginedActivity.class);
                intent.putExtra("params", "save");
                startActivity(intent);
                break;
        }
    }
}
