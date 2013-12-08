package com.forgepoker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.forgepoker.model.Suit.EType;

import junit.framework.Assert;

class GroupedSuits
{
	int count;
	int value;

	public List<Suit> a1		= new Vector<Suit>(); 	// single
	public List<Suit> a2		= new Vector<Suit>(); 	// double	
	public List<Suit> a3		= new Vector<Suit>(); 	// triple
	public List<Suit> a123		= new Vector<Suit>(); 	// sequence
	public List<Suit> a112233	= new Vector<Suit>();   // sequence double
	public List<Suit> a111222	= new Vector<Suit>();   // sequence triple
	public List<Suit> a4		= new Vector<Suit>();   // bomb
	public List<Suit> a0 		= new Vector<Suit>();   // rocket
	
	public void clearGroups()
	{
		a1.clear();
		a2.clear();
		a3.clear();
		a123.clear();
		a112233.clear();
		a111222.clear();
		a4.clear();
		a0.clear();
	}
}

public abstract class AIRobot {

	/** cards */
	private List<Card> mCards = new ArrayList<Card>();
	private Suit mCurPlayedSuit = new Suit();
	protected GroupedSuits mSuits = new GroupedSuits();

	
	
	public Suit curPlayedSuit() {
		return mCurPlayedSuit;
	}
	
	public void clearCurPlayedSuit() {
		mCurPlayedSuit.cards().clear();
	}

	public List<Card> cards() {
		return mCards;
	}

	public void cards(List<Card> cards) {
		synchronized (mCards) {
			mCards.clear();
			mCards.addAll(cards);
			sortCards();
		}
	}

	public void sortCards() {
		synchronized (mCards) {
			Collections.sort(mCards);
			Collections.reverse(mCards);
		}
	}

	/** Produce/play cards */
	public boolean playCards(Suit curPlayedSuit) {
		
		synchronized (mCurPlayedSuit) {
			mCurPlayedSuit = curPlayedSuit;
		}

		synchronized (mCards) {
			boolean re = mCards.removeAll(curPlayedSuit.cards());
			Assert.assertTrue("Fail to remove played cards!", re);
			if(re)
				reGroupCards();
		}
		return true;
	}
	
	public int reGroupCards()
	{
		mSuits.clearGroups();
		return groupSuits();
	}
	
	public void clearSelectedCards() {
		synchronized (mCards) 
		{
			for (Card c : mCards)
			{
				if (c.isSelected()) 
				{
					c.setIsSelected(false);
				}
			}
		}
	}
	
	public void reselectCards() {
		for(Card c : mCards) 
		{
			c.setIsSelected(false);
		}
	}
	
	public List<Card> selectedCards() 
	{
		List<Card> selCards = new ArrayList<Card>();
		for (Card c : mCards) 
		{
			if (c.isSelected())
				selCards.add(c);
		}
		return selCards;
	}
	
	public Suit selectedSuit() 
	{
		List<Card> selCards = this.selectedCards();
		if(selCards != null && selCards.size() > 0) 
		{
			return new Suit(selCards);
		}
		return null;
	}
	
	public void addBaseCard(List<Card> baseCards)
	{
		synchronized (mCards) 
		{
			assert(baseCards != null);
			if(null != baseCards) 
			{
				for(Card c : baseCards) 
				{
					mCards.add(c);
					c.setIsSelected(true);				
				}
				sortCards();
			}
		}
	}
	
	public void getSingle(List<Card> copiedCards){
		List<Card> del=new Vector<Card>();
		//1
		ICardPattern pattern = RuleManager.get().getPatternByName("Single");
		for(int i=0,len=copiedCards.size(); i<len; ++i)
		{
			List<Card> cards=new Vector<Card>();
			cards.add(copiedCards.get(i));
			Suit suit = new Suit(cards, EType.Single);
			suit.setPoints(pattern.calcRank(suit));
			mSuits.a1.add(suit);
			del.add(copiedCards.get(i));
		}
		copiedCards.removeAll(del);
	}
	
	public void getSequence(List<Card> copiedCards){
		if(copiedCards.size() < 5)
			return;
		
		ICardPattern pattern = RuleManager.get().getPatternByName("SingleSequence");
		List<Card> removedCards= new Vector<Card>();
		
		int len=copiedCards.size();
		for(int i=0; i<len-4; ++i)
		{
			int k=i;
			for(int j=(i +4); j<len; ++j){
				if( ( copiedCards.get(j).rank() - copiedCards.get(i).rank() ) == (j - i) )
					k = j; // find a single sequence
			}
			if(k-i>=4)
			{
				List<Card> addedCards = new Vector<Card>();
				for(int j=i; j<=k; ++j)
				{
					Card res = copiedCards.get(j);
					addedCards.add(res);
					removedCards.add(res);
				}
				Suit suit = new Suit(addedCards, EType.SingleSequence);
				suit.setPoints(pattern.calcRank(suit));
				mSuits.a123.add(suit);
				i=k;
			}
		}
		
		copiedCards.removeAll(removedCards);
	}

	public void getSequenceDouble(){
		int suitSize = mSuits.a2.size();
		if(suitSize < 3)
			return;
		
		List<Suit> removedSuits= new Vector<Suit>();
		
		ICardPattern pattern = RuleManager.get().getPatternByName("DoubleSequence");
		for(int i =0; i< (suitSize -2); ++i)
		{
			int k = i;
			for(int j = i + 2; j < suitSize; ++j)
			{
				Card cardFirstI = mSuits.a2.get(i).cards().get(0);
				Card cardFirstJ = mSuits.a2.get(j).cards().get(0);
				if( ( cardFirstJ.rank() - cardFirstI.rank() ) == (j - i) )
					k = j; // find a double sequence
					
			}
			
			if((k -i) >=2)
			{
				List<Card> addedCards = new Vector<Card>();
				for(int j=i; j<=k; ++j)
				{
					Suit suit = mSuits.a2.get(j);
					removedSuits.add(suit);
					addedCards.addAll(suit.cards());
				}
			
				Suit suit = new Suit(addedCards, EType.DoubleSequence);
				suit.setPoints(pattern.calcRank(suit));
				mSuits.a112233.add(suit);
				i=k;
			}
		}
		
		mSuits.a2.removeAll(removedSuits);
	}
	
