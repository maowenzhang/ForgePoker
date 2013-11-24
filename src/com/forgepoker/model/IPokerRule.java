package com.forgepoker.model;

import java.util.ArrayList;

public interface IPokerRule {
	
	/// How many players in the poker game.
	public int playerCount();

	/// Indicates the players is not fixed. It can vary
	/// in a range.
	public boolean dynamicPlayers();
	
	public int playersLowerBound();
	
	public int playersUpperBound();
	
	/// How many decks of the Poker game can be used.
	public int deckCount();
	
	/// Indicates the deck is not fixed. It can vary 
	/// in a range.
	public boolean dynamicDecks();
	
	public int decksLowerBound();
	
	public int decksUpperBound();
	
	/// Returns the patterns for the poker game.
	public ArrayList<ICardPattern> patterns();
	
	/// Indicates if a player can pass a round if he/she doesn't have a bigger hand.
	public boolean canPassRound();
}
