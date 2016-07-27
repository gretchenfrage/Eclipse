package com.phoenixkahlo.physics;

import java.util.ArrayList;
import java.util.List;

public class PhysicsBox {

	private List<Rigid> rigids = new ArrayList<Rigid>();
	
	public void addRigid(Rigid rigid) {
		rigids.add(rigid);
	}
	
	public void removeRigid(Rigid rigid) {
		rigids.remove(rigid);
	}
	
	/**
	 * Please don't modify.
	 */
	public List<Rigid> getRigids() {
		return rigids;
	}
	
	public void update() {
		for (Rigid rigid : rigids) {
			rigid.translate(rigid.getVelocity());
			rigid.changeAngle(rigid.getAngularVelocity());
		}
	}
	
}
