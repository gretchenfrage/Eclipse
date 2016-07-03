package com.phoenixkahlo.eclipse.server.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.server.ClientConnection;
import com.phoenixkahlo.eclipse.server.Server;

/**
 * Server-side event. 
 * Used for the FunctionReceiverThread to integrate client disconnection with 
 * the main game loop.
 */
public class ClientDisconnectionEvent implements Consumer<Server> {

	private ClientConnection client;
	private String cause;
	
	public ClientDisconnectionEvent(ClientConnection client, String cause) {
		this.client = client;
		this.cause = cause;
	}
	
	public ClientDisconnectionEvent(ClientConnection client, Exception cause) {
		this(client, cause.toString());
	}
	
	@Override
	public void accept(Server server) {
		server.disconnectClient(client, cause);
	}
	
}
