package com.forgepoker;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.forgepoker.model.Card;
import com.forgepoker.model.Deck;
import com.forgepoker.model.Player;

/** Render poker card
 * 
 * @author zhanglo
 *
 */
public class CardRender {
	/** Card attributes */
	
	static int mCardHeight = 94;
	static int mCardWidth = 70;
	private int mCardHeightImage = 52;
	private int mCardWidthImage = 35;
	private Bitmap mCardsImage;
	
	private GameController mGameController;
	private GameViewRender mViewRender;
	private Canvas mCanvas;
	private Context mContext;	
	
	public int cardHeight() {
		return mCardHeight;
	}
	
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
		
		for (Player p: mGameController.players()) {
			if (p.isCurrentPlayer())
				renderCurrentPlayer(p);
			else
				renderOtherPlayer(p);
		}
	}
	
	public void renderCurrentPlayer(Player p) {
		int totalCardWidth = (mGameController.mScreenWidth * 2) / 3;
		int indexOfCard = 0;
		int top = mGameController.mScreenHeight - mCardHeight - GameViewRender.mBottomSideMargin;
		for (Card c: p.cards()) {
			indexOfCard++;
			Rect des = getCardPosition(indexOfCard, p.cards().size(), totalCardWidth, top);
			renderCard(c, des);
		}
	}
	
	public void renderOtherPlayer(Player p) {
		
		int totalCardHeight = (mGameController.mScreenHeight * 1) / 6;
	
		boolean isLeftOrRight = true;
		if (p.seatIndex() != 1) {
			isLeftOrRight = false;
		}
		int indexOfCard = 0;
		for (Card c: p.cards()) {
			indexOfCard++;
			Rect des = getCardPositionLeftOrRight(indexOfCard, p.cards().size(), isLeftOrRight, totalCardHeight);
			renderCardBack(des);
		}
	}
	
	private Rect getCardPosition(int indexOfCard, int numOfCards, int totalWidth, int top) {
		
		int eachCardWidthOverlap = totalWidth / numOfCards;		
		int start = (mGameController.mScreenWidth - totalWidth) / 2;
		int left = start + eachCardWidthOverlap * indexOfCard;
		return new Rect(left, top, left + mCardWidth, top + mCardHeight);
	}
	
	private void renderCard(Card c, Rect des) {
		CardSceneNode cnode = mCardNodes.get(c);
		mCanvas.drawBitmap(mCardsImage, cnode.srcRect(), des, null);		
	}
	
	private void renderCardBack(Rect des) {
		mCanvas.drawBitmap(mCardsImage, mCardBackPos, des, null);		
	}
	
	/** Draw poker cards
	 * 
	 */
	private Map<Card, CardSceneNode> mCardNodes = new HashMap<Card, CardSceneNode>();
//	private List<CardSceneNode> mCardNodes = new ArrayList<CardSceneNode>();
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
			int left = mCardWidthImage * col;
			int top = mCardHeightImage * row;
			int right = left + mCardWidthImage;
			int bottom = top + mCardHeightImage;			
			Rect r = new Rect(left, top, right, bottom);
			
			CardSceneNode cnode = new CardSceneNode(c);
			cnode.srcRect(r);
			mCardNodes.put(c,  cnode);
		}
		
		// Card back is at 55th
		int col = 55 % numOfCol;
		int row = 55 / numOfCol;
		int left = mCardWidthImage * col;
		int top = mCardHeightImage * row;
		int right = left + mCardWidthImage;
		int bottom = top + mCardHeightImage;			
		mCardBackPos = new Rect(left, top, right, bottom);
	}
	
	
	private Rect getCardPositionLeftOrRight(int indexOfCard, int numOfCards, boolean isLeftOrRight, int totalHeight) {
		
		int eachCardOverlap = totalHeight / numOfCards;		
		int start = (mGameController.mScreenHeight - totalHeight) / 2;
		int left = GameViewRender.mLeftOrRightSideMargin;
		int top = start + eachCardOverlap * indexOfCard;
		
		if (!isLeftOrRight) {
			left = mGameController.mScreenWidth - left - mCardWidth;
		}
		
		return new Rect(left, top, left + mCardWidth, top + mCardHeight);
	}
}
