package com.yiwangrencai.ywkj.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yiwangrencai.R;
import com.yiwangrencai.wxapi.WXEntryActivity;
import com.yiwangrencai.ywkj.activity.BlacknumActivity;
import com.yiwangrencai.ywkj.activity.EditPartJobActivity;
import com.yiwangrencai.ywkj.activity.EditResumeActivity;
import com.yiwangrencai.ywkj.activity.EditResumeTwoActivity;
import com.yiwangrencai.ywkj.activity.MainActivity;
import com.yiwangrencai.ywkj.activity.PartJobActivity;
import com.yiwangrencai.ywkj.activity.PreviewResumeActivityTwo;
import com.yiwangrencai.ywkj.activity.RegisterActivity;
import com.yiwangrencai.ywkj.activity.ResumeStateActivity;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.view.CircleTransform;

import it.sephiroth.android.library.picasso.Picasso;


/**
 * Created by Administrator on 2017/4/1.
 */
@SuppressLint("ValidFragment")
public class ResumeFragent extends BaseFragment implements View.OnClickListener {

    private final Context context;
    private LinearLayout ll_resume_register;
    private LinearLayout ll_resume_golinn;
    private FrameLayout fram_job_back;
    private RelativeLayout editResume;
    private RelativeLayout previewResume;
    private RelativeLayout openResuem;
    private RelativeLayout companyBlack;
    private RelativeLayout rl_part_time_job;
    private TextView tv_user_name;
    private TextView tv_user_phone;
    private TextView textdegree;

    public ResumeFragent(MainActivity context) {
        this.context = context;
    }

    @Override
    protected void initData() {

    }

