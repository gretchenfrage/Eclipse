package com.phoenixkahlo.eclipse.world;

import org.dyn4j.dynamics.Body;
import org.newdawn.slick.Graphics;
import org.dyn4j.geometry.Vector2;

/**
 * It goes within the world.
 */
public interface Entity {

	/**
	 * Nullable if entity doesn't have body.
	 */
	Body getBody();
	
	void preTick(WorldState state);
	
	void postTick(WorldState state);
	
	void render(Graphics g);
	
	/**
	 * Nullable if entity isn't rendered.
	 */
	RenderLayer getRenderLayer();
	
	/**
	 * @return a hopefully unique, non-negative integer.
	 */
	int getID();

	/**
	 * Nullable if entity doesn't have perspective.
	 */
	Perspective getPerspective();
	
	boolean isStandingOn(Vector2 position);
	
}
