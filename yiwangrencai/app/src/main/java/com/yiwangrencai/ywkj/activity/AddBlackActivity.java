package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
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
import com.yiwangrencai.ywkj.adapter.MyCompayBlackAdapter;
import com.yiwangrencai.ywkj.bean.BlackNumBean;
import com.yiwangrencai.ywkj.content.ContentUrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/27.
 */

public class AddBlackActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.add_black_activity;
    }

    private ArrayList<String> list;

    @Override
    public void initData() {

    }

    private FrameLayout fram_job_back;
    private FrameLayout framelayoutempty;
    private TextView textsearch;
    private EditText editsearch;
    private View emptyView;
    private View successView;

    @Override
    public void initView() {
        Intent intent = getIntent();
        list = (ArrayList<String>) intent.getExtras().getSerializable("list");

        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        textsearch = (TextView) findViewById(R.id.textsearch);
        editsearch = (EditText) findViewById(R.id.editsearch);

        framelayoutempty = (FrameLayout) findViewById(R.id.framelayoutempty);

        emptyView = createEmptyView();
        successView = createSuccessView();

        fram_job_back.setOnClickListener(this);
        textsearch.setOnClickListener(this);

    }

    private XRefreshView xrefreshview;
    private RecyclerView recycler_view_test_rv;

    private View createSuccessView() {
        View view = View.inflate(this, R.layout.fram_listview, null);
        xrefreshview = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);
        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(AddBlackActivity.this));
        xrefreshview.setPullLoadEnable(true);

        return view;
    }


    /**
     * 创建搜索前的界面
     *
     * @return
     */
    private View createEmptyView() {
        View view = View.inflate(this, R.layout.fram_company_empty, null);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back:  //返回
                Intent intent = new Intent(AddBlackActivity.this, BlacknumActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.textsearch://搜索按钮
                //搜索公司
                searchCompanyName();
                break;

        }
    }

    /**
     * 搜索公司
     */
    String str;
    int code;
    String api_token;
    String companyName;

    private void searchCompanyName() {

        companyName = editsearch.getText().toString().trim();
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");

        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        formEncodingBuilder.add("kw", companyName);
        formEncodingBuilder.add("page", "1");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.SHIELD_SEARCH)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncodingBuilder.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.print("=============" + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("------------", str);
                handler.sendEmptyMessage(100);
            }
        });

    }

    String current_page;
    String next_page_url;
    String prev_page_url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    if (code == 200) {
                        framelayoutempty.removeAllViews();
                        framelayoutempty.addView(successView);
                        JSONObject json = JSON.parseObject(str);
                        current_page = json.getString("current_page");
                        next_page_url = json.getString("next_page_url");
                        prev_page_url = json.getString("prev_page_url");
                        String codes = json.getString("code");
                        String msgs = json.getString("msg");

                        if ("1".equals(codes)) {
                            JSONArray jsonData = json.getJSONArray("data");
                            parseJsonArray(jsonData);
                        } else if ("-1".equals(codes)) {
                            Intent intent = new Intent(AddBlackActivity.this, WXEntryActivity.class);
                            startActivity(intent);
                        }
                        if (msgs != null && !"".equals(msgs)) {
                            Toast.makeText(AddBlackActivity.this, msgs, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(AddBlackActivity.this, "搜索公司失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 130:
                    if (code == 200) {
                        JSONObject json = JSON.parseObject(str);
                        current_page = json.getString("current_page");
                        next_page_url = json.getString("next_page_url");
                        prev_page_url = json.getString("prev_page_url");
                        String codes = json.getString("code");
                        if ("1".equals(codes)) {
                            JSONArray jsonData = json.getJSONArray("data");
                            numList.clear();
                            for (int i = 0; i < jsonData.size(); i++) {
                                BlackNumBean blackNumbean = new BlackNumBean();
                                blackNumbean.setUid(jsonData.getJSONObject(i).getString("uid"));
                                blackNumbean.setCompany_name(jsonData.getJSONObject(i).getString("company_name"));
                                numList.add(blackNumbean);
                            }
                            xrefreshview.stopRefresh();
                            xrefreshview.setLoadComplete(false);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(AddBlackActivity.this, "下拉刷新失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddBlackActivity.this, "下拉刷新失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 140:
                    if (code == 200) {
                        JSONObject json = JSON.parseObject(str);
                        current_page = json.getString("current_page");
                        next_page_url = json.getString("next_page_url");
                        prev_page_url = json.getString("prev_page_url");
                        String codes = json.getString("code");
                        if ("1".equals(codes)) {
                            JSONArray jsonData = json.getJSONArray("data");
                            for (int i = 0; i < jsonData.size(); i++) {
                                BlackNumBean blackNumbean = new BlackNumBean();
                                blackNumbean.setUid(jsonData.getJSONObject(i).getString("uid"));
                                blackNumbean.setCompany_name(jsonData.getJSONObject(i).getString("company_name"));
                                numList.add(blackNumbean);
                            }
                            xrefreshview.setLoadComplete(true);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(AddBlackActivity.this, "下拉刷新失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddBlackActivity.this, "下拉刷新失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ONE_PAGE://解析数据为第一个界面时的数据
                    parsOnePageData();//解析当前页是第一页时的数据
                    break;
            }
        }
    };


    private List<BlackNumBean> numList = new ArrayList<>();
    /**
     * 解析数据
     *
     * @param jsonData
     */
    private MyCompayBlackAdapter adapter;

    private void parseJsonArray(JSONArray jsonData) {
        numList.clear();
        for (int i = 0; i < jsonData.size(); i++) {
            BlackNumBean blackNumbean = new BlackNumBean();
            blackNumbean.setUid(jsonData.getJSONObject(i).getString("uid"));
            blackNumbean.setCompany_name(jsonData.getJSONObject(i).getString("company_name"));
            numList.add(blackNumbean);
        }
        if (next_page_url == null) {
            xrefreshview.setLoadComplete(true);
            xrefreshview.setPullLoadEnable(false);
        }
        adapter = new MyCompayBlackAdapter(numList, list, this);
        recycler_view_test_rv.setAdapter(adapter);

        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);

        adapter.setCustomLoadMoreView(new XRefreshViewFooter(AddBlackActivity.this));
        //   下拉刷新与上垃加载
        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                refreshMoreNewData();//下拉刷新更多数据
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                downMoreNewData();//上垃加载更多数据
            }
        });

    }

    /**
     * 上拉加载更多数据
     */
    private void downMoreNewData() {
        if (next_page_url == null) {
            xrefreshview.setLoadComplete(true);
        } else {
            OkHttpClient okHttpClient = new OkHttpClient();
            FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
            Request request = new Request.Builder()
                    .url(ContentUrl.BASE_URL + next_page_url)
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
                    Log.i("aaaaaaaaaaa", str);
                    handler.sendEmptyMessage(140);

                }
            });
        }
    }

    /**
     * 下拉刷新更多数据
     */
    private void refreshMoreNewData() {
        if ("1".equals(current_page)) {
            refreshOneData();//刷新当前页是第一页时的下拉刷新
        } else {
            if (prev_page_url == null) {
                xrefreshview.stopRefresh();
            } else {
                refreshMoreDataFromService();//从服务器获取更多的数据
            }
        }
    }

    /**
     * 刷新当前页是第一页时的下拉刷新
     */
    private static final int ONE_PAGE = 1;
    private static final int ERROR_CODE = 2;

    private void refreshOneData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("kw", companyName);
        formEncoding.add("page", "1");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.SHIELD_SEARCH)
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
                Log.i("assasaaadassadsad", str);
                handler.sendEmptyMessage(ONE_PAGE);
            }
        });
    }

    /**
     * 解析当前页是第一页时的数据
     */
    private void parsOnePageData() {
        if (code == 200) {
            JSONObject jsonObject = JSON.parseObject(str);
            String msg = jsonObject.getString("msg");
            String codes = jsonObject.getString("code");
            current_page = jsonObject.getString("current_page");
            next_page_url = jsonObject.getString("next_page_url");
            prev_page_url = jsonObject.getString("prev_page_url");
            if ("1".equals(codes)) {
                JSONArray jsonData = jsonObject.getJSONArray("data");
                numList.clear();
                for (int i = 0; i < jsonData.size(); i++) {
                    BlackNumBean blackNumbean = new BlackNumBean();
                    blackNumbean.setUid(jsonData.getJSONObject(i).getString("uid"));
                    blackNumbean.setCompany_name(jsonData.getJSONObject(i).getString("company_name"));
                    numList.add(blackNumbean);
                }

                if (next_page_url == null || "".equals(next_page_url)) {
                    xrefreshview.setLoadComplete(true);
                    xrefreshview.setPullLoadEnable(false);
                }
                xrefreshview.stopRefresh();
                adapter.notifyDataSetChanged();
            } else if ("-1".equals(codes)) {
//                Intent intent=new Intent(AddBlackActivity.this,WXEntryActivity.class);
//                startActivity(intent);
            }

            if (msg != null && !"".equals(msg)) {
                Toast.makeText(AddBlackActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(AddBlackActivity.this, "联网超时", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 下拉刷新
     */
    private void refreshMoreDataFromService() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncod = new FormEncodingBuilder();

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + prev_page_url)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncod.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (e.getMessage() != null) {
                    handler.sendEmptyMessage(11111);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("aaaaaaaaaa", str);
                handler.sendEmptyMessage(130);
            }
        });
    }
}
