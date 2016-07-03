package com.phoenixkahlo.eclipse.client.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.client.ServerConnection;

public class SetEntityIDEvent implements Consumer<ServerConnection> {

	private int id;
	
	public SetEntityIDEvent(int id) {
		this.id = id;
	}
	
	@Override
	public void accept(ServerConnection connection) {
		connection.setEntityID(id);
	}
	
}
