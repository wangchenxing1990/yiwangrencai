package com.yiwangrencai.ywkj.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/13.
 */

public class MyLookMeAdapter extends BaseRecyclerAdapter<MyLookMeAdapter.MyViewHolder>implements View.OnClickListener {


    private List<CompanyJobBean> list;

    public MyLookMeAdapter(List<CompanyJobBean> list) {
        this.list = list;
    }
    public void setData(List<CompanyJobBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public MyLookMeAdapter.MyViewHolder getViewHolder(View view) {
        return new MyViewHolder(view, false);
    }

    @Override
    public MyLookMeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.look_mine_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view, true);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyLookMeAdapter.MyViewHolder holder, int position, boolean isItem) {
        holder.text_company_name.setText(list.get(position).getCompany_name());
        holder.text_industry_name.setText(list.get(position).getIndustry_name());
        holder.text_region_name.setText(list.get(position).getRegion_name());
        holder.text_time.setText(list.get(position).getTime());
        holder.itemView.setTag(list.get(position));
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView text_company_name, text_industry_name, text_region_name, text_time;

        public MyViewHolder(View itemView, boolean b) {
            super(itemView);
            text_company_name = (TextView) itemView.findViewById(R.id.text_company_name);
            text_industry_name = (TextView) itemView.findViewById(R.id.text_industry_name);
            text_region_name = (TextView) itemView.findViewById(R.id.text_region_name);
            text_time = (TextView) itemView.findViewById(R.id.text_time);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener!=null){
            mOnItemClickListener.onItenClick(v, (CompanyJobBean) v.getTag());
        }
    }


    public static interface OnRecyclerViewOnItemClickListener{
        void onItenClick(View view,CompanyJobBean data);
    }
    private OnRecyclerViewOnItemClickListener mOnItemClickListener=null;
    public void setmOnItemClickListener(OnRecyclerViewOnItemClickListener listener){
        this.mOnItemClickListener=listener;
    }

}
