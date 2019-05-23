package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import com.yiwangrencai.ywkj.adapter.MyJobSearchAdapter;
import com.yiwangrencai.ywkj.adapter.MySalaryAdapter;
import com.yiwangrencai.ywkj.adapter.MySearchAreaAdapter;
import com.yiwangrencai.ywkj.adapter.MySearchCityAdapter;
import com.yiwangrencai.ywkj.bean.AreaCity;
import com.yiwangrencai.ywkj.bean.AreaCounty;
import com.yiwangrencai.ywkj.bean.AreaLocationBean;
import com.yiwangrencai.ywkj.bean.AreaLocationBeanss;
import com.yiwangrencai.ywkj.bean.AreaOne;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.InitDatas;
import com.yiwangrencai.ywkj.tools.ViewUtilss;
import com.yiwangrencai.ywkj.view.RecycleViewDivider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */
public class SearchResultActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_search_result;
    }

    String keyword;
    String industryname;
    String location;
    String salary_above;
    String salary_below;
    String issuetime;
    String industryId = "";
    String locationId;
    String issuedataId;
    String keyword_type;
    List<String> job_category = new ArrayList<>();
    List<String> job_categoryId = new ArrayList<>();
    String employee_num = "";
    String employee_numId = "";
    String comkind = "";
    String comkindId = "";
    String industry = "";
    String work_year = "";
    String work_yearId = "";
    String updatetime = "";
    String updatetimeId = "";
    String api_token;
    String sex;
    String jobarea;
    String jobsort;
    String education;
    String age;
    String search_name;
    String localAreas = "";
    String localArea = "";
    String params = "";

    @Override
    public void initData() {
        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");
        keyword_type = intent.getStringExtra("keyword_type");
        industryname = intent.getStringExtra("industryname");
        location = intent.getStringExtra("location");
        sex = intent.getStringExtra("sex");
        salary_above = intent.getStringExtra("salarylittle");
        salary_below = intent.getStringExtra("salaryhigh");
        industryId = intent.getStringExtra("industry");
        locationId = intent.getStringExtra("locationId");

        jobarea = intent.getStringExtra("jobarea");
        jobsort = intent.getStringExtra("jobsort");
        education = intent.getStringExtra("education");
        work_year = intent.getStringExtra("work_year");
        work_yearId = intent.getStringExtra("work_yearId");
        keyword_type = intent.getStringExtra("keyword_type");
        age = intent.getStringExtra("age");
        updatetime = intent.getStringExtra("release_date");
        updatetimeId = intent.getStringExtra("release_dateId");
        search_name = intent.getStringExtra("search_name");
        params = intent.getStringExtra("params");

        SharedPreferences shares = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = shares.getString("api_token", "");

        //gainLocationAreaFile();//获取本地的城市
    }

    String strarea;

    /**
     * 解析地域的城市
     */
    private void gainLocationArea() {

        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.AREA_CODE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .post(formEncoding.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strarea = response.body().string();
                Log.i("areaareaareaare", strarea);
                handler.sendEmptyMessage(1000);
            }
        });
    }

    private TextView textcatgory;
    private TextView textArea;
    private TextView textsalary;
    private TextView textmore;
    private EditText edittext_input;
    private FrameLayout frameLayoutSearch;
    private View loadingView;
    private View emptyView;
    private View successView;

    @Override
    public void initView() {

        FrameLayout iv_search_back = (FrameLayout) findViewById(R.id.iv_search_back);
        TextView textview_search = (TextView) findViewById(R.id.textview_search);
        edittext_input = (EditText) findViewById(R.id.edittext_input);
        LinearLayout linearLayoutCategory = (LinearLayout) findViewById(R.id.linearLayoutCategory);
        LinearLayout liearLayoutArea = (LinearLayout) findViewById(R.id.liearLayoutArea);
        LinearLayout liearLayoutSalary = (LinearLayout) findViewById(R.id.liearLayoutSalary);
        LinearLayout linearLayoutMore = (LinearLayout) findViewById(R.id.linearLayoutMore);
        textcatgory = (TextView) findViewById(R.id.textcatgory);
        textArea = (TextView) findViewById(R.id.textArea);
        textsalary = (TextView) findViewById(R.id.textsalary);
        textmore = (TextView) findViewById(R.id.textmore);

        frameLayoutSearch = (FrameLayout) findViewById(R.id.frameLayoutSearch);
        loadingView = createLoadingView();
        emptyView = createEmptyView();
        successView = createSuccessView();

        frameLayoutSearch.addView(loadingView);
        if (location != null && !"".equals(location)) {
            textArea.setText(location);
        }
        if (keyword != null) {
            edittext_input.setText(keyword);
        }

        if (salary_above != null && !"".equals(salary_above)) {
            textsalary.setText(salary_above);
        } else {

        }

        textview_search.setOnClickListener(this);
        iv_search_back.setOnClickListener(this);
        linearLayoutCategory.setOnClickListener(this);
        liearLayoutArea.setOnClickListener(this);
        liearLayoutSalary.setOnClickListener(this);
        linearLayoutMore.setOnClickListener(this);

        searchJob();//搜索

    }

    /**
     * 创建加载成功的界面
     */
    RecyclerView recycler_view_test_rv;
    XRefreshView xrefreshview;

    private View createSuccessView() {

        View view = View.inflate(SearchResultActivity.this, R.layout.fram_listview, null);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);
        xrefreshview = (XRefreshView) view.findViewById(R.id.xrefreshview);
        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(this));

        recycler_view_test_rv.addItemDecoration(new RecycleViewDivider(this, RecyclerView.HORIZONTAL));
        xrefreshview.setPullLoadEnable(true);
        return view;
    }

    /**
     * 创建加载为空的界面
     *
     * @return
     */
    private View createEmptyView() {
        View view = View.inflate(SearchResultActivity.this, R.layout.empty_view_image, null);
        return view;
    }

    /**
     * 创建正在加载的界面
     */
    private View createLoadingView() {
        View view = View.inflate(SearchResultActivity.this, R.layout.loading_view, null);
        return view;
    }

    boolean two;
    boolean three;
    String keywordss = "";
    boolean isFirst = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_search://搜索
                SharedPreferences shared = getSharedPreferences("data", MODE_PRIVATE);
                keywordss = shared.getString("search", "");
                keyword = edittext_input.getText().toString().trim();
                if (keywordss.equals("") && !"".equals(keyword)) {
                    keywordss = keyword;
                } else if (keywordss.equals("") && "".equals(keyword)) {

                } else if (keywordss.contains(keyword)) {

                } else {
                    keywordss = keywordss + "," + keyword;
                }

                SharedPreferences.Editor editor = shared.edit();
                editor.putString("search", keywordss);
                editor.commit();
                selectRefreshNewData();
                break;
            case R.id.iv_search_back://返回
                if (params!=null&&"highSearch".equals(params)) {
                    finish();
                } else {
                    Intent intent = new Intent(SearchResultActivity.this, SearchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.linearLayoutCategory://职位类别
                Intent intentJob = new Intent(SearchResultActivity.this, AllWorkActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("job_category", (ArrayList<String>) job_category);
                bundle.putStringArrayList("job_categoryId", (ArrayList<String>) job_categoryId);
                intentJob.putExtras(bundle);

                startActivityForResult(intentJob, 50);
                if (popupwindow != null) {
                    popupwindow.dismiss();
                }
                if (areaPopupWindow != null) {
                    areaPopupWindow.dismiss();
                }
                textsalary.setTextColor(0xff555555);
                textArea.setTextColor(0xff555555);
                break;
            case R.id.liearLayoutArea://地区
                if ("".equals(localAreas) && "".equals(localArea)) {
                    gainLocationAreaFile();
                }

                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                }
                if (areaPopupWindow != null && areaPopupWindow.isShowing()) {
                    areaPopupWindow.dismiss();
                    areaPopupWindow = null;
                    textcatgory.setTextColor(0xff555555);
                    textArea.setTextColor(0xff555555);
                    textsalary.setTextColor(0xff555555);
                    textmore.setTextColor(0xff555555);
                } else {
                    showAreaPopuwind();//显示薪资的对话框
                    textcatgory.setTextColor(0xff555555);
                    textArea.setTextColor(0xff42bfb6);
                    textsalary.setTextColor(0xff555555);
                    textmore.setTextColor(0xff555555);
                }


                break;
            case R.id.liearLayoutSalary://薪资

                if (areaPopupWindow != null && areaPopupWindow.isShowing()) {
                    areaPopupWindow.dismiss();
                    areaPopupWindow = null;
                }
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                    textcatgory.setTextColor(0xff555555);
                    textArea.setTextColor(0xff555555);
                    textsalary.setTextColor(0xff555555);
                    textmore.setTextColor(0xff555555);
                } else {
                    showSalarlyPopuwind();//显示薪资的对话框
                    textcatgory.setTextColor(0xff555555);
                    textArea.setTextColor(0xff555555);
                    textsalary.setTextColor(0xff42bfb6);
                    textmore.setTextColor(0xff555555);
                }

                break;
            case R.id.linearLayoutMore://更多
                if (popupwindow != null) {
                    popupwindow.dismiss();
                }
                if (areaPopupWindow != null) {
                    areaPopupWindow.dismiss();
                }
                textArea.setTextColor(0xff555555);
                textsalary.setTextColor(0xff555555);
                Intent intentMore = new Intent(SearchResultActivity.this, MoreSelectActivity.class);
                intentMore.putExtra("industry", industry);
                intentMore.putExtra("industryId", industryId);
                intentMore.putExtra("work_year", work_year);
                intentMore.putExtra("work_yearId", work_yearId);
                intentMore.putExtra("employee_num", employee_num);
                intentMore.putExtra("employee_numId", employee_numId);
                intentMore.putExtra("comkind", comkind);
                intentMore.putExtra("comkindId", comkindId);
                intentMore.putExtra("updatetime", updatetime);
                intentMore.putExtra("updatetimeId", updatetimeId);
                startActivityForResult(intentMore, 60);
                break;
        }
    }

    /**
     * 显示地区的popuwind
     */
    private PopupWindow areaPopupWindow;
    private ListView listview_right;
    int positions;
    private Boolean flagg;
    ListView listview_left;
    List<AreaLocationBeanss.DataBean.NextsBean> listTaiZ = new ArrayList();
    private boolean isTai;

    private void showAreaPopuwind() {

        View contentVieww = LayoutInflater.from(SearchResultActivity.this).inflate(R.layout.popuwin_area, null);
        areaPopupWindow = new PopupWindow(this);
        areaPopupWindow.setContentView(contentVieww);

        areaPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        areaPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        areaPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        areaPopupWindow.showAsDropDown(findViewById(R.id.liearLayoutArea));
        listview_left = (ListView) contentVieww.findViewById(R.id.listview_left);
        listview_right = (ListView) contentVieww.findViewById(R.id.listview_right);

        MySearchAreaAdapter adapter = new MySearchAreaAdapter(listArea);
        listview_left.setAdapter(adapter);

        listview_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if (position == 0) {
                    textArea.setText("不限");
                    locationId = "";
                    textArea.setTextColor(0xff555555);
                    areaPopupWindow.dismiss();
                    selectRefreshNewData();//选择搜索条件之后的刷新
                } else if (position == 1) {
                    listview_right.setAdapter(new MySearchCityAdapter(listTaiZ));
                    isTai = true;
                } else {
                    if (areaLocationBeanss.getData().get(position - 2).getNext() == 1) {
                        listview_right.setAdapter(new MySearchCityAdapter(areaLocationBeanss.getData().get(position - 2).getNexts()));
                    }
                    positions = position - 2;
                    isTai = false;
                }
            }
        });

        areaPopupWindow.setFocusable(false);
        areaPopupWindow.update();
        listview_right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                areaPopupWindow.dismiss();
                if (isTai) {
                    textArea.setText(listTaiZ.get(position).getName());
                    locationId = areaLocationBean.getData().getNexts().get(position).getCid() + "";
                } else {
                    textArea.setText(areaLocationBeanss.getData().get(positions).getNexts().get(position).getName());
                    locationId = areaLocationBeanss.getData().get(positions).getNexts().get(position).getCid() + "";
                }

                textArea.setTextColor(0xff555555);
                selectRefreshNewData();//选择搜索条件之后的刷新
            }
        });
    }

    /**
     * 显示薪资的弹出框
     */
    PopupWindow popupwindow;
    List<String> saralyDatass;
    String saraly = "";

    private void showSalarlyPopuwind() {
        View contentView = LayoutInflater.from(SearchResultActivity.this).inflate(R.layout.popuwin_salary, null);
        popupwindow = new PopupWindow(this);
        popupwindow.setContentView(contentView);
        popupwindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupwindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupwindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupwindow.showAsDropDown(findViewById(R.id.liearLayoutSalary));
        ListView listView_salary = (ListView) contentView.findViewById(R.id.listView_salary);
        saralyDatass = InitDatas.saralyDatass();
        popupwindow.setFocusable(false);
        popupwindow.update();
        popupwindow.setOutsideTouchable(false);

        listView_salary.setAdapter(new MySalaryAdapter(saralyDatass, saraly));
        listView_salary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textsalary.setText(saralyDatass.get(position));
                textsalary.setTextColor(0xff555555);
                saraly = saralyDatass.get(position);
                sureSalary(position);//确定薪资
                popupwindow.dismiss();
                selectRefreshNewData();//选择搜索条件之后的刷新
            }
        });
    }

    /**
     * 确定薪资
     */
    private void sureSalary(int position) {
        switch (position) {
            case 0:
                salary_above = "";
                salary_below = "";
                break;

            case 1:
                salary_above = "";
                salary_below = "2000";
                break;

            case 2:
                salary_above = "2000";
                salary_below = "4000";
                break;

            case 3:
                salary_above = "4000";
                salary_below = "6000";
                break;

            case 4:
                salary_above = "6000";
                salary_below = "8000";
                break;

            case 5:
                salary_above = "8000";
                salary_below = "10000";
                break;

            case 6:
                salary_above = "10000";
                salary_below = "15000";
                break;

            case 7:
                salary_above = "15000";
                salary_below = "20000";
                break;

            case 8:
                salary_above = "20000";
                salary_below = "30000";
                break;

            case 9:
                salary_above = "30000";
                salary_below = "40000";
                break;

            case 10:
                salary_above = "40000";
                salary_below = "50000";
                break;

        }
    }

    /**
     * 搜索职位
     */
    private String str;
    private int code;
    OkHttpClient okhttp;

    private void searchJob() {

        okhttp = new OkHttpClient();
        FormEncodingBuilder formed = new FormEncodingBuilder();
        if (keyword != null) {
            formed.add("keyword", keyword);
        }

        if (keyword_type != null) {
            formed.add("keyword_type", keyword_type);
        }

        if (salary_above == null) {

        } else {
            formed.add("salary_above", salary_above);
        }

        if (salary_below == null) {
        } else {
            formed.add("salary_below", salary_below);
        }

        if (industryId != null) {
            formed.add("industry", industryId);
        }

        if (work_yearId != null) {
            formed.add("work_year", work_yearId);
        }

        if (updatetimeId != null) {
            formed.add("updatetime", updatetimeId);
        }

        if (jobCategoryId != null) {
            formed.add("job_category", jobCategoryId);
        }

        if (locationId != null) {
            formed.add("location", locationId);
        }

        if (sex != null) {
            formed.add("gender", sex);
        }

        if (education != null) {
            formed.add("education", education);
        }

        if (age != null) {
            formed.add("age", age);
        }
        if (jobsort != null) {
            formed.add("jobsort", jobsort);
        }

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.COMPANY_JOB_SEARCH)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formed.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("22222222", str);
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
                    parsSearchData();//解析数据
                    break;
                case 200:
                    parsLoadMoreData();//解析上拉加载更多的数据
                    break;
                case 220:
                    parsGainRefreshNewData();//解析下拉刷新获取更多的数据
                    break;
                case 1000://解析从网上获取的area的数据
                    //  praseJson();
                    //读取本地数据
                    String localArea = ViewUtilss.praseLocal(R.raw.area);
                    //解析本地数据
                    praseLocalArea(localArea);
                    break;
                case 130:
                    parsStartRefresh();//解析选择数据后的刷新数据
                    break;
                case ONE_PAGE:
                    parsonDataOnePage();//解析获取的当前为第一个界面的下拉刷新的数据
                    break;
            }
        }
    };

    /**
     * 解析获取的当前为第一个界面的下拉刷新的数据
     */
    private void parsonDataOnePage() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            String msg = json.getString("msg");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            if ("1".equals(codes)) {
                JSONArray jsonArray = json.getJSONArray("data");
                listCompany.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    CompanyJobBean companyJobBean = new CompanyJobBean();
                    JSONObject jsondata = jsonArray.getJSONObject(i);
                    companyJobBean.setId(jsondata.getString("id"));
                    companyJobBean.setJob_title(jsondata.getString("job_title"));
                    companyJobBean.setUpdatetime(jsondata.getString("updatetime"));
                    companyJobBean.setPart_status(jsondata.getString("part_status"));
                    companyJobBean.setLocation_name(jsondata.getString("location_name"));
                    companyJobBean.setSalary(jsondata.getString("salary"));
                    companyJobBean.setEducation_name(jsondata.getString("education_name"));
                    companyJobBean.setWork_year_name(jsondata.getString("work_year_name"));
                    companyJobBean.setCompany_name(jsondata.getString("company_name"));
                    companyJobBean.setIs_urgent(jsondata.getString("is_urgent"));
                    companyJobBean.setLogo(jsondata.getString("logo"));
                    listCompany.add(companyJobBean);
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
                Toast.makeText(SearchResultActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 解析选择数据后的刷新数据
     */
    private void parsStartRefresh() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codes = json.getString("code");
            //  Log.i("aaaaaaaa",next_page_url);
            if ("1".equals(codes)) {
                JSONArray jsonArray = json.getJSONArray("data");
                if (jsonArray.size() == 0) {
                    frameLayoutSearch.removeView(loadingView);
                    frameLayoutSearch.removeAllViews();
                    frameLayoutSearch.addView(emptyView);
                    // Toast.makeText(SearchResultActivity.this, "是否刷新成功", Toast.LENGTH_SHORT).show();
                } else {
                    frameLayoutSearch.removeView(loadingView);
                    frameLayoutSearch.removeAllViews();
                    frameLayoutSearch.addView(successView);
                    listCompany.clear();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        CompanyJobBean companyJobBean = new CompanyJobBean();
                        JSONObject jsondata = jsonArray.getJSONObject(i);
                        companyJobBean.setId(jsondata.getString("id"));
                        companyJobBean.setJob_title(jsondata.getString("job_title"));
                        companyJobBean.setUpdatetime(jsondata.getString("updatetime"));
                        companyJobBean.setPart_status(jsondata.getString("part_status"));
                        companyJobBean.setLocation_name(jsondata.getString("location_name"));
                        companyJobBean.setSalary(jsondata.getString("salary"));
                        companyJobBean.setEducation_name(jsondata.getString("education_name"));
                        companyJobBean.setWork_year_name(jsondata.getString("work_year_name"));
                        companyJobBean.setCompany_name(jsondata.getString("company_name"));
                        companyJobBean.setIs_urgent(jsondata.getString("is_urgent"));
                        companyJobBean.setLogo(jsondata.getString("logo"));
                        listCompany.add(companyJobBean);
                    }

                    adapter = new MyJobSearchAdapter(listCompany, this);
                    recycler_view_test_rv.setAdapter(adapter);
                    //  Log.i("aaaaaaaa",next_page_url);
                    if ("".equals(next_page_url) || next_page_url == null) {
                        //   Toast.makeText(SearchResultActivity.this, "aaaaaa", Toast.LENGTH_SHORT).show();
                        xrefreshview.setLoadComplete(true);
                        Log.i("aaaaaaaa", "aa");

                        xrefreshview.setPullLoadEnable(false);
                    }

                    xrefreshview.setAutoLoadMore(false);
                    xrefreshview.setPinnedTime(1000);
                    xrefreshview.setMoveForHorizontal(true);

                    adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
                    xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
                        @Override
                        public void onRefresh(boolean isPullDown) {
                            super.onRefresh(isPullDown);
                            refreshNewData();//下拉刷新数据
                        }

                        @Override
                        public void onLoadMore(boolean isSilence) {
                            super.onLoadMore(isSilence);
                            loadMoreNewData();//上拉加载更多数据
                        }
                    });
                    adapter.setOnItemClickListener(new MyJobSearchAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, CompanyJobBean data) {
                            Intent intent = new Intent(SearchResultActivity.this, JobInfoActivity.class);
                            intent.putExtra("id", data.getId());
                            startActivity(intent);
                        }
                    });

                }
            } else {

            }
        }
    }

    private List<AreaCity> listCode = new ArrayList<AreaCity>();
    /**
     * 解析数据
     */
    String areaname;

    private void praseJson(String strarea) {

        JSONObject jsonObject = JSON.parseObject(strarea);
        String data = jsonObject.getString("data");
        JSONObject jsonData = JSON.parseObject(data);
        areaname = jsonData.getString("name");
        JSONArray jsonArray = jsonData.getJSONArray("nexts");
        listCode.clear();
        for (int i = 0; i < jsonArray.size(); i++) {

            AreaCity areaCode = new AreaCity();
            JSONObject jsonObjectt = jsonArray.getJSONObject(i);
            areaCode.setName(jsonObjectt.getString("name"));
            areaCode.setCid(jsonObjectt.getString("cid"));

            listCode.add(areaCode);
        }
    }

    /**
     * 解析数据直辖市和省份的名字
     *
     * @param localArea
     */
    private List<AreaOne> listAreas = new ArrayList<AreaOne>();
    private List<List<AreaCity>> listAreass = new ArrayList<List<AreaCity>>();
    private List<List<List<AreaCounty>>> listAreasss = new ArrayList<List<List<AreaCounty>>>();
    private JSONArray jsonArrayCity;

    private void praseLocalArea(String localArea) {

        JSONObject jsonObject = JSON.parseObject(localArea);
        JSONArray jsonArrayProvince = jsonObject.getJSONArray("data");
        listAreas.clear();
        listAreass.clear();
        listAreasss.clear();
        List<AreaCity> areaListCityy = null;
        for (int i = 0; i < jsonArrayProvince.size(); i++) {

            AreaOne areaOne = new AreaOne();
            JSONObject jsonObjectt = JSON.parseObject(jsonArrayProvince.getString(i));
            String name = jsonObjectt.getString("name");
            String grade = jsonObjectt.getString("grade");
            String next = jsonObjectt.getString("next");
            String cid = jsonObjectt.getString("cid");

            if (next.equals("1")) {
                jsonArrayCity = jsonObjectt.getJSONArray("nexts");
                //解析各省里面的城市名
                areaListCityy = praseCity(jsonArrayCity);
                listAreass.add(areaListCityy);
            }

            areaOne.setName(name);
            areaOne.setGrade(grade);
            areaOne.setNext(next);
            areaOne.setCid(cid);

            listAreas.add(areaOne);

        }
    }

    /**
     * 解析数据各个省内和直辖市里的城市
     *
     * @param jsonArrayCity
     * @return
     */
    public List<AreaCity> praseCity(JSONArray jsonArrayCity) {
        List<AreaCity> areaListCity = new ArrayList<AreaCity>();
        List<AreaCounty> mapCountyy = new ArrayList<>();
        for (int i = 0; i < jsonArrayCity.size(); i++) {

            AreaCity areaCity = new AreaCity();
            JSONObject jsonObjectCity = JSON.parseObject(jsonArrayCity.getString(i));

            String name = jsonObjectCity.getString("name");
            String grade = jsonObjectCity.getString("grade");
            String next = jsonObjectCity.getString("next");
            String cid = jsonObjectCity.getString("cid");

            areaCity.setName(name);
            areaCity.setNext(next);
            areaCity.setGrade(grade);
            areaCity.setCid(cid);

            areaListCity.add(areaCity);
        }

        return areaListCity;
    }

    /**
     * 解析数据搜索的数据
     */
    String current_page;
    String next_page_url;
    String prev_page_url;
    private List<CompanyJobBean> listCompany = new ArrayList<>();
    MyJobSearchAdapter adapter;

    private void parsSearchData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codes = json.getString("code");
            if ("1".equals(codes)) {
                JSONArray jsonArray = json.getJSONArray("data");
                if (jsonArray.size() == 0) {
                    frameLayoutSearch.removeView(loadingView);
                    frameLayoutSearch.addView(emptyView);
                } else {
                    frameLayoutSearch.removeView(loadingView);
                    frameLayoutSearch.addView(successView);
                    for (int i = 0; i < jsonArray.size(); i++) {
                        CompanyJobBean companyJobBean = new CompanyJobBean();
                        JSONObject jsondata = jsonArray.getJSONObject(i);
                        companyJobBean.setId(jsondata.getString("id"));
                        companyJobBean.setJob_title(jsondata.getString("job_title"));
                        companyJobBean.setUpdatetime(jsondata.getString("updatetime"));
                        companyJobBean.setPart_status(jsondata.getString("part_status"));
                        companyJobBean.setLocation_name(jsondata.getString("location_name"));
                        companyJobBean.setSalary(jsondata.getString("salary"));
                        companyJobBean.setEducation_name(jsondata.getString("education_name"));
                        companyJobBean.setWork_year_name(jsondata.getString("work_year_name"));
                        companyJobBean.setCompany_name(jsondata.getString("company_name"));
                        companyJobBean.setIs_urgent(jsondata.getString("is_urgent"));
                        companyJobBean.setLogo(jsondata.getString("logo"));
                        listCompany.add(companyJobBean);
                    }

                    adapter = new MyJobSearchAdapter(listCompany, this);
                    recycler_view_test_rv.setAdapter(adapter);

                    if (next_page_url == null || "".equals(next_page_url)) {
                        xrefreshview.setPullLoadEnable(false);
                    }

                    xrefreshview.setAutoLoadMore(false);
                    xrefreshview.setPinnedTime(1000);
                    xrefreshview.setMoveForHorizontal(true);

                    adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
                    xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
                        @Override
                        public void onRefresh(boolean isPullDown) {
                            super.onRefresh(isPullDown);
                            refreshNewData();//下拉刷新数据
                        }

                        @Override
                        public void onLoadMore(boolean isSilence) {
                            super.onLoadMore(isSilence);
                            loadMoreNewData();//上拉加载更多数据
                        }
                    });
                    adapter.setOnItemClickListener(new MyJobSearchAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, CompanyJobBean data) {
                            Intent intent = new Intent(SearchResultActivity.this, JobInfoActivity.class);
                            intent.putExtra("id", data.getId());
                            startActivity(intent);
                        }
                    });
                }

            } else {
                //TOdo
            }
        } else {
            //TOdo
        }

    }

    List<String> listArea = new ArrayList<>();
    AreaLocationBeanss areaLocationBeanss;
    AreaLocationBean areaLocationBean;

    public void gainLocationAreaFile() {
        localAreas = new ViewUtilss().getJson(SearchResultActivity.this, "location_erea.json");
        //读取本地数据
        localArea = new ViewUtilss().getJson(SearchResultActivity.this, "area.json");

        Gson gson = new Gson();
        areaLocationBean = gson.fromJson(localAreas, AreaLocationBean.class);
        areaLocationBeanss = gson.fromJson(localArea, AreaLocationBeanss.class);
        for (int i = 0; i < areaLocationBean.getData().getNexts().size(); i++) {
            AreaLocationBeanss.DataBean.NextsBean nextsBean = new AreaLocationBeanss.DataBean.NextsBean();
            nextsBean.setCid(areaLocationBean.getData().getNexts().get(i).getCid());
            nextsBean.setName(areaLocationBean.getData().getNexts().get(i).getName());
            nextsBean.setGrade(areaLocationBean.getData().getNexts().get(i).getGrade());
            listTaiZ.add(nextsBean);
        }

        listArea.add("不限");
        listArea.add(areaLocationBean.getData().getName());
        for (int i = 0; i < areaLocationBeanss.getData().size(); i++) {
            listArea.add(areaLocationBeanss.getData().get(i).getName());
        }
    }

    /**
     * 下拉刷新获取数据
     */
    private boolean startRefresh;

    private void refreshNewData() {
        if ("1".equals(current_page)) {
            refreshOnePage();//当前是第一个页面时下拉刷新获取数据
        } else {
            if (startRefresh) {//是不是选择刷新
                selectRefreshNewData();//选择不同的数据后的刷新
            } else {
                if (prev_page_url == null) {
                    xrefreshview.stopRefresh();
                } else {
                    gainRefreshNewData();//刷新获取新数据
                }
            }
        }
    }

    /**
     * 当前是第一个页面时下拉刷新获取数据
     */
    private static final int ONE_PAGE = 1;

    private void refreshOnePage() {
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formed = new FormEncodingBuilder();
        if (keyword != null) {
            formed.add("keyword", keyword);
        }

        if (keyword_type != null) {
            formed.add("keyword_type", keyword_type);
        }

        if (salary_above == null) {

        } else {
            formed.add("salary_above", salary_above);
        }

        if (salary_below == null) {
        } else {
            formed.add("salary_below", salary_below);
        }

        if (industryId != null) {
            formed.add("industry", industryId);
        }

        if (work_yearId != null) {
            formed.add("work_year", work_yearId);
        }

        if (updatetimeId != null) {
            formed.add("updatetime", updatetimeId);
        }

        if (jobCategoryId != null) {
            formed.add("job_category", jobCategoryId);
        }

        if (locationId != null) {
            formed.add("location", locationId);
        }

        if (sex != null) {
            formed.add("gender", sex);
        }

        if (education != null) {
            formed.add("education", education);
        }

        if (age != null) {
            formed.add("age", age);
        }
        if (jobsort != null) {
            formed.add("jobsort", jobsort);
        }
//        if (keyword != null) {
//            formed.add("keyword", keyword);
//        }
//
//        if (keyword_type != null) {
//            formed.add("keyword_type", keyword_type);
//        }
//
//        if (salary_above == null) {
//
//        } else {
//            formed.add("salary_above", salary_above);
//        }
//
//        if (salary_below == null) {
//        } else {
//            formed.add("salary_below", salary_below);
//        }
//
//        if (industryId != null) {
//            formed.add("industry", industryId);
//        }
//
//        if (work_year != null) {
//            formed.add("work_year", work_year);
//        }
//
//        if (updatetime != null) {
//            formed.add("updatetime", updatetime);
//        }
//
//        if (locationId != null) {
//            formed.add("job_category", locationId);
//        }
//
//        if (jobarea != null) {
//            formed.add("jobarea", jobarea);
//        }
//
//        if (sex != null) {
//            formed.add("gender", sex);
//        }
//
//        if (education != null) {
//            formed.add("education", education);
//        }
//
//        if (age != null) {
//            formed.add("age", age);
//        }
//        if (jobsort != null) {
//            formed.add("jobsort", jobsort);
//        }

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.COMPANY_JOB_SEARCH)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formed.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("ssssss", str);
                handler.sendEmptyMessage(ONE_PAGE);
            }
        });
    }

    /**
     * 选择数据后的刷新
     */
    private void selectRefreshNewData() {
        frameLayoutSearch.removeAllViews();
        frameLayoutSearch.addView(loadingView);

        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formed = new FormEncodingBuilder();
        if (keyword != null) {
            formed.add("keyword", keyword);
        }

        if (keyword_type != null) {
            formed.add("keyword_type", keyword_type);
            Log.i("keyword_type", keyword_type);
        }

        if (salary_above != null) {
            formed.add("salary_above", salary_above);
        }

        if (salary_below != null) {
            formed.add("salary_below", salary_below);
        }

        if (industryId != null) {
            Log.i("industryId1111", industryId);
            formed.add("industry", industryId);
        }

        if (work_yearId != null) {
            formed.add("work_year", work_yearId);
        }

        if (updatetimeId != null) {
            formed.add("updatetime", updatetimeId);
        }

        if (jobCategoryId != null) {
            formed.add("job_category", jobCategoryId);
        }

        if (locationId != null) {
            formed.add("location", locationId);
        }
        if (comkindId != null) {
            formed.add("employee_num", comkindId);
            Log.i("employee_num", comkindId);
        }
        if (employee_numId != null) {
            formed.add("comkind", employee_numId);
            Log.i("comkind", employee_numId);
        }

        formed.add("gender", "");
        formed.add("education", "");
        formed.add("age", "");

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.COMPANY_JOB_SEARCH)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formed.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("选择了各种数据之后的搜索", str);
                handler.sendEmptyMessage(130);
            }
        });
    }

    /**
     * 刷新获取新数据
     */
    private void gainRefreshNewData() {
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
                Log.i("下拉刷新获取更多的数据", str);
                handler.sendEmptyMessage(220);
            }
        });
    }

    /**
     * 解析下拉刷新获取更多的数据
     */
    private void parsGainRefreshNewData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codes = json.getString("code");
            if ("1".equals(codes)) {
                JSONArray jsonArray = json.getJSONArray("data");
                listCompany.clear();
                for (int i = 0; i < jsonArray.size(); i++) {
                    CompanyJobBean companyJobBean = new CompanyJobBean();
                    JSONObject jsondata = jsonArray.getJSONObject(i);
                    companyJobBean.setId(jsondata.getString("id"));
                    companyJobBean.setJob_title(jsondata.getString("job_title"));
                    companyJobBean.setUpdatetime(jsondata.getString("updatetime"));
                    companyJobBean.setPart_status(jsondata.getString("part_status"));
                    companyJobBean.setLocation_name(jsondata.getString("location_name"));
                    companyJobBean.setSalary(jsondata.getString("salary"));
                    companyJobBean.setEducation_name(jsondata.getString("education_name"));
                    companyJobBean.setWork_year_name(jsondata.getString("work_year_name"));
                    companyJobBean.setCompany_name(jsondata.getString("company_name"));
                    companyJobBean.setIs_urgent(jsondata.getString("is_urgent"));
                    listCompany.add(companyJobBean);
                }
                adapter.notifyDataSetChanged();
                xrefreshview.stopRefresh();
                xrefreshview.setLoadComplete(false);
            } else {
                //Todo
            }
        } else {
            //TODO
        }
    }

    /**
     * 上拉加载更多数据
     */
    private void loadMoreNewData() {
        if (next_page_url == null) {
            xrefreshview.setLoadComplete(true);
        } else {
            gainLoadMoreNewData();//上拉加载获取数据
        }
    }

    private void gainLoadMoreNewData() {
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formed = new FormEncodingBuilder();
        if (keyword != null) {
            formed.add("keyword", keyword);
        }

        if (keyword_type != null) {
            formed.add("keyword_type", keyword_type);
        }

        if (salary_above != null) {
            formed.add("salary_above", salary_above);
        }

        if (salary_below != null) {
            formed.add("salary_below", salary_below);
        }

        if (industryId != null) {
            formed.add("industry", industryId);
        }

        if (work_yearId != null) {
            formed.add("work_year", work_yearId);
        }

        if (updatetimeId != null) {
            formed.add("updatetime", updatetimeId);
        }

        if (jobCategoryId != null) {
            formed.add("job_category", jobCategoryId);
        }

        if (locationId != null) {
            Log.i("locationId", locationId);
            formed.add("location", locationId);
        }
        if (employee_numId != null) {
            formed.add("employee_num", employee_numId);
        }
        if (comkindId != null) {
            formed.add("comkind", comkindId);
        }

        formed.add("gender", "");
        formed.add("education", "");
        formed.add("age", "");
        Request request = new Request.Builder()
                .url(next_page_url)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formed.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("上拉加载更多数据", str);
                handler.sendEmptyMessage(200);
            }
        });
    }

    /**
     * 解析上拉加载更多的数据
     */
    private void parsLoadMoreData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            String codes = json.getString("code");
            if ("1".equals(codes)) {
                JSONArray jsonArray = json.getJSONArray("data");
                for (int i = 0; i < jsonArray.size(); i++) {
                    CompanyJobBean companyJobBean = new CompanyJobBean();
                    JSONObject jsondata = jsonArray.getJSONObject(i);
                    companyJobBean.setId(jsondata.getString("id"));
                    companyJobBean.setJob_title(jsondata.getString("job_title"));
                    companyJobBean.setUpdatetime(jsondata.getString("updatetime"));
                    companyJobBean.setPart_status(jsondata.getString("part_status"));
                    companyJobBean.setLocation_name(jsondata.getString("location_name"));
                    companyJobBean.setSalary(jsondata.getString("salary"));
                    companyJobBean.setEducation_name(jsondata.getString("education_name"));
                    companyJobBean.setWork_year_name(jsondata.getString("work_year_name"));
                    companyJobBean.setCompany_name(jsondata.getString("company_name"));
                    companyJobBean.setIs_urgent(jsondata.getString("is_urgent"));
                    companyJobBean.setLogo(jsondata.getString("logo"));
                    listCompany.add(companyJobBean);
                }
                xrefreshview.stopLoadMore();
                adapter.notifyDataSetChanged();

            } else {
                //TODO
            }
        } else {
            //TODO
        }
    }

    /**
     * 开启一个界面返回的数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    String jobCategory = "";
    String jobCategoryId = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            switch (requestCode) {
                case 50:
                    job_category = (List<String>) data.getExtras().getSerializable("listItem");
                    job_categoryId = (List<String>) data.getExtras().getSerializable("cid");
                    jobCategory = "";
                    jobCategoryId = "";
                    for (int i = 0; i < job_category.size(); i++) {
                        if (job_category.size() == 0 || i == job_category.size() - 1) {
                            jobCategory += job_category.get(i);
                            jobCategoryId += job_categoryId.get(i);
                        } else {
                            jobCategory += job_category.get(i) + ",";
                            jobCategoryId += job_categoryId.get(i) + ",";
                        }
                    }

                    if (jobCategory != null && !"".equals(jobCategory)) {
                        textcatgory.setText(jobCategory);
                    } else {
                        textcatgory.setText("职位类别");
                    }

                    Log.i("job", jobCategory);
                    selectRefreshNewData();//选择数据后的刷新
                    break;
                case 60:

                    industryId = data.getExtras().getString("industryId");
                    industry = data.getExtras().getString("industry");
                    work_yearId = data.getExtras().getString("sufferId");
                    work_year = data.getExtras().getString("suffer");
                    employee_numId = data.getExtras().getString("natrueBusinessId");
                    employee_num = data.getExtras().getString("natrueBusiness");
                    comkindId = data.getExtras().getString("companyScaleId");
                    comkind = data.getExtras().getString("companyScale");
                    updatetimeId = data.getExtras().getString("issuedataId");
                    updatetime = data.getExtras().getString("issuedata");

                    selectRefreshNewData();//选择数据后的刷新

                    break;
            }
        }
    }

}
