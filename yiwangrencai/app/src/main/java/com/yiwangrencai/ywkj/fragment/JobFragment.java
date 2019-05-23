package com.yiwangrencai.ywkj.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.yiwangrencai.ywkj.activity.BrowserActivity;
import com.yiwangrencai.ywkj.activity.JobInfoActivity;
import com.yiwangrencai.ywkj.adapter.MyJobBrowseAdapter;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.RecycleViewDivider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/26.
 */

public class JobFragment extends Fragment {
    private FrameLayout frameLayout;
    private View successView;
    private View loadingView;
    private View emptyView;
    private String api_token;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences share = getActivity().getSharedPreferences("Activity", Context.MODE_PRIVATE);
        api_token = share.getString("api_token", "");
        frameLayout = new FrameLayout(getActivity());
        successView = createSuccessView();
        loadingView = inflater.inflate(R.layout.loading_view, null);
        emptyView = inflater.inflate(R.layout.empty_view_icon, null);
        TextView textview_click= (TextView) emptyView.findViewById(R.id.textview_click);
        textview_click.setText("还没有职位浏览记录");
        frameLayout.addView(loadingView);
        initData();
        return frameLayout;
    }

    String str;
    int code;

    private void initData() {
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formding = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.USER_BROWSE_JOB)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formding.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String eM = e.getMessage().toString();
                if (eM != null) {
                    handler.sendEmptyMessage(110);

                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("--------1", str);
                handler.sendEmptyMessage(100);
            }
        });
    }

    /**
     * 创建成功界面
     *
     * @return
     */
    XRefreshView xrefreshview;
    RecyclerView recycler_view_test_rv;
    MyJobBrowseAdapter adapter;
    List<CompanyJobBean> list = new ArrayList<>();

    private View createSuccessView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fram_listview, null);
        xrefreshview = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);

        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        xrefreshview.setPullLoadEnable(true);

        return view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    switchView();//根据请求的结果切换不同的界面
                    break;
                case 110:

                    break;
                case 300:
                    parsDataRefresh();//解析刷新的数据
                    break;
                case 301:
                    parsPullDataMore();//解析上拉加载更多数据
                    break;

                case ONE_PAGE:
                    parsOneDataNew();//解析当前是第一页的时候的数据
                    break;

            }
        }
    };
    /**
     * 根据不同的结果切换不同的界面
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

            if ("1".equals(codes)) {
                JSONArray dataArray = json.getJSONArray("data");
                frameLayout.removeView(loadingView);
                if (dataArray.size() == 0) {
                    frameLayout.addView(emptyView);
                } else {
                    frameLayout.addView(successView);
                    if ("1".equals(current_page) && dataArray.size() < 15) {
                        xrefreshview.setPullLoadEnable(false);
                    }
                    parsDataArray(dataArray);
                }
            } else if ("0".equals(codes)) {

            } else if ("-1".equals(codes)) {
                frameLayout.removeView(loadingView);
                frameLayout.removeView(emptyView);
            }
        } else {
            Toast.makeText(getActivity(), "联网失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析职位浏览的数据
     *
     * @param dataArray
     */

    List<CompanyJobBean> listJob = new ArrayList<>();

    private void parsDataArray(JSONArray dataArray) {

        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject jsonData = dataArray.getJSONObject(i);
            CompanyJobBean companyBean = new CompanyJobBean();
            companyBean.setId(jsonData.getString("id"));
            companyBean.setJob_title(jsonData.getString("job_title"));
            companyBean.setUpdatetime(jsonData.getString("updatetime"));
            companyBean.setLocation_name(jsonData.getString("location_name"));
            companyBean.setSalary(jsonData.getString("salary"));
            companyBean.setEducation_name(jsonData.getString("education_name"));
            companyBean.setWork_year_name(jsonData.getString("work_year_name"));
            companyBean.setCompany_name(jsonData.getString("company_name"));
            companyBean.setIs_urgent(jsonData.getString("is_urgent"));
            listJob.add(companyBean);
        }

        adapter = new MyJobBrowseAdapter(listJob, getActivity());
        recycler_view_test_rv.setAdapter(adapter);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        recycler_view_test_rv.addItemDecoration(new RecycleViewDivider(getActivity(), RecyclerView.HORIZONTAL));
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(getActivity()));

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
        adapter.setOnItemClickListener(new MyJobBrowseAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, CompanyJobBean data) {
                Intent intent = new Intent(getActivity(), JobInfoActivity.class);
                intent.putExtra("id", data.getId());
                startActivity(intent);
            }
        });
    }

    /**
     * 下拉刷新
     */
    private void pullToRefreshData() {
        if ("1".equals(current_page)) {
            Log.i("1111111", "1111111");
            refreshOnePage();//下拉刷新当前页面是第一页的时候的界面

        } else {
            if (prev_page_url == null) {
                Log.i("nullnullnullnull", "nullnullnullnull");
                xrefreshview.stopRefresh();//停止刷新

            } else {
                Log.i("otherotherother", "otherotherother");
                refreshData();//下拉刷新

            }
        }
    }

    /**
     * 上垃加载更多
     */
    private void loadMoreData() {
        if (next_page_url == null) {
            xrefreshview.setLoadComplete(true);
        } else {
            pullLoadMoreData();//上垃加载更多数据
        }
    }

    /**
     * 下拉刷新当前页面是第一页的时候的界面
     */
    private static final int ONE_PAGE = 1;

    private void refreshOnePage() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("page", "1");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.USER_BROWSE_JOB)
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
                Log.i("aaaaaaaa", str);
                handler.sendEmptyMessage(ONE_PAGE);
            }
        });
    }

    /**
     * 下拉刷新
     */
    private void refreshData() {

        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder fromd = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(prev_page_url)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(fromd.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("qqqqqqqq", str);
                handler.sendEmptyMessage(300);
            }
        });

    }

    /**
     * 上垃加载更多数据
     */
    private void pullLoadMoreData() {

        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
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
                Log.i("12222222", str);
                handler.sendEmptyMessage(301);

            }
        });
    }

    /**
     * 解析下拉刷新的数据
     */
    private void parsDataRefresh() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            if ("1".equals(codes)) {
                JSONArray dataArray = json.getJSONArray("data");
                listJob.clear();
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONObject jsonData = dataArray.getJSONObject(i);
                    CompanyJobBean companyBean = new CompanyJobBean();
                    companyBean.setId(jsonData.getString("id"));
                    companyBean.setJob_title(jsonData.getString("job_title"));
                    companyBean.setUpdatetime(jsonData.getString("updatetime"));
                    companyBean.setLocation_name(jsonData.getString("location_name"));
                    companyBean.setSalary(jsonData.getString("salary"));
                    companyBean.setEducation_name(jsonData.getString("education_name"));
                    companyBean.setWork_year_name(jsonData.getString("work_year_name"));
                    companyBean.setCompany_name(jsonData.getString("company_name"));
                    companyBean.setIs_urgent(jsonData.getString("is_urgent"));
                    listJob.add(companyBean);
                }

                adapter.notifyDataSetChanged();
                xrefreshview.stopRefresh();
                xrefreshview.setLoadComplete(false);
            } else {
                Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

    /**
     * 解析上啦加载更多数据
     */
    private void parsPullDataMore() {
        xrefreshview.stopLoadMore();
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            if ("1".equals(codes)) {
                JSONArray dataArray = json.getJSONArray("data");
                if ("1".equals(current_page) && dataArray.size() < 15) {
                    xrefreshview.setLoadComplete(true);
                }
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONObject jsonData = dataArray.getJSONObject(i);
                    CompanyJobBean companyBean = new CompanyJobBean();
                    companyBean.setId(jsonData.getString("id"));
                    companyBean.setJob_title(jsonData.getString("job_title"));
                    companyBean.setUpdatetime(jsonData.getString("updatetime"));
                    companyBean.setLocation_name(jsonData.getString("location_name"));
                    companyBean.setSalary(jsonData.getString("salary"));
                    companyBean.setEducation_name(jsonData.getString("education_name"));
                    companyBean.setWork_year_name(jsonData.getString("work_year_name"));
                    companyBean.setCompany_name(jsonData.getString("company_name"));
                    companyBean.setIs_urgent(jsonData.getString("is_urgent"));
                    listJob.add(companyBean);
                }

                adapter.notifyDataSetChanged();

            } else {
                Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

    /**
     * 解析当前页是第一页的时候的数据
     */
    private void parsOneDataNew() {
        JSONObject json = JSON.parseObject(str);
        String codes = json.getString("code");
        String msg = json.getString("msg");
        current_page = json.getString("current_page");
        next_page_url = json.getString("next_page_url");
        prev_page_url = json.getString("prev_page_url");
        if ("1".equals(codes)) {
            JSONArray dataArray = json.getJSONArray("data");
            listJob.clear();
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject jsonData = dataArray.getJSONObject(i);
                CompanyJobBean companyBean = new CompanyJobBean();
                companyBean.setId(jsonData.getString("id"));
                companyBean.setJob_title(jsonData.getString("job_title"));
                companyBean.setUpdatetime(jsonData.getString("updatetime"));
                companyBean.setLocation_name(jsonData.getString("location_name"));
                companyBean.setSalary(jsonData.getString("salary"));
                companyBean.setEducation_name(jsonData.getString("education_name"));
                companyBean.setWork_year_name(jsonData.getString("work_year_name"));
                companyBean.setCompany_name(jsonData.getString("company_name"));
                companyBean.setIs_urgent(jsonData.getString("is_urgent"));
                listJob.add(companyBean);
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
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }

    }
}
