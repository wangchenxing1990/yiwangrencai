package com.yiwangrencai.ywkj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/5/17.
 */

public class MySalaryAdapter extends DefualtAdapter {
    private final  String saraly;
    List<String> datas;

    public MySalaryAdapter(List<String> datas, String saraly) {
        super(datas);
        this.datas = datas;
        this.saraly=saraly;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = null;
        if (convertView == null) {
            viewholder = new ViewHolder();
            convertView = View.inflate(JGApplication.context, R.layout.salary_item, null);
            viewholder.textView = (TextView) convertView.findViewById(R.id.textView);
            viewholder.imageView = (ImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        if (datas.get(position).equals(saraly)){
            viewholder.imageView.setVisibility(View.VISIBLE);
        }else{
            viewholder.imageView.setVisibility(View.INVISIBLE);
        }
        viewholder.textView.setText(datas.get(position));
        return convertView;
    }

    class ViewHolder {
        private TextView textView;
        private ImageView imageView;
    }
}
