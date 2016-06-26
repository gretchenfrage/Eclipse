package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.WorldState;

public class EntityDeletionEvent implements Consumer<WorldState> {

	private int id;
	
	public EntityDeletionEvent() {}
	
	public EntityDeletionEvent(int id) {
		this.id = id;
	}
	
	@Override
	public void accept(WorldState state) {
		state.removeEntity(state.getEntity(id));
	}

}
