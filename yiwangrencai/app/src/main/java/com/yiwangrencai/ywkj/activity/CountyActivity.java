package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.adapter.MyCountyAdapter;
import com.yiwangrencai.ywkj.bean.AreaCounty;
import com.yiwangrencai.ywkj.bean.AreaLocationBean;
import com.yiwangrencai.ywkj.bean.AreaLocationBeanss;
import com.yiwangrencai.ywkj.bean.LocationAreaBean;
import com.yiwangrencai.ywkj.view.CountDownTimerUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * Created by Administrator on 2017/4/22.
 */

public class CountyActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_area_code;
    }

    @Override
    public void initData() {

    }

    private ListView listView;
    private FrameLayout backCounty;
    private TextView tv_province;
    private TextView text_save;
    private List<LocationAreaBean.DataBean.NextsBeanX.NextsBean> countyList = new ArrayList<>();
    private String params;
    private String paramss;
    private String style;
    private TagContainerLayout textSelectWork;
    @Override
    public void initView() {
        Intent intent = getIntent();
        params = intent.getStringExtra("params");
        paramss = intent.getStringExtra("paramss");
        String next = intent.getStringExtra("next");
        style = intent.getStringExtra("style");
        listArea=intent.getExtras().getStringArrayList("listArea");
        listAreaId=intent.getExtras().getStringArrayList("listAreaId");
        countyList = (List<LocationAreaBean.DataBean.NextsBeanX.NextsBean>) intent.getSerializableExtra("county");

        textSelectWork= (TagContainerLayout) findViewById(R.id.textSelectWork);
        backCounty = (FrameLayout) findViewById(R.id.iv_back_location);
        tv_province = (TextView) findViewById(R.id.tv_province);
        text_save = (TextView) findViewById(R.id.text_save);
        listView = (ListView) findViewById(R.id.list_view);

        if (params.equals("area")) {
            text_save.setVisibility(View.INVISIBLE);
        }
        tv_province.setText(next);
        if ("araes".equals(paramss)){
           // tv_province.setText("工作地区");
            text_save.setVisibility(View.INVISIBLE);
        }else if("cesus".equals(paramss)){
            //tv_province.setText("户籍地区");
            text_save.setVisibility(View.INVISIBLE);
        }
        if ("current_address".equals(style)){
            text_save.setVisibility(View.INVISIBLE);
        }
        text_save.setOnClickListener(this);
        backCounty.setOnClickListener(this);
        listView.setAdapter(new MyCountyAdapter(countyList));

        listView.setOnItemClickListener(myOnItemClickListener);
        if (listArea!=null){
            textSelectWork.setTags(listArea);
        }

        textSelectWork.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                listArea.remove(position);
                listAreaId.remove(position);
                textSelectWork.setTags(listArea);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                listArea.remove(position);
                listAreaId.remove(position);
                textSelectWork.setTags(listArea);
            }
        });
    }

    /**
     * listview数据的条目的展示
     */
    private List<String> listArea = new ArrayList<>();
    private List<String> listAreaId = new ArrayList<>();
    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//            SharedPreferences sharedPreferences=getSharedPreferences("date",MODE_PRIVATE);
//            SharedPreferences.Editor editor=sharedPreferences.edit();
//            editor.putString("location",countyList.get(position).getCid());
//            editor.commit();
            if (params.equals("area")) {
                Intent intent = new Intent();
                intent.putExtra("params", "area");
                intent.putExtra("location", countyList.get(position).getName());
                intent.putExtra("locationId", countyList.get(position).getCid());
                CountyActivity.this.setResult(9, intent);
                finish();
            } else {

                for (int i = 0; i < listArea.size(); i++) {
                    if (listArea.get(i).contains(countyList.get(position).getName())) {
                        Toast.makeText(CountyActivity.this, "您已经添加了,不能重复添加了", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (listArea.size() < 5) {
                    listArea.add(countyList.get(position).getName());
                    listAreaId.add(countyList.get(position).getCid()+"");
                    textSelectWork.setTags(listArea);
                } else {
                    Toast.makeText(CountyActivity.this, "只能添加5个", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_location:
                finish();
                break;
            case R.id.text_save:
                Intent intent = new Intent();
                intent.putExtra("params","");
                Bundle bundle = new Bundle();
                bundle.putSerializable("listArea", (Serializable) listArea);
                bundle.putSerializable("listAreaId", (Serializable) listAreaId);
                intent.putExtras(bundle);
                CountyActivity.this.setResult(9, intent);
                finish();
                break;
        }
    }
}
