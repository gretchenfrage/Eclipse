package com.phoenixkahlo.eclipse.world;

import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.phoenixkahlo.utils.MathUtils;

/**
 * A viewing perspective that transforms the graphics context.
 */
public interface Perspective {

	void transform(Graphics g, GameContainer container);
	
	Vector2 screenToWorld(Vector2 screenCoords, Vector2 containerSize);
	
	Vector2 worldToScreen(Vector2 worldCoords, Vector2 containerSize);
	
	default double getMinX(Vector2 containerSize) {
		return MathUtils.min(
				screenToWorld(new Vector2(0, 0), containerSize).x,
				screenToWorld(containerSize, containerSize).x,
				screenToWorld(new Vector2(containerSize.x, 0), containerSize).x,
				screenToWorld(new Vector2(0, containerSize.y), containerSize).x
				);
	}

	default double getMinY(Vector2 containerSize) {
		return MathUtils.min(
				screenToWorld(new Vector2(0, 0), containerSize).y,
				screenToWorld(containerSize, containerSize).y,
				screenToWorld(new Vector2(containerSize.x, 0), containerSize).y,
				screenToWorld(new Vector2(0, containerSize.y), containerSize).y
				);
	}

	default double getMaxX(Vector2 containerSize) {
		return MathUtils.max(
				screenToWorld(new Vector2(0, 0), containerSize).x,
				screenToWorld(containerSize, containerSize).x,
				screenToWorld(new Vector2(containerSize.x, 0), containerSize).x,
				screenToWorld(new Vector2(0, containerSize.y), containerSize).x
				);
	}

	default double getMaxY(Vector2 containerSize) {
		return MathUtils.max(
				screenToWorld(new Vector2(0, 0), containerSize).y,
				screenToWorld(containerSize, containerSize).y,
				screenToWorld(new Vector2(containerSize.x, 0), containerSize).y,
				screenToWorld(new Vector2(0, containerSize.y), containerSize).y
				);
	}
	
}
