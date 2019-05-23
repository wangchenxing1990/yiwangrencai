package com.yiwangrencai.ywkj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.CircleTransform;

import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Administrator on 2017/5/13.
 */

public class MyJobSearchAdapter extends BaseRecyclerAdapter<MyJobSearchAdapter.MyViewHolder>implements View.OnClickListener {

    private List<CompanyJobBean> list;
    private Context context;
    public MyJobSearchAdapter(List<CompanyJobBean> list, Context context) {
        this.list = list;
        this.context = context;
    }
    // 重新加载所有数据
    public void reloadAll(List<CompanyJobBean> totaList, boolean isClear) {
        if (isClear) {
            list.clear();
        }
        list.addAll(totaList);
        notifyDataSetChanged();
    }
    public void setData(List<CompanyJobBean> list) {

        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder getViewHolder(View view) {
        return new MyViewHolder(view,false);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.job_search_item,parent,false);
        MyViewHolder vh=new MyViewHolder(view,true);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, boolean isItem) {
     holder.text_job_title.setText(list.get(position).getJob_title());
     holder.text_company_name.setText(list.get(position).getCompany_name());
     holder.text_updatetime.setText(list.get(position).getUpdatetime());
     holder.text_location_name.setText(list.get(position).getLocation_name());
     holder.text_education_name.setText(list.get(position).getEducation_name());
     holder.text_work_year_name.setText(list.get(position).getWork_year_name());
     holder.text_salary.setText(list.get(position).getSalary());
//        Picasso.with(context)
//                .load(ContentUrl.BASE_ICON_URL+list.get(position).getLogo())
//                .placeholder(R.mipmap.avatar_m2x)
//                .error(R.mipmap.avatar_m2x)
//                .transform(new CircleTransform())
//                .into(holder.image_left);

     if (list.get(position).getPart_status()!=null){
         if (list.get(position).getPart_status().equals("1")){
             holder.text_part_states.setText("全职");
             holder.text_part_states.setVisibility(View.INVISIBLE);
              //holder.text_part_states.setBackgroundColor();
         }else if(list.get(position).getPart_status().equals("2")){
             holder.text_part_states.setText("实习 ");
             holder.text_part_states.setTextColor(0xff42bfb6);
             holder.text_part_states.setVisibility(View.VISIBLE);
             holder.text_part_states.setBackgroundResource(R.drawable.shape_ovil_green);
         }else if(list.get(position).getPart_status().equals("3")){
             holder.text_part_states.setText("兼职 ");
             holder.text_part_states.setTextColor(0xff42bfb6);
             holder.text_part_states.setVisibility(View.VISIBLE);
             holder.text_part_states.setBackgroundResource(R.drawable.shape_ovil_green);
         }else if(list.get(position).getPart_status().equals("4")){
             holder.text_part_states.setText("临时");
             holder.text_part_states.setTextColor(0xffff0000);
             holder.text_part_states.setVisibility(View.VISIBLE);
             holder.text_part_states.setBackgroundResource(R.drawable.shape_ovil_red);
         }else if(list.get(position).getPart_status().equals("0")){
             holder.text_part_states.setVisibility(View.INVISIBLE);
         }
     }

      holder.itemView.setTag(list.get(position));

    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView text_part_states,text_job_title,
                text_company_name,text_updatetime,text_location_name,
                text_education_name,text_work_year_name,text_salary;
      //  private ImageView image_left;
        public MyViewHolder(View itemView, boolean b) {
            super(itemView);
            text_job_title= (TextView) itemView.findViewById(R.id.text_job_title);
            text_company_name= (TextView) itemView.findViewById(R.id.text_company_name);
            text_updatetime= (TextView) itemView.findViewById(R.id.text_updatetime);
            text_location_name= (TextView) itemView.findViewById(R.id.text_location_name);
            text_education_name= (TextView) itemView.findViewById(R.id.text_education_name);
            text_work_year_name= (TextView) itemView.findViewById(R.id.text_work_year_name);
            text_salary= (TextView) itemView.findViewById(R.id.text_salary);
            text_part_states= (TextView) itemView.findViewById(R.id.text_part_states);
         //   image_left= (ImageView) itemView.findViewById(R.id.image_left);

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
        void onItemClick(View view, CompanyJobBean data);
    }

    private MyJobSearchAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(MyJobSearchAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
