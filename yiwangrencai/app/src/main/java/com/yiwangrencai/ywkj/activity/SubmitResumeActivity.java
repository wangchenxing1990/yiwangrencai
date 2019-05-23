package com.yiwangrencai.ywkj.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.SumPathEffect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.hss01248.dialog.StyledDialog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.wxapi.WXEntryActivity;
import com.yiwangrencai.ywkj.adapter.MySubmitAdapter;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.bean.HomePager;
import com.yiwangrencai.ywkj.bean.SubmitBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.jmessage.JGApplication;
import com.yiwangrencai.ywkj.tools.UiUtils;
import com.yiwangrencai.ywkj.view.LoginOffPopuWindow;
import com.yiwangrencai.ywkj.view.MyPopuwindown;
import com.yiwangrencai.ywkj.view.RecycleViewDivider;
import com.yiwangrencai.ywkj.view.SimpleDividerItemDecoration;
import com.yiwangrencai.ywkj.view.SwipeItemLayout;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */
public class SubmitResumeActivity extends BaseActiviyt implements View.OnClickListener {

    public SubmitBean data;

    @Override
    public int getLayoutId() {
        return R.layout.activity_submit_resume;
    }

    @Override
    public void initData() {
        gainDataFromService();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    if (code == 200) {
                        parsData();
                    } else {
                        frameLayout.removeView(loadingView);
                        frameLayout.addView(emptyView);
                    }
                    break;
                case 120:
                    frameLayout.removeView(loadingView);
                    frameLayout.addView(emptyView);
                    Toast.makeText(SubmitResumeActivity.this, "联网失败,请稍后重试", Toast.LENGTH_SHORT).show();
                    break;
                case 130:
                    loadMoreResult();
                    break;
                case 300:
                    parsRefreshData();//解析加载更多数据
                    break;
                case 30:
                    parsRepealResume();//撤销简历成功
                    break;
                case ONE_PAGE:
                    parsOnePage();//解析当前页为第一页的时候的下拉刷新
                    break;
            }
        }
    };


    /**
     * 撤销简历成功
     */
    private void parsRepealResume() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            String msg = json.getString("msg");
            StyledDialog.dismissLoading();
            if ("1".equals(codes)) {
                adapter.notifyDataSetChanged();
            } else {
            }
            if (msg != null && !"".equals(msg)) {
                Toast.makeText(SubmitResumeActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        } else {
            StyledDialog.dismissLoading();
            Toast.makeText(SubmitResumeActivity.this, "撤销简历失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示是否退出选项按钮
     */
    LoginOffPopuWindow loginOffWindow;

    private void showPopuwindow(SubmitBean data) {
        this.data = data;
        popupWindow.dismiss();
        loginOffWindow = new LoginOffPopuWindow(SubmitResumeActivity.this, R.layout.login_off_popuwindown);
        View view = loginOffWindow.getView();
        loginOffWindow.showAtLocation(SubmitResumeActivity.this.findViewById(R.id.ll_popuwind), Gravity.CENTER, 0, 0);
        TextView textview = (TextView) view.findViewById(R.id.textview);
        textview.setText("确定撤销简历吗?");

        TextView tv_off_cancel = (TextView) view.findViewById(R.id.tv_off_cancel);
        TextView tv_off_sure = (TextView) view.findViewById(R.id.tv_off_sure);
        tv_off_cancel.setOnClickListener(this);
        tv_off_sure.setOnClickListener(this);

    }

    /**
     * 解析加载更多数据
     */
    private void parsRefreshData() {
        if (codess == 200) {
            JSONObject json = JSON.parseObject(strss);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codess = json.getString("code");
            if ("1".equals(codess)) {
                listResume.clear();
                JSONArray jsonArray = json.getJSONArray("data");
                for (int i = 0; i < jsonArray.size(); i++) {
                    SubmitBean submitBean = new SubmitBean();
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    submitBean.setId(jsonData.getString("id"));
                    submitBean.setJob_id(jsonData.getString("job_id"));
                    submitBean.setStatus(jsonData.getString("status"));
                    submitBean.setCreated_at(jsonData.getString("created_at"));
                    submitBean.setJob_title(jsonData.getString("job_title"));
                    submitBean.setCompany_name(jsonData.getString("company_name"));
                    listResume.add(submitBean);
                }
                adapter.notifyDataSetChanged();
                xrefreshview.stopRefresh();
                xrefreshview.setLoadComplete(false);
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
                    SubmitBean submitBean = new SubmitBean();
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    submitBean.setId(jsonData.getString("id"));
                    submitBean.setJob_id(jsonData.getString("job_id"));
                    submitBean.setStatus(jsonData.getString("status"));
                    submitBean.setCreated_at(jsonData.getString("created_at"));
                    submitBean.setJob_title(jsonData.getString("job_title"));
                    submitBean.setCompany_name(jsonData.getString("company_name"));
                    listResume.add(submitBean);
                }
                adapter.notifyDataSetChanged();
            } else {

            }

        } else {

        }
    }

    List<SubmitBean> listResume = new ArrayList<>();
    MySubmitAdapter adapter;
    String next_page_url;
    String prev_page_url;
    String current_page;
    PopupWindow popupWindow;

    private void parsData() {
        JSONObject jsonObject = JSON.parseObject(str);
        String codes = jsonObject.getString("code");
        next_page_url = jsonObject.getString("next_page_url");
        prev_page_url = jsonObject.getString("prev_page_url");
        current_page = jsonObject.getString("current_page");
        String msg = jsonObject.getString("msg");
        if (codes.equals("1")) {
            frameLayout.removeView(loadingView);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if (jsonArray.size() == 0) {
                frameLayout.addView(emptyResumeView);
            } else {
                frameLayout.addView(successView);
                for (int i = 0; i < jsonArray.size(); i++) {
                    SubmitBean submitBean = new SubmitBean();
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    submitBean.setId(jsonData.getString("id"));
                    submitBean.setJob_id(jsonData.getString("job_id"));
                    submitBean.setStatus(jsonData.getString("status"));
                    submitBean.setCreated_at(jsonData.getString("created_at"));
                    submitBean.setJob_title(jsonData.getString("job_title"));
                    submitBean.setCompany_name(jsonData.getString("company_name"));
                    listResume.add(submitBean);
                }
            }

        } else if ("-1".equals(codes)) {
            frameLayout.removeView(loadingView);
            frameLayout.addView(emptyView);
            Intent intent = new Intent(SubmitResumeActivity.this, WXEntryActivity.class);
            intent.putExtra("register", "loginMine");
            startActivity(intent);
            finish();
        }

        if (msg != null && !"".equals(msg)) {
            Toast.makeText(SubmitResumeActivity.this, msg, Toast.LENGTH_SHORT).show();
        }

        adapter = new MySubmitAdapter(listResume, this);
        recycler_view_test_rv.setAdapter(adapter);

        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        xrefreshview.setPullLoadEnable(true);

        adapter.setCustomLoadMoreView(new XRefreshViewFooter(SubmitResumeActivity.this));
        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                refreshMoreData();//下拉刷新
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                downLoadMoreData();//上垃加载更多
            }
        });
    }

    /**
     * 撤销投递的简历
     *
     * @param
     */
    MyPopuwindown myPopuwindown;

    private void reapelSubmitResume() {
        StyledDialog.buildLoading().show();
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder foem = new FormEncodingBuilder();
        foem.add("id", data.getId());
        foem.add("status", "4");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_SEND_UPDATE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(foem.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("撤销简历成功的数据", str);
                handler.sendEmptyMessage(30);
                listResume.remove(data);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    /**
     * 上拉加载更多
     */
    private void downLoadMoreData() {
        if (next_page_url == null) {//表示完成上垃加载
            xrefreshview.setLoadComplete(true);
        } else {//还有数据还有可以进行加载更多
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

    /**
     * 下拉刷新
     */
    private void refreshMoreData() {
        if ("1".equals(current_page)) {//刷新第一页
            refreshOnePage();
        } else {//刷新的不是第一页
            if (prev_page_url == null) {
                xrefreshview.stopRefresh();
            } else {
                refreshData();//下拉刷新数据
            }
        }

    }

    /**
     * 刷新的为第一页
     */
    private static final int ONE_PAGE = 1;

    private void refreshOnePage() {


        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formd = new FormEncodingBuilder();
        formd.add("page", "1");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_SEND)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formd.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("ssssaaaa", str);
                handler.sendEmptyMessage(ONE_PAGE);
            }
        });
    }

    /**
     * 解析当前页为第一页的时候的下拉刷新
     */
    private void parsOnePage() {
        if (code == 200) {
            JSONObject jsonObject = JSON.parseObject(str);
            String codes = jsonObject.getString("code");
            String msg = jsonObject.getString("msg");
            next_page_url = jsonObject.getString("next_page_url");
            prev_page_url = jsonObject.getString("prev_page_url");
            current_page = jsonObject.getString("current_page");
            if ("1".equals(codes)) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                listResume.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    SubmitBean submitBean = new SubmitBean();
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    submitBean.setId(jsonData.getString("id"));
                    submitBean.setJob_id(jsonData.getString("job_id"));
                    submitBean.setStatus(jsonData.getString("status"));
                    submitBean.setCreated_at(jsonData.getString("created_at"));
                    submitBean.setJob_title(jsonData.getString("job_title"));
                    submitBean.setCompany_name(jsonData.getString("company_name"));
                    listResume.add(submitBean);
                }
                if (next_page_url == null || "".equals(next_page_url)) {
                    xrefreshview.setLoadComplete(true);
                    xrefreshview.setPullLoadEnable(false);
                }
                adapter.notifyDataSetChanged();
                xrefreshview.stopRefresh();
                //aaa
            } else {

            }
            if (msg != null && !"".equals(msg)) {
                Toast.makeText(SubmitResumeActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

    /**
     * 下拉刷新数据
     */
    String strss;
    int codess;

    private void refreshData() {
        SharedPreferences shares = getSharedPreferences("Activity", MODE_PRIVATE);
        String api_token = shares.getString("api_token", "");
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formcom = new FormEncodingBuilder();
        Request requests = new Request.Builder()
                .url(prev_page_url)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formcom.build())
                .build();
        okhttp.newCall(requests).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strss = response.body().string();
                codess = response.code();
                Log.i("232323232323", strss);
                handler.sendEmptyMessage(300);
            }
        });
    }

    String str;
    int code;

    private void gainDataFromService() {

        String edit = JGApplication.context.getSharedPreferences("Activity", Context.MODE_PRIVATE).getString("api_token", "");
        Log.i("aaaaa", edit);
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_SEND)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + edit)
                .post(formEncoding.build())
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
                Log.i("sssssssss", str + code);
                handler.sendEmptyMessage(100);
            }
        });
    }

    private int flags;
    private FrameLayout fram_job_back;
    private FrameLayout frameLayout;
    private ListView list_view_submit_resume;
    private View emptyView;
    private View loadingView;
    private View successView;
    private View emptyResumeView;
    private String api_token;

    @Override
    public void initView() {
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");
        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        list_view_submit_resume = (ListView) findViewById(R.id.list_view_submit_resume);

        emptyView = createEmptyView();
        loadingView = createLoadingView();
        successView = createSuccessView();
        emptyResumeView = createemptyResumeView();
        frameLayout.addView(loadingView);
        fram_job_back.setOnClickListener(this);
    }

    /**
     * 创建没有投递简历的空界面
     *
     * @return
     */
    private View createemptyResumeView() {
        View view = View.inflate(SubmitResumeActivity.this, R.layout.empty_resume_view, null);
        return view;
    }

    private XRefreshView xrefreshview;
    private RecyclerView recycler_view_test_rv;

    /**
     * 创建数据加载成功的界面
     */
    private View createSuccessView() {
        View view = View.inflate(SubmitResumeActivity.this, R.layout.fram_listview, null);
        xrefreshview = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);

        recycler_view_test_rv.setBackgroundColor(0xffffffff);
        //添加分割线
        recycler_view_test_rv.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL,1,0xffdfdfdf));

        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_test_rv.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));

        return view;
    }

    /**
     * 创建正在加载界面
     *
     * @return
     */
    private View createLoadingView() {
        View view = View.inflate(this, R.layout.loading_view, null);
        return view;
    }

    private TextView textview_click;

    /**
     * 创建空界面
     *
     * @return
     */
    private View createEmptyView() {
        View view = View.inflate(this, R.layout.empty_view, null);
        textview_click = (TextView) view.findViewById(R.id.textview_click);
        textview_click.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fram_job_back://返回
                finish();
                break;
            case R.id.textview_click://点击获取数据
                frameLayout.removeAllViews();
                frameLayout.addView(loadingView);
                gainDataFromService();
                break;
            case R.id.tv_off_sure:
                reapelSubmitResume();//撤销简历
                loginOffWindow.dismiss();
                break;
            case R.id.tv_off_cancel:
                loginOffWindow.dismiss();
                break;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }
}
