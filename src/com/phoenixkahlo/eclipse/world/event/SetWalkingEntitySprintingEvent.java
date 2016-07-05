package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.WalkingEntity;
import com.phoenixkahlo.eclipse.world.WorldState;

public class SetWalkingEntitySprintingEvent implements Consumer<WorldState> {

	private int id;
	private boolean sprinting;
	
	public SetWalkingEntitySprintingEvent() {}
	
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
