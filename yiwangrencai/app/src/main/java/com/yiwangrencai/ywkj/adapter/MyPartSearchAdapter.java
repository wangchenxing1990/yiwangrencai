package com.yiwangrencai.ywkj.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.SearchPartJobBean;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2017/7/1.
 */
public class MyPartSearchAdapter extends BaseRecyclerAdapter<MyPartSearchAdapter.MyViewHolder> implements View.OnClickListener {

    List<SearchPartJobBean.DataBean> list;

    public MyPartSearchAdapter(List<SearchPartJobBean.DataBean> searchPartJobBean) {
        this.list = searchPartJobBean;
    }

    public void setData(List<SearchPartJobBean.DataBean> lists,boolean one) {
        if (one){
            list.addAll(lists);
        }else{
            list.clear();
            list = lists;
        }
        notifyDataSetChanged();
    }

    @Override
    public MyPartSearchAdapter.MyViewHolder getViewHolder(View view) {
        return new MyViewHolder(view);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_part_time,parent,false);
        MyViewHolder  vh=new MyViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, boolean isItem) {

        holder.text_title.setText(list.get(position).getTitle());
        holder.text_today.setText(list.get(position).getTime());
        holder.text_address.setText(list.get(position).getCity_id_name()+"|"+list.get(position).getCompany_name());
        holder.text_salary.setText(list.get(position).getSalary_price()+"元");
        holder.text_time.setText("/"+list.get(position).getSalary_unit_name().split("/")[1]);
        holder.itemView.setTag(list.get(position));
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(SearchPartJobBean.DataBean)v.getTag());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView text_title;
        private TextView text_today;
        private TextView text_address;
        private TextView text_salary;
        private TextView text_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            text_title= (TextView) itemView.findViewById(R.id.text_title);
            text_today= (TextView) itemView.findViewById(R.id.text_today);
            text_address= (TextView) itemView.findViewById(R.id.text_address);
            text_salary= (TextView) itemView.findViewById(R.id.text_salary);
            text_time= (TextView) itemView.findViewById(R.id.text_time);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , SearchPartJobBean.DataBean data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
