package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.BaseApplication;
import com.yiwangrencai.ywkj.bean.LangOptionBean;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */

public class MyLangOptionAdapter extends BaseAdapter {

    private final List<LangOptionBean> optionList;

    public MyLangOptionAdapter(List<LangOptionBean> optionList) {
        this.optionList=optionList;
    }

    @Override
    public int getCount() {
        return optionList.size();
    }

    @Override
    public Object getItem(int position) {
        return optionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
     ViewHolder holder = null;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView = View.inflate(JGApplication.context, R.layout.list_items, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_grid_view);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(optionList.get(position).getOpt_name());

        return convertView;
    }

    class ViewHolder {
        private TextView tv;
    }
}
