package com.phoenixkahlo.eclipse.world;

import java.util.function.Function;

public class IDPerspectiveGetter implements Function<WorldState, Perspective>  {

	private int id;
	
	public IDPerspectiveGetter() {}
	
	public IDPerspectiveGetter(int id) {
		this.id = id;
	}

	@Override
	public Perspective apply(WorldState state) {
		return state.getEntity(id).getPerspective();
	}
	
}
