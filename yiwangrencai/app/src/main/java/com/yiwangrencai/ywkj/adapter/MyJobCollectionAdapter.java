package com.yiwangrencai.ywkj.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.JobCollectionActivity;
import com.yiwangrencai.ywkj.activity.JobInfoActivity;
import com.yiwangrencai.ywkj.bean.CompanyJobBean;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.CircleTransform;

import java.io.IOException;
import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Administrator on 2017/5/13.
 */

public class MyJobCollectionAdapter extends BaseRecyclerAdapter<MyJobCollectionAdapter.MyViewHolder> implements View.OnClickListener {

    private List<CompanyJobBean> list;
     private String api_token;
     private Context context;
    public MyJobCollectionAdapter(List<CompanyJobBean> list,Context context) {
        this.list = list;
        this.context = context;
        SharedPreferences shares=context.getSharedPreferences("Activity",Context.MODE_PRIVATE);
        api_token=shares.getString("api_token","");
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
        return new MyViewHolder(view, false);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_select_item_recycler, parent, false);
        MyViewHolder vh = new MyViewHolder(view, true);
        //   view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position, boolean isItem) {
        holder.text_job_title.setText(list.get(position).getJob_title());
        holder.text_company_name.setText(list.get(position).getCompany_name());
        holder.text_updatetime.setText(list.get(position).getUpdatetime());
        holder.text_location_name.setText(list.get(position).getLocation_name());
        holder.text_education_name.setText(list.get(position).getEducation_name());
        holder.text_work_year_name.setText(list.get(position).getWork_year_name());
        holder.text_salary.setText(list.get(position).getSalary());

        holder.select_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, JobInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", list.get(position).getJob_id());
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             cancelCollectionJob(position);

            }
        });

        if (list.get(position).getPart_status() != null) {

            if (list.get(position).getPart_status().equals("1")) {
                holder.text_part_states.setText("全职");
                holder.text_part_states.setVisibility(View.INVISIBLE);
                //holder.text_part_states.setBackgroundColor();
            } else if (list.get(position).getPart_status().equals("2")) {
                holder.text_part_states.setText("实习 ");
                holder.text_part_states.setTextColor(0xff42bfb6);
                holder.text_part_states.setVisibility(View.VISIBLE);
                holder.text_part_states.setBackgroundResource(R.drawable.shape_ovil_green);
            } else if (list.get(position).getPart_status().equals("3")) {
                holder.text_part_states.setText("兼职 ");
                holder.text_part_states.setTextColor(0xff42bfb6);
                holder.text_part_states.setVisibility(View.VISIBLE);
                holder.text_part_states.setBackgroundResource(R.drawable.shape_ovil_green);
            } else if (list.get(position).getPart_status().equals("4")) {
                holder.text_part_states.setText("临时");
                holder.text_part_states.setTextColor(0xffff0000);
                holder.text_part_states.setVisibility(View.VISIBLE);
                holder.text_part_states.setBackgroundResource(R.drawable.shape_ovil_red);
            } else if (list.get(position).getPart_status().equals("0")) {
                holder.text_part_states.setVisibility(View.INVISIBLE);
            }

        }

        //  holder.itemView.setTag(list.get(position));

    }

    /**
     * 撤销简历
     * @param position
     */
    String str;
    int code;
    private void cancelCollectionJob(final int position) {
        OkHttpClient okHttp=new OkHttpClient();
        FormEncodingBuilder formEnding=new FormEncodingBuilder();
        formEnding.add("com_id", list.get(position).getCom_id());
        formEnding.add("job_id", list.get(position).getJob_id());
        Request request=new Request.Builder().url(ContentUrl.BASE_URL+ContentUrl.FAVORITES_DELETE)
                .addHeader(ContentUrl.ACCEPT,ContentUrl.APPJSON)
                .addHeader(ContentUrl.AUTHORIZATION,ContentUrl.BEAR+api_token)
                .post(formEnding.build())
                .build();
        okHttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                str = response.body().string();
                code = response.code();
             //   Log.i("取消收藏", str);
                handler.sendEmptyMessage(CANCEL_COLLECTION);
                list.remove(position);
            }
        });
    }
    private static final int CANCEL_COLLECTION = 3;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CANCEL_COLLECTION:
                    notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public int getAdapterItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView text_part_states, text_job_title,
                text_company_name, text_updatetime, text_location_name,
                text_education_name, text_work_year_name, text_salary;
        private ImageView image_left;
        private LinearLayout select_main;
        private Button delete;

        public MyViewHolder(View itemView, boolean b) {
            super(itemView);
            text_job_title = (TextView) itemView.findViewById(R.id.text_job_title);
            text_company_name = (TextView) itemView.findViewById(R.id.text_company_name);
            text_updatetime = (TextView) itemView.findViewById(R.id.text_updatetime);
            text_location_name = (TextView) itemView.findViewById(R.id.text_location_name);
            text_education_name = (TextView) itemView.findViewById(R.id.text_education_name);
            text_work_year_name = (TextView) itemView.findViewById(R.id.text_work_year_name);
            text_salary = (TextView) itemView.findViewById(R.id.text_salary);
            text_part_states = (TextView) itemView.findViewById(R.id.text_part_states);
            select_main = (LinearLayout) itemView.findViewById(R.id.select_main);
            delete = (Button) itemView.findViewById(R.id.delete);
            //   image_left= (ImageView) itemView.findViewById(R.id.image_left);

        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (CompanyJobBean) v.getTag());
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, CompanyJobBean data);
    }

    private MyJobCollectionAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(MyJobCollectionAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
