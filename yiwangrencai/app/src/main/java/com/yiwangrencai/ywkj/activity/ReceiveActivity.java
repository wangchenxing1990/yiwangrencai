package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
//import com.baidu.platform.comapi.map.F;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.mm.opensdk.modelbiz.AddCardToWXCardPackage;
import com.yiwangrencai.R;
import com.yiwangrencai.wxapi.WXEntryActivity;
import com.yiwangrencai.ywkj.adapter.MyReceivePartAdapter;
import com.yiwangrencai.ywkj.bean.PartApplyBean;
import com.yiwangrencai.ywkj.bean.PartTimeBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.RecycleViewDivider;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/3.
 */

public class ReceiveActivity extends BaseActiviyt {

    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;
    @Bind(R.id.fram_job_back)
    FrameLayout framJobBack;

    @Override
    public int getLayoutId() {
        return R.layout.actiivty_receive_part;
    }

    String api_token;

    @Override
    public void initData() {
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");
    }

    private View loadingView;
    private View successView;
    private View emptyView;

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingView = createLoadingView();
        emptyView= LayoutInflater.from(ReceiveActivity.this).inflate(R.layout.empty_view_icon,null);
        TextView textview_click= (TextView) emptyView.findViewById(R.id.textview_click);
        textview_click.setText("还没有收到公司的邀请");
        successView = createSuccessView();
        frameLayout.addView(loadingView);
        gainReceivePartData();//获取加载的数据
    }


    /**
     * 加载数据成功的数据
     *
     * @return
     */
    private View createSuccessView() {
        View view = View.inflate(ReceiveActivity.this, R.layout.fram_listview, null);
        xrefreshview = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);


        adapter = new MyReceivePartAdapter(list);
        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_test_rv.setBackgroundColor(0xffffffff);
        recycler_view_test_rv.addItemDecoration(new RecycleViewDivider(ReceiveActivity.this, LinearLayoutManager.HORIZONTAL));
        recycler_view_test_rv.setAdapter(adapter);

        adapter.setCustomLoadMoreView(new XRefreshViewFooter(ReceiveActivity.this));
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        xrefreshview.setPullLoadEnable(true);


        adapter.setOnItemClickListener(new MyReceivePartAdapter.OnRecyclerViewOnItemClickListener() {
            @Override
            public void onItemClick(View view, PartApplyBean.DataBean dataBean) {
                int statue=dataBean.getStatus();
                if (statue==0){
                    changePartApplyStatue(dataBean);//操作邀请记录状态
                }
                Intent intent=new Intent(ReceiveActivity.this,PartDetailActivity.class);
                intent.putExtra("id",dataBean.getPt_job_id()+"");
                intent.putExtra("status",dataBean.getStatus()+"");
                intent.putExtra("ids",dataBean.getId()+"");
                intent.putExtra("params","receive");
                startActivity(intent);
            }
        });

        //下拉刷新与上垃加载
        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                onRefreshDataFromToService();//下拉刷新
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                onLoadMoreData();//上垃加载更多数据
            }
        });

        return view;

    }

    /**
     *  操作邀请记录状态
     */
    private void changePartApplyStatue(PartApplyBean.DataBean dataBean) {
        OkHttpClient okhttp=new OkHttpClient();
        FormEncodingBuilder form=new FormEncodingBuilder();
        form.add("id", String.valueOf(dataBean.getId()));
        form.add("status","1");
        Request request=new Request.Builder()
                .url(ContentUrl.BASE_URL+ContentUrl.PART_SIGN_PROCESS_UPDATE)
                .addHeader(ContentUrl.ACCEPT,ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION,ContentUrl.BEAR+api_token)
                .post(form.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
             Log.i("sssssssss",response.body().string());
            }
        });
    }

    /**
     * 上垃加载更多数据
     */
    private void onLoadMoreData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(next_page_url)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(form.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                code = response.code();
                str = response.body().string();
                Log.i("上垃加载获取更多数据", str);
                handler.sendEmptyMessage(LOAD_MORE_DATA);
            }
        });
    }

    /**
     * 下拉刷新
     */
    private void onRefreshDataFromToService() {
        if (current_page != null && "1".equals(current_page)) {
            refreshCurrentPageOne();//当前页是第一页时的下拉刷新
        } else {
            if (prev_page_url == null || "".equals(prev_page_url)) {
                xrefreshview.stopRefresh();
            } else {
                refeshMoreData();//下拉刷新更多数据
            }
        }
    }

    /**
     * 当前页不是第一页时的下拉刷新
     */
    private void refeshMoreData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(prev_page_url)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(form.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("当前页不是第一页时的下拉刷新", str);
                handler.sendEmptyMessage(GAIN_MORE_DATA_REFRESH);
            }
        });
    }

    /**
     * 当前页是第一页时的下拉刷新
     */
    private void refreshCurrentPageOne() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.PART_SIGN_PROCESS_RECEIVE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(form.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                code = response.code();
                str = response.body().string();
                Log.i("当前页是第一页的下拉刷新获取的数据", str);
                handler.sendEmptyMessage(GAIN_CURRENT_PAGE_ONE);
            }
        });

    }

    /**
     * 正在加载的界面
     *
     * @return
     */
    XRefreshView xrefreshview;
    RecyclerView recycler_view_test_rv;
    MyReceivePartAdapter adapter;
    List<PartApplyBean.DataBean> list = new ArrayList<>();

    private View createLoadingView() {
        View view = View.inflate(ReceiveActivity.this, R.layout.loading_view, null);
        return view;
    }


    @OnClick(R.id.fram_job_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fram_job_back:
                finish();//返回上一个界面
                break;
        }
    }

    /**
     * 获取收到的兼职邀请的数据列表
     */
    String str;
    int code;

    private void gainReceivePartData() {
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.PART_SIGN_PROCESS_RECEIVE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
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
                Log.i("收到的兼职邀请的数据", str);
                handler.sendEmptyMessage(GAIN_RECEIVE_DATA);
            }
        });
    }

    /**
     * 解析收到兼职邀请的数据
     */
    String current_page = "";
    String next_page_url = "";
    String prev_page_url = "";

    private void parsGainReceivePartData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            String message = json.getString("msg");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");

            if ("1".equals(codes)) {

                frameLayout.removeView(loadingView);

                Gson gson = new Gson();
                PartApplyBean receivePartBean = gson.fromJson(str, PartApplyBean.class);
                if (receivePartBean.getData().size()==0) {
                    frameLayout.addView(emptyView);
                } else {
                    frameLayout.addView(successView);
                    adapter.setData(receivePartBean.getData(), false);
                    if (next_page_url == null || "".equals(next_page_url)) {
                        xrefreshview.setLoadComplete(true);
                        xrefreshview.setPullLoadEnable(false);
                    }

                    xrefreshview.stopRefresh();
                }
                }else if ("-1".equals(codes)) {
                    Intent intent = new Intent(ReceiveActivity.this, WXEntryActivity.class);
                    startActivity(intent);
                    finish();
                }

                if (message != null && !"".equals(message)) {
                    Toast.makeText(ReceiveActivity.this, message, Toast.LENGTH_SHORT).show();
                }


        } else {

        }
    }

    private static final int GAIN_RECEIVE_DATA = 0;
    private static final int GAIN_CURRENT_PAGE_ONE = 1;
    private static final int GAIN_MORE_DATA_REFRESH = 2;
    private static final int LOAD_MORE_DATA = 4;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GAIN_RECEIVE_DATA:
                    parsGainReceivePartData();//解析收到兼职邀请的数据
                    break;
                case GAIN_CURRENT_PAGE_ONE:
                    parsCurrentPageOneData();//解析当前页是第一页时的下拉刷新的数据
                    break;
                case GAIN_MORE_DATA_REFRESH:
                    parsCurrentPageOneData();//解析当前页不是第一页时的下拉刷新的数据
                    break;
                case LOAD_MORE_DATA:
                    parsLoadMoreData();//解析上垃加载获取更多的数据
                    break;
            }
        }
    };

    /**
     * 解析上拉加载获取更多的数据
     */
    private void parsLoadMoreData() {
        if (code==200){
            JSONObject json=JSON.parseObject(str);
            String  codes =json.getString("code");
            String message=json.getString("msg");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");

            if ("1".equals(codes)){
                Gson gson=new Gson();
                PartApplyBean partApplyBean=gson.fromJson(str,PartApplyBean.class);
                adapter.setAllData(partApplyBean.getData());
            }else if("-1".equals(codes)){
                Intent intent=new Intent(ReceiveActivity.this,WXEntryActivity.class);
                startActivity(intent);
            }
            if (message!=null&&!"".equals(message)){
                Toast.makeText(ReceiveActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        }else{

        }
    }

    /**
     * 解析当前页是第一页时的下拉刷新的数据
     */
    private void parsCurrentPageOneData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            String message = json.getString("msg");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            if ("1".equals(codes)) {
                Gson gson = new Gson();
                PartApplyBean partApplyBean = gson.fromJson(str, PartApplyBean.class);
                adapter.setData(partApplyBean.getData(), true);
                if (next_page_url == null || "".equals(next_page_url)) {
                    xrefreshview.setLoadComplete(true);
                    xrefreshview.setPullLoadEnable(false);
                }
                xrefreshview.stopRefresh();
            } else if ("-1".equals(codes)) {
                Intent intent = new Intent(ReceiveActivity.this, WXEntryActivity.class);
                startActivity(intent);
            }

            if (message != null && !"".equals(message)) {
                Toast.makeText(ReceiveActivity.this, message, Toast.LENGTH_SHORT).show();
            }

        } else {

        }
    }
}
