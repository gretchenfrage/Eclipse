package com.phoenixkahlo.eclipse.server;

import java.util.function.Consumer;
import java.util.function.Function;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.client.ClientControlHandler;
import com.phoenixkahlo.eclipse.client.ClientWalkingHandlerCreator;
import com.phoenixkahlo.eclipse.client.ServerConnection;
import com.phoenixkahlo.eclipse.server.event.ImposeEventEvent;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.event.SetPlayerFacingAngleEvent;
import com.phoenixkahlo.eclipse.world.event.SetWalkingEntityDirectionEvent;
import com.phoenixkahlo.eclipse.world.event.SetWalkingEntitySprintingEvent;

public class ServerWalkingHandler extends NetworkedServerControlHandler {
	
	private Server server;
	private int entityID;
	
	public ServerWalkingHandler(ClientConnection connection, int entityID) {
		super(connection.getReceiver());
		
		server = connection.getServer();
		this.entityID = entityID;
		
		registerReceiveMethod("receiveSetDirection", int.class, Vector2.class);
		registerReceiveMethod("receiveSetSprinting", int.class, boolean.class);
		registerReceiveMethod("receiveSetAngle", int.class, float.class);
	}

	@Override
	public Function<ServerConnection, ClientControlHandler> getClientHandlerCreator() {
		return new ClientWalkingHandlerCreator(entityID, getOriginalFunctionHeader());
	}
	
	public void receiveSetDirection(int time, Vector2 direction) {
		queueImpose(time, new SetWalkingEntityDirectionEvent(entityID, direction));
	}
	
	public void receiveSetSprinting(int time, boolean sprinting) {
		queueImpose(time, new SetWalkingEntitySprintingEvent(entityID, sprinting));
	}
	
	public void receiveSetAngle(int time, float angle) {
		queueImpose(time, new SetPlayerFacingAngleEvent(entityID, angle));
	}
	
	private void queueImpose(int time, Consumer<WorldState> event) {
		server.queueEvent(new ImposeEventEvent(time, event));
	}
	
}
