package com.phoenixkahlo.eclipse.client.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.client.ServerConnection;

public class BringToTimeEvent implements Consumer<ServerConnection> {

	private int time;
	
	public BringToTimeEvent() {}
	
	public BringToTimeEvent(int time) {
		this.time = time;
	}
	
	@Override
	public void accept(ServerConnection connection) {
		connection.bringToTime(time);
	}
	
}
