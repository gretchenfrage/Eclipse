package com.phoenixkahlo.physics;

public class GeometryFactory {

	/**
	 * Segments can effectively be rays due to the finite range of float values. 
	 */
	public static Segment makeRay(Vector2f start, float theta) {
		theta %= Math.PI * 2;
		if (theta == Math.PI / 2) { // Straight down
			return new Segment(start.x, start.y, Float.MAX_VALUE);
		} else if (theta == Math.PI * 3 / 2) { // Straight up
			return new Segment(start.x, -Float.MAX_VALUE, start.y);
		} else {
			float slope = (float) Math.tan(theta);
			float intercept = -slope * start.x + start.y;
			if (theta < Math.PI / 2 || theta > Math.PI * 3 / 2) { // Angled right
				return new Segment(slope, intercept, start.x, Float.MAX_VALUE);
			} else { // Angled left
				return new Segment(slope, intercept, -Float.MAX_VALUE, start.x);
			}
		}
	}
	
}
