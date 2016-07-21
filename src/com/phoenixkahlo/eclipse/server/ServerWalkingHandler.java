package com.phoenixkahlo.eclipse.server;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.client.ClientControlHandler;
import com.phoenixkahlo.eclipse.client.ClientWalkingHandlerCreator;
import com.phoenixkahlo.eclipse.client.ServerConnection;
import com.phoenixkahlo.eclipse.world.entity.Entity;
import com.phoenixkahlo.eclipse.world.event.PlayerUseEvent;
import com.phoenixkahlo.eclipse.world.event.PlayerUseWeaponEvent;
import com.phoenixkahlo.eclipse.world.event.SetPlayerFacingAngleEvent;
import com.phoenixkahlo.eclipse.world.event.SetWalkingEntityDirectionEvent;
import com.phoenixkahlo.eclipse.world.event.SetWalkingEntitySprintingEvent;

public class ServerWalkingHandler extends BasicServerControlHandler {
	
	private int entityID;
	
	public ServerWalkingHandler(ClientConnection connection, int entityID) {
		super(connection);
		
		this.entityID = entityID;
		
		registerReceiveMethod("receiveSetDirection", int.class, Vector2.class);
		registerReceiveMethod("receiveSetSprinting", int.class, boolean.class);
		registerReceiveMethod("receiveSetAngle", int.class, float.class);
		registerReceiveMethod("receiveUse", int.class);
		registerReceiveMethod("receiveUseWeapon", int.class, Vector2.class);
		
		registerReceiveMethod("receivePrintState");
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
	
	public void receiveUseWeapon(int time, Vector2 target) {
		queueImpose(time, new PlayerUseWeaponEvent(entityID, target));
	}
	
	public void receiveUse(int time) {
		queueImpose(time, new PlayerUseEvent(entityID));
		
		ClientConnection connection = getConnection();
		Server server = connection.getServer();
		
		Vector2 position = server.getContinuum().getState().getEntity(entityID).getBody().getWorldCenter();
		for (Entity entity : server.getContinuum().getState().getEntities()) {
			BiFunction<ClientConnection, Integer, ServerControlHandler> function = entity.getHandler(position);
			if (function != null) {
				ServerControlHandler handler = function.apply(connection, entityID);
				try {
					connection.setAndBroadcastControlHandler(handler);
				} catch (IOException e) {
					connection.disconnect(e);
				}
				return;
			}
		}
	}
	
	public void receivePrintState() {
		System.out.println(getConnection().getServer().getContinuum().getState());
	}
	
}
