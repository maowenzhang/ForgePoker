package com.forgepoker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
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
	public RuleManager rule() {
		return mRule;
	}
	
	/** Screen size related */
	int mScreenWidth = 0;
	int mScreenHeight = 0;
	int mTouchPosX = 0;
	int mTouchPosY = 0;
	
	/** Game states */
	private static final int STATE_GAME = 0;
	private int mGameState = STATE_GAME;	
	private boolean mBidCompleted = false;
	
	private List<Integer> mAvailableSeat = new LinkedList<Integer>();
	
	public enum EPlayAction {
		eBidNo,
		eBid1,
		eBid2,
		eBid3,
		
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
		case ePlayCard:
			mCurPlayer.playCards();
			mCurPlayer = this.nextPlayer();
			break;
		}
		
		// playing round
		if(!mBidCompleted)
		{
			Player nextPlayer = this.nextPlayer();
			if(a == EPlayAction.eBid3 || nextPlayer.hasBid())
			{
				if(a != EPlayAction.eBid3)
				{
					Player lordPlayer = null;
					for(Player p : mPlayers)
					{
						if(lordPlayer == null) {
							lordPlayer = p;
							continue;
						}
						if(lordPlayer.bid() < p.bid())
							lordPlayer = p;
					}
					mCurPlayer = lordPlayer;
				}
				mBidCompleted = true;
				mCurPlayer.isLord(true);
				// Prepare the actions and Wait the current player to play cards.
				mCurActions.clear();
				mCurActions.add(EPlayAction.ePlayCard);
				mCurActions.add(EPlayAction.ePassCard);
				mCurActions.add(EPlayAction.ePromptCard);
				mCurActions.add(EPlayAction.eReselectCard);
			}
			else
			{
				if(a == EPlayAction.eBid1 || a == EPlayAction.eBid2)
					mCurActions.remove(a);
				mCurPlayer = nextPlayer;
			}
		}
		//Log.d("BidResult", str);
	}

	/** Data */
	private List<Player> mPlayers = new ArrayList<Player>();
	public List<Player> players() {
		return mPlayers;
	}
	
	// The player which joins on current device.
	// It will be rendered to show the cards in hand.
	private Player mThisJoinedPlayer = null;
	
	public Player ThisJoinedPlayer() {
		return mThisJoinedPlayer;
	}
	
	// Current player is used to control the game round.
	// Once the lord is decided, lord player will be the 
	// first current player, then next player will become
	// the current, and so on.
	private Player mCurPlayer = null;
	
	public Player CurrentPlayer() {
		return mCurPlayer;
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
		for(int i = 0; i < mRule.playerCount(); ++i)
			mAvailableSeat.add(i);
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
		
		mThisJoinedPlayer = new Player("Me", R.drawable.ic_launcher, 0, false);
		mThisJoinedPlayer.seatIndex(getSeat());
		mThisJoinedPlayer.isRobot();
		mPlayers.add(mThisJoinedPlayer);
		for(int i = 1; i < mRule.playerCount(); ++i)
		{
			Player p = new Player("player" + i, R.drawable.ic_launcher, 0, true);
			p.seatIndex(getSeat());
			mPlayers.add(p);
		}
		
		// Generate a random layer as the initial lord.
		// The final lord will be decided by the bid result.
		Random r = new Random();
		int lordIdx = r.nextInt() % mRule.playerCount();
		mCurPlayer = mPlayers.get(lordIdx);
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
	
	/// Get a available seat from current table. For now we just have
	/// one table. 
	/// TODO: we need to change getting approach once we have a game
	/// hall which contains many tables.
	public int getSeat()
	{
		if(mAvailableSeat.size() > 0)
		{
			return mAvailableSeat.remove(0);
		}
		assert(false);
		return -1;
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
