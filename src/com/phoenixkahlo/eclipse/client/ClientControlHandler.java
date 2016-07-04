package com.phoenixkahlo.eclipse.client;

import org.newdawn.slick.Input;

import com.phoenixkahlo.eclipse.world.Perspective;

public interface ClientControlHandler {

	Perspective getPerspective();
	
	void update(Input input);
	
}
