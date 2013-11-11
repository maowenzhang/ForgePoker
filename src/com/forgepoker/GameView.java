package com.forgepoker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
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

	/** SurfaceView implementation */
	private GameViewThread mViewThread;
	private boolean mHasSurface = false;	
	private SurfaceHolder mHolder;
		
	/** Draw graphics */
	private Context mContext;	
	
	private GameController mGameController;
	
	public GameView(Context context, int screenWidth, int screenHeight) {
		super(context);
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHasSurface = false;
		
		this.mContext = context;
		mGameController = GameController.get();		
		mGameController.init(context, screenWidth, screenHeight);
		
		this.setOnTouchListener(this);
	}

	/**
	 * Surface events
	*/
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (mViewThread != null) {
			mViewThread.onWindowResize(width, height);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mHasSurface = true;
		resume();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mHasSurface = false;
		pause();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGameController.onTouch(v, event);
	}
	
	private void resume() {
		if (mViewThread == null) {
			mViewThread = new GameViewThread();
			if (mHasSurface == true) {
				mViewThread.start();
			}
		}
	}
	
	private void pause() {
		
		if (mViewThread != null) {
			mViewThread.requestExitAndWait();
			mViewThread = null;
		}
	}
	
	/** Thread for Game surface view 
	 * 
	*/
	class GameViewThread extends Thread {
		private boolean done = false;
		
		public GameViewThread() {
			super();
			done = false;
		}
		
		@Override
		public void run() {
			System.out.print("run thread");
			SurfaceHolder surfHolder = mHolder;
			while (!done) {
				System.out.print("run thread - doing ");
				try {
					Canvas canvas = surfHolder.lockCanvas();
					// draw
					mGameController.render(canvas);
					surfHolder.unlockCanvasAndPost(canvas);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
		}
		
		public void requestExitAndWait() {
			System.out.print("requestExitAndWait - begin");
			done = true;
			try {
				join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.print("requestExitAndWait - end");
		}
		
		public void onWindowResize(int w, int h) {
			
		}
	}	
}