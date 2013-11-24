package com.forgepoker.model;

import java.util.LinkedList;

/// This is a abstract class for the definition of a pattern. The intent is 
/// just to describe the valid structure for the pattern.
public abstract class PatternDef {
	
	protected PatternDef() { }
	
	/// Indicates whether or not the input cards are matched this pattern definition.
	public boolean matched(LinkedList<Card> cards)
	{
		return false;
	}
	
}
