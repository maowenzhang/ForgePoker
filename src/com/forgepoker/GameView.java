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

/**
 * Represents view for game table
 * @author zhanglo
 *
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, OnTouchListener, Runnable {
	
	/** Game states */
	static final int STATE_GAME = 0;
	int mGameState = STATE_GAME;	

	int mScreenWidth = 0;
	int mScreenHeight = 0;
	int mTouchPosX = 0;
	int mTouchPosY = 0;
	
	/** View render thread */
	Thread mThread;
	boolean mIsThreadRunning = false;
	
	SurfaceHolder mSurfHolder;
	Canvas mCanvas;
	Context mContext;
	Bitmap mGameBackground;
	Paint mPaint = null;
	
	
	public GameView(Context context) {
		super(context);
		
		mContext = context;
		
		mSurfHolder = getHolder();
		mSurfHolder.addCallback(this);
		
		mGameState = STATE_GAME;
		mPaint = new Paint();
		mGameBackground = BitmapFactory.decodeResource(getResources(), R.drawable.game_background);
		this.getHolder().addCallback(this);
		this.setOnTouchListener(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		canvas.drawBitmap(mGameBackground, 0, 0, mPaint);
//		desk.paint(canvas);
	}

	/**
	 * Surface events
	*/
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mIsThreadRunning = true;
		mThread = new Thread(this);
		mThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mIsThreadRunning = false;
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
	
	/** Implement Runnable */
	@Override
	public void run() {
		while (mIsThreadRunning) {
			synchronized (mSurfHolder) {
				
				// Lock before drawing, and then unlock to show
				mCanvas = mSurfHolder.lockCanvas();
				Draw();
				mSurfHolder.unlockCanvasAndPost(mCanvas);
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void Draw() {
		
		renderBackground();
		
		switch (mGameState) {
		case STATE_GAME:
			break;
		}
	}
	
	public void renderBackground() {
		mCanvas.drawBitmap(mGameBackground, 0, 0, mPaint);
//		mCanvas.drawBitmap(mGameBackground, mTouchPosX, mTouchPosY, mPaint);
	}
	
}