package com.phoenixkahlo.eclipse.world;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * It can walk on things, maybe it can thrust through space.
 */
public abstract class WalkingEntity extends StandingEntity {

	private transient float walkSpeed = 0;
	private transient boolean canThrust = false;
	private transient float thrustForce = 0;
	private transient float runningMultiplier = 1;
	private transient float sprintThrustingMultiplier = 1;
	private Vector2 direction = new Vector2(0, 0);
	private boolean isSprinting = false;
	
	public WalkingEntity() {}
	
	public WalkingEntity(int id, RenderLayer layer) {
		super(id, layer);
	}
	
	public WalkingEntity(RenderLayer layer) {
		super(layer);
	}

	@Override
	public void preTick(WorldState state) {
		super.preTick(state);
		Entity platform = platformOn(state);
		if (platform != null) {
			Vector2 vector = direction.copy();
			vector.multiply(walkSpeed);
			if (isSprinting)
				vector.multiply(runningMultiplier);
			vector.subtract(getBody().getLinearVelocity());
			vector.multiply(getBody().getMass().getMass());
			getBody().applyImpulse(vector);
			Body platformBody = platform.getBody();
			if (platformBody != null)
				platformBody.applyImpulse(vector.multiply(-1));
		} else {
			Vector2 vector = direction.copy();
			vector.multiply(thrustForce);
			if (isSprinting)
				vector.multiply(sprintThrustingMultiplier);
			getBody().applyForce(vector);
		}
	}

	public float getWalkSpeed() {
		return walkSpeed;
	}

	public void setWalkSpeed(float walkSpeed) {
		this.walkSpeed = walkSpeed;
	}

	public boolean isCanThrust() {
		return canThrust;
	}

	public void setCanThrust(boolean canThrust) {
		this.canThrust = canThrust;
	}

	public float getThrustForce() {
		return thrustForce;
	}

	public void setThrustForce(float thrust) {
		this.thrustForce = thrust;
	}

	public float getRunningMultiplier() {
		return runningMultiplier;
	}

	public float getSprintThrustingMultiplier() {
		return sprintThrustingMultiplier;
	}

	public void setSprintWalkingMultiplier(float runningMultiplier) {
		this.runningMultiplier = runningMultiplier;
	}

	public void setSprintThrustingMultiplier(float sprintThrustingMultiplier) {
		this.sprintThrustingMultiplier = sprintThrustingMultiplier;
	}

	public Vector2 getDirection() {
		return direction;
	}

	public void setDirection(Vector2 direction) {
		this.direction = direction;
	}

	public boolean isSprinting() {
		return isSprinting;
	}

	public void setIsSprinting(boolean isSprinting) {
		this.isSprinting = isSprinting;
	}
	
}
