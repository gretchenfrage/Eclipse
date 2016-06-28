package com.phoenixkahlo.eclipse.client.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.client.ServerConnection;

public class SetTimeEvent implements Consumer<ServerConnection> {

	private int time;
	
	public SetTimeEvent(int time) {
		this.time = time;
	}
	
	@Override
	public void accept(ServerConnection client) {
		client.setTime(time);
	}

}
