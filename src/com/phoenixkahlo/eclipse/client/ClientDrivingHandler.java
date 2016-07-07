package com.phoenixkahlo.eclipse.client;

import java.io.IOException;

import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import com.phoenixkahlo.eclipse.world.BasicPerspective;
import com.phoenixkahlo.eclipse.world.Perspective;
import com.phoenixkahlo.eclipse.world.Ship;
import com.phoenixkahlo.eclipse.world.WorldStateContinuum;

public class ClientDrivingHandler extends BasicClientControlHandler {

	private ServerConnection connection;
	private BasicPerspective perspective;
	private int playerID;
	private int shipID;
	
	// Just holds the direction of these, the ship determines the amount of actual thrust
	private Vector2 cachedLinearThrust = new Vector2();
	private byte cachedAngularThrust = 0;
	
	public ClientDrivingHandler(ServerConnection connection, int functionHeader, int playerID, int shipID) {
		super(connection.getBroadcaster(), functionHeader);
		
		registerBroadcastToken("setLinearThrust");
		registerBroadcastToken("setAngularThrust");
		
		this.connection = connection;
		this.playerID = playerID;
		this.shipID = shipID;
		
		perspective = new BasicPerspective();
		perspective.setScale(10);
		perspective.setScaleMin(4);
		perspective.setScaleMax(20);
	}
	
	@Override
	public void update(Input input, WorldStateContinuum continuum, GameContainer container) {
		Ship ship = (Ship) continuum.getState().getEntity(shipID);
		int time = continuum.getTime();
		
		// Broadcasting stuff
		// Linear thrust
		Vector2 linearThrust = new Vector2();
		if (input.isKeyDown(Input.KEY_W))
			linearThrust.y--;
		if (input.isKeyDown(Input.KEY_S))
			linearThrust.y++;
		if (input.isKeyDown(Input.KEY_A))
			linearThrust.x--;
		if (input.isKeyDown(Input.KEY_D))
			linearThrust.x++;
		if (!linearThrust.equals(cachedLinearThrust)) {
			try {
				broadcastSetLinearThrust(time, linearThrust);
			} catch (IOException e) {
				connection.disconnect(e);
				return;
			}
			cachedLinearThrust = linearThrust;
		}
		// Angular thrust
		byte angularThrust = 0;
		if (input.isKeyDown(Input.KEY_E))
			angularThrust++;
		if (input.isKeyDown(Input.KEY_Q))
			angularThrust--;
		if (angularThrust != cachedAngularThrust) {
			try {
				broadcastSetAngularThrust(time, angularThrust);
			} catch (IOException e) {
				connection.disconnect(e);
				return;
			}
			cachedAngularThrust = angularThrust;
		}
		
		// Perspective
		// Rotation
		perspective.setRotation((float) ship.getBody().getTransform().getRotation());
		// Location
		Vector2 shipPos = ship.getBody().getWorldCenter();
		perspective.setX((float) shipPos.x);
		perspective.setY((float) shipPos.y);
	}
	
	private void broadcastSetLinearThrust(int time, Vector2 linearThrust) throws IOException {
		broadcast("setLinearThrust", time, linearThrust);
	}
	
	private void broadcastSetAngularThrust(int time, byte angularThrust) throws IOException {
		broadcast("setAngularThrust", time, angularThrust);
	}
	
	@Override
	public Perspective getPerspective() {
		return perspective;
	}

}
