package com.phoenixkahlo.physics;

public class Rigid {

	private Polygon shape;
	private float mass;
	private Vector2f velocity;
	private float angularVelocity;
	private Vector2f location;
	private float angle;
	
	public Rigid(Polygon shape) {
		this.shape = shape;
		velocity = new Vector2f();
		location = new Vector2f();
		mass = shape.area();
	}
	
	public void applyForce(Vector2f force, Vector2f location) {
		applyTorque(force.magnitude() * worldToLocal(location).magnitude() * 
				(float) Math.sin(force.shortestAngle(worldToLocal(location))));
		applyForce(force);
	}
	
	public void applyForce(Vector2f force) {
		velocity.add(force.divide(mass));
	}
	
	public void applyTorque(float torque) {
		//TODO: integrate moment of rotation
		angularVelocity += torque;
	}

	public Vector2f localToWorld(Vector2f local) {
		return local.add(location);
	}
	
	public Vector2f worldToLocal(Vector2f world) {
		return world.subtract(location);
	}
	
	public void translate(Vector2f translation) {
		location.add(translation);
	}
	
	public void changeAngle(float theta) {
		angle += theta;
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
		return angle;
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

	public void setAngle(float angle) {
		this.angle = angle;
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
