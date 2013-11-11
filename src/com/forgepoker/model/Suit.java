package com.forgepoker.model;

/**
 * One group of cards could produce
 * @author zhanglo
 *
 */
public class Suit {
	public enum EType {
		Invalid,
		Single,
		Double,
		Triple,
		TripleWithOne,
		TripleWithTwo,
		SingleSequence,
		DoubleSequence,
		TripleSequence,
		TripleWithOneSequence,
		TripleWithTwoSequence,
		FourWithTwo,
		Bomb,
		Rockets
	}
	
	private EType mType;
	private Card[] mCards;
	
	public Suit(Card[] cards) {
		mCards = cards;
		initType();
	}
	
	private void initType() {
		// figure out type
		//mType;
	}
	
	public EType type() {
		return mType;
	}
	
	public Card[] cards() {
		return mCards;
	}
}
