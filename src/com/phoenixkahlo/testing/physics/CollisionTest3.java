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
import com.phoenixkahlo.utils.MathUtils;

public class CollisionTest3 extends BasicGame {

	public static void main(String[] args) throws SlickException {
		AppGameContainer container = new AppGameContainer(new CollisionTest3(), 700, 700, false);
		container.setShowFPS(false);
		container.setTargetFrameRate(60);
		container.setMinimumLogicUpdateInterval(1000 / 60);
		container.start();
	}
	
	private PhysicsBox box = new PhysicsBox();
	
	public CollisionTest3() {
		super("Physics Test");
	}
	
	@Override
	public void init(GameContainer container) throws SlickException {
		Rigid r1 = new Rigid(new Polygon(new Convex(
				0, -5,
				0, 5,
				1, 5,
				1, -5
				)));
		box.addRigid(r1);
		r1.setLocation(new Vector2f(10, 0));
		
		Rigid r2 = new Rigid(new Polygon(new Convex(
				0, -5,
				0, 5,
				1, 5,
				1, -5
				)));
		box.addRigid(r2);
		r2.setLocation(new Vector2f(-7, -3));
		r2.setRotation(MathUtils.PI_F / 2);
		r2.applyForce(new Vector2f(0.5F, 0));
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.translate(container.getWidth() / 2, container.getHeight() / 2);
		g.scale(20, 20);
		for (Rigid rigid : box.getRigids()) {
			g.translate(rigid.getLocation().x, rigid.getLocation().y);
			g.rotate(0, 0, (float) Math.toDegrees(rigid.getRotation()));
			for (Convex convex : rigid.getShape().getConvexes()) {
				g.fill(convex.toSlickShape());
			}
			g.rotate(0, 0, (float) -Math.toDegrees(rigid.getRotation()));
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
