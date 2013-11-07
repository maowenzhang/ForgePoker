package com.forgepoker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import com.forgepoker.R;

/**
 * Represents startup view (several menu items: start game, exit game, etc.)
 * @author zhanglo
 *
 */
public class StartupView extends SurfaceView implements SurfaceHolder.Callback, 
	OnTouchListener
{
	private MainActivity mainActi;
	SurfaceHolder holder;
	Canvas canvas;
	boolean threadFlag = true;
	Bitmap back;
	
	private int x = 270;
	private int y = 50;
	private Bitmap[] menuItems;
	
	
	public StartupView(Context context, MainActivity mainActi) {
		super(context);
		this.mainActi = mainActi;
		menuItems = new Bitmap[5];
		holder = getHolder();
		back = BitmapFactory
				.decodeResource(mainActi.getResources(), R.drawable.menu);
		menuItems[0] = BitmapFactory.decodeResource(mainActi.getResources(),
				R.drawable.menu1);
		menuItems[1] = BitmapFactory.decodeResource(mainActi.getResources(),
				R.drawable.menu2);
		menuItems[2] = BitmapFactory.decodeResource(mainActi.getResources(),
				R.drawable.menu3);
		menuItems[3] = BitmapFactory.decodeResource(mainActi.getResources(),
				R.drawable.menu4);
		menuItems[4] = BitmapFactory.decodeResource(mainActi.getResources(),
				R.drawable.menu5);
		// for(int i=0;i<menuItems.length;i++)
		// {
		// menuItems[0]=BitmapFactory.decodeFile("menu"+(i+1)+".png");
		// }

		this.getHolder().addCallback(this);
		this.setOnTouchListener(this);
	}

	Thread menuThread = new Thread() {
		@Override
		public void run() {

			while (threadFlag) {
				try {
					canvas = holder.lockCanvas();
					synchronized (this) {
						//onDraw(canvas);
						draw(canvas);
					}
					// System.out.println("menuThread");
				} finally {
					holder.unlockCanvasAndPost(canvas);
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	};

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		canvas.drawBitmap(back, 0, 0, paint);
		for (int i = 0; i < menuItems.length; i++) {
			canvas.drawBitmap(menuItems[i], x, y + i * 43, paint);
		}
		// paint.setColor(Color.WHITE);
		// paint.setTextSize(32);
		// canvas.drawText("开始游戏", 158, 91, paint);
		// canvas.drawText("游戏帮助", 158, 121, paint);
		// canvas.drawText("关于游戏", 158, 151, paint);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		threadFlag = true;
		menuThread.start();
		System.out.println("surfaceCreated");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		threadFlag = false;
		boolean retry = true;
		while (retry) {
			try {
				menuThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int ex = (int) event.getX();
		int ey = (int) event.getY();
		System.out.println(event.getX() + "," + event.getY());
		int selectIndex = -1;
		for (int i = 0; i < menuItems.length; i++) {
			System.out.println(x+"  "+(y+i*43));
//			if (Poke.inRect(ex, ey, x, y + i * 43, 125, 33)) {
//				selectIndex = i;
//				break;
//			}
		}
		System.out.println(selectIndex);
		switch (selectIndex) {
		case 0:
			mainActi.handler.sendEmptyMessage(mainActi.GAME);
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			mainActi.finish();
			break;
		}
		return super.onTouchEvent(event);
	}
}
