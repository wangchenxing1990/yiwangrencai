package com.yiwangrencai.ywkj.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by JL1n on 2017/4/11.
 */

public class DropZoomScrollView extends ScrollView  {

    private static final String TAG = "BounceScrollView";
    //----头部收缩属性--------
    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 头部图片是否正在放大
    private Boolean mScaling = false;
    private View dropZoomView;//需要被放大的view
    private int dropZoomViewWidth;
    private int dropZoomViewHeight;
    //----头部收缩属性end--------
    //------尾部收缩属性--------
    private View inner;// 子View
    private float y;// 点击时y坐标
    private Rect normal = new Rect();// 矩形(这里只是个形式，只是用于判断是否需要动画.)
    private boolean isCount = false;// 是否开始计算
    //最后的坐标
    private float lastX = 0;
    private float lastY = 0;
    //当前坐标
    private float currentX = 0;
    private float currentY = 0;
    //移动的坐标量
    private float distanceX = 0;
    private float distanceY = 0;
    private boolean upDownSlide = false; //判断上下滑动的flag
    //------尾部收缩属性end--------
    public DropZoomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //初始化
    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        if (getChildAt(0) != null) {
            inner = getChildAt(0);//这个是底部收缩的view
            //头部收缩的
            ViewGroup vg = (ViewGroup) getChildAt(0);
            if (vg.getChildAt(0) != null) {
                dropZoomView = vg.getChildAt(0);
            }
        }
    }
    /***
     * 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
     * 方法，也应该调用父类的方法，使该方法得以执行.
     */
    @Override
    protected void onFinishInflate() {
        //初始化
        init();
        super.onFinishInflate();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //这里只是计算尾部坐标
        currentX = ev.getX();
        currentY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                distanceX = currentX - lastX;
                distanceY = currentY - lastY;
                if (Math.abs(distanceX) < Math.abs(distanceY) && Math.abs(distanceY) > 12) {
                    upDownSlide = true;
                }
                break;
        }
        lastX = currentX;
        lastY = currentY;
        if (upDownSlide && inner != null) commOnTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }
    /***
     * 触摸事件
     *
     * @param ev
     */
    public void commOnTouchEvent(MotionEvent ev) {
        //头部缩放计算
        if (dropZoomViewWidth <= 0 || dropZoomViewHeight <= 0) {
            dropZoomViewWidth = dropZoomView.getMeasuredWidth();
            dropZoomViewHeight = dropZoomView.getMeasuredHeight();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //手指离开后头部恢复图片
                mScaling = false;
                replyImage();
                // 手指松开尾部恢复
                if (isNeedAnimation()) {
                    animation();
                    isCount = false;
                }
                clear0();
                break;
            //这里头尾分开处理，互不干扰
            case MotionEvent.ACTION_MOVE:
                //尾部处理
                final float preY = y;// 按下时的y坐标
                float nowY = ev.getY();// 时时y坐标
                int deltaY = (int) (preY - nowY);// 滑动距离
                if (!isCount) {
                    deltaY = 0; // 在这里要归0.
                }
                y = nowY;
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove()) {
                    // 初始化头部矩形
                    if (normal.isEmpty()) {
                        // 保存正常的布局位置
                        normal.set(inner.getLeft(), inner.getTop(),
                                inner.getRight(), inner.getBottom());
                    }
                    // 移动布局
                    inner.layout(inner.getLeft(), inner.getTop() - deltaY / 2,
                            inner.getRight(), inner.getBottom() - deltaY / 2);
                }
                isCount = true;
                //尾部处理end
                //头部处理
                if (!mScaling) {
                    if (getScrollY() == 0) {
                        mFirstPosition = ev.getY();// 滚动到顶部时记录位置，否则正常返回
                    } else {
                        break;
                    }
                }
                int distance = (int) ((ev.getY() - mFirstPosition) * 0.6); // 滚动距离乘以一个系数
                if (distance < 0) { // 当前位置比记录位置要小，正常返回
                    break;
                }
                // 处理放大
                mScaling = true;
                setZoom(1 + distance);
                //头部处理end
                break;
        }
    }
    /***
     * 回缩动画,尾部往下缩动画
     */
    public void animation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
                normal.top);
        ta.setDuration(200);
        inner.startAnimation(ta);
        // 设置回到正常的布局位置
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();
    }
    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }
    // 回弹动画，header往上缩动画 (使用了属性动画)
    public void replyImage() {
        final float distance = dropZoomView.getMeasuredWidth() - dropZoomViewWidth;
        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration((long) (distance * 0.7));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                setZoom(distance - ((distance) * cVal));
            }
        });
        anim.start();
    }
    //头部缩放
    public void setZoom(float s) {
        if (dropZoomViewHeight <= 0 || dropZoomViewWidth <= 0) {
            return;
        }
        ViewGroup.LayoutParams lp = dropZoomView.getLayoutParams();
        lp.width = (int) (dropZoomViewWidth + s);
        lp.height = (int) (dropZoomViewHeight * ((dropZoomViewWidth + s) / dropZoomViewWidth));
        dropZoomView.setLayoutParams(lp);
    }
    /***
     * 是否需要移动布局 inner.getMeasuredHeight():获取的是控件的总高度
     *
     * getHeight()：获取的是屏幕的高度
     *
     * @return
     */
    public boolean isNeedMove() {
        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        // 0是顶部，后面那个是底部
        if (scrollY == 0 || scrollY == offset) {
            return true;
        }
        return false;
    }
    //清理尾部属性值
    private void clear0() {
        lastX = 0;
        lastY = 0;
        distanceX = 0;
        distanceY = 0;
        upDownSlide = false;
    }
