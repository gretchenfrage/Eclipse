package com.phoenixkahlo.eclipse.world.entity;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.BasicPerspective;
import com.phoenixkahlo.eclipse.world.ImageResource;
import com.phoenixkahlo.eclipse.world.Perspective;
import com.phoenixkahlo.eclipse.world.WalkingEntity;

public class Player extends WalkingEntity {

	private transient BasicPerspective perspective = new BasicPerspective();;

	public Player() {
		if (ImageResource.BALL_1.image() != null)
			injectTexture(ImageResource.BALL_1.image(), 1, 1, 0);
		addBodyFixture(new BodyFixture(new Circle(0.5)));
		getBody().setMass(MassType.NORMAL);
		setWalkSpeed(1);
		setThrustForce(1000);
		setCanThrust(true);
		
		perspective.setScale(50);
	}
	
	@Override
	public void postTick() {
		super.postTick();
		Vector2 position = getBody().getWorldPoint(new Vector2(0, 0));
		perspective.setX((float) position.x);
		perspective.setY((float) position.y);
	}

	@Override
	public Perspective getPerspective() {
		return perspective;
	}
	
}
