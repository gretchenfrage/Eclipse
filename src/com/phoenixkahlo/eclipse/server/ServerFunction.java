package com.phoenixkahlo.eclipse.server;

/**
 * The server side networked functions the client can activate.
 */
public enum ServerFunction {

	/**
	 * args = {}
	 */
	INIT_CLIENT,
	/**
	 * args = {int time, Consumer<WorldState> event}
	 */
	IMPOSE_EVENT,
	/**
	 * args = {}
	 */
	DISCONNECT;
	
}
