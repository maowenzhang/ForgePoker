package com.forgepoker;

import android.graphics.Rect;

import com.forgepoker.model.Card;

/**
 * Represents poker card on canvas
 * @author zhanglo
 *
 */
public class CardSceneNode extends SceneNode {
	private int mImageIndex = 0;
	private Card mCard;
	
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
}
