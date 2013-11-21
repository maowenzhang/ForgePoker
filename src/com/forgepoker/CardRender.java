package com.forgepoker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private Context mContext;
	private Canvas mCanvas;
	private Bitmap mCardsImage;						// Image sprite contains all cards

	/** Card render attributes */
	static int mCardHeight = 94;					// Single card height render in canvas
	static int mCardWidth = 70;						// Single card width render in canvas
	private int mCardHeightImage = 52;				// Single card height in image
	private int mCardWidthImage = 35;				// Single card width in image
	static int mCardSelectedPopupHeight = 30; 		// Height of selected card jumps
	
	
	public CardRender(Context context) {
		mContext = context;
	}
	
	public void canvas(Canvas canvas) {
		mCanvas = canvas;
	}
	
	public void init() {
		
		mCardsImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cards);
		
		initCardNodes();
	}
	
	public void renderCard(Card c, Rect des) {
		CardSceneNode cnode = mCardNodes.get(c);
		
		if (cnode.isSelected()) {
			des.offset(0, -mCardSelectedPopupHeight);
		}
		
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
	
	public void renderCards(List<Card> cards, Rect des) {
		
		// get total cards width
		int totalCardWidth = des.width();
		int allRemainCardsWidth = cards.size() * mCardWidth;
		if (totalCardWidth > allRemainCardsWidth) {
			totalCardWidth = allRemainCardsWidth;
		}
		
		int indexOfCard = 0;
		for (Card c: cards) {
			Rect tmp = getCardPosition(indexOfCard++, cards.size(), totalCardWidth, des);
			renderCard(c, tmp);
		}
	}
	
	private Rect getCardPosition(int indexOfCard, int numOfCards, int totalWidth, Rect des) {
		
		int eachCardWidthOverlap = 0;
		if (numOfCards > 1) {
			// last card shows no overlap
			eachCardWidthOverlap = (totalWidth - mCardWidth) / (numOfCards - 1); 
		}
		int delta = 5; // small adjustment
		int start = des.left + delta;
		int left = start + eachCardWidthOverlap * indexOfCard;
		return new Rect(left, des.top, left + mCardWidth, des.top + mCardHeight);
	}
	
	/** Draw poker cards
	 * 
	 */
	private Map<Card, CardSceneNode> mCardNodes = new HashMap<Card, CardSceneNode>();
	private Rect mCardBackPos;
	private void initCardNodes() {
		if (mCardNodes.size() > 0) {
			return;
		}
		
		final int numOfCol = 13;
		
		Deck deck = GameController.get().deck();
		
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
}
