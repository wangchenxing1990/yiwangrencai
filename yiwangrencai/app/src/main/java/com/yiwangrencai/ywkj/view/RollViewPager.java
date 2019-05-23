package com.yiwangrencai.ywkj.view;

import java.util.LinkedList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.yiwangrencai.ywkj.content.ContentUrl;
import com.yiwangrencai.ywkj.tools.UiUtils;

import it.sephiroth.android.library.picasso.Picasso;

public class RollViewPager extends ViewPager {
	private final JSONArray imageArray;
	private final Context context;

	//	private int[] imageRes = new int[] { R.drawable.icon_1, R.drawable.icon_2,
//			R.drawable.icon_3, R.drawable.icon_4 };
	public RollViewPager(Context context, AttributeSet attrs, JSONArray imageArray) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.imageArray = imageArray;
		this.context=context;
	}

	public RollViewPager(Context context,JSONArray imageArray) {
		super(context);
		// TODO Auto-generated constructor stub
		this.imageArray=imageArray;
		this.context=context;
	}

	//按下时间
    private int downTime;
    //按下的y坐标
    private int downY;
    private int downX;
	//private JSONArray datas;
	public void refreshView() {

		//this.datas = datas;
		setAdapter(new MyAdapter());
		setCurrentItem(2000*imageArray.size());// 设置起始的位置   Integer.Max_Vlue/2

		runTask = new AuToRunTask();
		runTask.start();
	}
	
	boolean flag;
	private AuToRunTask runTask;
	public class AuToRunTask implements Runnable{

		@Override
		public void run() {
			if(flag){
				UiUtils.cancel(this);  // 取消之前
				int currentItem = getCurrentItem();
				currentItem++;
				setCurrentItem(currentItem);
				//  延迟执行当前的任务
				UiUtils.postDelayed(this, 2000);// 递归调用
			}
		}
		public void start(){
			if(!flag){
				UiUtils.cancel(this);  // 取消之前
				flag=true;
				UiUtils.postDelayed(this, 2000);// 递归调用
			}
		}
		public  void stop(){
			if(flag){
				flag=false;
				UiUtils.cancel(this);
			}
		}
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			    downX = (int) event.getX();
                downY = (int) event.getY();
                downTime = (int) System.currentTimeMillis();
			runTask.stop();   
			break;
		case MotionEvent.ACTION_MOVE:
			 int moveX = (int) event.getX();
                int moveY = (int) event.getY();
                //如果是左右滚动,还是处理以下逻辑
                int disX = Math.abs(moveX - downX);
                int disY = Math.abs(moveY - downY);
			break;
		
		case MotionEvent.ACTION_UP:
			//抬起时间
            int upTime = (int) System.currentTimeMillis();
            int disTime = upTime-downTime;
            // 2. 限制 按下和拖动(抬起)的水平.垂直的距离
            //抬起坐标
            int upX = (int) event.getX();
            int upY = (int) event.getY();
            disX = Math.abs(upX - downX);
            disY = Math.abs(upY - downY);
            if(disTime<500 && disX<5 && disY<5){//单击
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(getCurrentItem());
                }
            }
			
			
		case MotionEvent.ACTION_CANCEL:  // 事件的取消
			runTask.start();
			break;
		}
		return super.onTouchEvent(event);
	}
	
	public class MyAdapter extends PagerAdapter {
		// 当前viewPager里面有多少个条目
				LinkedList<ImageView> convertView=new LinkedList<ImageView>();
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			int index=position%imageArray.size();
		
			ImageView view;
			if(convertView.size()>0){
				view=convertView.remove(0);
			}else{
				view= new ImageView(UiUtils.getContext());
			}
			ImageView imageView = new ImageView(getContext());
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			container.addView(imageView);
			String urlImage= (String) imageArray.get(index);

			Picasso.with(context).load(ContentUrl.BASE_ICON_URL+urlImage).into(imageView);
			//imageView.setImageResource(datas.get(index).get);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	private OnItemClickListener onItemClickListener;
	public interface OnItemClickListener{
        /**
         * 条目点击回调
         * @param position:点击的位置
         */
        public void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener  = onItemClickListener;
    }
	
}
