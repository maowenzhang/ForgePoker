package com.forgepoker;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.forgepoker.model.Card;
import com.forgepoker.model.Deck;

/** Render poker card
 * 
 * @author zhanglo
 *
 */
public class CardRender {
	/** Card attributes */
	
	private int leftRightDelta = 20;
	private int bottomDelta = 30;
	private int cardHeight = 94;
	private int cardWidth = 70;
	private int cardHeightImage = 52;
	private int cardWidthImage = 35;
	private Bitmap mCardsImage;
	
	private GameController mGameController;
	private GameViewRender mViewRender;
	private Canvas mCanvas;
	private Context mContext;	
	
	public CardRender(GameViewRender viewRender, Context context) {
		mViewRender = viewRender;
		mContext = context;
		mGameController = GameController.get();
		mCardsImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cards);
	}
	
	public void init() {
		initCardNodes();
	}
	
	public void render(Canvas canvas) {
		mCanvas = canvas;
	}
	
	private Rect getCardPosition(int indexOfCard, int numOfCards, int totalWidth, int top) {
		
		int eachCardWidthOverlap = totalWidth / numOfCards;		
		int start = (mGameController.mScreenWidth - totalWidth) / 2;
		int left = start + eachCardWidthOverlap * indexOfCard;
		return new Rect(left, top, left + cardWidth, top + cardHeight);
	}	
	
	/** Draw poker cards
	 * 
	 */
	private List<CardSceneNode> mCardNodes = new ArrayList<CardSceneNode>();
	private Rect mCardBackPos;
	private void initCardNodes() {
		if (mCardNodes.size() > 0) {
			return;
		}
		
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
			
			CardSceneNode cnode = new CardSceneNode(c);
			cnode.srcRect(r);
			mCardNodes.add(cnode);
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
	
	private void renderCardBack(Rect des) {
		mCanvas.drawBitmap(mCardsImage, mCardBackPos, des, null);		
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
}
