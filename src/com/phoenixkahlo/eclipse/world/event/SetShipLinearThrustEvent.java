package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.entity.Ship;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class SetShipLinearThrustEvent implements Consumer<WorldState> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(SetShipLinearThrustEvent.class, SetShipLinearThrustEvent::new, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(SetShipLinearThrustEvent.class, SetShipLinearThrustEvent::new, subDecoder);
	}
	
	private int id;
	private Vector2 linearThrust;
	
	private SetShipLinearThrustEvent() {}
	
	public SetShipLinearThrustEvent(int id, Vector2 linearThrust) {
		this.id = id;
		this.linearThrust = linearThrust;
	}
	
	@Override
	public void accept(WorldState state) {
		((Ship) state.getEntity(id)).setLinearThrust(linearThrust);
	}
	
}
