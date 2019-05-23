package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.AreaLocationBeanss;
import com.yiwangrencai.ywkj.bean.AreaOne;
import com.yiwangrencai.ywkj.bean.LocationAreaBean;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */

public class MyListViewAdapter extends DefualtAdapter {
    List<LocationAreaBean.DataBean> datas;
    public MyListViewAdapter(List<LocationAreaBean.DataBean> datas) {
        super(datas);
        this.datas=datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       HolderView holder=null;
        if (convertView==null){
            holder=new HolderView();
            convertView=View.inflate(JGApplication.context, R.layout.list_item,null);
            holder.tv= (TextView) convertView.findViewById(R.id.tv_grid_view);
            holder.iv= (ImageView) convertView.findViewById(R.id.iv_next_location);

            convertView.setTag(holder);
        }else {
            holder= (HolderView) convertView.getTag();
        }

        holder.tv.setText(datas.get(position).getName());

        if (datas.get(position).getNext()==0){
            holder.iv.setVisibility(View.INVISIBLE);
        }else{
            holder.iv.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class HolderView{
        private TextView tv;
        private ImageView iv;
    }

}
