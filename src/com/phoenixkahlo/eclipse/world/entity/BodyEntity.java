package com.phoenixkahlo.eclipse.world.entity;

import static com.phoenixkahlo.networking.SerializationUtils.readDouble;
import static com.phoenixkahlo.networking.SerializationUtils.writeDouble;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Convex;

import com.phoenixkahlo.networking.DecodingFinisher;
import com.phoenixkahlo.networking.EncodingFinisher;

/**
 * An entity with a body.
 */
public abstract class BodyEntity extends BasicEntity implements EncodingFinisher, DecodingFinisher {
	
	private transient Body body = new Body();

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
	public void finishEncoding(OutputStream out) throws IOException {
		writeDouble(body.getTransform().getTranslationX(), out);
		writeDouble(body.getTransform().getTranslationY(), out);
		writeDouble(body.getTransform().getRotation(), out);
		writeDouble(body.getLinearVelocity().x, out);
		writeDouble(body.getLinearVelocity().y, out);
		writeDouble(body.getAngularVelocity(), out);
	}
	
	@Override
	public void finishDecoding(InputStream in) throws IOException {
		System.out.println("BodyEntity.finishDecoding() in " + this);
		body.getTransform().setTranslationX(readDouble(in));
		body.getTransform().setTranslationY(readDouble(in));
		body.getTransform().setRotation(readDouble(in));
		body.getLinearVelocity().x = readDouble(in);
		body.getLinearVelocity().y = readDouble(in);
		body.setAngularVelocity(readDouble(in));
	}
	
}
