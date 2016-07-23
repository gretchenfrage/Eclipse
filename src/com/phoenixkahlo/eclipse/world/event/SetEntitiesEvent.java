package com.phoenixkahlo.eclipse.world.event;

import java.util.List;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.entity.Entity;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldEncoder;

public class SetEntitiesEvent implements Consumer<WorldState> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(SetEntitiesEvent.class, SetEntitiesEvent::new, subEncoder);
	}
	
	private Entity[] entities;
	
	private SetEntitiesEvent() {}
	
	public SetEntitiesEvent(List<Entity> entities) {
		this.entities = entities.toArray(new Entity[entities.size()]);
	}
	
	@Override
	public void accept(WorldState state) {
		state.removeAllEntities();
		for (Entity entity : entities) {
			state.addEntity(entity);
		}
	}
	
}
