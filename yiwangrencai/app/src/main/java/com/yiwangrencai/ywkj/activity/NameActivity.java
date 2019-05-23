package com.yiwangrencai.ywkj.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.jmessage.StorageUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/19.
 */

public class NameActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_input_name;
    }

    private String params;
    private String mobile;
    private String intentionjobs;
    private String PHONE="^1\\d{10}$";

    @Override
    public void initData() {
        Intent intent = getIntent();
        params = intent.getStringExtra("params");
        mobile = intent.getStringExtra("mobile");
        //intentionjobs = intent.getStringExtra("intension");

    }

    private FrameLayout nameBack;
    private TextView tv_save;
    private EditText et_input_name;
    private TextView tv_clear_all;
    private TextView tv_number_limit;
    private TextView titleName;
    private TextView text_view_input_number;

    @Override
    public void initView() {

        nameBack = (FrameLayout) findViewById(R.id.image_input_name_back);
        tv_save = (TextView) findViewById(R.id.tv_save);
        et_input_name = (EditText) findViewById(R.id.et_input_name);
        tv_clear_all = (TextView) findViewById(R.id.tv_clear_all);
        tv_number_limit = (TextView) findViewById(R.id.tv_number_limit);
        titleName = (TextView) findViewById(R.id.tv_input_title);
        text_view_input_number = (TextView) findViewById(R.id.text_view_input_number);
        tv_clear_all.setOnClickListener(this);
        nameBack.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        et_input_name.addTextChangedListener(mTextWaycher);
        et_input_name.setFocusable(true);
        et_input_name.setFocusableInTouchMode(true);
        et_input_name.requestFocus();//获取焦点 光标出现


        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(et_input_name.getWindowToken(), 0);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //根据传过来的不同的值显示不同的标题
        switchTitleName();

    }

    /**
     * 根据不同的只显示不同的标题名字
     */
    private void switchTitleName() {

        if (params.equals("phone")) {
            titleName.setText("手机号码");
            tv_number_limit.setText("11");
            text_view_input_number.setText("最多输入11个字/");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
            et_input_name.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (params.equals("email")) {
            titleName.setText("邮箱");
            tv_number_limit.setText("20");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            text_view_input_number.setText("最多输入20个字/");
        } else if (params.equals("qq")) {
            titleName.setText("qq");
            tv_number_limit.setText("20");
            et_input_name.setInputType(InputType.TYPE_CLASS_NUMBER);
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            text_view_input_number.setText("最多输入20个字/");
        } else if (params.equals("name")) {
            titleName.setText("姓名");
            tv_number_limit.setText("20");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            text_view_input_number.setText("最多输入20个字/");
        } else if (params.equals("intension")) {
            titleName.setText("意向工作岗位");
            tv_number_limit.setText("60");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60)});
            text_view_input_number.setText("最多输入60个字/");
        } else if (params.equals("majorr")) {
            titleName.setText("专业名称");
            tv_number_limit.setText("30");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
            text_view_input_number.setText("最多输入30个字/");
        } else if (params.equals("schoolname")) {
            titleName.setText("学校名称");
            tv_number_limit.setText("30");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
            text_view_input_number.setText("最多输入30个字/");
        } else if (params.equals("search")) {
            titleName.setText("职位");
            tv_number_limit.setText("60");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60)});
            text_view_input_number.setText("最多输入60个字/");
        } else if (params.equals("companyName")) {
            titleName.setText("公司名称");
            tv_number_limit.setText("60");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60)});
            text_view_input_number.setText("最多输入60个字/");
        } else if (params.equals("workName")) {
            titleName.setText("职位名称");
            tv_number_limit.setText("60");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60)});
            text_view_input_number.setText("最多输入60个字/");
        } else if (params.equals("base_name")) {
            titleName.setText("姓名");
            tv_number_limit.setText("60");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60)});
            text_view_input_number.setText("最多输入60个字/");
        } else if (params.equals("height")) {
            titleName.setText("身高");
            tv_number_limit.setText("3");
            text_view_input_number.setText("最多输入3个字/");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
            et_input_name.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (params.equals("train")) {
            titleName.setText("培训机构");
            tv_number_limit.setText("30");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
            text_view_input_number.setText("最多输入30个字/");
        } else if (params.equals("project")) {
            titleName.setText("培训项目");
            tv_number_limit.setText("30");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
            text_view_input_number.setText("最多输入30个字/");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        } else if (params.equals("projectName")) {
            titleName.setText("培训项目");
            tv_number_limit.setText("60");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
            text_view_input_number.setText("最多输入60个字/");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60)});
        } else if (params.equals("projectwork")) {
            titleName.setText("担任职位");
            tv_number_limit.setText("60");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60)});
            text_view_input_number.setText("最多输入60个字/");
        } else if (params.equals("skill")) {
            titleName.setText("技能名称");
        } else if (params.equals("other")) {
            titleName.setText("主题");
        } else if (params.equals("book")) {
            titleName.setText("证书");
        } else if (params.equals("kw")) {
            titleName.setText("关键字");
            tv_number_limit.setText("20");
            text_view_input_number.setText("最多输入20个字/");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        } else if (params.equals("searchername")) {
            titleName.setText("搜索器名");
            tv_number_limit.setText("20");
            text_view_input_number.setText("最多输入20个字/");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        }else if(params.equals("partTime")){
            titleName.setText("姓名");
            tv_number_limit.setText("10");
            text_view_input_number.setText("最多输入10个字/");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        }else if(params.equals("wechat")){
            titleName.setText("微信");
            tv_number_limit.setText("17");
            text_view_input_number.setText("最多输入17个字/");
            et_input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(17)});
        }

        et_input_name.setText(mobile);
        if(mobile!=null&&!"".equals(mobile)){
            et_input_name.setSelection(et_input_name.getText().length());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_input_name_back://返回上一个界面
                finish();
                break;
            case R.id.tv_save:
                //  Toast.makeText(NameActivity.this, "点击保存成功", Toast.LENGTH_SHORT).show();
                saveInput();
                break;
            case R.id.tv_clear_all:
                et_input_name.setText("");
                break;
        }
    }

    public  final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    /**
     * 点击保存谁的内容
     */
    private void saveInput() {
        Intent intent = new Intent();
        if (params.equals("name")) {

            intent.putExtra("name", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(10, intent);

        } else if (params.equals("phone")) {

            String phone = et_input_name.getText().toString().trim();
            if (phone == null) {
                Toast.makeText(NameActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.length() != 11) {
                Toast.makeText(NameActivity.this, "手机号码是11位", Toast.LENGTH_SHORT).show();
                return;
            }
            Pattern pattern = Pattern.compile(ContentUrl.PHONE_PARTTEN);
            Matcher matcher = pattern.matcher(phone);
            if (!matcher.matches()) {
                Toast.makeText(NameActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra("phone", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(12, intent);

        } else if (params.equals("email")) {
            String email=et_input_name.getText().toString().trim();
            Pattern pattern = Pattern.compile(REGEX_EMAIL);
            Matcher matcher = pattern.matcher(email);
            if (email==null||email.equals("")){
                Toast.makeText(NameActivity.this, "请输入邮箱号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!matcher.matches()){
                Toast.makeText(NameActivity.this, "请输入正确的邮箱号", Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra("email", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(13, intent);

        } else if (params.equals("qq")) {

            intent.putExtra("qq", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(14, intent);

        } else if (params.equals("intension")) {

            intent.putExtra("intension", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(21, intent);

        } else if (params.equals("schoolname")) {

            intent.putExtra("schoolname", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(30, intent);

        } else if (params.equals("majorr")) {

            intent.putExtra("major", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(31, intent);

        } else if (params.equals("companyName")) {

            intent.putExtra("companyName", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(40, intent);

        } else if (params.equals("workName")) {

            intent.putExtra("workName", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(41, intent);

        } else if (params.equals("search")) {
            intent.putExtra("search", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(70, intent);
        } else if (params.equals("base_name")) {
            intent.putExtra("base_name", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(100, intent);
        } else if (params.equals("height")) {
            intent.putExtra("height", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(101, intent);
        } else if (params.equals("train")) {
            intent.putExtra("train", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(300, intent);
        } else if (params.equals("project")) {
            intent.putExtra("project", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(301, intent);
        } else if (params.equals("projectName")) {
            intent.putExtra("projectName", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(300, intent);
        } else if (params.equals("projectwork")) {
            intent.putExtra("projectwork", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(301, intent);
        } else if (params.equals("skill")) {
            intent.putExtra("skill", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(100, intent);
        } else if (params.equals("other")) {
            intent.putExtra("other", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(200, intent);
        } else if (params.equals("kw")) {
            intent.putExtra("kw", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(17, intent);
        } else if (params.equals("searchername")) {
            intent.putExtra("searchname", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(18, intent);
        } else if (params.equals("book")) {
            intent.putExtra("book", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(400, intent);
        }else if(params.equals("partTime")){
            intent.putExtra("partTime", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(52, intent);
        }else if(params.equals("wechat")){
            intent.putExtra("wechat", et_input_name.getText().toString().trim());
            NameActivity.this.setResult(24, intent);
        }
        finish();
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

            editStart = et_input_name.getSelectionStart();
            editEnd = et_input_name.getSelectionEnd();
            if (temp.length() > 60) {
                s.delete(editStart - 1, editEnd);
                int tempSelector = editEnd;
                et_input_name.setText(temp);
                et_input_name.setSelection(tempSelector);
            }

            //显示应该输入的剩下的字数
            if (params.equals("phone")) {
                tv_number_limit.setText(11 - s.toString().length() + "");
            } else if (params.equals("email")) {
                tv_number_limit.setText(20 - s.toString().length() + "");
            } else if (params.equals("qq")) {
                tv_number_limit.setText(20 - s.toString().length() + "");
            } else if (params.equals("name")) {
                tv_number_limit.setText(20 - s.toString().length() + "");
            } else if (params.equals("intension")) {
                tv_number_limit.setText(60 - s.toString().length() + "");
            } else if (params.equals("schoolname")) {
                tv_number_limit.setText(30 - s.toString().length() + "");
            } else if (params.equals("majorr")) {
                tv_number_limit.setText(30 - s.toString().length() + "");
            } else if (params.equals("companyName")) {
                tv_number_limit.setText(60 - s.toString().length() + "");
            } else if (params.equals("workName")) {
                tv_number_limit.setText(60 - s.toString().length() + "");
            } else if (params.equals("search")) {
                tv_number_limit.setText(60 - s.toString().length() + "");
            } else if (params.equals("base_name")) {
                tv_number_limit.setText(60 - s.toString().length() + "");
            } else if (params.equals("skill")) {
                tv_number_limit.setText(60 - s.toString().length() + "");
            } else if (params.equals("other")) {
                tv_number_limit.setText(60 - s.toString().length() + "");
            } else if (params.equals("searchername")) {
                tv_number_limit.setText(20 - s.toString().length() + "");
            } else if (params.equals("kw")) {
                tv_number_limit.setText(20 - s.toString().length() + "");
            } else if (params.equals("train")) {
                tv_number_limit.setText(30 - s.toString().length() + "");
            } else if (params.equals("project")) {
                tv_number_limit.setText(30 - s.toString().length() + "");
            } else if (params.equals("projectName")) {
                tv_number_limit.setText(60 - s.toString().length() + "");
            } else if (params.equals("projectwork")) {
                tv_number_limit.setText(60 - s.toString().length() + "");
            } else if (params.equals("book")) {
                tv_number_limit.setText(60 - s.toString().length() + "");
            }else if(params.equals("height")){
                tv_number_limit.setText(3 - s.toString().length() + "");
            }else if(params.equals("partTime")){
                tv_number_limit.setText(10 - s.toString().length() + "");
            }else if(params.equals("wechat")){
                tv_number_limit.setText(17 - s.toString().length() + "");
            }

        }
    };
}
