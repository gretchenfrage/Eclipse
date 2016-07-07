package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.Ship;
import com.phoenixkahlo.eclipse.world.WorldState;

public class SetShipAngularThrustEvent implements Consumer<WorldState> {

	private int id;
	private byte angularThrust;
	
	public SetShipAngularThrustEvent() {}
	
	public SetShipAngularThrustEvent(int id, byte angularThrust) {
		this.id = id;
		this.angularThrust = angularThrust;
	}
	
	@Override
	public void accept(WorldState state) {
		((Ship) state.getEntity(id)).setAngularThrust(angularThrust);
	}
	
}
