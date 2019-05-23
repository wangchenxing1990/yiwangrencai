package com.yiwangrencai.ywkj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.HomeDataBean;
import com.yiwangrencai.ywkj.bean.HomePager;

import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private final Context context;
    private final List<HomeDataBean.DataBean.JobBean> homeBeanList;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public MyRecyclerAdapter(Context context, List<HomeDataBean.DataBean.JobBean> homeBeanList) {
        this.context = context;
        this.homeBeanList = homeBeanList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }

    }


    public void addData(List<HomeDataBean.DataBean.JobBean> list){
        homeBeanList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item, parent, false);
            view.setOnClickListener(this);
            return new MyViewHolder(view,viewType);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.home_more_job, parent, false);
            view.setOnClickListener(this);
            return new ItemViewHolder(view,viewType);
        }

        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).jobName.setText(homeBeanList.get(position).getJob_title());
            ((MyViewHolder) holder).address.setText(homeBeanList.get(position).getLocation_name());
            ((MyViewHolder) holder).companyName.setText(homeBeanList.get(position).getCompany_name());
            ((MyViewHolder) holder).salary.setText(homeBeanList.get(position).getSalary() + "/月");

            ((MyViewHolder) holder).itemView.setTag(homeBeanList.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return homeBeanList.size() + 1;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (HomeDataBean.DataBean.JobBean) v.getTag());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView jobName;
        private TextView address;
        private TextView companyName;
        private TextView salary;

        public MyViewHolder(final View view, final int position) {
            super(view);
            jobName = (TextView) view.findViewById(R.id.tv_job_name);
            address = (TextView) view.findViewById(R.id.tv_address);
            companyName = (TextView) view.findViewById(R.id.tv_company_name);
            salary = (TextView) view.findViewById(R.id.tv_salary);

        }
    }
 class ItemViewHolder extends RecyclerView.ViewHolder{

     public ItemViewHolder(View itemView,final int position) {
         super(itemView);

     }
 }
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, HomeDataBean.DataBean.JobBean data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
