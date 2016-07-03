package com.phoenixkahlo.eclipse.world;

import java.util.function.Consumer;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Graphics;

import com.phoenixkahlo.eclipse.world.impl.Player;

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
	 * @return a hopefully unique (RNG is fine), non-negative integer.
	 */
	int getID();

	/**
	 * Nullable if entity doesn't have perspective.
	 */
	Perspective getPerspective();
	
	boolean isStandingOn(Vector2 position);
	
	/**
	 * Nullable if not useable at that position.
	 */
	Consumer<Player> getUseable(Vector2 position);
	
}
