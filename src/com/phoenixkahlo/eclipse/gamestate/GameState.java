package com.phoenixkahlo.eclipse.gamestate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

import com.phoenixkahlo.networkingcore.FieldDecoder;

public class GameState {

	private transient World world = new World();
	private List<Entity> entities = new ArrayList<Entity>();
	private transient int index; // Iterates forward
	
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
	
	/**
	 * Gives the world any bodies the entities provide
	 */
	@FieldDecoder.DecodingFinisher
	public void finishDecoding() {
		for (Entity entity : entities) {
			Body body = entity.toBody();
			if (body != null) world.addBody(body);
		}
	}
	
}
