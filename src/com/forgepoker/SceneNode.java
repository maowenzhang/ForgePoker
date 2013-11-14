package com.forgepoker;

import android.graphics.Rect;

/**
 * Graphics node in canvas, used to keep graphics related attributes
 * @author zhanglo
 *
 */
public class SceneNode {
	
	private Rect mSrcRect;	
	private Rect mDesRect = null;
	private boolean mIsHit = false;
	private boolean mIsSelected = false;
	
	
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
		mIsHit = false;
		
		if (mDesRect == null)
			return false;
		
		if (mDesRect.contains(x, y)) {
			mIsHit = true;
			mIsSelected = !mIsSelected;
			return true;
		}
		return false;
	}
	
	public boolean isHit() {
		return mIsHit;
	}
	public boolean isSelected() {
		return mIsSelected;
	}
}
