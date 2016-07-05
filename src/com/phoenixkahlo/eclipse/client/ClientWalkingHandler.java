package com.phoenixkahlo.eclipse.client;

import java.io.IOException;

import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Input;

import com.phoenixkahlo.eclipse.world.BasicPerspective;
import com.phoenixkahlo.eclipse.world.Perspective;
import com.phoenixkahlo.eclipse.world.WorldState;

/**
 * The main client control handler for walking, running, thrusting, shooting, 
 * clicking, etc.
 */
public class ClientWalkingHandler extends NetworkedClientControlHandler {

	private static final double RADIANS_ROTATE_PER_TICK = 0.1;
	private static final double SCALE_FACTOR_PER_TICK = 0.01;
	
	private ServerConnection connection;
	private BasicPerspective perspective;
	private int entityID;
	
	public ClientWalkingHandler(ServerConnection connection, int functionHeader, int entityID) {
		super(connection.getBroadcaster(), functionHeader);
		
		registerBroadcastToken("broadcastG");
		
		this.connection = connection;
		this.entityID = entityID;
		
		perspective = new BasicPerspective();
		
		perspective.setScale(25);
		perspective.setScaleMin(10);
		perspective.setScaleMax(50);
	}
	
	@Override
	public Perspective getPerspective() {
		return perspective;
	}

	@Override
	public void update(Input input, WorldState worldState) {
		Vector2 pos = worldState.getEntity(entityID).getBody().getWorldCenter();
		perspective.setX((float) pos.x);
		perspective.setY((float) pos.y);
		
		if (input.isKeyPressed(Input.KEY_G))
			try {
				broadcastG();
			} catch (IOException e) {
				connection.disconnect(e);
			}
	}
	
	private void broadcastG() throws IOException {
		System.out.println("broadcasting G");
		broadcast("broadcastG");
	}

}
