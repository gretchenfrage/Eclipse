package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class EntityDeletionEvent implements Consumer<WorldState> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(EntityDeletionEvent.class, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(EntityDeletionEvent.class, EntityDeletionEvent::new, subDecoder);
	}
	
	private int id;
	
	private EntityDeletionEvent() {}
	
	public EntityDeletionEvent(int id) {
		this.id = id;
	}
	
	@Override
	public void accept(WorldState state) {
		state.removeEntity(state.getEntity(id));
	}

}
