package com.phoenixkahlo.eclipse.client.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.client.ClientConnectionState;
import com.phoenixkahlo.eclipse.world.WorldState;

public class ImposeEventEvent implements Consumer<ClientConnectionState> {

	int time;
	private Consumer<WorldState> event;
	
	public ImposeEventEvent(int time, Consumer<WorldState> event) {
		this.time = time;
		this.event = event;
	}
	
	@Override
	public void accept(ClientConnectionState client) {
		client.imposeEvent(time, event);
	}

}
