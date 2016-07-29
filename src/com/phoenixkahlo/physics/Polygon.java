package com.phoenixkahlo.physics;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

	private Convex[] convexes;
	private Vector2f[] perimiter;
	private float area;
	
	private Segment[] transformFaces;
	
	public Polygon(Convex... convexes) {
		this.convexes = convexes;
		this.perimiter = perimiter;
		
		// Area
		area = 0;
		for (Convex convex : convexes)
			area += convex.area();
	}
	
	private boolean isOnPerimiter(Vector2f vertex) {
		
	}
	
	public Polygon(Vector2f[] convexVertices) {
		this(convexVertices, new Convex(convexVertices));
	}
	
	/**
	 * Please don't modify.
	 */
	public Convex[] getConvexes() {
		return convexes;
	}
	
	public float area() {
		return area;
	}
	
	/**
	 * Caches a transformed version of all vertices and segments which will be used to 
	 * increase performance for some calculations. 
	 */
	public void cacheTransform(Vector2f translation, float rotation) {
		for (Convex convex : convexes)
			convex.cacheTransform(translation, rotation);
		Vector2f[] transformVertices = new Vector2f[vertices.length];
		for (int i = 0; i < transformVertices.length; i++) {
			transformVertices[i] = vertices[i].copy().rotate(rotation).add(translation);
		}
		transformFaces = new Segment[vertices.length];
		for (int i = 0; i < transformFaces.length; i++) {
			transformFaces[i] = new Segment(transformVertices[i], 
					transformVertices[(i + 1) % transformVertices.length]);
		}
	}
	
	/**
	 * Uncaches the transformed version of vertices and segments, causing subsequent calls 
	 * to methods which depend of that cache to throw IllegalStateExceptions until a 
	 * further call to cacheTransform. 
	 */
	public void invalidateTransform() {
		for (Convex convex : convexes)
			convex.invalidateTransform();
		transformFaces = null;
	}
	
	/**
	 * Translation cache dependent. Nullable.
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
