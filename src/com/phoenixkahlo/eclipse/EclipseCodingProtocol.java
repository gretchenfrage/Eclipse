package com.phoenixkahlo.eclipse;

import java.util.function.Function;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.client.ClientDrivingHandlerCreator;
import com.phoenixkahlo.eclipse.client.ClientWalkingHandlerCreator;
import com.phoenixkahlo.eclipse.world.BasicPerspective;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.entity.Ball;
import com.phoenixkahlo.eclipse.world.entity.BasicShip1;
import com.phoenixkahlo.eclipse.world.entity.Dummy;
import com.phoenixkahlo.eclipse.world.entity.ParsedShip;
import com.phoenixkahlo.eclipse.world.entity.Player;
import com.phoenixkahlo.eclipse.world.entity.RelativeLocationLock;
import com.phoenixkahlo.eclipse.world.entity.RelativePlayerFacingAngleLock;
import com.phoenixkahlo.eclipse.world.entity.SpaceBackground;
import com.phoenixkahlo.eclipse.world.event.EntityAdditionEvent;
import com.phoenixkahlo.eclipse.world.event.EntityDeletionEvent;
import com.phoenixkahlo.eclipse.world.event.PlayerSetWeaponEvent;
import com.phoenixkahlo.eclipse.world.event.PlayerUseEvent;
import com.phoenixkahlo.eclipse.world.event.PlayerUseWeaponEvent;
import com.phoenixkahlo.eclipse.world.event.SetBackgroundEvent;
import com.phoenixkahlo.eclipse.world.event.SetPlayerFacingAngleEvent;
import com.phoenixkahlo.eclipse.world.event.SetShipAngularThrustEvent;
import com.phoenixkahlo.eclipse.world.event.SetShipLinearThrustEvent;
import com.phoenixkahlo.eclipse.world.event.SetShipPilotedEvent;
import com.phoenixkahlo.eclipse.world.event.SetVelocityEvent;
import com.phoenixkahlo.eclipse.world.event.SetWalkingEntityDirectionEvent;
import com.phoenixkahlo.eclipse.world.event.SetWalkingEntitySprintingEvent;
import com.phoenixkahlo.eclipse.world.weapon.Pistol;
import com.phoenixkahlo.networking.ArrayDecoder;
import com.phoenixkahlo.networking.ArrayEncoder;
import com.phoenixkahlo.networking.ArrayListDecoder;
import com.phoenixkahlo.networking.ArrayListEncoder;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;
import com.phoenixkahlo.networking.UnionDecoder;
import com.phoenixkahlo.networking.UnionEncoder;
import com.phoenixkahlo.utils.ReflectionUtils;

/**
 * A class of ~evil static state~ providing the networking protocol. 
 * My justification for this is that it's basically a static utility class but 
 * with a neat object oriented way of configuring it. 
 */
public class EclipseCodingProtocol {

	private EclipseCodingProtocol() {}
	
	private static UnionEncoder union = new UnionEncoder();
	
	public static final UnionEncoder ENCODER = union;
	public static final DecodingProtocol DECODER = null;
	
	private static int id = 0;
	
	private static void register(EncodingProtocol encoder) {
		ENCODER.registerProtocol(id, encoder);
		id++;
	}
	
	private static void mkreg(Function<EncodingProtocol, EncodingProtocol> function) {
		register(function.apply(ENCODER));
	}
	
	private static void finish() {
		ReflectionUtils.setConstant(EclipseCodingProtocol.class, "DECODER", ENCODER.toDecoder());
	}
	
