package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.entity.Player;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class SetPlayerFacingAngleEvent implements Consumer<WorldState> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(SetPlayerFacingAngleEvent.class, SetPlayerFacingAngleEvent::new, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(SetPlayerFacingAngleEvent.class, SetPlayerFacingAngleEvent::new, subDecoder);
	}
	
	private int id;
	private float angle;
	
	private SetPlayerFacingAngleEvent() {}
	
	public SetPlayerFacingAngleEvent(int id, float angle) {
		this.id = id;
		this.angle = angle;
	}
	
	@Override
	public void accept(WorldState state) {
		try {
			((Player) state.getEntity(id)).setFacingAngle(angle);
		} catch (NullPointerException | ClassCastException e) {
			e.printStackTrace();
		}
	}

}
