package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.icu.text.MessagePattern;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.wxapi.WXEntryActivity;
import com.yiwangrencai.ywkj.adapter.MyListPartAdapter;
import com.yiwangrencai.ywkj.bean.PartDetailBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.MyListView;
import com.yiwangrencai.ywkj.view.MyPartGridView;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jaydenxiao.com.expandabletextview.ExpandableTextView;

/**
 * Created by Administrator on 2017/7/3.
 */
public class PartDetailActivity extends BaseActiviyt {
    @Bind(R.id.fram_job_back)
    FrameLayout framJobBack;
    @Bind(R.id.title_skill)
    TextView titleSkill;
    @Bind(R.id.text_reject)
    TextView textReject;
    @Bind(R.id.text_accept)
    TextView textAccept;
    @Bind(R.id.text_apply)
    TextView textApply;
    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;
    @Bind(R.id.ll_popuwind)
    LinearLayout llPopuwind;

    TextView textIcon1;
    TextView textIcon2;
    TextView textLocation;
    TextView textIcon3;
    TextView textIcon4;
    TextView textIcon5;
    TextView textIcon6;
    TextView textIcon7;
    TextView text_title;
    TextView text_company_name;
    TextView text_address;
    TextView text_salary_price;
    TextView text_need_num;
    TextView textView3;
    TextView text_contacts;
    TextView text_phone;
    TextView text_education;
    TextView text_sex;
    TextView text_content;
    TextView text_look;
    TextView text_part_address;
    TextView text_part_other_job;
    ExpandableTextView text_profile;

    @Override
    public int getLayoutId() {
        return R.layout.activity_part_detail;
    }

    String id;
    String api_token;
    String part_resume_id;
    String ids;
    String status;
    String params;

