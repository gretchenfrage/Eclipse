package com.phoenixkahlo.eclipse.world.impl;

import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

import com.phoenixkahlo.eclipse.world.ImageResource;
import com.phoenixkahlo.eclipse.world.Ship;
import com.phoenixkahlo.utils.GeomUtils;

public class BasicShip1 extends Ship {

	public BasicShip1() {
		// Create texture
		injectTexture(ImageResource.BASIC_SHIP_1.image(), 10, 10, 0);
		// Create walls
		addConvexFixture(GeomUtils.makeRect(-5, -5, -4, 5));
		addConvexFixture(GeomUtils.makeRect(4, -5, 5, 5));
		addConvexFixture(GeomUtils.makeRect(-5, -5, 5, -4));
		addConvexFixture(GeomUtils.makeRect(-5, 4, -2, 5));
		addConvexFixture(GeomUtils.makeRect(2, 4, 5, 5));
		// Create mass
		getBody().setMass(MassType.NORMAL);
		// Create floor
		addArea(new Rectangle(10, 10));
		// Create areas
		setHelmArea(GeomUtils.makeRect(-1, -4, 1, -2));
		// Configure some more stuff
		setForwardThrustMultiplier(400);
		setBackwardThrustMultiplier(250);
		setStrafeThrustMultiplier(350);
		setAngularThrustMultiplier(400);
	}
	
}
