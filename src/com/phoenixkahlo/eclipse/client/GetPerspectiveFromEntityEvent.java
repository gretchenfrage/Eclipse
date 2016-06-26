package com.phoenixkahlo.eclipse.client;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.WorldState;

/**
 * The reason this is not in the eclipse.world.event package is because this: 
 * -Does not mutate the WorldState
 * -Is dependent on a ClientConnectionState instance
 * -Is nonexistent on server and asynchronous between clients
 */
public class GetPerspectiveFromEntityEvent implements Consumer<WorldState> {

	private int id;
	private ClientConnectionState client;
	
	public GetPerspectiveFromEntityEvent(int id, ClientConnectionState client) {
		this.id = id;
		this.client = client;
	}

	@Override
	public void accept(WorldState state) {
		System.out.println("GetPerspectiveFromEntityEvent accepted");
		client.setPerspective(state.getEntity(id).getPerspective());
	}
	
}