    @Override
    public void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        status = intent.getStringExtra("status");
        ids = intent.getStringExtra("ids");
        params = intent.getStringExtra("params");
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");
        SharedPreferences shared = getSharedPreferences("data", MODE_PRIVATE);
        part_resume_id = shared.getString("part_resume_id", "");

    }

    /**
     * 从服务器获取数据
     */
    int code;
    String str;

    private void gainDataFromService() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        form.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.PART_JOB_SHOW)
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
                str = response.body().string();
                code = response.code();
                Log.i("显示职位详情：part_job/show", str);
                handler.sendEmptyMessage(100);
            }
        });
    }

    private static final int APPLY_PART_TIME = 0;
    private static final int REJECT_ACCEPT_PART_TIME = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    parsGainDataFromService();//解析获取的职位详情的数据
                    break;
                case APPLY_PART_TIME:
                    parsApplyPartTime();//解析报名成功的数据
                    break;
                case REJECT_ACCEPT_PART_TIME:
                    parsAcceptRejectPartTime();//解析拒绝或者接受的数据
                    break;
            }
        }
    };

    /**
     * 解析拒绝或者接受的数据
     */
    private void parsAcceptRejectPartTime() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            String message = json.getString("msg");
            if ("1".equals(codes)) {
                if (flag == "2") {
                    textReject.setVisibility(View.GONE);
                    textAccept.setText("已同意");
                    textAccept.setClickable(false);
                } else if (flag == "3") {
                    textAccept.setVisibility(View.GONE);
                    textReject.setText("已拒绝");
                    textReject.setClickable(false);
                }
            } else {

            }
            if (message != null && !"".equals(message)) {
                Toast.makeText(PartDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

    /**
     * 解析报名成功的数据
     */
    private void parsApplyPartTime() {
        if (code == 200) {
            JSONObject jsonObject = JSON.parseObject(str);
            String codes = jsonObject.getString("code");
            String message = jsonObject.getString("msg");
            if ("1".equals(codes)) {
                textApply.setText("已报名");
            } else if ("-1".equals(codes)) {

            }
            Toast.makeText(PartDetailActivity.this, message, Toast.LENGTH_SHORT).show();
        } else {

        }
    }

    PartDetailBean partDetailBean;
    private MyListPartAdapter listAdapter;

    private void parsGainDataFromService() {
        if (code == 200) {
            frameLayout.removeView(loadingView);
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            String message = json.getString("msg");
            if ("1".equals(codes)) {

                if ("0".equals(status)) {
                    textReject.setVisibility(View.VISIBLE);
                    textAccept.setVisibility(View.VISIBLE);
                } else if ("1".equals(status)) {
                    textReject.setVisibility(View.VISIBLE);
                    textAccept.setVisibility(View.VISIBLE);
                } else if ("2".equals(status)) {
                    textReject.setVisibility(View.GONE);
                    textAccept.setVisibility(View.VISIBLE);
                    textAccept.setText("已同意");
                } else if ("3".equals(status)) {
                    textReject.setVisibility(View.VISIBLE);
                    textAccept.setVisibility(View.GONE);
                    textReject.setText("已拒绝");
                }

                frameLayout.addView(successView);
                Gson gson = new Gson();
                partDetailBean = gson.fromJson(str, PartDetailBean.class);
                text_title.setText(partDetailBean.getData().getTitle());
                text_company_name.setText(partDetailBean.getData().getCompany_name());
                text_address.setText(partDetailBean.getData().getAddress());
                text_salary_price.setText(partDetailBean.getData().getSalary_price() + partDetailBean.getData().getSalary_unit_name() + "(" + partDetailBean.getData().getSalary_method_name() + ")");
                textApply.setVisibility(View.VISIBLE);

                if ("0".equals(partDetailBean.getData().getNeed_num())) {
                    text_need_num.setText("招聘人数若干");
                } else {
                    text_need_num.setText("招聘" + partDetailBean.getData().getNeed_num() + "人");
                }

                textView3.setText("有效时间: " + partDetailBean.getData().getTime_start() + "～" + partDetailBean.getData().getTime_end());
                text_contacts.setText("发  布  人: " + partDetailBean.getData().getContacts());
                text_phone.setText("手机号码: " + partDetailBean.getData().getMobile());
                text_education.setText("学历要求: " + partDetailBean.getData().getEducation_name());
                text_content.setText(partDetailBean.getData().getContent());
                text_profile.setText(partDetailBean.getData().getProfile());
                text_part_address.setText("工作区域 : "+partDetailBean.getData().getCity_id_name());

                if ("0".equals(partDetailBean.getData().getSex())) {
                    text_sex.setText("性别要求:不限");
                } else if ("1".equals(partDetailBean.getData().getSex())) {
                    text_sex.setText("性别要求:男性");
                } else if ("2".equals(partDetailBean.getData().getSex())) {
                    text_sex.setText("性别要求:女性");
                }

                if (partDetailBean.getData().getOther_job().size() == 0) {
                    text_part_other_job.setVisibility(View.VISIBLE);
                    list_view_part.setVisibility(View.GONE);
                } else {
                    text_part_other_job.setVisibility(View.GONE);
                    listAdapter = new MyListPartAdapter(partDetailBean.getData().getOther_job(),PartDetailActivity.this);
                    list_view_part.setAdapter(listAdapter);
                }

            } else if ("-1".equals(codes)) {
                Intent intent = new Intent(PartDetailActivity.this, WXEntryActivity.class);
                startActivity(intent);
            } else if ("0".equals(codes)) {
                frameLayout.addView(emptyView);
            }

            String coordinate_address = partDetailBean.getData().getCoordinate_address();
            if (coordinate_address != null && !"".equals(coordinate_address)) {
                image_view.setVisibility(View.VISIBLE);
                text_look.setVisibility(View.VISIBLE);
            } else {

                relativeLayout_address.setClickable(false);
            }

            if (0 == partDetailBean.getData().getSend()) {
                textApply.setText("报名");
            } else if (1 == partDetailBean.getData().getSend()) {
                textApply.setText("已报名");
            }

            my_grid_view.initView(partDetailBean.getData().getFree_time());

            if (message != null && !"".equals(message)) {
                Toast.makeText(PartDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }

        } else {

        }
    }

    private View loadingView;
    private View successView;
    private View emptyView;
    private ImageView image_view;
    private MyPartGridView my_grid_view;
    private MyListView list_view_part;

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingView = createLoadingView();
        emptyView = createEmptyView();
        successView = createSuccessView();
        frameLayout.addView(loadingView);
        gainDataFromService();//从服务器获取数据
    }

    private View createEmptyView() {
        View view = View.inflate(PartDetailActivity.this, R.layout.empty_view, null);
        TextView textView = (TextView) view.findViewById(R.id.textview_click);
        textView.setText("当前还没有该职位");
        return view;
    }

    private RelativeLayout relativeLayout_address;

    private View createSuccessView() {
        View view = View.inflate(PartDetailActivity.this, R.layout.part_detail_success, null);

        textIcon1 = (TextView) view.findViewById(R.id.text_icon_one);
        textIcon2 = (TextView) view.findViewById(R.id.text_icon_two);
        textIcon3 = (TextView) view.findViewById(R.id.text_icon_three);
        textIcon4 = (TextView) view.findViewById(R.id.text_icon_four);
        textIcon5 = (TextView) view.findViewById(R.id.text_icon_five);
        textIcon6 = (TextView) view.findViewById(R.id.text_icon_six);
        textIcon7 = (TextView) view.findViewById(R.id.text_icon_seven);
        textLocation = (TextView) view.findViewById(R.id.text_location);

        relativeLayout_address = (RelativeLayout) view.findViewById(R.id.relative_layout_address);

        text_title = (TextView) view.findViewById(R.id.text_title);
        text_company_name = (TextView) view.findViewById(R.id.text_company_name);
        text_address = (TextView) view.findViewById(R.id.text_address);
        text_salary_price = (TextView) view.findViewById(R.id.text_salary_price);
        text_need_num = (TextView) view.findViewById(R.id.text_need_num);
        textView3 = (TextView) view.findViewById(R.id.textView3);
        text_contacts = (TextView) view.findViewById(R.id.text_contacts);
        text_phone = (TextView) view.findViewById(R.id.text_phone);
        text_education = (TextView) view.findViewById(R.id.text_education);
        text_sex = (TextView) view.findViewById(R.id.text_sex);
        text_content = (TextView) view.findViewById(R.id.text_content);
        text_look = (TextView) view.findViewById(R.id.text_look);
        text_part_address = (TextView) view.findViewById(R.id.text_part_address);
        text_part_other_job = (TextView) view.findViewById(R.id.text_part_other_job);
        image_view = (ImageView) view.findViewById(R.id.image_view);
        text_profile = (ExpandableTextView) view.findViewById(R.id.text_profile);
        my_grid_view = (MyPartGridView) view.findViewById(R.id.my_grid_view);
        list_view_part = (MyListView) view.findViewById(R.id.list_view_part);

        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont.ttf");

        textIcon1.setTypeface(iconfont);
        textIcon2.setTypeface(iconfont);
        textIcon3.setTypeface(iconfont);
        textIcon4.setTypeface(iconfont);
        textIcon5.setTypeface(iconfont);
        textIcon6.setTypeface(iconfont);
        textIcon7.setTypeface(iconfont);
        textLocation.setTypeface(iconfont);

        relativeLayout_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("aaaaa", "1111111");
                String coordinate_address = partDetailBean.getData().getCoordinate_address();
                if (coordinate_address != null && !"".equals(coordinate_address)) {
                    Intent intent = new Intent(PartDetailActivity.this, AddressActivity.class);
                    intent.putExtra("latitude", partDetailBean.getData().getLatitude());
                    intent.putExtra("longitude", partDetailBean.getData().getLongitude());
                    intent.putExtra("coordinate_address", coordinate_address);
                    startActivity(intent);
                }
            }
        });

        list_view_part.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PartDetailActivity.this, PartDetailActivity.class);
                intent.putExtra("id", partDetailBean.getData().getOther_job().get(position).getId());
                startActivity(intent);
            }
        });

        return view;
    }

    private View createLoadingView() {
        View view = View.inflate(PartDetailActivity.this, R.layout.loading_view, null);
        return view;
    }

    String flag = "";

    @OnClick({R.id.fram_job_back, R.id.text_reject, R.id.text_accept, R.id.text_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fram_job_back://返回上一个页面
                if ("apply".equals(params)) {
                    Intent intent = new Intent(PartDetailActivity.this, PartApplyActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if ("receive".equals(params)) {
                    Intent intent = new Intent(PartDetailActivity.this, ReceiveActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.text_reject://拒绝
                if ("0".equals(status) || "1".equals(status)) {
                    rejectPartApplyPartTime("3");//拒绝邀请
                    flag = "3";
                } else {
                    Toast.makeText(PartDetailActivity.this, "已拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.text_accept://接受
                if ("0".equals(status) || "1".equals(status)) {
                    rejectPartApplyPartTime("2");//接受邀请
                    flag = "2";
                } else {
                    Toast.makeText(PartDetailActivity.this, "已同意", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.text_apply://报名

                if (0 == partDetailBean.getData().getSend()) {
                    if (api_token == null && api_token.isEmpty()) {
                        Intent intent = new Intent(PartDetailActivity.this, WXEntryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        if (part_resume_id != null && !part_resume_id.isEmpty()) {
                            applyPartTimeJob();//报名兼职
                        } else {
                            Intent intent = new Intent(PartDetailActivity.this, PartJobActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                } else if (1 == partDetailBean.getData().getSend()) {
                    Toast.makeText(PartDetailActivity.this, "已报名", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /**
     * 接受或者拒绝邀请
     */
    private void rejectPartApplyPartTime(String status) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        form.add("id", ids);
        form.add("status", status);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.PART_SIGN_PROCESS_UPDATE)
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
                str = response.body().string();
                code = response.code();
                Log.i("接受或拒绝的数据", str);
                handler.sendEmptyMessage(REJECT_ACCEPT_PART_TIME);
            }
        });
    }

    /**
     * 报名兼职
     */
    private void applyPartTimeJob() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        form.add("company_uid", partDetailBean.getData().getUid());
        form.add("com_id", partDetailBean.getData().getCom_id());
        form.add("pt_resume_id", part_resume_id);
        form.add("pt_job_id", partDetailBean.getData().getId());
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.PART_SIGN_UP_CREATE)
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
                str = response.body().string();
                code = response.code();
                Log.i("报名成功返回的数据", str);
                handler.sendEmptyMessage(APPLY_PART_TIME);
            }
        });
    }

}
