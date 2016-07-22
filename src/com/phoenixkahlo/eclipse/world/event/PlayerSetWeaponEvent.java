package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.entity.Player;
import com.phoenixkahlo.eclipse.world.weapon.Weapon;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class PlayerSetWeaponEvent implements Consumer<WorldState> {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(PlayerSetWeaponEvent.class, PlayerSetWeaponEvent::new, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(PlayerSetWeaponEvent.class, PlayerSetWeaponEvent::new, subDecoder);
	}
	
	private int id;
	private Weapon weapon;
	
	private PlayerSetWeaponEvent() {}
	
	public PlayerSetWeaponEvent(int id, Weapon weapon) {
		this.id = id;
		this.weapon = weapon;
	}
	
	@Override
	public void accept(WorldState state) {
		((Player) state.getEntity(id)).setWeapon(weapon);
	}
	
}
