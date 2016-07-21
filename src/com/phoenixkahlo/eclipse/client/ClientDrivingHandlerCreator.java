package com.phoenixkahlo.eclipse.client;

import java.util.function.Function;

import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class ClientDrivingHandlerCreator implements Function<ServerConnection, ClientControlHandler> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(ClientDrivingHandlerCreator.class, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(ClientDrivingHandlerCreator.class, ClientDrivingHandlerCreator::new,
				subDecoder);
	}
	
	private int playerID;
	private int shipID;
	private int functionHeader;
	
	private ClientDrivingHandlerCreator() {}
	
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
