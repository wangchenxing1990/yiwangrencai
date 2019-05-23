package com.yiwangrencai.ywkj.adapter;

import java.io.IOException;
import java.util.List;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.JobInfoActivity;
import com.yiwangrencai.ywkj.activity.SubmitResumeActivity;
import com.yiwangrencai.ywkj.bean.SubmitBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.MyPopuwindown;

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
import android.widget.Toast;

/**
 * Created by Administrator on 2017/5/12.
 */
public class MySubmitAdapter extends BaseRecyclerAdapter<MySubmitAdapter.MyViewHolder> {
    private List<SubmitBean> list;
    private Context context;
    private String api_token;
    private String str;
    private int code;
    public MySubmitAdapter(List<SubmitBean> list, Context context) {
        this.list = list;
        this.context = context;
        SharedPreferences shared=context.getSharedPreferences("Activity",Context.MODE_PRIVATE);
        api_token=shared.getString("api_token","");
    }

    public void setData(List<SubmitBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MySubmitAdapter.MyViewHolder holder, final int position, boolean isItem) {
        String statue = list.get(position).getStatus();
        holder.textcompanyname.setText(list.get(position).getCompany_name());
        holder.textjobname.setText(list.get(position).getJob_title());
        holder.textcreatetime.setText(list.get(position).getCreated_at());

        if (statue.equals("0")) {
            holder.textstatus.setText("未处理");
            holder.textstatus.setBackgroundColor(0xfff67c1f);
        } else if (statue.equals("1")) {
            holder.textstatus.setText("已查看");
            holder.textstatus.setBackgroundColor(0xffe3534f);
        } else if (statue.equals("2")) {
            holder.textstatus.setText("同意邀约");
            holder.textstatus.setBackgroundColor(0xff589e46);
        } else if (statue.equals("3")) {
            holder.textstatus.setText("已被婉拒");
            holder.textstatus.setBackgroundColor(0xffcccccc);
        } else if (statue.equals("4")) {
            holder.textstatus.setText("已删除");
            holder.textstatus.setBackgroundColor(0xffcccccc);
        }
                //holder.itemView.setTag(list.get(position));
        holder.mian_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, JobInfoActivity.class);
                intent.putExtra("id", list.get(position).getJob_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reapelSubmitResume(position);

            }
        });
    }

    @Override
    public int getAdapterItemViewType(int position) {
        return 0;
    }

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    @Override
    public MyViewHolder getViewHolder(View view) {
        return new MyViewHolder(view, false);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_submit_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view, true);
//        view.setOnClickListener(this);
//        view.setOnLongClickListener(this);
        return vh;
    }

    MyPopuwindown myPopuwindown;

    private void reapelSubmitResume(final int position) {
//        myPopuwindown = new MyPopuwindown(, R.layout.my_popuwindown);
//        myPopuwindown.showAtLocation(SubmitResumeActivity.this.findViewById(R.id.ll_popuwind), Gravity.CENTER, 0, 0);
        OkHttpClient okhttp = new OkHttpClient();
        FormEncodingBuilder foem = new FormEncodingBuilder();
        foem.add("id", String.valueOf(list.get(position).getId()));
        foem.add("status", "4");
        Request request = new Request.Builder()
                .url(ContentUrl.BASE_URL + ContentUrl.RESUME_SEND_UPDATE)
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
                handler.sendEmptyMessage(30);
                list.remove(position);
            }
        });

    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 30:
                    notifyDataSetChanged();
                    break;
            }
        }

    };
    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private TextView textcompanyname, textjobname, textcreatetime, textstatus;
        private Button delete;
        private LinearLayout mian_title;

        public MyViewHolder(View itemView, boolean isItem) {
            super(itemView);
            textcompanyname = (TextView) itemView.findViewById(R.id.textcompanyname);
            textjobname = (TextView) itemView.findViewById(R.id.textjobname);
            textcreatetime = (TextView) itemView.findViewById(R.id.textcreatetime);
            textstatus = (TextView) itemView.findViewById(R.id.textstatus);

            delete = (Button) itemView.findViewById(R.id.delete);
          //  mian_title = (LinearLayout) itemView.findViewById(R.id.mian_title);
            mian_title= (LinearLayout) itemView.findViewById(R.id.mian_title);
//            textstatus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            mian_title.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "点击了条目的位置" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
//                }
//            });
//            delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "点击了删除了的位置" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
//                }
//            });

        }

//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.main:
//
//                    break;
//                case R.id.delete:
//
//                    break;
//            }
//        }
    }

//    @Override
//    public void onClick(View v) {
//        if (mOnItemClickListener != null) {
//            //注意这里使用getTag方法获取数据
//            mOnItemClickListener.onItemClick(v, (SubmitBean) v.getTag());
//        }
//    }

//    public static interface OnRecyclerViewItemClickListener {
//        void onItemClick(View view, SubmitBean data);
//    }
//
//    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
//
//    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
//        this.mOnItemClickListener = listener;
//    }

//    @Override
//    public boolean onLongClick(View v) {
//        if (mOnItemLongClickListener != null) {
//            //注意这里使用getTag方法获取数据
//            mOnItemLongClickListener.onItemLongClick(v, (SubmitBean) v.getTag());
//        }
//        return true;
//    }

//    public static interface OnRecyclerViewItemLongClickListener {
//        void onItemLongClick(View view, SubmitBean data);
//    }
//
//    private OnRecyclerViewItemLongClickListener mOnItemLongClickListener = null;
//
//    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener longlistener) {
//        this.mOnItemLongClickListener = longlistener;
//    }

}
