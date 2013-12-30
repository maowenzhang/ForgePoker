package com.forgepoker;

import android.graphics.Rect;

import com.forgepoker.model.Card;

/**
 * Represents poker card on canvas
 * @author zhanglo
 *
 */
public class CardSceneNode extends SceneNode {
	//TODO: create sceneGraph to manager scene nodes
	//private int mImageIndex = 0;
	private Card mCard;
	private boolean mIsHit = false;
	
	public CardSceneNode(Card c, Rect srcRect)
	{
		super(srcRect);
		mCard = c;
	}
	
	public CardSceneNode(Card c)
	{
		super(null);
		mCard = c;
	}
	
	public Card card() {
		return mCard;
	}	
	
	public boolean isHit(int x, int y) {
		mIsHit = false;
		
		if (desRect() == null)
			return false;
		
		if (desRect().contains(x, y)) {
			mIsHit = true;
			mCard.setIsSelected(!mCard.isSelected());
			return true;
		}
		return false;
	}
	
	public boolean isHit() {
		return mIsHit;
	}
	public boolean isSelected() {
		return mCard.isSelected();
	}
}
