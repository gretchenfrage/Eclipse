package com.phoenixkahlo.eclipse.world.event;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.EclipseCodingProtocol;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.entity.Entity;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldEncoder;
import com.phoenixkahlo.networking.ProtocolViolationException;
import com.phoenixkahlo.utils.CompoundedByteArray;

public class SetEntitiesEvent implements Consumer<WorldState> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(SetEntitiesEvent.class, SetEntitiesEvent::new, subEncoder);
	}
	
	private CompoundedByteArray data;

	private SetEntitiesEvent() {}
	
	public SetEntitiesEvent(List<Entity> entities) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (Entity entity : entities) {
			try {
				EclipseCodingProtocol.getEncoder().encode(entity, out);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		data = new CompoundedByteArray(out.toByteArray());
	}
	
	@Override
	public void accept(WorldState state) {
		state.removeAllEntities();
		try (InputStream in = new ByteArrayInputStream(data.toArray())) {
			while (in.available() > 0)
				state.addEntity((Entity) EclipseCodingProtocol.getDecoder().decode(in));
		} catch (IOException | ProtocolViolationException e) {
			throw new RuntimeException(e);
		}
	}
	
}
