package com.phoenixkahlo.eclipse.world.entity;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class RelativeLocationLock extends BasicEntity {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(RelativeLocationLock.class, RelativeLocationLock::new, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(RelativeLocationLock.class, RelativeLocationLock::new, subDecoder);
	}
	
	private int targetID;
	private int platformID;
	private Vector2 relativeLocation;
	
	public RelativeLocationLock() {}
	
	public RelativeLocationLock(int targetID, int platformID, Vector2 relativeLocation) {
		this.targetID = targetID;
		this.platformID = platformID;
		this.relativeLocation = relativeLocation;
	}

	@Override
	public void postTick(WorldState state) {
		Entity target = state.getEntity(targetID);
		Entity platform = state.getEntity(platformID);
		Vector2 location = platform.getBody().getWorldPoint(relativeLocation);
		target.getBody().getTransform().setTranslation(location);
	}
	
}
