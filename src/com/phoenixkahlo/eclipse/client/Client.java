package com.phoenixkahlo.eclipse.client;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.phoenixkahlo.eclipse.world.WorldState;

/**
 * The game.
 */
public class Client extends StateBasedGame {

	public static void main(String[] args) {
		new Client().start();
	}
	
	public Client() {
		super("Eclipse");
	}
	
	public void start() {
		try {
			AppGameContainer container = new AppGameContainer(this, 1000, 900, false);
			container.setTargetFrameRate(WorldState.TICKS_PER_SECOND);
			container.setVSync(true);
			container.setShowFPS(false);
			container.start();
		} catch (SlickException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new MainMenu());
		
		enterState(ClientGameState.MAIN_MENU.ordinal());
	}

}
