package com.yiwangrencai.ywkj.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.PartApplyActivity;
import com.yiwangrencai.ywkj.activity.PartDetailActivity;
import com.yiwangrencai.ywkj.bean.PartApplyBean;
import com.yiwangrencai.ywkj.bean.PartApplyBean.DataBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

import org.apache.http.conn.scheme.HostNameResolver;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */
public class MyPartApplyAdapter extends BaseRecyclerAdapter<MyPartApplyAdapter.MyViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private List<DataBean> listBean;
    private Context context;
    private String api_token;
    private String str;
    private int code;
    private final static int REPEAL_APPLY_PART = 4;
    public MyPartApplyAdapter(List<DataBean> listBean,Context context) {
        this.listBean = listBean;
        this.context=context;
        SharedPreferences  shared=context.getSharedPreferences("Activity",Context.MODE_PRIVATE);
        api_token=shared.getString("api_token","");
    }

    public void setData(List<DataBean> list, boolean isRefresh) {
        if (isRefresh) {
            listBean.clear();
            this.listBean = list;
        } else {
            this.listBean = list;
        }
        notifyDataSetChanged();
    }

    public void setLoadmore(List<DataBean> listLoad) {
        listBean.addAll(listLoad);
        notifyDataSetChanged();
    }

    public void removeListItem(int id) {
       // int ids = 0;
        for (int i=0;i<listBean.size();i++){
            if (id==listBean.get(i).getId()){
                //ids=i;
                listBean.remove(i);
            }
            break;
        }

        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder getViewHolder(View view) {
        return new MyViewHolder(view);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_submit_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position, boolean isItem) {
        int statue = listBean.get(position).getStatus();
        holder.textcompanyname.setText(listBean.get(position).getCompany_name());
        holder.textjobname.setText(listBean.get(position).getTitle());
        holder.textcreatetime.setText(listBean.get(position).getCreated_at());

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

        holder.itemView.setTag(listBean.get(position));
        holder.mian_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PartDetailActivity.class);
                intent.putExtra("id",listBean.get(position).getPt_job_id());
                intent.putExtra("params","apply");
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reapelSubmitResume(position);
            }
        });
        //holder.itemView.setOnClickListener(this);
    }

    private void reapelSubmitResume(final int position) {
//        myPopuwindown = new MyPopuwindown(PartApplyActivity.this, R.layout.my_popuwindown);
//        myPopuwindown.showAtLocation(PartApplyActivity.this.findViewById(R.id.ll_popuwind), Gravity.CENTER, 0, 0);
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder foem = new FormEncodingBuilder();
        foem.add("id", String.valueOf(listBean.get(position).getId()));
        foem.add("status", "4");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.PART_SIGN_UPUPDATE)
                .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                .post(foem.build())
                .build();
        okhttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
                Log.i("撤销简历成功的数据", str);
                handler.sendEmptyMessage(REPEAL_APPLY_PART);
                listBean.remove(position);

            }
        });

    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REPEAL_APPLY_PART:
                    notifyDataSetChanged();
                break;
            }
        }
    };

    @Override
    public int getAdapterItemCount() {
        return listBean.size();
    }


    public interface OnRecyclerViewOnItemClickListener {
        void onItemClick(View view, DataBean data);
    }

    private OnRecyclerViewOnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewOnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view, (DataBean) view.getTag());
        }
    }


    public interface OnRecyclerViewItemLongClickListener {
        void onItemLongClick(View view, DataBean data);
    }

    OnRecyclerViewItemLongClickListener mOnItemLongClickListener = null;

    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    /**
     * 长按的点击事件
     *
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemLongClick(v, (DataBean) v.getTag());
        }
        return true;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textcompanyname;
        private TextView textjobname;
        private TextView textcreatetime;
        private TextView textstatus;
        private LinearLayout mian_title;
        private Button delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            textcompanyname = (TextView) itemView.findViewById(R.id.textcompanyname);
            textjobname = (TextView) itemView.findViewById(R.id.textjobname);
            textcreatetime = (TextView) itemView.findViewById(R.id.textcreatetime);
            textstatus = (TextView) itemView.findViewById(R.id.textstatus);
            mian_title = (LinearLayout) itemView.findViewById(R.id.mian_title);
            delete = (Button) itemView.findViewById(R.id.delete);
        }
    }

}
