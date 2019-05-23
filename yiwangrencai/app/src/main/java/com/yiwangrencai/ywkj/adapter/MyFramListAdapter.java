package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */

public class MyFramListAdapter extends BaseAdapter {
    private List<CompanyJobBean> datas;
    public MyFramListAdapter(List datas) {

        this.datas=datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyFramViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new MyFramViewHolder();
            convertView=View.inflate(JGApplication.context, R.layout.job_select_item,null);
            viewHolder.text_job_title= (TextView) convertView.findViewById(R.id.text_job_title);
            viewHolder.text_company_name= (TextView) convertView.findViewById(R.id.text_company_name);
            viewHolder.text_updatetime= (TextView) convertView.findViewById(R.id.text_updatetime);
            viewHolder.text_location_name= (TextView) convertView.findViewById(R.id.text_location_name);
            viewHolder.text_education_name= (TextView) convertView.findViewById(R.id.text_education_name);
            viewHolder.text_work_year_name= (TextView) convertView.findViewById(R.id.text_work_year_name);
            viewHolder.text_salary= (TextView) convertView.findViewById(R.id.text_salary);

            convertView.setTag(viewHolder);
        }else{

            viewHolder= (MyFramViewHolder) convertView.getTag();

        }

        viewHolder.text_job_title.setText(datas.get(position).getJob_title());
        viewHolder.text_company_name.setText(datas.get(position).getCompany_name());
        viewHolder.text_updatetime.setText(datas.get(position).getUpdatetime());
        viewHolder.text_location_name.setText(datas.get(position).getLocation_name());
        viewHolder.text_education_name.setText(datas.get(position).getEducation_name());
        viewHolder.text_work_year_name.setText(datas.get(position).getWork_year_name());
        viewHolder.text_salary.setText(datas.get(position).getSalary());


        return convertView;
    }

    class MyFramViewHolder{
        private TextView text_job_title,text_company_name,text_updatetime,text_location_name,text_education_name,text_work_year_name,text_salary;
    }
}
