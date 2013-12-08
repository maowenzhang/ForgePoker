package com.forgepoker.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.forgepoker.model.Card.ESuit;
import com.forgepoker.util.FileUtils;

import android.util.Log;

/**
 * Used to manage game rules
 * @author 
 *
 */
public class RuleManager implements IPokerRule {

	private int mPlayerCount = 3;
	private boolean mbDynamicPlayers = false;
	private int mPlayersLowerBound = 3;
	private int mPlayersUpperBound = 3;
	
	private Deck mDeck = null;
	private int mBaseCards = 3;
	private int mDeckCount = 1;
	private boolean mbDynamicDecks = false;
	private int mDecksLowerBound = 1;
	private int mDecksUpperBound = 1;
	
	private boolean mCanPassRound = true; // We can pass a round in fighting lord.
	
	private final ArrayList<ICardPattern> mPatterns = new ArrayList<ICardPattern>();
	private static ICardPattern mbombPattern = null;
	private static ICardPattern mTriplePattern = null;
	private static ICardPattern mDoublePattern = null;
	private static ICardPattern mRocketpattern = null;
	
	// just for debugging.
	private boolean mShowRivalCards = false;
	
	// Singleton for RuleManager.
	private RuleManager()
	{
		// Read rule from JSON file and de-serialize it into pattern objects.
		buildRuleFromJson(readJsonFile());
	}
	
	private static RuleManager sRuleManager = null;
	public static RuleManager get()
	{
		if(null == sRuleManager)
			sRuleManager = new RuleManager();
		return sRuleManager;
	}
	
	@Override
	public int playerCount() {
		return mPlayerCount;
	}

	@Override
	public boolean dynamicPlayers() {
		return mbDynamicPlayers;
	}

	@Override
	public int playersLowerBound() {
		return mPlayersLowerBound;
	}

	@Override
	public int playersUpperBound() {
		return mPlayersUpperBound;
	}

	@Override
	public Deck deck() {
		return mDeck;
	}
	
	@Override
	public int baseCards() {
		return mBaseCards;
	}
	
	@Override
	public int deckCount() {
		return mDeckCount;
	}

	@Override
	public boolean dynamicDecks() {
		return mbDynamicDecks;
	}

	@Override
	public int decksLowerBound() {
		return mDecksLowerBound;
	}

	@Override
	public int decksUpperBound() {
		return mDecksUpperBound;
	}

	@Override
	public List<ICardPattern> patterns() {
		return mPatterns;
	}

	@Override
	public boolean canPassRound() {
		return mCanPassRound;
	}
	
