package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.Entity;
import com.phoenixkahlo.eclipse.world.WorldState;

public class EntityAdditionEvent implements Consumer<WorldState> {

	private Entity entity;
	
	public EntityAdditionEvent() {}
	
	public EntityAdditionEvent(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void accept(WorldState state) {
		state.addEntity(entity);
	}
	

}
