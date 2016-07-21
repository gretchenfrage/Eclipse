package com.phoenixkahlo.eclipse.server;

/**
 * The server side networked functions the client can activate.
 */
public enum ServerFunction {

	/**
	 * args = {int time, Consumer<WorldState> event}
	 */
	IMPOSE_EVENT,
	/**
	 * args = {}
	 */
	DISCONNECT,
	/**
	 * Requests the server to tell the client to bring the continuum to thr correct time.
	 * args = {}
	 */
	REQUEST_SYNCHRONIZE_TIME;
	
}
