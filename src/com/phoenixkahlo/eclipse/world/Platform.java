package com.phoenixkahlo.eclipse.world;

import java.util.ArrayList;
import java.util.List;

import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;

/**
 * Things may stand on it.
 */
public class Platform extends BodyTextureEntity {
	
	private List<Convex> area = new ArrayList<Convex>();
	
	public Platform(int id, RenderLayer layer) {
		super(id, layer);
	}
	
	public Platform(RenderLayer layer) {
		super(layer);
	}
	
	public Platform() {}

	protected void addArea(Convex convex) {
		area.add(convex);
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

}
