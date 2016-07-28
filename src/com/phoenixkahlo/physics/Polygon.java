package com.phoenixkahlo.physics;

public class Polygon {

	private Convex[] convexes;
	
	public Polygon(Convex... convexes) {
		this.convexes = convexes;
	}
	
	/**
	 * @param vertices the vertices for a single convex.
	 */
	public Polygon(Vector2f... vertices) {
		this(new Convex(vertices));
	}
	
	public Convex[] getConvexes() {
		return convexes;
	}
	
	public float area() {
		float sum = 0;
		for (Convex convex : convexes)
			sum += convex.area();
		return sum;
	}
	
}
