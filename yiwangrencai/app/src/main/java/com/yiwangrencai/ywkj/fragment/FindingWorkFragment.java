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
import com.yiwangrencai.ywkj.activity.CompanydetailActivity;
import com.yiwangrencai.ywkj.activity.JobInfoActivity;
import com.yiwangrencai.ywkj.adapter.MyReceiveResumeAdapter;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.RecycleViewDivider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/26.
 */

public class FindingWorkFragment extends Fragment {
    FrameLayout frameLayout;
    String api_token;
    View loadingView;
    View successView;
    View empty;

    public FindingWorkFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences shares = getActivity().getSharedPreferences("Activity", Context.MODE_PRIVATE);
        api_token = shares.getString("api_token", "");
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        idid=sharedPreferences.getString("com_id","");
        frameLayout=new FrameLayout(getActivity());
        loadingView=inflater.inflate(R.layout.loading_view,null);
        successView=createSuccessView();
        empty=inflater.inflate(R.layout.empty_view_icon,null);
        frameLayout.addView(loadingView);
        initData();
        return frameLayout;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 110:
                    Toast.makeText(getActivity(), "联网超时，请重试", Toast.LENGTH_SHORT).show();
                    break;
                case 120:
                    parsCompanyJob();//解析公司所有职位的数据
                    break;
                case 130:
                    parsRefreshCompanyJob();//解析下拉刷新获取的数据
                    break;
                case 300:
                    parsLoadNewData();//上垃加载更多的数据
                    break;
                case 150:
                    parsRefreshNewData();//解析下拉刷新的数据
                    break;
                case ONE_PAGE:
                    parsOnePageData();//解析当前数据是第一个页面的数据
                    break;
            }
        }
    };

    /**
     * 解析当前数据是第一个页面的数据
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
                allJobList.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    CompanyJobBean companyBean = new CompanyJobBean();
                    JSONObject jsondata = jsonArray.getJSONObject(i);

                    companyBean.setId(jsondata.getString("id"));
                    companyBean.setCom_id(jsondata.getString("com_id"));
                    companyBean.setJob_title(jsondata.getString("job_title"));
                    companyBean.setUpdatetime(jsondata.getString("updatetime"));
                    companyBean.setJob_status(jsondata.getString("job_status"));
                    companyBean.setLocation_name(jsondata.getString("location_name"));
                    companyBean.setSalary(jsondata.getString("salary"));
                    companyBean.setEducation_name(jsondata.getString("education_name"));
                    companyBean.setWork_year_name(jsondata.getString("work_year_name"));
                    companyBean.setCompany_name(jsondata.getString("company_name"));
                    companyBean.setJob_status_name(jsondata.getString("job_status_name"));
                    companyBean.setIs_urgent(jsondata.getString("is_urgent"));

                    allJobList.add(companyBean);

                }
                if (next_page_url==null||"".equals(next_page_url)){
                    xrefreshview.setLoadComplete(true);
                    xrefreshview.setPullLoadEnable(false);
                }
                xrefreshview.stopRefresh();
                recyclerViewReceiveResumeAdapter.notifyDataSetChanged();
            } else if ("-1".equals(codes)) {

            }

            if (msg!=null&&!"".equals(msg)){
                Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
            }

        } else {

        }
    }
    private List<CompanyJobBean> allJobList = new ArrayList<>();

    MyReceiveResumeAdapter recyclerViewReceiveResumeAdapter;
    /**
     * 解析数据公司所有职位
     */
    private void parsCompanyJob() {
        if (code == 200) {
            JSONObject jsons = JSON.parseObject(str);
            String codes = jsons.getString("code");
            current_page = jsons.getString("current_page");
            next_page_url = jsons.getString("next_page_url");
            prev_page_url = jsons.getString("prev_page_url");
            String msg = jsons.getString("msg");
            String to = jsons.getString("to");
            if (codes.equals("1")) {
                JSONArray data = jsons.getJSONArray("data");
                if (next_page_url == null || "".equals(next_page_url)) {
                    xrefreshview.setLoadComplete(true);
                    xrefreshview.setPullLoadEnable(false);
                    Log.i("a=========a", "111111"+next_page_url);
                }
                frameLayout.removeView(loadingView);
                frameLayout.addView(successView);
                Log.i("a=========a", "2222222"+next_page_url);
                parsArrayJob(data);
            } else {
                Toast.makeText(getActivity(), jsons.getString("msg"), Toast.LENGTH_SHORT).show();
            }
            if (msg!=null&&!"".equals(msg)){
                Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 解析所有公司职位的数据
     *
     * @param jsonArray
     */
    private void parsArrayJob(JSONArray jsonArray) {
        allJobList.clear();
        for (int i = 0; i < jsonArray.size(); i++) {
            CompanyJobBean companyBean = new CompanyJobBean();
            JSONObject jsondata = jsonArray.getJSONObject(i);

            companyBean.setId(jsondata.getString("id"));
            companyBean.setCom_id(jsondata.getString("com_id"));
            companyBean.setJob_title(jsondata.getString("job_title"));
            companyBean.setUpdatetime(jsondata.getString("updatetime"));
            companyBean.setJob_status(jsondata.getString("job_status"));
            companyBean.setLocation_name(jsondata.getString("location_name"));
            companyBean.setSalary(jsondata.getString("salary"));
            companyBean.setEducation_name(jsondata.getString("education_name"));
            companyBean.setWork_year_name(jsondata.getString("work_year_name"));
            companyBean.setCompany_name(jsondata.getString("company_name"));
            companyBean.setJob_status_name(jsondata.getString("job_status_name"));
            companyBean.setIs_urgent(jsondata.getString("is_urgent"));

            allJobList.add(companyBean);

        }

        // Log.i("aaaaaaaa", allJobList.get(0).getCompany_name());
        recyclerViewReceiveResumeAdapter = new MyReceiveResumeAdapter(allJobList, getActivity());
        recycler_view_test_rv.setAdapter(recyclerViewReceiveResumeAdapter);
        recycler_view_test_rv.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
        xrefreshview.setPullLoadEnable(true);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);

        recyclerViewReceiveResumeAdapter.setCustomLoadMoreView(new XRefreshViewFooter(getActivity()));

        recyclerViewReceiveResumeAdapter.setOnItemClickListener(new MyReceiveResumeAdapter.OnRecyclerViewOnItemClickListener() {
            @Override
            public void onItemClick(View v, CompanyJobBean data) {
                Intent intent = new Intent(getActivity(), JobInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", data.getId());
                startActivity(intent);
            }
        });
    }
    private void initData() {
        OkHttpClient okhttpclient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", idid);
        //formEncoding.add("page", "1");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.COMPANY_JOB_LIST)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR+api_token)
                .post(formEncoding.build())
                .build();
        okhttpclient.newCall(request).enqueue(new Callback() {
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
                Log.i("111111114", str);
                handler.sendEmptyMessage(120);
            }
        });
    }


    /**
     * 上垃加载更多的数据
     */
    private void parsLoadNewData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            if (codes.equals("1")) {
                JSONArray data = json.getJSONArray("data");

                for (int i = 0; i < data.size(); i++) {
                    CompanyJobBean companyBean = new CompanyJobBean();
                    JSONObject jsondata = data.getJSONObject(i);

                    companyBean.setId(jsondata.getString("id"));
                    companyBean.setCom_id(jsondata.getString("com_id"));
                    companyBean.setJob_title(jsondata.getString("job_title"));
                    companyBean.setUpdatetime(jsondata.getString("updatetime"));
                    companyBean.setJob_status(jsondata.getString("job_status"));
                    companyBean.setLocation_name(jsondata.getString("location_name"));
                    companyBean.setSalary(jsondata.getString("salary"));
                    companyBean.setEducation_name(jsondata.getString("education_name"));
                    companyBean.setWork_year_name(jsondata.getString("work_year_name"));
                    companyBean.setCompany_name(jsondata.getString("company_name"));
                    companyBean.setJob_status_name(jsondata.getString("job_status_name"));
                    companyBean.setIs_urgent(jsondata.getString("is_urgent"));

                    allJobList.add(companyBean);

                }
                recyclerViewReceiveResumeAdapter.notifyDataSetChanged();
                xrefreshview.stopLoadMore();
            } else {
                Toast.makeText(getActivity(), json.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }
    /**
     * 解析下拉刷新获取的数据
     */
    private void parsRefreshCompanyJob() {
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(prev_page_url)
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
                Log.i("112124124124", str);
                handler.sendEmptyMessage(150);
            }
        });

    }

    /**
     * 解析下拉刷新的数据n
     */
    private void parsRefreshNewData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codes = json.getString("code");
            if ("1".equals(codes)) {
                JSONArray data = json.getJSONArray("data");
                allJobList.clear();
                for (int i = 0; i < data.size(); i++) {
                    CompanyJobBean companyBean = new CompanyJobBean();
                    JSONObject jsondata = data.getJSONObject(i);
                    companyBean.setId(jsondata.getString("id"));
                    companyBean.setCom_id(jsondata.getString("com_id"));
                    companyBean.setJob_title(jsondata.getString("job_title"));
                    companyBean.setUpdatetime(jsondata.getString("updatetime"));
                    companyBean.setJob_status(jsondata.getString("job_status"));
                    companyBean.setLocation_name(jsondata.getString("location_name"));
                    companyBean.setSalary(jsondata.getString("salary"));
                    companyBean.setEducation_name(jsondata.getString("education_name"));
                    companyBean.setWork_year_name(jsondata.getString("work_year_name"));
                    companyBean.setCompany_name(jsondata.getString("company_name"));
                    companyBean.setJob_status_name(jsondata.getString("job_status_name"));
                    companyBean.setIs_urgent(jsondata.getString("is_urgent"));

                    allJobList.add(companyBean);

                }
                recyclerViewReceiveResumeAdapter.notifyDataSetChanged();
                xrefreshview.stopRefresh();
                xrefreshview.setLoadComplete(false);
            } else {

            }
        } else {

        }
    }

    private RecyclerView recycler_view_test_rv;
    private XRefreshView xrefreshview;
    private View createSuccessView() {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.fram_listview,null);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);
        xrefreshview = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                refreshMoreData();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                downLoadMoreData();
            }
        });
        return view;
    }

    /**
     * 下拉刷新更多数据
     */
    String prev_page_url;
    String next_page_url;
    String current_page;
    private void refreshMoreData() {
        if ("1".equals(current_page)) {
            //refreshOnePageData();//刷新当前页面是第一个页面的数据
            xrefreshview.stopRefresh();
            xrefreshview.setLoadComplete(true);
        } else {
            if (prev_page_url == null) {
                xrefreshview.stopRefresh();
            } else {
                upLoadMore();//下拉加载更多
            }
        }
    }

    /**
     * 上垃加载更多数据
     */
    private String str;
    private int code;
    private void downLoadMoreData() {
        if (next_page_url == null||"".equals(next_page_url)) {
            xrefreshview.setLoadComplete(true);
            xrefreshview.stopRefresh();
        } else {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Activity", Context.MODE_PRIVATE);
            String api_token = sharedPreferences.getString("api_token", "");
            if (next_page_url == null) {
                xrefreshview.setLoadComplete(true);
            } else {
                OkHttpClient okHttpClient = new OkHttpClient();
                FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
                Request request = new Request.Builder()
                        .url(next_page_url)
                        .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                        .addHeader(ContentUrl.ACCEPT, ContentUrl.BEAR + api_token)
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
                        Log.i("::::::", str);
                        handler.sendEmptyMessage(300);
                    }
                });

            }
        }
    }

    /**
     * 刷新当前页面是第一个页面的数据
     */
    String idid;
    String uid;
    String company_name;
    String brands;
    String temptation;
    private static final int ONE_PAGE = 1;
    private void refreshOnePageData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("page", "1");
        formEncoding.add("id", idid);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.COMPANY_JOB)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR+api_token)
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
                Log.i("aassssss", str);
                handler.sendEmptyMessage(ONE_PAGE);
            }
        });

    }


    /**
     * 下拉加载更多
     */
    private void upLoadMore() {
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();

        Request request = new Request.Builder()
                .url(prev_page_url)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR+api_token)
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
                Log.i("ggggg", str);
                handler.sendEmptyMessage(130);
            }
        });
    }


}
