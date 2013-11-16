package com.forgepoker;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
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
		Log.d("forge", "surfaceChanged");
		if (mViewThread != null) {
			mViewThread.onWindowResize(width, height);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("forge", "surfaceCreated");
		mHasSurface = true;
		resume();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("forge", "surfaceDestroyed");
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
			SurfaceHolder surfHolder = mHolder;
			while (!done) {
				Log.d("forge", "run thread");
				Canvas canvas = null;
				try {
					canvas = surfHolder.lockCanvas();
					synchronized(surfHolder) {
						mGameController.render(canvas);
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
					if (canvas != null) {
						surfHolder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}
		
		public void requestExitAndWait() {
			Log.d("forge", "requestExitAndWait");
			done = true;
			try {
				join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void onWindowResize(int w, int h) {
			
		}
	}	
}