package com.phoenixkahlo.eclipse.client.event;

import java.io.IOException;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.client.ServerConnection;

public class RequestSynchronizeTimeEvent implements Consumer<ServerConnection> {

	@Override
	public void accept(ServerConnection connection) {
		try {
			connection.broadcastRequestSynchronizeTime();
		} catch (IOException e) {
			connection.disconnect(e);
		}
	}

}
