package com.forgepoker.model;

import java.util.List;

public class FightLordPattern implements ICardPattern {

	private String mName;
	private String mCaption;
	private boolean mHasLowerLimitation = false;
	private boolean mHasUpperLimitation = false;
	private int mMinCards = 1;
	private int mMaxCards = 1;
	private int mWeight = 1;
	private boolean mNeedSameSuit = false;
	private boolean mNeedMatchPattern = true;
	private PatternDef mPatternDef = null;
	
	public FightLordPattern(String name, 
			String caption,
			List<String> patterns)
	{
		mName = name;
		mCaption = caption;
		mPatternDef = new DefaultPatternDef(patterns);
	}
	
	@Override
	public String name() {
		return mName;
	}

	@Override
	public String caption() {
		return mCaption;
	}
	
	public void setCaption(String caption)
	{
		mCaption = caption;
	}

	@Override
	public boolean hasLowerLimitation() {
		return mHasLowerLimitation;
	}
	
	public void setHasLowerLimitation(boolean value) {
		mHasLowerLimitation = value;
	}

	@Override
	public boolean hasUpperLimitation() {
		return mHasUpperLimitation;
	}

	public void setHasUpperLimitation(boolean value) {
		mHasUpperLimitation = value;
	}
	
	@Override
	public int minCards() {
		return mMinCards;
	}
	
	public void setMinCards(int value)
	{
		mMinCards = value;
	}

	@Override
	public int maxCards() {
		return mMaxCards;
	}

	public void setMaxCards(int value)
	{
		mMaxCards = value;
	}
	
	@Override
	public int calcPoints(Suit suit) {
		
		List<Card> cards = suit.cards();
		if(cards == null || cards.size() <= 0)
			return 0;
		
		int points = 0;
		int szCards = cards.size();
		
		// NOTE: below calculation assumes the biggest cards is the last
		// in the suit.
		switch(suit.type()) {
		case Invalid:
			break;
		case Single:
		case Double:
		case Triple:
		case SingleSequence:
		case DoubleSequence:
		case TripleSequence:
		case Bomb:
		case Rocket:
			points = szCards * cards.get(szCards-1).rank() * weight();
			break;
		case TripleWithOne:
		case TripleWithTwo:
		case TripleWithOneSequence:
		case TripleWithTwoSequence:
		case FourWithOne:
		case FourWithTwo:
			{
				assert(szCards >= 4);
				Card card1, card2, card3;
				int i = 0;
				do {
					card1 = cards.get(i);
					card2 = cards.get(i+1);
					card3 = cards.get(i+2);
					++i;
					if(i >= szCards)
						break;
				} while(card1.type() != card2.type()  || card1.type() != card3.type() || card2.type() != card3.type());
				assert(card1.type() == card2.type() && card1.type() == card3.type());
				points = szCards * card1.rank() * weight();
			}
			break;
		}
		return points;
	}

	@Override
	public int weight() {
		return mWeight;
	}

	public void setWeight(int value)
	{
		mWeight = value;
	}
	
	@Override
	public boolean needSameSuit() {
		return mNeedSameSuit;
	}

	public void setNeedSameSuit(boolean value)
	{
		mNeedSameSuit = value;
	}
	
	@Override
	public boolean needMatchPattern() {
		return mNeedMatchPattern;
	}
	
	public void setNeedMatchPattern(boolean value)
	{
		mNeedMatchPattern = value;
	}

	@Override
	public PatternDef definition() {
		return mPatternDef;
	}

}
