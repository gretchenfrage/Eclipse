package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.entity.Player;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class PlayerUseWeaponEvent implements Consumer<WorldState> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(PlayerUseWeaponEvent.class, PlayerUseWeaponEvent::new, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(PlayerUseWeaponEvent.class, PlayerUseWeaponEvent::new, subDecoder);
	}
	
	private int id;
	private Vector2 target;
	
	private PlayerUseWeaponEvent() {}
	
	public PlayerUseWeaponEvent(int id, Vector2 target) {
		this.id = id;
		this.target = target;
	}
	
	@Override
	public void accept(WorldState state) {
		((Player) state.getEntity(id)).useWeapon(state, target);
	}
	
}
