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
	private boolean mIsCurrentPlayer = false;
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

	public void setLord(List<Card> baseCards) {
		mIsLord = true;	
		this.addBaseCard(baseCards);
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

	public boolean isCurrentPlayer() {
		return mIsCurrentPlayer;
	}

	public void isCurrentPlayer(boolean val) {
		this.mIsCurrentPlayer = val;
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
	}

	/** Produce/play cards */
	public boolean playCards(Suit curPlayedSuit) {
		
		return mAI.playCards(curPlayedSuit);
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
	
	public void addBaseCard(List<Card> baseCards)
	{
		mAI.addBaseCard(baseCards);
	}
}
