package com.forgepoker;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Game activity for poker game, includes elements like game table, startup menu, etc.
 * @author zhanglo
 *
 */
public class GameActivity extends Activity {

	private GameView mGameView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("forge", "GameActivity::onCreate");
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		Log.d("forge", ste[0].getMethodName());
		
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		mGameView = (GameView)findViewById(R.id.gameview);
		
		
		setContentView(R.layout.activity_game);
		
		
//		Display display = getWindowManager().getDefaultDisplay();
//		mGameView = new GameView(this);
		//LayoutParams params = new LayoutParams(display.getWidth(), display.getHeight());
		//mGameView.setLayoutParams(params);
//		setContentView(mGameView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	@Override
	public void onRestart() {
		Log.d("forge", "GameActivity::onRestart");
		super.onRestart();
	}
	
	@Override
	public void onStart() {
		Log.d("forge", "GameActivity::onStart");
		super.onStart();
	}
	
	@Override
	public void onResume() {
		Log.d("forge", "GameActivity::onResume");
		super.onResume();
	}
	
	@Override
	public void onPause() {
		Log.d("forge", "GameActivity::onPause");
		super.onPause();
	}
	
	@Override
	public void onStop() {
		Log.d("forge", "GameActivity::onStop");
		super.onStop();
	}	
}
