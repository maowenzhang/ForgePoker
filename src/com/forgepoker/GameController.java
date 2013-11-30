package com.forgepoker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.content.Context;
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
	private GameActivity gameActivity;
	private List<Integer> mAvailableSeat = new LinkedList<Integer>();
	private Suit mLastSuit = null;
	
	public GameActivity getGameActivity() {
		return gameActivity;
	}

	public void setGameActivity(GameActivity gameActivity) {
		this.gameActivity = gameActivity;
	}

	
	
	public enum EPlayAction {
		eNone(-1), 
		eBidNo(0), eBid1(1), eBid2(2), eBid3(3),

		eBid1Disalbe(4), eBid2Disalbe(5),

		ePlayCard(6), ePassCard(7), ePromptCard(8), eReselectCard(9);
		
		private int value; 
		public int value() {
			return this.value;
		}
		private EPlayAction(int val) {
			this.value = val;
		}
	}

	public void startGame() {
		startBid();
	}

	private void startBid() {
		// TODO: get first player to bid
		// TODO: handle AI bid
		gameActivity.showBidButtons(true, true, true, true);
	}

	//
	// public void endBid(int bidVal) {
	// // TODO: check which player has higher bid
	// mCurPlayer.isLord(true);
	//
	// startPlayCards();
	// }
	//
	public void startPlayCards() {
		// TODO: get lord to play cards first
		gameActivity.showPlayButtons(true, false);
	}

	//
	// private void playCards_curPlayer() {
	// // TODO: use AI to check played cards (suit)
	// Suit suit = new Suit(mCurPlayer.selectedCards());
	// mCurPlayer.playCards(suit);
	// }
	//
	// private void playCards_otherPlayer(Player p) {
	// // TODO: use AI to check played cards (suit)
	// List<Card> cards = new ArrayList<Card>();
	// cards.add(p.cards().get(0));
	// Suit suit = new Suit(cards);
	//
	// p.playCards(suit);
	// }
	//
	// private void playCards_otherPlayers() {
	// // other players
	// Player p = nextPlayer(mCurPlayer);
	// playCards_otherPlayer(p);
	//
	// playCards_otherPlayer(nextPlayer(p));
	// }
	//
	// public void onAction(EPlayAction a) {
	//
	// switch (a) {
	// case ePlayCard:
	// // get selected cards
	// playCards_curPlayer();
	//
	// playCards_otherPlayers();
	//
	// // continue
	// startPlayCards();
	//
	// break;
	// case ePromptCard:
	// // get selected cards
	// break;
	// case eReselectCard:
	// // get selected cards
	// mCurPlayer.clearSelectedCards();
	// break;
	// case ePassCard:
	// // get selected cards
	// mCurPlayer.clearSelectedCards();
	//
	// playCards_otherPlayers();
	//
	// // continue
	// startPlayCards();
	// break;
	// default:
	// break;
	// }
	// }

	public void onAction(EPlayAction a) {
		if(mCurPlayer == null)
			return;
		String str = new String();
		switch (a) {
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
			{
				if(mLastCurPlayer == mCurPlayer)
					mLastSuit = null; // clear last suit since no player's cards are greater than current.
				
				Suit selSuit = mCurPlayer.selectedSuit();
				if(selSuit == null) {
					Toast.makeText(gameActivity, "TO " + mCurPlayer.name() + ": No cards selected!", Toast.LENGTH_SHORT).show();
					break;
				}
				// Check if the cards is valid or not.
				ICardPattern pattern1 = GameController.get().rule().matched(selSuit);
				if(pattern1 == null) {
					Toast.makeText(gameActivity, "Selected cards are invalid", Toast.LENGTH_SHORT).show();
					break;
				}
				
				if(mLastSuit != null && selSuit.points() <= mLastSuit.points()) {
					Toast.makeText(gameActivity, "Selected cards must be greater than last.", Toast.LENGTH_SHORT).show();
					break;
				}
				
				if (mCurPlayer.playCards(selSuit)) {
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
				mCurPlayer = this.nextPlayer();
			break;
		}

		// playing round
		if (!mBidCompleted) 
		{
			boolean showBid1 = true, 
					showBid2 = true, 
					showBid3 = true,
					hasNoBid = false;
			for(Player p : this.mPlayers)
			{
				if(p.bid() == 1)
					showBid1 = false;
				else if(p.bid() == 2)
					showBid2 = false;
				else if(p.bid() == 3)
					showBid3 = false;
				else if(p.bid() == -1)
					hasNoBid = true;
			}

			Player nextPlayer = this.nextPlayer();
			mBidCompleted = (a == EPlayAction.eBid3 || nextPlayer.hasBid());
			gameActivity.showBidButtons(!mBidCompleted, showBid1, showBid2, showBid3);
				
			if (mBidCompleted) {
				if (a != EPlayAction.eBid3) {
					Player lordPlayer = null;
					for (Player p : mPlayers) {
						if (lordPlayer == null) {
							lordPlayer = p;
							continue;
						}
						if (lordPlayer.bid() < p.bid())
							lordPlayer = p;
					}
					mCurPlayer = lordPlayer;
				}
				mCurPlayer.isLord(true);
				// Star play cards, wait the current player to play cards.
				startPlayCards();

			} else {
				mCurPlayer = nextPlayer;
			}
		}
		else
		{
			boolean showPass = true;
			if(mLastCurPlayer == null)
				showPass = false;
			else if(mLastCurPlayer == mCurPlayer)
				showPass = false;
			gameActivity.showPlayButtons(true, showPass);
		}
		// Log.d("BidResult", str);
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
	
	private Player mLastCurPlayer = null;

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
		for (int i = 0; i < mRule.playerCount(); ++i)
			mAvailableSeat.add(i);
	}

	public void init(Context context) {
		Log.d("forge1", "GameController::init");

		initPlayers();

		dealCards();
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
		int lordIdx = r.nextInt() % mRule.playerCount();
		mCurPlayer = mPlayers.get(lordIdx);
	}

	private Player nextPlayer() {

		int next = (mCurPlayer.seatIndex() + 1) % mPlayers.size();
		for (Player p : mPlayers)
			if (p.seatIndex() == next)
				return p;
		return null;
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
		// TODO: restore status when re-enter game
		mDesks.clear();

		Deck d = new Deck();
		mDesks.add(d);

		d.shuffle();

		int size = mPlayers.size();
		int numOfCards = d.cards().size() / size;
		int i = 0;
		for (Player p : mPlayers) {
			p.cards(d.cards().subList(i++ * numOfCards, i * numOfCards));
		}
	}
}
