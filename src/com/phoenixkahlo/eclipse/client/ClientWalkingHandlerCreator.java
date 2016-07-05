package com.phoenixkahlo.eclipse.client;

import java.util.function.Function;

public class ClientWalkingHandlerCreator implements Function<ServerConnection, ClientControlHandler> {

	private int entityID;
	private int functionHeader;
	
	public ClientWalkingHandlerCreator() {}
	
	public ClientWalkingHandlerCreator(int entityID, int functionHeader) {
		this.entityID = entityID;
		this.functionHeader = functionHeader;
	}

	@Override
	public ClientControlHandler apply(ServerConnection connection) {
		return new ClientWalkingHandler(connection, functionHeader, entityID);
	}
	
}
