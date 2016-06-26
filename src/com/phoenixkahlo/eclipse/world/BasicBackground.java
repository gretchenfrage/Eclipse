package com.phoenixkahlo.eclipse.world;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.phoenixkahlo.utils.MathUtils;

public abstract class BasicBackground implements Background {

	private transient Image background;
	private transient float width;
	private transient float height;
	
	protected void injectTexture(Image background, float width, float height) {
		this.background = background;
		this.width = width;
		this.height = height;
	}
	
	protected void injectTexture(Image background, float width) {
		injectTexture(background, width, width / background.getWidth() * background.getHeight());
	}
	
	protected void injectTexture(Image background) {
		injectTexture(background, background.getWidth(), background.getHeight());
	}
	
	@Override
	public void render(Graphics g, GameContainer container, Perspective perspective) {
		float xStart;
		float yStart;
		float xMax;
		float yMax;
		if (perspective == null) {
			xStart = 0;
			yStart = 0;
			xMax = container.getWidth();
			yMax = container.getHeight();
		} else {
			xStart = MathUtils.roundDown((float) perspective.getMinX(container), width);
			yStart = MathUtils.roundDown((float) perspective.getMinY(container), height);
			xMax = (float) perspective.getMaxX(container);
			yMax = (float) perspective.getMaxY(container);
		}
		for (float x = xStart; x < xMax; x += width) {
			for (float y = yStart; y < yMax; y += height) {
				g.drawImage(background,
						x, y,
						x + width, y + height,
						0, 0,
						background.getWidth(), background.getHeight());
			}
		}
	}
	
}
