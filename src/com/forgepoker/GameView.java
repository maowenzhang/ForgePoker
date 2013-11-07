package com.forgepoker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Represents view for game table
 * @author zhanglo
 *
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, OnTouchListener {
	
	Bitmap gameBack;
	
	public GameView(Context context) {
		super(context);
//		gameBack=BitmapFactory.decodeResource(getResources(), R.drawable.vbg2);
		this.getHolder().addCallback(this);
		this.setOnTouchListener(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(gameBack, 0, 0, null);
//		desk.paint(canvas);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}