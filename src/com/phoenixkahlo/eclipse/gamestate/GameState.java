package com.phoenixkahlo.eclipse.gamestate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

public class GameState {

	private World world = new World();
	private List<Entity> entities = new ArrayList<Entity>();
	private int index; // Iterates forward
	
	public void addEntity(Entity entity) {
		entities.add(entity);
		Body body = entity.toBody();
		if (body != null) world.addBody(body);
	}
	
	public void removeEntity(Entity entity) {
		int location = entities.indexOf(entity);
		if (location < 0) return;
		if (location <= index) index--;
		entities.remove(index);
		Body body = entity.toBody();
		if (body != null) world.removeBody(body);
	}
	
	public List<Entity> getEntities() {
		return Collections.unmodifiableList(entities);
	}
	
}
