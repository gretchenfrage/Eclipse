package com.phoenixkahlo.physics;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

import com.phoenixkahlo.utils.ArrayUtils;
import com.phoenixkahlo.utils.LambdaUtils;
import com.phoenixkahlo.utils.MathUtils;

public class Convex {

	private Vector2f[] vertices;
	private Shape slickShape;
	private float area;
	private float maxRadius;
	private Vector2f translation;
	
	private Vector2f[] transformVertices;
	private Segment[] transformFaces;
	
	public Convex(Vector2f... vertices) {
		this.vertices = vertices;
		cacheData();
	}
	
	public Convex(float... vertices) {
		if (vertices.length % 2 != 0)
			throw new IllegalArgumentException("Must have even number of arguments");
		this.vertices = new Vector2f[vertices.length / 2];
		for (int i = 0; i < vertices.length / 2; i++) {
			this.vertices[i] = new Vector2f(vertices[i * 2], vertices[i * 2 + 1]);
		}
		cacheData();
	}
	
	private void cacheData() {
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
		
		// Max radius
		maxRadius = MathUtils.max(vertices, Vector2f::magnitude);
	}
	
	/**
	 * Untransformed. Please don't modify.
	 */
	public Vector2f[] getVertices() {
		return vertices;
	}
	
	public float area() {
		return area;
	}
	
	public Shape toSlickShape() {
		return slickShape;
	}
	
	/**
	 * Caches a lack of transformation.
	 */
	public void cacheNoTransform() {
		this.translation = new Vector2f(0, 0);
		
		transformVertices = vertices;
		transformFaces = new Segment[vertices.length];
		for (int i = 0; i < transformFaces.length; i++) {
			transformFaces[i] = new Segment(transformVertices[i], 
					transformVertices[(i + 1) % transformVertices.length]);
		}
	}
	
	/**
	 * Caches a transformed version of all vertices and segments which will be used to 
	 * increase performance for some calculations. 
	 */
	public void cacheTransform(Vector2f translation, float rotation) {
		this.translation = translation;
		
		transformVertices = new Vector2f[vertices.length];
		for (int i = 0; i < transformVertices.length; i++) {
			transformVertices[i] = vertices[i].rotate(rotation).add(translation);
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
		transformVertices = null;
		transformFaces = null;
	}
	
	/**
	 * Translation cache dependent.
	 */
	public boolean contains(Vector2f point) {
		// The random ray direction helps avoid a weird glitch
		return perimiterIntersections(GeometryFactory.makeRay(
				point, (float) (Math.random() * Math.PI * 2))) % 2 == 1;
	}
	
	/**
	 * Translation cache dependent.
	 */
	private int perimiterIntersections(Segment segment) {
		int count = 0;
		for (Segment face : transformFaces) {
			if (face.intersects(segment))
				count++;
		}
		return count;
	}
	
	/**
	 * Translation cache dependent. Nullable.
	 */
	public Convex intersection(Convex other) {
		// Quickly return null if no possibility of intersection.
		if (translation.distance(other.translation) > maxRadius + other.maxRadius)
			return null;
		
		List<Vector2f> intersectionVertices = new ArrayList<Vector2f>();
		// Any intersection between faces will be within the intersection polygon
		for (Segment thisFace : transformFaces) {
			for (Segment otherFace : other.transformFaces) {
				Vector2f intersection = thisFace.intersection(otherFace);
				if (intersection != null) {
					intersectionVertices.add(intersection);
				}
			}
		}
		// Any vertex of this contained within other will be within the intersection polygon
		for (Vector2f vertex : this.transformVertices) {
			if (other.contains(vertex)) {
				intersectionVertices.add(vertex);
			}
		}
		// Any vertex of other contained within this will be within the intersection polygon
		for (Vector2f vertex : other.transformVertices) {
			if (this.contains(vertex)) {
				intersectionVertices.add(vertex);
			}
		}
		// Sort
		Vector2f within = GeometryUtils.average(intersectionVertices);
		intersectionVertices.sort(LambdaUtils.compare(
				vector -> (double) vector.directionRelativeTo(within)
				));
		
		if (intersectionVertices.isEmpty())
			return null;
		else
			return new Convex(intersectionVertices.toArray(new Vector2f[intersectionVertices.size()]));
	}
	
	/**
	 * Translation cache dependent.
	 */
	public Vector2f centroid() {
		Vector2f connector = vertexAverage();
		Vector2f[] subCentroids = new Vector2f[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			subCentroids[i] = new Triangle(
					transformVertices[i],
					transformVertices[(i + 1) % vertices.length],
					connector
					).centroid();
		}
		return GeometryUtils.average(subCentroids);
	}
	
	/**
	 * Translation cache dependent
	 */
	public Vector2f vertexAverage() {
		return GeometryUtils.average(transformVertices);
	}

	public boolean containsVertex(Vector2f vertex) {
		return ArrayUtils.contains(vertices, vertex);
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