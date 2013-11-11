package com.forgepoker;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

/**
 * Used to controller whole game activities
 * @author 
 *
 */
public class GameController {
	
	private GameViewRender mRender;	
	
	int mScreenWidth;
	int mScreenHeight;
	int mTouchPosX = 0;
	int mTouchPosY = 0;
	
	/** Game states */
	private static final int STATE_GAME = 0;
	private int mGameState = STATE_GAME;	
	
	/** Single instance */
	private static GameController sGameController;
	static public GameController get() {
		if (sGameController == null) {
			sGameController = new GameController();
		}
		return sGameController;
	}
	
	private GameController() {
		
	}
	
	public void init(Context context, int screenWidth, int screenHeight) {
		mRender = new GameViewRender(this, context);
		mScreenWidth = screenWidth;
		mScreenHeight = screenHeight;
	}
	
	public void render(Canvas canvas) {
		mRender.render(canvas);
	}
	
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
}
