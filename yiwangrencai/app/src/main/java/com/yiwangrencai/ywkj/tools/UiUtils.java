package com.yiwangrencai.ywkj.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.yiwangrencai.ywkj.activity.BaseApplication;
import com.yiwangrencai.ywkj.jmessage.JGApplication;


public class UiUtils {
	/**
	 * 获取到字符数组
	 * @param tabNames  字符数组的id
	 */
	public static String[] getStringArray(int tabNames) {
		return getResource().getStringArray(tabNames);
	}

	public static Resources getResource() {
		return JGApplication.context.getResources();
	}
	public static Context getContext(){
		return JGApplication.context;
	}
	/** dip转换px */
	public static int dip2px(int dip) {
		final float scale = getResource().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/** pxz转换dip */

	public static int px2dip(int px) {
		final float scale = getResource().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}
	/**
	 * 把Runnable 方法提交到主线程运行
	 * @param runnable
	 */
	public static void runOnUiThread(Runnable runnable) {
		// 在主线程运行
		if(android.os.Process.myTid()==BaseApplication.getMainTid()){
			runnable.run();
		}else{
			//获取handler
			//BaseApplication.getHandler().post(runnable);
		}
	}
	public static View inflate(int id) {
		return View.inflate(getContext(), id, null);
	}

	public static Drawable getDrawalbe(int id) {
		return getResource().getDrawable(id);
	}

	/**
	 *
	 * @param auToRunTask
	 */
	public static void cancel(Runnable auToRunTask) {
	//	BaseApplication.getHandler().removeCallbacks(auToRunTask);
	}
	/**
	 *
	 * @param run
	 * @param time
	 */
	public static void postDelayed(Runnable run, int time) {
	//	BaseApplication.getHandler().postDelayed(run, time); //
	}

	/**
	 * 获取api_token
	 * @return
     */
	public static String getApiToken(){
		String api_token= JGApplication.context.getSharedPreferences("Activity",Context.MODE_PRIVATE).getString("api_token","");
	return api_token;
	}
	public static String getId(){
		SharedPreferences sharedPreferences=JGApplication.context.getSharedPreferences("data",Context.MODE_PRIVATE);
		return sharedPreferences.getString("id","");
	}
	/**
	 * 转化获取的时间格式
	 *
	 * @param data
	 * @return
	 */
	public static String getTime(String data) {
		String[] datas = data.split(" ");
		String mouth = datas[1];
		switch (mouth) {
			case "Jan":
				mouth = "01";
				break;
			case "Feb":
				mouth = "02";
				break;
			case "Mar":
				mouth = "03";
				break;
			case "Apr":
				mouth = "04";
				break;
			case "May":
				mouth = "05";
				break;
			case "Jun":
				mouth = "06";
				break;
			case "Jul":
				mouth = "07";
				break;
			case "Aug":
				mouth = "08";
				break;
			case "Sep":
				mouth = "09";
				break;
			case "Oct":
				mouth = "10";
				break;
			case "Nov":
				mouth = "11";
				break;
			case "Dec":
				mouth = "12";
				break;
		}
		String dataa = datas[datas.length - 1] + "-" + mouth;
		return dataa;
	}
}
