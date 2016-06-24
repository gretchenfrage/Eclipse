package com.phoenixkahlo.eclipse.server.event;

import java.io.IOException;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.server.ClientConnection;
import com.phoenixkahlo.eclipse.server.Server;

/**
 * Networked event received by client at the beginning of the connection to set up the client.
 */
public class ClientInitializationEvent implements Consumer<Server> {

	private ClientConnection connection;
	
	/**
	 * @param connection extra.
	 */
	public ClientInitializationEvent(ClientConnection connection) {
		this.connection = connection;
	}
	
	@Override
	public void accept(Server server) {
		try {
			connection.broadcastSetTime(server.getContinuum().getTime());
			connection.broadcastSetWorldState(server.getContinuum().getState());
			if (connection.getEntityID() >= 0)
				connection.broadcastSetPerspectiveToEntity(connection.getEntityID());
		} catch (IOException e) {
			server.disconnectClient(connection, e.toString());
		}
	}

}
