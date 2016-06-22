package com.phoenixkahlo.eclipse.server;

import com.phoenixkahlo.utils.Event;

public class TickerThread extends Thread {

	private Event event;
	private long nanoDelay;
	
	/**
	 * @param nanoDelay time between ticks in nanoseconds.
	 */
	public TickerThread(Event event, long nanoDelay) {
		super("Ticker thread, triggering " + event);
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
