package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.BaseApplication;
import com.yiwangrencai.ywkj.bean.LangBean;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8.
 */

public class MyPreLangAdapter extends DefualtAdapter {
    private List<LangBean> datas;
    public MyPreLangAdapter(List datas) {
        super(datas);
        this.datas=datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        String degree=datas.get(position).getDegree();
        if (convertView==null){
            holder=new MyViewHolder();
            convertView = View.inflate(JGApplication.context, R.layout.language_list, null);
            holder.textview_content = (TextView) convertView.findViewById(R.id.textview_content);
            holder.language_level = (TextView) convertView.findViewById(R.id.language_level);
            holder.textview_right = (TextView) convertView.findViewById(R.id.textview_right);

            convertView.setTag(holder);
        }else {
            holder= (MyViewHolder) convertView.getTag();
        }

        holder.textview_content.setText(datas.get(position).getLanguage_name());
        holder.language_level.setText(datas.get(position).getLevel_name());

        if (degree.equals("1")){
            holder.textview_right.setText("入门");
        }else if(degree.equals("2")){
            holder.textview_right.setText("熟练");
        }else if(degree.equals("3")){
            holder.textview_right.setText("精通");
        }

        return convertView;
    }
    class MyViewHolder{
        private TextView textview_content,language_level,textview_right,textcompanyname,textzhiyu;
       // private View view_line;
    }

}
