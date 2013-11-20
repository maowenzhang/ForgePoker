package com.forgepoker;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.forgepoker.model.Player;

public class PlayerRender {
	
	private GameController mGameController;
	private GameViewRender mViewRender;
	private Canvas mCanvas;
	private Context mContext;
	
	/** Layout attributes
	 */
	static int mAvatarWidthHeight = 60;
	private Rect mCurPlayerDesRect;
	private Rect mLeftPlayerDesRect;
	private Rect mRightPlayerDesRect;
	private Map<Player, Bitmap> mPlayerImages = new HashMap<Player, Bitmap>();
	
	public PlayerRender(GameViewRender viewRender, Context context) {
		mViewRender = viewRender;
		mContext = context;
		mGameController = GameController.get();
	}
	
	public void init() {
		
		// for current player
		int left = GameViewRender.mLeftOrRightMargin;
		int bottom = GameViewRender.mScreenHeight - GameViewRender.mBottomOrTopMargin - CardRender.mCardHeight;
		mCurPlayerDesRect = new Rect(left, 
				bottom - mAvatarWidthHeight, 
				left + mAvatarWidthHeight, 
				bottom);
		
		// for left player
		left = GameViewRender.mLeftOrRightMargin;
		int top = GameViewRender.mBottomOrTopMargin + 20;
		bottom = top + mAvatarWidthHeight;
		mLeftPlayerDesRect = new Rect(left, top, left + mAvatarWidthHeight, bottom);
		
		int right = GameViewRender.mScreenWidth - GameViewRender.mLeftOrRightMargin;
		mRightPlayerDesRect = new Rect(right - mAvatarWidthHeight, top, right, bottom);
		
		initPlayerImages();
	}
	
	/**
	 * Render all players
	 * all info of player (picture, name, score, also pokes, etc.)
	 */
	public void render(Canvas canvas) {
		mCanvas = canvas;
		
		for (Player p: mGameController.players()) {
			if (p.isCurrentPlayer()) {
				renderPlayerBasic(p, mCurPlayerDesRect);
			}
			// left player
			else if (p.seatIndex() == 1) {
				renderPlayerBasic(p, mLeftPlayerDesRect);
			}
			// right player
			else
				renderPlayerBasic(p, mRightPlayerDesRect);
		}
	}
	
	private void initPlayerImages() {
		for (Player p: mGameController.players()) {
			Bitmap img = BitmapFactory.decodeResource(mContext.getResources(), p.avatar());		
			mPlayerImages.put(p, img);
		}
	}
	
	private void renderPlayerBasic(Player p, Rect des) {
		// TODO: draw name, score
		mCanvas.drawBitmap(mPlayerImages.get(p), null, des, GameViewRender.mPaint);	
	}
}
