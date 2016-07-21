package com.phoenixkahlo.eclipse.world.weapon;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.entity.Entity;

public interface Weapon {

	void use(WorldState state, Vector2 target, Entity user);
	
}
