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
import com.yiwangrencai.ywkj.activity.CompanydetailActivity;
import com.yiwangrencai.ywkj.activity.JobInfoActivity;
import com.yiwangrencai.ywkj.adapter.MyJobBrowseAdapter;
import com.yiwangrencai.ywkj.adapter.MyLookMeAdapter;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.RecycleViewDivider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/26.
 */

public class CompanyFragment extends Fragment {
    private View loadingView;
    private View successView;
    private View emptyView;
    private FrameLayout framLayout;
    private String api_token;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences share = getActivity().getSharedPreferences("Activity", Context.MODE_PRIVATE);
        api_token = share.getString("api_token", "");
        framLayout=new FrameLayout(getActivity());
        loadingView=inflater.inflate(R.layout.loading_view,null);
        successView=createSuccessView();
        emptyView=inflater.inflate(R.layout.empty_view_icon,null);
        TextView textview_click= (TextView) emptyView.findViewById(R.id.textview_click);
        textview_click.setText("还没有公司浏览记录");
        framLayout.addView(loadingView);

        initData();
        return framLayout;
    }

    String strs;
    int codes;
    private void initData() {
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
    /**
     * 创建成功界面
     *
     * @return
     */
    XRefreshView recycler_view_rightt;
    RecyclerView recycler_view_test_rv_right;
    MyLookMeAdapter companyadapter;
    List<CompanyJobBean> list = new ArrayList<>();
    private View createSuccessView() {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.fram_listview,null);
        recycler_view_rightt = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv_right = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);

        recycler_view_test_rv_right.setLayoutManager(new LinearLayoutManager(getActivity()));

        recycler_view_test_rv_right.addItemDecoration(new RecycleViewDivider(getActivity(), RecyclerView.HORIZONTAL));
        recycler_view_rightt.setPullLoadEnable(true);
        return view;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                ase 100:
//                switchView();//根据请求的结果切换不同的界面
//                break;c
                case 110:
                    break;
                case 200:
                    parsDataCompany();//解析获取公司浏览的数据
                    break;
//                case 300:
//                    parsDataRefresh();//解析刷新的数据
//                    break;
//                case 301:
//                    parsPullDataMore();//解析上拉加载更多数据
//                    break;
                case 400:
                    parsCompanyDataUp();//解析上啦加载公司浏览的数据
                    break;
                case 410:
                    parsRefreshNewDataCompany();//解析下拉刷新的公司浏览记录的数据
                    break;
//                case ONE_PAGE:
//                    parsOneDataNew();//解析当前是第一页的时候的数据
//                    break;
                case ONE_COMPANY_PAGE:
                    parsOneDataNewCompany();//解析当前是第一页的时候的数据
                    break;

            }
        }
    };
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
                jsonArrayCompay = jsonObject.getJSONArray("data");
                framLayout.removeView(loadingView);
                if (jsonArrayCompay.size()==0){
                    framLayout.addView(emptyView);
                }else {
                    framLayout.addView(successView);

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
                    recycler_view_test_rv_right.addItemDecoration(new RecycleViewDivider(getActivity(), RecyclerView.HORIZONTAL));
                    companyadapter.setCustomLoadMoreView(new XRefreshViewFooter(getActivity()));

                    if (next_page_urls == null || "".equals(next_page_urls)) {
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
                            Intent intent = new Intent(getActivity(), CompanydetailActivity.class);
                            intent.putExtra("id", data.getEcom_id());
                            startActivity(intent);
                        }
                    });
                }
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
     * 刷新公司当前页是第一页的时候的数据
     */
    String str;
    int code;
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
    String current_page;
    String next_page_url;
    String prev_page_url;
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
                Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
            }
        }else{

        }
    }
}
