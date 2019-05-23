package com.yiwangrencai.ywkj.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hss01248.dialog.StyledDialog;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import java.io.IOException;

import javax.crypto.spec.DESKeySpec;

/**
 * Created by Administrator on 2017/4/27.
 */

public class DescribeActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.describe_activity;
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1000:
                    JSONObject jsonObject= JSON.parseObject(str);
                    String code =jsonObject.getString("code");
                    if (code.equals("1")){
                        Toast.makeText(DescribeActivity.this,"自我介绍更新成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(DescribeActivity.this,EditResumeTwoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else {
                        Toast.makeText(DescribeActivity.this,"自我介绍更新失败",Toast.LENGTH_SHORT).show();
                    }
                    StyledDialog.dismissLoading();
                    break;
            }
        }
    };
    @Override
    public void initData() {

    }

    private FrameLayout fram_decribe_back;
    private TextView tv_save_describe;
    private TextView tv_clear_all;
    private TextView title_textview;
    private TextView tv_number_limit;
    private TextView text_view_input_number;
    private EditText et_input_describe;
    private String params;
    private String introduction;

    @Override
    public void initView() {
        Intent intent = getIntent();
        params = intent.getStringExtra("params");
        introduction = intent.getStringExtra("introduction");
        fram_decribe_back = (FrameLayout) findViewById(R.id.fram_decribe_back);
        tv_save_describe = (TextView) findViewById(R.id.tv_save_describe);
        tv_clear_all = (TextView) findViewById(R.id.tv_clear_all);
        title_textview = (TextView) findViewById(R.id.title_textview);
        tv_number_limit = (TextView) findViewById(R.id.tv_number_limit);
        text_view_input_number = (TextView) findViewById(R.id.text_view_input_number);
        et_input_describe = (EditText) findViewById(R.id.et_input_describe);

        fram_decribe_back.setOnClickListener(this);
        tv_save_describe.setOnClickListener(this);
        tv_clear_all.setOnClickListener(this);
        et_input_describe.setOnClickListener(this);

        et_input_describe.addTextChangedListener(mTextWaycher);
        if ("self_assessment".equals(params)){
            title_textview.setText("自我评价");
            if (introduction!=null&&!introduction.isEmpty()){
                et_input_describe.setText(introduction);
            }
        }else if(params.equals("describes")){
            title_textview.setText("描述");
        }else if(params.equals("projectDiscrube")){
            title_textview.setText("项目介绍");
        }else if(params.equals("other")){
            title_textview.setText("内容描述");
        }else if(params.equals("describework")){
            title_textview.setText("工作描述");
        }else if(params.equals("oneselfDescr")){
            title_textview.setText("自我介绍");
            text_view_input_number.setText("最多输入400个字/");
            tv_number_limit.setText("400");
            et_input_describe.setFilters(new InputFilter[]{new InputFilter.LengthFilter(400)});
        }else if(params.equals("base_info_address")){
            title_textview.setText("详细地址");
        }
        et_input_describe.setText(introduction);
        if (introduction!=null&&!"".equals(introduction)){
            et_input_describe.setSelection(et_input_describe.getText().length());
        }
        et_input_describe.setFocusable(true);
        et_input_describe.setFocusableInTouchMode(true);
        et_input_describe.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_input_describe, 0);
       // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    String describe;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fram_decribe_back:
                finish();
                break;
            case R.id.tv_clear_all:
                et_input_describe.setText("");
                break;
            case R.id.tv_save_describe:
                 describe=et_input_describe.getText().toString().trim();
                if (params.equals("describe")){
                    Intent intent=new Intent();
                    intent.putExtra("describe",describe);
                    DescribeActivity.this.setResult(60,intent);
                    finish();
                }else if(params.equals("describework")){
                    Intent intent=new Intent();
                    intent.putExtra("describe",describe);
                    DescribeActivity.this.setResult(61,intent);
                    finish();
                }else if("base_info_address".equals(params)){
                    Intent intent=new Intent();
                    intent.putExtra("describe",describe);
                    DescribeActivity.this.setResult(104,intent);
                    finish();
                }else if(params.equals("self_assessment")){
                    //保存自我评价到服务器
                    saveDataServer();
                }else if(params.equals("describes")){
                    Intent intent=new Intent();
                    intent.putExtra("describes",describe);
                    DescribeActivity.this.setResult(61,intent);
                    finish();
                }else if(params.equals("projectDiscrube")){
                    Intent intent=new Intent();
                    intent.putExtra("describes",describe);
                    DescribeActivity.this.setResult(302,intent);
                    finish();
                }else if(params.equals("other")){
                    Intent intent=new Intent();
                    intent.putExtra("other",describe);
                    DescribeActivity.this.setResult(300,intent);
                    finish();
                }else if(params.equals("oneselfDescr")){
                    Intent intent=new Intent();
                    intent.putExtra("oneselfDescr",describe);
                    DescribeActivity.this.setResult(39,intent);
                    finish();
                }

                break;

        }
    }
    String str;
    MyPopuwindown myPopuwindown;
    /**
     * 保存数据到服务器
     */
    private void saveDataServer() {

        StyledDialog.buildLoading().show();
        SharedPreferences shares = getSharedPreferences("data", MODE_PRIVATE);
        String id = shares.getString("resume_id", "");
        SharedPreferences sharess = getSharedPreferences("Activity", MODE_PRIVATE);
        String api_token = sharess.getString("api_token", "");

        OkHttpClient okhttpclient=new OkHttpClient();
        FormEncodingBuilder formEncodingBuilder=new FormEncodingBuilder();
        formEncodingBuilder.add("id",id);
        formEncodingBuilder.add("introduction",describe);
        final Request request=new Request.Builder()
                .url(ContentUrl.BASE_URL+ContentUrl.RESUME_INTRO)
                .addHeader("Accept","application/json")
                .addHeader("Authorization", "Bearer " + api_token)
                .post(formEncodingBuilder.build()).build();
        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str=response.body().string();
                System.out.println("7777777777"+str);
                handler.sendEmptyMessage(1000);
            }
        });
    }

    private CharSequence temp;
    private int editStart;
    private int editEnd;
    TextWatcher mTextWaycher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            editStart = et_input_describe.getSelectionStart();
            editEnd = et_input_describe.getSelectionEnd();

            //显示应该输入的剩下的字数
            if (params.equals("describe")) {
                tv_number_limit.setText(1000 - s.toString().length() + "");
            } else if (params.equals("describework")) {
                tv_number_limit.setText(1000 - s.toString().length() + "");
            } else if (params.equals("qq")) {
                tv_number_limit.setText(20 - s.toString().length() + "");
            } else if (params.equals("name")) {
                tv_number_limit.setText(60 - s.toString().length() + "");
            } else if (params.equals("intension")) {
                tv_number_limit.setText(60 - s.toString().length() + "");
            } else if (params.equals("schoolname")) {
                tv_number_limit.setText(60 - s.toString().length() + "");
            } else if (params.equals("major")) {
                tv_number_limit.setText(60 - s.toString().length() + "");
            }else if (params.equals("companyName")) {
                tv_number_limit.setText(30 - s.toString().length() + "");
            }else if (params.equals("workName")){
                tv_number_limit.setText(30 - s.toString().length() + "");
            }else if (params.equals("search")){
                tv_number_limit.setText(60 - s.toString().length() + "");
            }else if (params.equals("base_name")){
                tv_number_limit.setText(60 - s.toString().length() + "");
            }else if(params.equals("skill")){
                tv_number_limit.setText(60 - s.toString().length() + "");
            }else if(params.equals("other")){
                tv_number_limit.setText(1000 - s.toString().length() + "");
            }else if (params.equals("searchername")){
                tv_number_limit.setText(20 - s.toString().length() + "");
            }else if (params.equals("kw")){
                tv_number_limit.setText(20 - s.toString().length() + "");
            }else if(params.equals("describes")){
                tv_number_limit.setText(1000 - s.toString().length() + "");
            }else if(params.equals("projectDiscrube")){
                tv_number_limit.setText(1000 - s.toString().length() + "");
            }else if(params.equals("self_assessment")){
                tv_number_limit.setText(1000 - s.toString().length() + "");
            }else if(params.equals("oneselfDescr")){
                tv_number_limit.setText(400 - s.toString().length() + "");
            }else if(params.equals("base_info_address")){
                tv_number_limit.setText(1000 - s.toString().length() + "");
            }

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        StyledDialog.dismissLoading();
    }
}
