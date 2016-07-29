package com.phoenixkahlo.testing.physics;

import com.phoenixkahlo.physics.Segment;
import com.phoenixkahlo.physics.Vector2f;

public class ClosestPointTest {

	public static void main(String[] args) {
		Segment segment = new Segment(new Vector2f(-5, -5), new Vector2f(5, 5));
		Vector2f p1 = new Vector2f(-5, -7);
		Vector2f p2 = new Vector2f(8, 8);
		Vector2f p3 = new Vector2f(2, -2);
		System.out.println(segment.closestPointTo(p1));
		System.out.println(segment.closestPointTo(p2));
		System.out.println(segment.closestPointTo(p3));
	}
	
}
