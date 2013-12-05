package com.forgepoker;

import com.forgepoker.model.Player;

public class PlayCardRunnable implements Runnable {

	final int mTimeWaitForEachPlayer = 30;
	private final static int ONE_SECOND = 1000;
	
	public PlayCardRunnable()
	{
		
	}
	
	@Override
	public void run() {
		GameController controller = GameController.get();
		boolean bBidFinished = false;
		int i = 0;
		Player curPlayer = controller.CurrentPlayer();
		while(!bBidFinished && i < controller.getPlayerCount())
		{
			curPlayer = controller.CurrentPlayer();
			if(curPlayer.isRobot())
			{
				waitForSecond(2);
				controller.bidForGame();
				if(controller.bidCompleted())
					bBidFinished = true;
			}
			else
			{
				controller.bidForGame();
				int waitTime = mTimeWaitForEachPlayer;
				while (!bBidFinished)
				{
					Player nextPlayer = controller.CurrentPlayer();
					bBidFinished = controller.bidCompleted();
					if(curPlayer == nextPlayer && !bBidFinished)
					{
						waitForSecond(1);
						waitTime--;
					}
					else 
						break;
					
					if(waitTime < 0)
						break;
				}
				
				if(waitTime < 0)
				{
					//TODO forcedly bid
				}	
			}
			
			i++;
		}
		
		if(!bBidFinished && i >=controller.getPlayerCount())
		{
			controller.selectTopBidAsLord();
		}

		
		boolean bGameFinished = false;
		while(!bGameFinished)
		{
			curPlayer = controller.CurrentPlayer();
			if(curPlayer.isRobot())
			{
				waitForSecond(2);
				controller.playCards();
				if(controller.gameFinished())
					bGameFinished = true;
			}
			else
			{
				controller.playCards();
				
				int waitTime = mTimeWaitForEachPlayer;
				while (!bGameFinished)
				{
					Player nextPlayer = controller.CurrentPlayer();
					if(curPlayer == nextPlayer && !controller.isNewRoundBegin() && !controller.gameFinished())
					{
						waitForSecond(1);
						waitTime--;
					}
					else 
					{
						bGameFinished = controller.gameFinished();
						break;
					}
					
					if(waitTime < 0)
						break;
				}
				
				if(waitTime < 0)
				{
					//TODO forcedly play card
				}
			}
		}
		
		if(bGameFinished)
			controller.endGame();
	}
	
	private void waitForSecond(int i) {
		try {
			if(i > 0)
				Thread.sleep(i * ONE_SECOND);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
