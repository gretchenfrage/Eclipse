package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.BodyTextureEntity;
import com.phoenixkahlo.eclipse.world.WorldState;

public class SetRenderAngleEvent implements Consumer<WorldState> {

	private int id;
	private float angle;
	
	public SetRenderAngleEvent() {}
	
	public SetRenderAngleEvent(int id, float angle) {
		this.id = id;
		this.angle = angle;
	}
	
	@Override
	public void accept(WorldState state) {
		try {
			((BodyTextureEntity) state.getEntity(id)).setRenderAngle(angle);
		} catch (NullPointerException | ClassCastException e) {
			e.printStackTrace();
		}
	}
	
}
