package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.WorkExperience;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8.
 */

public class MyWorkPreViewAdapter extends DefualtAdapter {
    private List<WorkExperience> datas;

    public MyWorkPreViewAdapter(List<WorkExperience> datas) {
        super(datas);
        this.datas = datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            holder = new MyViewHolder();
            convertView = View.inflate(JGApplication.context, R.layout.list_pre_work_exper, null);
            holder.textstarttime = (TextView) convertView.findViewById(R.id.textstarttime);
            holder.texttimeend = (TextView) convertView.findViewById(R.id.texttimeend);
            holder.textworkname = (TextView) convertView.findViewById(R.id.textworkname);
            holder.textcompanyname = (TextView) convertView.findViewById(R.id.textcompanyname);

            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        holder.textstarttime.setText(datas.get(position).getStarttime_name());
        holder.texttimeend.setText(datas.get(position).getEndtime_name());
        holder.textworkname.setText(datas.get(position).getPost());
        holder.textcompanyname.setText(datas.get(position).getCompany());

        return convertView;
    }

    class MyViewHolder {
        private TextView textstarttime, texttimeend, textworkname, textcompanyname;
    }
}
