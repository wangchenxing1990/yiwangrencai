package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiwangrencai.R;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class MyPartAdapter extends DefualtAdapter {
    List<String> datas;
    public MyPartAdapter(List<String> datas) {
        super(datas);
        this.datas=datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder=new MyViewHolder();
            convertView=View.inflate(parent.getContext(), R.layout.item_list,null);
            viewHolder.textView= (TextView) convertView.findViewById(R.id.textarea);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (MyViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(datas.get(position));
        return convertView;
    }

    class MyViewHolder{
        private TextView  textView;
    }
}
