package com.phoenixkahlo.eclipse.world;

import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * A BodyEntity with a texture.
 */
public abstract class BodyTextureEntity extends BodyEntity {

	private transient Image image;
	private transient float width;
	private transient float height;
	private transient float baseRenderAngle;
	private transient RenderLayer layer;
	private transient Color color; // Nullable
	
	public BodyTextureEntity(RenderLayer layer) {
		this.layer = layer;
	}
	
	public BodyTextureEntity() {
		this(RenderLayer.PLAYER);
	}
	
	protected void injectTexture(Image image, float width, float height, float renderAngle) {
		this.image = image;
		this.width = width;
		this.height = height;
		this.baseRenderAngle = renderAngle;
	}
	
	public float getBaseRenderAngle() {
		return baseRenderAngle;
	}
	
	public void setBaseRenderAngle(float renderAngle) {
		this.baseRenderAngle = renderAngle;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * @return not the base render angle, the angle it actually gets rendered at.
	 */
	protected double getRenderAngle() {
		return getBody().getTransform().getRotation() + baseRenderAngle;
	}
	
	@Override
	public void render(Graphics g) {
		Vector2 pos = getBody().getWorldPoint(new Vector2());
		Vector2 min = pos.copy().subtract(width / 2, height / 2);
		Vector2 max = pos.copy().add(width / 2, height / 2);
		g.rotate((float) pos.x, (float) pos.y,
				(float) Math.toDegrees(getRenderAngle()));
		if (color == null)
			g.drawImage(image,
					(float) min.x,
					(float) min.y,
					(float) max.x,
					(float) max.y,
					0, 0,
					image.getWidth(),
					image.getHeight());
		else
			g.drawImage(image,
					(float) min.x,
					(float) min.y,
					(float) max.x,
					(float) max.y,
					0, 0,
					image.getWidth(),
					image.getHeight(),
					color);
		g.rotate((float) pos.x, (float) pos.y,
				(float) -Math.toDegrees(getBody().getTransform().getRotation() + baseRenderAngle));
	}
	
	@Override
	public RenderLayer getRenderLayer() {
		return layer;
	}
	
}
