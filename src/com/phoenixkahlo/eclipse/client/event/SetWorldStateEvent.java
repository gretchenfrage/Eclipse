package com.phoenixkahlo.eclipse.client.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.client.ServerConnection;
import com.phoenixkahlo.eclipse.world.WorldState;

public class SetWorldStateEvent implements Consumer<ServerConnection> {

	private WorldState state;
	
	public SetWorldStateEvent(WorldState state) {
		this.state = state;
	}
	
	@Override
	public void accept(ServerConnection client) {
		client.setWorldState(state);
	}
	
}
