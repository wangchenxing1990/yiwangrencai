package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
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
import com.yiwangrencai.wxapi.WXEntryActivity;
import com.yiwangrencai.ywkj.adapter.MyPartApplyAdapter;
import com.yiwangrencai.ywkj.bean.PartApplyBean;
import com.yiwangrencai.ywkj.bean.PartTimeBean;
import com.yiwangrencai.ywkj.bean.SubmitBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.LoginOffPopuWindow;
import com.yiwangrencai.ywkj.view.MyPopuwindown;
import com.yiwangrencai.ywkj.view.RecycleViewDivider;
import com.yiwangrencai.ywkj.view.SwipeItemLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/3.
 */

public class PartApplyActivity extends BaseActiviyt {
    @Bind(R.id.fram_job_back)
    FrameLayout framJobBack;
    @Bind(R.id.title_skill)
    TextView titleSkill;
    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;
    @Bind(R.id.ll_popuwind)
    LinearLayout llPopuwind;

    @Override
    public int getLayoutId() {
        return R.layout.activity_part_app;
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
        loadingView = createLoadingView();//正在加载的界面
        successView = createSuccessView();//加载成功的界面
        emptyView = LayoutInflater.from(PartApplyActivity.this).inflate(R.layout.empty_view_icon, null);
        TextView textview_click = (TextView) emptyView.findViewById(R.id.textview_click);
        textview_click.setText("还没有报名的兼职");
        frameLayout.addView(loadingView);
        gainPartApplyData();//获取兼职的数据
    }


    private XRefreshView xrefreshview;
    private RecyclerView recycler_view_test_rv;

    /**
     * 加载成功的界面显示数据的界面
     *
     * @return
     */
    MyPartApplyAdapter adapter;
    List<PartApplyBean.DataBean> listBean = new ArrayList<>();
    PopupWindow popupWindow;

    private View createSuccessView() {
        View view = View.inflate(PartApplyActivity.this, R.layout.fram_listview, null);
        xrefreshview = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);