	private String readJsonFile()
	{
		FileInputStream fis = null;
		try{
			FileUtils fileUtil = new FileUtils();
			File file = new File(fileUtil.getSDPATH()+"ForgePoker/rule.json");
			fis = new FileInputStream(file);
        }catch(IOException e){  
            e.printStackTrace();  
            Log.d("ReadRule","=========="+fis);    
        }  
          
          
		BufferedReader br = null;  
        try{  
             br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));  
        }catch (UnsupportedEncodingException e)  {  
  
             e.printStackTrace();  
             Log.d("ReadRule","=========="+br);    
        }  
          
        String str = new String();
        try{ 
        	String temp;
            while(br!=null&&null!=(temp=br.readLine()))  
                {  
                str+=temp;   
                if(null!=str)  
                Log.d("ReadRule","str is =========="+str);   
                }  
        }catch(IOException e){  
            e.printStackTrace();  
            Log.d("ReadRule","=========="+str);    
        }

        try
        {
        	br.close();
        }
        catch(IOException e){  
            e.printStackTrace();      
        }
        return str;
	}
	
	private void buildRuleFromJson(String strRule)
	{
		try{  
            JSONObject ruleObj = new JSONObject(strRule);
            if(ruleObj != null)
            {
            	mPlayerCount = ruleObj.optInt("playerCount");
            	mbDynamicPlayers = ruleObj.optBoolean("dynamicPlayers");
            	mPlayersLowerBound = ruleObj.optInt("playersLowerBound");
            	mPlayersUpperBound = ruleObj.optInt("playersUpperBound");
            	
            	JSONArray deck = ruleObj.optJSONArray("deck");
            	List<Card> cards = new ArrayList<Card>();
            	for(int i = 0; i < deck.length(); ++i) {
            		JSONObject card = deck.optJSONObject(i);
            		String cName = card.optString("name");
            		String cSuit = card.optString("suit");
            		int cRank = card.optInt("rank");
            		cards.add(new Card(Card.charToType(cName.charAt(0)), Card.stringToSuit(cSuit), cRank, i));
            	}
            	mDeck = new Deck(cards);
            	
            	mDeckCount = ruleObj.optInt("deckCount");
            	mbDynamicDecks = ruleObj.optBoolean("dynamicDecks");
            	mDecksLowerBound = ruleObj.optInt("decksLowerBound");
            	mDecksUpperBound = ruleObj.optInt("decksUpperBound");
            	
            	mCanPassRound = ruleObj.optBoolean("canPassRound");
            	mShowRivalCards = ruleObj.optBoolean("showRivalCards");
            	
            	JSONArray patterns = ruleObj.optJSONArray("patterns");
            	for(int i = 0; i < patterns.length(); ++i)
            	{
            		JSONObject pattern = patterns.getJSONObject(i);
            		JSONArray patternDefs = pattern.optJSONArray("patternDef");
            		List<String> strDefs= new ArrayList<String>();
            		for(int j = 0; j < patternDefs.length(); ++j)
            		{
            			strDefs.add(patternDefs.getString(j));
            		}
            		FightLordPattern _pattern = new FightLordPattern(pattern.optString("name"),
            				pattern.optString("caption"), strDefs);
            		_pattern.setHasLowerLimitation(pattern.optBoolean("hasLowerLimit"));
            		_pattern.setHasUpperLimitation(pattern.optBoolean("hasUpperLimit"));
            		_pattern.setMinCards(pattern.optInt("minCards"));
            		_pattern.setMaxCards(pattern.optInt("maxCards"));
            		_pattern.setWeight(pattern.optInt("weight"));
            		_pattern.setNeedMatchPattern(pattern.optBoolean("needMatchPattern"));
            		_pattern.setNeedSameSuit(pattern.optBoolean("needSameSuit"));
            		initSpecialPatterns(_pattern);
            		mPatterns.add(_pattern);
            	}
            }            
		}catch (JSONException e){  
       
           e.printStackTrace();  
           //Log.d("ReadRule", " datajson is =========="+dataJson);   
        }
	}
	
	private boolean initSpecialPatterns(ICardPattern pattern)
	{
		if(pattern.name().equals("Bomb"))
			mbombPattern = pattern;
		else if(pattern.name().equals("Triple") )
			mTriplePattern = pattern;
		else if(pattern.name().equals("Double") )
			mDoublePattern = pattern;		
		else if(pattern.name().equals("Rocket") )
			mRocketpattern = pattern;

		return true;
	}
	
	public ICardPattern getBombPattern()
	{
		return mbombPattern;
	}
	
	public ICardPattern getTriplePattern()
	{
		return mTriplePattern;
	}
	public ICardPattern getDoublePattern()
	{
		return mDoublePattern;
	}
	public ICardPattern getRocketPattern()
	{
		return mRocketpattern;
	}

	@Override
	public boolean showRivalCards() {
		return mShowRivalCards;
	}

	public boolean hasSameSuit(List<Card> cards)
	{
		ESuit suit = null;
		for(Card c : cards)
		{
			if(suit == null) {
				suit = c.suit();
				continue;
			}
			if(c.suit() != suit)
				return false;
		}
		return true;
	}
	
	public Suit.EType getSuitTypeByPatternName(String patternName) {
		if(patternName.equals("Single"))
			return Suit.EType.Single;
		else if(patternName.equals("Double"))
			return Suit.EType.Double;
		else if(patternName.equals("Rocket"))
			return Suit.EType.Rocket;
		else if(patternName.equals("Bomb"))
			return Suit.EType.Bomb;
		else if(patternName.equals("DoubleSequence"))
			return Suit.EType.DoubleSequence;
		else if(patternName.equals("SingleSequence"))
			return Suit.EType.SingleSequence;
		else if(patternName.equals("TripleOne"))
			return Suit.EType.TripleWithOne;
		else if(patternName.equals("TripleOneSequence"))
			return Suit.EType.TripleWithOneSequence;
		else if(patternName.equals("Triple"))
			return Suit.EType.Triple;
		else if(patternName.equals("TripleSequence"))
			return Suit.EType.TripleSequence;
		else if(patternName.equals("TripleTwo"))
			return Suit.EType.TripleWithTwo;
		else if(patternName.equals("TripleTwoSequence"))
			return Suit.EType.TripleWithTwoSequence;
		else if(patternName.equals("FourOne"))
			return Suit.EType.FourWithOne;
		else if(patternName.equals("FourTwo"))
			return Suit.EType.FourWithTwo;
		return Suit.EType.Invalid;
	}
	
	@Override
	public ICardPattern matched(Suit suit) {
		try {
			int szCards = suit.cards().size();
			boolean hasSameSuit = this.hasSameSuit(suit.cards());
			for(ICardPattern pattern : mPatterns)
			{
				// Quick check via the card count and suit.
				if(pattern.hasLowerLimitation() && szCards < pattern.minCards())
					continue;
				if(pattern.hasUpperLimitation() && szCards > pattern.maxCards())
					continue;
				if(pattern.needSameSuit() && !hasSameSuit)
					continue;
				// Now check the real pattern.
				if(pattern.definition().matched(suit.cards())) {
					String patternName = pattern.name();
					suit.setType(getSuitTypeByPatternName(patternName));
					suit.setPoints(pattern.calcPoints(suit));
					return pattern;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ICardPattern getPatternByName(final String patternName)
	{
		if(patternName.length() > 0)
		{
			for(ICardPattern pattern : mPatterns)
			{
				if(pattern.name().equals(patternName))
				{
					return pattern;
				}
				
			}
		}
		return null;
	}
	
}
