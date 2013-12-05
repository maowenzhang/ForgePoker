package com.forgepoker.model;

import java.util.List;

/**
 * Represents game player
 * 
 * @author zhanglo
 * 
 */
public class Player {

	/** basic attributes */
	private String mName;
	private int mAvatar;
	private int mScore;
	private int mBid = -1;
	private boolean mHasBid = false;

	private boolean mIsLord = false;
	//private boolean mIsCurrentPlayer = false;
	private int mSeatIndex = 1;
	private boolean mIsRobot = true;
	
	private AIRobot mAI = null;

	public Player(String name, int avatar, int score, boolean isRobot) {
		mName = name;
		mAvatar = avatar;
		mScore = score;
		mIsRobot = isRobot;
		
		// TODO create simple AIRobot for now
		mAI = AIFactory.createAIRobot(AIFactory.AIDifficulty.valueOf(1));
	}

	public boolean isLord() {
		return mIsLord;
	}

	public void setLord(List<Card> baseCards) throws InterruptedException {
		mIsLord = true;	
		this.addBaseCard(baseCards);
		if(isRobot())
		{
			Thread.sleep(2 * 1000);
			clearSelectedCards();
			mAI.reGroupCards();	// group the cards after updating them
		}
	}

	public String name() {
		return mName;
	}

	public int avatar() {
		return mAvatar;
	}

	public int score() {
		return mScore;
	}

	public void score(int val) {
		mScore = val;
	}

	public int bid() {
		return mBid;
	}

	public void bid(int val) {
		assert (mHasBid == false);
		mBid = val;
		if (val != 0) {
			mHasBid = true;
		}
	}

	public boolean hasBid() {
		return mHasBid;
	}

	public int seatIndex() {
		return mSeatIndex;
	}

	public void seatIndex(int val) {
		mSeatIndex = val;
	}

	public boolean isRobot() {
		return mIsRobot;
	}
	
	public Suit curPlayedSuit() {
		return mAI.curPlayedSuit();
	}
	
	public void clearCurPlayedSuit() {
		mAI.clearCurPlayedSuit();
	}

	public List<Card> cards() {
		return mAI.cards();
	}

	public void cards(List<Card> cards) {
		mAI.cards(cards);
		mAI.groupSuits();	// group the cards after got them
	}

	/** Produce/play cards */
	public boolean playCards(Suit curPlayedSuit) {
		
		return mAI.playCards(curPlayedSuit);
	}


	public Suit showCards(Suit lastPlayedSuit) {
		Suit robotShowSuit = null;

		if(null == lastPlayedSuit)
			robotShowSuit = mAI.playSuit(0);
		else
			robotShowSuit = mAI.playCalledSuit(lastPlayedSuit, 0);
		
		if( null != robotShowSuit)
		{
			playCards(robotShowSuit);
		}
		
		return robotShowSuit;
	}

	public void clearSelectedCards() {
		mAI.clearSelectedCards();
	}
	
	public void reselectCards() {
		mAI.reselectCards();
	}
	
	public List<Card> selectedCards() 
	{
		return mAI.selectedCards();
	}
	
	public Suit selectedSuit() 
	{
		return mAI.selectedSuit();
	}
		
	public int getBidNum(int curBidNumber)
	{
		// TODO AI should do it
		mBid = curBidNumber + 1;
		if (mBid != 0) {
			mHasBid = true;
		}
		return mBid;
	}
	
	private void addBaseCard(List<Card> baseCards)
	{
		mAI.addBaseCard(baseCards);	
	}
}
