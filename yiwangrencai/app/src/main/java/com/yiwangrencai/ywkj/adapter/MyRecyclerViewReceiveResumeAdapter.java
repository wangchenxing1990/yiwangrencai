package com.yiwangrencai.ywkj.adapter;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyRecyclerViewReceiveResumeAdapter extends BaseRecyclerAdapter<MyRecyclerViewReceiveResumeAdapter.SimpleAdapterViewHolder> implements View.OnClickListener{
    private List<CompanyJobBean> list;
    private Context context;

    public MyRecyclerViewReceiveResumeAdapter(List<CompanyJobBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(SimpleAdapterViewHolder holder, int position, boolean isItem) {
        holder.text_job_title.setText(list.get(position).getJob_title());
        holder.text_company_name.setText(list.get(position).getCompany_name());
        holder.text_updatetime.setText(list.get(position).getUpdatetime());
        holder.text_location_name.setText(list.get(position).getLocation_name());
        holder.text_education_name.setText(list.get(position).getEducation_name());
        holder.text_work_year_name.setText(list.get(position).getWork_year_name());
        holder.text_salary.setText(list.get(position).getSalary());

        holder.itemView.setTag(list.get(position));
    }

    @Override
    public int getAdapterItemViewType(int position) {
        return 0;
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    @Override
    public SimpleAdapterViewHolder getViewHolder(View view) {
        return new SimpleAdapterViewHolder(view, false);
    }
    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.job_select_item, parent, false);
        SimpleAdapterViewHolder vh = new SimpleAdapterViewHolder(v, true);

        v.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener!=null){
            mOnItemClickListener.onItemClick(v, (CompanyJobBean) v.getTag());
        }
    }


    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView text_job_title;
        private TextView text_company_name;
        private TextView text_updatetime;
        private TextView text_location_name;
        private TextView text_education_name;
        private TextView text_work_year_name;
        private TextView text_salary;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            text_job_title = (TextView) itemView.findViewById(R.id.text_job_title);
            text_company_name = (TextView) itemView.findViewById(R.id.text_company_name);
            text_updatetime = (TextView) itemView.findViewById(R.id.text_updatetime);
            text_location_name = (TextView) itemView.findViewById(R.id.text_location_name);
            text_education_name = (TextView) itemView.findViewById(R.id.text_education_name);
            text_work_year_name = (TextView) itemView.findViewById(R.id.text_work_year_name);
            text_salary = (TextView) itemView.findViewById(R.id.text_salary);
        }
    }

    public static interface OnRecyclerViewOnItemClickListener{
        void onItemClick(View v,CompanyJobBean data);
    }

    private OnRecyclerViewOnItemClickListener mOnItemClickListener=null;

    public void setOnItemClickListener(OnRecyclerViewOnItemClickListener listener){
        this.mOnItemClickListener=listener;
    }
}