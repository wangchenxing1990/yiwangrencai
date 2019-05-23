package com.yiwangrencai.ywkj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.InterViewBean;
import com.yiwangrencai.ywkj.bean.NearWorkBean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */

public class MyNearWorkAdapter extends BaseRecyclerAdapter<MyNearWorkAdapter.MyNearViewHolder> implements View.OnClickListener {


    private final Context context;
    private final List<NearWorkBean> list;

    public MyNearWorkAdapter(Context context, List<NearWorkBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyNearViewHolder getViewHolder(View view) {
        return new MyNearViewHolder(view);
    }

    @Override
    public MyNearViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_job_item, parent, false);
        MyNearViewHolder vh = new MyNearViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyNearViewHolder holder, int position, boolean isItem) {
        float distance=Float.parseFloat(list.get(position).getDistance().split("米")[0]);
        if (distance>1000){
            float stance=distance/10;
            Log.i("stance11111",stance+"");
            float stancea=(float) (Math.round(stance))/100;
            Log.i("stance2222",stancea+"");
            holder.textdistance.setText(stancea+"千米");
        }else{
            holder.textdistance.setText(list.get(position).getDistance());
        }

        holder.text.setText(list.get(position).getJob_title());

        holder.textjobtitle.setText(list.get(position).getCompany_name());
        holder.textsalary.setText(list.get(position).getSalary() + "元/月");

        holder.itemView.setTag(list.get(position));
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }


    public class MyNearViewHolder extends RecyclerView.ViewHolder {

        private TextView text;
        private TextView textdistance;
        private TextView textjobtitle;
        private TextView textsalary;

        public MyNearViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            textdistance = (TextView) itemView.findViewById(R.id.textdistance);
            textjobtitle = (TextView) itemView.findViewById(R.id.textjobtitle);
            textsalary = (TextView) itemView.findViewById(R.id.textsalary);
        }
    }

    public static interface OnNearRecyelerViewClickListener {
        void onItemClick(View view, NearWorkBean data);
    }

    private OnNearRecyelerViewClickListener mOnClickListener;

    public void setOnItemClickListener(OnNearRecyelerViewClickListener listener) {
        this.mOnClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener != null) {
            mOnClickListener.onItemClick(v,(NearWorkBean)v.getTag());
        }
    }

}
