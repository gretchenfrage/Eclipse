package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.WorldState;

public class SetVelocityEvent implements Consumer<WorldState> {

	private int id;
	private Vector2 velocity;
	
	public SetVelocityEvent() {}
	
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
