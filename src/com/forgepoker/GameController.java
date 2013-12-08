package com.forgepoker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.forgepoker.model.Card;
import com.forgepoker.model.Deck;
import com.forgepoker.model.ICardPattern;
import com.forgepoker.model.Player;
import com.forgepoker.model.RuleManager;
import com.forgepoker.model.Suit;

/**
 * Used to controller whole game activities
 * 
 * @author
 * 
 */
public class GameController {

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
	
	public enum EPlayAction {
		eNone(-1), 
		eBidNo(0), eBid1(1), eBid2(2), eBid3(3),

		ePlayCard(4), ePassCard(5), ePromptCard(6), eReselectCard(7);
		
		private int value; 
		public int value() {
			return this.value;
		}
		private EPlayAction(int val) {
			this.value = val;
		}
	}
	
	/** Screen size related */
	int mScreenWidth = 0;
	int mScreenHeight = 0;
	int mTouchPosX = 0;
	int mTouchPosY = 0;

	/** Game states */
	//private static final int STATE_GAME = 0;
	//private int mGameState = STATE_GAME;
	
	// bid related
	private boolean mBidCompleted = false;
	private int mBidNumber = 0;
	
	// play cards related
	private List<Card> mBaseCards = new ArrayList<Card>();
	private boolean mFinished = false;
	private boolean mNewRoundBegin = false;
	private Suit mLastSuit = null;
	// Current player is used to control the game round.
	// Once the lord is decided, lord player will be the
	// first current player, then next player will become
	// the current, and so on.
	private Player mCurPlayer = null;
	private Player mLastCurPlayer = null;

	// general
	private GameActivity gameActivity;
	private List<Integer> mAvailableSeat = new LinkedList<Integer>();
	private List<Player> mPlayers = new ArrayList<Player>();
	private RuleManager mRule = RuleManager.get();	/** Define the rule of the game */
	private List<Deck> mDesks = new ArrayList<Deck>();
	// The player which joins on current device, It will be rendered to show the cards in hand.
	private Player mThisJoinedPlayer = null;

	public Player ThisJoinedPlayer() {
		return mThisJoinedPlayer;
	}
		
	public Deck deck() {
		return mDesks.get(0);
	}
	
	public RuleManager rule() {
		return mRule;
	}
	
	public List<Player> players() {
		return mPlayers;
	}
	
	public List<Card> baseCards() {
		return mBaseCards;
	}
	
	public void setGameActivity(GameActivity gameActivity) {
		this.gameActivity = gameActivity;
	}
	
	public boolean gameFinished()
	{
		return mFinished;
	}
	
	public Player CurrentPlayer() {
		return mCurPlayer;
	}
	
	public Player LastCurrentPlayer() {
		return mLastCurPlayer;
	}
	
	public boolean bidCompleted() {
		return mBidCompleted;
	}
	
	public void startGame() {
		PlayCardRunnable gameRunnable = new PlayCardRunnable();
		new Thread(gameRunnable, "PlayCard").start();
	}
	
