package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.AreaCity;
import com.yiwangrencai.ywkj.bean.AreaLocationBeanss;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */

public class MySearchCityAdapter extends DefualtAdapter {
    //private List<AreaCity> datas;
    private List<AreaLocationBeanss.DataBean.NextsBean> datas;
    public MySearchCityAdapter(List<AreaLocationBeanss.DataBean.NextsBean> datas) {
        super(datas);
        this.datas=datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(JGApplication.context, R.layout.item_list,null);
            holder.textview= (TextView) convertView.findViewById(R.id.textarea);

            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.textview.setText(datas.get(position).getName());
        return convertView;
    }
    class ViewHolder{
        private TextView textview;
    }


}
