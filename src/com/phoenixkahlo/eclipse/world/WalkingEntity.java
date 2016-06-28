package com.phoenixkahlo.eclipse.world;

import org.dyn4j.geometry.Vector2;

/**
 * A BodyTextureEntity that can walk and maybe thrust.
 */
public abstract class WalkingEntity extends BodyTextureEntity {

	private transient float walkSpeed = 0;
	private transient boolean canThrust = false;
	private transient float thrustForce = 0;
	private Vector2 direction = new Vector2(0, 0);
	
	public WalkingEntity() {}
	
	public WalkingEntity(int id, RenderLayer layer) {
		super(id, layer);
	}
	
	public WalkingEntity(RenderLayer layer) {
		super(layer);
	}

	@Override
	public void preTick() {
		super.preTick();
		if (false) { //TODO: make this detect if standing on platform
			Vector2 vector = direction.copy();
			vector.multiply(walkSpeed);
			vector.subtract(getBody().getLinearVelocity());
			vector.multiply(getBody().getMass().getMass());
			getBody().applyImpulse(vector);
		} else {
			getBody().applyForce(direction.copy().multiply(thrustForce));
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

	public Vector2 getDirection() {
		return direction;
	}

	public void setDirection(Vector2 direction) {
		this.direction = direction;
	}
	
}
