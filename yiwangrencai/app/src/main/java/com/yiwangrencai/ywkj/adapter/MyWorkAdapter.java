package com.yiwangrencai.ywkj.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.CompanydetailActivity;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */

public class MyWorkAdapter extends BaseRecyclerAdapter<MyWorkAdapter.MyViewHolder> {
    private List<CompanyJobBean> allJobList;
    private CompanydetailActivity companydetailActivity;
    public MyWorkAdapter(List<CompanyJobBean> allJobList, CompanydetailActivity companydetailActivity) {
        this.allJobList=allJobList;
        this.companydetailActivity=companydetailActivity;
    }


    @Override
    public MyViewHolder getViewHolder(View view) {
        MyViewHolder myViewHolder= (MyViewHolder) view.getTag();
        return myViewHolder;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view= LayoutInflater.from(companydetailActivity).inflate(R.layout.job_select_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view,viewType);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, boolean isItem) {

        holder.text_job_title.setText(allJobList.get(position).getJob_title());
        holder.text_company_name.setText(allJobList.get(position).getCompany_name());
        holder.text_updatetime.setText(allJobList.get(position).getUpdatetime());
        holder.text_location_name.setText(allJobList.get(position).getLocation_name());
        holder.text_education_name.setText(allJobList.get(position).getEducation_name());
        holder.text_work_year_name.setText(allJobList.get(position).getWork_year_name());
        holder.text_salary.setText(allJobList.get(position).getSalary());

       // holder.itemView.setTag(allJobList.get(position));

    }

    @Override
    public int getAdapterItemViewType(int position) {
        return 0;
    }

    @Override
    public int getAdapterItemCount() {
        return allJobList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView text_job_title;
        private TextView text_company_name;
        private TextView text_updatetime;
        private TextView text_location_name;
        private TextView text_education_name;
        private TextView text_work_year_name;
        private TextView text_salary;

        public MyViewHolder(View itemView, int viewType) {
            super(itemView);
            text_job_title= (TextView) itemView.findViewById(R.id.text_job_title);
            text_company_name= (TextView) itemView.findViewById(R.id.text_company_name);
            text_updatetime= (TextView) itemView.findViewById(R.id.text_updatetime);
            text_location_name= (TextView) itemView.findViewById(R.id.text_location_name);
            text_education_name= (TextView) itemView.findViewById(R.id.text_education_name);
            text_work_year_name= (TextView) itemView.findViewById(R.id.text_work_year_name);
            text_salary= (TextView) itemView.findViewById(R.id.text_salary);

        }
    }

}
