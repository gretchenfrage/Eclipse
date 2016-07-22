package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class SetVelocityEvent implements Consumer<WorldState> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(SetVelocityEvent.class, SetVelocityEvent::new, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(SetVelocityEvent.class, SetVelocityEvent::new, subDecoder);
	}
	
	private int id;
	private Vector2 velocity;
	
	private SetVelocityEvent() {}
	
	public SetVelocityEvent(int id, Vector2 velocity) {
		this.id = id;
		this.velocity = velocity;
	}
	
	@Override
	public void accept(WorldState state) {
		try {
			state.getEntity(id).getBody().setLinearVelocity(velocity);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
}
