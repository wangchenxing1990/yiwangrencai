package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.hss01248.dialog.StyledDialog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.UiUtils;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/6.
 */
public class AddBookItemActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_book_item;
    }

    private String id;
    private String api_token;

    @Override
    public void initData() {

        id = getIntent().getStringExtra("id");
        SharedPreferences share = getSharedPreferences("Activity", MODE_PRIVATE);
        api_token = share.getString("api_token", "");
        gainBookDAtaItem();//获取到数据

    }

    int code;
    String str;

    /**
     * 获取到数据
     */
    private void gainBookDAtaItem() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_CER_EDIT)
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
                code = response.code();
                str = response.body().string();
                Log.i("[][][][][", str);
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
                    if (code == 200) {
                        JSONObject json = JSON.parseObject(str);
                        String codes = json.getString("code");
                        String mess = json.getString("msg");
                        if (codes.equals("1")) {
                            //Toast.makeText(AddBookItemActivity.this,mess,Toast.LENGTH_SHORT).show();
                            JSONObject jsonData = json.getJSONObject("data");
                            paramsJsonData(jsonData);//解析数据
                        } else {
                            Toast.makeText(AddBookItemActivity.this, mess, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddBookItemActivity.this, "更新数据失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 300:
                    if (code == 200) {
                        JSONObject json = JSON.parseObject(strss);
                        String codess = json.getString("code");
                        if (codess.equals("1")) {
                            String jsonData = json.getString("msg");
                            Toast.makeText(AddBookItemActivity.this, jsonData, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddBookItemActivity.this, EditResumeTwoActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AddBookItemActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(AddBookItemActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }

                    StyledDialog.dismissLoading();
                    break;
            }

        }
    };

    private String certificate_name;
    private String gettime;
    private String gettime_name;

    /**
     * 解析数据
     *
     * @param jsonData
     */
    private void paramsJsonData(JSONObject jsonData) {
        certificate_name = jsonData.getString("certificate_name");
        gettime = jsonData.getString("gettime");
        gettime_name = jsonData.getString("gettime_name");

        tv_input_skill_name.setText(certificate_name);
        tv_skill_level.setText(gettime_name);

    }

    private TextView tv_input_skill_name;
    private TextView tv_skill_level;

    @Override
    public void initView() {
        FrameLayout fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        TextView tv_add_save = (TextView) findViewById(R.id.tv_add_save);
        RelativeLayout rl_skill_name = (RelativeLayout) findViewById(R.id.rl_skill_name);
        RelativeLayout rl_skill_degree = (RelativeLayout) findViewById(R.id.rl_skill_degree);
        LinearLayout linearLayout_delete = (LinearLayout) findViewById(R.id.linearLayout_delete);

        tv_input_skill_name = (TextView) findViewById(R.id.tv_input_skill_name);
        tv_skill_level = (TextView) findViewById(R.id.tv_skill_level);

        fram_job_back.setOnClickListener(this);
        tv_add_save.setOnClickListener(this);
        rl_skill_name.setOnClickListener(this);
        rl_skill_degree.setOnClickListener(this);
        linearLayout_delete.setOnClickListener(this);

        initTimePicker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back://返回
                finish();
                break;
            case R.id.tv_add_save://保存
                saveUpdataBook();//保存修改后的证书数据
                break;
            case R.id.rl_skill_name://证书名称
                Intent intent = new Intent(AddBookItemActivity.this, NameActivity.class);
                intent.putExtra("params", "book");
                intent.putExtra("mobile", certificate_name);
                startActivityForResult(intent, 400);
                break;
            case R.id.rl_skill_degree://获得时间

                pvBookTime.show();
                break;
            case R.id.linearLayout_delete://删除
                deleteBookData();//删除数据
                break;
        }
    }

    /**
     * 删除数据
     */
    private void deleteBookData() {
        StyledDialog.buildLoading().show();
        OkHttpClient okhttpclient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_CER_DELETE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncoding.build())
                .build();
        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strss = response.body().string();
                code = response.code();
                System.out.print("?????????" + strss);
                handler.sendEmptyMessage(300);
            }
        });
    }

    String strss;
    /**
     * 保存数据
     */
    private void saveUpdataBook() {
        StyledDialog.buildLoading().show();
        OkHttpClient okhttpclient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);
        formEncoding.add("certificate_name", certificate_name);
        formEncoding.add("gettime", gettime);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_CER_UPDATE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(formEncoding.build())
                .build();
        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strss = response.body().string();
                code = response.code();
                System.out.print("?????????" + strss);
                Log.i("???????",strss);
                handler.sendEmptyMessage(300);
            }
        });
    }

    /**
     * 开启界面返回来的数据
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
                case 400:
                    certificate_name = data.getExtras().getString("book");
                    tv_input_skill_name.setText(certificate_name);
                    break;
            }
        }
    }

    TimePickerView pvTime;

    /**
     * 选择时间
     */
    OptionsPickerView pvBookTime;

    private void initTimePicker() {
        final List<String> list = new ArrayList<>();
        for (int i = 1900; i < 2020; i++) {
            list.add(i + "年");
        }
        pvBookTime = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String dataTimes = list.get(options1);

                tv_skill_level.setText(dataTimes);

                gettime = dataTimes.split("年")[0]+"-01-02";
                // tvOptions.setText(tx);
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("获得时间")//标题
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
                .setSelectOptions(117, 118, 118)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .build();

        pvBookTime.setPicker(list);//添加数据源

    }

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }
}