package com.phoenixkahlo.eclipse.world;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.server.ClientConnection;
import com.phoenixkahlo.eclipse.server.ServerControlHandler;
import com.phoenixkahlo.eclipse.server.ServerDrivingHandler;
import com.phoenixkahlo.eclipse.world.impl.Player;

public abstract class Ship extends BodyPlatform {

	private transient Map<Convex, Consumer<Player>> useables = new HashMap<Convex, Consumer<Player>>();
	private transient Map<Convex, BiFunction<ClientConnection, Integer, ServerControlHandler>> handlers = 
			new HashMap<Convex, BiFunction<ClientConnection, Integer, ServerControlHandler>>();
	private transient Vector2 helmPos; // Local position
	private transient double forwardThrustMultiplier = 0;
	private transient double strafeThrustMultiplier = 0;
	private transient double backwardThrustMultiplier = 0;
	private transient double angularThrustMultiplier = 0;
	private Vector2 linearThrust = new Vector2();
	private double angularThrust = 0;
	
	protected void addUseable(Convex area, Consumer<Player> useable) {
		useables.put(area, useable);
	}
	
	protected void addUseable(Convex[] area, Consumer<Player> useable) {
		for (Convex convex : area)
			addUseable(convex, useable);
	}
	
	protected void addHandler(Convex area, BiFunction<ClientConnection, Integer, ServerControlHandler> handler) {
		handlers.put(area, handler);
	}
	
	protected void addHandler(Convex[] area, BiFunction<ClientConnection, Integer, ServerControlHandler> handler) {
		for (Convex convex : area)
			addHandler(convex, handler);
	}
	
	protected void setHelmPos(Vector2 helmPos) {
		this.helmPos = helmPos;
	}
	
	protected void setHelmArea(Convex area) {
		addHandler(area, (ClientConnection connection, Integer playerID) -> 
				new ServerDrivingHandler(connection, playerID, getID()));
		setHelmPos(area.getCenter());
	}
	
	protected void setForwardThrustMultiplier(double forwardThrustMultiplier) {
		this.forwardThrustMultiplier = forwardThrustMultiplier;
	}

	protected void setStrafeThrustMultiplier(double strafeThrustMultiplier) {
		this.strafeThrustMultiplier = strafeThrustMultiplier;
	}

	protected void setBackwardThrustMultiplier(double reverseThrustMultiplier) {
		this.backwardThrustMultiplier = reverseThrustMultiplier;
	}

	protected void setAngularThrustMultiplier(double angularThrustMultiplier) {
		this.angularThrustMultiplier = angularThrustMultiplier;
	}
	
	public void setLinearThrust(Vector2 linearThrust) {
		Vector2 vector = linearThrust.copy();
		if (linearThrust.y < 0)
			vector.y *= forwardThrustMultiplier;
		else
			vector.y *= backwardThrustMultiplier;
		vector.x *= strafeThrustMultiplier;
		this.linearThrust = vector;
	}
	
	public void setAngularThrust(byte angularThrust) {
		this.angularThrust = angularThrust * angularThrustMultiplier;
	}

	public Vector2 getHelmPos() {
		return helmPos;
	}

	@Override
	public Consumer<Player> getUseable(Vector2 position) {
		for (Convex convex : useables.keySet()) {
			if (convex.contains(getBody().getLocalPoint(position)))
				return useables.get(convex);
		}
		return null;
	}
	
	@Override
	public BiFunction<ClientConnection, Integer, ServerControlHandler> getHandler(Vector2 position) {
		for (Convex convex : handlers.keySet()) {
			if (convex.contains(getBody().getLocalPoint(position)))
				return handlers.get(convex);
		}
		return null;
	}

	@Override
	public void preTick(WorldState state) {
		getBody().applyForce(linearThrust.copy().rotate(getBody().getTransform().getRotation()));
		getBody().applyTorque(angularThrust);
	}
	
}
