package com.forgepoker;

import java.io.IOException;
import java.io.InputStream;

import com.forgepoker.util.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_start);
		
		// Copy the rule file in assets into SD card. We may need to change
		// the rule and save it out. So we need to do thist
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED))
		{
			try {
				copyAssetFileToSDCard("rule.json", "ForgePoker/");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}
	
	public void onClickStartGame(View v) {
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
	
	public void onClickGameRule(View v) {
		Intent intent = new Intent(this, RuleActivity.class);
		startActivity(intent);
	}
	
	public void copyAssetFileToSDCard(String assetName, String dir) throws IOException
    {  
        InputStream inputStream = this.getBaseContext().getAssets().open(assetName);  
 
        FileUtils fileUtil = new FileUtils();
        fileUtil.creatSDDir("ForgePoker/"); 
		fileUtil.creatSDFile("rule.json"); 
		fileUtil.write2SDFromInput(dir, assetName, inputStream);
		inputStream.close();
    }  
}
