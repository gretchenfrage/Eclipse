package com.phoenixkahlo.eclipse.world;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * A viewing perspective that transforms the graphics context.
 */
public interface Perspective {

	void transform(Graphics g, GameContainer container);
	
	double getMinX(GameContainer container);
	
	double getMinY(GameContainer container);
	
	double getMaxX(GameContainer container);
	
	double getMaxY(GameContainer container);
	
}
