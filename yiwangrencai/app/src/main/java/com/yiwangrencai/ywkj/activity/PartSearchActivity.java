package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.yiwangrencai.wxapi.WXEntryActivity;
import com.yiwangrencai.ywkj.adapter.MyPartAdapter;
import com.yiwangrencai.ywkj.adapter.MyPartJobAdapter;
import com.yiwangrencai.ywkj.adapter.MyPartSearchAdapter;
import com.yiwangrencai.ywkj.adapter.MySearchAreaAdapter;
import com.yiwangrencai.ywkj.adapter.MySearchCityAdapter;
import com.yiwangrencai.ywkj.bean.AreaCity;
import com.yiwangrencai.ywkj.bean.AreaCounty;
import com.yiwangrencai.ywkj.bean.AreaLocationBean;
import com.yiwangrencai.ywkj.bean.AreaLocationBeanss;
import com.yiwangrencai.ywkj.bean.AreaOne;
import com.yiwangrencai.ywkj.bean.SearchPartJobBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.InitDatas;
import com.yiwangrencai.ywkj.tools.ViewUtilss;
import com.yyydjk.library.DropDownMenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/30.
 */
public class PartSearchActivity extends AppCompatActivity {
    @Bind(R.id.search_back)
    FrameLayout searchBack;
    @Bind(R.id.edittext_key)
    EditText edittextKey;
    @Bind(R.id.textsearch)
    TextView textsearch;
    @Bind(R.id.dropDownMenu)
    DropDownMenu dropDownMenu;
    private String headers[] = {"兼职类型", "地区", "结算方式", "更多"};
    private List<String> listSalary = new ArrayList<>();
    private List<String> listSalaryId = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_search);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private List<String> partJob = new ArrayList<>();
    private List<String> partJobId = new ArrayList<>();
    private String api_token;
    public void initData() {

        gainLocationAreaFile();
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");
        listSalary.add("不限");
        listSalary.add("日结");
        listSalary.add("周结");
        listSalary.add("月结");
        listSalary.add("完工结算");

        listSalaryId.add("0");
        listSalaryId.add("1");
        listSalaryId.add("2");
        listSalaryId.add("3");
        listSalaryId.add("4");
        partJob = InitDatas.partJobDatas();
        partJobId = InitDatas.partJobDatasId();

    }

    private XRefreshView xRefreshview;
    private RecyclerView part_recyclerView;
    private MyPartSearchAdapter adapter;
    private FrameLayout frameLayout;
    private View loadingView;
    private View successView;
    private View emptyView;
    private List<SearchPartJobBean.DataBean> searchPartJobBean = new ArrayList();

    public void initView() {

        dropDownMenu = (DropDownMenu) findViewById(R.id.dropDownMenu);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        loadingView = createLoadingView();//正在加载的界面
        successView = createSuccessView();//加载数据成功的界面
        emptyView = createEmptyView();//加载数据成功的界面
        frameLayout.addView(loadingView);

        initDropView();//初始化dropView
        gainSearchPartJob();//获取搜索兼职的数据

    }

    /**
     * 创建加载成功的界面
     *
     * @return
     */
    private View createSuccessView() {
        View view = View.inflate(PartSearchActivity.this, R.layout.search_part_job, null);
        xRefreshview = (XRefreshView) view.findViewById(R.id.xRefreshview);
        part_recyclerView = (RecyclerView) view.findViewById(R.id.part_recyclerView);

        adapter = new MyPartSearchAdapter(searchPartJobBean);
        part_recyclerView.setAdapter(adapter);
        part_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRefreshview.setAutoLoadMore(false);
        xRefreshview.setPinnedTime(1000);
        xRefreshview.setMoveForHorizontal(true);
        xRefreshview.setPullLoadEnable(true);

        adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));

        xRefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);

                if (current_page != null && current_page.equals("1")) {
                    refreshDataCurrentPage();
                } else {
                    if (prev_page_url == null || "".equals(prev_page_url)) {
                        xRefreshview.stopRefresh();
                    } else {
                        refreshDataFromService();//下拉刷新数据
                    }
                }
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                if (next_page_url == null || "".equals(next_page_url)) {
                    xRefreshview.setLoadComplete(true);
                    xRefreshview.setPullLoadEnable(false);
                    xRefreshview.stopRefresh();
                } else {
                    loadMoreData();//上垃加载更多数据
                }
            }
        });

        adapter.setOnItemClickListener(new MyPartSearchAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, SearchPartJobBean.DataBean data) {
                Intent intent=new Intent(PartSearchActivity.this,PartDetailActivity.class);
                intent.putExtra("id",data.getId());
                startActivity(intent);
            }
        });

        return view;
    }

    /**
     * 上垃加载更多数据
     */
    private void loadMoreData() {

        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        if (free_time == null || "".equals(free_time)) {
            form.add("free_time", "");
        } else {
            form.add("free_time", free_time);
        }

        if (type_id == null || "".equals(type_id)) {
            form.add("type_id", "");
        } else {
            form.add("type_id", type_id);
        }

        if (salary_method == null || "".equals(salary_method)) {
            form.add("salary_method", "");
        } else {
            form.add("salary_method", salary_method);
        }

        if (city_id == null || "".equals(city_id)) {
            form.add("city_id", "");
        } else {
            form.add("city_id", city_id);
        }

        if (keyword == null || "".equals(keyword)) {
            form.add("keyword", "");
        } else {
            form.add("keyword", keyword);
        }

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
                Log.i("当前页不是第一页时的下拉刷新的数据", str);
                handler.sendEmptyMessage(GAIN_PART_ONE_DATA);
            }
        });

    }

    /**
     * 当前页面是第一页的时候的下拉刷新
     */
    private void refreshDataCurrentPage() {
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();

        if (free_time == null || "".equals(free_time)) {
            form.add("free_time", "");
        } else {
            form.add("free_time", free_time);
        }

        if (type_id == null || "".equals(type_id)) {
            form.add("type_id", "");
        } else {
            form.add("type_id", type_id);
        }

        if (salary_method == null || "".equals(salary_method)) {
            form.add("salary_method", "");
        } else {
            form.add("salary_method", salary_method);
        }

        if (city_id == null || "".equals(city_id)) {
            form.add("city_id", "");
        } else {
            form.add("city_id", city_id);
        }

        if (keyword == null || "".equals(keyword)) {
            form.add("keyword", "");
        } else {
            form.add("keyword", keyword);
        }

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.PART_JOB_SEARCH)
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
                code = response.code();
                str = response.body().string();
                Log.i("当前页是第一页时的下拉刷新的数据", str);
                handler.sendEmptyMessage(GAIN_PART_ONE_DATA);
            }
        });

    }

    /**
     * 下拉刷新数据
     */
    private void refreshDataFromService() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        if (free_time == null || "".equals(free_time)) {
            form.add("free_time", "");
        } else {
            form.add("free_time", free_time);
        }

        if (type_id == null || "".equals(type_id)) {
            form.add("type_id", "");
        } else {
            form.add("type_id", type_id);
        }

        if (salary_method == null || "".equals(salary_method)) {
            form.add("salary_method", "");
        } else {
            form.add("salary_method", salary_method);
        }

        if (city_id == null || "".equals(city_id)) {
            form.add("city_id", "");
        } else {
            form.add("city_id", city_id);
        }

        if (keyword == null || "".equals(keyword)) {
            form.add("keyword", "");
        } else {
            form.add("keyword", keyword);
        }

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
                Log.i("当前页不是第一页时的下拉刷新的数据", str);
                handler.sendEmptyMessage(GAIN_PART_ONE_DATA);
            }
        });
    }

    /**
     * 创建正在加载的view
     *
     * @return
     */
    private View createLoadingView() {
        View view = View.inflate(PartSearchActivity.this, R.layout.loading_view, null);
        return view;
    }

    /**
     * 获取数据为空的界面
     */
    private View createEmptyView(){
        View view=View.inflate(PartSearchActivity.this, R.layout.empty_view, null);
        TextView textview_click= (TextView) view.findViewById(R.id.textview_click);
        textview_click.setText("还没有兼职简历");
        return view;
    }

    /**
     * 获取搜索兼职的数据
     */
    private String str;
    private int code;
    private String free_time;
    private String type_id;
    private String salary_method;
    private String city_id;
    private String keyword;

    private void gainSearchPartJob() {
        frameLayout.removeAllViews();
        frameLayout.addView(loadingView);
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        if (free_time == null || "".equals(free_time)) {
            form.add("free_time", "");
        } else {
            form.add("free_time", free_time);
        }

        if (type_id == null || "".equals(type_id)) {
            form.add("type_id", "");
        } else {
            form.add("type_id", type_id);
        }

        if (salary_method == null || "".equals(salary_method)) {
            form.add("salary_method", "");
        } else {
            form.add("salary_method", salary_method);
        }

        if (city_id == null || "".equals(city_id)) {
            form.add("city_id", "");
        } else {
            form.add("city_id", city_id);
        }

        if (keyword == null || "".equals(keyword)) {
            form.add("keyword", "");
        } else {
            form.add("keyword", keyword);
        }

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.PART_JOB_SEARCH)
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
                Log.i("搜索兼职的数据", str);
                handler.sendEmptyMessage(GAIN_PART_JOB_SUCCESS);
            }
        });

    }

    private final static int GAIN_PART_JOB_SUCCESS = 0;
    private final static int GAIN_PART_ONE_DATA = 1;
    private final static int GAIN_PART_MORE_DATA = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GAIN_PART_JOB_SUCCESS:
                    parsGainServiceData();//解析从服务器获取的数据
                    break;
                case GAIN_PART_ONE_DATA:
                    parsGainOnePageData();//解析当前页是第一页时的下拉刷新获取的数据
                    break;
                case GAIN_PART_MORE_DATA://解析上垃加载更多的数据
                    parsGainMoreData();
                    break;
            }
        }
    };

    /**
     * 解析上垃加载更多的数据
     */
    private void parsGainMoreData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            String msg = json.getString("msg");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            if ("1".equals(codes)) {
                Gson gson = new Gson();
                SearchPartJobBean partJobBean = gson.fromJson(str, SearchPartJobBean.class);
                if (next_page_url == null || "".equals(next_page_url)) {
                    xRefreshview.setLoadComplete(true);
                    xRefreshview.setPullLoadEnable(false);
                }
                adapter.setData(partJobBean.getData(), true);
                xRefreshview.stopRefresh();

            } else {

            }
            if (msg != null && !"".equals(msg)) {
                Toast.makeText(PartSearchActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

        } else {

        }
    }

    /**
     * 解析当前页是第一页时的下拉刷新获取的数据
     */
    private void parsGainOnePageData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            String msg = json.getString("msg");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            if ("1".equals(codes)) {
                Gson gson = new Gson();
                SearchPartJobBean partJobBean = gson.fromJson(str, SearchPartJobBean.class);
                if (next_page_url == null || "".equals(next_page_url)) {
                    xRefreshview.setLoadComplete(true);
                    xRefreshview.setPullLoadEnable(false);
                }
                adapter.setData(partJobBean.getData(), true);
                xRefreshview.setLoadComplete(true);
                xRefreshview.stopRefresh();
            } else {
                xRefreshview.setLoadComplete(true);
                xRefreshview.stopRefresh();
            }

            if (msg != null && !"".equals(msg)) {
                Toast.makeText(PartSearchActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

        } else {
            xRefreshview.setLoadComplete(true);
            xRefreshview.stopRefresh();
        }
    }

    /**
     * 解析从服务器获取的数据
     */
    String current_page;
    String next_page_url;
    String prev_page_url;
    private void parsGainServiceData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            String msg = json.getString("msg");
            current_page = json.getString("current_page");
            next_page_url = json.getString("next_page_url");
            prev_page_url = json.getString("prev_page_url");
            frameLayout.removeView(loadingView);
            frameLayout.removeAllViews();

            if ("1".equals(codes)) {
                Gson gson = new Gson();
                SearchPartJobBean partJobBean = gson.fromJson(str, SearchPartJobBean.class);

                if (partJobBean.getData().size()==0){
                    frameLayout.addView(emptyView);
                }else{
                    frameLayout.addView(successView);
                    if (next_page_url == null || "".equals(next_page_url)) {
                        xRefreshview.setLoadComplete(true);
                        xRefreshview.setPullLoadEnable(false);
                    }
                    adapter.setData(partJobBean.getData(), false);
                }

            } else if ("-1".equals(codes)) {
                Intent intent = new Intent(PartSearchActivity.this, WXEntryActivity.class);
                startActivity(intent);
            }

            if (msg != null && !"".equals(msg)) {
                Toast.makeText(PartSearchActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(PartSearchActivity.this, "联网超时", Toast.LENGTH_SHORT).show();
        }
    }

    String localAreas = "";
    String localArea = "";
    AreaLocationBeanss areaLocationBeansLocation;
    List<AreaLocationBeanss.DataBean.NextsBean> listTaiZ=new ArrayList();
    List<String> listArea=new ArrayList<>();
    private void gainLocationAreaFile() {
        localAreas = new ViewUtilss().getJson(PartSearchActivity.this, "location_erea.json");
        //读取本地数据
        localArea = new ViewUtilss().getJson(PartSearchActivity.this, "area.json");
        Gson gson=new Gson();
        AreaLocationBean areaLocationBean= gson.fromJson(localAreas,AreaLocationBean.class);
        areaLocationBeansLocation=gson.fromJson(localArea, AreaLocationBeanss.class);
        for (int i=0;i<areaLocationBean.getData().getNexts().size();i++){
            AreaLocationBeanss.DataBean.NextsBean nextsBean=new AreaLocationBeanss.DataBean.NextsBean();
            nextsBean.setCid(areaLocationBean.getData().getNexts().get(i).getCid());
            nextsBean.setName(areaLocationBean.getData().getNexts().get(i).getName());
            nextsBean.setGrade(areaLocationBean.getData().getNexts().get(i).getGrade());
            listTaiZ.add(nextsBean);
        }

        listArea.add("不限");
        listArea.add(areaLocationBean.getData().getName());
        for (int i=0;i<areaLocationBeansLocation.getData().size();i++){
            listArea.add(areaLocationBeansLocation.getData().get(i).getName());
        }
    }

    /**
     * 初始化dropView
     */
    String chooseArray = "";
    TextView textSelectArea;

    private Boolean flagg;
    private List<View> popupViews = new ArrayList<>();
    String location;
    String locationId;
     TextView textarea;
    boolean isTai;
    int positions;
    private void initDropView() {

        final ListView partType = new ListView(this);
        partType.setBackgroundColor(0xffffffff);
        final MyPartJobAdapter partAdapter = new MyPartJobAdapter(partJob);
        partType.setAdapter(partAdapter);

        final View areaView = getLayoutInflater().inflate(R.layout.popuwin_area, null);

        ListView listview_left = (ListView) areaView.findViewById(R.id.listview_left);
        final ListView listview_right = (ListView) areaView.findViewById(R.id.listview_right);

        MySearchAreaAdapter adapter = new MySearchAreaAdapter(listArea);
        listview_left.setAdapter(adapter);

        listview_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if (position==0){
                    city_id="";
                    dropDownMenu.closeMenu();
                    gainSearchPartJob();
                }else if (position==1){
                    listview_right.setAdapter(new MySearchCityAdapter(listTaiZ));
                    isTai=true;
                }else{
                    if (areaLocationBeansLocation.getData().get(position-2).getNext()==1){
                        listview_right.setAdapter(new MySearchCityAdapter(areaLocationBeansLocation.getData().get(position-2).getNexts()));
                    }
                    positions=position-2;
                    isTai=false;
                }
            }
        });

        listview_right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isTai){
                    dropDownMenu.setTabText(listTaiZ.get(position).getName());
                    city_id=listTaiZ.get(position).getCid()+"";
                }else{
                    dropDownMenu.setTabText(areaLocationBeansLocation.getData().get(positions).getNexts().get(position).getName());
                    city_id=areaLocationBeansLocation.getData().get(positions).getNexts().get(position).getCid()+"";
                }

                dropDownMenu.closeMenu();
                gainSearchPartJob();
            }
        });

        final ListView listSalay = new ListView(this);
        listSalay.setBackgroundColor(0xffffffff);
        listSalay.setAdapter(new MyPartAdapter(listSalary));

        final View moreView = getLayoutInflater().inflate(R.layout.time_table, null);
        RelativeLayout relativeWorkArea = (RelativeLayout) moreView.findViewById(R.id.relativeWorkArea);
        TextView text_sure_free = (TextView) moreView.findViewById(R.id.text_sure_free);
        textSelectArea = (TextView) moreView.findViewById(R.id.textSelectArea);

        relativeWorkArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PartSearchActivity.this, ChoosePtTimeActivity.class);
                intent.putExtra("free", chooseArray);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 88);
            }
        });

        text_sure_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownMenu.closeMenu();
                gainSearchPartJob();
            }
        });

        //兼职类型
        partType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dropDownMenu.setTabText(partJob.get(position));
                type_id = partJobId.get(position);
                dropDownMenu.closeMenu();
                gainSearchPartJob();
            }
        });

        //兼职薪资结算方式
        listSalay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dropDownMenu.setTabText(listSalary.get(position));
                salary_method = listSalaryId.get(position);
                dropDownMenu.closeMenu();
                gainSearchPartJob();
            }
        });

        popupViews.add(partType);
        popupViews.add(areaView);
        popupViews.add(listSalay);
        popupViews.add(moreView);

        TextView contentView = new TextView(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 0);

        dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);

        if ("".equals(localAreas)&&"".equals(localArea)){
            gainLocationAreaFile();
        }
    }

    /**
     * 控件的点击事件
     * @param view
     */
    @OnClick({R.id.search_back, R.id.textsearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_back://返回
                onBackPressed();
                finish();
                break;
            case R.id.textsearch://点击搜索
                keyword=edittextKey.getText().toString().trim();
                gainSearchPartJob();
                break;
        }
    }

    /**
     * 开启界面返回数据并修改界面
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {

        } else {
            switch (requestCode) {
                case 88:
                    chooseArray = data.getExtras().getString("free");
                    free_time = chooseArray;
                    if (chooseArray == null) {

                    } else {
                        textSelectArea.setText("已填");
                    }

                    break;
            }
        }
    }

    /**
     * 解析地域的城市
     */
    String strarea;
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
                handler.sendEmptyMessage(1000);
            }
        });
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
        Log.i("textareatextarea",areaname);
        textarea.setText(areaname);
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

    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (dropDownMenu.isShowing()) {
            dropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }
}
