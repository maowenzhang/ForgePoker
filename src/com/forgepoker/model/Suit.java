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
		Invalid("Invalid"),
		Single("Single"),
		Double("Double"),
		Triple("Triple"),
		TripleWithOne("TripleOne"),
		TripleWithTwo("TripleTwo"),
		SingleSequence("SingleSequence"),
		DoubleSequence("DoubleSequence"),
		TripleSequence("TripleSequence"),
		TripleWithOneSequence("TripleOneSequence"),
		TripleWithTwoSequence("TripleTwoSequence"),
		FourWithOne("FourOne"),
		FourWithTwo("FourTwo"),
		Bomb("Bomb"),
		Rocket("Rocket");
		
		private String mTypeName; 
		public String getName() {
			return this.mTypeName;
		}
		private EType(String value) {
			this.mTypeName = value;
		}
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
		Collections.reverse(mCards);
		initType();
	}
	
	public Suit(List<Card> cards, EType type)
	{
		mCards = cards;
		Collections.sort(mCards);
		Collections.reverse(mCards);
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
		
		// Calculate the points of the suit
		ICardPattern pattern = RuleManager.get().getPatternByName(type.getName());
		assert(pattern != null);
		if(pattern != null)
		{
			mPoints = pattern.calcPoints(this);
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
		// check if the point is bigger
		if(points() <= comp.points())
			return -1;
		else
			return 1;
	}	
	
	public int sequenceCount() {
		return mSequenceCount;
	}
}
