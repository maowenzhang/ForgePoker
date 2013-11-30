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
	
	public List<Card> cards() {
		return mCards;
	}
	
	public Deck() {
		initCards();
	}
	
	private void initCards() {
		
		// TODO: get rank value from configuration (via JSON file, RuleManager)		
		
		// Rank from 3 to 15
		int r = 3;
		int imageIndex = 0;
		for (Card.EType t: Card.EType.values()) {
			if (t == Card.EType.BlackJoke || t == Card.EType.RedJoke) {
				continue;
			}
			
			for (Card.ESuit s: Card.ESuit.values()) {
				if (s == Card.ESuit.Jokers) {
					continue;
				}
				mCards.add(new Card(t, s, r, imageIndex++));
			}
			r++;
		}					
		
		mCards.add(new Card(Card.EType.BlackJoke, Card.ESuit.Jokers, r++, imageIndex++));
		mCards.add(new Card(Card.EType.RedJoke, Card.ESuit.Jokers, r, imageIndex++));
	}
	
	public void shuffle() {
		Collections.shuffle(mCards);
	}
}
