package com.phoenixkahlo.eclipse.client;

/**
 * The client side networked functions the server can activate.
 */
public enum ClientFunction {

	/**
	 * argstypes = {int}
	 */
	SET_TIME,
	/**
	 * argtypes = {WorldState}
	 */
	SET_WORLD_STATE,
	/**
	 * argtypes = {int}
	 */
	SET_PERSPECTIVE_TO_ENTITY;
	
}
