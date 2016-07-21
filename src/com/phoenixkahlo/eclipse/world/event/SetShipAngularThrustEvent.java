package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.entity.Ship;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class SetShipAngularThrustEvent implements Consumer<WorldState> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(SetShipAngularThrustEvent.class, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(SetShipAngularThrustEvent.class, SetShipAngularThrustEvent::new, subDecoder);
	}
	
	private int id;
	private byte angularThrust;
	
	private SetShipAngularThrustEvent() {}
	
	public SetShipAngularThrustEvent(int id, byte angularThrust) {
		this.id = id;
		this.angularThrust = angularThrust;
	}
	
	@Override
	public void accept(WorldState state) {
		((Ship) state.getEntity(id)).setAngularThrust(angularThrust);
	}
	
}
