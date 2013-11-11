package com.forgepoker.model;

/**
 * Represent one poker card
 * @author zhanglo
 *
 */
public class Card {
	
	/** Card type */
	public enum EType {
		One,
		Two,
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
		BlackJoke,
		RedJoke
	}
	
	/** Card suit */
	public enum ESuit {
		Clubs, // 梅花
		Diamonds, // 方块
		Hearts, // 红桃
		Spades // 黑桃
	}
	
	private EType mType = EType.One;
	private ESuit mSuit = ESuit.Clubs;
	private int mRank = 0;
	
	public Card(EType type, ESuit suit, int rank) {
		mType = type;
		mSuit = suit;
		mRank = rank;
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
	
	public String toString() {
		return mType.toString() + "-" + mSuit.toString();
	}
}