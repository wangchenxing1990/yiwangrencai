package com.yiwangrencai.ywkj.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.yiwangrencai.ywkj.bean.CarerBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.fragment.FindFragment;
import com.yiwangrencai.ywkj.subPageView.BasePager;
import com.yiwangrencai.ywkj.subPageView.CarerGossipPage;
import com.yiwangrencai.ywkj.subPageView.CarerProjectPage;
import com.yiwangrencai.ywkj.subPageView.EducationTrainPage;
import com.yiwangrencai.ywkj.subPageView.InterviewSkillPage;
import com.yiwangrencai.ywkj.subPageView.LaborLawsPage;
import com.yiwangrencai.ywkj.subPageView.PublicInstitutionPage;
import com.yiwangrencai.ywkj.subPageView.ResumeGuidePage;
import com.yiwangrencai.ywkj.subPageView.SalaryWelfarePage;
import com.yiwangrencai.ywkj.view.ViewPagerIndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/7.
 */

public class FindActivity extends BaseActiviyt {
    @Bind(R.id.image_input_name_back)
    FrameLayout imageInputNameBack;
    @Bind(R.id.tv_input_title)
    TextView tvInputTitle;
    @Bind(R.id.view_pager_indicator)
    ViewPagerIndicator viewPagerIndicator;
    @Bind(R.id.view_pager_find)
    ViewPager viewPagerFind;

    @Override
    public int getLayoutId() {
        return R.layout.activity_find;
    }

    @Override
    public void initData() {

    }

    String str;
    int code;

    @Override
    public void initView() {
        ButterKnife.bind(this);

        gainDataFromService();//从服务器获取数据
    }

    /**
     * 从服务器获取数据
     */
    private void gainDataFromService() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormEncodingBuilder form = new FormEncodingBuilder();
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.CAREER_INFORMATION)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .post(form.build())
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("请求职场资讯的数据成功", str);
                handler.sendEmptyMessage(1000);
            }
        });
    }

    @OnClick(R.id.image_input_name_back)
    public void onViewClicked() {
        finish();//关闭当前界面,返回上一个界面
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    parsGainDataFromService();//解析数据
                    break;
            }
        }
    };

    /**
     * 解析数据
     */
    private List<BasePager> listPages = new ArrayList();
    private List<CarerBean> carerLiset = new ArrayList<CarerBean>();
    private void parsGainDataFromService() {
        if (code == 200) {
            JSONObject jsonObject = JSON.parseObject(str);
            Log.i("获取缓存的数据jsonObject12233",jsonObject+"");
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            carerLiset.clear();
            for (int i = 0; i < jsonArray.size(); i++) {

                CarerBean carerBean = new CarerBean();
                JSONObject jsonArrayData = JSON.parseObject(jsonArray.get(i).toString());
                String category = jsonArrayData.getString("category");
                String name = jsonArrayData.getString("name");
                carerBean.setName(name);
                carerBean.setCategory(category);
                carerLiset.add(carerBean);

            }

            initViewPage();

            //设置indicator的标题
            viewPagerIndicator.setTabItemTitles(carerLiset);
            //设置选中的文本的字体的颜色
            viewPagerIndicator.setCurrentTextColor(Color.parseColor("#ff42bfb6"));
            //设置未被选中的文本的字体的颜色
            viewPagerIndicator.setOtherTextColor(Color.parseColor("#ff555555"));
            //viewPagerFind设置adapter
            viewPagerFind.setAdapter(new MyAdapter());
            //把indicator设置到Viewpager中去
            viewPagerIndicator.setViewPager(viewPagerFind);
        } else {

        }
    }

    /**
     * 初始化子viewpager
     */
    private void initViewPage() {

        // listPages.clear();
        listPages.add(new ResumeGuidePage(FindActivity.this,carerLiset.get(0).getCategory()));
        listPages.add(new InterviewSkillPage(FindActivity.this,carerLiset.get(1).getCategory()));
        listPages.add(new PublicInstitutionPage(FindActivity.this,carerLiset.get(2).getCategory()));
        listPages.add(new CarerGossipPage(FindActivity.this,carerLiset.get(3).getCategory()));
        listPages.add(new SalaryWelfarePage(FindActivity.this,carerLiset.get(4).getCategory()));
        listPages.add(new LaborLawsPage(FindActivity.this,carerLiset.get(5).getCategory()));
        listPages.add(new CarerProjectPage(FindActivity.this,carerLiset.get(6).getCategory()));
        listPages.add(new EducationTrainPage(FindActivity.this,carerLiset.get(7).getCategory()));
//
//        SharedPreferences shared=getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
//        int badges=shared.getInt("badges",0);
//
//        MainActivity mainActivity= (MainActivity) getActivity();
//        mainActivity.gain(badges);

    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return carerLiset.size();

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //主要根据position 和 object 找到 view
            container.removeView((View) object);

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            //获取当前的viewpager
            BasePager currentItemPager = listPages.get(position);
            //当前的viewpager初始化view
            View view = currentItemPager.initView();
            //view.setBackgroundColor(colors[position]);
            container.addView(view);
            //初始化数据
            currentItemPager.initData();

            return view;
        }
    }
}
