package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.entity.Entity;
import com.phoenixkahlo.eclipse.world.entity.Player;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class PlayerUseEvent implements Consumer<WorldState> 	{

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(PlayerUseEvent.class, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(PlayerUseEvent.class, PlayerUseEvent::new, subDecoder);
	}
	
	private int id;
	
	private PlayerUseEvent() {}
	
	public PlayerUseEvent(int id) {
		this.id = id;
	}
	
	@Override
	public void accept(WorldState state) {
		try {
			Player player = (Player) state.getEntity(id);
			for (Entity entity : state.getEntities()) {
				Consumer<Player> useable = entity.getUseable(player.getBody().getWorldCenter());
				if (useable != null) {
					useable.accept(player);
					return;
				}
			}
		} catch (ClassCastException | NullPointerException e) {
			e.printStackTrace();
		}
	}
	
}
