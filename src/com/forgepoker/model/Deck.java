package com.forgepoker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Used to represent set of poker cards in game
 * @author 
 *
 */
public class Deck {
	
	private List<Card> mCards = new ArrayList<Card>();
	
	public Deck() {
		initCards();
	}
	
	private void initCards() {
		
		// TODO: get rank value from configuration (via Jaon file, RuleManager)
		for (Card.ESuit s: Card.ESuit.values()) {
			if (s == Card.ESuit.Jokers) {
				continue;
			}
			// rank from 3 to 15
			int r = 1;
			for (Card.EType t: Card.EType.values()) {
				if (t == Card.EType.BlackJoke || t == Card.EType.RedJoke) {
					continue;
				}
				mCards.add(new Card(t, s, r++));
			}
		}
		
		int r = 20;
		mCards.add(new Card(Card.EType.BlackJoke, Card.ESuit.Jokers, 20));
		mCards.add(new Card(Card.EType.RedJoke, Card.ESuit.Jokers, 21));
	}
	
	public void shuffle() {
		Collections.shuffle(mCards);
	}
}
