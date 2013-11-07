package com.forgepoker;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import com.forgepoker.R;

/**
 * Main activity for poker game, includes elements like game table, startup menu, etc.
 * @author zhanglo
 *
 */
public class MainActivity extends Activity {

	public final static int MENU=0;
	public final static int GAME=1;
	public final static int RESULT=2;
	
	private GameView gameView;
	private StartupView startupView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//gameView = new GameView(this, this);
		startupView = new StartupView(this);
		
		setContentView(startupView);		
//		setContentView(R.layout.activity_main);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
