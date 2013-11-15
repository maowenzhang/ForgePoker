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
	private static int mDistanceAboveCard = 20;
	private static int mGapBetweenButtons = 15;
	private Bitmap mBitActionImage;
	
	public PlayActionRender(Context context) {
		mContext = context;
	}
	
	public void init() {
		mBitActionImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icons);
		
		// bit actions
		initPlayActions();
	}
	
	public void render(Canvas canvas) {
		
		// calculate render position
		//
		int totalLength = GameController.get().curActions().size() * (mButtonWidthImage + mGapBetweenButtons);
		int left = (GameController.get().mScreenWidth - totalLength ) / 2;
		int top = GameController.get().mScreenHeight - GameViewRender.mBottomSideMargin - CardRender.mCardHeight - mDistanceAboveCard;
		top -= mButtonHeightImage;
		
		int i = 0;
		for (EPlayAction a: GameController.get().curActions()) {
			SceneNode n = mActionNodes.get(a);
			
			int curleft = left + i++ * (mButtonWidthImage + mGapBetweenButtons); 
			Rect r = new Rect(curleft, top, curleft + mButtonWidthImage, top + mButtonHeightImage);
			n.desRect(r);
		}
		
		// render
		for (EPlayAction a: GameController.get().curActions()) {
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
		
//		addAction(EPlayAction.eBid1, 0, 0);
//		addAction(EPlayAction.eBid1, 0, 0);
//		addAction(EPlayAction.eBid1, 0, 0);
		
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
