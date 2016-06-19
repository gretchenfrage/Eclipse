package com.phoenixkahlo.eclipse;

import java.net.Socket;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import com.phoenixkahlo.eclipse.world.ImageResource;

/**
 * The main menu GameState.
 */
public class MainMenuState extends BasicGameState {
	
	private static final int IP_FIELD_WIDTH = 500;
	private static final int IP_FIELD_HEIGHT = 50;
	private static final int BANNER_Y = 100;
	private static final String MESSAGE = "Enter Server IP and Port:";
	
	private Image background;
	private Image banner;
	private TextField ipField;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
		ImageResource.init();
		
		background = ImageResource.STARS_1.image();
		banner = ImageResource.BANNER_2.image();
		
		Font font = new TrueTypeFont(new java.awt.Font("monospace", java.awt.Font.PLAIN, 30), false);
		ipField = new TextField(container, font,
				container.getWidth() / 2 - IP_FIELD_WIDTH / 2, container.getHeight() / 2 - IP_FIELD_HEIGHT / 2,
				IP_FIELD_WIDTH, IP_FIELD_HEIGHT);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// Background
		for (int x = 0; x < container.getWidth(); x += background.getWidth()) {
			for (int y = 0; y < container.getHeight(); y += background.getHeight()) {
				g.drawImage(background, x, y);
			}
		}
		// Banner
		g.drawImage(
				banner,
				container.getWidth() / 2 - banner.getWidth() / 2,
				BANNER_Y);
		// "Enter server ip and port:"
		g.drawString(MESSAGE,
				container.getWidth() / 2 - g.getFont().getWidth(MESSAGE) / 2,
				container.getHeight() / 2 - IP_FIELD_HEIGHT);
		// Ip field
		ipField.render(container, g);
		g.setColor(new Color(0, 255, 251));
		g.drawRect(container.getWidth() / 2 - IP_FIELD_WIDTH / 2, container.getHeight() / 2 - IP_FIELD_HEIGHT / 2,
				IP_FIELD_WIDTH, IP_FIELD_HEIGHT);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE))
			container.exit();
		if (container.getInput().isKeyPressed(Input.KEY_ENTER))
			connect(game);
	}
	
	private void connect(StateBasedGame game) {
		System.out.println("Attempting to connect to \"" + ipField.getText() + "\"");
		try {
			String address = ipField.getText().split(":")[0];
			int port = Integer.parseInt(ipField.getText().split(":")[1]);
			Socket socket = new Socket(address, port);
			GameState state = new ClientConnectionState(socket);
			game.addState(state);
			game.enterState(EclipseGameState.CLIENT_CONNECTION.ordinal());
		} catch (Exception e) {
			System.out.println("Failed to connect because of:");
			e.printStackTrace(System.out);
		}
	}

	@Override
	public int getID() {
		return EclipseGameState.MAIN_MENU.ordinal();
	}

}
