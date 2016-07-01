package com.phoenixkahlo.eclipse.world;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Graphics;

/**
 * Uninteresting implementation of Entity.
 */
public abstract class EntityAdapter implements Entity {

	@Override
	public Body getBody() {
		return null;
	}

	@Override
	public void preTick(WorldState state) {}

	@Override
	public void postTick(WorldState state) {}

	@Override
	public void render(Graphics g) {}

	@Override
	public RenderLayer getRenderLayer() {
		return null;
	}
	
	@Override
	public Perspective getPerspective() {
		return null;
	}
	
	@Override
	public boolean isStandingOn(Vector2 position) {
		return false;
	}
	
}
