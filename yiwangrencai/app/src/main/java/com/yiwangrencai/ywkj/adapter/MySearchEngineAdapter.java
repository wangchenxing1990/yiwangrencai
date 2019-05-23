package com.yiwangrencai.ywkj.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.SearchEnginedActivity;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.bean.SearcherBean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */

public class MySearchEngineAdapter extends BaseRecyclerAdapter<MySearchEngineAdapter.MySearchViewHolder> implements View.OnClickListener {

    private final SearchEnginedActivity context;
    private final List<SearcherBean> list;

    public MySearchEngineAdapter(SearchEnginedActivity context, List<SearcherBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MySearchViewHolder getViewHolder(View view) {
        return new MySearchViewHolder(view);
    }

    @Override
    public MySearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_engine_item, parent, false);
        MySearchViewHolder vh = new MySearchViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(MySearchViewHolder holder, int position, boolean isItem) {
        holder.textviewname.setText(list.get(position).getSearch_name());
        holder.textviewtime.setText(list.get(position).getUpdated_at());

        holder.itemView.setTag(list.get(position));
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }


    class MySearchViewHolder extends RecyclerView.ViewHolder {
        private TextView textviewname;
        private TextView textviewtime;

        public MySearchViewHolder(View itemView) {
            super(itemView);
            textviewname = (TextView) itemView.findViewById(R.id.textviewname);
            textviewtime = (TextView) itemView.findViewById(R.id.textviewtime);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (SearcherBean) v.getTag());
        }
    }

    public static interface OnRecyclerViewOnItemClickListener {
        void onItemClick(View v, SearcherBean data);
    }

    private OnRecyclerViewOnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewOnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}