        adapter = new MyPartApplyAdapter(listBean, PartApplyActivity.this);
        recycler_view_test_rv.setAdapter(adapter);
        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_test_rv.addItemDecoration(new RecycleViewDivider(PartApplyActivity.this, LinearLayoutManager.HORIZONTAL));
        recycler_view_test_rv.setBackgroundColor(0xffffffff);
        recycler_view_test_rv.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));

        adapter.setCustomLoadMoreView(new XRefreshViewFooter(PartApplyActivity.this));
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        xrefreshview.setPullLoadEnable(true);

        /**
         * 下拉刷新和上垃加载
         */
        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                onRefreshData();//下拉刷新
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                onLoadMoreData();//上垃加载更多数据
            }
        });

        adapter.setOnItemLongClickListener(new MyPartApplyAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final PartApplyBean.DataBean data) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }

                View viewPopu = LayoutInflater.from(PartApplyActivity.this).inflate(R.layout.reapel_resume, null);
                TextView textpealResume = (TextView) viewPopu.findViewById(R.id.textpealResume);
                textpealResume.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopuwindow(data);

                    }
                });

                popupWindow = new PopupWindow();
                popupWindow.setContentView(viewPopu);
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                int[] location = new int[2];
                view.getLocationInWindow(location);
                int top = location[1];
                popupWindow.showAtLocation(PartApplyActivity.this.findViewById(R.id.recycler_view_test_rv), Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, top);
            }
        });

        adapter.setOnItemClickListener(new MyPartApplyAdapter.OnRecyclerViewOnItemClickListener() {
            @Override
            public void onItemClick(View view, PartApplyBean.DataBean data) {
                Intent intent = new Intent(PartApplyActivity.this, PartDetailActivity.class);
                intent.putExtra("id", data.getPt_job_id());
                intent.putExtra("params", "apply");
                startActivity(intent);
            }
        });

        return view;

    }

    /**
     * 显示是否退出选项按钮
     */
    LoginOffPopuWindow loginOffWindow;
    PartApplyBean.DataBean data;

    private void showPopuwindow(PartApplyBean.DataBean data) {
        this.data = data;
        popupWindow.dismiss();
        loginOffWindow = new LoginOffPopuWindow(PartApplyActivity.this, R.layout.login_off_popuwindown);
        View view = loginOffWindow.getView();
        loginOffWindow.showAtLocation(PartApplyActivity.this.findViewById(R.id.ll_popuwind), Gravity.CENTER, 0, 0);
        TextView textview = (TextView) view.findViewById(R.id.textview);
        textview.setText("确定撤销简历吗?");

        TextView tv_off_cancel = (TextView) view.findViewById(R.id.tv_off_cancel);
        TextView tv_off_sure = (TextView) view.findViewById(R.id.tv_off_sure);
        tv_off_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOffWindow.dismiss();
            }
        });
        tv_off_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reapelSubmitResume();//撤销简历
                loginOffWindow.dismiss();
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
        myPopuwindown = new MyPopuwindown(PartApplyActivity.this, R.layout.my_popuwindown);
        myPopuwindown.showAtLocation(PartApplyActivity.this.findViewById(R.id.ll_popuwind), Gravity.CENTER, 0, 0);
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder foem = new FormEncodingBuilder();
        foem.add("id", String.valueOf(data.getId()));
        foem.add("status", "4");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.PART_SIGN_UPUPDATE)
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
                handler.sendEmptyMessage(REPEAL_APPLY_PART);

            }
        });

    }

    /**
     * 上垃加载更多数据
     */
    private void onLoadMoreData() {
        if (next_page_url == null || "".equals(next_page_url)) {
            xrefreshview.stopLoadMore();
        } else {
            onLoadMoreDatas();//上垃加载更多数据
        }
    }

    /**
     * 上垃加载更多数据
     */
    private void onLoadMoreDatas() {
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
                Log.i("上垃加载获取更多的数据", str);
                handler.sendEmptyMessage(GAIN_MORE_DATA_LOAD);
            }
        });
    }

    /**
     * 下拉刷新
     */
    private void onRefreshData() {
        if (current_page != null && "1".equals(current_page)) {
            onRefreshCurrentOneData();//下拉刷新当前页是第一页时的获得数据
        } else {
            if (prev_page_url != null && !"".equals(prev_page_url)) {
                onRefreshMoredata();//正常的下拉刷新获取数据
            } else {
                xrefreshview.stopRefresh();
            }

        }
    }

    /**
     * 正常的下拉刷新获取数据
     */
    private void onRefreshMoredata() {
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
                code = response.code();
                str = response.body().string();
                Log.i("正常的刷新获取的数据", str);
                handler.sendEmptyMessage(REFRESH_MORE_DATA);
            }
        });
    }

    /**
     * 刷新当前页是第一页时获取的数据
     */
    private void onRefreshCurrentOneData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.PART_SIGN_UP)
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
                Log.i("下拉刷新当前是第一页的数据", str);
                handler.sendEmptyMessage(CURRENT_PAGE_ONE);
            }
        });
    }

    /**
     * 解析当前页是第一页时的数据
     */
    private void parsGainCurrentPageOne() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codes = json.getString("code");
            String message = json.getString("msg");

            if ("1".equals(codes)) {
                Gson gson = new Gson();
                PartApplyBean partApplyBean = gson.fromJson(str, PartApplyBean.class);
                xrefreshview.stopRefresh();
                adapter.setData(partApplyBean.getData(), true);

                if (next_page_url == null || "".equals(next_page_url)) {
                    xrefreshview.setLoadComplete(true);
                    xrefreshview.stopLoadMore();
                }

            } else if ("-1".equals(codes)) {

            }

            if (message != null && !"".equals(message)) {
                Toast.makeText(PartApplyActivity.this, message, Toast.LENGTH_SHORT).show();
            }

        } else {

        }

    }

    /**
     * 正在加载的页面
     *
     * @return
     */
    private View createLoadingView() {
        View view = View.inflate(PartApplyActivity.this, R.layout.loading_view, null);
        return view;
    }

    /**
     * 获取兼职的数据
     */
    int code;
    String str;

    private void gainPartApplyData() {

        OkHttpClient okhtp = new OkHttpClient();
        FormEncodingBuilder formEnding = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.PART_SIGN_UP)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEnding.build())
                .build();
        okhtp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                code = response.code();
                str = response.body().string();
                Log.i("strstrstrstr", str);
                handler.sendEmptyMessage(GAIN_PART_DATA);
            }
        });

    }

    /**
     * 解析获取的报名列表的数据
     */
    private String current_page;
    private String next_page_url;
    private String prev_page_url;

    private void parsGainPartApplyData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String message = json.getString("msg");
            if ("1".equals(codes)) {
                frameLayout.removeView(loadingView);
                Gson gson = new Gson();
                PartApplyBean partApplyBean = gson.fromJson(str, PartApplyBean.class);
                if (partApplyBean.getData().size() == 0) {
                    frameLayout.addView(emptyView);
                } else {
                    frameLayout.addView(successView);
                    adapter.setData(partApplyBean.getData(), false);

                    if (next_page_url == null || "".equals(next_page_url)) {

                        xrefreshview.setPullLoadEnable(false);
                        xrefreshview.setLoadComplete(true);

                    }

                }
            } else if ("-1".equals(codes)) {
                Intent intent = new Intent(PartApplyActivity.this, WXEntryActivity.class);
                startActivity(intent);
                finish();
            }

            if (message != null && !"".equals(message)) {
                Toast.makeText(PartApplyActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

    private final static int GAIN_PART_DATA = 0;
    private final static int CURRENT_PAGE_ONE = 1;
    private final static int REFRESH_MORE_DATA = 2;
    private final static int GAIN_MORE_DATA_LOAD = 3;
    private final static int REPEAL_APPLY_PART = 4;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GAIN_PART_DATA:
                    parsGainPartApplyData();//解析获取的报名列表的数据
                    break;
                case CURRENT_PAGE_ONE:
                    parsGainCurrentPageOne();//解析当前页是第一页时的数据
                    break;
                case REFRESH_MORE_DATA:
                    parsGainCurrentPageOne();//解析正常刷新下拉获取数据
                    break;
                case GAIN_MORE_DATA_LOAD:
                    parsLoadMoreData();//解析上垃加载获取的额数据
                    break;
                case REPEAL_APPLY_PART:
                    parsRepealPartApply();//解析撤销简历报名的数据
                    break;
            }
        }
    };

    /**
     * 解析撤销简历报名的数据
     */
    private void parsRepealPartApply() {
        myPopuwindown.dismiss();
        if (code == 200) {
            JSONObject jsonObject = JSON.parseObject(str);
            String message = jsonObject.getString("msg");
            String codes = jsonObject.getString("code");
            if ("1".equals(codes)) {
                adapter.removeListItem(data.getId());
            } else if ("-1".equals(codes)) {

            }
            if (message != null || !"".equals(message)) {
                Toast.makeText(PartApplyActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

    /**
     * 解析上垃加载获取的额数据
     */
    private void parsLoadMoreData() {
        if (code == 200) {

            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codes = json.getString("code");
            String message = json.getString("msg");

            if ("1".equals(codes)) {
                Gson gson = new Gson();
                PartApplyBean partApplyBean = gson.fromJson(str, PartApplyBean.class);
                adapter.setLoadmore(partApplyBean.getData());

                if (next_page_url == null || "".equals(next_page_url)) {
                    xrefreshview.setLoadComplete(true);
                }
                xrefreshview.stopRefresh();
                xrefreshview.stopLoadMore();
            } else if ("-1".equals(codes)) {

            }

            if (message != null && !"".equals(message)) {
                Toast.makeText(PartApplyActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

    @OnClick(R.id.fram_job_back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fram_job_back://返回上一个界面
                finish();
                break;
        }
    }
}
