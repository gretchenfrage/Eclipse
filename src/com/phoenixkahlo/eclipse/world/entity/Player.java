package com.phoenixkahlo.eclipse.world.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.BasicPerspective;
import com.phoenixkahlo.eclipse.world.ImageResource;
import com.phoenixkahlo.eclipse.world.Perspective;
import com.phoenixkahlo.eclipse.world.WalkingEntity;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.networking.SerializationUtils;
import com.phoenixkahlo.utils.MathUtils;

/**
 * It's you!
 */
public class Player extends WalkingEntity {
	
	private BasicPerspective perspective = new BasicPerspective();

	public Player() {
		if (ImageResource.HUMAN_1.image() != null)
			injectTexture(ImageResource.HUMAN_1.image(), 1, 1, 0);
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
	public void setRenderAngle(float renderAngle) {
		super.setRenderAngle(renderAngle - MathUtils.PI_F / 2);
	}

	@Override
	public void postTick(WorldState state) {
		super.postTick(state);
		
		Vector2 position = getBody().getWorldPoint(new Vector2(0, 0));
		perspective.setX((float) position.x);
		perspective.setY((float) position.y);
		
		// Even on platforms, it mustn't rotate.
		getBody().getTransform().setRotation(0);
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
	public void finishEncoding(OutputStream out) throws IOException {
		super.finishEncoding(out);
		
		SerializationUtils.writeFloat(getRenderAngle(), out);
	}
	
	@Override
	public void finishDecoding(InputStream in) throws IOException {
		super.finishDecoding(in);
		
		setRenderAngle(SerializationUtils.readFloat(in));
	}
	
}
