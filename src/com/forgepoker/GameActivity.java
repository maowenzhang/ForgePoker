package com.forgepoker;

import com.forgepoker.GameController.EPlayAction;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("forge", "GameActivity::onCreate");

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_game);

		initControls();

		mLayoutBid.setVisibility(View.GONE);
		mLayoutPlay.setVisibility(View.GONE);
		GameController.get().setGameActivity(this);
		
		GameController.get().startGame();
	}

	private void initControls() {
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

	public void showBidButtons(boolean isShow, boolean hideBid1, boolean hideBid2) {
		if (isShow) {
			mLayoutBid.setVisibility(View.VISIBLE);
			if (hideBid1) {
				mBtnBid1.setVisibility(View.GONE);
			}
			if (hideBid2) {
				mBtnBid2.setVisibility(View.GONE);
			}
			
		} else {
			mLayoutBid.setVisibility(View.GONE);
		}
	}

	public void showPlayButtons(boolean isShow) {
		if (isShow) {
			mLayoutPlay.setVisibility(View.VISIBLE);
		} else {
			mLayoutPlay.setVisibility(View.GONE);
		}
	}
}
