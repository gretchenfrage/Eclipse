package com.phoenixkahlo.eclipse.world.entity;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;

import com.phoenixkahlo.eclipse.world.AbstractBodyTextureEntity;
import com.phoenixkahlo.eclipse.world.ImageResource;

public class Ball extends AbstractBodyTextureEntity {

	public Ball() {
		addBodyFixture(new BodyFixture(new Circle(50)));
		injectTexture(ImageResource.BALL_1.image(), 50, 50, 0);
	}
	
}
