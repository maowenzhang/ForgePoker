package com.forgepoker.model;

import java.util.List;

/**
 * Represents game player
 * @author zhanglo
 *
 */
public class Player {
	private String mName;
	private String mAvatar;
	private int mScore;
	private List<Card> mCards;
	private boolean mIsLord;
	
	public Player() {
		mIsLord = false;
	}
	
	public boolean isLord() {
		return mIsLord;
	}

}
