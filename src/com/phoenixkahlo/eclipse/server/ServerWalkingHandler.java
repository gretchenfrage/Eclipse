package com.phoenixkahlo.eclipse.server;

import java.util.function.Function;

import com.phoenixkahlo.eclipse.client.ClientControlHandler;
import com.phoenixkahlo.eclipse.client.ClientWalkingHandlerCreator;
import com.phoenixkahlo.eclipse.client.ServerConnection;

public class ServerWalkingHandler extends NetworkedServerControlHandler {
	
	private int entityID;
	
	public ServerWalkingHandler(ClientConnection connection, int entityID) {
		super(connection.getReceiver());
		this.entityID = entityID;
		
		registerReceiveMethod("receiveG");
	}

	@Override
	public Function<ServerConnection, ClientControlHandler> getClientHandlerCreator() {
		return new ClientWalkingHandlerCreator(entityID, getOriginalFunctionHeader());
	}
	
	public void receiveG() {
		System.out.println("received G");
	}
	
}
