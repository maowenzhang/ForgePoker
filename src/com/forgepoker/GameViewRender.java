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
	private CardRender mCardRender;
	private PlayerRender mPlayerRender;
	
	public GameViewRender(GameController gameController, Context context) {
		mGameController = gameController;
		mContext = context;
		mPaint = new Paint();
		
		mGameBackground = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.game_background);
		
		mCardRender = new CardRender(this, context);
		mPlayerRender = new PlayerRender(this, context);
		mCardRender.init();
		mPlayerRender.init();		
	}
	
	public void render(Canvas canvas) {
		mCanvas = canvas;
		renderBackground();
		
		mCardRender.render(canvas);
		mPlayerRender.render(canvas);
	}
	
	private void renderBackground() {
		Rect r = new Rect(0, 0, mGameController.mScreenWidth, mGameController.mScreenHeight);
		mCanvas.drawBitmap(mGameBackground, null, r, mPaint);
	}
	
	private void renderJiaoFen() {
		// 
	}
}
