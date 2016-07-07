package com.phoenixkahlo.eclipse.world.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.Entity;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.impl.Player;

public class PlayerUseEvent implements Consumer<WorldState> 	{

	private int id;
	
	public PlayerUseEvent() {}
	
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
