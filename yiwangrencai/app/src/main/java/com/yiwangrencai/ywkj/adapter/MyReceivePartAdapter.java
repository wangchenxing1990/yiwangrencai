package com.yiwangrencai.ywkj.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.PartApplyBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/4.
 */

public class MyReceivePartAdapter extends BaseRecyclerAdapter<MyReceivePartAdapter.MyViewHolder> implements View.OnClickListener {
    List<PartApplyBean.DataBean> list;
    private List<PartApplyBean.DataBean> allData;

    public MyReceivePartAdapter(List<PartApplyBean.DataBean> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder getViewHolder(View view) {
        return new MyViewHolder(view);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_submit, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, boolean isItem) {
        int statue = list.get(position).getStatus();
        holder.textcompanyname.setText(list.get(position).getCompany_name());
        holder.textjobname.setText(list.get(position).getTitle());
        holder.textcreatetime.setText(list.get(position).getCreated_at());
        if (statue == 0) {
            holder.textstatus.setText("未处理");
            holder.textstatus.setBackgroundColor(0xfff67c1f);
        } else if (statue == 1) {
            holder.textstatus.setText("已查看");
            holder.textstatus.setBackgroundColor(0xffe3534f);
        } else if (statue == 2) {
            holder.textstatus.setText("同意邀约");
            holder.textstatus.setBackgroundColor(0xff589e46);
        } else if (statue == 3) {
            holder.textstatus.setText("已被婉拒");
            holder.textstatus.setBackgroundColor(0xffcccccc);
        } else if (statue == 4) {
            holder.textstatus.setText("已删除");
            holder.textstatus.setBackgroundColor(0xffcccccc);
        }
        holder.itemView.setTag(list.get(position));
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    public void setData(List<PartApplyBean.DataBean> data, boolean isRefresh) {
        if (isRefresh) {
            list.clear();
            this.list = data;
            notifyDataSetChanged();
        } else {
            this.list = data;
            notifyDataSetChanged();
        }

        notifyDataSetChanged();
    }

    public void setAllData(List<PartApplyBean.DataBean> allData) {
        list.addAll(allData);
        notifyDataSetChanged();
    }

    public interface OnRecyclerViewOnItemClickListener{
        void onItemClick(View view,PartApplyBean.DataBean dataBean);
    }

    private OnRecyclerViewOnItemClickListener mOnRecyclerViewOnItemClickListener=null;
    public void setOnItemClickListener(OnRecyclerViewOnItemClickListener listener){
        this.mOnRecyclerViewOnItemClickListener=listener;
    }
    @Override
    public void onClick(View v) {
        if (mOnRecyclerViewOnItemClickListener!=null){
            mOnRecyclerViewOnItemClickListener.onItemClick(v, (PartApplyBean.DataBean) v.getTag());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textcompanyname, textjobname, textcreatetime, textstatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            textcompanyname = (TextView) itemView.findViewById(R.id.textcompanyname);
            textjobname = (TextView) itemView.findViewById(R.id.textjobname);
            textcreatetime = (TextView) itemView.findViewById(R.id.textcreatetime);
            textstatus = (TextView) itemView.findViewById(R.id.textstatus);
        }
    }
}
