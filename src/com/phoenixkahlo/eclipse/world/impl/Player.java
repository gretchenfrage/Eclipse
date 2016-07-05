package com.phoenixkahlo.eclipse.world.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;

import com.phoenixkahlo.eclipse.world.ImageResource;
import com.phoenixkahlo.eclipse.world.WalkingEntity;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.networking.SerializationUtils;
import com.phoenixkahlo.utils.MathUtils;

/**
 * It's you!
 */
public class Player extends WalkingEntity {
		
	private static final float TRUE_BASE_RENDER_ANGLE = MathUtils.PI_F / 2;
	
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
	}
	
	@Override
	public void setRenderAngle(float renderAngle) {
		super.setRenderAngle(renderAngle - TRUE_BASE_RENDER_ANGLE);
	}

	@Override
	public void postTick(WorldState state) {
		super.postTick(state);
		
		// Even on platforms, it mustn't rotate.
		getBody().getTransform().setRotation(0);
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
