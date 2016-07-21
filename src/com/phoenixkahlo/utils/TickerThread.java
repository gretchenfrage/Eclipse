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
		// Active waiting because Thread.sleep only guarantees 100 ms accuracy
		while (true) {
			if (System.nanoTime() >= timeForNextTick) {
				event.invoke();
				timeForNextTick += nanoDelay;
			}
		}
	}
	
}
