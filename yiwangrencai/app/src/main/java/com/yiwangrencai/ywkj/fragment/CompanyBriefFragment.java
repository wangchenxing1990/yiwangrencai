package com.yiwangrencai.ywkj.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.platform.comapi.map.C;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.AddressActivity;
import com.yiwangrencai.ywkj.activity.CompanydetailActivity;
import com.yiwangrencai.ywkj.activity.CompanydetailActivityTwo;
import com.yiwangrencai.ywkj.content.ContentUrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;

/**
 * Created by Administrator on 2017/8/26.
 */

public class CompanyBriefFragment extends Fragment implements View.OnClickListener {
    View view;
    View successView;
    View emptyView;
    String api_token;
    String com_id;
    private FrameLayout framLayout;
    public CompanyBriefFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         SharedPreferences shares = getActivity().getSharedPreferences("Activity", Context.MODE_PRIVATE);
         api_token = shares.getString("api_token", "");
         SharedPreferences sharedPreferences=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
         com_id=sharedPreferences.getString("com_id","");
         framLayout = new FrameLayout(getActivity());
         view=inflater.inflate(R.layout.loading_view,null);
         successView=createSuccessView();

         emptyView=inflater.inflate(R.layout.empty_view_icon,null);
         framLayout.addView(view);
         initdata();//获取数据
        return framLayout;
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    parsData();//解析数据
                    break;
                case 110:
                    framLayout.removeView(view);
                    framLayout.addView(successView);
                    break;
            }
        }
    };

    private void parsData() {
        if (code == 200) {
            JSONObject json = JSON.parseObject(str);
            String codes = json.getString("code");
            if (codes.equals("1")) {
                JSONObject jsonData = json.getJSONObject("data");
                parsdatadata(jsonData);//解析data数据
                framLayout.removeView(view);
                framLayout.addView(successView);
            //    Toast.makeText(getActivity(), "successViewsuccessViewsuccessView", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), json.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    String idid;
    String uid;
    String company_name;
    String brands;
    String temptation;

    private void parsdatadata(JSONObject jsonData) {
        idid = jsonData.getString("id");
        uid = jsonData.getString("uid");
        company_name = jsonData.getString("company_name");
        brands = jsonData.getString("brands");
        temptation = jsonData.getString("temptation");
        String profile = jsonData.getString("profile");
        String contacts = jsonData.getString("contacts");
        String mobile = jsonData.getString("mobile");
        String phone = jsonData.getString("phone");
        String fax = jsonData.getString("fax");
        String email = jsonData.getString("email");
        String qq = jsonData.getString("qq");
        String website = jsonData.getString("website");
        address = jsonData.getString("address");
        String lines = jsonData.getString("lines");
        longitude = jsonData.getString("longitude");
        latitude = jsonData.getString("latitude");
        coordinate_address = jsonData.getString("coordinate_address");
        String logo = jsonData.getString("logo");
        String micro_like = jsonData.getString("micro_like");
        String famous = jsonData.getString("famous");
        String cert_status = jsonData.getString("cert_status");
        String industry_name = jsonData.getString("industry_name");
        String comkind_name = jsonData.getString("comkind_name");
        String region_name = jsonData.getString("region_name");
        String employee_num_name = jsonData.getString("employee_num_name");
        String established_name = jsonData.getString("established_name");

        textcompanyname.setText(company_name);
        texthone.setText(brands);
        textlocation.setText(region_name);
        textscancel.setText(employee_num_name);
        textindustry.setText(comkind_name);
        texthangye.setText(industry_name);
        textaddress.setText(address);
     //   textbycarroad.setText(website);
        textprofile.setText(profile);
        text_estanted_time.setText(established_name);

        String[] welfare = temptation.split(",");
        List<String> listWelfare = new ArrayList<>();
        for (int i = 0; i < welfare.length; i++) {
            if (welfare[i]!=null&&!"".equals(welfare[i])){
                listWelfare.add(welfare[i]);
            }

        }
        if (listWelfare.size()==0){

        }else{
            text_welfare.setTags(listWelfare);
        }

    }

    String str;
    int code;
    private void initdata() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        formEncodingBuilder.add("id", com_id);

        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.COMPANY_BASIC_SHOW)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR+api_token)
                .post(formEncodingBuilder.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
             handler.sendEmptyMessage(110);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("22222222str", str);
                Log.i("22222222code", code + "");
                handler.sendEmptyMessage(100);
            }
        });
    }

    RelativeLayout relativelocation;
    TextView textcompanyname;
    TextView texthone;
    TextView textlocation;
    TextView textscancel;
    TextView textindustry;
    TextView texthangye;
    TextView textaddress;
 //   TextView textbycarroad;
    TextView textprofile;
    private TextView text_estanted_time;
    private TagContainerLayout text_welfare;
    private View  createSuccessView(){
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.framlayout_company_frief,null);
        relativelocation = (RelativeLayout) view.findViewById(R.id.relativelocation);
        textcompanyname = (TextView) view.findViewById(R.id.textcompanyname);
        texthone = (TextView) view.findViewById(R.id.texthone);
        textlocation = (TextView) view.findViewById(R.id.textlocation);
        textscancel = (TextView) view.findViewById(R.id.textscancel);
        textindustry = (TextView) view.findViewById(R.id.textindustry);
        texthangye = (TextView) view.findViewById(R.id.texthangye);
        textaddress = (TextView) view.findViewById(R.id.textaddress);
      //  textbycarroad = (TextView) view.findViewById(R.id.textbycarroad);
        textprofile = (TextView) view.findViewById(R.id.textprofile);

        text_estanted_time = (TextView) view.findViewById(R.id.text_times);
        text_welfare = (TagContainerLayout) view.findViewById(R.id.text_welfare);

        relativelocation.setOnClickListener(this);
        return view;
    }
    String latitude;
    String longitude;
    String coordinate_address;
    String address;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relativelocation:
                Intent intentAdd = new Intent(getActivity(), AddressActivity.class);
                intentAdd.putExtra("address", address);
                intentAdd.putExtra("latitude", latitude);
                intentAdd.putExtra("longitude", longitude);
                intentAdd.putExtra("coordinate_address", coordinate_address);
                startActivity(intentAdd);
                break;
        }
    }
}
