package com.phoenixkahlo.physics;

public class Triangle {

	private Vector2f p1;
	private Vector2f p2;
	private Vector2f p3;
	
	public Triangle(Vector2f p1, Vector2f p2, Vector2f p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}
	
	public Vector2f centroid() {
		Segment s1 = new Segment(p1, midPoint(p2, p3));
		Segment s2 = new Segment(p3, midPoint(p1, p2));
		return s1.intersection(s2);
	}
	
	private static Vector2f midPoint(Vector2f p1, Vector2f p2) {
		return new Vector2f((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}
	
}
