package com.forgepoker;

import junit.framework.Assert;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
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
public class GameView extends SurfaceView implements SurfaceHolder.Callback, 
OnTouchListener, Runnable {

	/** SurfaceView */
	private boolean mHasSurface = false;
	private SurfaceHolder mHolder;
		
	/** Draw graphics */
	private GameController mGameController;
	private GameViewRender mRender;	
	public GameViewRender render() {
		return mRender;
	}
	
	/** Thread */
	private Thread mViewThread;
	private boolean mIsThreadDone = false;
	
	/** Constructors */
	public GameView(Context context, AttributeSet attrs) {
		super(context,attrs);
		
		init(context);
		
		Log.d("forge", "create GameView");
	}
	
	public GameView(Context context) {
		super(context);
		
		init(context);
		
		Log.d("forge", "create GameView");
	}
	
	private void init(Context context) { 
		try {
		setKeepScreenOn(true);
		setLongClickable(true);
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHasSurface = false;
		
		mGameController = GameController.get();		
		mGameController.init(context);
		
		setOnTouchListener(this);
		
		mRender = new GameViewRender(context);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		GameViewRender.mScreenWidth = getWidth();
		GameViewRender.mScreenHeight = getHeight();
		
		Log.d("forge", "surfaceCreated");
		mHasSurface = true;
		
		mIsThreadDone = false;
		mViewThread = new Thread(this, "forge");
		mViewThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("forge", "surfaceDestroyed");
		mHasSurface = false;
		mIsThreadDone = true;
		mViewThread.interrupt();
		mViewThread = null;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.d("forge", "GameView::onTouch");
		
		if (!mHasSurface) {
			return true;
		}

		// get touch position
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		// start touch
		case MotionEvent.ACTION_DOWN:
			mRender.OnTouch(x, y);
			
			break;
		// touch and move
		case MotionEvent.ACTION_MOVE:
			break;
			
		// end touch
		case MotionEvent.ACTION_UP:
			break;
		}
		
		return true;
	}
	
	private void draw() {
		
		if(mRender == null)
			return;

		Canvas canvas = null;
		try {
			
			mRender.init();
			
			synchronized(mHolder) {	
				canvas = mHolder.lockCanvas();
				//Log.d("thread", "draw::lockCanvas");
				if (canvas == null) {
					return;
				}
				mRender.render(canvas);
			}
			
		} catch (Exception e) {
			Log.e("thread", "Error in draw of gameview thead!");
			Assert.assertTrue("Error in draw of gameview thread!", true);
			e.printStackTrace();
			
		} finally {
			
			try {
				// do this in a finally so that if an exception is thrown
	            // during the above, we don't leave the Surface in an
	            // inconsistent state
				if (canvas != null) {
					mHolder.unlockCanvasAndPost(canvas);
					//Log.d("thread", "draw::unlockCanvasAndPost");
				}
			} catch (Exception e) {
				Log.e("thread", "fail to unlockCanvasAndPost!");
				e.printStackTrace();
			}
		}
	}
	
	/** Thread run method
	 * 
	*/
	@Override
	public void run() {
		
		Log.d("forge", "start thread");
		
		while (!mIsThreadDone) {
			
			draw();
			
			try {    
                Thread.sleep(100);    
            } catch (Exception ex) {
            	ex.printStackTrace();
            } 
		}
		
		Log.d("forge", "exit thread");
	}	
}