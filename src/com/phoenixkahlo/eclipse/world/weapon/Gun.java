package com.phoenixkahlo.eclipse.world.weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.RaycastResult;
import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.DamageType;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.entity.Entity;
import com.phoenixkahlo.utils.LambdaUtils;

public abstract class Gun implements Weapon {

	private transient double damage;
	private transient double range;
	
	public Gun(double damage, double range) {
		this.damage = damage;
		this.range = range;
	}
	
	protected void hit(WorldState state, Entity entity) {
		entity.hurt(damage, DamageType.BULLET);
	}
	
	@Override
	public void use(WorldState state, Vector2 target, Entity user) {
		Vector2 userPos = user.getBody().getWorldCenter();
		if (userPos.equals(target))
			return;
		
		double distance = userPos.distance(target);
		target.subtract(userPos);
		target.multiply(1 / distance);
		target.multiply(range);
		target.add(userPos);
		
		List<RaycastResult> collisions = new ArrayList<RaycastResult>();
		state.getWorld().raycast(userPos, target, null, false, false, true, collisions);
		
		Map<Body, Entity> map = new HashMap<Body, Entity>();
		for (Entity entity : state.getEntities()) {
			Body body = entity.getBody();
			if (body != null)
				map.put(body, entity);
		}
		
		collisions.removeIf(collision -> !map.containsKey(collision.getBody()));
		collisions.sort(LambdaUtils.compare(
				result -> result.getBody().getWorldCenter().distance(userPos)));
		
		if (!collisions.isEmpty())
			hit(state, map.get(collisions.get(0).getBody()));
	}
	
}