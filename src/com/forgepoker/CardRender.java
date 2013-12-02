package com.forgepoker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.forgepoker.model.Card;
import com.forgepoker.model.Deck;
import com.forgepoker.model.Player;
import com.forgepoker.util.ImageUtil;

/**
 * Render poker card
 * 
 * @author zhanglo
 * 
 */
public class CardRender {

	private Context mContext;
	private Canvas mCanvas;
	private Bitmap mCardsImage; // Image sprite contains all cards

	/** Card render attributes */
	static int mCardHeight = 110; // Single card height render in canvas
	static int mCardWidth = 80; // Single card width render in canvas
	private int mCardHeightImage = 110; // Single card height in image
	private int mCardWidthImage = 80; // Single card width in image
	static int mCardSelectedPopupHeight = 20; // Height of selected card jumps
	static int mGapBetweenCards = 36;
	
	public CardRender(Context context) {
		mContext = context;
	}

	public void canvas(Canvas canvas) {
		mCanvas = canvas;
	}

	public void init() {

		mCardsImage = ImageUtil.decodeResource(mContext.getResources(),
				R.drawable.cards);

		initCardNodes();
	}

	public void renderCard(Player player, Card c, Rect des) {
		CardSceneNode cnode = mCardNodes.get(c);

		if (c.isSelected()) {
		
			boolean bDebug = GameController.get().rule().showRivalCards();
			if(bDebug && player.isRobot()) {
				des.offset(player.seatIndex() == 2 ? mCardSelectedPopupHeight : (-mCardSelectedPopupHeight), 0);
			} else {
				des.offset(0, -mCardSelectedPopupHeight);
			}
		}

		cnode.desRect(des);
		mCanvas.drawBitmap(mCardsImage, cnode.srcRect(), des, null);
	}
	
	public void renderCard(Card c, Rect des) {
		CardSceneNode cnode = mCardNodes.get(c);
		cnode.desRect(des);
		mCanvas.drawBitmap(mCardsImage, cnode.srcRect(), des, null);
	}

	public void renderBaseCard(Card c, Rect des) {
		CardSceneNode cnode = mBaseCardNodes.get(c);
		if(cnode == null)
			return;
		cnode.desRect(des);
		mCanvas.drawBitmap(mCardsImage, cnode.srcRect(), des, null);
	}
	
	public void renderCardBack(Rect des) {
		mCanvas.drawBitmap(mCardsImage, mCardBackPos, des, null);
	}

	public boolean isTouched(Card c, int touchPosX, int touchPosY) {
		CardSceneNode cnode = mCardNodes.get(c);
		if (cnode.isHit(touchPosX, touchPosY))
			return true;
		return false;
	}

	public void renderCards(Player player, List<Card> cards, Rect des) {

		synchronized (cards) {
			int indexOfCard = 0;
			for (Card c : cards) {
				Rect tmp = getCardPosition(indexOfCard++, cards.size(), des);
				renderCard(player, c, tmp);
			}
		}
	}
	
	public void renderPlayedCards(Player player, List<Card> cards, Rect des) {

		synchronized (cards) {
			int indexOfCard = 0;
			for (Card c : cards) {
				int left = des.left + mGapBetweenCards * indexOfCard++;
				Rect tmp = new Rect(left, des.top, left + mCardWidth, des.top + mCardHeight);
				renderCard(player, c, tmp);
			}
		}
		
	}

	private Rect getCardPosition(int indexOfCard, int numOfCards, Rect des) {
		
		int totalWidth = numOfCards * mGapBetweenCards + (mCardWidth - mGapBetweenCards);
		int start = (GameViewRender.mScreenWidth - totalWidth)/2; 
		int left = start + mGapBetweenCards * indexOfCard;
		return new Rect(left, des.top, left + mCardWidth, des.top + mCardHeight);
	}

	public void renderCardsVertical(Player player, List<Card> cards, Rect des) {

		synchronized (cards) {
			// get total cards width
			int totalHeight = des.height();
			double showCardRate = 0.4;
			int allRemainCardsHeight = (int) (cards.size() * mCardHeight * showCardRate);
			if (totalHeight > allRemainCardsHeight) {
				totalHeight = allRemainCardsHeight;
			}

			int indexOfCard = 0;
			for (Card c : cards) {
				Rect tmp = getCardPositionVertical(indexOfCard++, cards.size(),
						totalHeight, des);
				renderCard(player, c, tmp);
			}
		}
	}
	
	private Rect getCardPositionVertical(int indexOfCard, int numOfCards, int totalHeight, Rect des) {
		
		int eachCardOverlap = 0;
		if (numOfCards > 1) {
			// last card shows no overlap
			eachCardOverlap = 20;//(totalHeight - mCardHeight) / (numOfCards - 1);
		}

		int top = des.top + eachCardOverlap * indexOfCard;
		return new Rect(des.left, top, des.left + mCardWidth, top + mCardHeight);
	}

	
//	private void renderCurPlayedCards()
//	{
//		List<Card> playedCards = mGameController.CurrentPlayedCards();
//		if(playedCards != null)
//		{
//			int left = (mGameController.mScreenWidth - (mCardWidth + mCardsOffset)*playedCards.size())/2;
//			int top = (mGameController.mScreenHeight - mCardHeight)/2;
//			int bottom = top + mCardHeight;
//			for (Card c : playedCards)
//			{
//				left = left + mCardsOffset;
//				int right = left + mCardWidth;
//				Rect des = new Rect(left, top, right, bottom);
//				CardSceneNode cnode = mCardNodes.get(c);
//				cnode.desRect(des);	
//				mCanvas.drawBitmap(mCardsImage, cnode.srcRect(), des, null);
//			}
//		}
//	}


	/**
	 * Draw poker cards
	 * 
	 */
	private Map<Card, CardSceneNode> mCardNodes = new HashMap<Card, CardSceneNode>();
	private Map<Card, CardSceneNode> mBaseCardNodes = new HashMap<Card, CardSceneNode>();
	private Rect mCardBackPos;

	private void initCardNodes() {
		if (mCardNodes.size() > 0) {
			return;
		}

		final int numOfCol = 13;

		Deck deck = GameController.get().deck();

		for (Card c : deck.cards()) {

			int col = c.imageIndex() % numOfCol;
			int row = c.imageIndex() / numOfCol;
			int left = mCardWidthImage * col;
			int top = mCardHeightImage * row;
			int right = left + mCardWidthImage;
			int bottom = top + mCardHeightImage;
			Rect r = new Rect(left, top, right, bottom);

			CardSceneNode cnode = new CardSceneNode(c);
			cnode.srcRect(r);
			mCardNodes.put(c, cnode);
		}
		
		for (Card c : GameController.get().baseCards()) {
			CardSceneNode cnode = mCardNodes.get(c);
			CardSceneNode cBaseNode = new CardSceneNode(c);
			cBaseNode.srcRect(new Rect(cnode.srcRect()));
			mBaseCardNodes.put(c, cBaseNode);
		}

		// Card back is at 55th
		int col = 2 % numOfCol;
		int row = 54 / numOfCol;
		int left = mCardWidthImage * col;
		int top = mCardHeightImage * row;
		int right = left + mCardWidthImage;
		int bottom = top + mCardHeightImage;
		mCardBackPos = new Rect(left, top, right, bottom);
	}
}
