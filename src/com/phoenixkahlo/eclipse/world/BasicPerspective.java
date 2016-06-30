package com.phoenixkahlo.eclipse.world;

import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class BasicPerspective implements Perspective {

	private float x;
	private float y;
	private float scale;
	private float rotation;
	
	public BasicPerspective(float x, float y, float scale, float rotation) {
		this.x = x;
		this.y = y;
		this.scale = scale;
		this.rotation = rotation;
	}
	
	public BasicPerspective() {
		this(0, 0, 1, 0);
	}
	
	@Override
	public void transform(Graphics g, GameContainer container) {
		g.translate(container.getWidth() / 2, container.getHeight() / 2);
		g.scale(scale, scale);
		g.translate(-x, -y);
		g.rotate(x, y, (float) -Math.toDegrees(rotation));
	}
	
	public Vector2 screenToWorld(Vector2 screenPoint, Vector2 containerSize) {
		Vector2 out = screenPoint.copy();
		
		// Scale the screen around its center
		Vector2 screenCenter = containerSize.copy().multiply(0.5);
		//out.subtract(screenCenter);
		out.multiply(1 / scale);
		//out.add(screenCenter);
		// Move the origin of the screen to the point of the perspective
		out.add(x, y);
		// Move the center of the screen to the point of the perspective
		out.subtract(screenCenter.multiply(1 / scale));
		// Rotate the screen around the perspective
		out.rotate(rotation);
		
		return out;
	}
	
	private Vector2 getMinCorner(Vector2 containerSize) {
		return screenToWorld(new Vector2(0, 0), containerSize);
	}
	
	private Vector2 getMaxCorner(Vector2 containerSize) {
		return screenToWorld(containerSize, containerSize);
	}
	
	@Override
	public double getMinX(Vector2 containerSize) {
		Vector2 c1 = getMinCorner(containerSize);
		Vector2 c2 = getMaxCorner(containerSize);
		return c1.x < c2.x ? c1.x : c2.x;
	}

	@Override
	public double getMinY(Vector2 containerSize) {
		Vector2 c1 = getMinCorner(containerSize);
		Vector2 c2 = getMaxCorner(containerSize);
		return c1.y < c2.y ? c1.y : c2.y;
	}

	@Override
	public double getMaxX(Vector2 containerSize) {
		Vector2 c1 = getMinCorner(containerSize);
		Vector2 c2 = getMaxCorner(containerSize);
		return c1.x > c2.x ? c1.x : c2.x;
	}

	@Override
	public double getMaxY(Vector2 containerSize) {
		Vector2 c1 = getMinCorner(containerSize);
		Vector2 c2 = getMaxCorner(containerSize);
		return c1.y > c2.y ? c1.y : c2.y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

}
