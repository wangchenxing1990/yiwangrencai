package com.yiwangrencai.ywkj.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yiwangrencai.R;
import com.yiwangrencai.ywkj.activity.BrowserActivity;
import com.yiwangrencai.ywkj.activity.BrowserActivityTwo;
import com.yiwangrencai.ywkj.activity.InterviewActivity;
import com.yiwangrencai.ywkj.activity.JobCollectionActivity;
import com.yiwangrencai.ywkj.activity.LookMineActivity;
import com.yiwangrencai.ywkj.activity.MainActivity;
import com.yiwangrencai.ywkj.activity.PartApplyActivity;
import com.yiwangrencai.ywkj.activity.ReceiveActivity;
import com.yiwangrencai.ywkj.activity.SearchEnginedActivity;
import com.yiwangrencai.ywkj.activity.SubmitResumeActivity;
import com.yiwangrencai.ywkj.activity.SystemSetActivity;
import com.yiwangrencai.ywkj.view.LoginOffPopuWindow;

/**
 * Created by Administrator on 2017/4/1.
 */
@SuppressLint("ValidFragment")
public class MineFragent extends BaseFragment implements View.OnClickListener {


    private final Context context;

    public MineFragent(Context context) {
        this.context = context;
    }

    private Button button_login_off;
    private RelativeLayout sendResume;
    private RelativeLayout interviewInform;
    private RelativeLayout workSellect;
    private RelativeLayout lookMe;
    private RelativeLayout browsingHistory;
    private RelativeLayout searcher;
    private RelativeLayout systemSetting;
    private RelativeLayout rl_mine_apply;
    private RelativeLayout rl_mine_receive_part;
    private TextView text_notification;

    @Override
    protected View initView() {

        MainActivity mainActivity= (MainActivity) getActivity();
        SharedPreferences shared = mainActivity.getSharedPreferences("data", Context.MODE_PRIVATE);
        int badges = shared.getInt("badges", 0);
        mainActivity.gain(badges);

        View view = View.inflate(getActivity(), R.layout.mine_fragment, null);
        sendResume = (RelativeLayout) view.findViewById(R.id.rl_mine_send_resume);
        interviewInform = (RelativeLayout) view.findViewById(R.id.rl_mine_interview_inform);
        workSellect = (RelativeLayout) view.findViewById(R.id.rl_mine_work_collect);
        lookMe = (RelativeLayout) view.findViewById(R.id.rl_mine_look_me);
        browsingHistory = (RelativeLayout) view.findViewById(R.id.rl_mine_browsing_history);
        searcher = (RelativeLayout) view.findViewById(R.id.rl_mine_seacher);
        systemSetting = (RelativeLayout) view.findViewById(R.id.rl_mine_system_setting);
        rl_mine_apply = (RelativeLayout) view.findViewById(R.id.rl_mine_apply);
        rl_mine_receive_part = (RelativeLayout) view.findViewById(R.id.rl_mine_receive_part);
        text_notification = (TextView) view.findViewById(R.id.text_notification);
        // button_login_off = (Button) view.findViewById(R.id.button_login_off);

        sendResume.setOnClickListener(this);
        interviewInform.setOnClickListener(this);
        workSellect.setOnClickListener(this);
        lookMe.setOnClickListener(this);
        browsingHistory.setOnClickListener(this);
        searcher.setOnClickListener(this);
        systemSetting.setOnClickListener(this);
        rl_mine_apply.setOnClickListener(this);
        rl_mine_receive_part.setOnClickListener(this);
        //button_login_off.setOnClickListener(this);
        if (badges == 0) {
            text_notification.setVisibility(View.INVISIBLE);
        } else {
            text_notification.setVisibility(View.VISIBLE);
            text_notification.setText("你有" + badges + "个面试通知");
        }

        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_mine_send_resume://发送简历
                Intent submitResumeActivity = new Intent(context, SubmitResumeActivity.class);
               // submitResumeActivity.putExtra("register","interView");
                startActivity(submitResumeActivity);
                break;

            case R.id.rl_mine_interview_inform://面试通知
                Intent interviewIntent = new Intent(context, InterviewActivity.class);
                interviewIntent.putExtra("register","interView");
                startActivity(interviewIntent);
                break;

            case R.id.rl_mine_work_collect://职位收藏
                Intent jobSelectIntent = new Intent(context, JobCollectionActivity.class);
                startActivity(jobSelectIntent);
                break;

            case R.id.rl_mine_look_me://谁看过我
                Intent lookMeIntent = new Intent(context, LookMineActivity.class);
                startActivity(lookMeIntent);
                break;

            case R.id.rl_mine_browsing_history://浏览记录
                Intent intentBrows = new Intent(context, BrowserActivityTwo.class);
                startActivity(intentBrows);
                break;

            case R.id.rl_mine_seacher://搜索器
                Intent intent = new Intent(context, SearchEnginedActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_mine_apply://报名的兼职
                Intent intentApply=new Intent(context,PartApplyActivity.class);
                startActivity(intentApply);
                break;

            case R.id.rl_mine_receive_part://收到的监制邀请
                Intent intentReceive=new Intent(context,ReceiveActivity.class);
                startActivity(intentReceive);
                break;

            case R.id.rl_mine_system_setting://系统设置
                Intent systemSetIntent = new Intent(context, SystemSetActivity.class);
                startActivity(systemSetIntent);
                break;

            case R.id.tv_off_cancel:
                loginOffWindow.dismiss();
                break;

            case R.id.tv_off_sure:
                logOff();
                break;

        }
    }

    LoginOffPopuWindow loginOffWindow;
    /**
     * 显示是否退出选项按钮
     */
    private void showPopuwindow() {

        loginOffWindow = new LoginOffPopuWindow(getActivity(), R.layout.login_off_popuwindown);
        View view = loginOffWindow.getView();
        loginOffWindow.showAtLocation(getActivity().findViewById(R.id.mine_fragment_popuwind), Gravity.CENTER, 0, 0);
        TextView tv_off_cancel = (TextView) view.findViewById(R.id.tv_off_cancel);
        TextView tv_off_sure = (TextView) view.findViewById(R.id.tv_off_sure);
        tv_off_cancel.setOnClickListener(this);
        tv_off_sure.setOnClickListener(this);

    }

    /**
     * 退出登录
     */
    private void logOff() {

        SharedPreferences share = context.getSharedPreferences("Activity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("api_token", "").commit();

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("register", "offMine");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        loginOffWindow.dismiss();

    }
}
