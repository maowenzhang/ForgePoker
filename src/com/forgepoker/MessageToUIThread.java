package com.forgepoker;

public class MessageToUIThread {
	
	public MessageToUIThread(int signal, String str, int arg1)
	{
		mSignal = signal;
		mShowStr.append(str);
		mArg1 = arg1;
	}
	
	public StringBuffer mShowStr = new StringBuffer();
	public int mSignal = 0;
	public int mArg1 = 0;
	
	
	public String toString() {
        return mShowStr.toString();
   }
}
