package com.phoenixkahlo.eclipse.server.event;

import java.io.IOException;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.server.ClientConnection;
import com.phoenixkahlo.eclipse.server.Server;
import com.phoenixkahlo.eclipse.world.Entity;
import com.phoenixkahlo.eclipse.world.IDPerspectiveGetter;
import com.phoenixkahlo.eclipse.world.entity.Player;
import com.phoenixkahlo.eclipse.world.event.EntityAdditionEvent;
import com.phoenixkahlo.eclipse.world.event.SetPerspectiveGetterEvent;

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
			// Setup world
			//TODO: have server not broadcast imposeEvent to clients that haven't been initialized
			int reminder;
			client.broadcastSetTime(server.getContinuum().getTime());
			client.broadcastSetWorldState(server.getContinuum().getState());
			Entity player = new Player();
			int time = server.getContinuum().getTime();
			server.imposeEvent(time, new EntityAdditionEvent(player));
			client.broadcastImposeEvent(time, new SetPerspectiveGetterEvent(new IDPerspectiveGetter(player.getID())));
			// TODO: when the missing worldstate requests are added, the server'll need to remember this
			client.setEntityID(player.getID());
			//client.broadcastImposeGetPerspectiveFromEntityEvent(time, player.getID());
		} catch (IOException e) {
			server.disconnectClient(client, e.toString());
		}
	}

}
