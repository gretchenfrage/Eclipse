package com.phoenixkahlo.eclipse.server.event;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.server.ClientConnection;
import com.phoenixkahlo.eclipse.server.Server;
import com.phoenixkahlo.eclipse.server.ServerControlHandler;
import com.phoenixkahlo.eclipse.server.ServerWalkingHandler;
import com.phoenixkahlo.eclipse.world.entity.Entity;
import com.phoenixkahlo.eclipse.world.entity.ParsedShip;
import com.phoenixkahlo.eclipse.world.entity.ParsedShipTemplate;
import com.phoenixkahlo.eclipse.world.entity.Player;
import com.phoenixkahlo.eclipse.world.event.EntityAdditionEvent;
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
		try {
			// Set up connection
			ClientConnection connection = new ClientConnection(socket, server);
			connection.broadcastSetTimeLogiclessly(server.getContinuum().getTime());
			connection.broadcastSetWorldState(server.getContinuum().getState());
			server.addClient(connection);
			
			// Set up entity
			Player player = new Player();
			player.setWeapon(new Pistol());
			server.imposeEvent(new EntityAdditionEvent(player));
			
			// Set up control handler
			ServerControlHandler handler = new ServerWalkingHandler(connection, player.getID());
			connection.setAndBroadcastControlHandler(handler);
			
			// Make stuff (temporary code)
			Entity ship = new ParsedShip(ParsedShipTemplate.BASIC_SHIP_1);
			server.imposeEvent(new EntityAdditionEvent(ship));
			
			// Make final time synchronize request
			connection.broadcastRequestRequestSynchronizeTime();
		} catch (IOException e) {
			System.out.println("Failed to create client with " + socket + "because:");
			e.printStackTrace(System.out);
		}
	}

}
