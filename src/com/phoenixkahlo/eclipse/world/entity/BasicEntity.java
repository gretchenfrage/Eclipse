package com.phoenixkahlo.eclipse.world.entity;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Graphics;

import com.phoenixkahlo.eclipse.server.ClientConnection;
import com.phoenixkahlo.eclipse.server.ServerControlHandler;
import com.phoenixkahlo.eclipse.world.DamageType;
import com.phoenixkahlo.eclipse.world.RenderLayer;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.utils.MathUtils;

/**
 * Uninteresting implementation of Entity.
 */
public abstract class BasicEntity implements Entity {
	
	private int id;
	
	public BasicEntity() {
		id = MathUtils.RANDOM.nextInt(Integer.MAX_VALUE);
	}
	
	@Override
	public int getID() {
		return id;
	}
	
	@Override
	public void preTick(WorldState state) {}
	
	@Override
	public void postTick(WorldState state) {}
	
	@Override
	public void render(Graphics g) {}
	
	@Override
	public Body getBody() {
		return null;
	}
	
	@Override
	public void hurt(double points, DamageType type) {}

	@Override
	public void heal(double points) {}
	
	@Override
	public RenderLayer getRenderLayer() {
		return null;
	}
	
	@Override
	public Consumer<Player> getUseable(Vector2 position) {
		return null;
	}
	
	@Override
	public BiFunction<ClientConnection, Integer, ServerControlHandler> getHandler(Vector2 position) {
		return null;
	}
	
	@Override
	public boolean isStandingOn(Vector2 position) {
		return false;
	}
	
	@Override
	public double getAlignmentAngle() {
		return 0;
	}
	
}
