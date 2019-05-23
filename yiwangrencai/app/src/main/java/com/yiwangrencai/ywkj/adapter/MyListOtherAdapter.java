package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.OtherBean;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */

public class MyListOtherAdapter extends DefualtAdapter {
    private List<OtherBean> datas;
    public MyListOtherAdapter(List<OtherBean> datas) {
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
            holder.textzhiyu= (TextView) convertView.findViewById(R.id.textzhiyu);
            holder.view_line= (View) convertView.findViewById(R.id.view_line);

            convertView.setTag(holder);
        }else{
            holder= (MyViewHolder) convertView.getTag();
        }

        holder.texttime.setVisibility(View.GONE);
        holder.texttimeend.setVisibility(View.GONE);
        holder.textzhiyu.setVisibility(View.GONE);
        holder.textworkname.setText(datas.get(position).getTitle());
        holder.textcompanyname.setText(datas.get(position).getContent());
        holder.textworkarea.setVisibility(View.INVISIBLE);
        holder.view_line.setVisibility(View.INVISIBLE);

        return convertView;
    }

    class MyViewHolder{
        private TextView texttime, texttimeend, textworkname, textcompanyname, textworkarea,textzhiyu;
        private View view_line;
    }
}
