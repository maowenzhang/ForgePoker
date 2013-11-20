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
	private GameViewRender mRender;	
	public GameViewRender render() {
		return mRender;
	}
	
	public GameView(Context context, int screenWidth, int screenHeight) {
		super(context);
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHasSurface = false;
		
		this.mContext = context;
		mGameController = GameController.get();		
		mGameController.init(context);
		
		this.setOnTouchListener(this);
		
		GameViewRender.mScreenWidth = screenWidth;
		GameViewRender.mScreenHeight = screenHeight;
		mRender = new GameViewRender(context);
		
		mViewThread = new GameViewThread(context, this, screenWidth, screenHeight);
	}

	/**
	 * Surface events
	*/
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.d("forge", "surfaceChanged");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("forge", "surfaceCreated");
		mHasSurface = true;
		mViewThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("forge", "surfaceDestroyed");
		mHasSurface = false;
		mViewThread.requestExitAndWait();
	}

	private int mTouchPosX = 0;
	private int mTouchPosY = 0;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (!mHasSurface) {
			return true;
		}

		// get touch position
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		// start touch
		case MotionEvent.ACTION_DOWN:
			mTouchPosX = x;
			mTouchPosY = y;
			mRender.OnTouch(x, y);
			
			break;
		// touch and move
		case MotionEvent.ACTION_MOVE:
			mTouchPosX = x;
			mTouchPosY = y;
			break;
			
		// end touch
		case MotionEvent.ACTION_UP:
			break;
		}
		
		return true;
	}
	
	/** Thread for Game surface view 
	 * 
	*/
	class GameViewThread extends Thread {
		
		private boolean mIsDone = false;
		private GameViewRender mRender;	
		private SurfaceHolder mHolder;
		
		public GameViewThread(Context context, GameView gameView, int screenWidth, int screenHeight) {
			super();
			mIsDone = false;
			mHolder = gameView.getHolder();
			mRender = gameView.render();			
		}
		
		@Override
		public void run() {
			
			mRender.init();
			
			while (!mIsDone) {
				Log.d("forge", "run thread");
				Canvas canvas = null;
				try {
					canvas = mHolder.lockCanvas();
					if (canvas == null) {
						continue;
					}
					synchronized(mHolder) {
						mRender.render(canvas);
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					// do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
					if (canvas != null) {
						mHolder.unlockCanvasAndPost(canvas);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		public void requestExitAndWait() {
			Log.d("forge", "requestExitAndWait");
			mIsDone = true;
			try {
				join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}	
}