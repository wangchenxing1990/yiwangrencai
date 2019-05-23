package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.adapter.MyBlackNumAdapter;
import com.yiwangrencai.ywkj.adapter.MyInterViewAdapter;
import com.yiwangrencai.ywkj.bean.BlackNumBean;
import com.yiwangrencai.ywkj.bean.InterViewBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.LoginOffPopuWindow;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */

public class BlacknumActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.black_num_activity;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    if (code == 200) {
                        JSONObject json = JSON.parseObject(str);
                        String codes = json.getString("code");
                        if ("1".equals(codes)) {
                            JSONArray jsonArray = json.getJSONArray("data");
                            if (jsonArray.size() == 0) {
                                frameLayout.removeView(viewLoading);
                                frameLayout.addView(viewEmpty);
                            } else {
                                frameLayout.removeView(viewLoading);
                                frameLayout.addView(viewSuccess);
                                parsBlackNumber(jsonArray);//解析黑名单列表
                            }
                        } else if ("0".equals(codes)) {
                            frameLayout.removeView(viewLoading);
                            //todo
                        } else if ("-1".equals(codes)) {
                            frameLayout.removeView(viewLoading);
                            //todo
                        }

                    } else {

                    }
                    break;
                case 110:
                    Toast.makeText(BlacknumActivity.this, "联网超时,请稍后重试", Toast.LENGTH_SHORT).show();
                    break;
                case 120:
                    cancelWindow.dismiss();
                    black.remove(blackNumList.get(positions).getCompany_name());
                    blackNumList.remove(positions);

                    if (blackNumList.size()==0){
                       // adapter.notifyDataSetChanged();
                        frameLayout.removeAllViews();
                        frameLayout.addView(viewEmpty);
                    }else{
                        adapter.notifyDataSetChanged();
                    }

                    break;
            }
        }
    };

    private int positions;
    private List<BlackNumBean> blackNumList = new ArrayList<>();
    private List<String> black = new ArrayList<>();

    /**
     * 解析黑名单列表
     *
     * @param jsonArray
     */
    private MyBlackNumAdapter adapter;
    private void parsBlackNumber(JSONArray jsonArray) {
        blackNumList.clear();
        for (int i = 0; i < jsonArray.size(); i++) {

            BlackNumBean blacknem = new BlackNumBean();
            JSONObject jsondatas = jsonArray.getJSONObject(i);
            blacknem.setId(jsondatas.getString("id"));
            blacknem.setCompany_name(jsondatas.getString("company_name"));
            blacknem.setCompany_uid(jsondatas.getString("company_uid"));

            blackNumList.add(blacknem);
            black.add(blackNumList.get(i).getCompany_name());
        }

        adapter = new MyBlackNumAdapter(blackNumList);
        recycler_view_test_rv.setAdapter(adapter);

        adapter.setOnItemClickListener(new MyBlackNumAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                id = blackNumList.get(position).getId();
                positions=position;
                showPopuwind(position);//显示取消黑名单的对话框
            }
        });
    }

    String id;
    /**
     * 显示取消黑名单的对话框
     */
    LoginOffPopuWindow cancelWindow;
    private void showPopuwind(final int position) {
        cancelWindow = new LoginOffPopuWindow(BlacknumActivity.this, R.layout.login_off_popuwindown);
        View view = cancelWindow.getView();
        cancelWindow.showAtLocation(BlacknumActivity.this.findViewById(R.id.system_setting_popuwind), Gravity.CENTER, 0, 0);
        TextView textview = (TextView) view.findViewById(R.id.textview);
        TextView tv_off_cancel = (TextView) view.findViewById(R.id.tv_off_cancel);
        TextView tv_off_sure = (TextView) view.findViewById(R.id.tv_off_sure);
        textview.setText("确定取消黑名单吗?");
        tv_off_cancel.setOnClickListener(this);
        tv_off_sure.setOnClickListener(this);
    }

    String str;
    int code;
    String api_token;

    @Override
    public void initData() {
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.SHIELD)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncodingBuilder.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
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
                System.out.println("rrrrrrrrrrrrrr" + str);
                handler.sendEmptyMessage(100);
            }
        });


    }

    private FrameLayout fram_job_back;
    private TextView tv_add_black;
    private FrameLayout frameLayout;
    private View viewLoading;
    private View viewEmpty;
    private View viewSuccess;

    @Override
    public void initView() {

        tv_add_black = (TextView) findViewById(R.id.tv_add_black);
        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        tv_add_black.setOnClickListener(this);
        fram_job_back.setOnClickListener(this);

        viewLoading = createLoadingView();
        viewEmpty = createEmptyView();
        viewSuccess = createSuccessView();

        frameLayout.addView(viewLoading);

    }

    private RecyclerView recycler_view_test_rv;
    /**
     * 创建加载成功界面
     */
    private View createSuccessView() {
        View view = View.inflate(BlacknumActivity.this, R.layout.black_listview, null);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);
        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(this));
        return view;
    }

    /**
     * 创建没有黑名单界面
     *
     * @return
     */
    private View createEmptyView() {
        View view = View.inflate(BlacknumActivity.this, R.layout.frameblackempty, null);

        return view;
    }

    /**
     * 创建正在加载黑名单的界面
     *
     * @return
     */
    private View createLoadingView() {
        View view = View.inflate(BlacknumActivity.this, R.layout.loading_view, null);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back:
                finish();
                break;
            case R.id.tv_add_black:
                Intent intent = new Intent(BlacknumActivity.this, AddBlackActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("list", (Serializable) black);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tv_off_cancel://点击取消黑名单
                cancelWindow.dismiss();
                break;
            case R.id.tv_off_sure://点击确认添加黑名单
                deleteBlackNum();//删除黑名单
                break;
        }
    }

    /**
     * 删除黑名单
     */
    private void deleteBlackNum() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder fromEncoding = new FormEncodingBuilder();
        fromEncoding.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.SHIELD_DELETE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(fromEncoding.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("删除黑名单",str);
                handler.sendEmptyMessage(120);
            }
        });
    }
}
