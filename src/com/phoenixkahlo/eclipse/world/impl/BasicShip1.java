package com.phoenixkahlo.eclipse.world.impl;

import java.util.function.Consumer;

import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

import com.phoenixkahlo.eclipse.world.ImageResource;
import com.phoenixkahlo.eclipse.world.Ship;

public class BasicShip1 extends Ship {

	public BasicShip1() {
		// Create texture
		injectTexture(ImageResource.BASIC_SHIP_1.image(), 10, 10, 0);
		// Create walls
		addConvexFixture(makeRect(-5, -5, -4, 5));
		addConvexFixture(makeRect(4, -5, 5, 5));
		addConvexFixture(makeRect(-5, -5, 5, -4));
		addConvexFixture(makeRect(-5, 4, -2, 5));
		addConvexFixture(makeRect(2, 4, 5, 5));
		// Create mass
		getBody().setMass(MassType.NORMAL);
		// Create floor
		addArea(new Rectangle(10, 10));
		// Create useables
		addUseable(makeRect(-1, -4, 1, -3), new Consumer<Player>() {
			@Override
			public void accept(Player player) {
				System.out.println("accepted " + player);
			}
		});
	}
	
	private Convex makeRect(double x1, double y1, double x2, double y2) {
		Convex out = new Rectangle(x2 - x1, y2 - y1);
		out.translate((x2 - x1) / 2, (y2 - y1) / 2);
		out.translate(x1, y1);
		return out;
	}
	
}
