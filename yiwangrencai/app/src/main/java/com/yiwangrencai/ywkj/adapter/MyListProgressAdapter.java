package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.ProgressBean;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */

public class MyListProgressAdapter extends DefualtAdapter {
    private List<ProgressBean> datas;
    public MyListProgressAdapter(List<ProgressBean> datas) {
        super(datas);
        this.datas=datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder=null;
        if (convertView==null){
            holder=new MyViewHolder();
            convertView=View.inflate(JGApplication.context, R.layout.list_view_work_exper,null);
            holder.texttime= (TextView) convertView.findViewById(R.id.texttime);
            holder.texttimeend= (TextView) convertView.findViewById(R.id.texttimeend);
            holder.textworkname= (TextView) convertView.findViewById(R.id.textworkname);
            holder.textcompanyname= (TextView) convertView.findViewById(R.id.textcompanyname);
            holder.textworkarea= (TextView) convertView.findViewById(R.id.textworkarea);
            holder.view_line= (View) convertView.findViewById(R.id.view_line);

            convertView.setTag(holder);
        }else{
            holder= (MyViewHolder) convertView.getTag();
        }
        holder.texttime.setText(datas.get(position).getStarttime_name());
        holder.texttimeend.setText(datas.get(position).getEndtime_name());
        holder.textworkname.setText(datas.get(position).getProject_name());
        holder.textcompanyname.setText(datas.get(position).getPost());
        holder.textworkarea.setVisibility(View.INVISIBLE);
        holder.view_line.setVisibility(View.INVISIBLE);

        return convertView;
    }

    class MyViewHolder{
        private TextView texttime, texttimeend, textworkname, textcompanyname,textworkarea;
        private View view_line;
    }
}
