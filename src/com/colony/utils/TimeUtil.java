package com.colony.utils;

public class TimeUtil {



	/**
	 * @param howMuch in ms.
	 */
	public static void delay(long howMuch) {
		try {
			Thread.sleep(howMuch);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}


}
