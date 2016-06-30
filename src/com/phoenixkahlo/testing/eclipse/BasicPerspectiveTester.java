package com.phoenixkahlo.testing.eclipse;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.BasicPerspective;

public class BasicPerspectiveTester {

	public static void main(String[] args) {
		BasicPerspective perspective = new BasicPerspective();
		perspective.setScale(1);
		perspective.setX(10);
		perspective.setY(5);
		perspective.setRotation((float) (Math.PI / 2));
		System.out.println(
				perspective.screenToWorld(
						new Vector2(10, 5), 
						new Vector2(30, 20)
						));
	}
	
}
