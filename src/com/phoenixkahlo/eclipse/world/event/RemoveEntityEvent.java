package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class RemoveEntityEvent implements Consumer<WorldState> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(RemoveEntityEvent.class, RemoveEntityEvent::new, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(RemoveEntityEvent.class, RemoveEntityEvent::new, subDecoder);
	}
	
	private int id;
	
	private RemoveEntityEvent() {}
	
	public RemoveEntityEvent(int id) {
		this.id = id;
	}
	
	@Override
	public void accept(WorldState state) {
		state.removeEntity(state.getEntity(id));
	}

}
