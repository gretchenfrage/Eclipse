package com.phoenixkahlo.eclipse.client;

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

import com.phoenixkahlo.eclipse.ImageResource;

/**
 * The main menu GameState.
 * This class is officially Very Messy and should be cleaned up.
 */
public class MainMenu extends BasicGameState {
	
	private static final Font IP_FIELD_FONT = new TrueTypeFont(new java.awt.Font("futura",
			java.awt.Font.PLAIN, 30), true);
	private static final Font LABEL_FONT = new TrueTypeFont(new java.awt.Font("futura",
			java.awt.Font.PLAIN, 15), true);
	private static final Font BANNER_FONT = new TrueTypeFont(new java.awt.Font("futura",
			java.awt.Font.PLAIN, 60), true);
	private static final int IP_FIELD_WIDTH = 500;
	private static final int IP_FIELD_HEIGHT = IP_FIELD_FONT.getHeight("|");
	private static final int BANNER_Y = 100;
	private static final String MESSAGE = "Enter Server IP and Port:";
	//private static final int BANNER_WIDTH = 500;
	
	private Image background;
	//private Image banner;
	private TextField ipField;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		ImageResource.init();
		
		background = ImageResource.STARS_1.image();
		
		ipField = new TextField(container, IP_FIELD_FONT,
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
		String banner = "E  C  L  I  P  S  E";
		g.setColor(new Color(0, 255, 251));
		g.setFont(BANNER_FONT);
		g.drawString(
				banner,
				container.getWidth() / 2 - BANNER_FONT.getWidth(banner) / 2,
				BANNER_Y);
		g.setFont(LABEL_FONT);
		g.drawString(MESSAGE,
				container.getWidth() / 2 - LABEL_FONT.getWidth(MESSAGE) / 2,
				container.getHeight() / 2 - ipField.getHeight() / 2 - LABEL_FONT.getHeight(MESSAGE));
		// Ip field
		ipField.render(container, g);
		g.drawRect(container.getWidth() / 2 - IP_FIELD_WIDTH / 2, container.getHeight() / 2 - IP_FIELD_HEIGHT / 2,
				IP_FIELD_WIDTH, IP_FIELD_HEIGHT);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		ipField.setText("");
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
			GameState state = new ServerConnection(socket, game);
			game.addState(state);
			game.enterState(ClientGameState.SERVER_CONNECTION.ordinal());
		} catch (Exception e) {
			System.out.println("Failed to connect because of:");
			e.printStackTrace(System.out);
		}
	}

	@Override
	public int getID() {
		return ClientGameState.MAIN_MENU.ordinal();
	}

}
