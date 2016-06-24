package com.phoenixkahlo.eclipse.server.event;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.server.ClientConnection;
import com.phoenixkahlo.eclipse.server.Server;

/**
 * Server-side event for the connection of a new client.
 */
public class ClientConnectionEvent implements Consumer<Server> {

	private Socket socket;
	
	public ClientConnectionEvent(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void accept(Server server) {
		try {
			server.addClient(new ClientConnection(socket, server));
		} catch (IOException e) {
			System.out.println("Failed to construct client with " + socket + "because:");
			e.printStackTrace(System.out);
		}
	}

}
