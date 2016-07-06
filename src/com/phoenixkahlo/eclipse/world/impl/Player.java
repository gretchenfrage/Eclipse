package com.phoenixkahlo.eclipse.world.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;

import com.phoenixkahlo.eclipse.world.ImageResource;
import com.phoenixkahlo.eclipse.world.WalkingEntity;
import com.phoenixkahlo.networking.SerializationUtils;
import com.phoenixkahlo.utils.MathUtils;

/**
 * It's you!
 */
public class Player extends WalkingEntity {
	
	private double facingAngle;
	
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
		
		setBaseRenderAngle(-MathUtils.PI_F / 2);
	}
	
	public void setFacingAngle(double facingAngle) {
		this.facingAngle = facingAngle;
	}
	
	@Override
	public double getRenderAngle() {
		return getBaseRenderAngle() + facingAngle;
	}
	
	@Override
	public void finishEncoding(OutputStream out) throws IOException {
		super.finishEncoding(out);
		
		SerializationUtils.writeFloat(getBaseRenderAngle(), out);
	}
	
	@Override
	public void finishDecoding(InputStream in) throws IOException {
		super.finishDecoding(in);
		
		setBaseRenderAngle(SerializationUtils.readFloat(in));
	}
	
}
