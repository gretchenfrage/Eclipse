package com.phoenixkahlo.physics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.phoenixkahlo.utils.ArrayUtils;
import com.phoenixkahlo.utils.MathUtils;

public class Polygon {

	private Convex[] convexes;
	private Vector2f[] perimiter;
	private float area;
	private Vector2f translation;
	private float maxRadius;
	
	private Segment[] transformFaces;
	
	private Polygon(Vector2f[] perimiter, Convex... convexes) {
		this.convexes = convexes;
		this.perimiter = perimiter;
		
		cacheData();
	}
	
	public Polygon(Convex... convexes) {
		this.convexes = convexes;
		
		// Find perimiter
		List<Vector2f> perimiter = new ArrayList<Vector2f>();
		Vector2f start = furthestVertex(convexes);
		Vector2f last = start;
		Vector2f current = nextPerimiterVertex(start.direction(), start, convexes);
		perimiter.add(current);
		do {
			Vector2f next = nextPerimiterVertex(last, current, convexes);
			perimiter.add(next);
			last = current;
			current = next;
		} while (!current.equals(start));
		this.perimiter = perimiter.toArray(new Vector2f[perimiter.size()]);
		
		cacheData();
	}
	
	/**
	 * Traces in direction of positive theta.
	 */
	private static Vector2f nextPerimiterVertex(float targetTheta, Vector2f current, Convex[] convexes) {
		float minDifference = Float.MAX_VALUE;
		Vector2f bestVertex = null;
		for (Vector2f vertex : connectedVertices(current, convexes)) {
			float difference = MathUtils.positiveDifference(vertex.directionRelativeTo(current), targetTheta);
			if (difference < minDifference && difference > 0) {
				minDifference = difference;
				bestVertex = vertex;
			}
		}
		return bestVertex;
	}
	
	/**
	 * Traces in direction of positive theta.
	 */
	private static Vector2f nextPerimiterVertex(Vector2f last, Vector2f current, Convex[] convexes) {
		return nextPerimiterVertex(last.directionRelativeTo(current), current, convexes);
	}
	
	private static Set<Vector2f> connectedVertices(Vector2f start, Convex[] convexes) {
		Set<Vector2f> out = new HashSet<Vector2f>();
		for (Convex convex : convexes) {
			Vector2f[] vertices = convex.getVertices();
			for (int i = 0; i < vertices.length; i++) {
				if (vertices[i].equals(start)) {
					out.add(vertices[(i + 1) % vertices.length]);
					out.add(vertices[i == 0 ? vertices.length - 1 : i - 1]);
				}
			}
		}
		return out;
	}
	
	private static Vector2f furthestVertex(Convex[] convexes) {
		Vector2f furthest = null;
		float maxMagnitude = -Float.MAX_VALUE;
		for (Convex convex : convexes) {
			for (Vector2f vertex : convex.getVertices()) {
				float magnitude = vertex.magnitude();
				if (magnitude >= maxMagnitude) {
					furthest = vertex;
					maxMagnitude = magnitude;
				}
			}
		}
		return furthest;
	}
	
	private void cacheData() {
		// Area
		area = 0;
		for (Convex convex : convexes)
			area += convex.area();
		
		// Max radius
		maxRadius = MathUtils.max(perimiter, Vector2f::magnitude);		
	}
	
	public Polygon(Vector2f[] convexVertices) {
		this(convexVertices, new Convex(convexVertices));
	}
	
	/**
	 * Please don't modify. Untransformed.
	 */
	public Vector2f[] getPerimiter() {
		return perimiter;
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
		this.translation = translation;
		
		for (Convex convex : convexes)
			convex.cacheTransform(translation, rotation);
		Vector2f[] transformVertices = new Vector2f[perimiter.length];
		for (int i = 0; i < transformVertices.length; i++) {
			transformVertices[i] = perimiter[i].copy().rotate(rotation).add(translation);
		}
		transformFaces = new Segment[perimiter.length];
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
		// Return null if no possibility of intersection
		if (translation.distance(other.translation) > maxRadius + other.maxRadius)
			return null;
		
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
	
	/**
	 * Translation cache dependent
	 */
	public Vector2f closestPerimiterPointTo(Vector2f point) {
		return ArrayUtils.minProperty(ArrayUtils.map(transformFaces, 
				segment -> segment.closestPointTo(point), Vector2f.class), 
				item -> (double) item.distance(point));
	}
	
}
