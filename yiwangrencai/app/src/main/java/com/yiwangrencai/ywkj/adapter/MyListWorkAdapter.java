package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.WorkExperience;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */

public class MyListWorkAdapter extends DefualtAdapter {
    private List<WorkExperience> datas;

    public MyListWorkAdapter(List<WorkExperience> datas) {
        super(datas);
        this.datas = datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(JGApplication.context, R.layout.list_view_work_exper, null);
            holder.texttime = (TextView) convertView.findViewById(R.id.texttime);
            holder.texttimeend = (TextView) convertView.findViewById(R.id.texttimeend);
            holder.textworkname = (TextView) convertView.findViewById(R.id.textworkname);
            holder.textcompanyname = (TextView) convertView.findViewById(R.id.textcompanyname);
            holder.textworkarea = (TextView) convertView.findViewById(R.id.textworkarea);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textcompanyname.setText(datas.get(position).getCompany());
        holder.texttime.setText(datas.get(position).getStarttime_name());
        holder.texttimeend.setText(datas.get(position).getEndtime_name());
        holder.textworkname.setText(datas.get(position).getPost());
        holder.textworkarea.setText(datas.get(position).getIndustry_name());
        return convertView;
    }

    class ViewHolder {
        private TextView texttime, texttimeend, textworkname, textcompanyname, textworkarea;
    }
}
