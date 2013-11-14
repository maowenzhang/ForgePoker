package com.forgepoker;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.forgepoker.model.Card;
import com.forgepoker.model.Player;

public class PlayerRender {
	
	private GameController mGameController;
	private GameViewRender mViewRender;
	private Canvas mCanvas;
	private Context mContext;
	
	public PlayerRender(GameViewRender viewRender, Context context) {
		mViewRender = viewRender;
		mContext = context;
		mGameController = GameController.get();
	}
	
	public void init() {
		initPlayerImages();
	}
	
	public void render(Canvas canvas) {
		mCanvas = canvas;
		renderPlayers();
	}
	
	/**
	 * Render all players
	 * all info of player (picture, name, score, also pokes, etc.)
	 */
	private void renderPlayers() {
		for (Player p: mGameController.players()) {
			renderPlayer(p);
		}
		
		// render host
	}
	
	private void renderPlayer(Player p) {
		if (p.isCurrentPlayer())
			renderCurrentPlayer(p);
		else
			renderOtherPlayer(p);
	}
	
	private int mAvatarWidthHeight = 50;
	private void renderCurrentPlayer(Player p) {
		// general
		int totalCardWidth = (mGameController.mScreenWidth * 2) / 3;		
		int top = mGameController.mScreenHeight - cardHeight - bottomDelta; 
		
		// render player basic attributes		
		int left = (mGameController.mScreenWidth - totalCardWidth) / 2 - mAvatarWidthHeight;
		Rect des1 = new Rect(left, top, left+60, top+60);
		renderPlayerBasic(p, des1);
		
		// render cards		
		int indexOfCard = 0;
		for (Card c: p.cards()) {
			indexOfCard++;
			Rect des = getCardPosition(indexOfCard, p.cards().size(), totalCardWidth, top);
			renderCard(c, des);
		}
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
		int left = leftRightDelta;
		if (!isLeftOrRight) {
			left = mGameController.mScreenWidth - left - cardWidth;
		}
		int top = (mGameController.mScreenHeight - totalCardHeight) / 2 - mAvatarWidthHeight;
		Rect des1 = new Rect(left, top, left+mAvatarWidthHeight, top+mAvatarWidthHeight);		
		renderPlayerBasic(p, des1);
		
		int indexOfCard = 0;
		for (Card c: p.cards()) {
			indexOfCard++;
			Rect des = getCardPositionLeftOrRight(indexOfCard, p.cards().size(), isLeftOrRight, totalCardHeight);
			renderCardBack(des);
		}
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
		mCanvas.drawBitmap(mPlayerImages.get(p), null, des, mPaint);	
	}
}
