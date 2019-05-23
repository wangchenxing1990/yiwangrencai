package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.AreaCounty;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */

public class MyWorkTwoAdapter extends DefualtAdapter {
    private List<AreaCounty> datas;
    public MyWorkTwoAdapter(List datas) {
        super(datas);
        this.datas=datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(JGApplication.context, R.layout.list_item,null);
            holder.tv= (TextView) convertView.findViewById(R.id.tv_grid_view);

            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(datas.get(position).getName());
        return convertView;
    }
    class ViewHolder{
        private TextView tv;
    }
}
