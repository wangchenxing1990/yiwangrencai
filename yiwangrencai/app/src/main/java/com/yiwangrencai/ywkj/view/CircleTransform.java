package com.yiwangrencai.ywkj.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import it.sephiroth.android.library.picasso.Transformation;


/**
 * Created by JL1n on 2017/4/12.
 * 对图片进行画圆处理
 */
public class CircleTransform implements Transformation {

	@Override
	public String key() {
		// TODO Auto-generated method stub
		return "circle";
	}

	@Override
	public Bitmap transform(Bitmap source) {
		// TODO Auto-generated method stub
		int size = Math.min(source.getWidth(), source.getHeight());

		int x = (source.getWidth() - size) / 2;
		int y = (source.getHeight() - size) / 2;

		Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
		if (squaredBitmap != source) {
			source.recycle();
		}

		/*Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());*/
		Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888 );
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		BitmapShader shader = new BitmapShader(squaredBitmap,
				BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
		paint.setShader(shader);
		paint.setAntiAlias(true);

		float r = size / 2f;
		canvas.drawCircle(r, r, r, paint);

		squaredBitmap.recycle();
		return bitmap;

	}

}
