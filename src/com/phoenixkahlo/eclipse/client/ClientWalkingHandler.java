package com.phoenixkahlo.eclipse.client;

import java.io.IOException;

import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import com.phoenixkahlo.eclipse.world.BasicPerspective;
import com.phoenixkahlo.eclipse.world.Perspective;
import com.phoenixkahlo.eclipse.world.WorldStateContinuum;
import com.phoenixkahlo.eclipse.world.entity.Entity;
import com.phoenixkahlo.eclipse.world.entity.Player;

/**
 * The main client control handler for walking, running, thrusting, shooting, 
 * clicking, etc.
 */
public class ClientWalkingHandler extends BasicClientControlHandler {

	private static final double RADIANS_ROTATE_PER_TICK = 0.06;
	private static final double SCALE_FACTOR_PER_TICK = 0.01;
	
	private ServerConnection connection;
	private BasicPerspective perspective;
	private int entityID;
	
	private Vector2 cachedDirection = new Vector2();
	private boolean cachedSprinting = false;
	private float cachedAngle = 0;
	
	public ClientWalkingHandler(ServerConnection connection, int functionHeader, int entityID) {
		super(connection.getBroadcaster(), functionHeader);
		
		registerBroadcastToken("setDirection");
		registerBroadcastToken("setSprinting");
		registerBroadcastToken("setAngle");
		registerBroadcastToken("use");
		registerBroadcastToken("useWeapon");
		
		registerBroadcastToken("printState");
		
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
	public void update(Input input, WorldStateContinuum continuum, GameContainer container) {
		int time = continuum.getTime();
		Player entity = (Player) continuum.getState().getEntity(entityID);
		Vector2 containerSize = new Vector2(container.getWidth(), container.getHeight());
		
		// Broadcasting stuff
		// Moving direction
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
				broadcastSetDirection(time, direction);
				//connection.imposeEvent(time, new SetWalkingEntityDirectionEvent(entityID, direction));
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
				broadcastSetSprinting(time, sprinting);
				//connection.imposeEvent(time, new SetWalkingEntitySprintingEvent(entityID, sprinting));
				cachedSprinting = sprinting;
			} catch (IOException e) {
				connection.disconnect(e);
				return;
			}
		}
		// Facing direction
		if (entity != null) {
			Vector2 p1 = entity.getBody().getWorldCenter();
			Vector2 p2 = perspective.screenToWorld(
					new Vector2(input.getMouseX(), input.getMouseY()),
					new Vector2(container.getWidth(), container.getHeight()));
			float angle = (float) Math.atan2(p2.y - p1.y, p2.x - p1.x);
			if (angle != cachedAngle) {
				try {
					broadcastSetAngle(time, angle);
					//connection.imposeEvent(time, new SetPlayerFacingAngleEvent(entityID, angle));
					cachedAngle = angle;
				} catch (IOException e) {
					connection.disconnect(e);
					return;
				}
			}
		}
		// Activation
		if (entity != null && input.isKeyPressed(Input.KEY_SPACE)) {
			try {
				broadcastUse(time);
			} catch (IOException e) {
				connection.disconnect(e);
				return;
			}
		}
		// Weapon
		if (entity != null && input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			Vector2 target = new Vector2(input.getMouseX(), input.getMouseY());
			target = perspective.screenToWorld(target, containerSize);
			try {
				broadcastUseWeapon(time, target);
			} catch (IOException e) {
				connection.disconnect(e);
				return;
			}
		}
		
		if (input.isKeyPressed(Input.KEY_P))
			try {
				broadcast("printState");
				System.out.println(connection.getContinuum().getState());
			} catch (IOException e) {
				e.printStackTrace();
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
		// Location
		if (entity != null) {
			Vector2 pos = entity.getBody().getWorldCenter();
			perspective.setX((float) pos.x);
			perspective.setY((float) pos.y);
		}
		// Platform rotation
		if (entity != null)
			perspective.addRotation(entity.getLatestPlatformRotationChange());
		// Alignment
		if (entity != null && input.isKeyDown(Input.KEY_G)) {
			Entity platform = entity.platformOn(continuum.getState());
			if (platform != null)
				perspective.setRotation((float) platform.getAlignmentAngle());
		}
	}
	
	private void broadcastSetDirection(int time, Vector2 direction) throws IOException {
		broadcast("setDirection", time, direction);
	}
	
	private void broadcastSetSprinting(int time, boolean sprinting) throws IOException {
		broadcast("setSprinting", time, sprinting);
	}
	
	private void broadcastSetAngle(int time, float angle) throws IOException {
		broadcast("setAngle", time, angle);
	}
	
	private void broadcastUse(int time) throws IOException {
		broadcast("use", time);
	}
	
	private void broadcastUseWeapon(int time, Vector2 target) throws IOException {
		broadcast("useWeapon", time, target);
	}
	
}
