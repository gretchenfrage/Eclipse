package com.phoenixkahlo.eclipse.world.entity;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class RelativePlayerFacingAngleLock extends BasicEntity {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(RelativePlayerFacingAngleLock.class, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(RelativePlayerFacingAngleLock.class, 
				RelativePlayerFacingAngleLock::new, subDecoder);
	}
	
	private int playerID;
	private int platformID;
	private double angle;
	
	public RelativePlayerFacingAngleLock() {}
	
	public RelativePlayerFacingAngleLock(int playerID, int platformID, double angle) {
		this.playerID = playerID;
		this.platformID = platformID;
		this.angle = angle;
	}
	
	@Override
	public void postTick(WorldState state) {
		double targetAngle = state.getEntity(platformID).getBody().getTransform().getRotation();
		targetAngle += angle;
		((Player) state.getEntity(playerID)).setFacingAngle(targetAngle);
	}
	
}
