/**
 * 
 */
package com.forgepoker.model;

import java.util.List;
import java.util.Vector;

import com.forgepoker.model.Suit.EType;

/**
 * @author songx
 *
 */
public class AIRobotSimple extends AIRobot {

	/**
	 * 
	 */
	public AIRobotSimple() {
		// TODO Auto-generated constructor stub
	}

	private Suit playBomb(Suit res)
	{
			if(mSuits.a4.size() > 0)
				return mSuits.a4.get(0);
			else if(mSuits.a0.size() > 0)
				return mSuits.a0.get(0);
			return null;
	}
	
	/* (non-Javadoc)
	 * @see com.forgepoker.model.AIRobot#playCalledSuit(com.forgepoker.model.Suit, int)
	 */
	@Override
	public Suit playCalledSuit(Suit oppo, int repeatTimes) {
		int i = 0;
		Suit res = null;
		switch(oppo.type())
		{
		case Single:
			if(mSuits.a1.size() > mSuits.a3.size())
				i = mSuits.a3.size();
			for(; i < mSuits.a1.size(); ++i)
			{
				if(mSuits.a1.get(i).compareTo(oppo) > 0)
					res = mSuits.a1.get(i);
			}
			
			if(null == res)
			{
				res = playBomb(res);
			}
			break;
		case Double:
			for(i = 0; i < mSuits.a2.size(); ++i)
			{
				if(mSuits.a2.get(i).compareTo(oppo) > 0)
					res = mSuits.a2.get(i);
			}
			
			if(null == res)
			{
				res = playBomb(res);
			}
			break;
		case Triple:
			for(i = 0; i < mSuits.a3.size(); ++i)
			{
				if(mSuits.a3.get(i).compareTo(oppo) > 0)
					res = mSuits.a3.get(i);
			}
			
			if(null == res)
			{
				res = playBomb(res);
			}
			break;
		case TripleWithOne:
			for(i = 0; i < mSuits.a3.size(); ++i)
			{
				if(mSuits.a3.get(i).compareTo(oppo) > 0)
				{
					if(mSuits.a1.size() > 0)
					{
						List<Card> cards = new Vector<Card>(mSuits.a3.get(i).cards());
						cards.add(mSuits.a1.get(0).cards().get(0));
						return new Suit(cards, EType.TripleWithOne);
					}
				}
			}
			
			if(null == res)
			{
				res = playBomb(res);
			}
			break;
		case TripleWithTwo:
			for(i = 0; i < mSuits.a3.size(); ++i)
			{
				if(mSuits.a3.get(i).compareTo(oppo) > 0)
				{
					if(mSuits.a2.size() > 0)
					{
						List<Card> cards = new Vector<Card>(mSuits.a3.get(i).cards());
						cards.addAll(mSuits.a2.get(0).cards());
						return new Suit(cards, EType.TripleWithTwo);
					}
				}
			}
			
			if(null == res)
			{
				res = playBomb(res);
			}
			break;
		case SingleSequence:
			for(i = 0; i < mSuits.a123.size(); ++i)
			{
				if(mSuits.a123.get(i).compareTo(oppo) > 0)
					res = mSuits.a123.get(i);
			}
			
			if(null == res)
			{
				res = playBomb(res);
			}
			break;
		case DoubleSequence:
			for(i = 0; i < mSuits.a112233.size(); ++i)
			{
				if(mSuits.a112233.get(i).compareTo(oppo) > 0)
					res = mSuits.a112233.get(i);
			}
			
			if(null == res)
			{
				res = playBomb(res);
			}
			break;
		case TripleSequence:
			for(i = 0; i < mSuits.a111222.size(); ++i)
			{
				if(mSuits.a111222.get(i).compareTo(oppo) > 0)
					res = mSuits.a111222.get(i);
			}
			
			if(null == res)
			{
				res = playBomb(res);
			}
			break;
		case TripleWithOneSequence:
			for(i = 0; i < mSuits.a111222.size(); ++i)
			{
				if(mSuits.a111222.get(i).compareTo(oppo) > 0)
				{
					if(mSuits.a1.size() > oppo.sequenceCount())
					{
						List<Card> cards = new Vector<Card>(mSuits.a111222.get(i).cards());
						for(int j = 0; j < oppo.sequenceCount(); ++j)
						{
							cards.addAll(mSuits.a1.get(j).cards());
						}			
						
						return new Suit(cards, EType.TripleWithOneSequence);
					}
				}
			}
			
			if(null == res)
			{
				res = playBomb(res);
			}
		case TripleWithTwoSequence:
			for(i = 0; i < mSuits.a111222.size(); ++i)
			{
				if(mSuits.a111222.get(i).compareTo(oppo) > 0)
				{
					if(mSuits.a2.size() > oppo.sequenceCount())
					{
						List<Card> cards = new Vector<Card>(mSuits.a111222.get(i).cards());
						for(int j = 0; j < oppo.sequenceCount(); ++j)
						{
							cards.addAll(mSuits.a2.get(j).cards());
						}			
						
						return new Suit(cards, EType.TripleWithTwoSequence);
					}
				}
			}
			
			if(null == res)
			{
				res = playBomb(res);
			}
		case FourWithOne:
		case FourWithTwo:
			if(mSuits.a4.size() > 0)
				res = mSuits.a4.get(0);
			else if(mSuits.a0.size() > 0)
				res = mSuits.a0.get(0);
			break;
		case Bomb:
			for(i = 0; i < mSuits.a4.size(); ++i)
			{
				if(mSuits.a4.get(i).compareTo(oppo) > 0)
					res = mSuits.a4.get(i);
			}
			if(null == res)
			{
				if(mSuits.a0.size() > 0)
					res = mSuits.a0.get(0);
			}			
			break;
		case Rocket:
			break;
		default:
			break;
	
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see com.forgepoker.model.AIRobot#groupSuits()
	 */
	@Override
	public int groupSuits() {
		List<Card> allCards = cards();
		List<Card> copiedCards = new Vector<Card>(allCards);

		getBomb(copiedCards);
		getRocket(copiedCards);
		getTriple(copiedCards);
		getDouble(copiedCards);
		getSequence(copiedCards);
		getSingle(copiedCards);
		
		getSequenceTriple();
		getSequenceDouble();
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.forgepoker.model.AIRobot#playSuit(int)
	 */
	@Override
	public Suit playSuit(int i) {
		if(!mSuits.a1.isEmpty())
		{
			if(!mSuits.a3.isEmpty())
			{
				List<Card> cards = new Vector<Card>(mSuits.a3.get(0).cards());
				cards.add(mSuits.a1.get(0).cards().get(0));
				return new Suit(cards, EType.TripleWithOne);
			}				
			else
				return mSuits.a1.get(0);
		}
		else if(!mSuits.a2.isEmpty())
		{
			if(!mSuits.a3.isEmpty())
			{
				List<Card> cards = new Vector<Card>(mSuits.a3.get(0).cards());
				cards.addAll(mSuits.a2.get(0).cards());
				return new Suit(cards, EType.TripleWithTwo);
			}				
			else
				return mSuits.a2.get(0);
		}	
		else if(!mSuits.a3.isEmpty())
			return mSuits.a3.get(0);		
		else if(!mSuits.a123.isEmpty())
			return mSuits.a123.get(0);		
		else if(!mSuits.a112233.isEmpty())
			return mSuits.a112233.get(0);		
		else if(!mSuits.a111222.isEmpty())
			return mSuits.a111222.get(0);		
		else if(!mSuits.a4.isEmpty())
			return mSuits.a4.get(0);		
		else if(!mSuits.a0.isEmpty())
			return mSuits.a0.get(0);	
		return null;
	}

}
