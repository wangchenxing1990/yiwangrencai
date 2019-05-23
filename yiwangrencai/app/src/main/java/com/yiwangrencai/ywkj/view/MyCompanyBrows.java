package com.yiwangrencai.ywkj.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.BaseApplication;
import com.yiwangrencai.ywkj.content.ContentUrl;

import java.io.IOException;

/**
 * Created by Administrator on 2017/5/15.
 */

public class MyCompanyBrows extends CompanyView {

    XRefreshView recycler_view;
    RecyclerView recycler_view_test_rv;
    Context context;

    public MyCompanyBrows(Context context) {
        this.context = context;
    }

    @Override
    public void initView() {
        View view = View.inflate(BaseApplication.getApplication(), R.layout.fram_listview, null);
        recycler_view = (XRefreshView) view.findViewById(R.id.recycler_view);
        recycler_view_test_rv = (RecyclerView) view.findViewById(R.id.recycler_view_test_rv);

        recycler_view.setPullLoadEnable(true);
        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
            }
        });

    }

    @Override
    public void initData() {
        gainDataFromService();
    }

    String str;
    int code;

    private void gainDataFromService() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Activity", Context.MODE_PRIVATE);
        String api_token = sharedPreferences.getString("api_token", "");
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.USER_BROWSE_COM)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
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
                    parsdata();
                    break;
            }
        }
    };

    private void parsdata() {
        if (code == 200) {
            JSONObject jsonObject = JSON.parseObject(str);
            String codes = jsonObject.getString("codes");

            if ("1".equals(codes)) {


            } else if ("0".equals(codes)) {

            } else if ("-1".equals(codes)) {

            }
        }
    }
}
