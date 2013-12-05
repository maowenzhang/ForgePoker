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
		Diamonds("diamond"), // 方块
		Clubs("club"), // 梅花		
		Hearts("heart"), // 红桃
		Spades("spade"), // 黑桃
		Jokers("joker");
		
		private String name; 
		public String getName() {
			return this.name;
		}
		private ESuit(String value) {
			this.name = value;
		}
	}
	
	private EType mType = EType.Three;
	private ESuit mSuit = ESuit.Clubs;
	private int mRank = 0;
	private int mImageIndex = 0;	// index used to find card's image
	private boolean mIsSelected = false;
	
	public boolean isSelected() {
		return mIsSelected;
	}

	public void setIsSelected(boolean mIsSelected) {
		this.mIsSelected = mIsSelected;
	}

	public Card(EType type, ESuit suit, int rank, int imageIndex) {
		mType = type;
		mSuit = suit;
		mRank = rank;
		mImageIndex = imageIndex;
	}
	
	public int compareTo(Card c) {
		if (mRank == c.rank()) {
			return 0; //mSuit.compareTo(c.suit());
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
	
	static public EType charToType(char c) {
		switch(c)
		{
		case '3': return EType.Three;
		case '4': return EType.Four;
		case '5': return EType.Five;
		case '6': return EType.Six;
		case '7': return EType.Seven;
		case '8': return EType.Eight;
		case '9': return EType.Nine;
		case 'A': return EType.Ten;
		case 'B': return EType.Jack;
		case 'C': return EType.Queen;
		case 'D': return EType.King;
		case 'E': return EType.Ace;
		case '2': return EType.Two;
		case 'J': return EType.BlackJoke;
		case 'K': return EType.RedJoke;
		default:
			assert(false);
			break;
		}
		return EType.Three;
	}
	
	static public ESuit stringToSuit(String suit) {
		if(suit.equals("spade"))
			return ESuit.Spades;
		else if(suit.equals("heart"))
			return ESuit.Hearts;
		else if(suit.equals("club"))
			return ESuit.Clubs;
		else if(suit.equals("diamond"))
			return ESuit.Diamonds;
		else if(suit.equals("joker"))
			return ESuit.Jokers;
		else 
		{
			assert(false);
			return ESuit.Spades;
		}
	}
}