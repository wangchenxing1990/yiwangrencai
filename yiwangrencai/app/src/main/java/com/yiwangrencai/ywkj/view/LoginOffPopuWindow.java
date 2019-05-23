package com.yiwangrencai.ywkj.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.yiwangrencai.R;

/**
 * Created by Administrator on 2017/4/18.
 */

public class LoginOffPopuWindow extends PopupWindow {
    View view;
    public LoginOffPopuWindow(Activity context, int layout){
        super(context);
         LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         view=inflater.inflate(layout,null);

        this.setContentView(view);
        //设置屏幕的宽度
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置弹出窗体的高度
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置弹出窗体可点击
        this.setFocusable(true);
        //实例化一个colordrawable颜色为半透明
        ColorDrawable colorDrawable=new ColorDrawable(0x66000000);
        //设置弹出窗体的背景
        this.setBackgroundDrawable(colorDrawable);
        //view添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.login_off_popuwindow).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }


    public View getView(){
        return view;
    }
}
