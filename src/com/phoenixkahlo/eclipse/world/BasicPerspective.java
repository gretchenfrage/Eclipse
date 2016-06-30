package com.phoenixkahlo.eclipse.world;

import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class BasicPerspective implements Perspective {

	private float x;
	private float y;
	private float scale;
	private float rotation;
	private float suggestibleScaleMin = Float.NaN; // May be NaN to represent lack thereof
	private float suggestibleScaleMax = Float.NaN; // May be NaN to represent lack thereof
	
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
		Vector2 screenCenter = containerSize.copy().multiply(0.5);
		Vector2 out = screenPoint.copy();
		
		// Scale the screen to its origin
		out.multiply(1 / scale);
		// Move the origin of the screen to the point of the perspective
		out.add(x, y);
		// Move the center of the screen to the point of the perspective
		out.subtract(screenCenter.multiply(1 / scale));
		// Rotate the screen around the perspective
		out.rotate(-rotation, x, y);
		
		return out;
	}
	
	@Override
	public double getMinX(Vector2 containerSize) {
		return min(new double[] {
				screenToWorld(new Vector2(0, 0), containerSize).x,
				screenToWorld(containerSize, containerSize).x,
				screenToWorld(new Vector2(containerSize.x, 0), containerSize).x,
				screenToWorld(new Vector2(0, containerSize.y), containerSize).x
				});
	}

	@Override
	public double getMinY(Vector2 containerSize) {
		return min(new double[] {
				screenToWorld(new Vector2(0, 0), containerSize).y,
				screenToWorld(containerSize, containerSize).y,
				screenToWorld(new Vector2(containerSize.x, 0), containerSize).y,
				screenToWorld(new Vector2(0, containerSize.y), containerSize).y
				});
	}

	@Override
	public double getMaxX(Vector2 containerSize) {
		return max(new double[] {
				screenToWorld(new Vector2(0, 0), containerSize).x,
				screenToWorld(containerSize, containerSize).x,
				screenToWorld(new Vector2(containerSize.x, 0), containerSize).x,
				screenToWorld(new Vector2(0, containerSize.y), containerSize).x
				});
	}

	@Override
	public double getMaxY(Vector2 containerSize) {
		return max(new double[] {
				screenToWorld(new Vector2(0, 0), containerSize).y,
				screenToWorld(containerSize, containerSize).y,
				screenToWorld(new Vector2(containerSize.x, 0), containerSize).y,
				screenToWorld(new Vector2(0, containerSize.y), containerSize).y
				});
	}
	
	@Override
	public void suggestAddRotation(double radians) {
		rotation += radians;
		rotation %= Math.PI * 2;
	}
	
	@Override
	public void suggestRaiseScale(double factor) {
		scale = (float) Math.pow(scale, factor);
		if (scale > suggestibleScaleMax)
			scale = suggestibleScaleMax;
		if (scale < suggestibleScaleMin)
			scale = suggestibleScaleMin;
	}

	@Override
	public double attemptGetRotation() {
		return rotation;
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
	
	public float getSuggestibleScaleMin() {
		return suggestibleScaleMin;
	}

	public void setSuggestibleScaleMin(float suggestibleScaleMin) {
		this.suggestibleScaleMin = suggestibleScaleMin;
	}

	public float getSuggestibleScaleMax() {
		return suggestibleScaleMax;
	}

	public void setSuggestibleScaleMax(float suggestibleScaleMax) {
		this.suggestibleScaleMax = suggestibleScaleMax;
	}

	private double max(double[] arr) {
		double out = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > out)
				out = arr[i];
		}
		return out;
	}

	private double min(double[] arr) {
		double out = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] < out)
				out = arr[i];
		}
		return out;
	}
	
}
