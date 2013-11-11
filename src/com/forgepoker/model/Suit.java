package com.forgepoker.model;

import java.util.ArrayList;
import java.util.List;

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
	private List<Card> mCards;
	
	public Suit(List<Card> cards) {
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
	
	public List<Card> cards() {
		return mCards;
	}
}