//    public DropZoomScrollView(Context context) {
//        super(context);
//    }
//
//    public DropZoomScrollView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public DropZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    //    用于记录下拉位置
//    private float y = 0f;
//    //    zoomView原本的宽高
//    private int zoomViewWidth = 0;
//    private int zoomViewHeight = 0;
//
//    //    是否正在放大
//    private boolean mScaling = false;
//
//    //    放大的view，默认为第一个子view
//    private View zoomView;
//    public void setZoomView(View zoomView) {
//        this.zoomView = zoomView;
//    }
//
//    //    滑动放大系数，系数越大，滑动时放大程度越大
//    private float mScaleRatio = 0.4f;
//    public void setmScaleRatio(float mScaleRatio) {
//        this.mScaleRatio = mScaleRatio;
//    }
//
//    //    最大的放大倍数
//    private float mScaleTimes = 2f;
//    public void setmScaleTimes(int mScaleTimes) {
//        this.mScaleTimes = mScaleTimes;
//    }
//
//    //    回弹时间系数，系数越小，回弹越快
//    private float mReplyRatio = 0.5f;
//    public void setmReplyRatio(float mReplyRatio) {
//        this.mReplyRatio = mReplyRatio;
//    }
//
//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
////        不可过度滚动，否则上移后下拉会出现部分空白的情况
//        setOverScrollMode(OVER_SCROLL_NEVER);
////        获得默认第一个view
//        if (getChildAt(0) != null && getChildAt(0) instanceof ViewGroup && zoomView == null) {
//            ViewGroup vg = (ViewGroup) getChildAt(0);
//            if (vg.getChildCount() > 0) {
//                zoomView = vg.getChildAt(0);
//            }
//        }
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (zoomViewWidth <= 0 || zoomViewHeight <=0) {
//            zoomViewWidth = zoomView.getMeasuredWidth();
//            zoomViewHeight = zoomView.getMeasuredHeight();
//        }
//        if (zoomView == null || zoomViewWidth <= 0 || zoomViewHeight <= 0) {
//            return super.onTouchEvent(ev);
//        }
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                if (!mScaling) {
//                    if (getScrollY() == 0) {
//                        y = ev.getY();//滑动到顶部时，记录位置
//                    } else {
//                        break;
//                    }
//                }
//                int distance = (int) ((ev.getY() - y)*mScaleRatio);
//                if (distance < 0) break;//若往下滑动
//                mScaling = true;
//                setZoom(distance);
//                return true;
//            case MotionEvent.ACTION_UP:
//                mScaling = false;
//                replyView();
//                break;
//        }
//        return super.onTouchEvent(ev);
//    }
//
//    /**放大view*/
//    private void setZoom(float s) {
//        float scaleTimes = (float) ((zoomViewWidth+s)/(zoomViewWidth*1.0));
////        如超过最大放大倍数，直接返回
//        if (scaleTimes > mScaleTimes) return;
//
//        ViewGroup.LayoutParams layoutParams = zoomView.getLayoutParams();
//        layoutParams.width = (int) (zoomViewWidth + s);
//        layoutParams.height = (int)(zoomViewHeight*((zoomViewWidth+s)/zoomViewWidth));
////        设置控件水平居中
//        ((MarginLayoutParams) layoutParams).setMargins(-(layoutParams.width - zoomViewWidth) / 2, 0, 0, 0);
//        zoomView.setLayoutParams(layoutParams);
//    }
//
//    /**回弹*/
//    private void replyView() {
//        final float distance = zoomView.getMeasuredWidth() - zoomViewWidth;
//        // 设置动画
//        ValueAnimator anim = ObjectAnimator.ofFloat(distance, 0.0F).setDuration((long) (distance * mReplyRatio));
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                setZoom((Float) animation.getAnimatedValue());
//            }
//        });
//        anim.start();
//    }
//
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener!=null) onScrollListener.onScroll(l,t,oldl,oldt);
    }

    private OnScrollListener onScrollListener;
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**滑动监听*/
    public  interface OnScrollListener{
        void onScroll(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }
//    // 记录首次按下位置
//    private float mFirstPosition = 0;
//    // 是否正在放大
//    private Boolean mScaling = false;
//
//    private View dropZoomView;
//    private int dropZoomViewWidth;
//    private int dropZoomViewHeight;
//
//    public DropZoomScrollView(Context context) {
//        super(context);
//    }
//
//    public DropZoomScrollView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public DropZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        init();
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//    }
//
//    private void init() {
//        setOverScrollMode(OVER_SCROLL_NEVER);
//        if (getChildAt(0) != null) {
//            ViewGroup vg = (ViewGroup) getChildAt(0);
//            if (vg.getChildAt(0) != null) {
//                dropZoomView = vg.getChildAt(0);
//                setOnTouchListener(this);
//
//            }
//        }
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if (dropZoomViewWidth <= 0 || dropZoomViewHeight <= 0) {
//            dropZoomViewWidth = dropZoomView.getMeasuredWidth();
//            dropZoomViewHeight = dropZoomView.getMeasuredHeight();
//        }
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_UP:
//                //手指离开后恢复图片
//                mScaling = false;
//                replyImage();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (!mScaling) {
//                    if (getScrollY() == 0) {
//                        mFirstPosition = event.getY();// 滚动到顶部时记录位置，否则正常返回
//                    } else {
//                        break;
//                    }
//                }
//                int distance = (int) ((event.getY() - mFirstPosition) * 0.6); // 滚动距离乘以一个系数
//                if (distance < 0) { // 当前位置比记录位置要小，正常返回
//                    break;
//                }
//
//                // 处理放大
//                mScaling = true;
//                setZoom(1 + distance);
//                return true; // 返回true表示已经完成触摸事件，不再处理
//        }
//        return false;
//    }
//
//    // 回弹动画 (使用了属性动画)
//    public void replyImage() {
//        final float distance = dropZoomView.getMeasuredWidth() - dropZoomViewWidth;
//
//        // 设置动画
//        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration((long) (distance * 0.7));
//
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float cVal = (Float) animation.getAnimatedValue();
//                setZoom(distance - ((distance) * cVal));
//            }
//        });
//        anim.start();
//
//    }
//
//    //缩放
//    public void setZoom(float s) {
//        if (dropZoomViewHeight <= 0 || dropZoomViewWidth <= 0) {
//            return;
//        }
//        ViewGroup.LayoutParams lp = dropZoomView.getLayoutParams();
//        lp.width = (int) (dropZoomViewWidth + s);
//        lp.height = (int) (dropZoomViewHeight * ((dropZoomViewWidth + s) / dropZoomViewWidth));
//        dropZoomView.setLayoutParams(lp);
//    }
}
