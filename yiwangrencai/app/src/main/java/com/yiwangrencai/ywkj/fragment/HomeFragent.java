package com.yiwangrencai.ywkj.fragment;


import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.hss01248.dialog.StyledDialog;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yiwangrencai.R;
import com.yiwangrencai.wxapi.WXEntryActivity;
import com.yiwangrencai.ywkj.activity.CompanydetailActivity;
import com.yiwangrencai.ywkj.activity.CompanydetailActivityTwo;
import com.yiwangrencai.ywkj.activity.EditResumeActivity;
import com.yiwangrencai.ywkj.activity.FindActivity;
import com.yiwangrencai.ywkj.activity.HighSearchActivity;
import com.yiwangrencai.ywkj.activity.JobInfoActivity;
import com.yiwangrencai.ywkj.activity.MainActivity;
import com.yiwangrencai.ywkj.activity.MoreJobRecomActivity;
import com.yiwangrencai.ywkj.activity.NearByActivity;
import com.yiwangrencai.ywkj.activity.PartSearchActivity;
import com.yiwangrencai.ywkj.activity.SearchActivity;
import com.yiwangrencai.ywkj.activity.WebViewActivity;
import com.yiwangrencai.ywkj.activity.WebViewActivity2;
import com.yiwangrencai.ywkj.adapter.MyPagerAdapter;
import com.yiwangrencai.ywkj.adapter.MyRecyclerAdapter;

import com.yiwangrencai.ywkj.bean.HomeDataBean;
import com.yiwangrencai.ywkj.bean.HomePager;
import com.yiwangrencai.ywkj.bean.HomeUrl;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.DropZoomScrollView;
import com.yiwangrencai.ywkj.view.MyPopuwindown;
import com.yiwangrencai.ywkj.view.MySexPopuwin;
import com.yiwangrencai.ywkj.view.RecyclerViewHeader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.yiwangrencai.ywkj.view.SimpleDividerItemDecoration;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

import static com.tencent.open.utils.Global.getPackageName;

/**
 * Created by Administrator on 2017/4/1.
 */
@SuppressLint("ValidFragment")
public class HomeFragent extends BaseFragment implements View.OnClickListener {

    private HomeDataBean homeDataBean;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    //把数据保存到本地
                    if (code == 200) {
                        Gson gson=new Gson();
                        homeDataBean= gson.fromJson(string,HomeDataBean.class);
                        parseJson();
                    }
                    break;
                case 2000:
                    StyledDialog.dismissLoading();
                    Toast.makeText(context, "联网失败,请重试!", Toast.LENGTH_SHORT).show();
                    break;
                case 100:
                    if (code == 200) {
                        JSONObject jsonss = JSON.parseObject(str);
                        String codess = jsonss.getString("code");
                        String message = jsonss.getString("msg");
                        if (codess.equals("1")) {
                            SharedPreferences shared = context.getSharedPreferences("data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = shared.edit();
                            editor.putString("resume_mobile", resume_mobile);
                            editor.putString("job_status", job_status);
                            editor.commit();

                            Log.i("resume_mobile", resume_mobile);

                            StyledDialog.dismissLoading();

                        } else if ("-1".equals(codess)) {
                            StyledDialog.dismissLoading();
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } else {
                        StyledDialog.dismissLoading();
                        Toast.makeText(context, "简历刷新失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 120://是否刷新简历
                    if (code == 200) {
                        JSONObject jsonObject = JSON.parseObject(string);
                        String codes = jsonObject.getString("code");
                        if ("1".equals(codes)) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String need = jsonObject1.getString("need");
                            if ("0".equals(need)) {
                                //不需要刷新
                            } else if ("1".equals(need)) {//需要刷新
                                showRefreashResumes();
                            }
                        }
                    }
                    break;
                case 8888:
                    if (code == 200) {
                        JSONObject jsonObject = JSON.parseObject(str);
                        String codes = jsonObject.getString("code");
                        if (codes.equals("1")) {
                            //Todo
                            String jsonData = jsonObject.getString("data");
                            JSONObject jsonObject1 = JSON.parseObject(jsonData);
                            String api_token = jsonObject1.getString("api_token");
                            String resume_id = jsonObject1.getString("resume_id");
                            String uid = jsonObject1.getString("uid");
                            String username = jsonObject1.getString("username");
                            String mobile = jsonObject1.getString("mobile");
                            String degree = jsonObject1.getString("degree");
                            String chkphoto_open = jsonObject1.getString("chkphoto_open");
                            String resume_status = jsonObject1.getString("resume_status");
                            String name = jsonObject1.getString("name");
                            String resume_mobile = jsonObject1.getString("resume_mobile");
                            String job_status = jsonObject1.getString("job_status");
                            String avatar = jsonObject1.getString("avatar");
                            String part_resume_id = jsonObject1.getString("part_resume_id");
                            String sex = jsonObject1.getString("sex");
                            String chat_pwd = jsonObject1.getString("chat_pwd");

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Activity", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editer = sharedPreferences.edit();
                            editer.putString("api_token", api_token);
                            editer.commit();
                            SharedPreferences share = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editors = share.edit();
                            editors.putString("id", resume_id);
                            editors.putString("username", username);
                            editors.putString("resume_id", resume_id);
                            editors.putString("uid", uid);
                            editors.putString("mobile", mobile);
                            editors.putString("degree1", degree);
                            editors.putString("chkphoto_open", chkphoto_open);
                            editors.putString("resume_status", resume_status);
                            editors.putString("name", name);
                            editors.putString("resume_mobile", resume_mobile);
                            editors.putString("job_status", job_status);
                            editors.putString("avatar", avatar);
                            editors.putString("part_resume_id", part_resume_id);
                            editors.putString("sex", sex);
                            editors.putString("chat_pwd", chat_pwd);
                            editors.commit();
                        }
                    }
                    break;
            }
        }
    };

