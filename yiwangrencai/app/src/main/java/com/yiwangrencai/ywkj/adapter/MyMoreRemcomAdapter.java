package com.yiwangrencai.ywkj.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.bean.SubmitBean;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class MyMoreRemcomAdapter extends BaseRecyclerAdapter<MyMoreRemcomAdapter.MyViewHolder> implements View.OnClickListener {
    private List<CompanyJobBean> list;

    public MyMoreRemcomAdapter(List<CompanyJobBean> listJob) {
        this.list = listJob;
    }

    @Override
    public MyViewHolder getViewHolder(View view) {
        return new MyViewHolder(view, false);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_job_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view, true);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, boolean isItem) {
        holder.textjobtitle.setText(list.get(position).getJob_title());
        holder.textdistance.setText(list.get(position).getLocation_name());
        holder.textcompany.setText(list.get(position).getCompany_name());
        holder.textsalary.setText(list.get(position).getSalary() + "元/月");

        holder.itemView.setTag(list.get(position));
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    public void setData(List<CompanyJobBean> listJob, boolean b) {
        if(b){
            list.addAll(listJob);
        }
        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textjobtitle, textdistance, textcompany, textsalary;

        public MyViewHolder(View itemView, boolean b) {
            super(itemView);
            textjobtitle = (TextView) itemView.findViewById(R.id.text);
            textdistance = (TextView) itemView.findViewById(R.id.textdistance);
            textcompany = (TextView) itemView.findViewById(R.id.textjobtitle);
            textsalary = (TextView) itemView.findViewById(R.id.textsalary);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(CompanyJobBean)v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , CompanyJobBean data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
