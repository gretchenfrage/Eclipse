package com.phoenixkahlo.eclipse.world;

import org.dyn4j.dynamics.Body;
import org.newdawn.slick.Graphics;

public abstract class EntityAdapter implements Entity {

	@Override
	public Body getBody() {
		return null;
	}

	@Override
	public void preTick() {}

	@Override
	public void postTick() {}

	@Override
	public void render(Graphics g) {}

	@Override
	public RenderLayer getRenderLayer() {
		return null;
	}
	
	@Override
	public Perspective getPerspective() {
		return null;
	}
	
}
