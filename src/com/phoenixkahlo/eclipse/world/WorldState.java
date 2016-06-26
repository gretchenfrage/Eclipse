package com.phoenixkahlo.eclipse.world;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;

import com.phoenixkahlo.networking.FieldDecoder;

/**
 * The state of the world at an instance in time. Can be ticked.
 */
public class WorldState {
	
	public static final double SECONDS_PER_TICK = 1D / 60;
	public static final int MILLISECONDS_PER_TICK = (int) (SECONDS_PER_TICK * 1000);

	private transient World world = new World();
	private List<Entity> entities = new ArrayList<Entity>();
	private transient int index; // Iterates forward
	private Background background; // Nullable
	
	public void addEntity(Entity entity) {
		entities.add(entity);
		Body body = entity.getBody();
		if (body != null) world.addBody(body);
	}
	
	public void removeEntity(Entity entity) {
		int location = entities.indexOf(entity);
		if (location < 0) return;
		if (location <= index) index--;
		entities.remove(index);
		Body body = entity.getBody();
		if (body != null) world.removeBody(body);
	}
	
	/**
	 * @return a copy of the list of entities.
	 */
	public List<Entity> getEntities() {
		return new ArrayList<Entity>(entities);
	}
	
	/**
	 * Nullable.
	 */
	public Entity getEntity(int id) {
		for (Entity entity : entities) {
			if (entity.getID() == id)
				return entity;
		}
		return null;
	}
	
	public void tick() {
		index = 0;
		while (index < entities.size()) {
			entities.get(index).preTick();
			index++;
		}
		world.update(WorldState.SECONDS_PER_TICK);
		index = 0;
		while (index < entities.size()) {
			entities.get(index).postTick();
			index++;
		}
	}
	
	public Background getBackground() {
		return background;
	}

	public void setBackground(Background background) {
		this.background = background;
	}

	/**
	 * Gives the world any bodies the entities provide
	 */
	@FieldDecoder.DecodingFinisher
	public void finishDecoding(InputStream in) {
		world.removeAllBodies();
		for (Entity entity : entities) {
			Body body = entity.getBody();
			if (body != null) world.addBody(body);
		}
	}
	
}
