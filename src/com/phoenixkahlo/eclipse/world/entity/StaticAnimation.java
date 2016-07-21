package com.phoenixkahlo.eclipse.world.entity;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.phoenixkahlo.eclipse.world.WorldState;

public abstract class StaticAnimation extends BasicEntity {

	private transient Image image;
	private transient float width;
	private transient float height;
	private float x;
	private float y;
	private int ticksRemaining;
	
	public StaticAnimation(float x, float y, int ticks) {
		this.x = x;
		this.y = y;
		this.ticksRemaining = ticks;
	}
	
	protected void injectTexture(Image image, float width, float height) {
		this.image = image;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Null safe.
	 */
	protected void injectTexture(Image image, float width) {
		if (image != null)
			injectTexture(image, width, width / image.getWidth() * image.getHeight());
	}

	@Override
	public void postTick(WorldState state) {
		ticksRemaining--;
		if (ticksRemaining < 0)
			state.removeEntity(this);
	}

	@Override
	public void render(Graphics g) {
		if (image != null)
			g.drawImage(image, x, y, x + width, x + height, 0, 0, image.getWidth(), image.getHeight());
	}
	
}
