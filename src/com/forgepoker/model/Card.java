package com.forgepoker.model;

/**
 * Represent one poker card
 * @author zhanglo
 *
 */
public class Card {
	
	/** Card type */
	public enum EType {
		Three,
		Four,
		Five,
		Six,
		Seven,
		Eight,
		Nine,
		Ten,
		Jack,
		Queen,
		King,
		Ace,
		Two,
		BlackJoke,
		RedJoke
	}
	
	/** Card suit */
	public enum ESuit {
		Diamonds, // 方块
		Clubs, // 梅花		
		Hearts, // 红桃
		Spades, // 黑桃
		Jokers
	}
	
	private EType mType = EType.Three;
	private ESuit mSuit = ESuit.Clubs;
	private int mRank = 0;
	private int mImageIndex = 0;	// index used to find card's image
	
	public Card(EType type, ESuit suit, int rank, int imageIndex) {
		mType = type;
		mSuit = suit;
		mRank = rank;
		mImageIndex = imageIndex;
	}
	
	public EType type() {
		return mType;
	}
	
	public ESuit suit() {
		return mSuit;
	}
	
	public int rank() { 
		return mRank;
	}
	
	public String id() {
		return toString();
	}
	
	public String toString() {
		return mType.toString() + "-" + mSuit.toString();
	}

	public int imageIndex() {
		return mImageIndex;
	}

	public void imageIndex(int val) {
		mImageIndex = val;
	}
}