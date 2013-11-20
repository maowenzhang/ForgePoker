package com.forgepoker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameViewRender {
	
	static int mScreenWidth = 0;
	static int mScreenHeight = 0;
	static Paint mPaint = new Paint();
	private Bitmap mGameBackground;
	
	private Context mContext;
	private CardRender mCardRender;
	private PlayerRender mPlayerRender;
	private PlayActionRender mPlayActionRender;
	private boolean mHasInit = false;
	
	/** margins */
	static int mLeftOrRightMargin = 10;
	static int mBottomOrTopMargin = 10;
	
	public GameViewRender(Context context) {
		mContext = context;
		mPaint = new Paint();
		
		mGameBackground = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.game_background);
	}
	
	public void init() {
		if (mHasInit) {
			return;
		}
		mCardRender = new CardRender(this, mContext);
		mPlayerRender = new PlayerRender(this, mContext);
		mCardRender.init();
		mPlayerRender.init();
		mPlayActionRender = new PlayActionRender(mContext);
		mPlayActionRender.init();
		mHasInit = true;
	}
	
	public void render(Canvas canvas) {
		renderBackground(canvas);			
		mCardRender.render(canvas);
		mPlayerRender.render(canvas);
		mPlayActionRender.render(canvas);
	}
	
	private void renderBackground(Canvas canvas) {
		Rect r = new Rect(0, 0, mScreenWidth, mScreenHeight);
		canvas.drawBitmap(mGameBackground, null, r, mPaint);
	}

	public boolean OnTouch(int x, int y) {
		if (!mHasInit) {
			return true;
		}
		
		if (mPlayActionRender.OnTouch(x, y))
			return true;
		if (mCardRender.OnTouch(x, y))
			return true;
		
		return false;
	}
}
