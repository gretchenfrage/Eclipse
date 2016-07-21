package com.phoenixkahlo.eclipse.world.entity;

import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;

import com.phoenixkahlo.eclipse.ImageResource;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;
import com.phoenixkahlo.utils.GeomUtils;

public class BasicShip1 extends Ship {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(BasicShip1.class, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(BasicShip1.class, BasicShip1::new, subDecoder);
	}
	
	public BasicShip1() {
		// Create texture
		injectTexture(ImageResource.BASIC_SHIP_1.image(), 10);
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
