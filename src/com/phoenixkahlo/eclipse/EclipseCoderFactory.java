package com.phoenixkahlo.eclipse;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.client.ClientWalkingHandlerCreator;
import com.phoenixkahlo.eclipse.world.BasicPerspective;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.event.EntityAdditionEvent;
import com.phoenixkahlo.eclipse.world.event.EntityDeletionEvent;
import com.phoenixkahlo.eclipse.world.event.SetBackgroundEvent;
import com.phoenixkahlo.eclipse.world.event.SetPlayerFacingAngleEvent;
import com.phoenixkahlo.eclipse.world.event.SetVelocityEvent;
import com.phoenixkahlo.eclipse.world.event.SetWalkingEntityDirectionEvent;
import com.phoenixkahlo.eclipse.world.event.SetWalkingEntitySprintingEvent;
import com.phoenixkahlo.eclipse.world.impl.Ball;
import com.phoenixkahlo.eclipse.world.impl.BasicShip1;
import com.phoenixkahlo.eclipse.world.impl.Player;
import com.phoenixkahlo.eclipse.world.impl.SpaceBackground;
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

public class EclipseCoderFactory {

	private EclipseCoderFactory() {}
	
	public static final EncodingProtocol ENCODER = makeEncoder();
	public static final DecodingProtocol DECODER = makeDecoder();
	
	public static EncodingProtocol makeEncoder() {
		UnionEncoder encoder = new UnionEncoder();
		encoder.registerProtocol(CodableType.ARRAY_LIST.ordinal(),
				new ArrayListEncoder(encoder));
		encoder.registerProtocol(CodableType.WORLD_STATE.ordinal(),
				new FieldEncoder(WorldState.class, encoder));
		encoder.registerProtocol(CodableType.BALL.ordinal(),
				new FieldEncoder(Ball.class, encoder));
		encoder.registerProtocol(CodableType.ENTITY_ADDITION_EVENT.ordinal(),
				new FieldEncoder(EntityAdditionEvent.class, encoder));
		encoder.registerProtocol(CodableType.VECTOR_2.ordinal(),
				new FieldEncoder(Vector2.class, encoder));
		encoder.registerProtocol(CodableType.SET_VELOCITY_EVENT.ordinal(), 
				new FieldEncoder(SetVelocityEvent.class, encoder));
		encoder.registerProtocol(CodableType.PLAYER.ordinal(), 
				new FieldEncoder(Player.class, encoder));
		encoder.registerProtocol(CodableType.BYTE_ARRAY.ordinal(),
				new ArrayEncoder(byte.class));
		encoder.registerProtocol(CodableType.ENTITY_DELETION_EVENT.ordinal(),
				new FieldEncoder(EntityDeletionEvent.class, encoder));
		encoder.registerProtocol(CodableType.SPACE_BACKGROUND.ordinal(),
				new FieldEncoder(SpaceBackground.class, encoder));
		encoder.registerProtocol(CodableType.SET_BACKGROUND_EVENT.ordinal(),
				new FieldEncoder(SetBackgroundEvent.class, encoder));
		encoder.registerProtocol(CodableType.SET_WALKING_ENTITY_DIRECTION_EVENT.ordinal(),
				new FieldEncoder(SetWalkingEntityDirectionEvent.class, encoder));
		encoder.registerProtocol(CodableType.SET_WALKING_ENTITY_IS_SPRINTING_EVENT.ordinal(), 
				new FieldEncoder(SetWalkingEntitySprintingEvent.class, encoder));
		encoder.registerProtocol(CodableType.BASIC_PERSPECTIVE.ordinal(), 
				new FieldEncoder(BasicPerspective.class, encoder));
		encoder.registerProtocol(CodableType.BASIC_SHIP_1.ordinal(),
				new FieldEncoder(BasicShip1.class, encoder));
		encoder.registerProtocol(CodableType.CLIENT_WALKING_HANDLER_CREATOR.ordinal(), 
				new FieldEncoder(ClientWalkingHandlerCreator.class, encoder));
		encoder.registerProtocol(CodableType.SET_PLAYER_FACING_ANGLE_EVENT.ordinal(), 
				new FieldEncoder(SetPlayerFacingAngleEvent.class, encoder));
		return encoder;
	}
	
	public static DecodingProtocol makeDecoder() {
		UnionDecoder decoder = new UnionDecoder();
		decoder.registerProtocol(CodableType.ARRAY_LIST.ordinal(),
				new ArrayListDecoder(decoder));
		decoder.registerProtocol(CodableType.WORLD_STATE.ordinal(),
				new FieldDecoder(WorldState.class, WorldState::new, decoder));
		decoder.registerProtocol(CodableType.BALL.ordinal(),
				new FieldDecoder(Ball.class, Ball::new, decoder));
		decoder.registerProtocol(CodableType.ENTITY_ADDITION_EVENT.ordinal(),
				new FieldDecoder(EntityAdditionEvent.class, EntityAdditionEvent::new, decoder));
		decoder.registerProtocol(CodableType.VECTOR_2.ordinal(),
				new FieldDecoder(Vector2.class, Vector2::new, decoder));
		decoder.registerProtocol(CodableType.SET_VELOCITY_EVENT.ordinal(),
				new FieldDecoder(SetVelocityEvent.class, SetVelocityEvent::new, decoder));
		decoder.registerProtocol(CodableType.PLAYER.ordinal(), 
				new FieldDecoder(Player.class, Player::new, decoder));
		decoder.registerProtocol(CodableType.BYTE_ARRAY.ordinal(),
				new ArrayDecoder(byte.class));
		decoder.registerProtocol(CodableType.ENTITY_DELETION_EVENT.ordinal(),
				new FieldDecoder(EntityDeletionEvent.class, EntityDeletionEvent::new, decoder));
		decoder.registerProtocol(CodableType.SPACE_BACKGROUND.ordinal(),
				new FieldDecoder(SpaceBackground.class, SpaceBackground::new, decoder));
		decoder.registerProtocol(CodableType.SET_BACKGROUND_EVENT.ordinal(),
				new FieldDecoder(SetBackgroundEvent.class, SetBackgroundEvent::new, decoder));
		decoder.registerProtocol(CodableType.SET_WALKING_ENTITY_DIRECTION_EVENT.ordinal(), 
				new FieldDecoder(SetWalkingEntityDirectionEvent.class, SetWalkingEntityDirectionEvent::new, decoder));
		decoder.registerProtocol(CodableType.SET_WALKING_ENTITY_IS_SPRINTING_EVENT.ordinal(), 
				new FieldDecoder(SetWalkingEntitySprintingEvent.class, SetWalkingEntitySprintingEvent::new, decoder));
		decoder.registerProtocol(CodableType.BASIC_PERSPECTIVE.ordinal(), 
				new FieldDecoder(BasicPerspective.class, BasicPerspective::new, decoder));
		decoder.registerProtocol(CodableType.BASIC_SHIP_1.ordinal(),
				new FieldDecoder(BasicShip1.class, BasicShip1::new, decoder));
		decoder.registerProtocol(CodableType.CLIENT_WALKING_HANDLER_CREATOR.ordinal(),
				new FieldDecoder(ClientWalkingHandlerCreator.class, ClientWalkingHandlerCreator::new, decoder));
		decoder.registerProtocol(CodableType.SET_PLAYER_FACING_ANGLE_EVENT.ordinal(),
				new FieldDecoder(SetPlayerFacingAngleEvent.class, SetPlayerFacingAngleEvent::new, decoder));	
		return decoder;
	}
	
}
