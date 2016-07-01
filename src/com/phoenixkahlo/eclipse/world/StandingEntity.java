package com.phoenixkahlo.eclipse.world;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;

/**
 * It stands on things.
 */
public class StandingEntity extends BodyTextureEntity {

	private transient Body preTickPlatformBody;
	private transient Vector2 platformPreTickVelocity;
	
	public StandingEntity(int id, RenderLayer layer) {
		super(id, layer);
	}
	
	public StandingEntity(RenderLayer layer) {
		super(layer);
	}
	
	public StandingEntity() {}
	
	/**
	 * @return nullable
	 */
	public Entity platformOn(WorldState state) {
		for (Entity entity : state.getEntities()) {
			if (entity.isStandingOn(getBody().getWorldPoint(new Vector2())))
				return entity;
		}
		return null;
	}

	@Override
	public void preTick(WorldState state) {
		super.preTick(state);
		preTickPlatformBody = null;
		Entity platformOn = platformOn(state);
		if (platformOn == null)
			return;
		preTickPlatformBody = platformOn.getBody();
		if (preTickPlatformBody == null)
			return;
		platformPreTickVelocity = preTickPlatformBody.getLinearVelocity(
				getBody().getLocalPoint(new Vector2()));
	}

	@Override
	public void postTick(WorldState state) {
		super.postTick(state);
		Entity platformOn = platformOn(state);
		if (platformOn == null)
			return;
		Body platformBody = platformOn.getBody();
		if (platformBody == null || platformBody != preTickPlatformBody)
			return;
		getBody().applyImpulse(platformBody.getLinearVelocity(getBody().getLocalPoint(
				new Vector2())).subtract(platformPreTickVelocity));
	}
	
}
