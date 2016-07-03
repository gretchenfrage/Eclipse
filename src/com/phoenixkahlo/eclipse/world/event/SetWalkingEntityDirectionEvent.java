package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.WalkingEntity;
import com.phoenixkahlo.eclipse.world.WorldState;

public class SetWalkingEntityDirectionEvent implements Consumer<WorldState> {

	private int id;
	private Vector2 direction;
	
	public SetWalkingEntityDirectionEvent() {}
	
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
