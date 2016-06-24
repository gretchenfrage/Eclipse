package com.phoenixkahlo.eclipse.server.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.server.Server;
import com.phoenixkahlo.eclipse.world.WorldState;

/**
 * Server loop event that imposes an event on the WorldStateContinuum.
 */
public class ImposeEventEvent implements Consumer<Server> {

	private int time;
	private Consumer<WorldState> event;
	
	public ImposeEventEvent(int time, Consumer<WorldState> event) {
		this.time = time;
		this.event = event;
	}

	@Override
	public void accept(Server server) {
		server.imposeEvent(time, event);
	}
	
}
