package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.AreaOne;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */

public class MyWorkThreeAdapter extends DefualtAdapter {
    private List<AreaOne> datas;
    public MyWorkThreeAdapter(List datas) {
        super(datas);
        this.datas=datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(JGApplication.context, R.layout.list_item,null);
            holder.tv= (TextView) convertView.findViewById(R.id.tv_grid_view);
            holder.iv= (ImageView) convertView.findViewById(R.id.iv_next_location);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(datas.get(position).getName());
        holder.iv.setVisibility(View.INVISIBLE);
        return convertView;
    }
    class ViewHolder{
        private TextView tv;
        private ImageView iv;
    }
}
