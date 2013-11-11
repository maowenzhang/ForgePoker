package com.forgepoker.model;

/**
 * Represents game player
 * @author zhanglo
 *
 */
public class Player {
	private String mName;
	private String mAvatar;
	private int mScore;
	private Card[] mCards;
	private boolean mIsLord;
	
	public Player() {
		mIsLord = false;
	}
	
	public boolean isLord() {
		return mIsLord;
	}
}
