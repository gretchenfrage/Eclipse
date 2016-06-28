package com.phoenixkahlo.eclipse.client.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.client.ClientConnectionState;
import com.phoenixkahlo.eclipse.client.GetPerspectiveFromEntityEvent;

public class ImposeGetPerspectiveFromEntityEventEvent implements Consumer<ClientConnectionState> {

	private int time;
	private int id;
	
	public ImposeGetPerspectiveFromEntityEventEvent(int time, int id) {
		this.time = time;
		this.id = id;
	}
	
	@Override
	public void accept(ClientConnectionState client) {
		client.imposeEvent(time, new GetPerspectiveFromEntityEvent(id, client));
	}
	
}
