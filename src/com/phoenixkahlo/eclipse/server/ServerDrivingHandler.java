package com.phoenixkahlo.eclipse.server;

import java.io.IOException;
import java.util.function.Function;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.client.ClientControlHandler;
import com.phoenixkahlo.eclipse.client.ClientDrivingHandlerCreator;
import com.phoenixkahlo.eclipse.client.ServerConnection;
import com.phoenixkahlo.eclipse.world.event.SetShipAngularThrustEvent;
import com.phoenixkahlo.eclipse.world.event.SetShipLinearThrustEvent;

public class ServerDrivingHandler extends BasicServerControlHandler {

	private int playerID;
	private int shipID;
	
	public ServerDrivingHandler(ClientConnection connection, int playerID, int shipID) {
		super(connection);
		
		registerReceiveMethod("receiveSetLinearThrust", int.class, Vector2.class);
		registerReceiveMethod("receiveSetAngularThrust", int.class, byte.class);
		registerReceiveMethod("receiveEscape");
		
		this.playerID = playerID;
		this.shipID = shipID;
	}

	public void receiveSetLinearThrust(int time, Vector2 linearThrust) {
		queueImpose(time, new SetShipLinearThrustEvent(shipID, linearThrust));
	}
	
	public void receiveSetAngularThrust(int time, byte angularThrust) {
		queueImpose(time, new SetShipAngularThrustEvent(shipID, angularThrust));
	}
	
	public void receiveEscape() {
		try {
			getConnection().setAndBroadcastControlHandler(new ServerWalkingHandler(getConnection(), playerID));
		} catch (IOException e) {
			getConnection().disconnect(e);
		}
	}
	
	@Override
	public Function<ServerConnection, ClientControlHandler> getClientHandlerCreator() {
		return new ClientDrivingHandlerCreator(playerID, shipID, getOriginalFunctionHeader());
	}
	
}
