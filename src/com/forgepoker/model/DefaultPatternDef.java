package com.forgepoker.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class DefaultPatternDef extends PatternDef {

	/// Use a string to describe the default pattern definition.
	/// It will be regex string.
	private String mPattern;
	
	public DefaultPatternDef(String strPattern)
	{
		super();
		mPattern = strPattern;
	}

	public String getPattern() {
		return mPattern;
	}
	
	public void setPattern(String strPattern) {
		mPattern = strPattern;
	}
	
	@Override
	public boolean matched(LinkedList<Card> cards)
	{
		String inputPattern = cardsToString(cards);
		Pattern p = Pattern.compile(mPattern);
		boolean ret = p.matcher(inputPattern).matches();
		return ret;
	}
	
	/// Convert input cards to a special string for matching.
	public String cardsToString(LinkedList<Card> cards)
	{
		Collections.sort(cards);
		Collections.reverse(cards);
		
		StringBuilder builder = new StringBuilder();
		for(Card card : cards)
		{
			builder.append(card.type().getShort());
		}
		
		String inputPattern = builder.toString();
		return inputPattern;
	}
}