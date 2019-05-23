package com.yiwangrencai.ywkj.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yiwangrencai.R;

/**
 * Created by Administrator on 2017/4/6.
 */

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;     //分割线Drawable
    private int mDividerHeight;  //分割线高度

    public SimpleDividerItemDecoration(Context context,int dividerHeight){
        mDivider= ContextCompat.getDrawable(context,R.drawable.line_divider);
        mDividerHeight=dividerHeight;
    }

    public SimpleDividerItemDecoration(Context context,Drawable divider,int dividerHeight){

        if (divider==null){
            mDivider= ContextCompat.getDrawable(context,R.drawable.line_divider);
        }else{
            mDivider=divider;
        }
        mDividerHeight=dividerHeight;

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0,0,0,mDividerHeight);

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int left=parent.getPaddingLeft();
        int right=parent.getWidth()-parent.getPaddingRight();
        int childCount=parent.getChildCount();

        for (int i=0;i<childCount;i++){

            View child=parent.getChildAt(i);
            RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) child.getLayoutParams();
            int top=child.getBottom()+params.bottomMargin;
            int bottom=top+mDividerHeight;
            mDivider.setBounds(left,right,top,bottom);

        }

    }

}
