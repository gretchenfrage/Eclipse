package com.phoenixkahlo.eclipse.client;

/**
 * The client side networked functions the server can activate.
 */
public enum ClientFunction {

	/**
	 * args = {int time}
	 */
	SET_TIME_LOGICLESSLY,
	/**
	 * args = {int time}
	 */
	BRING_TO_TIME,
	/**
	 * args = {WorldState state}
	 */
	SET_WORLD_STATE,
	/**
	 * args = {int time, Consumer<WorldState> event}
	 */
	IMPOSE_EVENT,
	/**
	 * args = {Function<ServerConnection, ControlHandler> function}
	 */
	CREATE_CONTROL_HANDLER;
	
}
