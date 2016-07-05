package com.phoenixkahlo.eclipse.server;

import java.util.function.Function;

import com.phoenixkahlo.eclipse.client.ClientControlHandler;
import com.phoenixkahlo.eclipse.client.ServerConnection;

/**
 * Server side of the ClientControlHandler. Receives data about the client's 
 * movements and controls. Has a limited lifespan after which data will be ignored.
 */
public interface ServerControlHandler {
	
	Function<ServerConnection, ClientControlHandler> getClientHandlerCreator();
	
	/**
	 * End the lifespan of this handler and make any subsequent received data be ignored.
	 */
	void disable();
	
}
