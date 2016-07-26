package com.phoenixkahlo.physics;

public class Convex {

	private Vector2f[] vertices;
	
	public Convex(Vector2f... vertices) {
		this.vertices = vertices;
	}
	
	/**
	 * Please don't modify.
	 */
	public Vector2f[] getVertices() {
		return vertices;
	}
	
	public float area() {
		throw new RuntimeException("unimplemented"); //TODO
	}
	
}
