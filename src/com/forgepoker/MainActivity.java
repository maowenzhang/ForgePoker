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
	
	private GameView mGameView;
	private StartupView mStartView;
	private MainActivity mMain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		mMain = this;
		mGameView = new GameView(this, this);
		mStartView = new StartupView(this, this);
		
		setContentView(mStartView);		
//		setContentView(R.layout.activity_main);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
				case 0:
					setContentView(mStartView);
					break;
				case 1:
					setContentView(mGameView);
					break;
				case 2:
					break;
			}
		}

	};

}
