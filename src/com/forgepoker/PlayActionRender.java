package com.forgepoker;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.forgepoker.GameController.EPlayAction;
import com.forgepoker.model.Player;

/**
 * Play cards, bid actions render
 * @author zhanglo
 *
 */
public class PlayActionRender {
	
	Context mContext;
	
	/**
	 * Layout attributes
	 */
	// Bid action (score 1,2,3 or not)
	private static int mButtonWidthImage = 44;
	private static int mButtonHeightImage = 24;
	private static int mDistanceAboveCard = 25;
	private static int mGapBetweenButtons = 15;
	private Bitmap mBitActionImage;
	private GameController mGameController;
	
	public PlayActionRender(Context context) {
		mContext = context;
	}
	
	public void init() {
		mBitActionImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icons);
		
		// bit actions
		initPlayActions();
		
		// cache the game controller 
		mGameController = GameController.get();
		assert(mGameController != null);
	}
	
	public void render(Canvas canvas) {
		
		Player curPlayer = mGameController.CurrentPlayer();
		// Now we suppose the left player's seat index is 2, the right player
		// seat index is 1 and the ThisJoinedPlayer's index is always 0.
		// TODO: we need to revisit it if we'd like to support playing across
		// a connection (by WIFI, blue-tooth, etc).
		int seatIdx = curPlayer.seatIndex();
	
		// calculate render position
		//
		int totalLength = 0, left = 0, top = 0;
		if(seatIdx == 0)
		{
			totalLength = mGameController.curActions().size() * (mButtonWidthImage + mGapBetweenButtons);
			left = (mGameController.mScreenWidth - totalLength ) / 2;
			top = mGameController.mScreenHeight - GameViewRender.mBottomSideMargin 
												- CardRender.mCardHeight
												- mDistanceAboveCard
												- mButtonHeightImage;
		}
		else
		{
			totalLength = mButtonWidthImage;
			top = (mGameController.mScreenHeight - (mButtonHeightImage + mGapBetweenButtons) * mGameController.curActions().size())/2;
			// left player
			left = GameViewRender.mLeftOrRightSideMargin + CardRender.mCardWidth + CardRender.mCardSelectedPopupHeight;
			if(seatIdx == 1)
			{
				// right player
				left = mGameController.mScreenWidth - left - mButtonWidthImage;
			}
		}
		
		int i = 0;
		for (EPlayAction a: mGameController.curActions()) {
			SceneNode n = mActionNodes.get(a);
			if(seatIdx == 0)
			{
				int curleft = left + i++ * (mButtonWidthImage + mGapBetweenButtons); 
				Rect r = new Rect(curleft, top, curleft + mButtonWidthImage, top + mButtonHeightImage);
				n.desRect(r);
			}
			else
			{
				int curTop = top + (i++)*(mButtonHeightImage + mGapBetweenButtons);
				Rect r = new Rect(left, curTop, left + mButtonWidthImage, curTop + mButtonHeightImage);
				n.desRect(r);
			}
		}
		
		// render
		for (EPlayAction a: mGameController.curActions()) {
			SceneNode n = mActionNodes.get(a);
			
			canvas.drawBitmap(mBitActionImage, n.srcRect(), n.desRect(), null);
		}	
	}
	
	
	private Map<EPlayAction, SceneNode> mActionNodes = new HashMap<EPlayAction, SceneNode>();
	private void initPlayActions() {
		// refer to file "doc/sprite-icons.png" which contains detailed positions of sprite icons
		addAction(EPlayAction.eBid1, 0, 0);
		addAction(EPlayAction.eBid2, 0, 36);
		addAction(EPlayAction.eBid3, 0, 72);
		addAction(EPlayAction.eBidNo, 0, 108);
		
		addAction(EPlayAction.eBid1Disalbe, 0, 144);
		addAction(EPlayAction.eBid2Disalbe, 0, 180);
		
		addAction(EPlayAction.ePlayCard, 0, 282);
		addAction(EPlayAction.ePassCard, 0, 249);
		addAction(EPlayAction.ePromptCard, 0, 216);
		addAction(EPlayAction.eReselectCard, 0, 314);
	}
	
	private void addAction(EPlayAction type, int srcLeft, int srcTop) {
		Rect srcRect = new Rect(srcLeft, srcTop, srcLeft + mButtonWidthImage, srcTop + mButtonHeightImage);
		mActionNodes.put(type, new SceneNode(srcRect));
	}
	
	public boolean OnTouch(int x, int y) {
		for (EPlayAction a: GameController.get().curActions()) {
			SceneNode cnode = mActionNodes.get(a);
			if (cnode.isHit(x, y)) {
				
				GameController.get().onAction(a);
				
				return true;
			}
				
		}
		
		return false;
	}
}
