package com.yiwangrencai.ywkj.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/8/25.
 */

public class RatioView extends View {
    public RatioView(Context context) {
        super(context);
        initView();
    }

    public RatioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RatioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private Paint mPaint;
    Path mPath;
    private void initView() {
        mPaint = new Paint();
        mPath=new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(0xff00c6c9); //画笔颜色
        mPaint.setStrokeWidth(10); //画笔宽度
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        int x=getWidth();
        int y=getHeight();
        RectF oval2 = new RectF(-130,-200,x+130,200);
        canvas.drawArc(oval2, 0,360,true,mPaint);


    }

}
