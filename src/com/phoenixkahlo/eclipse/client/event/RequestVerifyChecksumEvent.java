package com.phoenixkahlo.eclipse.client.event;

import java.io.IOException;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.client.ServerConnection;

public class RequestVerifyChecksumEvent implements Consumer<ServerConnection> {

	private int time;
	
	public RequestVerifyChecksumEvent(int time) {
		this.time = time;
	}

	@Override
	public void accept(ServerConnection connection) {
		try {
			connection.verifyChecksum(time);
		} catch (IOException e) {
			connection.disconnect(e);
		}
	}
	
}
