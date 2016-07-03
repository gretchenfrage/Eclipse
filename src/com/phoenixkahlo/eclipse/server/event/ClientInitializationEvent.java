package com.phoenixkahlo.eclipse.server.event;

import java.io.IOException;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.server.ClientConnection;
import com.phoenixkahlo.eclipse.server.Server;
import com.phoenixkahlo.eclipse.world.Entity;
import com.phoenixkahlo.eclipse.world.IDPerspectiveGetter;
import com.phoenixkahlo.eclipse.world.event.EntityAdditionEvent;
import com.phoenixkahlo.eclipse.world.event.SetPerspectiveGetterEvent;
import com.phoenixkahlo.eclipse.world.impl.BasicShip1;
import com.phoenixkahlo.eclipse.world.impl.Player;

/**
 * Networked event received by client at the beginning of the connection to set up the client.
 */
public class ClientInitializationEvent implements Consumer<Server> {

	private ClientConnection client;
	
	/**
	 * @param client extra.
	 */
	public ClientInitializationEvent(ClientConnection client) {
		this.client = client;
	}
	
	@Override
	public void accept(Server server) {
		try {
			client.broadcastSetTimeLogiclessly(server.getContinuum().getTime());
			client.broadcastSetWorldState(server.getContinuum().getState());
			client.setIsInitialized();
			
			Entity player = new Player();
			server.imposeEvent(new EntityAdditionEvent(player));
			
			Entity ship = new BasicShip1();
			server.imposeEvent(new EntityAdditionEvent(ship));
			
			client.broadcastImposeEvent(server.getContinuum().getTime(),
					new SetPerspectiveGetterEvent(new IDPerspectiveGetter(player.getID())));
			client.setAndBroadcastEntityID(player.getID());
		} catch (IOException e) {
			server.disconnectClient(client, e);
		}
	}

}
