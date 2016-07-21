package com.phoenixkahlo.eclipse.world;

import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class BasicPerspective implements Perspective {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(BasicPerspective.class, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(BasicPerspective.class, BasicPerspective::new, subDecoder);
	}
	
	private float x;
	private float y;
	private float scale;
	private float rotation;
	private float scaleMin = Float.NaN;
	private float scaleMax = Float.NaN;
	
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
		// Translate the world origin to the screen center
		g.translate(container.getWidth() / 2, container.getHeight() / 2);
		// Scale the world around the screen center / it's origin
		g.scale(scale, scale);
		// Translate the world's position coords to the screen center
		g.translate(-x, -y);
		// Rotate the world around the perspective
		g.rotate(x, y, (float) -Math.toDegrees(rotation));
	}
	
	@Override
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
		out.rotate(rotation, x, y);
		
		return out;
	}
	
	@Override
	public Vector2 worldToScreen(Vector2 worldPoint, Vector2 containerSize) {
		Vector2 out = worldPoint.copy();
		
		// Translate the world origin to the screen center
		out.add(containerSize.copy().multiply(0.5));
		// Scale the world around the screen center / it's origin
		worldPoint.multiply(scale);
		// Translate the world's position coords to the screen center
		worldPoint.subtract(new Vector2(x, y));
		// Rotate the world around the perspective
		worldPoint.rotate(-rotation, new Vector2(x, y));
		
		return out;
	}
	
	public void addRotation(double radians) {
		rotation += radians;
		rotation %= Math.PI * 2;
	}
	
	public void raiseScale(double factor) {
		scale = (float) Math.pow(scale, factor);
		if (scale > scaleMax)
			scale = scaleMax;
		else if (scale < scaleMin)
			scale = scaleMin;
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
	
	public float getScaleMin() {
		return scaleMin;
	}

	public void setScaleMin(float scaleMin) {
		this.scaleMin = scaleMin;
	}

	public float getScaleMax() {
		return scaleMax;
	}

	public void setScaleMax(float scaleMax) {
		this.scaleMax = scaleMax;
	}
	
}
