package com.phoenixkahlo.eclipse.client.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.client.ClientConnectionState;
import com.phoenixkahlo.eclipse.world.WorldState;

public class SetWorldStateEvent implements Consumer<ClientConnectionState> {

	private WorldState state;
	
	public SetWorldStateEvent(WorldState state) {
		this.state = state;
	}
	
	@Override
	public void accept(ClientConnectionState client) {
		client.setWorldState(state);
	}
	
}
