package com.phoenixkahlo.eclipse.server;

import java.util.function.Consumer;

/**
 * Networked event received by client at the beginning of the connection to set up the client.
 */
public class InitClientEvent implements Consumer<Server> {

	private ClientConnection connection;
	
	/**
	 * @param connection extra.
	 */
	public InitClientEvent(ClientConnection connection) {
		this.connection = connection;
		System.out.println(this + " constructed");
	}
	
	@Override
	public void accept(Server server) {
		connection.broadcastSetTime(server.getContinuum().getTime());
		connection.broadcastSetWorldState(server.getContinuum().getState());
		if (connection.getEntityID() >= 0)
			connection.broadcastSetPerspectiveToEntity(connection.getEntityID());
	}

}
