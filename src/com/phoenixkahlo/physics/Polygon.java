package com.phoenixkahlo.physics;

import java.util.ArrayList;
import java.util.List;

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
	
	/**
	 * Please don't modify.
	 */
	public Convex[] getConvexes() {
		return convexes;
	}
	
	public float area() {
		float sum = 0;
		for (Convex convex : convexes)
			sum += convex.area();
		return sum;
	}
	
	/**
	 * Nullable.
	 */
	public Polygon intersection(Polygon other) {
		List<Convex> list = new ArrayList<Convex>();
		for (Convex convex1 : convexes) {
			for (Convex convex2 : other.getConvexes()) {
				Convex intersection = convex1.intersection(convex2);
				if (intersection != null)
					list.add(intersection);
			}
		}
		if (list.isEmpty())
			return null;
		else
			return new Polygon(list.toArray(new Convex[list.size()]));
	}
	
}