    String id;
    String degree1;
    String resume_status;
    ImageView imageviewresume;
    ProgressBar progressbar;
    TextView textresume_status;
    String api_token;
    String part_resume_id;
    String resume_id;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected View initView() {
        SharedPreferences shareds = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String names = shareds.getString("name", "");
        String phonee = shareds.getString("resume_mobile", "");
        id = shareds.getString("id", "");
        degree1 = shareds.getString("degree1", "");
        resume_status = shareds.getString("resume_status", "");
        part_resume_id = shareds.getString("part_resume_id", "");
        resume_id = shareds.getString("resume_id", "");
        int badges = shareds.getInt("badges", 0);
        View view;
        SharedPreferences shared = getContext().getSharedPreferences("Activity", Context.MODE_PRIVATE);
        api_token = shared.getString("api_token", "");

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.gain(badges);

        if (api_token.isEmpty()) {
            view = View.inflate(getActivity(), R.layout.resume_fragment, null);

            fram_job_back = (FrameLayout) view.findViewById(R.id.fram_job_back);
            ll_resume_register = (LinearLayout) view.findViewById(R.id.ll_resume_register);
            ll_resume_golinn = (LinearLayout) view.findViewById(R.id.ll_resume_login);

            ll_resume_register.setOnClickListener(this);
            ll_resume_golinn.setOnClickListener(this);
            fram_job_back.setVisibility(View.INVISIBLE);

        } else {
            if (resume_id==null||"".equals(resume_id)){
                view=View.inflate(getActivity(),R.layout.resgister_no_resume,null);
                TextView text_create_resume= (TextView) view.findViewById(R.id.text_create_resume);
                text_create_resume.setOnClickListener(this);
            }else{
            view = View.inflate(getActivity(), R.layout.user_register_success, null);
            editResume = (RelativeLayout) view.findViewById(R.id.rl_edit_resume);
            previewResume = (RelativeLayout) view.findViewById(R.id.rl_preview_resume);
            openResuem = (RelativeLayout) view.findViewById(R.id.rl_open_resume);
            companyBlack = (RelativeLayout) view.findViewById(R.id.rl_company_blacklist);
            rl_part_time_job = (RelativeLayout) view.findViewById(R.id.rl_part_time_job);
            tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
            tv_user_phone = (TextView) view.findViewById(R.id.tv_user_phone);
            textdegree = (TextView) view.findViewById(R.id.textdegree);
            textresume_status = (TextView) view.findViewById(R.id.textresume_status);
            imageviewresume = (ImageView) view.findViewById(R.id.imageviewresume);
            progressbar = (ProgressBar) view.findViewById(R.id.progressbar);

            tv_user_name.setText(names);
            tv_user_phone.setText(phonee);

            editResume.setOnClickListener(this);
            previewResume.setOnClickListener(this);
            openResuem.setOnClickListener(this);
            companyBlack.setOnClickListener(this);
            rl_part_time_job.setOnClickListener(this);

            if ("0".equals(degree1) || "".equals(degree1)) {
                textdegree.setText("0%");
            } else {
                Log.i("degree1", degree1);
                final Float degree = Float.parseFloat(degree1) * 100;
                textdegree.setText(Math.round(degree) + "%");
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        for (int i = 0; i < degree; i++) {
                            SystemClock.sleep(10);
                            final int finalI = i;
                            progressbar.setProgress(finalI);
                        }
                    }
                }.start();
            }

            if ("1".equals(resume_status)) {
                textresume_status.setText("完全公开");
            } else if ("2".equals(resume_status)) {
                textresume_status.setText("不公开");
            } else if ("3".equals(resume_status)) {
                textresume_status.setText("完全保密");
            }

            SharedPreferences shares = context.getSharedPreferences("data", Context.MODE_PRIVATE);
            String avatar = shares.getString("avatar", "");
                if (avatar==null||avatar.isEmpty()){
                    Picasso.with(getActivity())
                            .load(R.mipmap.avatar_m2x)
                            .transform(new CircleTransform())
                            .into(imageviewresume);
                }else{
                    Picasso.with(getActivity())
                            .load(ContentUrl.BASE_ICON_URL + avatar)
                            .transform(new CircleTransform())
                            .into(imageviewresume);
                }

            }
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_resume_register:
                Intent intentRegister = new Intent(getActivity(), RegisterActivity.class);
                intentRegister.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentRegister);
                break;
            case R.id.ll_resume_login:
                Intent intentLogin = new Intent(getActivity(), WXEntryActivity.class);
                intentLogin.putExtra("register", "register");
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentLogin);
                break;
            case R.id.rl_edit_resume://编辑简历
                    if (resume_id == null || "".equals(resume_id)) {//没有创建简历进入的界面
                        Intent editResumeIntent = new Intent(context, EditResumeActivity.class);
                        startActivity(editResumeIntent);

                    } else {//创建简历之后进入的页面
                        Intent editTwoResumeIntent = new Intent(context, EditResumeTwoActivity.class);
                        startActivity(editTwoResumeIntent);
                    }

                break;
            case R.id.rl_preview_resume://预览简历

                if (resume_id==null||"".equals(resume_id)){
                    Toast.makeText(context,"您还没有创建简历",Toast.LENGTH_SHORT).show();
                }else{
                    Intent previewIntent = new Intent(context, PreviewResumeActivityTwo.class);
                    startActivity(previewIntent);
                }

                break;
            case R.id.rl_open_resume://公开隐藏简历
                if (resume_id==null||"".equals(resume_id)){
                    Toast.makeText(context,"您还没有创建简历",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(context, ResumeStateActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_company_blacklist://公司黑名单
                if (resume_id==null||"".equals(resume_id)){
                    Toast.makeText(context,"您还没有创建简历",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intentBlack = new Intent(context, BlacknumActivity.class);
                    startActivity(intentBlack);
                }
                break;
            case R.id.rl_part_time_job://编辑兼职简历
                if (part_resume_id==null||"".equals(part_resume_id)){
                    Intent intent=new Intent(context,PartJobActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(context,EditPartJobActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.text_create_resume:
                Intent intent=new Intent(getActivity(),EditResumeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
