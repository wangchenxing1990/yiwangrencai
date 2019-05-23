package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.BaseApplication;
import com.yiwangrencai.ywkj.bean.EducaExperBean;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */

public class MyListEduAdapter extends DefualtAdapter {
    private List<EducaExperBean> datas;
    public MyListEduAdapter(List<EducaExperBean> datas) {
        super(datas);
        this.datas=datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder=null;
        if (convertView==null){
            holder=new MyViewHolder();
            convertView=View.inflate(JGApplication.context, R.layout.list_view_work_exper,null);
            holder.textcompanyname= (TextView) convertView.findViewById(R.id.textcompanyname);
            holder.texttime= (TextView) convertView.findViewById(R.id.texttime);
            holder.texttimeend= (TextView) convertView.findViewById(R.id.texttimeend);
            holder.textworkname= (TextView) convertView.findViewById(R.id.textworkname);
            holder.textworkarea= (TextView) convertView.findViewById(R.id.textworkarea);

            convertView.setTag(holder);
        }else{
            holder= (MyViewHolder) convertView.getTag();
        }

        holder.texttime.setText(datas.get(position).getStarttime_name());
        holder.texttimeend.setText(datas.get(position).getEndtime_name());
        holder.textworkname.setText(datas.get(position).getEducation_name());
        holder.textcompanyname.setText(datas.get(position).getSpeciality());
        holder.textworkarea.setText(datas.get(position).getSchool());

        return convertView;
    }

    class MyViewHolder{
        private TextView texttime, texttimeend, textworkname, textcompanyname, textworkarea;
    }

}
