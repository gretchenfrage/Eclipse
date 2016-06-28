package com.phoenixkahlo.eclipse.client.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.client.ServerConnection;
import com.phoenixkahlo.eclipse.world.WorldState;

public class ImposeEventEvent implements Consumer<ServerConnection> {

	int time;
	private Consumer<WorldState> event;
	
	public ImposeEventEvent(int time, Consumer<WorldState> event) {
		this.time = time;
		this.event = event;
	}
	
	@Override
	public void accept(ServerConnection client) {
		client.imposeEvent(time, event);
	}

}
