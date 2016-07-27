package com.phoenixkahlo.testing.physics;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.phoenixkahlo.physics.PhysicsBox;
import com.phoenixkahlo.physics.Rigid;
import com.phoenixkahlo.physics.Vector2f;
import com.phoenixkahlo.physics.Convex;

public class PhysicsTest extends BasicGame {

	public static void main(String[] args) throws SlickException {
		AppGameContainer container = new AppGameContainer(new PhysicsTest(), 700, 700, false);
		container.setShowFPS(false);
		container.setTargetFrameRate(60);
		container.setMinimumLogicUpdateInterval(1000 / 60);
		container.start();
	}
	
	private PhysicsBox box = new PhysicsBox();
	
	public PhysicsTest() {
		super("Physics Test");
	}
	
	@Override
	public void init(GameContainer container) throws SlickException {
		
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		for (Rigid rigid : box.getRigids()) {
			for (Convex convex : rigid.getShape().getConvexes()) {
				for (Vector2f vertex : convex.getVertices()) {
					g.fill
				}
			}
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		
	}

}
