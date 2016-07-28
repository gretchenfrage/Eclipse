package com.phoenixkahlo.testing.physics;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.phoenixkahlo.physics.Convex;
import com.phoenixkahlo.physics.PhysicsBox;
import com.phoenixkahlo.physics.Polygon;
import com.phoenixkahlo.physics.Rigid;
import com.phoenixkahlo.physics.Vector2f;

public class CollisionTest extends BasicGame {

	public static void main(String[] args) throws SlickException {
		AppGameContainer container = new AppGameContainer(new CollisionTest(), 700, 700, false);
		container.setShowFPS(false);
		container.setTargetFrameRate(60);
		container.setMinimumLogicUpdateInterval(1000 / 60);
		container.start();
	}
	
	private PhysicsBox box = new PhysicsBox();
	
	public CollisionTest() {
		super("Physics Test");
	}
	
	@Override
	public void init(GameContainer container) throws SlickException {
		Rigid rigid = new Rigid(new Polygon(
				new Vector2f(-1, -1),
				new Vector2f(1, -1),
				new Vector2f(0, 1)
				));
		box.addRigid(rigid);
		rigid.applyForce(new Vector2f(0.1F, 0), new Vector2f(-8, -8));
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.translate(container.getWidth() / 2, container.getHeight() / 2);
		g.scale(20, 20);
		for (Rigid rigid : box.getRigids()) {
			g.translate(rigid.getLocation().x, rigid.getLocation().y);
			g.rotate(0, 0, rigid.getAngle());
			for (Convex convex : rigid.getShape().getConvexes()) {
				g.fill(convex.toSlickShape());
			}
			g.rotate(0, 0, rigid.getAngle());
			g.translate(-rigid.getLocation().x, -rigid.getLocation().y);
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE))
			container.exit();
		
		box.update();
	}

}
