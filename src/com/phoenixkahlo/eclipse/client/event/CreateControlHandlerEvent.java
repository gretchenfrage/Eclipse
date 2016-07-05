package com.phoenixkahlo.eclipse.client.event;

import java.util.function.Consumer;
import java.util.function.Function;

import com.phoenixkahlo.eclipse.client.ClientControlHandler;
import com.phoenixkahlo.eclipse.client.ServerConnection;

public class CreateControlHandlerEvent implements Consumer<ServerConnection> {

	private Function<ServerConnection, ClientControlHandler> function;
	
	public CreateControlHandlerEvent(Function<ServerConnection, ClientControlHandler> function) {
		this.function = function;
	}
	
	@Override
	public void accept(ServerConnection connection) {
		connection.setControlHandler(function.apply(connection));
	}
	
}
