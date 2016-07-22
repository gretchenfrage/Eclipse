package com.phoenixkahlo.eclipse.world.entity;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;

import com.phoenixkahlo.eclipse.ImageResource;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class Dummy extends HurtableWalkingEntity {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(Dummy.class, Dummy::new, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(Dummy.class, Dummy::new, subDecoder);
	}
	
	public Dummy() {
		injectTexture(ImageResource.BALL_1.image(), 1);
		addBodyFixture(new BodyFixture(new Circle(0.5)));
		getBody().setMass(MassType.FIXED_ANGULAR_VELOCITY);
	}
	
}
