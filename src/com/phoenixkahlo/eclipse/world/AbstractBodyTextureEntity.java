package com.phoenixkahlo.eclipse.world;

import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public abstract class AbstractBodyTextureEntity extends AbstractBodyEntity {

	private transient Image image;
	private transient float width;
	private transient float height;
	private transient float renderAngle;
	
	public AbstractBodyTextureEntity(int id) {
		super(id);
	}
	
	public AbstractBodyTextureEntity() {}
	
	protected void injectTexture(Image image, float width, float height, float renderAngle) {
		this.image = image;
		this.width = width;
		this.height = height;
		this.renderAngle = renderAngle;
	}
	
	@Override
	public void render(Graphics g) {
		Vector2 pos = getBody().getTransform().getTranslation();
		Vector2 min = pos.copy().subtract(width / 2, height / 2);
		Vector2 max = pos.copy().add(width / 2, height / 2);
		g.rotate((float) pos.x, (float) pos.y,
				(float) -(getBody().getTransform().getRotation() + renderAngle));
		g.drawImage(image,
				(float) min.x,
				(float) min.y,
				(float) max.x,
				(float) max.y,
				0, 0,
				image.getWidth(),
				image.getHeight());
		g.rotate((float) pos.x, (float) pos.y,
				(float) getBody().getTransform().getRotation() + renderAngle);
	}	
	
}
