package com.forgepoker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import com.forgepoker.R;

/**
 * Represents startup view (several menu items: start game, exit game, etc.)
 * @author zhanglo
 *
 */
public class StartupView extends View {
	
	private Bitmap background;
	private Paint paint;
	
	public StartupView(Context context) {
		super(context);
		paint = new Paint();
		background = BitmapFactory.decodeResource(this.getResources(), R.drawable.menu);
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(background, 0,  0, paint);
	}
}
