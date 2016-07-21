package com.phoenixkahlo.eclipse.world.event;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.EclipseCodingProtocol;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.entity.Entity;
import com.phoenixkahlo.networking.DecodingFinisher;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;
import com.phoenixkahlo.networking.ProtocolViolationException;

/**
 * Holds the entity in serialized byte[] form so that it can decode a fresh copy with each
 * invocation.
 */
public class EntityAdditionEvent implements Consumer<WorldState>, DecodingFinisher {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(EntityAdditionEvent.class, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(EntityAdditionEvent.class, EntityAdditionEvent::new, subDecoder);
	}
	
	private static EncodingProtocol encoder = EclipseCodingProtocol.ENCODER;
	private static DecodingProtocol decoder = EclipseCodingProtocol.DECODER;
	
	private byte[] entityBytes;
	
	private EntityAdditionEvent() {}
	
	public EntityAdditionEvent(Entity entity) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			encoder.encode(entity, out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		entityBytes = out.toByteArray();
	}
	
	@Override
	public void accept(WorldState state) {
		try {
			state.addEntity((Entity) decoder.decode(new ByteArrayInputStream(entityBytes)));
		} catch (IOException | ProtocolViolationException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Ensures that entityBytes follows protocol.
	 */
	@Override
	public void finishDecoding(InputStream in) throws ProtocolViolationException {
		try {
			Object obj = decoder.decode(new ByteArrayInputStream(entityBytes));
			if (!(obj instanceof Entity))
				throw new ProtocolViolationException("entityBytes doesn't decode to Entity");
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ProtocolViolationException e) {
			throw e;
		}
	}

}
