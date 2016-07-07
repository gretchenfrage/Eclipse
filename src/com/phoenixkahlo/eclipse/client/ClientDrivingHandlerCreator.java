package com.phoenixkahlo.eclipse.client;

import java.util.function.Function;

public class ClientDrivingHandlerCreator implements Function<ServerConnection, ClientControlHandler> {

	private int playerID;
	private int shipID;
	private int functionHeader;
	
	public ClientDrivingHandlerCreator() {}
	
	public ClientDrivingHandlerCreator(int playerID, int shipID, int functionHeader) {
		this.playerID = playerID;
		this.shipID = shipID;
		this.functionHeader = functionHeader;
	}
	
	@Override
	public ClientControlHandler apply(ServerConnection connection) {
		return new ClientDrivingHandler(connection, functionHeader, playerID, shipID);
	}
	
}
