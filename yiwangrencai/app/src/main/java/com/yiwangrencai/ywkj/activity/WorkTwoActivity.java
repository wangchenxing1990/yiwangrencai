package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.adapter.MyWorkTwoAdapter;
import com.yiwangrencai.ywkj.bean.AreaCounty;
import com.yiwangrencai.ywkj.bean.AreaOne;
import com.yiwangrencai.ywkj.view.LoginOffPopuWindow;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */

public class WorkTwoActivity extends BaseActiviyt implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.activity_area_code;
    }

    @Override
    public void initData() {

    }

    private FrameLayout iv_back_location;
    private ListView list_view;
    private TextView tv_province;
    private TextView text_save;
    List<AreaCounty> listTwo;
    List<List<AreaOne>> listTwoo;
    @Override
    public void initView() {
        Intent intent = getIntent();
        listTwo = (List<AreaCounty>) intent.getSerializableExtra("listTwo");
        listTwoo = (List<List<AreaOne>>) intent.getSerializableExtra("listTwoo");

        tv_province = (TextView) findViewById(R.id.tv_province);
        text_save = (TextView) findViewById(R.id.text_save);
        iv_back_location = (FrameLayout) findViewById(R.id.iv_back_location);
        list_view = (ListView) findViewById(R.id.list_view);

        text_save.setVisibility(View.INVISIBLE);
        iv_back_location.setOnClickListener(this);

        list_view.setOnItemClickListener(myOnItemClickListener);
        Log.i("===========", listTwo.size() + "");
        list_view.setAdapter(new MyWorkTwoAdapter(listTwo));
    }

    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener()   {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(WorkTwoActivity.this,WorkThreeActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("workTwoo", (Serializable) listTwoo.get(position));
            intent.putExtras(bundle);
            startActivityForResult(intent,50);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_location:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){}else{
        switch (requestCode){
            case 50:
                List<String> listJob= (List<String>) data.getExtras().getSerializable("listItem");
                List<String> listJobId= (List<String>) data.getExtras().getSerializable("cid");

                if (listJob.equals("请选择工作岗位")){

                }else{
                    Intent intent=new Intent();
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("listItem", (Serializable) listJob);
                    bundle.putSerializable("cid", (Serializable) listJobId);
                    intent.putExtras(bundle);
                    WorkTwoActivity.this.setResult(50,intent);
                    finish();
                }

                break;
        }
        }
    }
}
