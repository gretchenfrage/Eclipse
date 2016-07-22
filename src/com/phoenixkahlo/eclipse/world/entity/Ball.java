package com.phoenixkahlo.eclipse.world.entity;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;

import com.phoenixkahlo.eclipse.ImageResource;
import com.phoenixkahlo.eclipse.world.RenderLayer;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class Ball extends BodyTextureEntity {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(Ball.class, Ball::new, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(Ball.class, Ball::new, subDecoder);
	}
	
	public Ball() {
		addBodyFixture(new BodyFixture(new Circle(1)));
		injectTexture(ImageResource.BALL_1.image(), 2);
		getBody().setMass(MassType.NORMAL);
	}

	@Override
	public RenderLayer getRenderLayer() {
		return RenderLayer.SUPER_PLAYER;
	}
	
}
