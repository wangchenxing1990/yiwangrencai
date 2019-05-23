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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.hss01248.dialog.StyledDialog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.LangOptionBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.MyPopuwindown;
import com.yiwangrencai.ywkj.view.MySexPopuwin;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 */

public class AddLangActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.add_lang_activity;
    }

    @Override
    public void initData() {

    }

    private FrameLayout fram_job_back;
    private TextView tv_add_save;
    private TextView tv_project_start;
    private TextView tv_input_project_name;
    private TextView tv_lanf_degree;
    private RelativeLayout rl_langue_name;
    private RelativeLayout rl_proficiency_degree;
    private RelativeLayout rl_lang_degree;

    @Override
    public void initView() {
        fram_job_back = (FrameLayout) findViewById(R.id.fram_job_back);
        tv_add_save = (TextView) findViewById(R.id.tv_add_save);

        rl_langue_name = (RelativeLayout) findViewById(R.id.rl_langue_name);
        rl_proficiency_degree = (RelativeLayout) findViewById(R.id.rl_proficiency_degree);
        rl_lang_degree = (RelativeLayout) findViewById(R.id.rl_lang_degree);

        tv_project_start = (TextView) findViewById(R.id.tv_project_start);
        tv_input_project_name = (TextView) findViewById(R.id.tv_input_project_name);
        tv_lanf_degree = (TextView) findViewById(R.id.tv_lanf_degree);
        fram_job_back.setOnClickListener(this);
        tv_add_save.setOnClickListener(this);
        rl_langue_name.setOnClickListener(this);
        rl_proficiency_degree.setOnClickListener(this);
        rl_lang_degree.setOnClickListener(this);
    }

    String flag;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_job_back://返回上一个界面
                finish();
                break;
            case R.id.tv_add_save://保存数据

                saveDataToServicer();

                break;
            case R.id.rl_langue_name://选择语种
                Intent addLangName = new Intent(AddLangActivity.this, EducationActivity.class);
                addLangName.putExtra("education", "langue");
                addLangName.putExtra("company_name", langData);
                startActivityForResult(addLangName, 20);
                break;
            case R.id.rl_proficiency_degree://熟练程度
                showLangDegree();
                break;
            case R.id.rl_lang_degree://语言等级
                gainLanguageDegree();
                break;
        }
    }

    String strdata;
    MyPopuwindown mypopuwinds;

    /**
     * 向服务器提交数据
     */
    private void saveDataToServicer() {

        if (langDataID==null){
            Toast.makeText(AddLangActivity.this,"请选择语种",Toast.LENGTH_SHORT).show();
            return;
        }
        if (flag==null){
            Toast.makeText(AddLangActivity.this,"请选择熟练程度",Toast.LENGTH_SHORT).show();
            return;
        }
        StyledDialog.buildLoading().show();

        SharedPreferences share = getSharedPreferences("data", MODE_PRIVATE);
        String id = share.getString("resume_id", "");
        SharedPreferences shares = getSharedPreferences("Activity", MODE_PRIVATE);
        String api_token = shares.getString("api_token", "");

        OkHttpClient okhttpclients = new OkHttpClient();
        FormEncodingBuilder formEncondingg = new FormEncodingBuilder();
        formEncondingg.add("resume_id", id);
        formEncondingg.add("language", langDataID);
        formEncondingg.add("degree", flag);
        if (langoptionId == null) {
            //formEnconding.add("level","");
        } else {
            formEncondingg.add("level", langoptionId);
        }


        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_LANG_CREATE)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(formEncondingg.build())
                .build();
        okhttpclients.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strdata = response.body().string();
                codes = response.code();
                Log.i("++++++++111111333", strdata);
                handler.sendEmptyMessage(2000);
            }
        });
    }

    String str;
    int code;
    int codes;
    MyPopuwindown myPopuwion;

    /**
     * 获取语言等级
     */
    private void gainLanguageDegree() {
        if (langDataID == null || "".equals(langDataID)) {
            Toast.makeText(AddLangActivity.this, "请选择语言类型", Toast.LENGTH_SHORT).show();
            return;
        }

        myPopuwion = new MyPopuwindown(AddLangActivity.this, R.layout.my_popuwindown);
        myPopuwion.showAtLocation(AddLangActivity.this.findViewById(R.id.ll_popuwind), Gravity.CENTER, 0, 0);
        OkHttpClient okhttpclient = new OkHttpClient();
        FormEncodingBuilder formEncoding = new FormEncodingBuilder();
        formEncoding.add("sign", "language");
        formEncoding.add("pid", langDataID);
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
                handler.sendEmptyMessage(100);
            }
        });
    }

    private List<LangOptionBean> optionList = new ArrayList<LangOptionBean>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    if (code == 200) {
                        JSONObject jsonObject = JSON.parseObject(str);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.size() == 0) {
                            Toast.makeText(AddLangActivity.this, "没有语言等级", Toast.LENGTH_SHORT).show();
                        } else {
                            optionList.clear();
                            for (int i = 0; i < jsonArray.size(); i++) {
                                LangOptionBean langOption = new LangOptionBean();
                                JSONObject json = JSON.parseObject(jsonArray.getString(i));
                                langOption.setOpt_name(json.getString("opt_name"));
                                langOption.setOpt_id(json.getString("opt_id"));
                                optionList.add(langOption);
                            }
                            Intent intent = new Intent(AddLangActivity.this, EducationActivity.class);
                            intent.putExtra("education", "langOption");
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("optionlist", (Serializable) optionList);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 60);
                        }
                    } else {
                        Toast.makeText(AddLangActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                    myPopuwion.dismiss();
                    break;
                case 2000:
                    if (codes == 200) {
                        JSONObject json = JSON.parseObject(strdata);
                        String message = json.getString("msg");
                        String code = json.getString("code");
                        if ("1".equals(code)){
                            Toast.makeText(AddLangActivity.this, message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddLangActivity.this, EditResumeTwoActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else if("0".equals(code)){
                            Toast.makeText(AddLangActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(AddLangActivity.this, "添加语言技能失败", Toast.LENGTH_SHORT).show();
                    }
                    StyledDialog.dismissLoading();
                    break;
            }
        }
    };

    /**
     * 显示语言的等级
     */
    private void writeLangDegree() {
        if (flag.equals("1")) {
            tv_project_start.setText("入门");
        } else if (flag.equals("2")) {
            tv_project_start.setText("熟练");
        } else if (flag.equals("3")) {
            tv_project_start.setText("精通");
        }
    }

    /**
     * 显示语言的熟练程度
     */
    OptionsPickerView pvOptions;

    private void showLangDegree() {
        final List<String> options1Items = new ArrayList<>();
        options1Items.add("入门");
        options1Items.add("熟练");
        options1Items.add("精通");
       // options1Items.add("其他");
        pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);
                tv_project_start.setText(tx);
                if (options1 == 0) {
                    flag = "1";
                } else if (options1 == 1) {
                    flag = "2";
                } else if (options1 == 2) {
                    flag = "3";
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

    String langData="";
    String langDataID;
    String langoption;
    String langoptionId;

    /**
     * 开启一个界面返回的数据
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
                    langData = data.getExtras().getString("languedata");
                    langDataID = data.getExtras().getString("languedataId");
                    if (langDataID==null||"".equals(langDataID)){

                    }else{
                        tv_input_project_name.setText(langData);
                    }

                    break;
                case 60:
                    langoption = data.getExtras().getString("langoption");
                    langoptionId = data.getExtras().getString("langoptionId");
                    if (langoptionId==null||"".equals(langoptionId)){

                    }else{
                        tv_lanf_degree.setText(langoption);
                    }

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
