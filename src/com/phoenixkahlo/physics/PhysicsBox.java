package com.phoenixkahlo.physics;

import java.util.ArrayList;
import java.util.List;

/**
 * A physics sandbox. Holds a list of rigids and handles updating them.
 */
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
		// Movement
		for (Rigid rigid : rigids) {
			rigid.translate(rigid.getVelocity());
			rigid.changeRotation(rigid.getAngularVelocity());
		}
		
		// Collision
		for (int a = 0; a < rigids.size(); a++) {
			Rigid r1 = rigids.get(a);
			Polygon p1 = r1.getShape();
			p1.cacheTransform(r1.getLocation(), r1.getRotation());
			
			for (int b = a + 1; b < rigids.size(); b++) {
				Rigid r2 = rigids.get(b);
				Polygon p2 = r2.getShape();
				p2.cacheTransform(r2.getLocation(), r2.getRotation());
				
				Polygon intersection = p1.intersection(p2);
				if (intersection != null) {
					intersection.cacheNoTransform();
					Vector2f centroid = intersection.centroid();
					r1.applyForce(r2.getVelocity().multiply(r2.getMass()), centroid);
					r1.applyForce(r1.getVelocity().multiply(r1.getMass()).opposite(), centroid);
					r2.applyForce(r1.getVelocity().multiply(r1.getMass()), centroid);
					r2.applyForce(r2.getVelocity().multiply(r2.getMass()).opposite(), centroid);
				}
			}
			
			p1.invalidateTransform();
		}
	}
	
}
