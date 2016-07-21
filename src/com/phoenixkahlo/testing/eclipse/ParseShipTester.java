package com.phoenixkahlo.testing.eclipse;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.phoenixkahlo.eclipse.ImageResource;
import com.phoenixkahlo.eclipse.world.entity.Entity;
import com.phoenixkahlo.eclipse.world.entity.FileResource;
import com.phoenixkahlo.eclipse.world.entity.ParsedShip;

public class ParseShipTester extends BasicGame {

	public static void main(String[] args) throws SlickException {
		AppGameContainer container = new AppGameContainer(new ParseShipTester(), 700, 700, false);
		container.start();
	}
	
	private Entity ship;
	
	public ParseShipTester() { super(""); }

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.translate(container.getWidth() / 2, container.getHeight() / 2);
		g.scale(20, 20);
		ship.render(g);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		ImageResource.init();
		ship = new ParsedShip(FileResource.BASIC_SHIP_1);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {}
	
}
