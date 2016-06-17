package com.phoenixkahlo.eclipse.gamestate;

import static com.phoenixkahlo.networking.SerializationUtils.readDouble;
import static com.phoenixkahlo.networking.SerializationUtils.writeDouble;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.dyn4j.dynamics.Body;

import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

/**
 * Base class for entities with bodies.
 */
public abstract class AbstractBodyEntity implements Entity {

	private transient Body body;
	
	public AbstractBodyEntity(Body body) {
		this.body = body;
	}

	@Override
	public Body toBody() {
		return body;
	}
	
	@FieldEncoder.EncodingFinisher
	public void finishEncoding(OutputStream out) throws IOException {
		writeDouble(body.getTransform().getTranslationX(), out);
		writeDouble(body.getTransform().getTranslationY(), out);
		writeDouble(body.getTransform().getRotation(), out);
		writeDouble(body.getLinearVelocity().x, out);
		writeDouble(body.getLinearVelocity().y, out);
		writeDouble(body.getAngularVelocity(), out);
	}
	
	@FieldDecoder.DecodingFinisher
	public void finishDecoding(InputStream in) throws IOException {
		body.getTransform().setTranslationX(readDouble(in));
		body.getTransform().setTranslationY(readDouble(in));
		body.getTransform().setRotation(readDouble(in));
		body.getLinearVelocity().x = readDouble(in);
		body.getLinearVelocity().y = readDouble(in);
		body.setAngularVelocity(readDouble(in));
	}
	
}
