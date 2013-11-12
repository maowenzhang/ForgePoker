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
	
	private GameViewRender mRender;	
	
	/** Screen size related */
	int mScreenWidth = 0;
	int mScreenHeight = 0;
	int mTouchPosX = 0;
	int mTouchPosY = 0;
	
	/** Game states */
	private static final int STATE_GAME = 0;
	private int mGameState = STATE_GAME;	
	
	/** Data */
	private List<Player> mPlayers = new ArrayList<Player>();
	public List<Player> players() {
		return mPlayers;
	}
	
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
	
	public void init(Context context, int screenWidth, int screenHeight) {
		mRender = new GameViewRender(this, context);
		mScreenWidth = screenWidth;
		mScreenHeight = screenHeight;
		
		initPlayers();
		// bid
		dealCards();
	}
	
	private void initPlayers() {
		
		Player p1 = new Player("张飞", R.drawable.ic_launcher, 0);
		Player p2 = new Player("关羽", R.drawable.ic_launcher, 100);
		Player p3 = new Player("刘备", R.drawable.ic_launcher, 10);
		
		p3.isCurrentPlayer(true);
		p3.isLord(true);
		
		mPlayers.add(p1);
		mPlayers.add(p2);
		mPlayers.add(p3);		
	}
	
	private void dealCards() {
		Deck d = new Deck();
		mDesks.add(d);
		
		d.shuffle();
		
		int size = mPlayers.size();
		int numOfCards = d.cards().size() / size;
		int i = 0;
		for (Player p: mPlayers) {
			p.cards().addAll(d.cards().subList(i++ * numOfCards, i * numOfCards));
		}
	}
	
	public void render(Canvas canvas) {
		mRender.render(canvas);
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		// get touch position
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		// start touch
		case MotionEvent.ACTION_DOWN:
			break;
		// touch and move
		case MotionEvent.ACTION_MOVE:
			mTouchPosX = x;
			mTouchPosY = y;
			break;
			
		// end touch
		case MotionEvent.ACTION_UP:
			break;
		}
		
		return true;
	}
}
