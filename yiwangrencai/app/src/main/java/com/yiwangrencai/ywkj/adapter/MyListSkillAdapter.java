package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.SkillBean;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */

public class MyListSkillAdapter extends DefualtAdapter {
    private List<SkillBean> datas;
    public MyListSkillAdapter(List datas) {
        super(datas);
        this.datas=datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder=null;
        String degree=datas.get(position).getDegree();
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
        holder.view_line.setVisibility(View.INVISIBLE);
        holder.texttime.setVisibility(View.GONE);
        holder.texttimeend.setVisibility(View.GONE);
        holder.textzhiyu.setVisibility(View.GONE);
        holder.textworkname.setText(datas.get(position).getSkillname());
        if (degree.equals("1")){
            holder.textcompanyname.setText("入门");
        }else if(degree.equals("2")){
            holder.textcompanyname.setText("熟练");
        }else if(degree.equals("3")){
            holder.textcompanyname.setText("精通");
        }

        holder.textworkarea.setVisibility(View.INVISIBLE);
        return convertView;
    }

    class MyViewHolder{
        private TextView texttime, texttimeend, textworkname, textcompanyname, textworkarea,textzhiyu;
        private View view_line;
    }
}
