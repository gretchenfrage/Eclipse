package com.phoenixkahlo.eclipse.world;

import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * A viewing perspective that transforms the graphics context.
 */
public interface Perspective {

	void transform(Graphics g, GameContainer container);
	
	double getMinX(Vector2 containerSize);
	
	double getMinY(Vector2 containerSize);
	
	double getMaxX(Vector2 containerSize);
	
	double getMaxY(Vector2 containerSize);
	
}
