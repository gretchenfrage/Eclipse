package com.phoenixkahlo.eclipse.world.entity;

import java.io.IOException;
import java.io.InputStream;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Color;

import com.phoenixkahlo.eclipse.world.BasicPerspective;
import com.phoenixkahlo.eclipse.world.ImageResource;
import com.phoenixkahlo.eclipse.world.Perspective;
import com.phoenixkahlo.eclipse.world.WalkingEntity;
import com.phoenixkahlo.eclipse.world.WorldState;

/**
 * It's you!
 */
public class Player extends WalkingEntity {
	
	private BasicPerspective perspective = new BasicPerspective();
	private int color = (int) (Math.random() * (0xFFFFFF + 1)) | (int) (Math.random() * (0xFFFFFF + 1));

	public Player() {
		if (ImageResource.BALL_2.image() != null)
			injectTexture(ImageResource.BALL_2.image(), 1, 1, 0);
		addBodyFixture(new BodyFixture(new Circle(0.5)));
		getBody().setMass(MassType.FIXED_ANGULAR_VELOCITY);
		setWalkSpeed(10);
		setThrustForce(0.25F);
		setCanThrust(true);
		setSprintWalkingMultiplier(2);
		setSprintThrustingMultiplier(2);
		
		perspective.setScale(25);
		perspective.setSuggestibleScaleMin(10);
		perspective.setSuggestibleScaleMax(50);
	}
	
	@Override
	public void postTick(WorldState state) {
		super.postTick(state);
		Vector2 position = getBody().getWorldPoint(new Vector2(0, 0));
		perspective.setX((float) position.x);
		perspective.setY((float) position.y);
	}

	@Override
	public Perspective getPerspective() {
		return perspective;
	}
	
	@Override
	protected void onPlatformRotation(double theta) {
		perspective.setRotation(perspective.getRotation() + (float) theta);
	}
	
	@Override
	public void finishDecoding(InputStream in) throws IOException {
		super.finishDecoding(in);
		setColor(new Color(color));
	}
	
}
