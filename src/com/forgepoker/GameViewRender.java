package com.forgepoker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameViewRender {
	
	GameController mGameController;
	static Paint mPaint = new Paint();
	private Bitmap mGameBackground;
	
	private Canvas mCanvas;
	private Context mContext;
	private CardRender mCardRender;
	private PlayerRender mPlayerRender;
	private PlayActionRender mPlayActionRender;
	
	/** margins */
	static int mLeftOrRightSideMargin = 20;
	static int mBottomSideMargin = 30;
	
	public GameViewRender(GameController gameController, Context context) {
		mGameController = gameController;
		mContext = context;
		mPaint = new Paint();
		
		mGameBackground = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.game_background);
		
		mCardRender = new CardRender(this, context);
		mPlayerRender = new PlayerRender(this, context);
		mCardRender.init();
		mPlayerRender.init();
		mPlayActionRender = new PlayActionRender(context);
		mPlayActionRender.init();
	}
	
	public void render(Canvas canvas) {
		renderBackground(canvas);			
		mCardRender.render(canvas);
		mPlayerRender.render(canvas);
		mPlayActionRender.render(canvas);
	}
	
	private void renderBackground(Canvas canvas) {
		Rect r = new Rect(0, 0, mGameController.mScreenWidth, mGameController.mScreenHeight);
		canvas.drawBitmap(mGameBackground, null, r, mPaint);
	}

	public boolean OnTouch(int x, int y) {
		if (mPlayActionRender.OnTouch(x, y))
			return true;
		if (mCardRender.OnTouch(x, y))
			return true;
		
		return false;
	}
}
