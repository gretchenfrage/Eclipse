package com.phoenixkahlo.testing.physics;

import com.phoenixkahlo.physics.Segment;
import com.phoenixkahlo.physics.Vector2f;

public class SegmentTest {

	public static void main(String[] args) {
		Segment s1 = new Segment(new Vector2f(1, 0), new Vector2f(3, 2));
		Segment s2 = new Segment(new Vector2f(0, 1), new Vector2f(10, 1));
		System.out.println(s1.intersection(s2));
	}
	
}
