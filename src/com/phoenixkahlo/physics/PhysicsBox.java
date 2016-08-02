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
		// Movement
		for (Rigid rigid : rigids) {
			rigid.translate(rigid.getVelocity());
			rigid.changeAngle(rigid.getAngularVelocity());
		}
		
		// Collision
		for (int a = 0; a < rigids.size(); a++) {
			for (int b = a + 1; b < rigids.size(); b++) {
				Rigid r1 = rigids.get(a);
				Rigid r2 = rigids.get(b);
				Polygon p1 = r1.getShape();
				Polygon p2 = r2.getShape();
				p1.cacheTransform(r1.getLocation(), r1.getAngle());
				p2.cacheTransform(r2.getLocation(), r2.getAngle());
				Polygon intersection = p1.intersection(p2);
				if (intersection != null) {
					System.out.println("detected intersection: " + intersection);
					intersection.cacheNoTransform();
					float area = intersection.area();
					Vector2f centroid = intersection.centroid();
					Vector2f p1closest = p1.closestPerimiterPointTo(centroid);
					Vector2f p2closest = p2.closestPerimiterPointTo(centroid);
					Vector2f force1 = p1closest.subtract(centroid).multiply(area);
					Vector2f force2 = p2closest.subtract(centroid).multiply(area);
					r1.applyForce(force1, centroid);
					r2.applyForce(force2, centroid);
					r1.applyForce(force2.multiply(-1), centroid);
					r2.applyForce(force1.multiply(-1), centroid);
				}
			}
		}
	}
	
}
