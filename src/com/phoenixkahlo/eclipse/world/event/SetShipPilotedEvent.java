package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.entity.Ship;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class SetShipPilotedEvent implements Consumer<WorldState> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(SetShipPilotedEvent.class, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(SetShipPilotedEvent.class, SetShipPilotedEvent::new, subDecoder);
	}
	
	private int id;
	private boolean piloted;
	
	private SetShipPilotedEvent() {}
	
	public SetShipPilotedEvent(int id, boolean piloted) {
		this.id = id;
		this.piloted = piloted;
	}
	
	@Override
	public void accept(WorldState state) {
		((Ship) state.getEntity(id)).setIsPiloted(piloted);
	}
	
}
