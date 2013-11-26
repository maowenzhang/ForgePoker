package com.forgepoker;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.forgepoker.model.Card;
import com.forgepoker.model.Player;

public class GameViewRender {
	
	/** General attributes */
	static int mScreenWidth = 0;
	static int mScreenHeight = 0;
	static Paint mPaint = new Paint();
	private Bitmap mGameBackground;
	
	private GameController mGameController;
	private Context mContext;
	private Canvas mCanvas;
	private CardRender mCardRender;
	private boolean mHasInit = false;
	
	/** Margins */
	static int mLeftOrRightMargin = 10;
	static int mBottomOrTopMargin = 10;
	
	/** Player Layout attributes
	 */
	static int mAvatarWidthHeight = 60;
	private Rect mCurPlayerDesRect;
	private Rect mLeftPlayerDesRect;
	private Rect mRightPlayerDesRect;
	private Map<Player, Bitmap> mPlayerImages = new HashMap<Player, Bitmap>();
	
	/** Card layout attributes 
	 */
	private Rect mLeftPlayerCardRect;
	private Rect mRightPlayerCardRect;
	
	public GameViewRender(Context context) {
		mContext = context;
		mGameController = GameController.get();
		mCardRender = new CardRender(context);
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		
		mGameBackground = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.game_background);
	}
	
	/** Init all related data before rendering
	 *  
	 */
	public void init() {
		if (mHasInit) {
			return;
		}
		Log.d("forge1", "GameViewRender::init");
		
		mCardRender.init();
		initCards();
		initPlayerAvatars();
		mHasInit = true;
	}
	
	/** Render graphics in canvas
	 * 
	 * @param canvas
	 */
	public void render(Canvas canvas) {
		mCanvas = canvas;
		mCardRender.canvas(canvas);
		
		canvas.drawColor(Color.BLACK);
		
		renderBackground();		
		
		renderCards();
		
		renderPlayerAvatars();
	}
	
	private void renderBackground() {
		Rect r = new Rect(0, 0, mScreenWidth, mScreenHeight);
		mCanvas.drawBitmap(mGameBackground, null, r, mPaint);
	}

	/** Touch events 
	 * 1. touch card
	 * 2. 
	 */
	public boolean OnTouch(int x, int y) {
		if (!mHasInit) {
			return true;
		}
		
		if (OnTouchCards(x, y))
			return true;
		
		return false;
	}
	
	/** Init data */
	private void initPlayerAvatars() {
		
		// #. player avatar positions
		//
		// for current player
		int left = mLeftOrRightMargin;
		int bottom = mScreenHeight - mBottomOrTopMargin - CardRender.mCardHeight;
		bottom -= CardRender.mCardSelectedPopupHeight - 5;
		mCurPlayerDesRect = new Rect(left, 
				bottom - mAvatarWidthHeight, 
				left + mAvatarWidthHeight, 
				bottom);
		
		// for left player
		left = mLeftOrRightMargin;
		int top = mBottomOrTopMargin;
		bottom = top + mAvatarWidthHeight;
		mLeftPlayerDesRect = new Rect(left, top, left + mAvatarWidthHeight, bottom);
		
		int right = mScreenWidth - mLeftOrRightMargin;
		mRightPlayerDesRect = new Rect(right - mAvatarWidthHeight, top, right, bottom);
		
		// #. player avatar images
		//
		for (Player p: mGameController.players()) {
			Bitmap img = BitmapFactory.decodeResource(mContext.getResources(), p.avatar());		
			mPlayerImages.put(p, img);
		}
	}
	
	/**
	 * Render all players 
	 * all info of player (picture, name, score, also pokes, etc.)
	 */
	private void renderPlayerAvatars() {
	
		for (Player p: mGameController.players()) {
			if (p.isCurrentPlayer()) {
				renderPlayerBasic(p, mCurPlayerDesRect);
			}
			// left player
			else if (p.seatIndex() == 1) {
				renderPlayerBasic(p, mLeftPlayerDesRect);
			}
			// right player
			else
				renderPlayerBasic(p, mRightPlayerDesRect);
		}
	}
	
	private void renderPlayerBasic(Player p, Rect des) {
		// TODO: draw name, score
		mCanvas.drawBitmap(mPlayerImages.get(p), null, des, mPaint);	
	}
	
	/** render cards
	 * 
	 */
	private void initCards() {
		
		int left = mLeftOrRightMargin;
		int top = mBottomOrTopMargin + mAvatarWidthHeight + 15;
		int bottom = top + CardRender.mCardHeight;
		mLeftPlayerCardRect = new Rect(left, top, left + CardRender.mCardWidth, bottom);
		
		int right = mScreenWidth - mLeftOrRightMargin;
		mRightPlayerCardRect = new Rect(right-CardRender.mCardWidth, top, right, bottom);
	}
	
	private void renderCards() {
		for (Player p: mGameController.players()) {
			if (p.isCurrentPlayer())
				renderCards_CurrentPlayer(p);
			else
				renderCards_OtherPlayer(p);
		}
	}
	
	private void renderCards_CurrentPlayer(Player p) {
		
		int left = mLeftOrRightMargin;
		int bottom = mBottomOrTopMargin;
		int right = mScreenWidth - mLeftOrRightMargin;
		int top = mScreenHeight - CardRender.mCardHeight - mBottomOrTopMargin;
		Rect des = new Rect(left, top, right, bottom);
		
		mCardRender.renderCards(p.cards(), des);
	}
	
	private void renderCards_OtherPlayer(Player p) {
	
		// left player
		if (p.seatIndex() == 1) {
			mCardRender.renderCardBack(mLeftPlayerCardRect);
		}
		// right player
		else
			mCardRender.renderCardBack(mRightPlayerCardRect);
	}
	
	private boolean OnTouchCards(int x, int y) {
		for (Player p: mGameController.players()) {
			if (!p.isCurrentPlayer())
				continue;
			
			for (int i=p.cards().size()-1; i>=0; i--) {
				Card c = p.cards().get(i);
				if (mCardRender.isTouched(c, x, y))
					return true;
			}
		}
		
		return false;
	}
}
