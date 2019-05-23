package com.yiwangrencai.ywkj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.PartDetailBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 */

public class MyListPartAdapter extends BaseAdapter {
    private final List<PartDetailBean.DataBean.OtherJobBean> other_job;
    private final Context context;

    public MyListPartAdapter(List<PartDetailBean.DataBean.OtherJobBean> other_job, Context context) {
        this.other_job=other_job;
        this.context=context;
    }

    @Override
    public int getCount() {
        return other_job.size();
    }

    @Override
    public Object getItem(int position) {
        return other_job.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.part_list_item,null);
            holder.textView= (TextView) convertView.findViewById(R.id.text_part_job);

            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(other_job.get(position).getTitle());
        return convertView;
    }
    
    class ViewHolder{
        private TextView textView;
    }
}
