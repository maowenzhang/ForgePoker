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
	
	private void renderCurrentPlayer(Player p) {
		// render player basic attributes
		
		// render cards
		int indexOfCard = 0;
		for (Card c: p.cards()) {
			indexOfCard++;
			Rect des = getCardPosition(indexOfCard, p.cards().size());
			renderCard(c, des);
		}		
	}
	
	private Rect getCardPosition(int indexOfCard, int numOfCards) {
		int bottomDelta = 10;
		int eachCardHeight = 5;
		
		int totalWidth = (mGameController.mScreenWidth * 2) / 3;
		int eachCardWidth = totalWidth / numOfCards;		
		int start = (mGameController.mScreenWidth - totalWidth) / 2;
		int left = start + eachCardWidth * indexOfCard;
		int top = mGameController.mScreenHeight - eachCardHeight - bottomDelta; 
		return new Rect(left, top, left + eachCardWidth, top + eachCardHeight);
	}
	
	private void renderOtherPlayer(Player p) {
		// render left player
		// render right player
	}
	
	private void renderJiaoFen() {
		// 
	}
	
	private void renderCard(Card c, Rect des) {
		Rect src = mCardImageData.get(c);		
		mCanvas.drawBitmap(mCardsImage, src, des, mPaint);		
	}
	
	private Map<Card, Rect> mCardImageData = new HashMap<Card, Rect>();
	private void initCardImageData() {
		int cardWidth = 5;
		final int cardHeight = 5;
		final int numOfRow = 5;
		final int numOfCol = 13;
		
		Deck deck = mGameController.deck();
		
		for (Card c: deck.cards()) {
			
			int left = c.imageIndex() % numOfCol;
			int top = c.imageIndex() / numOfCol;
			int right = left + cardWidth;
			int bottom = top + cardHeight;			
			Rect r = new Rect(left, top, right, bottom);
			
			mCardImageData.put(c, r);
		}
	}
}
