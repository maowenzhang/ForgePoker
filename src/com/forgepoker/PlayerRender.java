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
	
	private int mBottomDelta = 100;
	private Rect mDesRect;	
	private int mAvatarWidthHeight = 50;
	
	
	public PlayerRender(GameViewRender viewRender, Context context) {
		mViewRender = viewRender;
		mContext = context;
		mGameController = GameController.get();
	}
	
	public void init() {
		// general
		int totalCardWidth = (mGameController.mScreenWidth * 2) / 3;		
		int top = mGameController.mScreenHeight - mBottomDelta;		
		// render player basic attributes		
		int left = (mGameController.mScreenWidth - totalCardWidth) / 2 - mAvatarWidthHeight;
		mDesRect = new Rect(left, top, left+60, top+60);		
		
		initPlayerImages();
	}
	
	/**
	 * Render all players
	 * all info of player (picture, name, score, also pokes, etc.)
	 */
	public void render(Canvas canvas) {
		mCanvas = canvas;
		
		for (Player p: mGameController.players()) {
			if (p.isCurrentPlayer())
				renderCurrentPlayer(p);
			else
				renderOtherPlayer(p);
		}
	}
	
	private void renderCurrentPlayer(Player p) {
		renderPlayerBasic(p, mDesRect);
	}
	
	private void renderOtherPlayer(Player p) {
		// general
		int totalCardHeight = (mGameController.mScreenHeight * 1) / 6;
	
		// render cards
		//
		boolean isLeftOrRight = true;
		if (p.seatIndex() != 1) {
			isLeftOrRight = false;
		}
		
		// render player basic attributes
		int left = GameViewRender.mLeftOrRightSideMargin;
		if (!isLeftOrRight) {
			left = mGameController.mScreenWidth - left - CardRender.mCardWidth;
		}
		int top = (mGameController.mScreenHeight - totalCardHeight) / 2 - mAvatarWidthHeight;
		Rect des1 = new Rect(left, top, left+mAvatarWidthHeight, top+mAvatarWidthHeight);	
		
		renderPlayerBasic(p, des1);
	}
	
	private Map<Player, Bitmap> mPlayerImages = new HashMap<Player, Bitmap>();
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
