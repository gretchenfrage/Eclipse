package com.phoenixkahlo.eclipse;

import java.util.function.Function;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.client.ClientDrivingHandlerCreator;
import com.phoenixkahlo.eclipse.client.ClientWalkingHandlerCreator;
import com.phoenixkahlo.eclipse.world.BasicPerspective;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.WorldStateContinuum;
import com.phoenixkahlo.eclipse.world.entity.Ball;
import com.phoenixkahlo.eclipse.world.entity.BasicShip1;
import com.phoenixkahlo.eclipse.world.entity.Dummy;
import com.phoenixkahlo.eclipse.world.entity.Entity;
import com.phoenixkahlo.eclipse.world.entity.ParsedShip;
import com.phoenixkahlo.eclipse.world.entity.Player;
import com.phoenixkahlo.eclipse.world.entity.RelativeLocationLock;
import com.phoenixkahlo.eclipse.world.entity.RelativePlayerFacingAngleLock;
import com.phoenixkahlo.eclipse.world.entity.SpaceBackground;
import com.phoenixkahlo.eclipse.world.event.AddEntityEvent;
import com.phoenixkahlo.eclipse.world.event.PlayerSetWeaponEvent;
import com.phoenixkahlo.eclipse.world.event.PlayerUseEvent;
import com.phoenixkahlo.eclipse.world.event.PlayerUseWeaponEvent;
import com.phoenixkahlo.eclipse.world.event.RemoveEntityEvent;
import com.phoenixkahlo.eclipse.world.event.SetBackgroundEvent;
import com.phoenixkahlo.eclipse.world.event.SetEntitiesEvent;
import com.phoenixkahlo.eclipse.world.event.SetPlayerFacingAngleEvent;
import com.phoenixkahlo.eclipse.world.event.SetShipAngularThrustEvent;
import com.phoenixkahlo.eclipse.world.event.SetShipLinearThrustEvent;
import com.phoenixkahlo.eclipse.world.event.SetShipPilotedEvent;
import com.phoenixkahlo.eclipse.world.event.SetVelocityEvent;
import com.phoenixkahlo.eclipse.world.event.SetWalkingEntityDirectionEvent;
import com.phoenixkahlo.eclipse.world.event.SetWalkingEntitySprintingEvent;
import com.phoenixkahlo.eclipse.world.weapon.Pistol;
import com.phoenixkahlo.networking.ArrayEncoder;
import com.phoenixkahlo.networking.ArrayListEncoder;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldEncoder;
import com.phoenixkahlo.networking.HashMapEncoder;
import com.phoenixkahlo.networking.UnionDecoder;
import com.phoenixkahlo.networking.UnionEncoder;
import com.phoenixkahlo.utils.CheckSum;

/**
 * A class of ~evil static state~ providing the networking protocol. 
 * My justification for this is that it's basically a static utility class but 
 * with a neat object oriented way of configuring it. 
 */
public class EclipseCodingProtocol {

	private EclipseCodingProtocol() {}
	
	private static UnionEncoder encoder = new UnionEncoder();
	private static DecodingProtocol decoder = new UnionDecoder();
	private static int id = 0;
	
	private static void register(EncodingProtocol encoder) {
		EclipseCodingProtocol.encoder.registerProtocol(id, encoder);
		id++;
	}
	
	private static void mkreg(Function<EncodingProtocol, EncodingProtocol> function) {
		register(function.apply(encoder));
	}
	
	private static void finish() {
		decoder = encoder.toDecoder();
	}
	
	public static EncodingProtocol getEncoder() {
		return encoder;
	}
	
	public static DecodingProtocol getDecoder() {
		return decoder;
	}
	
	static {
		register(new ArrayEncoder(byte.class));
		register(new ArrayEncoder(int.class));
		register(new ArrayEncoder(Entity.class, encoder));
		mkreg(ArrayListEncoder::new);
		register(new HashMapEncoder(encoder, encoder));
		mkreg(ClientDrivingHandlerCreator::makeEncoder);
		mkreg(ClientWalkingHandlerCreator::makeEncoder);
		mkreg(BasicPerspective::makeEncoder);
		mkreg(WorldState::makeEncoder);
		mkreg(AddEntityEvent::makeEncoder);
		mkreg(RemoveEntityEvent::makeEncoder);
		mkreg(PlayerUseEvent::makeEncoder);
		mkreg(SetBackgroundEvent::makeEncoder);
		mkreg(SetPlayerFacingAngleEvent::makeEncoder);
		mkreg(SetShipAngularThrustEvent::makeEncoder);
		mkreg(SetShipLinearThrustEvent::makeEncoder);
		mkreg(SetShipPilotedEvent::makeEncoder);
		mkreg(SetVelocityEvent::makeEncoder);
		mkreg(SetWalkingEntityDirectionEvent::makeEncoder);
		mkreg(SetWalkingEntitySprintingEvent::makeEncoder);
		mkreg(Ball::makeEncoder);
		mkreg(BasicShip1::makeEncoder);
		mkreg(Player::makeEncoder);
		mkreg(RelativeLocationLock::makeEncoder);
		mkreg(RelativePlayerFacingAngleLock::makeEncoder);
		mkreg(SpaceBackground::makeEncoder);
		mkreg(Pistol::makeEncoder);
		mkreg(PlayerSetWeaponEvent::makeEncoder);
		mkreg(PlayerUseWeaponEvent::makeEncoder);
		mkreg(Dummy::makeEncoder);
		mkreg(ParsedShip::makeEncoder);
		mkreg(WorldStateContinuum::makeEncoder);
		mkreg(CheckSum::makeEncoder);
		mkreg(SetEntitiesEvent::makeEncoder);
		register(new FieldEncoder(Vector2.class, Vector2::new));
		finish();
	}
	
}
