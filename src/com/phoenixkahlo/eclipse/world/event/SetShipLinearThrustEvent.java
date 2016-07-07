package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.Ship;
import com.phoenixkahlo.eclipse.world.WorldState;

public class SetShipLinearThrustEvent implements Consumer<WorldState> {

	private int id;
	private Vector2 linearThrust;
	
	public SetShipLinearThrustEvent() {}
	
	public SetShipLinearThrustEvent(int id, Vector2 linearThrust) {
		this.id = id;
		this.linearThrust = linearThrust;
	}
	
	@Override
	public void accept(WorldState state) {
		((Ship) state.getEntity(id)).setLinearThrust(linearThrust);
	}
	
}
