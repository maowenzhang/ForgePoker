package com.forgepoker;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.forgepoker.model.Card;
import com.forgepoker.model.Deck;
import com.forgepoker.model.Player;

public class GameViewRender {
	
	GameController mGameController;
	private Paint mPaint;
	private Bitmap mGameBackground;
	private Bitmap mCardsImage;
	private Canvas mCanvas;
	private Context mContext;
	
	public GameViewRender(GameController gameController, Context context) {
		mGameController = gameController;
		mContext = context;
		mPaint = new Paint();
		
		mGameBackground = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.game_background);
		mCardsImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cards);
		initCardImageData();
		initPlayerImages();
	}
	
	public void render(Canvas canvas) {
		mCanvas = canvas;
		renderBackground();
		renderPlayers();
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
	
	private void renderCardBack(Rect des) {
		mCanvas.drawBitmap(mCardsImage, mCardBackPos, des, mPaint);		
	}
	
	private Rect getCardPositionLeftOrRight(int indexOfCard, int numOfCards, boolean isLeftOrRight, int totalHeight) {
		
		int eachCardOverlap = totalHeight / numOfCards;		
		int start = (mGameController.mScreenHeight - totalHeight) / 2;
		int left = leftRightDelta;
		int top = start + eachCardOverlap * indexOfCard;
		
		if (!isLeftOrRight) {
			left = mGameController.mScreenWidth - left - cardWidth;
		}
		
		return new Rect(left, top, left + cardWidth, top + cardHeight);
	}
	
	/** Card attributes */
	
	private int leftRightDelta = 20;
	private int bottomDelta = 30;
	private int cardHeight = 94;
	private int cardWidth = 70;
	private int cardHeightImage = 52;
	private int cardWidthImage = 35;
	
	private Rect getCardPosition(int indexOfCard, int numOfCards, int totalWidth, int top) {
	
		
		int eachCardWidthOverlap = totalWidth / numOfCards;		
		int start = (mGameController.mScreenWidth - totalWidth) / 2;
		int left = start + eachCardWidthOverlap * indexOfCard;
		return new Rect(left, top, left + cardWidth, top + cardHeight);
	}
	
	private void renderJiaoFen() {
		// 
	}
	
	private void renderCard(Card c, Rect des) {
		Rect src = mCardImageData.get(c);		
		mCanvas.drawBitmap(mCardsImage, src, des, mPaint);		
	}
	
	private Map<Card, Rect> mCardImageData = new HashMap<Card, Rect>();
	private Rect mCardBackPos;
	private void initCardImageData() {
		
		if (!mCardImageData.isEmpty()) {
			return;
		}
		
		final int numOfRow = 5;
		final int numOfCol = 13;
		
		Deck deck = mGameController.deck();
		
		for (Card c: deck.cards()) {
			
			int col = c.imageIndex() % numOfCol;
			int row = c.imageIndex() / numOfCol;
			int left = cardWidthImage * col;
			int top = cardHeightImage * row;
			int right = left + cardWidthImage;
			int bottom = top + cardHeightImage;			
			Rect r = new Rect(left, top, right, bottom);
			
			mCardImageData.put(c, r);
		}
		
		// Card back is at 55th
		int col = 55 % numOfCol;
		int row = 55 / numOfCol;
		int left = cardWidthImage * col;
		int top = cardHeightImage * row;
		int right = left + cardWidthImage;
		int bottom = top + cardHeightImage;			
		mCardBackPos = new Rect(left, top, right, bottom);		
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
