package com.phoenixkahlo.eclipse.server;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.client.ClientControlHandler;
import com.phoenixkahlo.eclipse.client.ClientDrivingHandlerCreator;
import com.phoenixkahlo.eclipse.client.ServerConnection;
import com.phoenixkahlo.eclipse.world.entity.Entity;
import com.phoenixkahlo.eclipse.world.entity.RelativeLocationLock;
import com.phoenixkahlo.eclipse.world.entity.RelativePlayerFacingAngleLock;
import com.phoenixkahlo.eclipse.world.entity.Ship;
import com.phoenixkahlo.eclipse.world.event.AddEntityEvent;
import com.phoenixkahlo.eclipse.world.event.RemoveEntityEvent;
import com.phoenixkahlo.eclipse.world.event.SetShipAngularThrustEvent;
import com.phoenixkahlo.eclipse.world.event.SetShipLinearThrustEvent;
import com.phoenixkahlo.eclipse.world.event.SetShipPilotedEvent;
import com.phoenixkahlo.utils.MathUtils;

public class ServerDrivingHandler extends BasicServerControlHandler {

	private int playerID;
	private int shipID;
	private int locationLockID;
	private int angleLockID;
	
	public ServerDrivingHandler(ClientConnection connection, int playerID, int shipID) {
		super(connection);
		
		registerReceiveMethod("receiveSetLinearThrust", int.class, Vector2.class);
		registerReceiveMethod("receiveSetAngularThrust", int.class, byte.class);
		registerReceiveMethod("receiveEscape");
		
		this.playerID = playerID;
		this.shipID = shipID;
		
		Server server = connection.getServer();
		
		Ship ship = (Ship) connection.getServer().getContinuum().getState().getEntity(shipID);
		
		Entity locationLock = new RelativeLocationLock(playerID, shipID, ship.getHelmPos());
		locationLockID = locationLock.getID();
		server.imposeEvent(new AddEntityEvent(locationLock));
		
		Entity angleLock = new RelativePlayerFacingAngleLock(playerID, shipID, -MathUtils.PI_F / 2);
		angleLockID = angleLock.getID();
		server.imposeEvent(new AddEntityEvent(angleLock));
		
		server.imposeEvent(new SetShipPilotedEvent(shipID, true));
	}

	public void receiveSetLinearThrust(int time, Vector2 linearThrust) {
		queueImpose(time, new SetShipLinearThrustEvent(shipID, linearThrust));
	}
	
	public void receiveSetAngularThrust(int time, byte angularThrust) {
		queueImpose(time, new SetShipAngularThrustEvent(shipID, angularThrust));
	}
	
	public void receiveEscape() {
		queue(new Consumer<Server>() {
			
			@Override
			public void accept(Server server) {
				try {
					getConnection().setAndBroadcastControlHandler(
							new ServerWalkingHandler(getConnection(), playerID));
				} catch (IOException e) {
					getConnection().disconnect(e);
				}
			}
			
		});
		
		int time = getConnection().getServer().getContinuum().getTime();
		
		queueImpose(time, new RemoveEntityEvent(locationLockID));
		queueImpose(time, new RemoveEntityEvent(angleLockID));
		queueImpose(time, new SetShipPilotedEvent(shipID, false));
	}
	
	@Override
	public Function<ServerConnection, ClientControlHandler> getClientHandlerCreator() {
		return new ClientDrivingHandlerCreator(playerID, shipID, getOriginalFunctionHeader());
	}
	
}
