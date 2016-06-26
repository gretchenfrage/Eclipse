package com.phoenixkahlo.eclipse.world;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public interface Background {

	/**
	 * @param perspective nullable
	 */
	void render(Graphics g, GameContainer container, Perspective perspective);
	
}
