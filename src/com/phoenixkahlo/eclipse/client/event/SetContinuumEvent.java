package com.phoenixkahlo.eclipse.client.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.client.ServerConnection;
import com.phoenixkahlo.eclipse.world.WorldStateContinuum;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldEncoder;

public class SetContinuumEvent implements Consumer<ServerConnection> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(SetContinuumEvent.class, SetContinuumEvent::new, subEncoder);
	}
	
	private WorldStateContinuum continuum;
	
	private SetContinuumEvent() {}
	
	public SetContinuumEvent(WorldStateContinuum continuum) {
		this.continuum = continuum;
	}
	
	@Override
	public void accept(ServerConnection connection) {
		connection.setContinuum(continuum);
	}

}
