package com.forgepoker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents game player
 * @author zhanglo
 *
 */
public class Player {
	
	/** basic attributes */
	private String mName;
	private int mAvatar;
	private int mScore;	
	
	/** cards */
	private List<Card> mCards = new ArrayList<Card>();
	private Suit mCurPlayedSuit;
	
	private boolean mIsLord = false;
	private boolean mIsCurrentPlayer = false;
	
	public Player(String name, int avatar, int score) {
		mName = name;
		mAvatar = avatar;
		mScore = score;
	}
	
	public boolean isLord() {
		return mIsLord;
	}
	
	public void isLord(boolean val) {
		mIsLord = val;
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
	
	public Suit curPlayedSuit() {
		return mCurPlayedSuit;
	}
	
	public List<Card> cards() {
		return mCards;
	}

	/** Produce/play cards */
	public boolean playCards(Suit playedSuit) {
		mCurPlayedSuit = playedSuit;
		
		mCards.remove(playedSuit.cards());
		return true;
	}

	public boolean isCurrentPlayer() {
		return mIsCurrentPlayer;
	}

	public void isCurrentPlayer(boolean val) {
		this.mIsCurrentPlayer = val;
	}
}
