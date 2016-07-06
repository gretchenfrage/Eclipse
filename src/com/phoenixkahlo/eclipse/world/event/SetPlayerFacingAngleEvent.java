package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.impl.Player;

public class SetPlayerFacingAngleEvent implements Consumer<WorldState> {

	private int id;
	private float angle;
	
	public SetPlayerFacingAngleEvent() {}
	
	public SetPlayerFacingAngleEvent(int id, float angle) {
		this.id = id;
		this.angle = angle;
	}
	
	@Override
	public void accept(WorldState state) {
		try {
			((Player) state.getEntity(id)).setFacingAngle(angle);
		} catch (NullPointerException | ClassCastException e) {
			e.printStackTrace();
		}
	}

}
