package com.phoenixkahlo.eclipse.world.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.ImageResource;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.weapon.Weapon;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;
import com.phoenixkahlo.networking.SerializationUtils;
import com.phoenixkahlo.utils.MathUtils;

/**
 * It's you!
 */
public class Player extends WalkingEntity {
	
	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(Player.class, Player::new, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(Player.class, Player::new, subDecoder);
	}
	
	private double facingAngle;
	private Weapon weapon; // Nullable

	public Player() {
		injectTexture(ImageResource.HUMAN_2.image(), 1);
		setWalkSpeed(10);
		setThrustForce(0.25F);
		setCanThrust(true);
		setSprintWalkingMultiplier(2);
		setSprintThrustingMultiplier(2);
		setBaseRenderAngle(-MathUtils.PI_F / 2);
		createBody();
	}
	
	public void setFacingAngle(double facingAngle) {
		this.facingAngle = facingAngle;
	}
	
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	
	public void useWeapon(WorldState state, Vector2 target) {
		weapon.use(state, target, this);
	}
	
	@Override
	protected double getRenderAngle() {
		return getBaseRenderAngle() + facingAngle;
	}
	
	@Override
	public void finishEncoding(OutputStream out) throws IOException {
		super.finishEncoding(out);
		
		SerializationUtils.writeFloat(getBaseRenderAngle(), out);
	}
	
	@Override
	public void finishDecoding(InputStream in) throws IOException {
		super.finishDecoding(in);
		
		setBaseRenderAngle(SerializationUtils.readFloat(in));
	}

	@Override
	protected void setupBody(Body body) {
		body.addFixture(new Circle(0.5));
		body.setMass(MassType.FIXED_ANGULAR_VELOCITY);
	}
	
}
