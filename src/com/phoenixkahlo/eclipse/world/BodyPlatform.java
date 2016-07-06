package com.phoenixkahlo.eclipse.world;

import java.util.ArrayList;
import java.util.List;

import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;

/**
 * Things may stand on it.
 */
public abstract class BodyPlatform extends BodyTextureEntity {
	
	private transient List<Convex> area = new ArrayList<Convex>();
	private transient double baseAlignmentAngle = 0;
	
	public BodyPlatform(RenderLayer layer) {
		super(layer);
	}
	
	public BodyPlatform() {
		this(RenderLayer.SHIP);
	}

	protected void addArea(Convex convex) {
		area.add(convex);
	}
	
	protected void setBaseAlignmentAngle(double angle) {
		this.baseAlignmentAngle = angle;
	}
	
	@Override
	public boolean isStandingOn(Vector2 position) {
		position = getBody().getLocalPoint(position);
		for (Convex convex : area) {
			if (convex.contains(position))
				return true;
		}
		return false;
	}
	
	@Override
	public double getAlignmentAngle() {
		return getBody().getTransform().getRotation() + baseAlignmentAngle;
	}

}
