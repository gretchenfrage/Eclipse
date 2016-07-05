package com.phoenixkahlo.eclipse.client;

import com.phoenixkahlo.networking.FunctionBroadcaster;

/**
 * Handles function registration.
 */
public abstract class NetworkedClientControlHandler implements ClientControlHandler {

	private FunctionBroadcaster broadcaster;
	private int functionHeader;
	
	public NetworkedClientControlHandler(FunctionBroadcaster broadcaster, int functionHeader) {
		this.broadcaster = broadcaster;
		this.functionHeader = functionHeader;
	}
	
	protected void registerBroadcastMethod(Object token) throws IllegalArgumentException {
		broadcaster.registerFunction(functionHeader, token);
		functionHeader++;
	}
	
}
