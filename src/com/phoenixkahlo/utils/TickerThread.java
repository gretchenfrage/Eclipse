package com.phoenixkahlo.utils;

public class TickerThread extends Thread {

	private Event event;
	private long nanoDelay;
	
	/**
	 * @param nanoDelay time between ticks in nanoseconds.
	 */
	public TickerThread(Event event, long nanoDelay) {
		super("TickerThread");
		this.event = event;
		this.nanoDelay = nanoDelay;
	}
	
	@Override
	public void run() {
		long timeForNextTick = System.nanoTime();
		while (true) {
			if (System.nanoTime() >= timeForNextTick) {
				event.invoke();
				timeForNextTick += nanoDelay;
			}
		}
	}
	
}
