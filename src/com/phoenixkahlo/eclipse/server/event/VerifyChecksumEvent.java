package com.phoenixkahlo.eclipse.server.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.server.Server;
import com.phoenixkahlo.eclipse.world.event.SetEntitiesEvent;
import com.phoenixkahlo.utils.CheckSum;

public class VerifyChecksumEvent implements Consumer<Server> {
	
	private int time;
	private CheckSum checksum;
	
	public VerifyChecksumEvent(int time, CheckSum checksum) {
		this.time = time;
		this.checksum = checksum;
	}

	@Override
	public void accept(Server server) {
		if (!server.getContinuum().getChecksum(time).equals(checksum)) {
			server.imposeEvent(time, new SetEntitiesEvent(server.getContinuum().getState(time).getEntities()));
		}
	}
	
}
