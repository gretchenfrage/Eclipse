package com.phoenixkahlo.physics;

import com.phoenixkahlo.utils.MathUtils;

public class Vector2f {
	
	public float x;
	public float y;
	
	public Vector2f() {}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f copy() {
		return new Vector2f(x, y);
	}
	
	public Vector2f add(Vector2f other) {
		x += other.x;
		y += other.y;
		return this;
	}
	
	public Vector2f subtract(Vector2f other) {
		x -= other.x;
		y -= other.y;
		return this;
	}
	
	public Vector2f multiply(float scalar) {
		x *= scalar;
		y *= scalar;
		return this;
	}
	
	public Vector2f divide(float scalar) {
		x /= scalar;
		y /= scalar;
		return this;
	}
	
	public Vector2f rotate(float theta) {
		float direction = direction();
		float magnitude = magnitude();
		direction += theta;
		x = (float) Math.cos(direction) * magnitude;
		y = (float) Math.sin(direction) * magnitude;
		return this;
	}
	
	public Vector2f rotate(float theta, Vector2f around) {
		subtract(around);
		rotate(theta);
		add(around);
		return this;
	}
	
	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public float direction() {
		if (x == 0 && y == 0)
			return Float.NaN;
		else
			return (float) Math.atan2(y, x);
	}
	
	public float distance(Vector2f other) {
		return (float) Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
	}
	
	public float shortestAngle(Vector2f other) {
		float angle = direction();
		float otherAngle = other.direction();
		return (float) MathUtils.min(
				Math.abs(angle - otherAngle), 
				Math.abs((angle + 2 * Math.PI) - otherAngle), 
				Math.abs((angle - 2 * Math.PI) - otherAngle)
				);
	}
	
}
