package com.phoenixkahlo.eclipse.server.event;

import java.io.IOException;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.server.ClientConnection;
import com.phoenixkahlo.eclipse.server.Server;
import com.phoenixkahlo.eclipse.server.ServerWalkingHandler;
import com.phoenixkahlo.eclipse.world.Entity;
import com.phoenixkahlo.eclipse.world.event.EntityAdditionEvent;
import com.phoenixkahlo.eclipse.world.impl.BasicShip1;
import com.phoenixkahlo.eclipse.world.impl.Player;

/**
 * Networked event received by connection at the beginning of the connection to set up the connection.
 */
public class ClientInitializationEvent implements Consumer<Server> {

	private ClientConnection connection;
	
	/**
	 * @param connection extra.
	 */
	public ClientInitializationEvent(ClientConnection client) {
		this.connection = client;
	}
	
	@Override
	public void accept(Server server) {
		try {
			connection.broadcastSetTimeLogiclessly(server.getContinuum().getTime());
			connection.broadcastSetWorldState(server.getContinuum().getState());
			connection.setIsInitialized();
			
			Entity player = new Player();
			server.imposeEvent(new EntityAdditionEvent(player));
			
			Entity ship = new BasicShip1();
			server.imposeEvent(new EntityAdditionEvent(ship));
			
			connection.setAndBroadcastControlHandler(new ServerWalkingHandler(connection, player.getID()));
		} catch (IOException e) {
			server.disconnectClient(connection, e);
		}
	}

}
