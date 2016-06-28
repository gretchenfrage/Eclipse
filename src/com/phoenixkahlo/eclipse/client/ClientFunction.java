package com.phoenixkahlo.eclipse.client;

/**
 * The client side networked functions the server can activate.
 */
public enum ClientFunction {

	/**
	 * args = {int time}
	 */
	SET_TIME,
	/**
	 * args = {WorldState state}
	 */
	SET_WORLD_STATE,
	/**
	 * args = {int time, Consumer<WorldState> event}
	 */
	IMPOSE_EVENT;
	
}
