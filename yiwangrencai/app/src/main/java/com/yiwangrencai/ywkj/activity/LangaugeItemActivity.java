package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import com.alibaba.fastjson.JSONArray;
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
import com.yiwangrencai.ywkj.bean.LangOptionBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.UiUtils;
import com.yiwangrencai.ywkj.view.MyPopuwindown;
import com.yiwangrencai.ywkj.view.MySexPopuwin;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/5.
 */
public class LangaugeItemActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_language_item;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    JSONObject json = JSON.parseObject(strdata);
                    String scode = json.getString("code");
                    if (scode.equals("1")) {
                       // Toast.makeText(LangaugeItemActivity.this, "获取数据成功", Toast.LENGTH_SHORT).show();
                        JSONObject jsonData = json.getJSONObject("data");
                        parasJsonData(jsonData);//解析数据
                    }
                    break;
                case 300:
                    if (code == 200) {

                        JSONObject jsons = JSON.parseObject(strsss);
                        String code = jsons.getString("code");
                        if (code.equals("1")) {

                          //  Toast.makeText(LangaugeItemActivity.this, jsons.getString("msg"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LangaugeItemActivity.this, EditResumeTwoActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } else {
                            Toast.makeText(LangaugeItemActivity.this, "更新数据失败请重试", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LangaugeItemActivity.this, "更新数据失败请重试" + code, Toast.LENGTH_SHORT).show();
                    }
                    StyledDialog.dismissLoading();
                    break;
                case 220:
                    if (code == 200) {
                        JSONObject jsonss = JSON.parseObject(str);
                        String code = jsonss.getString("code");
                        JSONArray data = jsonss.getJSONArray("data");
                        if ("1".equals(code)) {
                            if (data.size() != 0) {
                                List<LangOptionBean> optionList = parasJsonDatas(data);
                                Intent intent = new Intent(LangaugeItemActivity.this, EducationActivity.class);
                                intent.putExtra("education", "langOption");
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("optionlist", (Serializable) optionList);
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 60);
                            } else {
                                Toast.makeText(LangaugeItemActivity.this, "没有语言等级", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LangaugeItemActivity.this, "获取语言等级失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LangaugeItemActivity.this, "获取语言等级失败", Toast.LENGTH_SHORT).show();
                    }
                    StyledDialog.dismissLoading();
                    break;
            }
        }


    };
    private List<LangOptionBean> optionList = new ArrayList<LangOptionBean>();

    /**
     * 解析数据语言等级
     *
     * @param data
     */
    private List<LangOptionBean> parasJsonDatas(JSONArray data) {
        optionList.clear();
        for (int i = 0; i < data.size(); i++) {
            LangOptionBean langOption = new LangOptionBean();
            JSONObject json = JSON.parseObject(data.getString(i));
            langOption.setOpt_name(json.getString("opt_name"));
            langOption.setOpt_id(json.getString("opt_id"));
            optionList.add(langOption);
        }
        return optionList;
    }

    private String id;
    private String api_token;

    @Override
    public void initData() {
        //获取api_token

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        SharedPreferences share=getSharedPreferences("Activity",MODE_PRIVATE);
        api_token = share.getString("api_token","");
        //获取服务器数据
        gainDataFromService();
    }

    private String language;
    private String degree;
    private String level;
    private String language_name;
    private String level_name;

    /**
     * 解析数据
     *
     * @param jsonData
     */
    private void parasJsonData(JSONObject jsonData) {
        id = jsonData.getString("id");
        language = jsonData.getString("language");
        degree = jsonData.getString("degree");
        level = jsonData.getString("level");
        language_name = jsonData.getString("language_name");
        level_name = jsonData.getString("level_name");

        tv_input_lang_name.setText(language_name);
        tv_lang_degree.setText(level_name);

        if (degree.equals("1")) {
            tv_lang_start.setText("入门");
        } else if (degree.equals("2")) {
            tv_lang_start.setText("熟练");
        } else if (degree.equals("3")) {
            tv_lang_start.setText("精通");
        }

    }

    private TextView tv_input_lang_name;
    private TextView tv_lang_start;
    private TextView tv_lang_degree;

    @Override
    public void initView() {
        FrameLayout fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        TextView tv_add_save = (TextView) findViewById(R.id.tv_add_save);

        RelativeLayout rl_langue_name = (RelativeLayout) findViewById(R.id.rl_langue_name);
        RelativeLayout rl_proficiency_degree = (RelativeLayout) findViewById(R.id.rl_proficiency_degree);
        RelativeLayout rl_lang_degree = (RelativeLayout) findViewById(R.id.rl_lang_degree);
        LinearLayout linearLayout_delete = (LinearLayout) findViewById(R.id.linearLayout_delete);

        tv_input_lang_name = (TextView) findViewById(R.id.tv_input_lang_name);
        tv_lang_start = (TextView) findViewById(R.id.tv_lang_start);
        tv_lang_degree = (TextView) findViewById(R.id.tv_lang_degree);
        fram_job_back.setOnClickListener(this);
        tv_add_save.setOnClickListener(this);
        rl_langue_name.setOnClickListener(this);
        rl_proficiency_degree.setOnClickListener(this);
        rl_lang_degree.setOnClickListener(this);
        linearLayout_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back://返回
                finish();
                break;
            case R.id.tv_add_save://保存
                updateDataservicess();//更新数据
                break;
            case R.id.rl_langue_name://语种
                Intent intentLang = new Intent(LangaugeItemActivity.this, EducationActivity.class);
                intentLang.putExtra("education", "langue");
                intentLang.putExtra("company_name", language_name);
                startActivityForResult(intentLang, 20);
                break;
            case R.id.rl_proficiency_degree://熟练程度
                showPopuDegree();
                break;
            case R.id.rl_lang_degree://等级
                gainLanguageDegree();//获取语言等级
                break;

            case R.id.linearLayout_delete://删除数据
                deleteProjectData();
                break;
        }
    }
    String str;
    /**
     * 获取语言等级
     */
    private void gainLanguageDegree() {
        StyledDialog.buildLoading().show();
        OkHttpClient okhttpclient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("sign", "language");
        formEncoding.add("pid", language);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.OPTION_SECOND)
                .addHeader("Accept", "application/json")
                .post(formEncoding.build())
                .build();
        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("===============", str);
                handler.sendEmptyMessage(220);
            }
        });
    }

    /**
     * 删除数据
     */
    private void deleteProjectData() {
        StyledDialog.buildLoading().show();
        OkHttpClient okhttpClient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_LANG_DELETE)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(formEncoding.build())
                .build();
        okhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strsss = response.body().string();
                code = response.code();
                //System.out.print("更新项目数据成功"+strsss);
                handler.sendEmptyMessage(300);
            }
        });
    }

    private TextView tv_cancel;
    private TextView tv_sure;
    private TextView tv_secret;
    private TextView tv_man;
    private TextView tv_woman;
    MySexPopuwin mySex;

    /**
     * 显示熟练程度
     */
    OptionsPickerView pvOptions;
    private void showPopuDegree() {
        final List<String> options1Items = new ArrayList<>();
        options1Items.add("入门");
        options1Items.add("熟练");
        options1Items.add("精通");
        //options1Items.add("其他");
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);
                tv_lang_start.setText(tx);
                if (options1 == 0) {
                    degree = "1";
                } else if (options1 == 1) {
                    degree = "2";
                } else if (options1 == 2) {
                    degree = "3";
                }
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("熟练程度")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFffffff)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
//                .setLabels("省", "市", "区")//设置选择的三级单位
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .build();

        pvOptions.setPicker(options1Items);//添加数据源
        pvOptions.show();

    }

    String strdata;
    int code;

    /**
     * 从服务器获取数据
     */
    private void gainDataFromService() {
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_LANG_EDIT)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(formEncoding.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strdata = response.body().string();
                code = response.code();
                Log.i("010110101010010101011001", strdata);
                handler.sendEmptyMessage(100);
            }
        });
    }

    String strsss;
    MyPopuwindown mypopu;

    /**
     * 更新数据
     */
    private void updateDataservicess() {
        StyledDialog.buildLoading().show();

        OkHttpClient okhttpp = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("id", id);
        formEncoding.add("language", language);
        formEncoding.add("degree", degree);

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_LANG_UPDATE)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(formEncoding.build())
                .build();
        okhttpp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strsss = response.body().string();
                code = response.code();
                System.out.print("============" + code);
                handler.sendEmptyMessage(300);
            }
        });
    }

    /**
     * 打开一个界面 返回来的数据
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
                case 20:
                    language_name = data.getExtras().getString("languedata");
                    language = data.getExtras().getString("languedataId");
                    tv_input_lang_name.setText(language_name);
                    break;
                case 60:
                    level_name = data.getExtras().getString("langoption");
                    level = data.getExtras().getString("langoptionId");
                    tv_lang_degree.setText(level_name);
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }

}
