package com.phoenixkahlo.eclipse.gamestate;

import org.dyn4j.dynamics.Body;
import org.newdawn.slick.Graphics;

public interface Entity {

	/**
	 * Nullable
	 */
	Body toBody();
	
	void preTick();
	
	void postTick();
	
	void render(Graphics g);
	
}
