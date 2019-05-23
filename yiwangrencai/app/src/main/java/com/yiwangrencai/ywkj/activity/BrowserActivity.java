package com.yiwangrencai.ywkj.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.yiwangrencai.ywkj.adapter.MyJobBrowseAdapter;
import com.yiwangrencai.ywkj.adapter.MyJobCollectionAdapter;
import com.yiwangrencai.ywkj.adapter.MyLookMeAdapter;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.RecycleViewDivider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/15.
 */

public class BrowserActivity extends BaseActiviyt implements View.OnClickListener {


    @Override
    public int getLayoutId() {
        return R.layout.activity_broesing_history;
    }

    String api_token;

    @Override
    public void initData() {
        SharedPreferences share = getSharedPreferences("Activity", Context.MODE_PRIVATE);
        api_token = share.getString("api_token", "");
        gainDataFromSevice();
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
                case 200:
                    parsDataCompany();//解析获取公司浏览的数据
                    break;
                case 300:
                    parsDataRefresh();//解析刷新的数据
                    break;
                case 301:
                    parsPullDataMore();//解析上拉加载更多数据
                    break;
                case 400:
                    parsCompanyDataUp();//解析上啦加载公司浏览的数据
                    break;
                case 410:
                    parsRefreshNewDataCompany();//解析下拉刷新的公司浏览记录的数据
                    break;
                case ONE_PAGE:
                    parsOneDataNew();//解析当前是第一页的时候的数据
                    break;
                case ONE_COMPANY_PAGE:
                    parsOneDataNewCompany();//解析当前是第一页的时候的数据
                    break;

            }
        }
    };




    /**
     * 解析下拉刷新的公司浏览记录的数据
     */
    private void parsRefreshNewDataCompany() {
        if (codes == 200) {
            JSONObject jsonObject = JSON.parseObject(strs);
            current_pages = jsonObject.getString("current_page");
            next_page_urls = jsonObject.getString("next_page_url");
            prev_page_urls = jsonObject.getString("prev_page_url");
            String codess = jsonObject.getString("code");
            if ("1".equals(codess)) {
                jsonArrayCompay = jsonObject.getJSONArray("data");
                listCompany.clear();
                for (int i = 0; i < jsonArrayCompay.size(); i++) {
                    JSONObject json = jsonArrayCompay.getJSONObject(i);
                    CompanyJobBean companyJobBean = new CompanyJobBean();
                    companyJobBean.setCom_id(json.getString("com_id"));
                    companyJobBean.setEcom_id(json.getString("ecom_id"));
                    companyJobBean.setCompany_name(json.getString("company_name"));
                    companyJobBean.setTime(json.getString("time"));
                    companyJobBean.setIndustry_name(json.getString("industry_name"));
                    companyJobBean.setRegion_name(json.getString("region_name"));
                    listCompany.add(companyJobBean);
                }
                recycler_view_rightt.stopRefresh();
                recycler_view_rightt.setLoadComplete(false);
                companyadapter.notifyDataSetChanged();

            } else {

            }
        } else {

        }
    }

    /**
     * 解析上啦加载公司浏览的数据
     */
    private void parsCompanyDataUp() {
        if (codes == 200) {
            recycler_view_rightt.stopLoadMore();
            JSONObject jsonObject = JSON.parseObject(strs);
            current_pages = jsonObject.getString("current_page");
            next_page_urls = jsonObject.getString("next_page_url");
            prev_page_urls = jsonObject.getString("prev_page_url");
            String codess = jsonObject.getString("code");
            if ("1".equals(codess)) {
                jsonArrayCompay = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArrayCompay.size(); i++) {
                    JSONObject json = jsonArrayCompay.getJSONObject(i);
                    CompanyJobBean companyJobBean = new CompanyJobBean();
                    companyJobBean.setCom_id(json.getString("com_id"));
                    companyJobBean.setEcom_id(json.getString("ecom_id"));
                    companyJobBean.setCompany_name(json.getString("company_name"));
                    companyJobBean.setTime(json.getString("time"));
                    companyJobBean.setIndustry_name(json.getString("industry_name"));
                    companyJobBean.setRegion_name(json.getString("region_name"));
                    listCompany.add(companyJobBean);
                }

                companyadapter.notifyDataSetChanged();

            } else {

            }
        } else {

        }
    }

    /**
     * 解析公司浏览记录
     */
    String current_pages;
    String next_page_urls;
    String prev_page_urls;
    JSONArray jsonArrayCompay;
    private List<CompanyJobBean> listCompany = new ArrayList<>();

    private void parsDataCompany() {
        if (codes == 200) {
            JSONObject jsonObject = JSON.parseObject(strs);
            current_pages = jsonObject.getString("current_page");
            next_page_urls = jsonObject.getString("next_page_url");
            prev_page_urls = jsonObject.getString("prev_page_url");
            String codess = jsonObject.getString("code");
            if ("1".equals(codess)) {
                frameLayoutRight.removeView(loadingViewRight);
                frameLayoutRight.addView(successsViewRight);
                jsonArrayCompay = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArrayCompay.size(); i++) {
                    JSONObject json = jsonArrayCompay.getJSONObject(i);
                    CompanyJobBean companyJobBean = new CompanyJobBean();
                    companyJobBean.setCom_id(json.getString("com_id"));
                    companyJobBean.setEcom_id(json.getString("ecom_id"));
                    companyJobBean.setCompany_name(json.getString("company_name"));
                    companyJobBean.setTime(json.getString("time"));
                    companyJobBean.setIndustry_name(json.getString("industry_name"));
                    companyJobBean.setRegion_name(json.getString("region_name"));
                    listCompany.add(companyJobBean);
                }

                companyadapter = new MyLookMeAdapter(listCompany);
                recycler_view_test_rv_right.setAdapter(companyadapter);

                recycler_view_rightt.setAutoLoadMore(false);
                recycler_view_rightt.setPinnedTime(1000);
                recycler_view_rightt.setMoveForHorizontal(true);
                recycler_view_test_rv_right.addItemDecoration(new RecycleViewDivider(this, RecyclerView.HORIZONTAL));
                companyadapter.setCustomLoadMoreView(new XRefreshViewFooter(this));

                if (next_page_url == null || "".equals(next_page_url)) {
                    recycler_view_rightt.setLoadComplete(true);
                    recycler_view_rightt.setPullLoadEnable(false);
                }
                recycler_view_rightt.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
                    @Override
                    public void onRefresh(boolean isPullDown) {//下拉刷新
                        super.onRefresh(isPullDown);
                        pullToRefreshCompanyData();//下拉刷新公司浏览记录的数据
                    }

                    @Override
                    public void onLoadMore(boolean isSilence) {//上垃加载
                        super.onLoadMore(isSilence);
                        loadMoreCompanyData();//上垃加载更多公司浏览数据

                    }
                });
                companyadapter.setmOnItemClickListener(new MyLookMeAdapter.OnRecyclerViewOnItemClickListener() {
                    @Override
                    public void onItenClick(View view, CompanyJobBean data) {
                        Intent intent = new Intent(BrowserActivity.this, CompanydetailActivity.class);
                        intent.putExtra("id", data.getEcom_id());
                        startActivity(intent);
                    }
                });
            } else {

            }
        } else {

        }
    }

    /**
     * 下拉刷新公司浏览记录的数据
     */
    private void pullToRefreshCompanyData() {
        if ("1".equals(current_page)) {
            refreshOnePageData();//下拉刷新当前公司的页是第一页的时候的数据
        } else {
            if (prev_page_urls == null) {
                recycler_view_rightt.stopRefresh();
            } else {
                OkHttpClient okhttp = new OkHttpClient();
                FormEncodingBuilder formencoding = new FormEncodingBuilder();
                Request request = new Request.Builder()
                        .url(prev_page_urls)
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
                        strs = response.body().string();
                        codes = response.code();
                        Log.i("11122124214", strs);
                        handler.sendEmptyMessage(410);
                    }
                });
            }
        }
    }

    /**
     * 刷新公司当前页是第一页的时候的数据
     */
    private static final int ONE_COMPANY_PAGE=3;
    private void refreshOnePageData() {
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
                Log.i("aaddssadfs",str);
                handler.sendEmptyMessage(ONE_COMPANY_PAGE);
            }
        });
    }
    /**
     * 解析当前是第一页的时候的数据
     */
    private void parsOneDataNewCompany() {
        if (code==200){
            JSONObject json=JSON.parseObject(str);
            String codes=json.getString("code");
            String msg=json.getString("msg");
            current_pages = json.getString("current_page");
            next_page_urls = json.getString("next_page_url");
            prev_page_urls = json.getString("prev_page_url");
            if ("1".equals(codes)){
                JSONArray jsonArray = json.getJSONArray("data");
                listCompany.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsons = jsonArray.getJSONObject(i);
                    CompanyJobBean companyJobBean = new CompanyJobBean();
                    companyJobBean.setCom_id(jsons.getString("com_id"));
                    companyJobBean.setEcom_id(jsons.getString("ecom_id"));
                    companyJobBean.setCompany_name(jsons.getString("company_name"));
                    companyJobBean.setTime(jsons.getString("time"));
                    companyJobBean.setIndustry_name(jsons.getString("industry_name"));
                    companyJobBean.setRegion_name(jsons.getString("region_name"));
                    listCompany.add(companyJobBean);
                }

                if (next_page_url==null||"".equals(next_page_url)){
                    recycler_view_rightt.setPullLoadEnable(false);
                    recycler_view_rightt.setLoadComplete(true);
                }
                recycler_view_rightt.stopRefresh();
                recycler_view_rightt.setLoadComplete(false);
                companyadapter.notifyDataSetChanged();
            }else if("-1".equals(codes)){

            }
            if (msg != null && !"".equals(msg)){
                Toast.makeText(BrowserActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        }else{

        }
    }
    /**
     * 上垃加载更多公司浏览的数据
     */
    private void loadMoreCompanyData() {
        if (next_page_urls == null) {
            recycler_view_rightt.setLoadComplete(true);
        } else {
            OkHttpClient okhttp = new OkHttpClient();
            FormEncodingBuilder formEncoding = new FormEncodingBuilder();
            Request request = new Request.Builder()
                    .url(next_page_urls)
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
                    strs = response.body().string();
                    codes = response.code();
                    Log.i("12233432523554", strs);
                    handler.sendEmptyMessage(400);
                }
            });
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
                Toast.makeText(BrowserActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
            }
        } else {

        }
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
                Toast.makeText(BrowserActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

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
                frameLayoutLeft.removeView(loadingView);
                frameLayoutLeft.addView(successView);
                JSONArray dataArray = json.getJSONArray("data");
                if ("1".equals(current_page) && dataArray.size() < 15) {
                    xrefreshview.setPullLoadEnable(false);
                }
                parsDataArray(dataArray);

            } else if ("0".equals(codes)) {

            } else if ("-1".equals(codes)) {
                frameLayoutLeft.removeView(loadingView);
//                Intent intent=new Intent(BrowserActivity.this,LoginActivity.class);
//                intent.putExtra("register","register");
//                startActivity(intent);
            }
        } else {
            Toast.makeText(BrowserActivity.this, "联网失败", Toast.LENGTH_SHORT).show();
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

        adapter = new MyJobBrowseAdapter(listJob,this);
        recycler_view_test_rv.setAdapter(adapter);
        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);
        recycler_view_test_rv.addItemDecoration(new RecycleViewDivider(this, RecyclerView.HORIZONTAL));
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
        adapter.setOnItemClickListener(new MyJobBrowseAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, CompanyJobBean data) {
                Intent intent = new Intent(BrowserActivity.this, JobInfoActivity.class);
                intent.putExtra("id", data.getId());
                startActivity(intent);
            }
        });

    }

    private List<CompanyJobBean> parsDataArrayy(JSONArray dataArray) {

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

        return listJob;
    }

    /**
     * 下拉刷新
     */
    private void pullToRefreshData() {
        if ("1".equals(current_page)) {
            Log.i("1111111","1111111");
            refreshOnePage();//下拉刷新当前页面是第一页的时候的界面

        } else {
            if (prev_page_url == null) {
                Log.i("nullnullnullnull","nullnullnullnull");
                xrefreshview.stopRefresh();//停止刷新

            } else {
                Log.i("otherotherother","otherotherother");
                refreshData();//下拉刷新

            }
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
            Toast.makeText(BrowserActivity.this, msg, Toast.LENGTH_SHORT).show();
        }

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
     * 从服务器获取数据
     */
    String str;
    int code;

    private void gainDataFromSevice() {

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
                //request.body().toString()
                if (eM != null) {
                    handler.sendEmptyMessage(110);
                    //Log.i("11111222222", eM);
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

    FrameLayout frameLayoutLeft;
    FrameLayout frameLayoutRight;
    LinearLayout linear_job_browse;
    LinearLayout linear_company_browse;
    TextView textjobbrowse;
    TextView textcompanybrowse;
    //    View view_left;
//    View view_right;
    View loadingView;
    View enptyView;
    View successView;
    View loadingViewRight;
    View successsViewRight;

    @Override
    public void initView() {

        FrameLayout fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        frameLayoutLeft = (FrameLayout) findViewById(R.id.frameLayoutLeft);
        frameLayoutRight = (FrameLayout) findViewById(R.id.frameLayoutRight);
        linear_job_browse = (LinearLayout) findViewById(R.id.linear_job_browse);
        linear_company_browse = (LinearLayout) findViewById(R.id.linear_company_browse);
        textjobbrowse = (TextView) findViewById(R.id.textjobbrowse);
        textcompanybrowse = (TextView) findViewById(R.id.textcompanybrowse);

//        view_left = (View) findViewById(R.id.view_left);
//        view_right = (View) findViewById(R.id.view_right);

        loadingView = createLoadingView();
        enptyView = createEnptyView();
        successView = createSuccessView();

        frameLayoutLeft.addView(loadingView);

        loadingViewRight = createLoadingViewRight();
        successsViewRight = createSuccesssViewRight();

        fram_job_back.setOnClickListener(this);
        linear_job_browse.setOnClickListener(this);
        linear_company_browse.setOnClickListener(this);

    }

    XRefreshView recycler_view_rightt;
    RecyclerView recycler_view_test_rv_right;
    MyLookMeAdapter companyadapter;

    private View createSuccesssViewRight() {
        View view = View.inflate(BrowserActivity.this, R.layout.fram_listvieww, null);
        recycler_view_rightt = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv_right = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);

        recycler_view_test_rv_right.setLayoutManager(new LinearLayoutManager(this));

        recycler_view_test_rv_right.addItemDecoration(new RecycleViewDivider(this, RecyclerView.HORIZONTAL));
        recycler_view_rightt.setPullLoadEnable(true);

        return view;
    }

    private View createLoadingViewRight() {
        View view = View.inflate(BrowserActivity.this, R.layout.loading_vieww, null);
        return view;
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
        View view = View.inflate(BrowserActivity.this, R.layout.fram_listview, null);
        xrefreshview = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);

        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(this));
        xrefreshview.setPullLoadEnable(true);

        return view;
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
     * 创建加载为空界面
     *
     * @return
     */
    private View createEnptyView() {
        View view = View.inflate(BrowserActivity.this, R.layout.empty_view, null);
        return view;
    }

    /**
     * 创建正在加载界面
     *
     * @return
     */
    private View createLoadingView() {
        View view = View.inflate(BrowserActivity.this, R.layout.loading_view, null);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back:
                finish();
                break;
            case R.id.linear_job_browse:
                textjobbrowse.setTextColor(0xff42bfb6);
                textcompanybrowse.setTextColor(0xff555555);
//                view_left.setBackgroundColor(0x42bfb6);
//                view_right.setBackgroundColor(0x898989);

                frameLayoutLeft.setVisibility(View.VISIBLE);
                frameLayoutRight.setVisibility(View.GONE);
                break;
            case R.id.linear_company_browse:
                textjobbrowse.setTextColor(0xff555555);
                textcompanybrowse.setTextColor(0xff42bfb6);
//                view_left.setBackgroundColor(0x898989);
//                view_right.setBackgroundColor(0x42bfb6);
                frameLayoutLeft.setVisibility(View.GONE);
                frameLayoutRight.setVisibility(View.VISIBLE);

                if (jsonArrayCompay == null) {
                    frameLayoutRight.removeAllViews();
                    frameLayoutRight.addView(loadingViewRight);
                    gainDataCompanyData();
                }

                break;
        }
    }

    /**
     * 获取公司浏览记录的数据
     */
    String strs;
    int codes;

    private void gainDataCompanyData() {
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

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strs = response.body().string();
                codes = response.code();
                handler.sendEmptyMessage(200);
                Log.i("3433333332222", strs);
            }
        });
    }
}
