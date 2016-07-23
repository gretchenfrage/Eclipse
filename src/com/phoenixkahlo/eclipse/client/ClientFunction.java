package com.phoenixkahlo.eclipse.client;

/**
 * The client side networked functions the server can activate.
 */
public enum ClientFunction {

	/**
	 * args = {int time}
	 */
	BRING_TO_TIME,
	/**
	 * args = {int time, Consumer<WorldState> event}
	 */
	IMPOSE_EVENT,
	/**
	 * args = {Function<ServerConnection, ControlHandler> function}
	 */
	CREATE_CONTROL_HANDLER,
	/**
	 * Requests the client to request the server to synchronize time.
	 * args = {}
	 */
	REQUEST_REQUEST_SYNCHRONIZE_TIME,
	/**
	 * args = {WorldStateContinuum continuum}
	 */
	SET_CONTINUUM,
	/**
	 * args = {int time}
	 */
	REQUEST_VERIFY_CHECKSUM
	
}