	public void getSequenceTriple(){
		int suitSize = mSuits.a3.size();
		if(suitSize < 2)
			return;
		
		ICardPattern pattern = RuleManager.get().getPatternByName("TripleSequence");
		List<Suit> removedSuits= new Vector<Suit>();
		for(int i =0; i< (suitSize -1); ++i)
		{
			int k = i;
			for(int j = i + 1; j < suitSize; ++j)
			{
				Card cardFirstI = mSuits.a3.get(i).cards().get(0);
				Card cardFirstJ = mSuits.a3.get(j).cards().get(0);
				if( ( cardFirstJ.rank() - cardFirstI.rank() ) == (j - i) )
					k = j; // find a Triple sequence
					
			}
			
			if((k -i) >=1)
			{
				List<Card> addedCards = new Vector<Card>();
				for(int j=i; j<=k; ++j)
				{
					Suit suit = mSuits.a3.get(j);
					removedSuits.add(suit);
					addedCards.addAll(suit.cards());
				}
			
				Suit suit = new Suit(addedCards, EType.TripleSequence);
				suit.setPoints(pattern.calcRank(suit));
				mSuits.a111222.add(suit);
				i=k;
			}
		}
		
		mSuits.a3.removeAll(removedSuits);
	}

	public void getDouble(List<Card> copiedCards){
		
		ICardPattern pattern = RuleManager.get().getDoublePattern();
		List<String> matchs = new Vector<String>();
		RuleManager.get().getDoublePattern().definition().getMatchResult(copiedCards, matchs);
		for(String str:matchs)
		{
			List<Card> cards = getCards(str, copiedCards);
			if(!cards.isEmpty())
			{
				Suit suit = new Suit(cards, EType.Double);
				suit.setPoints(pattern.calcRank(suit));
				mSuits.a2.add(suit);
			}			
		}
	}
	
	public void getTriple(List<Card> copiedCards){
		/* the triple regular pattern does not work 
		 * List<String> matchs = new Vector<String>();
		RuleManager.get().getTriplePattern().definition().getMatchResult(copiedCards, matchs);
		for(String str:matchs)
		{
			List<Card> cards = getCards(str, copiedCards);
			if(!cards.isEmpty())
			{
				mSuits.a3.add(new Suit(cards, EType.Triple));
			}			
		}*/
		
		if(copiedCards.size() < 3)
			return;
		
		ICardPattern pattern = RuleManager.get().getTriplePattern();
		List<Card> removedCards= new Vector<Card>();
		
		int len=copiedCards.size();
		for(int i=0; i<len-2; ++i)
		{
			int k=i;
			for(int j=(i + 2); j<len; ++j){
				if( ( copiedCards.get(j).rank() - copiedCards.get(i).rank() ) == 0 ){
					k = j; // find a triple
					break;
				}		
			}
			if( (k-i) >= 2 )
			{
				List<Card> addedCards = new Vector<Card>();
				for(int j=i; j<=k; ++j)
				{
					Card res = copiedCards.get(j);
					addedCards.add(res);
					removedCards.add(res);
				}
				Suit suit = new Suit(addedCards, EType.Triple);
				suit.setPoints(pattern.calcRank(suit));
				mSuits.a3.add(suit);
				i=k;
			}
		}
		
		copiedCards.removeAll(removedCards);
	}
	
	public void getBomb(List<Card> copiedCards){
		ICardPattern bombPattern = RuleManager.get().getBombPattern();
		List<String> matchs = new Vector<String>();
		RuleManager.get().getBombPattern().definition().getMatchResult(copiedCards, matchs);
		for(String str:matchs)
		{
			List<Card> cards = getCards(str, copiedCards);
			if(!cards.isEmpty())
			{
				Suit suit = new Suit(cards, EType.Bomb);
				suit.setPoints(bombPattern.calcRank(suit));
				mSuits.a4.add(suit);
			}
		}
	}
	
	public void getRocket(List<Card> copiedCards){

		ICardPattern pattern = RuleManager.get().getRocketPattern();
		List<String> matchs = new Vector<String>();
		RuleManager.get().getRocketPattern().definition().getMatchResult(copiedCards, matchs);
		for(String str:matchs)
		{
			List<Card> cards = getCards(str, copiedCards);
			if(!cards.isEmpty())
			{
				Suit suit = new Suit(cards, EType.Rocket);
				suit.setPoints(pattern.calcRank(suit));
				mSuits.a0.add(suit);
			}			
		}
	}
	
	private static List<Card> getCards(String str, List<Card> cards)
	{
		List<Card> resCards = new Vector<Card>();
		char chs[] = str.toCharArray();
		for(int i=0; i < chs.length; ++i)
		{
			Card findResultCard = findCard(chs[i], cards);
			if(null != findResultCard)
			{
				resCards.add(findResultCard);
				cards.remove(findResultCard);
			}
		}
		return resCards;
	}
	
	private static Card findCard(char ch, List<Card> cards)
	{
		for(Card card:cards)
		{
			if(card.type().equals(Card.charToType(ch))) 
				return card;
		}
		return null;
	}

	public abstract Suit playCalledSuit(Suit oppo,int i);
	public abstract int groupSuits();
	public abstract Suit playSuit(int i);
	
}


