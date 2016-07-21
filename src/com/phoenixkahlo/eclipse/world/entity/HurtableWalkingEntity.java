package com.phoenixkahlo.eclipse.world.entity;

import com.phoenixkahlo.eclipse.world.DamageType;
import com.phoenixkahlo.eclipse.world.WorldState;

public abstract class HurtableWalkingEntity extends WalkingEntity {

	private double health;
	private transient double maxHealth = Double.NaN; // May be NaN
	
	public void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public double getHealth() {
		return health;
	}
	
	public void setHealth(double health) {
		this.health = health;
	}
	
	@Override
	public void postTick(WorldState state) {
		super.postTick(state);
		if (health <= 0)
			die(state);
	}

	protected void die(WorldState state) {
		state.removeEntity(this);
	}
	
	@Override
	public void hurt(double points, DamageType type) {
		health -= points;
		if (health < 0) {
			health = 0;
		}
	}
	
	@Override
	public void heal(double points) {
		health += points;
		if (health > maxHealth)
			health = maxHealth;
	}
	
}
