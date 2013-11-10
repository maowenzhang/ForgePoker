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
	private GameViewThread viewThread;
	private boolean hasSurface = false;	
	private SurfaceHolder holder;
		
	/** Draw graphics */
	private Context context;
	private Canvas canvas;
	private Bitmap gameBackground;
	private Paint paint = null;
	
	private int screenWidth = 0;
	private int screenHeight = 0;
	private int touchPosX = 0;
	private int touchPosY = 0;
	
	/** Game states */
	private static final int STATE_GAME = 0;
	private int gameState = STATE_GAME;	
	
	
	public GameView(Context context, int screenWidth, int screenHeight) {
		super(context);
		
		holder = getHolder();
		holder.addCallback(this);
		hasSurface = false;
		
		this.context = context;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		gameState = STATE_GAME;
		paint = new Paint();
		gameBackground = BitmapFactory.decodeResource(getResources(), R.drawable.game_background);
		
		this.setOnTouchListener(this);
	}

	/**
	 * Surface events
	*/
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (viewThread != null) {
			viewThread.onWindowResize(width, height);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		hasSurface = true;
		resume();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
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
			touchPosX = x;
			touchPosY = y;
			break;
			
		// end touch
		case MotionEvent.ACTION_UP:
			break;
		}		
		
		return true;
	}
	
	private void resume() {
		if (viewThread == null) {
			viewThread = new GameViewThread(this);
			if (hasSurface == true) {
				viewThread.start();
			}
		}
	}
	
	private void pause() {
		
		if (viewThread != null) {
			viewThread.requestExitAndWait();
			viewThread = null;
		}
	}
	
	public void renderGameView(Canvas canvas) {
		
		this.canvas = canvas;
		renderBackground();
		
		switch (gameState) {
		case STATE_GAME:
			break;
		}
	}
	
	private void renderBackground() {
		Rect r = new Rect(0, 0, screenWidth, screenHeight);
		canvas.drawBitmap(gameBackground, null, r, paint);
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
			SurfaceHolder surfHolder = holder;
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