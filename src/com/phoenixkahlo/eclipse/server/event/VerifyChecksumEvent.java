package com.phoenixkahlo.eclipse.server.event;

import java.io.IOException;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.server.ClientConnection;
import com.phoenixkahlo.eclipse.server.Server;
import com.phoenixkahlo.eclipse.world.event.SetEntitiesEvent;
import com.phoenixkahlo.utils.CheckSum;

public class VerifyChecksumEvent implements Consumer<Server> {
	
	private ClientConnection client;
	private int time;
	private CheckSum checksum;
	
	public VerifyChecksumEvent(ClientConnection client, int time, CheckSum checksum) {
		this.client = client;
		this.time = time;
		this.checksum = checksum;
	}

	@Override
	public void accept(Server server) {
		if (server.getContinuum().getChecksum(time).equals(checksum)) {
			System.out.println("checksums verified for time " + time);
		} else {
			System.err.println("checksums failed for time " + time + " (correcting)");
			try {
				client.broadcastImposeEvent(time, new SetEntitiesEvent(server.getContinuum().
						getState(time).getEntities()));
			} catch (IOException e) {
				server.disconnectClient(client, e);
			}
		}
	}
	
}
