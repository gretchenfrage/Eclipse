package com.phoenixkahlo.eclipse.world;

import static com.phoenixkahlo.networking.SerializationUtils.readDouble;
import static com.phoenixkahlo.networking.SerializationUtils.writeDouble;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;

import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

/**
 * An entity with a body.
 */
public abstract class BodyEntity extends EntityAdapter {

	protected static final Random RANDOM = new Random();
	
	private transient Body body = new Body();
	private int id;
	
	public BodyEntity(int id) {
		this.id = id;
	}
	
	public BodyEntity() {
		this(RANDOM.nextInt(Integer.MAX_VALUE));
	}

	protected void addBodyFixture(BodyFixture fixture) {
		body.addFixture(fixture);
	}
	
	protected void addConvexFixture(Convex convex) {
		addBodyFixture(new BodyFixture(convex));
	}
	
	@Override
	public Body getBody() {
		return body;
	}
	
	@Override
	public int getID() {
		return id;
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
