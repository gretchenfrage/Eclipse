package com.phoenixkahlo.eclipse.client;

import java.util.function.Function;

import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class ClientWalkingHandlerCreator implements Function<ServerConnection, ClientControlHandler> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(ClientWalkingHandlerCreator.class, ClientWalkingHandlerCreator::new,
				subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(ClientWalkingHandlerCreator.class, ClientWalkingHandlerCreator::new,
				subDecoder);
	}
	
	private int entityID;
	private int functionHeader;
	
	private ClientWalkingHandlerCreator() {}
	
	public ClientWalkingHandlerCreator(int entityID, int functionHeader) {
		this.entityID = entityID;
		this.functionHeader = functionHeader;
	}

	@Override
	public ClientControlHandler apply(ServerConnection connection) {
		return new ClientWalkingHandler(connection, functionHeader, entityID);
	}
	
}
