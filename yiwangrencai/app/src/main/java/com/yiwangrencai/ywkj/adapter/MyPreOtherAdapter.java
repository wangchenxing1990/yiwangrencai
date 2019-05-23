package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.BaseApplication;
import com.yiwangrencai.ywkj.bean.OtherBean;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8.
 */

public class MyPreOtherAdapter extends DefualtAdapter {
    private List<OtherBean> datas;
    public MyPreOtherAdapter(List datas) {
        super(datas);
        this.datas=datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            holder = new MyViewHolder();
            convertView = View.inflate(JGApplication.context, R.layout.language_list, null);
            holder.textview_content = (TextView) convertView.findViewById(R.id.textview_content);
            holder.language_level = (TextView) convertView.findViewById(R.id.language_level);
            holder.textview_right = (TextView) convertView.findViewById(R.id.textview_right);
            holder.view2=convertView.findViewById(R.id.view2);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        holder.textview_content.setText(datas.get(position).getTitle());
        holder.language_level.setText(datas.get(position).getContent());
        holder.textview_right.setVisibility(View.INVISIBLE);
        holder.view2.setVisibility(View.INVISIBLE);
        return convertView;
    }

    class MyViewHolder {
        private TextView textview_content, language_level, textview_right;
        private View view2;
    }
}
