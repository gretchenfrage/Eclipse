package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.impl.Player;

public class RightTriggerPlayerEvent implements Consumer<WorldState> {

	private int id;
	private Vector2 worldPos;
	
	public RightTriggerPlayerEvent() {}
	
	public RightTriggerPlayerEvent(int id, Vector2 worldPos) {
		this.id = id;
		this.worldPos = worldPos;
	}
	
	@Override
	public void accept(WorldState state) {
		try {
			((Player) state.getEntity(id)).rightTrigger(worldPos, state);
		} catch (ClassCastException | NullPointerException e) {
			e.printStackTrace();
		}
	}
	
}