	/*
	private static void register(EncodingProtocol encoder, DecodingProtocol decoder) {
		ENCODER.registerProtocol(id, encoder);
		DECODER.registerProtocol(id, decoder);
		id++;
	}
	
	private static void mkreg(Function<EncodingProtocol, EncodingProtocol> encoderCreator,
			Function<DecodingProtocol, DecodingProtocol> decoderCreator) {
		register(encoderCreator.apply(ENCODER), decoderCreator.apply(DECODER));
	}
	*/
	static {
		register(new ArrayEncoder(byte.class), new ArrayDecoder(byte.class));
		mkreg(ArrayListEncoder::new, ArrayListDecoder::new);
		mkreg(ClientDrivingHandlerCreator::makeEncoder, ClientDrivingHandlerCreator::makeDecoder);
		mkreg(ClientWalkingHandlerCreator::makeEncoder, ClientWalkingHandlerCreator::makeDecoder);
		mkreg(BasicPerspective::makeEncoder, BasicPerspective::makeDecoder);
		mkreg(WorldState::makeEncoder, WorldState::makeDecoder);
		mkreg(EntityAdditionEvent::makeEncoder, EntityAdditionEvent::makeDecoder);
		mkreg(EntityDeletionEvent::makeEncoder, EntityDeletionEvent::makeDecoder);
		mkreg(PlayerUseEvent::makeEncoder, PlayerUseEvent::makeDecoder);
		mkreg(SetBackgroundEvent::makeEncoder, SetBackgroundEvent::makeDecoder);
		mkreg(SetPlayerFacingAngleEvent::makeEncoder, SetPlayerFacingAngleEvent::makeDecoder);
		mkreg(SetShipAngularThrustEvent::makeEncoder, SetShipAngularThrustEvent::makeDecoder);
		mkreg(SetShipLinearThrustEvent::makeEncoder, SetShipLinearThrustEvent::makeDecoder);
		mkreg(SetShipPilotedEvent::makeEncoder, SetShipPilotedEvent::makeDecoder);
		mkreg(SetVelocityEvent::makeEncoder, SetVelocityEvent::makeDecoder);
		mkreg(SetWalkingEntityDirectionEvent::makeEncoder, SetWalkingEntityDirectionEvent::makeDecoder);
		mkreg(SetWalkingEntitySprintingEvent::makeEncoder, SetWalkingEntitySprintingEvent::makeDecoder);
		mkreg(Ball::makeEncoder, Ball::makeDecoder);
		mkreg(BasicShip1::makeEncoder, BasicShip1::makeDecoder);
		mkreg(Player::makeEncoder, Player::makeDecoder);
		mkreg(RelativeLocationLock::makeEncoder, RelativeLocationLock::makeDecoder);
		mkreg(RelativePlayerFacingAngleLock::makeEncoder, RelativePlayerFacingAngleLock::makeDecoder);
		mkreg(SpaceBackground::makeEncoder, SpaceBackground::makeDecoder);
		mkreg(Pistol::makeEncoder, Pistol::makeDecoder);
		mkreg(PlayerSetWeaponEvent::makeEncoder, PlayerSetWeaponEvent::makeDecoder);
		mkreg(PlayerUseWeaponEvent::makeEncoder, PlayerUseWeaponEvent::makeDecoder);
		mkreg(Dummy::makeEncoder, Dummy::makeDecoder);
		mkreg(ParsedShip::makeEncoder, ParsedShip::makeDecoder);
		register(new FieldEncoder(Vector2.class, Vector2::new), new FieldDecoder(Vector2.class, Vector2::new));
		/*
		register(new ArrayEncoder(byte.class), new ArrayDecoder(byte.class));
		mkreg(ArrayListEncoder::new, ArrayListDecoder::new);
		mkreg(ClientDrivingHandlerCreator::makeEncoder, ClientDrivingHandlerCreator::makeDecoder);
		mkreg(ClientWalkingHandlerCreator::makeEncoder, ClientWalkingHandlerCreator::makeDecoder);
		mkreg(BasicPerspective::makeEncoder, BasicPerspective::makeDecoder);
		mkreg(WorldState::makeEncoder, WorldState::makeDecoder);
		mkreg(EntityAdditionEvent::makeEncoder, EntityAdditionEvent::makeDecoder);
		mkreg(EntityDeletionEvent::makeEncoder, EntityDeletionEvent::makeDecoder);
		mkreg(PlayerUseEvent::makeEncoder, PlayerUseEvent::makeDecoder);
		mkreg(SetBackgroundEvent::makeEncoder, SetBackgroundEvent::makeDecoder);
		mkreg(SetPlayerFacingAngleEvent::makeEncoder, SetPlayerFacingAngleEvent::makeDecoder);
		mkreg(SetShipAngularThrustEvent::makeEncoder, SetShipAngularThrustEvent::makeDecoder);
		mkreg(SetShipLinearThrustEvent::makeEncoder, SetShipLinearThrustEvent::makeDecoder);
		mkreg(SetShipPilotedEvent::makeEncoder, SetShipPilotedEvent::makeDecoder);
		mkreg(SetVelocityEvent::makeEncoder, SetVelocityEvent::makeDecoder);
		mkreg(SetWalkingEntityDirectionEvent::makeEncoder, SetWalkingEntityDirectionEvent::makeDecoder);
		mkreg(SetWalkingEntitySprintingEvent::makeEncoder, SetWalkingEntitySprintingEvent::makeDecoder);
		mkreg(Ball::makeEncoder, Ball::makeDecoder);
		mkreg(BasicShip1::makeEncoder, BasicShip1::makeDecoder);
		mkreg(Player::makeEncoder, Player::makeDecoder);
		mkreg(RelativeLocationLock::makeEncoder, RelativeLocationLock::makeDecoder);
		mkreg(RelativePlayerFacingAngleLock::makeEncoder, RelativePlayerFacingAngleLock::makeDecoder);
		mkreg(SpaceBackground::makeEncoder, SpaceBackground::makeDecoder);
		mkreg(Pistol::makeEncoder, Pistol::makeDecoder);
		mkreg(PlayerSetWeaponEvent::makeEncoder, PlayerSetWeaponEvent::makeDecoder);
		mkreg(PlayerUseWeaponEvent::makeEncoder, PlayerUseWeaponEvent::makeDecoder);
		mkreg(Dummy::makeEncoder, Dummy::makeDecoder);
		mkreg(ParsedShip::makeEncoder, ParsedShip::makeDecoder);
		register(new FieldEncoder(Vector2.class, Vector2::new), new FieldDecoder(Vector2.class, Vector2::new));
		*/
	}
	
}
