package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * 学历界面学究的展示
 * Created by Administrator on 2017/4/24.
 */

public class MyEducationAdapter extends DefualtAdapter {
    private List<String> datas;

    public MyEducationAdapter(List<String> datas) {
        super(datas);
        this.datas = datas;
    }

    public void setData(List<String> datas,Boolean bool) {
        this.datas = datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(JGApplication.context, R.layout.list_items, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_grid_view);
          //  holder.checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_next_location);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(datas.get(position));

        return convertView;
    }

    class ViewHolder {
        private TextView tv;
        private ImageView imageView;
        private CheckBox checkBox;
    }

}
