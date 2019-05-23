package com.yiwangrencai.ywkj.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.MainActivity;
import com.yiwangrencai.ywkj.bean.CarerBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/1.
 */
@SuppressLint("ValidFragment")
public class FindFragment extends BaseFragment {
    private final Context context;

    public FindFragment(Context context) {
        this.context = context;
    }

    private ViewPagerIndicator viewPagerIndicator;
    private ViewPager viewPagerFind;
    private String string;
    private List<CarerBean> carerLiset = new ArrayList<CarerBean>();
    private ProgressDialog progressDialog;

    @Override
    protected void initData() {

        //显示联网获取数据的状态
        showDialog();

       string = readLoad();

        System.out.println("读取本地文件的数据====="+string);
        if (string!=null){
            System.out.println("++++++++++"+string);
            parseJson();

        }else {

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder().build();
            Request request = new Request.Builder()
                    .url(ContentUrl.BASE_URL + ContentUrl.CAREER_INFORMATION)
                    .addHeader("Accept", "application/json")
                    .post(requestBody)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    string = response.body().string();
                    Log.i("请求职场资讯的数据成功", string);
                    handler.sendEmptyMessage(1000);

                }
            });
        }



    }

    private String readLoad() {
        String path = context.getExternalCacheDir().toString();
        File file = new File(path, "find_0");
        StringWriter sw =null;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferReader = new BufferedReader(fileReader);
            sw= new StringWriter();
            String str = null;
            while ((str = bufferReader.readLine()) != null) {
                sw.write(str);
            }
            System.out.print("----------"+sw.toString());
            sw.flush();
            sw.close();
            return sw.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 显示联网的状态
     */
    private void showDialog() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("正在加载数据中.....");
        progressDialog.show();

    }

    private List<BasePager> listPages = new ArrayList();

    /**
     * 初始化子viewpager
     */
    private void initViewPage() {

       // listPages.clear();
        listPages.add(new ResumeGuidePage(context,carerLiset.get(0).getCategory()));
        listPages.add(new InterviewSkillPage(context,carerLiset.get(1).getCategory()));
        listPages.add(new PublicInstitutionPage(context,carerLiset.get(2).getCategory()));
        listPages.add(new CarerGossipPage(context,carerLiset.get(3).getCategory()));
        listPages.add(new SalaryWelfarePage(context,carerLiset.get(4).getCategory()));
        listPages.add(new LaborLawsPage(context,carerLiset.get(5).getCategory()));
        listPages.add(new CarerProjectPage(context,carerLiset.get(6).getCategory()));
        listPages.add(new EducationTrainPage(context,carerLiset.get(7).getCategory()));

        SharedPreferences shared=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        int badges=shared.getInt("badges",0);

        MainActivity mainActivity= (MainActivity) getActivity();
        mainActivity.gain(badges);

    }

    @Override
    protected View initView() {

        View view = View.inflate(getActivity(), R.layout.find_fragment, null);

        viewPagerIndicator = (ViewPagerIndicator) view.findViewById(R.id.view_pager_indicator);
        viewPagerFind = (ViewPager) view.findViewById(R.id.view_pager_find);

        return view;
    }



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    //保存数据
                    saveLoad(string);
                    //解析数据
                    parseJson();
                    break;
            }
        }
    };

    /**
     * 保存数据
     */
    private void saveLoad(String string) {
        //判断文件是否存在
        String path = context.getExternalCacheDir().toString();
        File file = new File(path, "find_0");

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(string);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e1) {
            e1.printStackTrace();
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

    /**
     * 解析数据
     */
    private void parseJson() {

        JSONObject jsonObject = JSON.parseObject(string);
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
        progressDialog.dismiss();

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
