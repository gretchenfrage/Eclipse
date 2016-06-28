package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;
import java.util.function.Function;

import com.phoenixkahlo.eclipse.world.Perspective;
import com.phoenixkahlo.eclipse.world.WorldState;

public class SetPerspectiveGetterEvent implements Consumer<WorldState> {

	private Function<WorldState, Perspective> getter;
	
	public SetPerspectiveGetterEvent() {}
	
	public SetPerspectiveGetterEvent(Function<WorldState, Perspective> getter) {
		this.getter = getter;
	}
	
	@Override
	public void accept(WorldState state) {
		state.setPerspectiveGetter(getter);
	}
	
}
