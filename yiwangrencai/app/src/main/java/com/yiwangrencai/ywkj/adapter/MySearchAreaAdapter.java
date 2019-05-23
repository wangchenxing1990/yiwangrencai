package com.yiwangrencai.ywkj.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.bean.AreaOne;
import com.yiwangrencai.ywkj.jmessage.JGApplication;

import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */

public class MySearchAreaAdapter extends DefualtAdapter {
    private List<String> datas;
    public MySearchAreaAdapter(List<String> datas) {
        super(datas);
        this.datas=datas;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(JGApplication.context, R.layout.item_list,null);
            holder.textarea= (TextView) convertView.findViewById(R.id.textarea);

            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

       // holder.textarea.setText(datas.get(position).getName());
        holder.textarea.setText(datas.get(position));
//        holder.textarea.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               //Toast.makeText(BaseApplication.getApplication(),position,Toast.LENGTH_SHORT).show();
//               Log.i("iiiiiii",position+"");
//           }
//       });
        return convertView;
    }
    class ViewHolder{
        private TextView textarea;
    }
}
