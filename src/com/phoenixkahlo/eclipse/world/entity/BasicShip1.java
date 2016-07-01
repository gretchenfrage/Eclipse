package com.phoenixkahlo.eclipse.world.entity;

import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

import com.phoenixkahlo.eclipse.world.BodyPlatform;
import com.phoenixkahlo.eclipse.world.ImageResource;

public class BasicShip1 extends BodyPlatform {

	public BasicShip1() {
		if (ImageResource.BASIC_SHIP_1.image() != null)
			injectTexture(ImageResource.BASIC_SHIP_1.image(), 10, 10, 0);
		
		Convex c1 = new Rectangle(1, 10);
		c1.translate(-4.5, 0);
		addConvexFixture(c1);
		Convex c2 = new Rectangle(1, 10);
		c2.translate(4.5, 0);
		addConvexFixture(c2);
		Convex c3 = new Rectangle(3, 1);
		c3.translate(-3.5, 4.5);
		addConvexFixture(c3);
		Convex c4 = new Rectangle(3, 1);
		c4.translate(3.5, 4.5);
		addConvexFixture(c4);
		Convex c5 = new Rectangle(10, 1);
		c5.translate(0, -4.5);
		addConvexFixture(c5);
		
		getBody().setMass(MassType.NORMAL);
		
		addArea(new Rectangle(10, 10));
	}
	
}
