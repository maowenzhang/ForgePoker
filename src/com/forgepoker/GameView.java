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
	private Canvas mCanvas;
	private Bitmap mGameBackground;
	private Paint mPaint = null;
	
	private int mScreenWidth = 0;
	private int mScreenHeight = 0;
	private int mTouchPosX = 0;
	private int mTouchPosY = 0;
	
	/** Game states */
	private static final int STATE_GAME = 0;
	private int mGameState = STATE_GAME;	
	
	
	public GameView(Context context, int screenWidth, int screenHeight) {
		super(context);
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHasSurface = false;
		
		this.mContext = context;
		this.mScreenWidth = screenWidth;
		this.mScreenHeight = screenHeight;
		
		mGameState = STATE_GAME;
		mPaint = new Paint();
		mGameBackground = BitmapFactory.decodeResource(getResources(), R.drawable.game_background);
		
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
		// get touch position
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		// start touch
		case MotionEvent.ACTION_DOWN:
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
	
	private void resume() {
		if (mViewThread == null) {
			mViewThread = new GameViewThread(this);
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
	
	public void renderGameView(Canvas canvas) {
		
		this.mCanvas = canvas;
		renderBackground();
		
		switch (mGameState) {
		case STATE_GAME:
			break;
		}
	}
	
	private void renderBackground() {
		Rect r = new Rect(0, 0, mScreenWidth, mScreenHeight);
		mCanvas.drawBitmap(mGameBackground, null, r, mPaint);
//		canvas.drawBitmap(gameBackground, touchPosX, touchPosY, paint);
	}
	
	
	/** Thread for Game surface view 
	 * 
	*/
	class GameViewThread extends Thread {
		private boolean done = false;
		private GameView gameView = null;
		
		GameViewThread(GameView gameView) {
			super();
			done = false;
			this.gameView = gameView;
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
					gameView.renderGameView(canvas);
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