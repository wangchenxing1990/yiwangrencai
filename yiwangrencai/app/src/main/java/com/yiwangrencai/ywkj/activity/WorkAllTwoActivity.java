package com.yiwangrencai.ywkj.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.adapter.MyWorkAllTwoAdapter;
import com.yiwangrencai.ywkj.adapter.MyWorkTwoAdapter;
import com.yiwangrencai.ywkj.bean.AllWorkBean;
import com.yiwangrencai.ywkj.bean.AreaCounty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * Created by Administrator on 2017/10/8.
 */

public class WorkAllTwoActivity extends AppCompatActivity implements View.OnClickListener {
    List<AllWorkBean.DataBean.NextsBeanX> listTwo;
    private FrameLayout iv_back_location;
    private ListView list_view;
    private TextView tv_province;
    private TextView text_save;
    private TagContainerLayout textSelectWork;
    List<String> job_category=new ArrayList<>();
    List<String> job_categoryId=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_code);
        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        job_categoryId=bundle.getStringArrayList("job_categoryId");
        job_category=bundle.getStringArrayList("job_category");
        listTwo= (List<AllWorkBean.DataBean.NextsBeanX>) intent.getSerializableExtra("listTwo");
        tv_province = (TextView) findViewById(R.id.tv_province);
        text_save = (TextView) findViewById(R.id.text_save);
        iv_back_location = (FrameLayout) findViewById(R.id.iv_back_location);
        textSelectWork = (TagContainerLayout) findViewById(R.id.textSelectWork);
        list_view = (ListView) findViewById(R.id.list_view);

        //text_save.setVisibility(View.INVISIBLE);
        iv_back_location.setOnClickListener(this);
        text_save.setOnClickListener(this);

        list_view.setOnItemClickListener(myOnItemClickListener);
        // Log.i("===========", listTwo.size() + "");
        list_view.setAdapter(new MyWorkAllTwoAdapter(listTwo));
        textSelectWork.setTags(job_category);
        textSelectWork.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                job_category.remove(position);
                job_categoryId.remove(position);
                textSelectWork.setTags(job_category);
            }
        });
    }

    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(WorkAllTwoActivity.this,WorkAllThreeActivity.class);
            Bundle bundle=new Bundle();
            bundle.putStringArrayList("job_category", (ArrayList<String>) job_category);
            bundle.putStringArrayList("job_categoryId", (ArrayList<String>) job_categoryId);
            bundle.putSerializable("listThree", (Serializable) listTwo.get(position).getNexts());
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
            case R.id.text_save:
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putStringArrayList("listItem", (ArrayList<String>) job_category);
                bundle.putStringArrayList("cid", (ArrayList<String>) job_categoryId);
                intent.putExtras(bundle);
                setResult(50,intent);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
        } else {
            switch (requestCode) {
                case 50:
                    List<String> listJob = (List<String>) data.getExtras().getSerializable("listItem");
                    List<String> listJobId = (List<String>) data.getExtras().getSerializable("cid");

                    if (listJob.equals("请选择工作岗位")) {

                    } else {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("listItem", (Serializable) listJob);
                        bundle.putSerializable("cid", (Serializable) listJobId);
                        intent.putExtras(bundle);
                        WorkAllTwoActivity.this.setResult(50, intent);
                        finish();
                    }

                    break;
            }
        }
    }
}
