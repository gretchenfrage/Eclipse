package com.phoenixkahlo.eclipse.client;

import java.io.IOException;

import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Input;

import com.phoenixkahlo.eclipse.world.BasicPerspective;
import com.phoenixkahlo.eclipse.world.Entity;
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
	
	private Vector2 cachedDirection = new Vector2();
	private boolean cachedSprinting = false;
	
	public ClientWalkingHandler(ServerConnection connection, int functionHeader, int entityID) {
		super(connection.getBroadcaster(), functionHeader);
		
		registerBroadcastToken("setDirection");
		registerBroadcastToken("setSprinting");
		
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
		Entity entity = worldState.getEntity(entityID);
		if (entity != null) {
			Vector2 pos = entity.getBody().getWorldCenter();
			perspective.setX((float) pos.x);
			perspective.setY((float) pos.y);
		}
		
		// Broadcasting stuff
		// Direction
		Vector2 direction = new Vector2();
		if (input.isKeyDown(Input.KEY_W))
			direction.y--;
		if (input.isKeyDown(Input.KEY_S))
			direction.y++;
		if (input.isKeyDown(Input.KEY_A))
			direction.x--;
		if (input.isKeyDown(Input.KEY_D))
			direction.x++;
		direction.rotate(perspective.getRotation());
		if (!direction.equals(cachedDirection)) {
			try {
				broadcastSetDirection(direction);
				cachedDirection = direction;
			} catch (IOException e) {
				connection.disconnect(e);
				return;
			}
		}
		// Sprinting
		boolean sprinting = input.isKeyDown(Input.KEY_LSHIFT);
		if (sprinting != cachedSprinting) {
			try {
				broadcastSetSprinting(sprinting);
				cachedSprinting = sprinting;
			} catch (IOException e) {
				connection.disconnect(e);
				return;
			}
		}
		
		// Perspective
		// Rotation
		if (input.isKeyDown(Input.KEY_E))
			perspective.addRotation(RADIANS_ROTATE_PER_TICK);
		if (input.isKeyDown(Input.KEY_Q))
			perspective.addRotation(-RADIANS_ROTATE_PER_TICK);
		// Scale
		if (input.isKeyDown(Input.KEY_R))
			perspective.raiseScale(1 + SCALE_FACTOR_PER_TICK);
		if (input.isKeyDown(Input.KEY_F))
			perspective.raiseScale(1 - SCALE_FACTOR_PER_TICK);
	}
	
	private void broadcastSetDirection(Vector2 direction) throws IOException {
		broadcast("setDirection", direction);
	}
	
	private void broadcastSetSprinting(boolean sprinting) throws IOException {
		broadcast("setSprinting", sprinting);
	}
	
}
