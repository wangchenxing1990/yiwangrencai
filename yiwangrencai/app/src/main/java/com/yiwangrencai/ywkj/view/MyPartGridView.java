package com.yiwangrencai.ywkj.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.ChoosePtTimeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */

public class MyPartGridView extends GridView {


    public MyPartGridView(Context context) {
        super(context);

    }

    public MyPartGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MyPartGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    private List<Integer> booleans=new ArrayList<>();
    private List<Integer> status=new ArrayList<>();
    private List<String> list=new ArrayList<>();
    MyGridAdapter adapter;
    private String chooseArray;

    public void initView(String freeTime){
        this.chooseArray=freeTime;

        addHeader();
        addData();
        getChooseList();
        adapter=new MyGridAdapter();
        setAdapter(adapter);


    }
    public void addHeader() {
        String items[] = {"","周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        for (String strText : items) {
            booleans.add(0);
            status.add(0);
            list.add(strText);
        }
       // adapter.notifyDataSetChanged(); //更新数据
    }
    public void addData() {

        String titles[] = {"上午", "中午", "下午"};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
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
       // adapter.notifyDataSetChanged(); //更新数据
    }
    class MyGridAdapter extends BaseAdapter{

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
                Typeface iconfont = Typeface.createFromAsset(getResources().getAssets(), "iconfont.ttf");
                convertView = View.inflate(getContext(), R.layout.item_gridview_choosetime, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.tv_item = (TextView) convertView.findViewById(R.id.tv_item);
                holder.tv_choose = (TextView) convertView.findViewById(R.id.tv_choose);
                holder.ll_item = (RelativeLayout) convertView.findViewById(R.id.ll_item);
                holder.tv_choose.setTypeface(iconfont);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

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
    }

    class ViewHolder {
        private TextView tv_item;
        private TextView tv_choose;
        private RelativeLayout ll_item;
    }

    private void getChooseList(){
        String [] str=chooseArray.split(",");
        for (int i=0;i<str.length;i++){
            if ("1".equals(str[i])){

                status.set(9, 1);
            }else if("2".equals(str[i])){

                status.set(17, 1);
            }else if("3".equals(str[i])){

                status.set(25, 1);
            }else if("4".equals(str[i])){

                status.set(10, 1);
            }else if("5".equals(str[i])){

                status.set(18, 1);
            }else if("6".equals(str[i])){

                status.set(26, 1);
            }else if("7".equals(str[i])){

                status.set(11, 1);
            }else if("8".equals(str[i])){

                status.set(19, 1);
            }else if("9".equals(str[i])){

                status.set(27, 1);
            }else if("10".equals(str[i])){

                status.set(12, 1);
            }else if("11".equals(str[i])){
                status.set(20, 1);
            }else if("12".equals(str[i])){

                status.set(28, 1);
            }else if("13".equals(str[i])){

                status.set(13, 1);
            }else if("14".equals(str[i])){
                status.set(21, 1);
            }else if("15".equals(str[i])){
                status.set(29, 1);
            }else if("16".equals(str[i])){
                status.set(14, 1);
            }else if("17".equals(str[i])){
                status.set(22, 1);
            }else if("18".equals(str[i])){
                status.set(30, 1);
            }else if("19".equals(str[i])){
                status.set(15, 1);
            }else if("20".equals(str[i])){
                status.set(23, 1);
            }else if("21".equals(str[i])){
                status.set(31, 1);
            }else {
                status.set(i,0);
            }
        }

        //adapter.notifyDataSetChanged();
    }
}
