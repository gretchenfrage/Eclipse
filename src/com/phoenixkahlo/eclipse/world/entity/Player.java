package com.phoenixkahlo.eclipse.world.entity;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.phoenixkahlo.eclipse.world.BasicPerspective;
import com.phoenixkahlo.eclipse.world.ImageResource;
import com.phoenixkahlo.eclipse.world.Perspective;
import com.phoenixkahlo.eclipse.world.WalkingEntity;

public class Player extends WalkingEntity {
	
	private transient BasicPerspective perspective = new BasicPerspective();
	private int color = (int) (Math.random() * (0xFFFFFF + 1));

	public Player() {
		if (ImageResource.BALL_2.image() != null)
			injectTexture(ImageResource.BALL_2.image(), 1, 1, 0);
		addBodyFixture(new BodyFixture(new Circle(0.5)));
		getBody().setMass(MassType.NORMAL);
		setWalkSpeed(10);
		setThrustForce(10);
		setCanThrust(true);
		setRunningMultiplier(1.6F);
		setSprintThrustingMultiplier(2);
		
		perspective.setScale(25);
	}
	
	@Override
	public void postTick() {
		super.postTick();
		Vector2 position = getBody().getWorldPoint(new Vector2(0, 0));
		perspective.setX((float) position.x);
		perspective.setY((float) position.y);
	}

	@Override
	public Perspective getPerspective() {
		return perspective;
	}
	
	@Override
	public void render(Graphics g) {
		Color beforeColor = g.getColor();
		g.setColor(new Color(color));
		super.render(g);
		g.setColor(beforeColor);
	}
	
}
