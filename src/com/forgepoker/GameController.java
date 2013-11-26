package com.forgepoker;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.forgepoker.model.Card;
import com.forgepoker.model.Deck;
import com.forgepoker.model.Player;
import com.forgepoker.model.Suit;

/**
 * Used to controller whole game activities
 * 
 * @author
 * 
 */
public class GameController {

	/** Game states */
	private static final int STATE_GAME = 0;
	private int mGameState = STATE_GAME;

	private GameActivity gameActivity;

	public GameActivity getGameActivity() {
		return gameActivity;
	}

	public void setGameActivity(GameActivity gameActivity) {
		this.gameActivity = gameActivity;
	}

	public enum EPlayAction {
		eNone, eBid, ePlayCard, ePassCard, ePromptCard, eReselectCard
	}

	public void startGame() {
		startBid();
	}

	private void startBid() {
		// TODO: get first player to bid
		// TODO: handle AI bid
		gameActivity.showBidButtons(true);
	}

	public void endBid(int bidVal) {
		// TODO: check which player has higher bid
		mCurPlayer.isLord(true);

		startPlayCards();
	}

	public void startPlayCards() {
		// TODO: get lord to play cards first
		gameActivity.showPlayButtons(true);
	}

	private void playCards_curPlayer() {
		// TODO: use AI to check played cards (suit)
		Suit suit = new Suit(mCurPlayer.selectedCards());
		mCurPlayer.playCards(suit);
	}

	private void playCards_otherPlayer(Player p) {
		// TODO: use AI to check played cards (suit)
		List<Card> cards = new ArrayList<Card>();
		cards.add(p.cards().get(0));
		Suit suit = new Suit(cards);

		p.playCards(suit);
	}

	private void playCards_otherPlayers() {
		// other players
		Player p = nextPlayer(mCurPlayer);
		playCards_otherPlayer(p);

		playCards_otherPlayer(nextPlayer(p));
	}

	public void onAction(EPlayAction a) {

		switch (a) {
		case ePlayCard:
			// get selected cards
			playCards_curPlayer();

			playCards_otherPlayers();

			// continue
			startPlayCards();

			break;
		case ePromptCard:
			// get selected cards
			break;
		case eReselectCard:
			// get selected cards
			mCurPlayer.clearSelectedCards();
			break;
		case ePassCard:
			// get selected cards
			mCurPlayer.clearSelectedCards();

			playCards_otherPlayers();

			// continue
			startPlayCards();
			break;
		default:
			break;
		}
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
		Log.d("forge1", "GameController::init");

		initPlayers();

		dealCards();
	}

	private Player nextPlayer(Player p) {
		int index = mPlayers.indexOf(p);
		index++;
		index %= mPlayers.size();
		return mPlayers.get(index);
	}

	private void initPlayers() {

		// TODO: restore status when re-enter game
		mPlayers.clear();

		Player p1 = new Player("张飞", R.drawable.zhangfei, 0);
		Player p2 = new Player("刘备", R.drawable.liubei, 100);
		Player p3 = new Player("诸葛亮", R.drawable.zhugeliang, 10);

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
		for (Player p : mPlayers) {
			p.cards(d.cards().subList(i++ * numOfCards, i * numOfCards));
		}
	}
}