    private String read() {
        if (api_token == null || "".equals(api_token)) {
            File path = context.getExternalCacheDir();
            File file = new File(path, "home_0");
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bw = new BufferedReader(fileReader);
                StringWriter sw = new StringWriter();
                String str = null;
                while ((str = bw.readLine()) != null) {
                    sw.write(str);
                }

                sw.flush();
                sw.close();

                return sw.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            File path = context.getExternalCacheDir();
            File file = new File(path, "home_1");
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bw = new BufferedReader(fileReader);
                StringWriter sw = new StringWriter();
                String str = null;
                while ((str = bw.readLine()) != null) {
                    sw.write(str);
                }

                sw.flush();
                sw.close();

                return sw.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private Context context;
    private RelativeLayout homeSearch;
    private RecyclerView recycler_view;
    private String string;
    private List<HomeDataBean.DataBean.JobBean> homeBeanList = new ArrayList<HomeDataBean.DataBean.JobBean>();
    private List<HomeUrl> homeUrlList = new ArrayList<HomeUrl>();
    private ProgressDialog progressDialog;
    private ViewPager viewPager;
    private LinearLayout refreshResume;
    private LinearLayout findWork;
    private LinearLayout highSearch;
    private LinearLayout locationWork;
    private LinearLayout ll_part_time;
    private LinearLayout ll_find;
    private LinearLayout titlessss;
    private RecyclerViewHeader header;
    private JSONArray imageArray;
    private JSONArray url;
    private String api_token;

    public HomeFragent(Context context) {
        this.context = context;
    }

    @Override
    public void initData() {
        StyledDialog.buildLoading().show();
        //从本地获取数据
        if (api_token == null || "".equals(api_token)) {
            //联网从服务器获取数据
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder().build();
            Request request = new Request.Builder()
                    .url(ContentUrl.BASE_URL + ContentUrl.HOME_URL)
                    .addHeader("Accept", "application/json")
                    .post(requestBody)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    code = response.code();
                    string = response.body().string();
                    Log.i("qqqqqqqqqqapi_token=null", string);
                    handler.sendEmptyMessage(1000);
                }
            });
        } else if (api_token != null && !"".equals(api_token)) {
            //联网从服务器获取数据
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder().build();
            Request request = new Request.Builder()
                    .url(ContentUrl.BASE_URL + ContentUrl.HOME_URL)
                    .addHeader("Accept", "application/json")
                    .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                    .post(requestBody).build();
            okHttpClient.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {

                    if (e.getMessage().toString() != null) {
                        StyledDialog.dismissLoading();
                    }
                    handler.sendEmptyMessage(2000);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    code = response.code();
                    string = response.body().string();
                    Log.i("api_token!=null----", string);
                    handler.sendEmptyMessage(1000);
                }
            });
        }
    }

    /**
     * 显示正在加载中
     */
    private void showDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("请稍后，正在加载中.....");
        progressDialog.show();
    }

    /**
     * 保存数据到本地
     */
    public void saveDataToCache() {
        if (api_token == null || "".equals(api_token)) {
            FileWriter writer = null;
            System.out.println("获取文件的路径" + context.getExternalCacheDir());
            String path = context.getExternalCacheDir().toString();
            File file = new File(path, "home_0");
            try {
                writer = new FileWriter(file);
                writer.write(string);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            FileWriter writer = null;
            System.out.println("获取文件的路径" + context.getExternalCacheDir());
            String path = context.getExternalCacheDir().toString();
            File file = new File(path, "home_1");
            try {
                writer = new FileWriter(file);
                writer.write(string);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 解析数据
     */
    private void parseJson() {

        System.out.println("请求的轮播图的图片" + string);
        JSONObject json = JSON.parseObject(string);
        final String codes = json.getString("code");

        if ("1".equals(codes)) {
            //saveDataToCache();//保存数据到本地
              Log.i("==========","<------------->");
            SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            JSONObject jsonData = json.getJSONObject("data");
            String badge = jsonData.getString("badge");
            Log.i("badge", badge + "");


            if (badge != null && !"0".equals(badge) && !"".equals(badge)) {

                int badges = Integer.parseInt(badge);
                editor.putInt("badges", homeDataBean.getData().getBadge());
                editor.commit();

                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity == null) {

                } else {
                    mainActivity.gain(badges);
                }
            } else {

                editor.putInt("badges", 0);
                editor.commit();

                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity == null) {

                } else {
                    mainActivity.gain(0);
                }
            }

            myAdapter.addData(homeDataBean.getData().getJob());
            myAdapter.setOnItemClickListener(new MyRecyclerAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, HomeDataBean.DataBean.JobBean data) {
                    if (data == null) {
                        if (api_token == null || "".equals(api_token)) {
                            Intent intent = new Intent(context, WXEntryActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intentMore = new Intent(context, MoreJobRecomActivity.class);
                            startActivity(intentMore);
                        }

                    } else {
                        Intent intent = new Intent(context, JobInfoActivity.class);
                        intent.putExtra("id", data.getId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });


            rollPagerView.setHintView(new ColorPointHintView(context, getResources().getColor(R.color.foot_text_color_green), getResources().getColor(R.color.color_text)));
            rollPagerView.setAdapter(new MyPagerAdapter(context, homeDataBean.getData().getImg()));

            StyledDialog.dismissLoading();
        } else {
            StyledDialog.dismissLoading();
        }
    }

    MyRecyclerAdapter myAdapter;
    RollPagerView rollPagerView;
    Typeface typeface;
    DropZoomScrollView scrollView;
    private RelativeLayout scan_code;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected View initView() {
//        Window window = getActivity().getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        //设置状态栏颜色
//        window.setStatusBarColor(0xff3f51b5);
        SharedPreferences shares = context.getSharedPreferences("Activity", Context.MODE_PRIVATE);
        api_token = shares.getString("api_token", "");
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "iconfont.ttf");
        SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        name = share.getString("name", "");
        resume_id = share.getString("id", "");
        resume_mobile = share.getString("resume_mobile", "");
        job_status = share.getString("job_status", "");
        uid=share.getString("uid","");

        showResumeStatue();//显示简历的求职状态

        View view = View.inflate(context, R.layout.scroll_layout, null);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int windownHeight = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.line_divider);

        recycler_view.addItemDecoration(new SimpleDividerItemDecoration(context, drawable, 20));
        homeSearch = (RelativeLayout) view.findViewById(R.id.et_home_fragment_search);

        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        refreshResume = (LinearLayout) view.findViewById(R.id.ll_refresh_resume);
        findWork = (LinearLayout) view.findViewById(R.id.ll_find_work);
        highSearch = (LinearLayout) view.findViewById(R.id.ll_high_search);
        locationWork = (LinearLayout) view.findViewById(R.id.ll_location_work);
        ll_part_time = (LinearLayout) view.findViewById(R.id.ll_part_time);
        ll_find = (LinearLayout) view.findViewById(R.id.ll_find);
        titlessss = (LinearLayout) view.findViewById(R.id.titlessss);
        scrollView = (DropZoomScrollView) view.findViewById(R.id.scrollView);
        scan_code = (RelativeLayout) view.findViewById(R.id.scan_code);
        rollPagerView = (RollPagerView) view.findViewById(R.id.rollPagerView);
        //设置点击的监听
        refreshResume.setOnClickListener(this);
        findWork.setOnClickListener(this);
        highSearch.setOnClickListener(this);
        locationWork.setOnClickListener(this);
        homeSearch.setOnClickListener(this);
        ll_part_time.setOnClickListener(this);
        ll_find.setOnClickListener(this);
        scan_code.setOnClickListener(this);

        ViewGroup.LayoutParams params = rollPagerView.getLayoutParams();
        params.height = (int) (width / 2.34);
        rollPagerView.setLayoutParams(params);

        rollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (homeDataBean.getData().getUrl().size() == 0) {

                } else {
                    String url_type=homeDataBean.getData().getUrl().get(position).getUrl_type()+"";
                    if ("1".equals(url_type)) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("id", homeDataBean.getData().getUrl().get(position).getUrl());
                        startActivity(intent);
                    } else if ("2".equals(url_type)) {
                        Intent intentjob = new Intent(context, CompanydetailActivity.class);
                        intentjob.putExtra("id",  homeDataBean.getData().getUrl().get(position).getEid());
                        startActivity(intentjob);
                    } else if ("3".equals(url_type)) {
                        Intent intentCom = new Intent(context, JobInfoActivity.class);
                        intentCom.putExtra("id",  homeDataBean.getData().getUrl().get(position).getEid());
                        startActivity(intentCom);
                    }
                }
            }
        });

        myAdapter = new MyRecyclerAdapter(context, homeBeanList);
        recycler_view.setAdapter(myAdapter);

        recycler_view.setFocusable(false);
        scrollView.setOnScrollListener(new DropZoomScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY >= 200) {
                    titlessss.setBackgroundColor(0xff00c9c6);
                } else {
                    titlessss.setBackgroundColor(0x00ffffff);
                }
            }

        });
        //requestAlertWindowPermission();
        autoShowPopuwind();

        return view;
    }

    /**
     * 自动显示刷新的弹出框
     */
    private final static long ONE_TIME_DAY = 86400000;

    private void autoShowPopuwind() {
        SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        Long oldtime = share.getLong("oldtime", 0);
        Long newtime = System.currentTimeMillis();

        if (newtime - oldtime > ONE_TIME_DAY) {
            SharedPreferences.Editor editor = share.edit();
            editor.putLong("oldtime", newtime);
            editor.commit();//1500365575916 1500365511416
            requestRefreshResume();
        } else {
        }
    }

