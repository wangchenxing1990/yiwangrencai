package com.yiwangrencai.ywkj.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
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
import com.tencent.tauth.IUiListener;
import com.yiwangrencai.R;
import com.yiwangrencai.wxapi.WXEntryActivity;
import com.yiwangrencai.ywkj.adapter.MyInterViewAdapter;
import com.yiwangrencai.ywkj.bean.InterViewBean;
import com.yiwangrencai.ywkj.bean.SubmitBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.UiUtils;
import com.yiwangrencai.ywkj.view.RecycleViewDivider;
import com.yiwangrencai.ywkj.view.SimpleDividerItemDecoration;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */
public class InterviewActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_interview;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    switchView();//根据请求数据返回的数据判断显示的界面
                    break;
                case 110:
                    frameLayout.removeView(loadingView);
                    frameLayout.addView(emptyView);
                    Toast.makeText(InterviewActivity.this, "联网失败,请稍后重试", Toast.LENGTH_SHORT).show();
                    break;
                case 130:
                    loadMoreResult();
                    break;
                case 300:
                    parsRefreshNewData();//解析下拉刷新的数据
                    break;
                case CHECK_INTERVIEW:
                    parsCheckData();//解析查看返回的数据
                    break;
                case ONE_PAGE:
                    parsOnePageData();//解析当前也是第一页时候的下拉刷新
                    break;
            }
        }
    };


    /**
     * 解析返回的查看数据
     */
    private void parsCheckData() {
        if (code == 200) {
            JSONObject jsonObject = JSON.parseObject(str);
            String codes = jsonObject.getString("code");
            if ("1".equals(codes)) {
                SharedPreferences shared = getSharedPreferences("data", MODE_PRIVATE);
                int badges = shared.getInt("badges", 0);
                badges = badges - 1;
                Log.i("badges", badges + "");
                SharedPreferences.Editor editor = shared.edit();
                editor.putInt("badges", badges);
                editor.commit();
                Intent intent = new Intent();
                intent.setAction("com.xiazdong");

            } else {
                Toast.makeText(InterviewActivity.this, "网络异常,稍后查看", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(InterviewActivity.this, "网络异常,稍后查看", Toast.LENGTH_SHORT).show();
        }
    }

    private void parsRefreshNewData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codess = json.getString("code");
            if ("1".equals(codess)) {
                JSONArray jsonArray = json.getJSONArray("data");
                listInterView.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    InterViewBean interBean = new InterViewBean();
                    interBean.setId(jsonObject.getString("id"));
                    interBean.setJob_id(jsonObject.getString("job_id"));
                    interBean.setInterview_time(jsonObject.getString("interview_time"));
                    interBean.setStatus(jsonObject.getString("status"));
                    interBean.setRemark(jsonObject.getString("remark"));
                    interBean.setContacts(jsonObject.getString("contacts"));
                    interBean.setMobile(jsonObject.getString("mobile"));
                    interBean.setPhone(jsonObject.getString("phone"));
                    interBean.setCompany_name(jsonObject.getString("company_name"));
                    interBean.setJob_title(jsonObject.getString("job_title"));
                    interBean.setAddress(jsonObject.getString("address"));
                    interBean.setLines(jsonObject.getString("lines"));
                    interBean.setTime(jsonObject.getString("time"));
                    interBean.setSalary(jsonObject.getString("salary"));

                    listInterView.add(interBean);
                }

                interAdapter.notifyDataSetChanged();
                xrefreshview.stopRefresh();
                xrefreshview.setLoadComplete(false);

            } else {
                Toast.makeText(InterviewActivity.this, "刷新失败,请稍后重试", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(InterviewActivity.this, "刷新失败,请稍后重试", Toast.LENGTH_SHORT).show();
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
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    InterViewBean interBean = new InterViewBean();
                    interBean.setId(jsonObject.getString("id"));
                    interBean.setJob_id(jsonObject.getString("job_id"));
                    interBean.setInterview_time(jsonObject.getString("interview_time"));
                    interBean.setStatus(jsonObject.getString("status"));
                    interBean.setRemark(jsonObject.getString("remark"));
                    interBean.setContacts(jsonObject.getString("contacts"));
                    interBean.setMobile(jsonObject.getString("mobile"));
                    interBean.setPhone(jsonObject.getString("phone"));
                    interBean.setCompany_name(jsonObject.getString("company_name"));
                    interBean.setJob_title(jsonObject.getString("job_title"));
                    interBean.setAddress(jsonObject.getString("address"));
                    interBean.setLines(jsonObject.getString("lines"));
                    interBean.setTime(jsonObject.getString("time"));
                    interBean.setSalary(jsonObject.getString("salary"));

                    listInterView.add(interBean);
                }
                interAdapter.notifyDataSetChanged();
            } else {

            }
        } else {

        }
    }

    String next_page_url;
    String prev_page_url;
    String current_page;

    /**
     * 根据从服务器获得数据判断显示的界面
     */
    private void switchView() {
        if (code == 200) {//请求成功
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            current_page = json.getString("current_page");
            String msg = json.getString("msg");
            if (codes.equals("1")) {
                JSONArray jsonArray = json.getJSONArray("data");

                frameLayout.removeView(loadingView);
                if (jsonArray.size() == 0) {//请求的数据为空就是没有收到面试通知显示的界面
                    frameLayout.addView(interViewEmptyView);
                } else {//请求到的数据有面试的通知显示的界面
                    frameLayout.addView(successView);
                    paseJsonArrayData(jsonArray);//解析请求的面试通知的数据
                }

            } else if (codes.equals("0")) {

            } else if (codes.equals("-1")) {
                Intent intent = new Intent(InterviewActivity.this, WXEntryActivity.class);
                intent.putExtra("register", "loginMine");
                startActivity(intent);
                finish();
            }
            if (msg!=null&&!"".equals(msg)){
                Toast.makeText(InterviewActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        } else {//请求失败
            frameLayout.removeView(loadingView);
            frameLayout.addView(emptyView);
            Toast.makeText(InterviewActivity.this, "联网失败", Toast.LENGTH_SHORT).show();
        }
    }

    private List<InterViewBean> listInterView = new ArrayList<>();
    MyInterViewAdapter interAdapter;

    /**
     * 解析请求的面试通知的数据
     *
     * @param jsonArray
     */
    String api_token;
    String id;
    String ids;

    private void paseJsonArrayData(JSONArray jsonArray) {
        listInterView.clear();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            InterViewBean interBean = new InterViewBean();
            interBean.setId(jsonObject.getString("id"));
            interBean.setJob_id(jsonObject.getString("job_id"));
            interBean.setInterview_time(jsonObject.getString("interview_time"));
            interBean.setStatus(jsonObject.getString("status"));
            interBean.setRemark(jsonObject.getString("remark"));
            interBean.setContacts(jsonObject.getString("contacts"));
            interBean.setMobile(jsonObject.getString("mobile"));
            interBean.setPhone(jsonObject.getString("phone"));
            interBean.setCompany_name(jsonObject.getString("company_name"));
            interBean.setJob_title(jsonObject.getString("job_title"));
            interBean.setAddress(jsonObject.getString("address"));
            interBean.setLines(jsonObject.getString("lines"));
            interBean.setTime(jsonObject.getString("time"));
            interBean.setSalary(jsonObject.getString("salary"));

            listInterView.add(interBean);
        }

        if (next_page_url == null || "".equals(next_page_url)) {
            xrefreshview.setLoadComplete(true);
            xrefreshview.setPullLoadEnable(false);
        }

        interAdapter = new MyInterViewAdapter(listInterView, this);
        recycler_view_test_rv.setAdapter(interAdapter);

        xrefreshview.setAutoLoadMore(false);
        xrefreshview.setPinnedTime(1000);
        xrefreshview.setMoveForHorizontal(true);

        interAdapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
        xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                refreshMoreData();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                loadMoreData();//加载更多数据
            }
        });

        interAdapter.setOnItemClickListener(new MyInterViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, InterViewBean data) {
                id = data.getJob_id();
                ids = data.getId();
                if (data.getStatus().equals("0")) {
                    checkInterViewNotion();//查看面试通知
                    Intent intent = new Intent(InterviewActivity.this, JobInfoActivity.class);
                    if ("message".equals(register)){
                        intent.putExtra("register", "message");
                    }else if("interView".equals(register)){
                        intent.putExtra("register", "interView");
                    }
                    intent.putExtra("id", id);
                    intent.putExtra("ids", ids);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(InterviewActivity.this, JobInfoActivity.class);
                    intent.putExtra("id", data.getJob_id());
                    intent.putExtra("ids", data.getId());
                    if ("message".equals(register)){
                        intent.putExtra("register", "message");
                    }else if("interView".equals(register)){
                        intent.putExtra("register", "interView");
                    }
                    intent.putExtra("statues", data.getStatus());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 查看面试通知
     */
    private static final int CHECK_INTERVIEW = 1;

    private void checkInterViewNotion() {

        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formcode = new FormEncodingBuilder();
        formcode.add("id", ids);
        formcode.add("status", "1");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.INTERVIEW_UPDATE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formcode.build())
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("查看面试通知", str);
                handler.sendEmptyMessage(CHECK_INTERVIEW);
            }
        });
    }

    /**
     * 下拉刷新
     */
    private void refreshMoreData() {
        if ("1".equals(current_page)) {//当前页为第一页的时候的下拉刷新
            refreshNewOnePage();//当前页为第一页的时候下拉刷新
        } else {//当前页不是第一页的时候刷新
            if (prev_page_url == null) {
                xrefreshview.stopRefresh();
            } else {
                refreshNewData();//下拉刷新数据
            }
        }

    }

    /**
     * 当前页为第一页的时候下拉刷新
     */
    private static final int ONE_PAGE = 2;

    private void refreshNewOnePage() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEd = new FormEncodingBuilder();
        formEd.add("page", "1");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.INTERVIEW_RECEIVE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEd.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("hhhhh", str);
                handler.sendEmptyMessage(ONE_PAGE);
            }
        });
    }

    /**
     * 解析当前也是第一页时候的下拉刷新
     */
    private void parsOnePageData() {
        if (code == 200) {
            JSONObject jsonObject1 = JSON.parseObject(str);
            String codes = jsonObject1.getString("code");
            String msg = jsonObject1.getString("msg");
            next_page_url = jsonObject1.getString("next_page_url");
            prev_page_url = jsonObject1.getString("prev_page_url");
            current_page = jsonObject1.getString("current_page");
            if ("1".equals(codes)) {
                JSONArray jsonArray = jsonObject1.getJSONArray("data");
                listInterView.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    InterViewBean interBean = new InterViewBean();
                    interBean.setId(jsonObject.getString("id"));
                    interBean.setJob_id(jsonObject.getString("job_id"));
                    interBean.setInterview_time(jsonObject.getString("interview_time"));
                    interBean.setStatus(jsonObject.getString("status"));
                    interBean.setRemark(jsonObject.getString("remark"));
                    interBean.setContacts(jsonObject.getString("contacts"));
                    interBean.setMobile(jsonObject.getString("mobile"));
                    interBean.setPhone(jsonObject.getString("phone"));
                    interBean.setCompany_name(jsonObject.getString("company_name"));
                    interBean.setJob_title(jsonObject.getString("job_title"));
                    interBean.setAddress(jsonObject.getString("address"));
                    interBean.setLines(jsonObject.getString("lines"));
                    interBean.setTime(jsonObject.getString("time"));
                    interBean.setSalary(jsonObject.getString("salary"));

                    listInterView.add(interBean);
                }

                if (next_page_url == null || "".equals(next_page_url)) {
                    xrefreshview.setLoadComplete(true);
                    xrefreshview.setPullLoadEnable(false);
                }
                xrefreshview.stopRefresh();
                interAdapter.notifyDataSetChanged();

            } else {

            }

            if (msg != null && !"".equals(msg)) {
                Toast.makeText(InterviewActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

        } else {

        }

    }

    /**
     * 下拉刷新数据
     */
    private void refreshNewData() {

        OkHttpClient okhttps = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(prev_page_url)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncoding.build())
                .build();
        okhttps.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("123456677889", str);
                handler.sendEmptyMessage(300);
            }
        });
    }

    /**
     * 加载更多数据
     */
    private void loadMoreData() {
        if (next_page_url == null) {
            xrefreshview.setLoadComplete(true);
        } else {
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

    @Override
    public void initData() {
        SharedPreferences shares = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = shares.getString("api_token", "");
        gainDataFromService();
    }

    private int flags;
    private FrameLayout fram_job_back;
    private FrameLayout frameLayout;
    private ListView list_view_submit_resume;
    private View emptyView;
    private View loadingView;
    private View successView;
    private View interViewEmptyView;
    private String register="";
    @Override
    public void initView() {
        Intent intent=getIntent();
        register=intent.getStringExtra("register");
        SharedPreferences sharedd = getSharedPreferences("data", MODE_PRIVATE);
        String names = sharedd.getString("name", "");
        String avatar = sharedd.getString("avatar", "");
        Log.i("lognamenamename11111", names);
        Log.i("avatar", avatar);
        System.out.print("1111111111" + names);
        System.out.print("avatar" + avatar);

        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        list_view_submit_resume = (ListView) findViewById(R.id.list_view_submit_resume);

        emptyView = createEmptyView();
        loadingView = createLoadingView();
        successView = createSuccessView();
        interViewEmptyView = createinterViewEmptyView();

        frameLayout.addView(loadingView);
        fram_job_back.setOnClickListener(this);

    }

    /**
     * 创建没有数据的界面
     *
     * @return
     */
    private View createinterViewEmptyView() {
        View view = View.inflate(InterviewActivity.this, R.layout.empty_resume_view, null);
        TextView textview = (TextView) view.findViewById(R.id.textview);
        textview.setText("你还没有面试通知的邀请");
        return view;
    }

    private XRefreshView xrefreshview;
    private RecyclerView recycler_view_test_rv;

    /**
     * 创建加载数据成功的界面
     */
    private View createSuccessView() {
        View view = View.inflate(InterviewActivity.this, R.layout.fram_listview, null);
        xrefreshview = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);

        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(this));
        xrefreshview.setPullLoadEnable(true);
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

    /**
     * 控件的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back://返回
                Intent intent = new Intent(InterviewActivity.this, MainActivity.class);
                if ("message".equals(register)){
                    intent.putExtra("register", "message");
                }else if("interView".equals(register)){
                    intent.putExtra("register", "interView");
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.textview_click://点击获取数据
                frameLayout.removeView(emptyView);
                frameLayout.addView(loadingView);
                gainDataFromService();
                break;
        }
    }

    private String str;
    private int code;

    /**
     * 从服务器获取数据
     */
    private void gainDataFromService() {
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.INTERVIEW_RECEIVE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncoding.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
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
                System.out.println(";;;;;;;;;;;" + str);
                handler.sendEmptyMessage(100);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(InterviewActivity.this, MainActivity.class);
        if ("message".equals(register)){
            intent.putExtra("register", "message");
        }else if("interView".equals(register)){
            intent.putExtra("register", "interView");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
