package com.forgepoker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameViewRender {
	
	GameController mGameController;
	private Paint mPaint;
	private Bitmap mGameBackground;
	private Canvas mCanvas;
	private Context mContext;
	
	public GameViewRender(GameController gameController, Context context) {
		mGameController = gameController;
		mContext = context;
		mPaint = new Paint();
		mGameBackground = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.game_background);
	}
	
	public void render(Canvas canvas) {
		mCanvas = canvas;
		renderBackground();
	}
	
	private void renderBackground() {
		Rect r = new Rect(0, 0, mGameController.mScreenWidth, mGameController.mScreenHeight);
		mCanvas.drawBitmap(mGameBackground, null, r, mPaint);
//		canvas.drawBitmap(gameBackground, touchPosX, touchPosY, paint);
	}
	
	/**
	 * Render all players
	 * all info of player (picture, name, score, also pokes, etc.)
	 */
	private void renderPlayers() {
		// render left player
		// render right player
		// render host
	}
	
	private void renderJiaoFen() {
		// 
	}
	
	private void renderCard() {
		
	}
}
