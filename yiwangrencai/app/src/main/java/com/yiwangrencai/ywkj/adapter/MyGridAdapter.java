package com.yiwangrencai.ywkj.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.BaseApplication;
import com.yiwangrencai.ywkj.bean.AreaCode;
import com.yiwangrencai.ywkj.jmessage.JGApplication;


import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */

public class MyGridAdapter extends DefualtAdapter {
   private List<AreaCode> datas;
    public MyGridAdapter(List<AreaCode> datas) {
        super(datas);
        this.datas=datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(JGApplication.context, R.layout.grid_item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_grid_view);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(datas.get(position).getName());
        return convertView;
    }

    class ViewHolder {
        private TextView tv;

    }
}
