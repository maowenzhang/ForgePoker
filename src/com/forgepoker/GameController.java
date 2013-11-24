package com.forgepoker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.forgepoker.model.Deck;
import com.forgepoker.model.Player;
import com.forgepoker.model.RuleManager;

/**
 * Used to controller whole game activities
 * @author 
 *
 */
public class GameController {
	
	private GameViewRender mRender;	

	/** Define the rule of the game */
	private RuleManager mRule = RuleManager.get();
	
	/** Screen size related */
	int mScreenWidth = 0;
	int mScreenHeight = 0;
	int mTouchPosX = 0;
	int mTouchPosY = 0;
	
	/** Game states */
	private static final int STATE_GAME = 0;
	private int mGameState = STATE_GAME;	
	private boolean mBidCompleted = false;
	
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
		
		String str = new String();
		switch(a)
		{
		case eBid1:
			mCurPlayer.bid(1);
			str = "一分";
			break;
		case eBid2:
			mCurPlayer.bid(2);
			str = "二分";
			break;
		case eBid3:
			mCurPlayer.bid(3);
			str = "三分";
			break;
		case eBidNo:
			mCurPlayer.bid(0);
			str = "不叫";
			break;
		}
		
		// playing round
		if(!mBidCompleted)
		{
			Player nextPlayer = this.nextPlayer();
			if(mCurPlayer.bid() == EPlayAction.eBid3.ordinal() || nextPlayer != null && nextPlayer.hasBid())
			{
				mBidCompleted = true;
				// Wait the current player to play cards.
				
			}
			else
			{
				mCurPlayer = nextPlayer;
				if(mCurPlayer.isRobot())
				{
					// TODO: currently we don't allow a robot to be a lord.
					// We need to change it base on AI manager.
					mCurPlayer.bid(EPlayAction.eBidNo.ordinal());
				}
			}
		}
		Log.d("BidResult", str);
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
	
	public void init(Context context, int screenWidth, int screenHeight) {
		mScreenWidth = screenWidth;
		mScreenHeight = screenHeight;
		
		initPlayers();
		// bid
		dealCards();
		// play action
		mCurActions.add(EPlayAction.eBid1);
		mCurActions.add(EPlayAction.eBid2);
		mCurActions.add(EPlayAction.eBid3);
		mCurActions.add(EPlayAction.eBidNo);
		
		mRender = new GameViewRender(this, context);
	}
	
	private void initPlayers() {
		
		// TODO: restore status when re-enter game
		mPlayers.clear();
		
		Player playerMe = new Player("Me", R.drawable.ic_launcher, 0);
		playerMe.seatIndex(0);
		playerMe.isCurrentPlayer(true);
		mPlayers.add(playerMe);
		for(int i = 1; i < mRule.playerCount(); ++i)
		{
			Player p = new Player("player" + i, R.drawable.ic_launcher, 0);
			p.seatIndex(i);
			mPlayers.add(p);
		}
		
		// Generate a random layer as the initial lord.
		// The final lord will be decided by the bid result.
		Random r = new Random();
		int lordIdx = r.nextInt() % mRule.playerCount();
		mCurPlayer = mPlayers.get(lordIdx);
		mCurPlayer.isLord(true);
		mCurPlayer.isCurrentPlayer(true);
		
		/*
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
		mPlayers.add(p3);	*/	
	}
	
	private Player nextPlayer()
	{
		Collections.sort(mPlayers, new Comparator<Player> (){
			public int compare(Player p1, Player p2) {
				if(p1.seatIndex() < p2.seatIndex())
					return -1;
				else if(p1.seatIndex() == p2.seatIndex())
					return 0;
				else
					return 1;
			}
		});
		
		int next = (mCurPlayer.seatIndex() + 1) % mPlayers.size();
		for(Player p : mPlayers)
			if(p.seatIndex() == next)
				return p;
		return null;
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
			mRender.OnTouch(x, y);
			
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
