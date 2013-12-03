package com.forgepoker.model;

import java.util.ArrayList;
import java.util.Collections;
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
		FourWithOne,
		FourWithTwo,
		Bomb,
		Rocket
	}
	
	private EType mType = EType.Invalid;
	private List<Card> mCards = new ArrayList<Card>();
	private int mPoints = 0;
	// if the type is a sequence, we need to know the sequence count
	private int mSequenceCount = 0; 
	
	public Suit() {
	}
	
	public Suit(List<Card> cards) {
		mCards = cards;
		Collections.sort(mCards);
		initType();
	}
	
	public Suit(List<Card> cards, EType type)
	{
		mCards = cards;
		Collections.sort(mCards);
		mType = type;
		switch(type)
		{
		case SingleSequence:
			mSequenceCount = cards.size();
			break;
		case DoubleSequence:
			mSequenceCount = cards.size()/2;
			break;
		case TripleSequence:
			mSequenceCount = cards.size()/3;
			break;
		case TripleWithOneSequence:
			mSequenceCount = cards.size()/4;
			break;
		case TripleWithTwoSequence:
			mSequenceCount = cards.size()/5;
			break;
		default:
			mSequenceCount = 0; 
			break;
		}
	}
	
	private void initType() {
		// figure out type
		//mType;
	}
	
	public EType type() {
		return mType;
	}
	
	public void setType(EType type) {
		mType = type;
	}
	
	public List<Card> cards() {
		return mCards;
	}
	
	public int points() {
		return mPoints;
	}
	
	public void setPoints(int val) {
		mPoints = val;
	}
	
	public int compareTo(Suit comp) {
		if(mSequenceCount != comp.sequenceCount())
			return -1;
		return mCards.get(0).compareTo(comp.cards().get(0));
	}	
	
	public int sequenceCount() {
		return mSequenceCount;
	}
}
