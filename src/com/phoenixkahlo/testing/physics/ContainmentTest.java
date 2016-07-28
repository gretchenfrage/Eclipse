package com.phoenixkahlo.testing.physics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.phoenixkahlo.physics.Convex;
import com.phoenixkahlo.physics.Vector2f;

public class ContainmentTest extends BasicGame {

	public static void main(String[] args) throws SlickException {
		AppGameContainer container = new AppGameContainer(new ContainmentTest(), 700, 700, false);
		container.setShowFPS(false);
		container.setTargetFrameRate(60);
		container.setMinimumLogicUpdateInterval(1000 / 60);
		container.start();
	}
	
	public ContainmentTest() {
		super("Physics Test");
	}
	
	private Convex c1;
	private List<Vector2f> contained = new ArrayList<Vector2f>();
	private List<Vector2f> uncontained = new ArrayList<Vector2f>();
	
	@Override
	public void init(GameContainer container) throws SlickException {
		c1 = new Convex(
				new Vector2f(1, -1),
				new Vector2f(1, 1),
				new Vector2f(0, 2),
				new Vector2f(-2, 2),
				new Vector2f(-3, 1),
				new Vector2f(-3, -1),
				new Vector2f(-2, -2),
				new Vector2f(0, -2)
				);
		c1.cacheTransform(new Vector2f(0, 0), 0);
		
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			Vector2f point = new Vector2f(
					random.nextFloat() * 20 - 10, 
					random.nextFloat() * 20 - 10
					);
			if (c1.contains(point))
				contained.add(point);
			else
				uncontained.add(point);
		}
		
		Vector2f point = new Vector2f(-3, 2);
		if (c1.contains(point))
			contained.add(point);
		else
			uncontained.add(point);
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.translate(container.getWidth() / 2, container.getHeight() / 2);
		g.scale(30, 30);
		
		g.setColor(Color.blue);
		g.fill(c1.toSlickShape());
		
		g.setColor(Color.green);
		for (Vector2f point : contained) {
			g.fillOval(point.x - 0.1F, point.y - 0.1F, 0.2F, 0.2F);
		}
		
		g.setColor(Color.red);
		for (Vector2f point : uncontained) {
			g.fillOval(point.x - 0.1F, point.y - 0.1F, 0.2F, 0.2F);
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		
	}

}
