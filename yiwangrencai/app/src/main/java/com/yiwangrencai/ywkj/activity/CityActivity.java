package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.adapter.MyCityAdapter;
import com.yiwangrencai.ywkj.bean.AreaCity;
import com.yiwangrencai.ywkj.bean.AreaCounty;
import com.yiwangrencai.ywkj.bean.AreaLocationBeanss;
import com.yiwangrencai.ywkj.bean.LocationAreaBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * Created by Administrator on 2017/4/21.
 */

public class CityActivity extends BaseActiviyt implements View.OnClickListener {

    private FrameLayout backLocation;
    private TextView textProvince;

    @Override
    public int getLayoutId() {
        return R.layout.activity_area_code;
    }

    List<LocationAreaBean.DataBean.NextsBeanX> datas;
    List<List<AreaCounty>> countyList;

    @Override
    public void initData() {

    }

    private ListView listview;
    private TextView tv_province;
    private TextView text_save;
    private TagContainerLayout textSelectWork;
    private String params;
    private String paramss;
    private String city;
    private String style;

    @Override
    public void initView() {
        Intent intent = getIntent();
        final String province = intent.getStringExtra("province");
        params = intent.getStringExtra("params");
        paramss = intent.getStringExtra("paramss");
        datas = (List<LocationAreaBean.DataBean.NextsBeanX>)intent.getSerializableExtra("city");
        city=intent.getStringExtra("next");
        style=intent.getStringExtra("style");
        listArea= intent.getExtras().getStringArrayList("listArea");
        listAreaId= intent.getExtras().getStringArrayList("listAreaId");
        listview = (ListView) findViewById(R.id.list_view);
        backLocation = (FrameLayout) findViewById(R.id.iv_back_location);
        text_save = (TextView) findViewById(R.id.text_save);
        textProvince = (TextView) findViewById(R.id.tv_province);
        textSelectWork = (TagContainerLayout) findViewById(R.id.textSelectWork);

        textProvince.setText(province);
        text_save.setOnClickListener(this);
        backLocation.setOnClickListener(this);
        listview.setAdapter(new MyCityAdapter(datas));
        textProvince.setText(city);

        if ("areas".equals(paramss)) {
            text_save.setVisibility(View.INVISIBLE);
            Log.i("paramss", paramss);

        } else if ("cesus".equals(paramss)) {
            text_save.setVisibility(View.INVISIBLE);
        }

        if ("current_address".equals(style)){
            text_save.setVisibility(View.INVISIBLE);
        }
        if (params.equals("")) {
            text_save.setVisibility(View.VISIBLE);
            textSelectWork.setTags(listArea);
        }

        listview.setOnItemClickListener(myCountyOnClickListener);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_location:
                finish();
                break;
            case R.id.text_save:
                Intent intent = new Intent();
                intent.putExtra("params", "");
                Bundle bundle = new Bundle();
                bundle.putSerializable("listArea", (Serializable) listArea);
                bundle.putSerializable("listAreaId", (Serializable) listAreaId);
                intent.putExtras(bundle);
                CityActivity.this.setResult(9, intent);
                finish();
                break;
        }
    }

    /**
     * 点击条目保存选择区域
     */
    private List<String> listArea = new ArrayList<>();
    private List<String> listAreaId = new ArrayList<>();
    AdapterView.OnItemClickListener myCountyOnClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (datas.get(position).getNext()==1) {

                if (params.equals("area")) {
                    Intent intent = new Intent(CityActivity.this, CountyActivity.class);
                    intent.putExtra("params", params);
                    intent.putExtra("paramss", paramss);
                    intent.putExtra("style", style);
                    intent.putExtra("county", (Serializable) datas.get(position).getNexts());
                    intent.putExtra("next", (Serializable) datas.get(position).getName());
                    Bundle bundle=new Bundle();
                    bundle.putStringArrayList("listArea", (ArrayList<String>) listArea);
                    bundle.putStringArrayList("listAreaId", (ArrayList<String>) listAreaId);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 9);
                } else {
                    Intent intent = new Intent(CityActivity.this, CountyActivity.class);
                    intent.putExtra("params", params);
                    intent.putExtra("paramss", paramss);
                    intent.putExtra("style", style);
                    intent.putExtra("county", (Serializable) datas.get(position).getNexts());
                    intent.putExtra("next", (Serializable) datas.get(position).getName());
                    Bundle bundle=new Bundle();
                    bundle.putStringArrayList("listArea", (ArrayList<String>) listArea);
                    bundle.putStringArrayList("listAreaId", (ArrayList<String>) listAreaId);
                    intent.putExtras(bundle);
                   // intent.putExtra("city", datas.get(position).getName());
                    startActivityForResult(intent, 9);
                }


            } else if (datas.get(position).getNext()==0) {

                if (params.equals("area")) {
                    Intent intent = new Intent();
                    intent.putExtra("params", "area");
                    intent.putExtra("location", datas.get(position).getName());
                    intent.putExtra("locationId", datas.get(position).getCid()+"");
                    Bundle bundle=new Bundle();
                    bundle.putStringArrayList("listArea", (ArrayList<String>) listArea);
                    bundle.putStringArrayList("listAreaId", (ArrayList<String>) listAreaId);
                    intent.putExtras(bundle);
                    Log.i("citycity",datas.get(position).getCid()+"");
//                    intent.putExtra("county", (Serializable) datas.get(position).getNexts());
//                    intent.putExtra("next", (Serializable) datas.get(position).getName());
                    CityActivity.this.setResult(9, intent);
                    finish();
                } else {
                    for (int i = 0; i < listArea.size(); i++) {
                        if (listArea.get(i).contains(datas.get(position).getName())) {
                            Toast.makeText(CityActivity.this, "您已经添加了,不能重复添加", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    if (listArea.size() < 5) {
                        listArea.add(datas.get(position).getName());
                        listAreaId.add(datas.get(position).getCid()+"");
                        textSelectWork.setTags(listArea);
                    } else {
                        Toast.makeText(CityActivity.this, "只能添加5个", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {

        } else {
            switch (resultCode) {
                case 9:

                    List<String> listArea = (List<String>) data.getExtras().getSerializable("listArea");
                    List<String> listAreaId = (List<String>) data.getExtras().getSerializable("listAreaId");
                    String location = data.getExtras().getString("location");
                    String locationId = data.getExtras().getString("locationId");
                    String params = data.getExtras().getString("params");
                    if (params.equals("area")) {
                        Intent intent = new Intent();
                        intent.putExtra("params", "area");
                        intent.putExtra("location", location);
                        intent.putExtra("locationId", locationId);
                        CityActivity.this.setResult(9, intent);
                        finish();
                    } else {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("listArea", (Serializable) listArea);
                        bundle.putSerializable("listAreaId", (Serializable) listAreaId);
                        intent.putExtra("params", "");
                        intent.putExtras(bundle);
                        CityActivity.this.setResult(9, intent);
                        finish();
                    }
                    break;
            }
        }
    }
}
