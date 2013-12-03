package com.forgepoker.model;

public class AIFactory {

	public enum AIDifficulty {
		eNone(0),
		eSimple(1),
		eModerate(2),
		eHard(3),
		eHell(4);
		
		private int mValue; 
		public int getValue() {
			return this.mValue;
		}
		private AIDifficulty(int value) {
			this.mValue = value;
		}
		
		public static AIDifficulty valueOf(int value)
		{
			switch(value)
			{
			case 1: 
				return eSimple;
			case 2:
				return eModerate;
			case 3:
				return eHard;
			case 4:
				return eHell;
			default:
				return eNone;			
			}
		}
	}
	
	static AIRobot createAIRobot(AIDifficulty diff)
	{
		switch(diff)
		{
		case eSimple:
			return new AIRobotSimple();
		case eModerate:
		case eHard:
		case eHell:
		default:
			break;
		}
		return null;
	}
}
