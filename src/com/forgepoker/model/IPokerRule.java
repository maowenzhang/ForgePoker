package com.forgepoker.model;

import java.util.List;

public interface IPokerRule {
	
	/// How many players in the poker game.
	public int playerCount();

	/// Indicates the players is not fixed. It can vary
	/// in a range.
	public boolean dynamicPlayers();
	
	public int playersLowerBound();
	
	public int playersUpperBound();
	
	/// The deck contents
	public Deck deck();
	
	/// The number of base cards
	public int baseCards();
	
	/// How many decks of the Poker game can be used.
	public int deckCount();
	
	/// Indicates the deck is not fixed. It can vary 
	/// in a range.
	public boolean dynamicDecks();
	
	public int decksLowerBound();
	
	public int decksUpperBound();
	
	/// Returns the patterns for the poker game.
	public List<ICardPattern> patterns();
	
	/// Indicates if a player can pass a round if he/she doesn't have a bigger hand.
	public boolean canPassRound();
	
	/// Indicates whether or not to display rival's cards when rendering.
	/// By default, the flag is false and just used for debugging.
	public boolean showRivalCards();
	
	/// Indicates whether or not the input cards are matched this pattern definition.
	public ICardPattern matched(Suit suit);
}
