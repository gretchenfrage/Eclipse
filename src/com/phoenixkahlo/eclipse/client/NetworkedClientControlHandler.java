package com.phoenixkahlo.eclipse.client;

import java.io.IOException;

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
	
	protected void registerBroadcastToken(Object token) throws IllegalArgumentException {
		broadcaster.registerFunction(functionHeader, token);
		functionHeader++;
	}
	
	protected void broadcast(Object token, Object... args) throws IOException {
		broadcaster.broadcast(token, args);
	}
	
}
