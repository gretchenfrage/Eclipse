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
		g.translate(-x, -y);
		g.scale(scale, scale);
		g.rotate(x, y, rotation);
	}

	private Vector2 getMin(GameContainer container) {
		Vector2 min = new Vector2(-container.getWidth() / 2, -container.getHeight() / 2);
		min.multiply(1 / scale);
		min.rotate(rotation);
		min.add(x, y);
		return min;
	}
	
	private Vector2 getMax(GameContainer container) {
		Vector2 max = new Vector2(container.getWidth() / 2, container.getHeight() / 2);
		max.multiply(1 / scale);
		max.rotate(rotation);
		max.add(x, y);
		return max;
	}
	
	@Override
	public double getMinX(GameContainer container) {
		return getMin(container).x;
	}

	@Override
	public double getMinY(GameContainer container) {
		return getMin(container).y;
	}

	@Override
	public double getMaxX(GameContainer container) {
		return getMax(container).x;
	}

	@Override
	public double getMaxY(GameContainer container) {
		return getMax(container).y;
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
