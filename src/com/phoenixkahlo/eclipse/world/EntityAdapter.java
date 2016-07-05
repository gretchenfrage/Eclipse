package com.phoenixkahlo.eclipse.world;

import java.util.Random;
import java.util.function.Consumer;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Graphics;

import com.phoenixkahlo.eclipse.world.impl.Player;

/**
 * Uninteresting implementation of Entity.
 */
public abstract class EntityAdapter implements Entity {

	protected static final Random RANDOM = new Random();
	
	private int id;
	
	public EntityAdapter() {
		id = RANDOM.nextInt(Integer.MAX_VALUE);
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
	public RenderLayer getRenderLayer() {
		return null;
	}
	
	@Override
	public Consumer<Player> getUseable(Vector2 position) {
		return null;
	}
	
	@Override
	public boolean isStandingOn(Vector2 position) {
		return false;
	}
	
}
