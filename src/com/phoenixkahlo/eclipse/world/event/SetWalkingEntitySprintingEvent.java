package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.entity.WalkingEntity;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class SetWalkingEntitySprintingEvent implements Consumer<WorldState> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(SetWalkingEntitySprintingEvent.class, 
				SetWalkingEntitySprintingEvent::new, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(SetWalkingEntitySprintingEvent.class, 
				SetWalkingEntitySprintingEvent::new, subDecoder);
	}
	
	private int id;
	private boolean sprinting;
	
	private SetWalkingEntitySprintingEvent() {}
	
	public SetWalkingEntitySprintingEvent(int id, boolean sprinting) {
		this.id = id;
		this.sprinting = sprinting;
	}
	
	@Override
	public void accept(WorldState state) {
		try {
			((WalkingEntity) state.getEntity(id)).setIsSprinting(sprinting);
		} catch (NullPointerException | ClassCastException e) {
			e.printStackTrace();
		}
	}
	
}
