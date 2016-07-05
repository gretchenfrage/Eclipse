package com.phoenixkahlo.eclipse.server;

import java.util.function.Function;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.client.ClientControlHandler;
import com.phoenixkahlo.eclipse.client.ClientWalkingHandlerCreator;
import com.phoenixkahlo.eclipse.client.ServerConnection;
import com.phoenixkahlo.eclipse.world.event.SetWalkingEntityDirectionEvent;
import com.phoenixkahlo.eclipse.world.event.SetWalkingEntitySprintingEvent;

public class ServerWalkingHandler extends NetworkedServerControlHandler {
	
	private Server server;
	private int entityID;
	
	public ServerWalkingHandler(ClientConnection connection, int entityID) {
		super(connection.getReceiver());
		
		server = connection.getServer();
		this.entityID = entityID;
		
		registerReceiveMethod("receiveSetDirection", Vector2.class);
		registerReceiveMethod("receiveSetSprinting", boolean.class);
	}

	@Override
	public Function<ServerConnection, ClientControlHandler> getClientHandlerCreator() {
		return new ClientWalkingHandlerCreator(entityID, getOriginalFunctionHeader());
	}
	
	public void receiveSetDirection(Vector2 direction) {
		server.imposeEvent(new SetWalkingEntityDirectionEvent(entityID, direction));
	}
	
	public void receiveSetSprinting(boolean sprinting) {
		server.imposeEvent(new SetWalkingEntitySprintingEvent(entityID, sprinting));
	}
	
}
