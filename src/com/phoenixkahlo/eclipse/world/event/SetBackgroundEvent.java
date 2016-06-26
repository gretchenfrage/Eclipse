package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.Background;
import com.phoenixkahlo.eclipse.world.WorldState;

public class SetBackgroundEvent implements Consumer<WorldState> {

	private Background background;
	
	public SetBackgroundEvent() {}
	
	public SetBackgroundEvent(Background background) {
		this.background = background;
	}
	
	@Override
	public void accept(WorldState state) {
		state.setBackground(background);
	}

}
