package com.phoenixkahlo.physics;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

import com.phoenixkahlo.utils.MathUtils;

public class Convex {

	private Vector2f[] vertices;
	private Shape slickShape;
	private float area;
	private float maxRadius;
	private Segment[] segmentsCache;
	
	public Convex(Vector2f... vertices) {
		this.vertices = vertices;
		
		// Cache slick shape
		float[] fvertices = new float[vertices.length * 2];
		for (int i = 0; i < vertices.length; i++) {
			fvertices[i * 2] = vertices[i].x;
			fvertices[i * 2 + 1] = vertices[i].y;
		}
		slickShape = new Polygon(fvertices);
		
		// Cache area
		area = 0;
		for (int i = 0; i < vertices.length; i++) {
			area += vertices[i].x * vertices[(i + 1) % vertices.length].y - 
					vertices[i].y * vertices[(i + 1) % vertices.length].x;
		}
		area /= 2;
		
		// Cache max radius
		maxRadius = MathUtils.max(vertices, Vector2f::magnitude);
	}
	
	/**
	 * Please don't modify.
	 */
	public Vector2f[] getVertices() {
		return vertices;
	}
	
	public float area() {
		return area;
	}
	
	public boolean couldIntersect(Convex other, Vector2f otherTranslate) {
		return otherTranslate.magnitude() < maxRadius + other.maxRadius;
	}
	
	public Shape toSlickShape() {
		return slickShape;
	}
	
	public void cacheSegments(Vector2f translation, float rotation) {
		Vector2f[] transformed = new Vector2f[vertices.length];
		for (int i = 0; i < transformed.length; i++) {
			transformed[i] = vertices[i].copy().rotate(rotation).add(translation);
		}
		segmentsCache = new Segment[vertices.length];
		for (int i = 0; i < segmentsCache.length; i++) {
			segmentsCache[i] = new Segment(transformed[i], transformed[(i + 1) % transformed.length]);
		}
	}
	
	public void invalidateSegmentsCache() {
		segmentsCache = null;
	}
	
	public Segment[] getSegmentsCache() {
		return segmentsCache;
	}
	
	/**
	 * Depends on segment cache.
	 */
	public boolean contains(Vector2f point) {
		int intersections = 0;
		for (Segment segment : segmentsCache) {
			if (segment.isVertical()) {
				if (point.y > segment.getMin() && point.y < segment.getMax())
					intersections++;
			} else {
				float x = segment.xAt(point.y);
				if (x > segment.getMin() && x < segment.getMax())
					intersections++;
			}
		}
		return intersections % 2 == 1;
	}
	
	/**
	 * Depends of segment cache.
	 * Nullable.
	 */
	public Polygon intersection(Convex other) {
		// Find a vertex of other that is contained within this
		int vertexID = 0;
		while (!contains(other.getVertices()[vertexID]) && vertexID < other.getVertices().length)
			vertexID++;
		if (vertexID >= other.getVertices().length)
			// No intersection case
			return null;
		
		Convex tracing = other; // The convex we're currently tracing
		Convex notTracing = this; // The convex we're currently not tracing
		int startID = vertexID; // The vertex index of other that we started with
		List<Vector2f> intersectionVertices = new ArrayList<Vector2f>(); // Vertices of intersection polygon
		
		do {
			Segment traceSegment = tracing.getSegmentsCache()[vertexID];
			for (int i = 0; i < notTracing.getSegmentsCache().length; i++) {
				
			}
		} while (!(vertexID == startID) && tracing == other);
	}
	
}