package com.forgepoker;

import java.lang.ref.WeakReference;

import com.forgepoker.GameController.EPlayAction;
import com.forgepoker.model.RuleManager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Game activity for poker game, includes elements like game table, startup
 * menu, etc.
 * 
 * @author zhanglo
 * 
 */
public class GameActivity extends Activity implements OnClickListener {

	private GameView mGameView;
	
	private LinearLayout mLayoutBid;
	private Button mBtnBid1;
	private Button mBtnBid2;
	private Button mBtnBid3;
	private Button mBtnBidno;

	private LinearLayout mLayoutPlay;
	private Button mBtnPlay;
	private Button mBtnPrompt;
	private Button mBtnReselect;
	private Button mBtnPass;
	
	public final static int SHOW_MSG = 0;	
	public final static int SHOW_BID = 1;	
	public final static int SHOW_PLAY = 2;
	public final static int END_GAME = 3;

	public GameView gameView() {
		return mGameView;
	}
	
	static class MyHandler extends Handler {
        WeakReference<GameActivity> mActivity;

        MyHandler(GameActivity activity) {
                mActivity = new WeakReference<GameActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) 
        {
        	GameActivity theActivity = mActivity.get();
        	switch (msg.what) 
            {
            case SHOW_MSG:
   				 //Log.d("SHOW_MSG", msg.obj.toString());
   				 Toast.makeText(theActivity, msg.obj.toString(), Toast.LENGTH_SHORT).show();
   				 break;
            case SHOW_BID:
   				 boolean bShow = (msg.arg1==1);
   				 theActivity.showBidButtons(bShow, msg.arg2 <1, msg.arg2 <2, msg.arg2 <3);
   				 break;
            case SHOW_PLAY:
   				theActivity.showPlayButtons(true, (msg.arg2==1));
   				 break;
            case END_GAME:
            	theActivity.finish();
            default:  
   		         super.handleMessage(msg);  
   		         break; 
            }
        }
	};

	private Handler mHandler = new MyHandler(this);

	
	public Handler getHandler(){

		return this.mHandler;

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("forge", "GameActivity::onCreate");

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_game);

		initSettings();
		initControls();

		mLayoutBid.setVisibility(View.GONE);
		mLayoutPlay.setVisibility(View.GONE);
		GameController.get().setGameActivity(this);
		GameController.get().startGame();
	}

	private void initControls() {
		try {
		mGameView = (GameView) findViewById(R.id.gameview);
		
		mLayoutBid = (LinearLayout) findViewById(R.id.layout_bid);
		mBtnBid1 = (Button) findViewById(R.id.button_bid1);
		mBtnBid2 = (Button) findViewById(R.id.button_bid2);
		mBtnBid3 = (Button) findViewById(R.id.button_bid3);
		mBtnBidno = (Button) findViewById(R.id.button_bidno);

		mBtnBid1.setOnClickListener(this);
		mBtnBid2.setOnClickListener(this);
		mBtnBid3.setOnClickListener(this);
		mBtnBidno.setOnClickListener(this);

		mLayoutPlay = (LinearLayout) findViewById(R.id.layout_play);
		mBtnPlay = (Button) findViewById(R.id.button_play);
		mBtnPrompt = (Button) findViewById(R.id.button_prompt);
		mBtnReselect = (Button) findViewById(R.id.button_reselect);
		mBtnPass = (Button) findViewById(R.id.button_pass);

		mBtnPlay.setOnClickListener(this);
		mBtnPrompt.setOnClickListener(this);
		mBtnReselect.setOnClickListener(this);
		mBtnPass.setOnClickListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initSettings()
	{
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean showRivalCards = sharedPref.getBoolean("prefShowRivalCards", false);
        RuleManager.get().setShowRivalCards(showRivalCards);
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

	@Override
	protected void onDestroy() {
		Log.d("forge", "GameActivity::onDestroy");
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {

		// Bid
		if (mLayoutBid.getVisibility() == View.VISIBLE) {
			mLayoutBid.setVisibility(View.GONE);

			GameController.EPlayAction a = EPlayAction.eNone;
			switch (v.getId()) {
			case R.id.button_bid1:
				a = EPlayAction.eBid1;
				break;
			case R.id.button_bid2:
				a = EPlayAction.eBid2;
				break;
			case R.id.button_bid3:
				a = EPlayAction.eBid3;
				break;
			case R.id.button_bidno:
				a = EPlayAction.eBidNo;
				break;
			default:
				break;
			}

			GameController.get().onAction(a);
			return;
		}

		// Play cards
		if (mLayoutPlay.getVisibility() == View.VISIBLE) {
			EPlayAction a = EPlayAction.eNone;
			switch (v.getId()) {
			case R.id.button_play:
				a = EPlayAction.ePlayCard;
				break;
			case R.id.button_prompt:
				a = EPlayAction.ePromptCard;
				break;
			case R.id.button_reselect:
				a = EPlayAction.eReselectCard;
				break;
			case R.id.button_pass:
				a = EPlayAction.ePassCard;
				break;
			default:
				break;
			}
			GameController.get().onAction(a);
		}
	}

	public void showBidButtons(boolean isShow, boolean showBid1, boolean showBid2, boolean showBid3) {
		if (isShow) {
			mLayoutBid.setVisibility(View.VISIBLE);
			mBtnBid1.setVisibility(showBid1 ? View.VISIBLE : View.GONE);
			mBtnBid2.setVisibility(showBid2 ? View.VISIBLE : View.GONE);
			mBtnBid3.setVisibility(showBid3 ? View.VISIBLE : View.GONE);
		} else {
			mLayoutBid.setVisibility(View.GONE);
		}
	}

	public void showPlayButtons(boolean isShow, boolean showPass) {
		if (isShow) {
			mLayoutPlay.setVisibility(View.VISIBLE);
			mBtnPass.setVisibility(showPass ? View.VISIBLE : View.GONE);
		} else {
			mLayoutPlay.setVisibility(View.GONE);
		}
	}
}
