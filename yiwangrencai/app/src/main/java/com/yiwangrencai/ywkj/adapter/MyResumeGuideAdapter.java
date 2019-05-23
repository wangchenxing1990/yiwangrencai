package com.yiwangrencai.ywkj.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.CarerBean;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Administrator on 2017/5/20.
 */

public class MyResumeGuideAdapter extends BaseRecyclerAdapter<MyResumeGuideAdapter.MyViewHolder> implements View.OnClickListener {
    private List<CarerBean> list;
    private Context context;
    public MyResumeGuideAdapter(Context context,List<CarerBean> list) {
        this.list=list;
        this.context=context;
    }

    public void setData(List<CarerBean> list){
        this.list=list;
        //notifyDataSetChanged();
    }

    @Override
    public MyViewHolder getViewHolder(View view) {
        return new MyViewHolder(view);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.resume_guide_item,parent,false);
        MyViewHolder vh=new MyViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, boolean isItem) {
        holder.texttitle.setText(list.get(position).getTitle());
        holder.texttime.setText(list.get(position).getCreated_at()+"  "+"阅读量:"+list.get(position).getReadnum());
        holder.texttitletwo.setText(list.get(position).getTitle());

        if (list.get(position).getImg().isEmpty()){
            Picasso.with(JGApplication.context).load(R.mipmap.cell_com_default2x).into(holder.imageview);
        }else{
            Picasso.with(JGApplication.context).load(ContentUrl.BASE_ICON_URL+list.get(position).getImg()).into(holder.imageview);
        }


        holder.itemView.setTag(list.get(position));
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{
         private ImageView imageview;
         private TextView texttitle,texttime,texttitletwo;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageview= (ImageView) itemView.findViewById(R.id.imageview);
            texttitle= (TextView) itemView.findViewById(R.id.texttitle);
            texttime= (TextView) itemView.findViewById(R.id.texttime);
            texttitletwo= (TextView) itemView.findViewById(R.id.texttitletwo);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (CarerBean) v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, CarerBean data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {

        this.mOnItemClickListener = listener;

    }
}
