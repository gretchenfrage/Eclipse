package com.phoenixkahlo.eclipse.world;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * A viewing perspective that transforms the graphics context.
 */
public interface Perspective {

	void transform(Graphics g, GameContainer container);
	
}
