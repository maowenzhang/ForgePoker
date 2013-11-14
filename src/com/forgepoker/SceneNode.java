package com.forgepoker;

import android.graphics.Rect;

/**
 * Graphics node in canvas, used to keep graphics related attributes
 * @author zhanglo
 *
 */
public class SceneNode {
	
	private Rect mSrcRect;	
	private Rect mDesRect;
	
	
	public void srcRect(Rect val) {
		mSrcRect = val;
	}
	public Rect srcRect() {
		return mSrcRect;
	}	
	public void desRect(Rect val) {
		mDesRect = val;
	}
	public Rect desRect() {
		return mDesRect;
	}
	
	public boolean isHit(int x, int y) {
		return mDesRect.contains(x, y);
	}
}
