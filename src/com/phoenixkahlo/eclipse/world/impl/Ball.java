package com.phoenixkahlo.eclipse.world.impl;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;

import com.phoenixkahlo.eclipse.world.BodyTextureEntity;
import com.phoenixkahlo.eclipse.world.ImageResource;

public class Ball extends BodyTextureEntity {

	public Ball() {
		addBodyFixture(new BodyFixture(new Circle(50)));
		injectTexture(ImageResource.BALL_1.image(), 50, 50, 0);
	}
	
}
