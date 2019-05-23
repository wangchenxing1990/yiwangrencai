package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yiwangrencai.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Jeremy on 2017/6/28.
 * 一网科技 JL1n
 */

public class ChoosePtTimeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.gridView_choosePtTime)
    GridView gridViewChoosePtTime;
    @Bind(R.id.fram_job_back)
    FrameLayout framJobBack;
    @Bind(R.id.framLayout)
    FrameLayout framLayout;
    @Bind(R.id.title_height)
    RelativeLayout title_height;
    private List<Integer> booleans;
    private List<Integer> status;
    private TableAdapter adapter;
    private ArrayList<Integer> listChoose;
    private List<String> list;
    private int titleHeight;
    private boolean hasMeasured= false;
    int btWidth;
    int btHeight;
    int oneHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pt_time);
        ButterKnife.bind(this);
       final RelativeLayout title_height = (RelativeLayout) findViewById(R.id.title_height);
//        int w = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        title_height.measure(w, h);
//        btHeight = title_height.getMeasuredHeight();
//        int width = title_height.getMeasuredWidth();
        int btHeight = getTitleHeight();//获取控件的高度(标题栏的高度)
        final int height = getWindownHeight();//屏幕的高度
        final int bar = getStatusBarHeight();//状态栏的高度
        oneHeight = (height - bar - btHeight) / 8;

        Log.i("屏幕的高度",height+"");
        Log.i("状态栏的高度",bar+"");
        Log.i("标题栏的高度",btHeight+"");
        Log.i("oneHeight",oneHeight+"");

        initData();
        initView();
        list = new ArrayList<>();
        booleans = new ArrayList<>();
        status = new ArrayList<>();
        adapter = new TableAdapter();
        listChoose = new ArrayList();
        gridViewChoosePtTime.setAdapter(adapter);
        // 添加消息处理
        gridViewChoosePtTime.setOnItemClickListener(this);
        //添加表头
        addHeader();
        //添加基础数据
        addData();
        if (chooseArray == null || "".equals(chooseArray)) {

        } else {
            getChooseList();
        }
    }

    private void initData() {
        Intent intent = getIntent();
        chooseArray = intent.getStringExtra("free");
    }

    private void initView() {
        //setBackBtn();
        setTitle("时间表");
        //setTitleR("确定");
    }

    String chooseArray = "";

    public void onRBtnClick() {
//        super.onRBtnClick();
        chooseArray = "";
        listChoose.clear();
        for (int i = 0; i < status.size(); i++) {
            if (status.get(i) == 1) {
                listChoose.add(i);
            }
        }

        for (int i = 0; i < listChoose.size(); i++) {
            if (i == listChoose.size() - 1) {
                int chooseNum = getChoose(listChoose.get(i));
                chooseArray = chooseArray + chooseNum;
            } else {
                int chooseNum = getChoose(listChoose.get(i));
                chooseArray = chooseArray + chooseNum + ",";
            }
        }

        Log.i("chooseArraychooseArra", chooseArray);
    }

    public void addHeader() {
        String items[] = {"TIME", "上午", "中午", "下午"};
        for (String strText : items) {
            booleans.add(0);
            status.add(0);
            list.add(strText);
        }
        adapter.notifyDataSetChanged(); //更新数据
    }

    public void addData() {

        String titles[] = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 0) {
                    list.add(titles[i]);
                } else {
//                    list.add("i=" + i + "j=" + j);
                    list.add(" ");
                }
                if (i % 2 == 0) {
                    booleans.add(1);
                } else {
                    booleans.add(2);
                }
                status.add(0);
            }
        }
        // Log.e("size====>", "size:" + status.size());
        adapter.notifyDataSetChanged(); //更新数据
    }

    //清空列表
    public void RemoveAll() {
        list.clear();
        adapter.notifyDataSetChanged();
    }

    //表格点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0 && position != 1 && position != 2 && position != 3 && position != 4 && position != 8 && position != 12 && position != 16 && position != 20 && position != 24 && position != 28) {
            // Toast.makeText(ChoosePtTimeActivity.this, position + "", Toast.LENGTH_SHORT).show();
            if (status.get(position) == 1) {
                status.set(position, 0);
            } else if (status.get(position) == 0) {
                status.set(position, 1);
            }
            adapter.notifyDataSetChanged();
            Log.i("statusstatusstatus", status.get(position) + "");
        }
    }

    @OnClick({R.id.fram_job_back, R.id.framLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fram_job_back://返回
                finish();
                break;
            case R.id.framLayout://确定
                onRBtnClick();
                Intent intent = new Intent();
                intent.putExtra("free", chooseArray);
                ChoosePtTimeActivity.this.setResult(88, intent);
                finish();
                break;
        }
    }

    int windownHeight = 0;

    public int getWindownHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int windownHeight = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        return windownHeight;
    }

    /**
     * 获取控件的高度(标题栏的高度)
     * @return
     */
    public int getTitleHeight() {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        title_height.measure(w, h);
        btHeight = title_height.getMeasuredHeight();
        int width = title_height.getMeasuredWidth();
        return btHeight;
    }

    class TableAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                Typeface iconfont = Typeface.createFromAsset(ChoosePtTimeActivity.this.getAssets(), "iconfont.ttf");
                convertView = View.inflate(ChoosePtTimeActivity.this, R.layout.item_gridview_choosetime, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.tv_item = (TextView) convertView.findViewById(R.id.tv_item);
                holder.tv_choose = (TextView) convertView.findViewById(R.id.tv_choose);
                holder.ll_item = (RelativeLayout) convertView.findViewById(R.id.ll_item);
                holder.tv_choose.setTypeface(iconfont);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ViewGroup.LayoutParams params = holder.tv_item.getLayoutParams();
            params.height = oneHeight - 2;
            holder.tv_item.setLayoutParams(params);

            ViewGroup.LayoutParams paramss = holder.tv_choose.getLayoutParams();
            paramss.height = oneHeight - 2;
            holder.tv_choose.setLayoutParams(paramss);

            holder.tv_item.setText(list.get(position));

            if (booleans.get(position) == 0) {
                //表头颜色
                holder.tv_item.setBackgroundColor(Color.parseColor("#95c5ef"));
                holder.ll_item.setBackgroundColor(Color.WHITE);
            } else if (booleans.get(position) == 1) {
                //奇数行颜色
                holder.tv_item.setBackgroundColor(Color.parseColor("#EAEBEB"));
                holder.ll_item.setBackgroundColor(Color.parseColor("#dadada"));
            } else {
                //偶数行颜色
                holder.tv_item.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.ll_item.setBackgroundColor(Color.parseColor("#dadada"));
            }
            if (status.get(position) == 1) {
                holder.tv_choose.setVisibility(View.VISIBLE);
            } else if (status.get(position) == 0) {
                holder.tv_choose.setVisibility(View.GONE);
            }


            return convertView;
        }

        class ViewHolder {
            private TextView tv_item;
            private TextView tv_choose;
            private RelativeLayout ll_item;
        }
    }

    private int getChoose(int old) {
        int num = 1;
        switch (old) {
            case 5:
                num = 1;
                break;
            case 6:
                num = 2;
                break;
            case 7:
                num = 3;
                break;
            case 9:
                num = 4;
                break;
            case 10:
                num = 5;
                break;
            case 11:
                num = 6;
                break;
            case 13:
                num = 7;
                break;
            case 14:
                num = 8;
                break;
            case 15:
                num = 9;
                break;
            case 17:
                num = 10;
                break;
            case 18:
                num = 11;
                break;
            case 19:
                num = 12;
                break;
            case 21:
                num = 13;
                break;
            case 22:
                num = 14;
                break;
            case 23:
                num = 15;
                break;
            case 25:
                num = 16;
                break;
            case 26:
                num = 17;
                break;
            case 27:
                num = 18;
                break;
            case 29:
                num = 19;
                break;
            case 30:
                num = 20;
                break;
            case 31:
                num = 21;
                break;
        }
        return num;
    }

    private void getChooseList() {
        String[] str = chooseArray.split(",");
        for (int i = 0; i < str.length; i++) {

            if ("1".equals(str[i])) {

                status.set(5, 1);
            } else if ("2".equals(str[i])) {

                status.set(6, 1);
            } else if ("3".equals(str[i])) {

                status.set(7, 1);
            } else if ("4".equals(str[i])) {

                status.set(9, 1);
            } else if ("5".equals(str[i])) {

                status.set(10, 1);
            } else if ("6".equals(str[i])) {

                status.set(11, 1);
            } else if ("7".equals(str[i])) {

                status.set(13, 1);
            } else if ("8".equals(str[i])) {

                status.set(14, 1);
            } else if ("9".equals(str[i])) {

                status.set(15, 1);
            } else if ("10".equals(str[i])) {

                status.set(17, 1);
            } else if ("11".equals(str[i])) {
                // listChoose.add(18);
                //status.add(1);
                status.set(18, 1);
            } else if ("12".equals(str[i])) {

                status.set(19, 1);
            } else if ("13".equals(str[i])) {
                // listChoose.add(21);
                // status.add(1);
                status.set(21, 1);
            } else if ("14".equals(str[i])) {


                status.set(22, 1);
            } else if ("15".equals(str[i])) {

                status.set(23, 1);
            } else if ("16".equals(str[i])) {

                status.set(25, 1);
            } else if ("17".equals(str[i])) {

                status.set(26, 1);
            } else if ("18".equals(str[i])) {

                status.set(27, 1);
            } else if ("19".equals(str[i])) {

                status.set(29, 1);
            } else if ("20".equals(str[i])) {

                status.set(30, 1);
            } else if ("21".equals(str[i])) {

                status.set(31, 1);
            } else {
                status.set(i, 0);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private int getStatusBarHeight() {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = this.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }
}
