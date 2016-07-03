package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.WalkingEntity;
import com.phoenixkahlo.eclipse.world.WorldState;

public class SetWalkingEntityIsSprintingEvent implements Consumer<WorldState> {

	private int id;
	private boolean isSprinting;
	
	public SetWalkingEntityIsSprintingEvent() {}
	
	public SetWalkingEntityIsSprintingEvent(int id, boolean isSprinting) {
		this.id = id;
		this.isSprinting = isSprinting;
	}
	
	@Override
	public void accept(WorldState state) {
		try {
			((WalkingEntity) state.getEntity(id)).setIsSprinting(isSprinting);
		} catch (NullPointerException | ClassCastException e) {
			e.printStackTrace();
		}
	}
	
}
