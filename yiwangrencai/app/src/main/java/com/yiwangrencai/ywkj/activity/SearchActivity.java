package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
//import com.baidu.platform.comapi.map.F;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.InitDatas;
import com.yiwangrencai.ywkj.view.LoginOffPopuWindow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * Created by Administrator on 2017/4/2.
 */
public class SearchActivity extends BaseActiviyt implements View.OnClickListener {


    private FrameLayout back;
    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initData() {

        gainCompanyKeyWord();//获取职位的关键字
        gainJobKeyWord();//获取公司的关键字

    }

    private void gainJobKeyWord() {
        OkHttpClient okHttpClient=new OkHttpClient();
        FormEncodingBuilder formding=new FormEncodingBuilder();
        Request request=new Request.Builder()
                .url(ContentUrl.BASE_URL+"company_job/search_com_hot_word")
                .addHeader(ContentUrl.ACCEPT,ContentUrl.APPJSON)
                .post(formding.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str=response.body().string();
                Log.i("aaaaaaa",str);
                handler.sendEmptyMessage(JOB_HOT);
            }
        });
    }

    private static final int COMPANY_HOT=0;
    private static final int JOB_HOT=1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case COMPANY_HOT:
                    saveCompanyData();//保存公司热门搜索的数据
                    break;
                case JOB_HOT:
                    saveJobData();//保存职位热门搜索的数据
                    break;
            }
        }
    };

    /**
     * 保存职位热门搜索的数据
     */
    private void saveJobData() {
        FileWriter fileWriter=null;
        String path=getExternalCacheDir().toString();
        File file=new File(path,"company_hot");
        try {
            fileWriter=new FileWriter(file);
            fileWriter.write(str);
            fileWriter.flush();
            fileWriter.close();
            Log.i("jobjobjob","保存数据成功");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileWriter!=null){
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String str;
    private String strs;
    private void gainCompanyKeyWord() {
        OkHttpClient okHttpClient=new OkHttpClient();
        FormEncodingBuilder formBuilder=new FormEncodingBuilder();
        Request request=new Request.Builder()
                .url(ContentUrl.BASE_URL+"company_job/search_job_hot_word")
                .addHeader(ContentUrl.AUTHORIZATION,ContentUrl.APPJSON)
                .post(formBuilder.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                strs=response.body().string();
                handler.sendEmptyMessage(COMPANY_HOT);
            }
        });

    }

    private void saveCompanyData() {
        FileWriter fileWriter=null;
        String path= getExternalCacheDir().toString();
        File file=new File(path,"job_hot");
        try {
            fileWriter=new FileWriter(file);
            fileWriter.write(strs);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileWriter!=null){
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private TextView textsearch;
    private TextView textkeywordtype;
    private TagContainerLayout textSearchHistory;
    private TagContainerLayout textSearcher;
    private List<String> lists = new ArrayList<>();
    private LinearLayout linearLayout;
    private LinearLayout textclear;
    private EditText edittext_key;
    private List<String> list = new ArrayList<>();
    private List<String> listCompany = new ArrayList<>();
    private String keyword_type = "1";
    private List<String> listss = new ArrayList<>();

    @Override
    public void initView() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        keywordss = sharedPreferences.getString("search", "");

        String[] strings = keywordss.split(",");
        for (int i = 0; i < strings.length; i++) {
            listss.add(strings[i]);
        }

        String str=readJobData();//读取本地的职位热门搜索
        if (str==null){
            list = InitDatas.initdataSearch();
        }else {
            parsJobhot(str);//解析职位热门搜索的数据
        }

        String string=readCompanyData();//读取本地的公司热门搜索
        if (string==null){
            listCompany = InitDatas.initdataSearchCompany();
        }else{
            parsCompanyhot(string);//解析职位热门搜索的数据
        }

        back = (FrameLayout) findViewById(R.id.search_back);
        textsearch = (TextView) findViewById(R.id.textsearch);
        textkeywordtype = (TextView) findViewById(R.id.textkeywordtype);
        textSearchHistory = (TagContainerLayout) findViewById(R.id.textSearchHistory);
        textSearcher = (TagContainerLayout) findViewById(R.id.textSearcher);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        textclear = (LinearLayout) findViewById(R.id.textclear);
        edittext_key = (EditText) findViewById(R.id.edittext_key);

        textSearcher.setTags(list);
        back.setOnClickListener(this);
        textsearch.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
        textclear.setOnClickListener(this);

        if (listss.get(0).equals("")) {

        } else {
            textSearchHistory.setTags(listss);
        }

        textSearchHistory.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                Intent intentSearch = new Intent(SearchActivity.this, SearchResultActivity.class);
                intentSearch.putExtra("salarylittle", "");
                intentSearch.putExtra("keyword", listss.get(position));
                intentSearch.putExtra("industryname", "");
                intentSearch.putExtra("location", "");
                intentSearch.putExtra("salarylittle", "");
                intentSearch.putExtra("salaryhigh", "");
                intentSearch.putExtra("issuetime", "");
                intentSearch.putExtra("industryId", "");
                intentSearch.putExtra("locationId", "");
                intentSearch.putExtra("issuedataId", "");
                intentSearch.putExtra("work_year", "");
                intentSearch.putExtra("release_date", "");
                startActivity(intentSearch);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

                listss.remove(position);
                textSearchHistory.setTags(listss);

                keywordss = "";
                for (int i = 0; i < listss.size(); i++) {
                    if (i == listss.size() - 1) {
                        keywordss += listss.get(i);
                    } else {
                        keywordss += listss.get(i) + ",";
                    }
                }

                SharedPreferences share = getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor editor = share.edit();
                editor.putString("search", keywordss);
                editor.commit();
            }
        });

        /**
         *热门搜索
         */
        textSearcher.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

                edittext_key.setText(list.get(position));
                Intent intentSearch = new Intent(SearchActivity.this, SearchResultActivity.class);
                intentSearch.putExtra("salarylittle", "");

                if (keyword_type.equals("1")) {
                    intentSearch.putExtra("keyword", list.get(position));
                    intentSearch.putExtra("keyword_type", keyword_type);
                } else if (keyword_type.equals("2")) {
                    intentSearch.putExtra("keyword", list.get(position));
                    intentSearch.putExtra("keyword_type", keyword_type);
                } else if (keyword_type.equals("3")) {
                    intentSearch.putExtra("keyword", listCompany.get(position));
                    intentSearch.putExtra("keyword_type", keyword_type);
                }

                intentSearch.putExtra("industryname", "");
                intentSearch.putExtra("location", "");
                intentSearch.putExtra("salarylittle", "");
                intentSearch.putExtra("salaryhigh", "");
                intentSearch.putExtra("issuetime", "");
                intentSearch.putExtra("industryId", "");
                intentSearch.putExtra("locationId", "");
                intentSearch.putExtra("issuedataId", "");
                intentSearch.putExtra("work_year", "");
                intentSearch.putExtra("release_date", "");
                startActivity(intentSearch);

            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

    }

    /**
     * 解析公司热门搜索的数据
     * @param string
     */
    private void parsCompanyhot(String string) {
        JSONObject jsonObject=JSON.parseObject(string);
        JSONArray jsonData=jsonObject.getJSONArray("data");
        for (int i=0;i<jsonData.size();i++){
            listCompany.add(jsonData.get(i)+"");
            Log.i(i+"",listCompany.get(i));
        }
    }

    /**
     * 读取本地的公司热门搜索的数据
     */
    private String readCompanyData() {
        String path=getExternalCacheDir().toString();
        File file=new File(path,"company_hot");
        FileReader fileReader;
        try {
            fileReader=new FileReader(file);
            BufferedReader br=new BufferedReader(fileReader);
            StringWriter sw=new StringWriter();
            String string=null;
            while ((string = br.readLine())!=null){
                sw.write(string);
            }

            fileReader.close();
            br.close();
            sw.close();
            return sw.toString();
        } catch (Exception e) {

            return null;
        }
    }

    /**
     * 解析 职位热门搜索的数据
     * @param str
     */
    private void parsJobhot(String str) {
        JSONObject jsonObject= JSON.parseObject(str);
        JSONArray jsonData=jsonObject.getJSONArray("data");
        for (int i=0;i<jsonData.size();i++){
            list.add(jsonData.get(i)+"");
            Log.i(i+"",list.get(i));
        }

    }

    /**
     * 读取本地职位热门搜索
     */
    private String readJobData() {
        FileReader fileReader=null;
        String path=getExternalCacheDir().toString();
        File file=new File(path,"job_hot");
        try {
            fileReader=new FileReader(file);
            BufferedReader bw=new BufferedReader(fileReader);
            StringWriter sw=new StringWriter();
            String str=null;
            while ((str = bw.readLine())!=null){
                sw.write(str);
            }
            bw.close();
            sw.close();
           return sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
          return null;
        }
    }

    String keywordss = "";
    private Boolean flag = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_back://返回上一个界面
                finish();
                break;
            case R.id.textsearch:
                String keyword = edittext_key.getText().toString().trim();
                if (keywordss.equals("") && !"".equals(keyword)) {
                    keywordss = keyword;
                } else if (keywordss.equals("") && "".equals(keyword)) {

                } else if(keywordss.contains(keyword)){

                }else {
                    keywordss = keywordss + "," + keyword;
                }

                SharedPreferences shared = getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("search", keywordss);
                editor.commit();

                Intent intentSearch = new Intent(SearchActivity.this, SearchResultActivity.class);
                intentSearch.putExtra("salarylittle", "");
                intentSearch.putExtra("keyword", keyword);
                intentSearch.putExtra("keyword_type", keyword_type);
                intentSearch.putExtra("industryname", "");
                intentSearch.putExtra("location", "");
                intentSearch.putExtra("salarylittle", "");
                intentSearch.putExtra("salaryhigh", "");
                intentSearch.putExtra("issuetime", "");
                intentSearch.putExtra("industryId", "");
                intentSearch.putExtra("locationId", "");
                intentSearch.putExtra("issuedataId", "");
                intentSearch.putExtra("issuedata", "");
                intentSearch.putExtra("work_year", "");
                intentSearch.putExtra("work_yearId", "");
                intentSearch.putExtra("release_date", "");
                intentSearch.putExtra("release_dateId", "");
                startActivity(intentSearch);

                break;
            case R.id.textclear:
                SharedPreferences share = getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor edit = share.edit();
                edit.putString("search", "");
                edit.commit();

                listss.clear();
                keywordss = "";
                textSearchHistory.setTags(listss);

                break;
            case R.id.linearLayout:

                if ( popupWindow!=null&&popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    showPopuwind();//弹出下拉框
                }

                break;
            case R.id.linearJob://点击职位

                popupWindow.dismiss();
                textkeywordtype.setText("职位");
                keyword_type = "2";
                textSearcher.setTags(list);
                flag = true;

                break;
            case R.id.linearcontent://点击全文

                popupWindow.dismiss();
                textkeywordtype.setText("全文");
                keyword_type = "1";
                textSearcher.setTags(list);
                flag = true;

                break;
            case R.id.linearCompany://点击公司

                popupWindow.dismiss();
                textkeywordtype.setText("公司");
                keyword_type = "3";
                textSearcher.setTags(listCompany);
                flag = true;

                break;
        }
    }

    /**
     * 弹出选择下拉框
     */
    PopupWindow popupWindow;
    LinearLayout linearKeyWordStype;
    View contentView;
    LinearLayout linearcontent;
    LinearLayout linearJob;
    LinearLayout linearCompany;

    private void showPopuwind() {

        contentView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.key_word_type, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAsDropDown(findViewById(R.id.linearLayoutTitle), 80, 10);
        linearKeyWordStype = (LinearLayout) contentView.findViewById(R.id.linearLayout);
        linearcontent = (LinearLayout) contentView.findViewById(R.id.linearcontent);
        linearJob = (LinearLayout) contentView.findViewById(R.id.linearJob);
        linearCompany = (LinearLayout) contentView.findViewById(R.id.linearCompany);

        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.setOutsideTouchable(true);
            popupWindow.update();
        } else {

        }

        linearcontent.setOnClickListener(this);
        linearJob.setOnClickListener(this);
        linearCompany.setOnClickListener(this);

    }

}
