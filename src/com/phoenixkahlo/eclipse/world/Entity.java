package com.phoenixkahlo.eclipse.world;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Graphics;

import com.phoenixkahlo.eclipse.server.ClientConnection;
import com.phoenixkahlo.eclipse.server.ServerControlHandler;
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
	
	boolean isStandingOn(Vector2 position);
	
	/**
	 * Nullable.
	 */
	Consumer<Player> getUseable(Vector2 position);
	
	/**
	 * Nullable
	 */
	BiFunction<ClientConnection, Integer, ServerControlHandler> getHandler(Vector2 position);
	
	double getAlignmentAngle();
	
}
