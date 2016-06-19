package com.phoenixkahlo.eclipse.server;

import java.net.Socket;
import java.util.function.Consumer;

public class ClientConnectionEvent implements Consumer<Server> {

	private Socket socket;
	
	public ClientConnectionEvent(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void accept(Server server) {
		
	}

}
