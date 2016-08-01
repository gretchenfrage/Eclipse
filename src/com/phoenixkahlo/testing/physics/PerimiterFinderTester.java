package com.phoenixkahlo.testing.physics;

import java.util.Arrays;

import com.phoenixkahlo.physics.Convex;
import com.phoenixkahlo.physics.Polygon;

public class PerimiterFinderTester {

	public static void main(String[] args) {
		Convex c1 = new Convex(
				-1, -1,
				3, -1,
				4, 1,
				3, 4,
				-1, 4,
				-2, 1
				);
		Convex c2 = new Convex(
				-2, 1,
				-1, 4,
				-3, 4
				);
		Convex c3 = new Convex(
				3, 4,
				4, 1,
				7, 4,
				6, 7
				);
		Polygon poly = new Polygon(c1, c2, c3);
		System.out.println(Arrays.toString(poly.getPerimiter()));
	}
	
}