//    private static final int REQUEST_CODE = 1;

    private void requestAlertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2010, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 1, 1);
        TimePickerView pvTime = new TimePickerView.Builder(HomeFragent.this.getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
//                tvTime.setText(getTime(date));
            }
        }).setType(TimePickerView.Type.YEAR_MONTH)
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }


    /**
     * 点击事件的方法
     *
     * @param v
     */
    private String name;
    private String resume_id;
    private String resume_mobile;
    private String job_status;
    private String uid;
    private Boolean flag = false;
    private static final int REQUEST_CODE = 12;

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_home_fragment_search://头部的点击搜索
                Intent intent = new Intent(context, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_refresh_resume://刷新简历
                flag = true;
                SharedPreferences shared = context.getSharedPreferences("data", Context.MODE_PRIVATE);
                resume_mobile = shared.getString("resume_mobile", "");
                Log.i("moilesssss", resume_mobile);
                //刷新简历
                clickRefreshResumes();//点击刷新简历
                break;
            case R.id.ll_find_work://找工作
                Intent intentFindwork = new Intent(context, SearchActivity.class);
                startActivity(intentFindwork);
                break;
            case R.id.ll_high_search://高级搜索
                Intent highIntent = new Intent(context, HighSearchActivity.class);
                startActivity(highIntent);
                break;
            case R.id.ll_location_work://附近工作
                Intent nearByIntent = new Intent(context, NearByActivity.class);
                startActivity(nearByIntent);
                break;
            case R.id.ll_part_time://兼职搜索
                Intent intentPartSearch = new Intent(context, PartSearchActivity.class);
                startActivity(intentPartSearch);
                break;
            case R.id.ll_find://资讯
                Intent intentFind = new Intent(context, FindActivity.class);
                startActivity(intentFind);
                break;
            case R.id.scan_code://二维码扫描
                Intent intentCode = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intentCode, REQUEST_CODE);
                break;
        }
    }

    /**
     * 请求是否刷新简历
     */
    private void requestRefreshResume() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_NEED_REFRESH)
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
                string = response.body().string();
                code = response.code();
                Log.i("nedddddd", string);
                handler.sendEmptyMessage(120);
            }
        });
    }

    /**
     * 点击刷新简历
     */
    private void clickRefreshResumes() {
        if (api_token != null && resume_id != null && !"".equals(api_token) && !"".equals(resume_id)) {
            SharedPreferences share = context.getSharedPreferences("data", Context.MODE_PRIVATE);

            long newtime = System.currentTimeMillis();
            SharedPreferences.Editor editor = share.edit();
            editor.putLong("oldtime", newtime);
            editor.commit();
            showRefreashResumes();//刷新简历的弹出框

        } else if (api_token == null || "".equals(api_token)) {
            if (flag) {
                Intent intent = new Intent(context, WXEntryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        } else if (resume_id == null || "".equals(resume_id)) {

            if (flag) {
                Intent intent = new Intent(context, EditResumeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        } else {
            if (flag) {
                //Toast.makeText(context, "您还没有创建账号", Toast.LENGTH_SHORT).show();
            }
        }

        flag = false;
    }

    /**
     * 刷新简历的tanchukuang
     */
    MySexPopuwin mySexPopuwin;
    TextView text_resume_state;
    TextView text_icon;
    EditText edittext_phone;

    private void showRefreashResumes() {

        mySexPopuwin = new MySexPopuwin(getActivity(), R.layout.refreash_resume_popuwind);
        mySexPopuwin.showAtLocation(getView().findViewById(R.id.ll_popuwind), Gravity.CENTER, 0, 0);
        View view = mySexPopuwin.getView();

        edittext_phone = (EditText) view.findViewById(R.id.edittext_phone);
        text_resume_state = (TextView) view.findViewById(R.id.text_resume_state);
        text_icon = (TextView) view.findViewById(R.id.text_icon);
        TextView text_sure_refresh = (TextView) view.findViewById(R.id.text_sure_refresh);
        TextView text_cancel_refresh = (TextView) view.findViewById(R.id.text_cancel_refresh);
        edittext_phone.setText(resume_mobile);
        text_icon.setTypeface(typeface);
        if ("1".equals(job_status)) {
            text_resume_state.setText("不在职，正在找工作");
        } else if ("2".equals(job_status)) {
            text_resume_state.setText("在职，打算近期换工作");
        } else if ("3".equals(job_status)) {
            text_resume_state.setText("在职，有更好的机会才考虑");
        } else if ("4".equals(job_status)) {
            text_resume_state.setText("不考虑换工作");
        }

        //更改简历求职状态
        text_resume_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvOptions.show();
            }
        });
        //点击确认刷新
        text_sure_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySexPopuwin.dismiss();
                clickRefreshResume();
            }
        });

        //取消刷新
        text_cancel_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySexPopuwin.dismiss();
            }
        });

    }

    OptionsPickerView pvOptions;
    private List<String> options1Items = new ArrayList();

    private void showResumeStatue() {
        options1Items.add("不在职，正在找工作");
        options1Items.add("在职，打算近期换工作");
        options1Items.add("在职，有更好的机会才考虑");
        options1Items.add("不考虑换工作");
        pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);
                text_resume_state.setText(tx);
                if (options1 == 0) {
                    job_status = "1";
                } else if (options1 == 1) {
                    job_status = "2";
                } else if (options1 == 2) {
                    job_status = "3";
                } else if (options1 == 3) {
                    job_status = "4";
                }
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("求职状态选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFffffff)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
                // .setLabels("省", "市", "区")//设置选择的三级单位
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(true)//是否显示为对话框样式
                .build();

        pvOptions.setPicker(options1Items);//添加数据源

    }

    String str;
    int code;
    MyPopuwindown myPopu;
    /**
     * 点击刷新简历
     */
    private void clickRefreshResume() {
        resume_mobile = edittext_phone.getText().toString().trim();
        StyledDialog.buildLoading().show();
        OkHttpClient okhtt = new OkHttpClient();
        FormEncodingBuilder requestBody = new FormEncodingBuilder();
        requestBody.add("mobile", resume_mobile);
        requestBody.add("job_status", job_status);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_REFRESH_INFO)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(requestBody.build())
                .build();
        okhtt.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("osjdfoiisajfosji", str);
                handler.sendEmptyMessage(100);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {

            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    final String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Log.i("resultresultresultr?sult", result);
                    if (!result.startsWith("http")) {//不是URL
                        Toast.makeText(getActivity(), "解析结果:" + result, Toast.LENGTH_LONG).show();
                    } else {//是url
                        if (result.contains(ContentUrl.BASE_URL_SHORT_CODE)) {//是本地的地址
                            String[] results = result.split("\\?");
                            Log.i("00000?sult", results + "");
                            if (results.length == 2) {
                                String[] resultThr = results[1].split("&");
                                if (resultThr.length == 2) {
                                    String[] resultThr1 = resultThr[0].split("=");
                                    String[] resultThr2 = resultThr[1].split("=");
                                    if (resultThr1[0].equals("m") && resultThr1[1].equals("chat")) {
                                        Intent intent = new Intent(getActivity(), JobInfoActivity.class);
                                        intent.putExtra("id", resultThr2[1]);
                                        startActivity(intent);
                                    }else if(resultThr1[0].equals("m") && resultThr1[1].equals("com")){
                                        Intent intent = new Intent(getActivity(), CompanydetailActivityTwo.class);
                                        intent.putExtra("id", resultThr2[1]);
                                        intent.putExtra("com_id", resultThr2[1]);
                                        startActivity(intent);
                                    }
                                } else if (resultThr.length >= 3 && resultThr[1] != null) {
                                    String[] resultThr1 = resultThr[0].split("=");
                                    String[] resultThr2 = resultThr[1].split("=");
                                    String[] resultThr3 = resultThr[2].split("=");
                                    if (resultThr3.length == 2) {
                                        if (resultThr3[1] == api_token) {
                                        } else {
                                            requestApiToken(resultThr3[1]);//当api_token不相等时
                                        }
                                    }
                                    if (resultThr1[0].equals("m") && resultThr1[1].equals("chat")) {
                                        Intent intent = new Intent(getActivity(), JobInfoActivity.class);
                                        intent.putExtra("id", resultThr2[1]);
                                        intent.putExtra("params", "codess");
                                        startActivity(intent);
                                    }else if(resultThr1[0].equals("m") && resultThr1[1].equals("com")){
                                        Intent intent = new Intent(getActivity(), CompanydetailActivityTwo.class);
                                        intent.putExtra("id", resultThr2[1]);
                                        intent.putExtra("com_id", resultThr2[1]);
                                        startActivity(intent);
                                    }
                                }
                            }
                        } else {//如果不是本地的地址
                            Intent intent = new Intent(getActivity(), WebViewActivity2.class);
                            intent.putExtra("id", result);
                            getActivity().startActivity(intent);
                        }
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void requestApiToken(final String t) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        form.add("t", t);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + "user/get_user_info").addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .post(form.build()).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                handler.sendEmptyMessage(8888);
                Log.i("strstrstrstr", str);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }
}
