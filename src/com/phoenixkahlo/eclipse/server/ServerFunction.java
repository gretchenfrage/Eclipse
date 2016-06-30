package com.phoenixkahlo.eclipse.server;

/**
 * The server side networked functions the client can activate.
 */
public enum ServerFunction {

	/**
	 * When the ServerConnection is constructed, it sends this to the server 
	 * to tell the server to initialize the client. 
	 * args = {}
	 */
	INIT_CLIENT,
	/**
	 * args = {int time, Consumer<WorldState> event}
	 */
	IMPOSE_EVENT,
	/**
	 * args = {Vector2 direction}
	 */
	SET_DIRECTION,
	/**
	 * args = {boolean sprinting}
	 */
	SET_IS_SPRINTING,
	/**
	 * args = {}
	 */
	DISCONNECT;
	
}
