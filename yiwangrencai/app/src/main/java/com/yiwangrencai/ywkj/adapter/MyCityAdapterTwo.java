package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.AllWorkBean;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/10/8.
 */

public class MyCityAdapterTwo extends BaseAdapter {
    private final List<AllWorkBean.DataBean> data;

    public MyCityAdapterTwo(List<AllWorkBean.DataBean> data) {
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
        holder.tv.setText(data.get(position).getName());

        if (data.get(position).getNext()==0){
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
