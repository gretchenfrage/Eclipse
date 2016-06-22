package com.phoenixkahlo.eclipse.client;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

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
			container.setTargetFrameRate(60);
			container.setMinimumLogicUpdateInterval(1000 / 60);
			container.setMaximumLogicUpdateInterval(1000 / 60);
			container.setVSync(true);
			container.setShowFPS(false);
			container.start();
		} catch (SlickException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new MainMenuState());
		
		enterState(EclipseGameState.MAIN_MENU.ordinal());
	}

}
