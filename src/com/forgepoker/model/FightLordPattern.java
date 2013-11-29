package com.forgepoker.model;

import java.util.LinkedList;
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
		mHasLowerLimitation = value;
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
	public int calcRank(LinkedList<Card> cards) {
		if(cards == null || cards.size() <= 0)
			return 0;
		//TODO: how to calculate the rank with a consistent approach?
		return 0;
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
