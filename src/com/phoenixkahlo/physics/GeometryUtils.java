package com.phoenixkahlo.physics;

import java.util.Arrays;
import java.util.List;

public class GeometryUtils {

	public static Vector2f average(Vector2f[] points) {
		return GeometryUtils.average(Arrays.asList(points));
	}

	public static Vector2f average(List<Vector2f> points) {
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
