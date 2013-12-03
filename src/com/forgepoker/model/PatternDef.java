package com.forgepoker.model;

import java.util.List;

/// This is a abstract class for the definition of a pattern. The intent is 
/// just to describe the valid structure for the pattern.
public abstract class PatternDef {
	
	protected PatternDef() { }
	
	/// Indicates whether or not the input cards are matched this pattern definition.
	public boolean matched(List<Card> cards)
	{
		return false;
	}
	
	public abstract int getMatchResult(List<Card> cards, List<String> matchedSuits);
	
}
