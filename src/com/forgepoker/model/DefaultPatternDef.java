package com.forgepoker.model;

import java.util.ArrayList;
import java.util.List;

import jregex.Matcher;
import jregex.Pattern;

public class DefaultPatternDef extends PatternDef {

	/// Use a string to describe the default pattern definition.
	/// It will be regex string.
	private List<String> mPatterns = new ArrayList<String>();
	
	public DefaultPatternDef(List<String> patterns)
	{
		super();
		mPatterns = patterns;
	}

	public List<String> getPattern() {
		return mPatterns;
	}
	
	public void setPattern(List<String> patterns) {
		mPatterns = patterns;
	}
	
	@Override
	public boolean matched(List<Card> cards)
	{
		try {
			String inputPattern = cardsToString(cards);
			String revInputPattern = new StringBuffer(inputPattern).reverse().toString();
			for(String strPattern : mPatterns)
			{
				Pattern p = new Pattern(strPattern);
				Matcher m = p.matcher(inputPattern);
				if(m.matches())
					return true;
				else
				{
					m = p.matcher(revInputPattern);
					if(m.matches())
						return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public int getMatchResult(List<Card> cards, List<String> matchedSuits)
	{
		try {
			String inputPattern = cardsToString(cards);
			for(String strPattern : mPatterns)
			{
				Pattern p = new Pattern(strPattern);
				Matcher m = p.matcher(inputPattern);
				while(m.find())
				{
					String result = m.toString();
					matchedSuits.add(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matchedSuits.size();
	}
	
	/// Convert input cards to a special string for matching.
	public String cardsToString(List<Card> cards)
	{
		// The cards have been sorted in creating a suit.
		//Collections.sort(cards);
		
		StringBuilder builder = new StringBuilder();
		for(Card card : cards)
		{
			builder.append(card.type().getShort());
		}
		
		String inputPattern = builder.toString();
		return inputPattern;
	}
}
