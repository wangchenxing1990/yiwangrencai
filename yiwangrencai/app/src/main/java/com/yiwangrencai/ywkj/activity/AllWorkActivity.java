package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.adapter.MyCityAdapter;
import com.yiwangrencai.ywkj.adapter.MyCityAdapterTwo;
import com.yiwangrencai.ywkj.bean.AllWorkBean;
import com.yiwangrencai.ywkj.bean.AreaCity;
import com.yiwangrencai.ywkj.bean.AreaCounty;
import com.yiwangrencai.ywkj.bean.AreaOne;
import com.yiwangrencai.ywkj.tools.ViewUtilss;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * Created by Administrator on 2017/4/25.
 */

public class AllWorkActivity extends BaseActiviyt implements View.OnClickListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    List<String> job_category=new ArrayList<>();
    List<String> job_categoryId=new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_area_code;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        job_category = bundle.getStringArrayList("job_category");
        job_categoryId = bundle.getStringArrayList("job_categoryId");
        Log.i("job_category",job_category+"");
        Log.i("job_categoryId",job_categoryId+"");
    }

    private TextView tv_province;
    private TextView text_save;
    private FrameLayout iv_back_location;
    private ListView list_view;
    private TagContainerLayout textSelectWork;
    AllWorkBean allWorkBean;

    @Override
    public void initView() {
        //读取本地数据


        tv_province = (TextView) findViewById(R.id.tv_province);
        text_save = (TextView) findViewById(R.id.text_save);
        textSelectWork = (TagContainerLayout) findViewById(R.id.textSelectWork);
        iv_back_location = (FrameLayout) findViewById(R.id.iv_back_location);
        list_view = (ListView) findViewById(R.id.list_view);

       // text_save.setVisibility(View.INVISIBLE);
        text_save.setOnClickListener(this);
        iv_back_location.setOnClickListener(this);
      //  if (job_category.size()!=0){
            textSelectWork.setTags(job_category);
     //   }

        textSelectWork.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                job_category.remove(position);
                job_categoryId.remove(position);
                textSelectWork.setTags(job_category);
            }
        });

        String workAll = new ViewUtilss().getJson(this, "work_all.json");
        Gson gson = new Gson();
        allWorkBean = gson.fromJson(workAll, AllWorkBean.class);

        list_view.setOnItemClickListener(myOnItemClickListener);

        list_view.setAdapter(new MyCityAdapterTwo(allWorkBean.getData()));


    }

    private List<AreaCity> listOne = new ArrayList<>();
    private List<List<AreaCounty>> listtwo = new ArrayList<>();

    /**
     * 解析数据
     */
    private void praseDAta(String data) {
        JSONObject jsonObject = JSON.parseObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        listOne.clear();
        for (int i = 0; i < jsonArray.size(); i++) {
            AreaCity areaCity = new AreaCity();
            JSONObject jsonData = JSON.parseObject(jsonArray.getString(i));
            String name = jsonData.getString("name");
            String grade = jsonData.getString("grade");
            String next = jsonData.getString("next");
            String cid = jsonData.getString("cid");
            areaCity.setName(name);
            areaCity.setGrade(grade);
            areaCity.setNext(next);
            areaCity.setCid(cid);

            listOne.add(areaCity);
            if (next.equals("1")) {
                JSONArray jsonArrayTwo = jsonData.getJSONArray("nexts");
                List<AreaCounty> listTwo = praseJsonArrayTwo(jsonArrayTwo);
                listtwo.add(listTwo);
            }
        }
    }

    List<List<List<AreaOne>>> listthreeee = new ArrayList();

    /**
     * 解析第二层数据
     *
     * @param jsonArrayTwo
     */
    private List<AreaCounty> praseJsonArrayTwo(JSONArray jsonArrayTwo) {
        List<List<AreaOne>> listThree = new ArrayList<>();
        List<AreaOne> listThreee = new ArrayList<>();
        List<AreaCounty> listTwo = new ArrayList<>();
        for (int i = 0; i < jsonArrayTwo.size(); i++) {
            AreaCounty areaCounty = new AreaCounty();
            JSONObject jsonObjectTwo = JSON.parseObject(jsonArrayTwo.getString(i));
            String name = jsonObjectTwo.getString("name");
            String grade = jsonObjectTwo.getString("grade");
            String next = jsonObjectTwo.getString("next");
            String cid = jsonObjectTwo.getString("cid");
            areaCounty.setName(name);
            areaCounty.setNext(next);
            areaCounty.setCid(cid);

            listTwo.add(areaCounty);
            if (next.equals("1")) {
                JSONArray jsonArrayThree = jsonObjectTwo.getJSONArray("nexts");
                listThreee = praseJsonArrayThree(jsonArrayThree);
            }
            listThree.add(listThreee);
        }
        listthreeee.add(listThree);

        return listTwo;
    }

    /**
     * 解析第三层数据
     *
     * @param jsonArrayThree
     */
    private List<AreaOne> praseJsonArrayThree(JSONArray jsonArrayThree) {

        List<AreaOne> listThree = new ArrayList<>();
        for (int i = 0; i < jsonArrayThree.size(); i++) {
            AreaOne areaOne = new AreaOne();
            JSONObject jsonthree = JSON.parseObject(jsonArrayThree.getString(i));
            String name = jsonthree.getString("name");
            String next = jsonthree.getString("next");
            String cid = jsonthree.getString("cid");

            areaOne.setName(name);
            areaOne.setNext(next);
            areaOne.setCid(cid);

            listThree.add(areaOne);
        }

        return listThree;
    }

    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Intent intent = new Intent(AllWorkActivity.this, WorkTwoActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("listTwo", (Serializable) listtwo.get(position));
//            bundle.putSerializable("listTwoo", (Serializable) listthreeee.get(position));
//            intent.putExtras(bundle);
//            startActivityForResult(intent, 50);
            Intent intent = new Intent(AllWorkActivity.this, WorkAllTwoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("job_category", (ArrayList<String>) job_category);
            bundle.putStringArrayList("job_categoryId", (ArrayList<String>) job_categoryId);
            bundle.putSerializable("listTwo", (Serializable) allWorkBean.getData().get(position).getNexts());
            intent.putExtras(bundle);
            startActivityForResult(intent, 50);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_location:
                finish();
                break;
            case R.id.text_save:
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putStringArrayList("listItem", (ArrayList<String>) job_category);
                bundle.putStringArrayList("cid", (ArrayList<String>) job_categoryId);
                intent.putExtras(bundle);
                setResult(50,intent);
                Log.i("job_categoryIdjob_categoryIdqqqqqqqqq",job_categoryId.size()+"");
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            List<String> listJob = (List<String>) data.getExtras().getSerializable("listItem");
            List<String> listJobId = (List<String>) data.getExtras().getSerializable("cid");
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("listItem", (Serializable) listJob);
            bundle.putSerializable("cid", (Serializable) listJobId);
            intent.putExtras(bundle);
            AllWorkActivity.this.setResult(50, intent);
            finish();
        }
    }

}
