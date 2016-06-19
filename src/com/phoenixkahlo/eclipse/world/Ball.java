package com.phoenixkahlo.eclipse.world;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;

public class Ball extends AbstractBodyTextureEntity {

	public Ball() {
		addBodyFixture(new BodyFixture(new Circle(1)));
		injectTexture(ImageResource.BALL_1.image(), 1, 1, 0);
	}
	
}
