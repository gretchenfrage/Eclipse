package com.phoenixkahlo.eclipse.world.entity;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.AbstractWalkingEntity;
import com.phoenixkahlo.eclipse.world.BasicPerspective;
import com.phoenixkahlo.eclipse.world.ImageResource;
import com.phoenixkahlo.eclipse.world.Perspective;

public class Player extends AbstractWalkingEntity {

	private transient BasicPerspective perspective = new BasicPerspective();;

	public Player() {
		if (ImageResource.BALL_1.image() != null)
			injectTexture(ImageResource.BALL_1.image(), 20, 20, 0);
		addBodyFixture(new BodyFixture(new Circle(10)));
		getBody().setMass(MassType.NORMAL);
		setWalkSpeed(100);
		setThrustForce(100);
		setCanThrust(true);
	}
	
	@Override
	public void postTick() {
		super.postTick();
		Vector2 position = getBody().getTransform().getTranslation();
		perspective.setX((float) position.x);
		perspective.setY((float) position.y);
	}

	@Override
	public Perspective getPerspective() {
		return perspective;
	}
	
}
