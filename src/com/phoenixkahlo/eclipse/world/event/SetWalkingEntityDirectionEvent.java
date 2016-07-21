package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.entity.WalkingEntity;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class SetWalkingEntityDirectionEvent implements Consumer<WorldState> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(SetWalkingEntityDirectionEvent.class, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(SetWalkingEntityDirectionEvent.class, 
				SetWalkingEntityDirectionEvent::new, subDecoder);
	}
	
	private int id;
	private Vector2 direction;
	
	private SetWalkingEntityDirectionEvent() {}
	
	public SetWalkingEntityDirectionEvent(int id, Vector2 direction) {
		this.id = id;
		this.direction = direction;
	}
	
	@Override
	public void accept(WorldState state) {
		try {
			((WalkingEntity) state.getEntity(id)).setDirection(direction);
		} catch (NullPointerException | ClassCastException e) {
			e.printStackTrace();
		}
	}

}
