package com.forgepoker;

import com.forgepoker.model.Card;

/**
 * Represents poker card on canvas
 * @author zhanglo
 *
 */
public class CardSceneNode extends SceneNode {
	private int mImageIndex = 0;
	private Card mCard;
	
	public CardSceneNode(Card c) {
		mCard = c;
	}
	
	public Card card() {
		return mCard;
	}	
}
