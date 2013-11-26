package com.forgepoker.model;

/**
 * Represent one poker card
 * @author zhanglo
 *
 */
public class Card implements Comparable<Card> {
	
	/** Card type */
	public enum EType {
		Three('3'),
		Four('4'),
		Five('5'),
		Six('6'),
		Seven('7'),
		Eight('8'),
		Nine('9'),
		Ten('A'),
		Jack('B'),
		Queen('C'),
		King('D'),
		Ace('E'),
		Two('2'),
		BlackJoke('J'),
		RedJoke('K');
		
		private char shortName; 
		public char getShort() {
			return this.shortName;
		}
		private EType(char value) {
			this.shortName = value;
		}
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
	private boolean mSelected = false;
	
	public Card(EType type, ESuit suit, int rank, int imageIndex) {
		mType = type;
		mSuit = suit;
		mRank = rank;
		mImageIndex = imageIndex;
	}
	
	public int compareTo(Card c) {
		if (mRank == c.rank()) {
			return 0;
		}
		if (mRank < c.rank()) {
			return -1;
		}
		return 1;
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
	
	public boolean selected() {
		return mSelected;
	}
	
	public void selected(boolean val) {
		mSelected = val;
	}
}