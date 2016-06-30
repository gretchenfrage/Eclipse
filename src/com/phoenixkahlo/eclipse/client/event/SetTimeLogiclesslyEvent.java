package com.phoenixkahlo.eclipse.client.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.client.ServerConnection;

public class SetTimeLogiclesslyEvent implements Consumer<ServerConnection> {

	private int time;
	
	public SetTimeLogiclesslyEvent(int time) {
		this.time = time;
	}
	
	@Override
	public void accept(ServerConnection client) {
		client.setTimeLogiclessly(time);
	}

}
