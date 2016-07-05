package com.phoenixkahlo.eclipse.client;

import org.newdawn.slick.Input;

import com.phoenixkahlo.eclipse.world.Perspective;
import com.phoenixkahlo.eclipse.world.WorldState;

/**
 * Exists on the client side to handle input and provide perspective. 
 * Is created by a function that is received by the server and is networkedly 
 * coupled to the ServerControlHandler.
 */
public interface ClientControlHandler {

	/**
	 * Nullable.
	 */
	Perspective getPerspective();
	
	void update(Input input, WorldState worldState);
	
}
