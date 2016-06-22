package com.phoenixkahlo.eclipse.server;

import java.net.Socket;
import java.util.function.Consumer;

/**
 * Entirely server-side event for the connection of a new client.
 */
public class ClientConnectionEvent implements Consumer<Server> {

	private Socket socket;
	
	public ClientConnectionEvent(Socket socket) {
		this.socket = socket;
		System.out.println(this + " constructed");
	}
	
	@Override
	public void accept(Server server) {
		server.addClient(new ClientConnection(socket, server));
	}

}
