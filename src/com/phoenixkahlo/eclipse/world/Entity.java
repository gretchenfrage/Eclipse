package com.phoenixkahlo.eclipse.world;

import org.dyn4j.dynamics.Body;
import org.newdawn.slick.Graphics;

public interface Entity {

	/**
	 * Nullable if entity doesn't have body.
	 */
	Body getBody();
	
	void preTick();
	
	void postTick();
	
	void render(Graphics g);
	
	/**
	 * Nullable if entity isn't rendered.
	 */
	RenderLayer getRenderLayer();
	
	int getID();
	
}
