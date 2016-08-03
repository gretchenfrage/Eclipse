package com.phoenixkahlo.physics;

public class Rigid {

	private Polygon shape;
	private float mass;
	private Vector2f velocity;
	private float angularVelocity;
	private Vector2f location;
	private float rotation;
	
	public Rigid(Polygon shape) {
		this.shape = shape;
		velocity = new Vector2f();
		location = new Vector2f();
		mass = shape.area();
	}
	
	/**
	 * @param force unmutated.
	 * @param location unmutated.
	 */
	public void applyForce(Vector2f force, Vector2f location) {
		if (force.x == 0 && force.y == 0)
			return;
		worldToLocal(location);
		if (!(location.x == 0 && location.y == 0)) {
			float torque = force.magnitude() * location.magnitude() * 
					(float) Math.sin(force.shortestAngle(location));
			applyTorque(torque);
		}
		applyForce(force);
	}
	
	public void applyForce(Vector2f force) {
		velocity.add(force.divide(mass));
	}
	
	public void applyTorque(float torque) {
		//TODO: integrate moment of rotation
		angularVelocity += torque;
	}

	/**
	 * Mutates.
	 */
	public Vector2f localToWorld(Vector2f local) {
		return local.add(location);
	}
	
	/**
	 * Mutates.
	 */
	public Vector2f worldToLocal(Vector2f world) {
		return world.subtract(location);
	}
	
	public void translate(Vector2f translation) {
		location.add(translation);
	}
	
	public void changeRotation(float theta) {
		rotation += theta;
	}
	
	public Vector2f getVelocity() {
		return velocity;
	}

	public float getAngularVelocity() {
		return angularVelocity;
	}

	public Vector2f getLocation() {
		return location;
	}

	public float getRotation() {
		return rotation;
	}

	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}

	public void setAngularVelocity(float angularVelocity) {
		this.angularVelocity = angularVelocity;
	}

	public void setLocation(Vector2f location) {
		this.location = location;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}
	
	public Polygon getShape() {
		return shape;
	}
	
}
