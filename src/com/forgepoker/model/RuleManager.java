package com.forgepoker.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	
	private int mDeckCount = 1;
	private boolean mbDynamicDecks = false;
	private int mDecksLowerBound = 1;
	private int mDecksUpperBound = 1;
	
	private boolean mCanPassRound = true; // We can pass a round in fighting lord.
	
	private final ArrayList<ICardPattern> mPatterns = new ArrayList<ICardPattern>();
	
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
	public ArrayList<ICardPattern> patterns() {
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
            		FightLordPattern _pattern = new FightLordPattern(pattern.optString("name"),
            				pattern.optString("caption"), pattern.optString("patternDef"));
            		mPatterns.add(_pattern);
            	}
            }            
		}catch (JSONException e){  
       
           e.printStackTrace();  
           //Log.d("ReadRule", " datajson is =========="+dataJson);   
        }
	}

	@Override
	public boolean showRivalCards() {
		return mShowRivalCards;
	}

	@Override
	public boolean matched(List<Card> cards) {
		try {
			int szCards = cards.size();
			for(ICardPattern pattern : mPatterns)
			{
				if(pattern.hasLowerLimitation() && szCards < pattern.minCards())
					continue;
				if(pattern.hasUpperLimitation() && szCards > pattern.maxCards())
					continue;
				// TODO: need improve the performance
				if(pattern.definition().matched(cards))
					return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
