package com.phoenixkahlo.eclipse.server.event;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.server.ClientConnection;
import com.phoenixkahlo.eclipse.server.Server;
import com.phoenixkahlo.eclipse.server.ServerControlHandler;
import com.phoenixkahlo.eclipse.server.ServerWalkingHandler;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.WorldStateContinuum;
import com.phoenixkahlo.eclipse.world.entity.Entity;
import com.phoenixkahlo.eclipse.world.entity.ParsedShip;
import com.phoenixkahlo.eclipse.world.entity.ParsedShipTemplate;
import com.phoenixkahlo.eclipse.world.entity.Player;
import com.phoenixkahlo.eclipse.world.event.AddEntityEvent;
import com.phoenixkahlo.eclipse.world.weapon.Pistol;

/**
 * Server-side event for the connection of a new client.
 */
public class ClientConnectionEvent implements Consumer<Server> {

	private Socket socket;
	
	public ClientConnectionEvent(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void accept(Server server) {
		System.out.println("ClientConnectionEvent accepted in thread " + Thread.currentThread());
		
		try {
			// Set up connection
			ClientConnection connection = new ClientConnection(socket, server);
			//connection.broadcastSetTimeLogiclessly(server.getContinuum().getTime());
			//connection.broadcastSetWorldState(server.getContinuum().getState());
			WorldStateContinuum continuum = new WorldStateContinuum();
			int time = server.getContinuum().getOldestRememberedTime();
			continuum.setWorldState(server.getContinuum().getState(time));
			continuum.setTimeLogiclessly(time);
			for (int n = time; n < server.getContinuum().getTime(); n++) {
				List<Consumer<WorldState>> events = server.getContinuum().getEvents(time);
				if (events != null) {
					for (Consumer<WorldState> event : events) {
						try {
							continuum.imposeEvent(event, n, null);
						} catch (NoSuchFieldException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
			connection.broadcastSetContinuum(continuum);
			
			server.addClient(connection);
			
			// Set up entity
			Player player = new Player();
			player.setWeapon(new Pistol());
			server.imposeEvent(new AddEntityEvent(player));
			
			// Set up control handler
			ServerControlHandler handler = new ServerWalkingHandler(connection, player.getID());
			connection.setAndBroadcastControlHandler(handler);
			
			// Make stuff (temporary code)
			Entity ship = new ParsedShip(ParsedShipTemplate.BASIC_SHIP_1);
			server.imposeEvent(new AddEntityEvent(ship));
			
			// Make final time synchronize request
			connection.broadcastRequestRequestSynchronizeTime();
		} catch (IOException e) {
			System.out.println("Failed to create client with " + socket + "because:");
			e.printStackTrace(System.out);
		}
	}

}