	public void onAction(EPlayAction a) {
		if(mCurPlayer == null)
			return;
		
		switch (a) {
		case eBid1:
			mCurPlayer.bid(1);
			mBidNumber = 1;
			gameActivity.showBidButtons(false, false, false, false);
			Toast.makeText(gameActivity, "TO " + mCurPlayer.name() + ": bid 1!", Toast.LENGTH_SHORT).show();
			mCurPlayer = this.nextPlayer();
			break;
		case eBid2:
			mCurPlayer.bid(2);
			mBidNumber = 2; 
			gameActivity.showBidButtons(false, false, false, false);
			Toast.makeText(gameActivity, "TO " + mCurPlayer.name() + ": bid 2!", Toast.LENGTH_SHORT).show();
			mCurPlayer = this.nextPlayer();
			break;
		case eBid3:
			mCurPlayer.bid(3);
			mBidNumber = 3;
			gameActivity.showBidButtons(false, false, false, false);
			setLord(mCurPlayer);
			Toast.makeText(gameActivity, "TO " + mCurPlayer.name() + ": bid 3!", Toast.LENGTH_SHORT).show();
			break;
		case eBidNo:
			mCurPlayer.bid(0);
			gameActivity.showBidButtons(false, false, false, false);
			Toast.makeText(gameActivity, "TO " + mCurPlayer.name() + ": bid 0!", Toast.LENGTH_SHORT).show();
			mCurPlayer = this.nextPlayer();
			break;
		case ePlayCard:
			{
				if(mLastCurPlayer == mCurPlayer)
				{
					mNewRoundBegin = true;
					mLastSuit = null; // clear last suit since no player's cards are greater than current.
				}
				
				List<Card> selCards = mCurPlayer.selectedCards();
				if(selCards.isEmpty()) {
					Toast.makeText(gameActivity, "TO " + mCurPlayer.name() + ": No cards selected!", Toast.LENGTH_SHORT).show();
					break;
				}
				
				Suit selSuit = mCurPlayer.selectedSuit();
				// Check if the cards is valid or not.
				ICardPattern pattern1 = GameController.get().rule().matched(selSuit);
				if(pattern1 == null) {
					Toast.makeText(gameActivity, "Selected cards are invalid", Toast.LENGTH_SHORT).show();
					break;
				}
				
				if(mLastSuit != null) {
					// check if the pattern is matched
					if(pattern1.needMatchPattern() )
					{
						if( !pattern1.name().equals(mLastSuit.type().getName()) )
						{
							Toast.makeText(gameActivity, "Selected cards must be the same suit as last.", Toast.LENGTH_SHORT).show();
							break;
						}
					}

					// check if the point is bigger
					if(selSuit.points() <= mLastSuit.points())
					{
						Toast.makeText(gameActivity, "Selected cards must be greater than last.", Toast.LENGTH_SHORT).show();
						break;
					}
				}
				
				gameActivity.showPlayButtons(false, false);
				
				mNewRoundBegin = false;
				if (mCurPlayer.playCards(selSuit)) {
					if(tryFinishGame()) {
						//gameActivity.finish();
						mFinished = true;
						return;
					}
					mLastCurPlayer = mCurPlayer;
					mCurPlayer = this.nextPlayer();
					mLastSuit = selSuit;
				} else {
					Toast.makeText(gameActivity, "Fail to remove played cards!", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case ePassCard:
			if(mLastCurPlayer != mCurPlayer)
			{
				mCurPlayer.clearSelectedCards();
				mCurPlayer = this.nextPlayer();
				if(mLastCurPlayer == mCurPlayer)
				{
					restartNewRound();
				}	
				gameActivity.showPlayButtons(false, false);
			}
			
			break;
		case eReselectCard:
			if(null != mCurPlayer) {
				mCurPlayer.reselectCards();
			}
			break;
		case ePromptCard:
		case eNone:
		default:
			break;
			
		}
	}

	public boolean tryFinishGame() {
		if(mCurPlayer.cards().size() == 0) {
			
			if( Thread.currentThread().getName().equals("PlayCard") ) 
			{
				
			}
			else
			{
				if(mCurPlayer.isLord())
					Toast.makeText(gameActivity, "Lord wins! Come on, peasant!", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(gameActivity, "Peasant wins!", Toast.LENGTH_SHORT).show();
			}

			// Reset game states
			mCurPlayer = null;
			mLastCurPlayer = null;
			mLastSuit = null;
			mBidCompleted = false;
			mFinished = true;
			mBidNumber = 0;
			return true;
		}
		return false;
	}
	
	
	public void init(Context context) {
		Log.d("forge1", "GameController::init");

		initTableSeat();
		
		initPlayers();

		dealCards();
	}

	private void initTableSeat() {
		mAvailableSeat.clear();
		for (int i = 0; i < mRule.playerCount(); ++i)
			mAvailableSeat.add(i);
	}
	
	private void initPlayers() {

		// TODO: restore status when re-enter game
		mPlayers.clear();

		// Player p1 = new Player("张飞", R.drawable.zhangfei, 0);
		// Player p2 = new Player("刘备", R.drawable.liubei, 100);
		// Player p3 = new Player("诸葛亮", R.drawable.zhugeliang, 10);

		mThisJoinedPlayer = new Player("Me", R.drawable.ic_launcher, 0, false);
		mThisJoinedPlayer.seatIndex(getSeat());
		mPlayers.add(mThisJoinedPlayer);
		for (int i = 1; i < mRule.playerCount(); ++i) {
			Player p = new Player("player" + i, R.drawable.ic_launcher, 0, true);
			p.seatIndex(getSeat());
			mPlayers.add(p);
		}
		
		// Sort the players by seat index. It's useful when getting next player in a round.
		Collections.sort(mPlayers, new Comparator<Player>() {
			public int compare(Player p1, Player p2) {
				if (p1.seatIndex() < p2.seatIndex())
					return -1;
				else if (p1.seatIndex() == p2.seatIndex())
					return 0;
				else
					return 1;
			}
		});

		// Generate a random layer as the initial lord.
		// The final lord will be decided by the bid result.
		Random r = new Random();
		int lordIdx = r.nextInt(mRule.playerCount());
		mCurPlayer = mPlayers.get(lordIdx);
	}

	private Player nextPlayer() {

		if(null == mCurPlayer)
			return null;
		
		int next = (mCurPlayer.seatIndex() + 1) % mPlayers.size();
		for (Player p : mPlayers)
			if (p.seatIndex() == next)
				return p;
		return null;
	}

	public int getPlayerCount()
	{
		return mPlayers.size();
	}
	
	// / Get a available seat from current table. For now we just have
	// / one table.
	// / TODO: we need to change getting approach once we have a game
	// / hall which contains many tables.
	public int getSeat() {
		if (mAvailableSeat.size() > 0) {
			return mAvailableSeat.remove(0);
		}
		assert (false);
		return -1;
	}

	private void dealCards() {
		
		if(mRule == null)
			return;
		
		// TODO: restore status when re-enter game
		mDesks.clear();
		mBaseCards = null;
		
		List<Card> allCards = new ArrayList<Card>();
		for(int i = 0; i < mRule.deckCount(); ++i)
		{
			Deck d = mRule.deck();
			d.resetCards();
			mDesks.add(d);
			allCards.addAll(d.cards());
		}
		Collections.shuffle(allCards);
		
		int size = mPlayers.size();
		int numOfCards = (allCards.size() - mRule.baseCards()) / size;
		int i = 0;
		for (Player p : mPlayers) {
			p.cards(allCards.subList(i++ * numOfCards, i * numOfCards));
		}
		for (Player p : mPlayers) {
			
			allCards.removeAll(p.cards());
		}
		
		
		mBaseCards = allCards;
	}
	
	public boolean isNewRoundBegin()
	{
		return mNewRoundBegin;
	}
	
	private void restartNewRound()
	{
		mNewRoundBegin = true;
		mLastCurPlayer = null;
		mLastSuit = null;
	}
	
	public void playCards()
	{
		if( mCurPlayer!= null && mCurPlayer.isRobot())
		{
			Suit showedSuit = mCurPlayer.showCards(mLastSuit);
			
			mNewRoundBegin = false;
			if(showedSuit != null)
			{
				if(tryFinishGame()) {
					mFinished = true;
					return;
				}
				mLastCurPlayer = mCurPlayer;
				mCurPlayer = this.nextPlayer();
				mLastSuit = showedSuit;
			}
			else
			{
				// don't update the last player
				//mLastCurPlayer = mCurPlayer;
				//mLastSuit = showedSuit;
				Message message = Message.obtain();
				message.what = GameActivity.SHOW_MSG;
				message.obj = mCurPlayer.seatIndex() + " player does not play any cards " ;
				gameActivity.getHandler().sendMessage(message);
				
				mCurPlayer = this.nextPlayer();	
				if(mLastCurPlayer == mCurPlayer)
					restartNewRound();
			}
		}	
		else
		{
			Message message = Message.obtain();
			message.what = GameActivity.SHOW_PLAY;
			message.arg1 = 1 ;
			message.arg2 = (mLastSuit == null) ? 0 : 1;
			gameActivity.getHandler().sendMessage(message);
			mNewRoundBegin = false;
			//gameActivity.showPlayButtons(true, (mLastSuit == null) );
		}
	}
	
	public void bidForGame()
	{
		if( mCurPlayer!= null && mCurPlayer.isRobot())
		{
			int bidNum = mCurPlayer.getBidNum(mBidNumber);
			if(bidNum > mBidNumber)
			{
				Message message = Message.obtain();
				message.what = GameActivity.SHOW_MSG;
				message.obj = mCurPlayer.seatIndex() + " player bids " + bidNum;
				gameActivity.getHandler().sendMessage(message);
				
				mBidNumber = bidNum;
				if(mBidNumber >= 3) {
					setLord(mCurPlayer);
					return;
				}
			}
			mCurPlayer = this.nextPlayer();
		}	
		else
		{
			Message message = Message.obtain();
			message.what = GameActivity.SHOW_BID;
			message.arg1 = 1;
			message.arg2 = mBidNumber;
			gameActivity.getHandler().sendMessage(message);
			//gameActivity.showBidButtons(!mBidCompleted, mBidNumber < 1, mBidNumber < 2, mBidNumber < 3);
		}
	}
	
	public void endGame()
	{
		Message message = Message.obtain();
		message.what = GameActivity.END_GAME;
		message.arg1 = 1;
		gameActivity.getHandler().sendMessage(message);
	}
	
	public void selectTopBidAsLord()
	{
		mBidNumber = 0;
		int maxBidPlayer = 0;
		for(int i = 0; i < getPlayerCount(); ++i)
		{
			int numBid = mPlayers.get(i).bid();
			if( numBid > mBidNumber );
			{
				mBidNumber = numBid;
				maxBidPlayer = i;
			}
		}
		setLord( mPlayers.get(maxBidPlayer) );
	}
	
	private void setLord(Player lord)
	{
		if(null != lord)
		{
			mCurPlayer = lord;
			mBidCompleted = true;
			mNewRoundBegin = true;
			mFinished = false;
			mLastCurPlayer = null;
			mLastSuit = null;
			
			try
			{
				mCurPlayer.setLord(mBaseCards);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
}
