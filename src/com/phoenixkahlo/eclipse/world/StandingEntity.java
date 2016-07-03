package com.phoenixkahlo.eclipse.world;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * It stands on things.
 */
public class StandingEntity extends BodyTextureEntity {
	
	/**
	 * Correcting rotation without fixed angular velocity can be weird.
	 */
	private transient boolean correctRotation = true;
	private int lastPlatformID = -1;
	private double lastPlatformRotation;
	
	public StandingEntity(int id, RenderLayer layer) {
		super(id, layer);
	}
	
	public StandingEntity(RenderLayer layer) {
		super(layer);
	}
	
	public StandingEntity() {}
	
	/**
	 * @return nullable
	 */
	public Entity platformOn(WorldState state) {
		for (Entity entity : state.getEntities()) {
			if (entity.isStandingOn(getBody().getWorldPoint(new Vector2())))
				return entity;
		}
		return null;
	}

	/**
	 * Override if this entity wants to be moving relative to its platform.
	 */
	protected Vector2 getTargetRelativeVelocity() {
		return new Vector2();
	}

	/**
	 * Called when StandingEntity rotates itself due to platform rotation. 
	 */
	protected void onPlatformRotation(double theta) {}
	
	@Override
	public void postTick(WorldState state) {
		super.postTick(state);
		
		Body body = getBody();
		Entity platformOn = platformOn(state);
		if (platformOn != null && platformOn.getBody() != null) {
			Vector2 location = body.getWorldPoint(new Vector2());
			Vector2 relativeTarget = getTargetRelativeVelocity();
			Vector2 vector;
			vector = platformOn.getBody().getLinearVelocity(location); // Platform velocity
			vector.add(relativeTarget); // Target velocity
			vector.subtract(body.getLinearVelocity()); // Target velocity difference
			vector.multiply(body.getMass().getMass()); // Force to apply
			body.applyImpulse(vector);
			platformOn.getBody().applyImpulse(vector.copy().multiply(-1), body.getWorldCenter());
		}
		
		if (correctRotation) {
			int currentPlatformID;
			if (platformOn == null || platformOn.getBody() == null)
				currentPlatformID = -1;
			else
				currentPlatformID = platformOn.getID();
			if (lastPlatformID == currentPlatformID && currentPlatformID != -1) {
				double theta = platformOn.getBody().getTransform().getRotation() - lastPlatformRotation;
				body.rotateAboutCenter(theta);
				onPlatformRotation(theta);
			}
			lastPlatformID = currentPlatformID;
			if (currentPlatformID != -1) {
				lastPlatformRotation = platformOn.getBody().getTransform().getRotation();
			}
		}
	}

	public boolean getCorrectRotation() {
		return correctRotation;
	}

	public void setCorrectRotation(boolean correctRotation) {
		this.correctRotation = correctRotation;
	}
	
}
