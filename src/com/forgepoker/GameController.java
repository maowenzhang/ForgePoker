package com.forgepoker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.forgepoker.model.Deck;
import com.forgepoker.model.Player;

/**
 * Used to controller whole game activities
 * @author 
 *
 */
public class GameController {
	
	/** Game states */
	private static final int STATE_GAME = 0;
	private int mGameState = STATE_GAME;	
	
	public enum EPlayAction {
		eBid1,
		eBid2,
		eBid3,
		eBidNo,
		eBid1Disalbe,
		eBid2Disalbe,
		
		ePlayCard,
		ePassCard,
		ePromptCard,
		eReselectCard
	}
	private List<EPlayAction> mCurActions = new ArrayList<EPlayAction>();
	public List<EPlayAction> curActions() {
		return mCurActions;
	}
	public void onAction(EPlayAction a) {
		int bidScore = 1;
		mCurActions.clear();
//		mCurPlayer.bidScore()
	}
	
	/** Data */
	private List<Player> mPlayers = new ArrayList<Player>();
	public List<Player> players() {
		return mPlayers;
	}
	private Player mCurPlayer;
	
	private List<Deck> mDesks = new ArrayList<Deck>();
	public Deck deck() {
		return mDesks.get(0);
	}
	
	/** Single instance */
	private static GameController sGameController;
	static public GameController get() {
		if (sGameController == null) {
			sGameController = new GameController();
		}
		return sGameController;
	}
	
	private GameController() {
		
	}
	
	public void init(Context context) {
		initPlayers();
		// bid
		dealCards();
		// play action
		mCurActions.add(EPlayAction.eBid1);
		mCurActions.add(EPlayAction.eBid2);
		mCurActions.add(EPlayAction.eBid3);
		mCurActions.add(EPlayAction.eBidNo);
	}
	
	private void initPlayers() {
		
		// TODO: restore status when re-enter game
		mPlayers.clear();
		
		Player p1 = new Player("张飞", R.drawable.ic_launcher, 0);
		Player p2 = new Player("关羽", R.drawable.ic_launcher, 100);
		Player p3 = new Player("刘备", R.drawable.ic_launcher, 10);

		p1.seatIndex(1);
		p2.seatIndex(2);
		p3.seatIndex(3);
		
		p2.isCurrentPlayer(true);
		mCurPlayer = p2;
		p2.isLord(true);
		
		mPlayers.add(p1);
		mPlayers.add(p2);
		mPlayers.add(p3);		
	}
	
	private void dealCards() {
		// TODO: restore status when re-enter game
		mDesks.clear();
		
		Deck d = new Deck();
		mDesks.add(d);
		
		d.shuffle();
		
		int size = mPlayers.size();
		int numOfCards = d.cards().size() / size;
		int i = 0;
		for (Player p: mPlayers) {
			p.cards(d.cards().subList(i++ * numOfCards, i * numOfCards));
		}
	}
}
