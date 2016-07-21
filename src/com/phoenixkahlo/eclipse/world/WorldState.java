package com.phoenixkahlo.eclipse.world;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.entity.Entity;
import com.phoenixkahlo.networking.DecodingFinisher;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

/**
 * The state of the world at an instance in time. Can be ticked.
 */
public class WorldState implements DecodingFinisher {
	
	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(WorldState.class, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(WorldState.class, WorldState::new, subDecoder);
	}
	
	public static final int TICKS_PER_SECOND = 60;
	public static final double SECONDS_PER_TICK = 1D / TICKS_PER_SECOND;

	private transient World world = new World();
	private List<Entity> entities = new ArrayList<Entity>();
	private transient int index; // Iterates forward
	private Background background; // Nullable
	
	public WorldState() {
		world.setGravity(new Vector2(0, 0));
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
		Body body = entity.getBody();
		if (body != null) world.addBody(body);
	}
	
	public void removeEntity(Entity entity) {
		int location = entities.indexOf(entity);
		if (location < 0) return;
		if (location <= index) index--;
		entities.remove(location);
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
	 * @return nullable
	 */
	public Entity getEntity(int id) {
		for (Entity entity : entities) {
			if (entity.getID() == id)
				return entity;
		}
		return null;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void tick() {
		index = 0;
		while (index < entities.size()) {
			entities.get(index).preTick(this);
			index++;
		}
		world.update(WorldState.SECONDS_PER_TICK);
		index = 0;
		while (index < entities.size()) {
			entities.get(index).postTick(this);
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
	@Override
	public void finishDecoding(InputStream in) {
		world.removeAllBodies();
		for (Entity entity : entities) {
			Body body = entity.getBody();
			if (body != null) world.addBody(body);
		}
	}
	
	@Override
	public String toString() {
		return "state with entities: " + entities.toString();
	}
	
}
