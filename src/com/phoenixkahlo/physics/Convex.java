package com.phoenixkahlo.physics;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

import com.phoenixkahlo.utils.LambdaUtils;

public class Convex {

	private Vector2f[] vertices;
	private Shape slickShape;
	private float area;
	
	private Vector2f[] translateVertices;
	private Segment[] translateFaces;
	
	public Convex(Vector2f... vertices) {
		this.vertices = vertices;
		
		// Slick shape
		float[] fvertices = new float[vertices.length * 2];
		for (int i = 0; i < vertices.length; i++) {
			fvertices[i * 2] = vertices[i].x;
			fvertices[i * 2 + 1] = vertices[i].y;
		}
		slickShape = new Polygon(fvertices);
		
		// Area
		area = 0;
		for (int i = 0; i < vertices.length; i++) {
			area += vertices[i].x * vertices[(i + 1) % vertices.length].y - 
					vertices[i].y * vertices[(i + 1) % vertices.length].x;
		}
		area /= 2;
	}
	
	public float area() {
		return area;
	}
	
	public Shape toSlickShape() {
		return slickShape;
	}
	
	/**
	 * Caches a transformed version of all vertices and segments which will be used to 
	 * increase performance for some calculations. 
	 */
	public void cacheTransform(Vector2f translation, float rotation) {
		translateVertices = new Vector2f[vertices.length];
		for (int i = 0; i < translateVertices.length; i++) {
			translateVertices[i] = vertices[i].copy().rotate(rotation).add(translation);
		}
		translateFaces = new Segment[vertices.length];
		for (int i = 0; i < translateFaces.length; i++) {
			translateFaces[i] = new Segment(translateVertices[i], 
					translateVertices[(i + 1) % translateVertices.length]);
		}
	}
	
	/**
	 * Uncaches the transformed version of vertices and segments, causing subsequent calls 
	 * to methods which depend of that cache to throw IllegalStateExceptions until a 
	 * further call to cacheTransform. 
	 */
	public void invalidateTransform() {
		translateVertices = null;
		translateFaces = null;
	}
	
	/**
	 * Translation cache dependent.
	 */
	public boolean contains(Vector2f point) {
		if (translateVertices == null)
			throw new IllegalStateException("Transform must be cached");
		
		// The random ray direction helps avoid a weird glitch
		return perimiterIntersections(GeometryFactory.makeRay(
				point, (float) (Math.random() * Math.PI * 2))) % 2 == 1;
	}
	
	/**
	 * Translation cache dependent.
	 */
	private int perimiterIntersections(Segment segment) {
		int count = 0;
		for (Segment face : translateFaces) {
			if (face.intersects(segment))
				count++;
		}
		return count;
	}
	
	/**
	 * Translation cache dependent. Nullable.
	 */
	public Convex intersection(Convex other) {
		if (translateVertices == null)
			throw new IllegalStateException("Transform must be cached");
		
		List<Vector2f> intersectionVertices = new ArrayList<Vector2f>();
		// Any intersection between faces will be within the intersection polygon
		for (Segment thisFace : translateFaces) {
			for (Segment otherFace : other.translateFaces) {
				Vector2f intersection = thisFace.intersection(otherFace);
				if (intersection != null) {
					intersectionVertices.add(intersection);
				}
			}
		}
		// Any vertex of this contained within other will be within the intersection polygon
		for (Vector2f vertex : this.translateVertices) {
			if (other.contains(vertex)) {
				intersectionVertices.add(vertex);
			}
		}
		// Any vertex of other contained within this will be within the intersection polygon
		for (Vector2f vertex : other.translateVertices) {
			if (this.contains(vertex)) {
				intersectionVertices.add(vertex);
			}
		}
		// Sort
		Vector2f within = average(intersectionVertices);
		intersectionVertices.sort(LambdaUtils.compare(
				vector -> (double) vector.directionRelativeTo(within)
				));
		
		if (intersectionVertices.isEmpty())
			return null;
		else
			return new Convex(intersectionVertices.toArray(new Vector2f[intersectionVertices.size()]));
	}
	
	private static Vector2f average(List<Vector2f> points) {
		float x = 0;
		float y = 0;
		for (Vector2f vertex : points) {
			x += vertex.x;
			y += vertex.y;
		}
		x /= points.size();
		y /= points.size();
		return new Vector2f(x, y);
	}
	
}