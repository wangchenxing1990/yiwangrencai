package com.yiwangrencai.ywkj.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.AddBlackActivity;
import com.yiwangrencai.ywkj.activity.BlacknumActivity;
import com.yiwangrencai.ywkj.bean.BlackNumBean;
import com.yiwangrencai.ywkj.content.ContentUrl;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/5/9.
 */

public class MyCompayBlackAdapter extends BaseRecyclerAdapter<MyCompayBlackAdapter.MyViewHolder> {
    private final AddBlackActivity context;
    private final List<String> list;
    private List<BlackNumBean> datas;
    private MyViewHolder holder;
    private String api_token;
    private String str;
    int code;

    public MyCompayBlackAdapter(List datas, List list, AddBlackActivity context) {
        this.datas = datas;
        this.list = list;
        this.context = context;
        SharedPreferences share = context.getSharedPreferences("Activity", Context.MODE_PRIVATE);
        api_token = share.getString("api_token", "");
    }

    @Override
    public MyViewHolder getViewHolder(View view) {
        return new MyViewHolder(view);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.textview, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position, boolean isItem) {
        this.holder = holder;
        holder.textview.setText(datas.get(position).getCompany_name());
        if (list.size() == 0) {

        } else {

            for (int i = 0; i < datas.size(); i++) {
            if (list.contains(datas.get(position).getCompany_name())) {
                holder.imageview.setVisibility(View.VISIBLE);
            } else {
                holder.imageview.setVisibility(View.INVISIBLE);
            }
            }

        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("position",position+"");
                OkHttpClient okHttpClient = new OkHttpClient();
                FormEncodingBuilder form = new FormEncodingBuilder();
                form.add("company_uid", datas.get(position).getUid());
                Request request = new Request.Builder()
                        .url(ContentUrl.BASE_URL + ContentUrl.SHIELD_CREATE)
                        .addHeader(ContentUrl.ACCEPT, ContentUrl.APPJSON)
                        .addHeader(ContentUrl.AUTHORIZATION, ContentUrl.BEAR + api_token)
                        .post(form.build())
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        str = response.body().string();
                        code = response.code();
                        if (code == 200) {
                            JSONObject json = JSON.parseObject(str);
                            String codes = json.getString("code");
                            if ("1".equals(codes)) {

                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        holder.imageview.setVisibility(View.VISIBLE);
                                    }
                                });
                                Intent intent=new Intent(context, BlacknumActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
                                context.finish();
                            } else {
                            }
                        } else {
                        }
                    }
                });
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:

                    break;
            }
        }
    };

    @Override
    public int getAdapterItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textview;
        private RelativeLayout linearLayout;
        private ImageView imageview;

        public MyViewHolder(View itemView) {
            super(itemView);
            textview = (TextView) itemView.findViewById(R.id.textview);
            linearLayout = (RelativeLayout) itemView.findViewById(R.id.linearLayout);
            imageview = (ImageView) itemView.findViewById(R.id.imageview);
        }
    }

}
