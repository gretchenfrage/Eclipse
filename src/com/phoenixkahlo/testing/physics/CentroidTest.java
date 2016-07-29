package com.phoenixkahlo.testing.physics;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.phoenixkahlo.physics.Convex;
import com.phoenixkahlo.physics.Vector2f;

public class CentroidTest extends BasicGame {

	public static void main(String[] args) throws SlickException {
		AppGameContainer container = new AppGameContainer(new CentroidTest(), 700, 700, false);
		container.setShowFPS(false);
		container.setTargetFrameRate(60);
		container.setMinimumLogicUpdateInterval(1000 / 60);
		container.start();
	}
	
	public CentroidTest() {
		super("Physics Test");
	}
	
	private Convex convex;
	private Vector2f translate = new Vector2f(1, 3);
	private float rotate = 1;
	private Vector2f centroid;
	
	@Override
	public void init(GameContainer container) throws SlickException {
		/*
		convex = new Convex(
				new Vector2f(1, -1),
				new Vector2f(1, 1),
				new Vector2f(0, 2),
				new Vector2f(-2, 2),
				new Vector2f(-3, 1),
				new Vector2f(-3, -1),
				new Vector2f(-2, -2),
				new Vector2f(0, -2)
				);
				*/
		convex = new Convex(
				-3, 5,
				4, 4,
				0, -7
				);
		convex.cacheTransform(translate, rotate);
		centroid = convex.centroid();
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.translate(container.getWidth() / 2, container.getHeight() / 2);
		g.scale(30, 30);
		
		g.setColor(Color.blue);
		g.translate(translate.x, translate.y);
		g.rotate(0, 0, (float) Math.toDegrees(rotate));
		g.fill(convex.toSlickShape());
		g.rotate(0, 0, (float) -Math.toDegrees(rotate));
		g.translate(-translate.x, -translate.y);
		
		g.setColor(Color.red);
		g.fillOval(centroid.x - 0.1F, centroid.y - 0.1F, 0.2F, 0.2F);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		
	}

}
