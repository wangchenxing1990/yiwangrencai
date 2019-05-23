package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.adapter.MyGridAdapter;
import com.yiwangrencai.ywkj.adapter.MyListViewAdapter;
import com.yiwangrencai.ywkj.bean.AreaCity;
import com.yiwangrencai.ywkj.bean.AreaCode;
import com.yiwangrencai.ywkj.bean.AreaCounty;
import com.yiwangrencai.ywkj.bean.AreaLocationBeanss;
import com.yiwangrencai.ywkj.bean.AreaOne;
import com.yiwangrencai.ywkj.bean.LocationAreaBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.ViewUtilss;
import com.yiwangrencai.ywkj.view.MyGridView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * Created by Administrator on 2017/4/20.
 */
public class LocationActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_area_code;
    }

    private String string;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    //解析从网上获取的数据
//                    if ("area".equals(params)) {
//
//                    } else {
                        praseJson();
//                    }
                    //读取本地数据
                    String localArea = ViewUtilss.praseLocal(R.raw.area);
                    Log.i("localArealocalArea11",localArea);
                    Gson gson=new Gson();
                    areaLocationBeanss=gson.fromJson(localArea,LocationAreaBean.class);
                    listView.setAdapter(new MyListViewAdapter(areaLocationBeanss.getData()));
                    //解析本地数据
                 //  praseLocalArea(localArea);

                    break;
            }
        }
    };
    LocationAreaBean areaLocationBeanss;
    private List<AreaOne> listAreas = new ArrayList<AreaOne>();
    private List<List<AreaCity>> listAreass = new ArrayList<List<AreaCity>>();
    private List<List<List<AreaCounty>>> listAreasss = new ArrayList<List<List<AreaCounty>>>();
    private JSONArray jsonArrayCity;

    /**
     * 解析数据直辖市和省份的名字
     *
     * @param localArea
     */
    private void praseLocalArea(String localArea) {

        JSONObject jsonObject = JSON.parseObject(localArea);
        JSONArray jsonArrayProvince = jsonObject.getJSONArray("data");
        listAreas.clear();
        listAreass.clear();
        listAreasss.clear();
        List<AreaCity> areaListCityy = null;
        for (int i = 0; i < jsonArrayProvince.size(); i++) {

            AreaOne areaOne = new AreaOne();
            JSONObject jsonObjectt = JSON.parseObject(jsonArrayProvince.getString(i));
            String name = jsonObjectt.getString("name");
            String grade = jsonObjectt.getString("grade");
            String next = jsonObjectt.getString("next");
            String cid = jsonObjectt.getString("cid");

            if (next.equals("1")) {
                jsonArrayCity = jsonObjectt.getJSONArray("nexts");
                //解析各省里面的城市名
                areaListCityy = praseCity(jsonArrayCity);
                listAreass.add(areaListCityy);
            }

            areaOne.setName(name);
            areaOne.setGrade(grade);
            areaOne.setNext(next);
            areaOne.setCid(cid);

            listAreas.add(areaOne);
        }

       // listView.setAdapter(new MyListViewAdapter(listAreas));
    }

    private List<List<AreaCounty>> listCountys = null;

    /**
     * 解析数据各个省内和直辖市里的城市
     *
     * @param jsonArrayCity
     * @return
     */
    public List<AreaCity> praseCity(JSONArray jsonArrayCity) {
        List<AreaCity> areaListCity = new ArrayList<AreaCity>();
        listCountys = new ArrayList<List<AreaCounty>>();
        List<AreaCounty> mapCountyy = new ArrayList<>();
        for (int i = 0; i < jsonArrayCity.size(); i++) {

            AreaCity areaCity = new AreaCity();
            JSONObject jsonObjectCity = JSON.parseObject(jsonArrayCity.getString(i));

            String name = jsonObjectCity.getString("name");
            String grade = jsonObjectCity.getString("grade");
            String next = jsonObjectCity.getString("next");
            String cid = jsonObjectCity.getString("cid");

            areaCity.setName(name);
            areaCity.setNext(next);
            areaCity.setGrade(grade);
            areaCity.setCid(cid);

            if (next.equals("1")) {
                JSONArray jsonArrayCounty = jsonObjectCity.getJSONArray("nexts");
                //解析城市下面的县城和城区
                mapCountyy = praseCounty(jsonArrayCounty);
            } else if (next.equals("0")) {
                mapCountyy = praseNullCoounty();
            }

            areaListCity.add(areaCity);
            listCountys.add(mapCountyy);
        }

        listAreasss.add(listCountys);
        return areaListCity;
    }

    /**
     * 解析城市下面的县城和城区
     *
     * @param jsonArrayCounty
     */
    private List<AreaCounty> praseCounty(JSONArray jsonArrayCounty) {
        List<AreaCounty> mapCounty = new ArrayList<>();
        for (int i = 0; i < jsonArrayCounty.size(); i++) {

            AreaCounty areaCounty = new AreaCounty();
            JSONObject jsonCounty = JSON.parseObject(jsonArrayCounty.getString(i));
            String name = jsonCounty.getString("name");
            String next = jsonCounty.getString("next");
            String nexts = jsonCounty.getString("nexts");
            String cid = jsonCounty.getString("cid");

            areaCounty.setName(name);
            areaCounty.setNext(next);
            areaCounty.setNexts(nexts);
            areaCounty.setCid(cid);

            mapCounty.add(areaCounty);

        }
        return mapCounty;
    }

    /**
     * 解析空的数据
     */
    private List<AreaCounty> praseNullCoounty() {
        AreaCounty areaCounty = new AreaCounty();
        List<AreaCounty> mapCounty = new ArrayList<>();
        areaCounty.setName(null);
        areaCounty.setNext(null);
        areaCounty.setNexts(null);
        mapCounty.add(areaCounty);
        return mapCounty;
    }

    private List<AreaCode> listCode = new ArrayList<AreaCode>();

    /**
     * 解析数据
     */
    private void praseJson() {

        JSONObject jsonObject = JSON.parseObject(string);
        String data = jsonObject.getString("data");
        JSONObject jsonData = JSON.parseObject(data);
        JSONArray jsonArray = jsonData.getJSONArray("nexts");
        String name = jsonData.getString("name");
        tv_city_grid.setText(name);
        listCode.clear();

        for (int i = 0; i < jsonArray.size(); i++) {

            AreaCode areaCode = new AreaCode();
            JSONObject jsonObjectt = jsonArray.getJSONObject(i);
            areaCode.setName(jsonObjectt.getString("name"));
            areaCode.setCid(jsonObjectt.getString("cid"));

            listCode.add(areaCode);

        }

        gridView.setAdapter(new MyGridAdapter(listCode));

    }

    List<String> jobareaa=new ArrayList();
    List<String> jobareaaName=new ArrayList();
    @Override
    public void initData() {
        Intent intent = getIntent();
        params = intent.getStringExtra("params");
        paramss = intent.getStringExtra("paramss");
        paramsss = intent.getStringExtra("paramsss");
        paramsss = intent.getStringExtra("paramssss");
        paramss_name = intent.getStringExtra("paramss_name");
        style = intent.getStringExtra("style");
        listAreaId = intent.getExtras().getStringArrayList("jobareaaId");
        listArea = intent.getExtras().getStringArrayList("jobareaaName");

        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        if ("cesus".equals(paramss_name)) {
            formEncodingBuilder.add("type", "1");
        }
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.AREA_CODE)
                .addHeader("Accept", "application/json")
                .post(formEncodingBuilder.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                string = response.body().string();
                System.out.println("请求的地域的地理位置" + string);
                handler.sendEmptyMessage(1000);
            }
        });
    }



    private MyGridView gridView;
    private ListView listView;
    private FrameLayout backLocation;
    private TextView tv_city_grid;
    private RelativeLayout relative;
    private String params;
    private String paramss;
    private String paramsss;
    private String paramss_name;
    private String style;
    private TextView tv_province;
    private TextView text_save;
    private TagContainerLayout textSelectWork;

    @Override
    public void initView() {

        backLocation = (FrameLayout) findViewById(R.id.iv_back_location);
        tv_province = (TextView) findViewById(R.id.tv_province);
        text_save = (TextView) findViewById(R.id.text_save);
        listView = (ListView) findViewById(R.id.list_view);
        textSelectWork = (TagContainerLayout) findViewById(R.id.textSelectWork);

        if ("areas".equals(paramss)) {
            tv_province.setText("工作地区");
            text_save.setVisibility(View.INVISIBLE);
        } else if ("cesus".equals(paramss)) {
            tv_province.setText("户籍地区");
            text_save.setVisibility(View.INVISIBLE);
        }else {
            tv_province.setText("工作地区");
        }

        if ("current_address".equals(style)){
            text_save.setVisibility(View.INVISIBLE);
            tv_province.setText("现居住地");
        }
        if ("".equals(params)) {
            text_save.setVisibility(View.VISIBLE);
            if (listArea!=null){
                textSelectWork.setTags(listArea);
            }
        }

        /**
         * 工作区域的选择
         */
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

        View header = LayoutInflater.from(this).inflate(R.layout.header_list_view, null);
        gridView = (MyGridView) header.findViewById(R.id.grid_view);
        tv_city_grid = (TextView) header.findViewById(R.id.tv_city_grid);
        relative = (RelativeLayout) header.findViewById(R.id.relative);
        listView.addHeaderView(header);
        //取消点击GridView时出现的背景颜色
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        text_save.setOnClickListener(this);
        backLocation.setOnClickListener(this);
        gridView.setOnItemClickListener(myGridViewOnclickListener);//设置GridView的条目点击监听
        listView.setOnItemClickListener(myListOnclickListener);//设置ListView条目的点击监听事件

        if ("area".equals(params)) {
//            gridView.setVisibility(View.GONE);
//            relative.setVisibility(View.GONE);
        } else {
            gridView.setVisibility(View.VISIBLE);
            relative.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back_location:
                finish();//返回上一个界面
                break;
            case R.id.text_save:

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("listArea", (Serializable) listArea);
                bundle.putSerializable("listAreaId", (Serializable) listAreaId);
                intent.putExtras(bundle);
                LocationActivity.this.setResult(9, intent);

                finish();
                break;
        }

    }

    /**
     * 点击listview的条目的事件
     */
    AdapterView.OnItemClickListener myListOnclickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if ("area".equals(params)) {

                if (areaLocationBeanss.getData().get(position-1).getNext()!=0) {
                    Intent intent = new Intent(LocationActivity.this, CityActivity.class);
                    intent.putExtra("params", params);
                    intent.putExtra("paramss", paramss);
                    intent.putExtra("style", style);
                    intent.putExtra("next",areaLocationBeanss.getData().get(position - 1).getName());
                    intent.putExtra("city", (Serializable) areaLocationBeanss.getData().get(position-1).getNexts());
                    Bundle bundle=new Bundle();
                    bundle.putStringArrayList("listArea", (ArrayList<String>) listArea);
                    bundle.putStringArrayList("listAreaId", (ArrayList<String>) listAreaId);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 9);
                }

            } else {

                if (areaLocationBeanss.getData().get(position-1).getNext()!=0) {

                    if (params.equals("area")) {
                        Intent intent = new Intent(LocationActivity.this, CityActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putStringArrayList("listArea", (ArrayList<String>) listArea);
                        bundle.putStringArrayList("listAreaId", (ArrayList<String>) listAreaId);
                        intent.putExtras(bundle);
                        intent.putExtra("params", params);
                        intent.putExtra("paramss", paramss);
                        intent.putExtra("style", style);
                        intent.putExtra("next",areaLocationBeanss.getData().get(position - 1).getName());
                        intent.putExtra("city", (Serializable) areaLocationBeanss.getData().get(position - 1).getNexts());
                        startActivityForResult(intent, 9);
                    } else {
                        Intent intent = new Intent(LocationActivity.this, CityActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putStringArrayList("listArea", (ArrayList<String>) listArea);
                        bundle.putStringArrayList("listAreaId", (ArrayList<String>) listAreaId);
                        intent.putExtras(bundle);
                        intent.putExtra("params", params);
                        intent.putExtra("paramss", paramss);
                        intent.putExtra("style", style);
                        intent.putExtra("next",areaLocationBeanss.getData().get(position - 1).getName());
                        intent.putExtra("city", (Serializable) areaLocationBeanss.getData().get(position - 1).getNexts());
                        startActivityForResult(intent, 9);
                    }
                }
            }
        }
    };

    /**
     * 点击Gridview返回数据
     */
    private List<String> listArea = new ArrayList<>();
    private List<String> listAreaId = new ArrayList<>();
    AdapterView.OnItemClickListener myGridViewOnclickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if ("cesus".equals(paramss_name)){
                Intent intent = new Intent();
                intent.putExtra("location", listCode.get(position).getName());
                intent.putExtra("locationId", listCode.get(position).getCid());
                LocationActivity.this.setResult(9, intent);
                finish();
            }else {
                if (listArea.contains(listCode.get(position).getName())) {
                    Toast.makeText(LocationActivity.this, "您已经添加了,不能重复添加", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (listArea.size() < 5) {
                    listArea.add(listCode.get(position).getName());
                    listAreaId.add(listCode.get(position).getCid());
                    textSelectWork.setTags(listArea);
                } else {
                    Toast.makeText(LocationActivity.this, "只能选择5个", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    /**
     * 点击返回上一个界面
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
            List<String> listArea = (List<String>) data.getExtras().getSerializable("listArea");
            List<String> listAreaId = (List<String>) data.getExtras().getSerializable("listAreaId");
            String location = data.getExtras().getString("location");
            String locationId = data.getExtras().getString("locationId");
            String params = data.getExtras().getString("params");
            if (params.equals("area")) {
                Intent intent = new Intent();
                intent.putExtra("location", location);
                intent.putExtra("locationId", locationId);
                LocationActivity.this.setResult(9, intent);
                Log.i("locationIdlocationId",locationId+"");
                finish();
            } else {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("listArea", (Serializable) listArea);
                bundle.putSerializable("listAreaId", (Serializable) listAreaId);
                intent.putExtra("params", "");
                intent.putExtras(bundle);
//                intent.putExtra("location", location);
//                intent.putExtra("locationId", locationId);
                LocationActivity.this.setResult(9, intent);
                finish();
            }
        }
    }
}
