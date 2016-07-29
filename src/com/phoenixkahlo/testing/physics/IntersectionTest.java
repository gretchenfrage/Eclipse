package com.phoenixkahlo.testing.physics;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.phoenixkahlo.physics.Convex;
import com.phoenixkahlo.physics.Vector2f;

public class IntersectionTest extends BasicGame {

	public static void main(String[] args) throws SlickException {
		AppGameContainer container = new AppGameContainer(new IntersectionTest(), 700, 700, false);
		//container.setShowFPS(false);
		//container.setTargetFrameRate(60);
		//container.setMinimumLogicUpdateInterval(1000 / 60);
		container.start();
	}
	
	public IntersectionTest() {
		super("Physics Test");
	}
	
	private Vector2f c1Translation = new Vector2f(0.5F, 0.5F);
	private float c1Rotation = 1;
	private Convex c1;
	private Vector2f c2Translation = new Vector2f(0, 0.3F);
	private float c2Rotation = 7;
	private Convex c2;
	
	private Convex intersection;
	
	@Override
	public void init(GameContainer container) throws SlickException {
		update(container, 0);
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		c1 = new Convex(
				new Vector2f(1, -1),
				new Vector2f(5, 1),
				new Vector2f(0, 2),
				new Vector2f(-2, 2),
				new Vector2f(-3, 1),
				new Vector2f(-3, -1),
				new Vector2f(-2, -2),
				new Vector2f(0, -2)
				);
		c2 = new Convex(
				new Vector2f(-1, -1),
				new Vector2f(0, -2),
				new Vector2f(2, -2),
				new Vector2f(3.5F, -1),
				new Vector2f(3, 1),
				new Vector2f(2, 3),
				new Vector2f(0, 2),
				new Vector2f(-1, 1)
				);
		
		c1.cacheTransform(c1Translation, c1Rotation);
		c2.cacheTransform(c2Translation, c2Rotation);
		
		intersection = c1.intersection(c2);
		
		g.translate(container.getWidth() / 2, container.getHeight() / 2);
		g.scale(30, 30);
		
		g.setColor(Color.red);
		g.translate(c1Translation.x, c1Translation.y);
		g.rotate(0, 0, (float) Math.toDegrees(c1Rotation));
		g.fill(c1.toSlickShape());
		g.rotate(0, 0, -(float) Math.toDegrees(c1Rotation));
		g.translate(-c1Translation.x, -c1Translation.y);
		
		g.setColor(Color.green);
		g.translate(c2Translation.x, c2Translation.y);
		g.rotate(0, 0, (float) Math.toDegrees(c2Rotation));
		g.fill(c2.toSlickShape());
		g.rotate(0, 0, -(float) Math.toDegrees(c2Rotation));
		g.translate(-c2Translation.x, -c2Translation.y);
		
		g.setColor(Color.magenta);
		if (intersection != null)
			g.fill(intersection.toSlickShape());
		else
			System.out.println("intersection is null");
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		
	}

}
