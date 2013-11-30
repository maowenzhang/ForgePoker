package com.forgepoker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One group of cards could produce
 * @author zhanglo
 *
 */
public class Suit {
	public enum EType {
		Invalid,
		Single,
		Double,
		Triple,
		TripleWithOne,
		TripleWithTwo,
		SingleSequence,
		DoubleSequence,
		TripleSequence,
		TripleWithOneSequence,
		TripleWithTwoSequence,
		FourWithOne,
		FourWithTwo,
		Bomb,
		Rocket
	}
	
	private EType mType = EType.Invalid;
	private List<Card> mCards = new ArrayList<Card>();
	private int mPoints = 0;
	
	public Suit() {
	}
	
	public Suit(List<Card> cards) {
		mCards = cards;
		Collections.sort(mCards);
		initType();
	}
	
	private void initType() {
		// figure out type
		//mType;
	}
	
	public EType type() {
		return mType;
	}
	
	public void setType(EType type) {
		mType = type;
	}
	
	public List<Card> cards() {
		return mCards;
	}
	
	public int points() {
		return mPoints;
	}
	
	public void setPoints(int val) {
		mPoints = val;
	}

}
