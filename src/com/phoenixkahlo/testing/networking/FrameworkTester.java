package com.phoenixkahlo.testing.networking;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;

public class FrameworkTester {

	public static void main(String[] args) {
		Body body = new Body();
		body.addFixture(new Circle(10));
		System.out.println(body.getMass().getMass());
	}

}
