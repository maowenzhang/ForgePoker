package com.forgepoker.model;

import java.util.LinkedList;

public interface ICardPattern {
	
	/// The unique name id of the pattern.
	public String name();
	
	/// The displaying name of the pattern.
	public String caption();
	
	/// Indicates whether or not the card is limited at a minimum count.
	public boolean hasLowerLimitation();
	/// Indicates whether or not the card is limited at a maximum count.
	public boolean hasUpperLimitation();
		
	/// The minimum card count.
	public int minCards();
	
	/// The maximum card count.
	public int maxCards();
	
	/// Calculate the rank for input cards follow the pattern. The rank will be used to check if
	/// one pattern cards is greater or lesser than another.
	/// By default, it only makes sense to compare the rank between same pattern type,
	/// unless a pattern is allowed to be used in another pattern comparing.
	/// 
	public int calcRank(LinkedList<Card> cards);
	
	/// The weight will be used in calculate the rank. Normally, bigger weight, bigger rank.
	public int weight();
	
	/// Indicates whether or not the cards in the pattern should have same suit.
	/// Here the suit is like spade, heart, club or diamond.
	public boolean needSameSuit();
	
	/// Normally, we need the same patterns to deal cards from a player. But if this method
	/// returns false, the limitation will be ignored. For example, in Fight Lord game, the
	/// bomb pattern can be dealed in all other patterns as long as its rank is bigger.
	public boolean needMatchPattern();
		
	/// Pattern definition. 
	public PatternDef definition();
	
}
