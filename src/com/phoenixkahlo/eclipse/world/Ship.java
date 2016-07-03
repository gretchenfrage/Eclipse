package com.phoenixkahlo.eclipse.world;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.impl.Player;

public abstract class Ship extends BodyPlatform {

	private transient Map<Convex, Consumer<Player>> useables = new HashMap<Convex, Consumer<Player>>();
	
	protected void addUseable(Convex area, Consumer<Player> useable) {
		useables.put(area, useable);
	}
	
	protected void addUseable(Convex[] area, Consumer<Player> useable) {
		for (Convex convex : area)
			addUseable(convex, useable);
	}

	@Override
	public Consumer<Player> getUseable(Vector2 position) {
		for (Convex convex : useables.keySet()) {
			if (convex.contains(getBody().getLocalPoint(position)))
				return useables.get(convex);
		}
		return null;
	}
	
}
