package com.phoenixkahlo.eclipse.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.function.Consumer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.phoenixkahlo.eclipse.EclipseCoderFactory;
import com.phoenixkahlo.eclipse.server.ServerFunction;
import com.phoenixkahlo.eclipse.world.Entity;
import com.phoenixkahlo.eclipse.world.Perspective;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.WorldStateContinuum;
import com.phoenixkahlo.networking.FunctionBroadcaster;
import com.phoenixkahlo.networking.FunctionReceiver;
import com.phoenixkahlo.networking.FunctionReceiverThread;
import com.phoenixkahlo.networking.InstanceMethod;

public class ClientConnectionState extends BasicGameState {

	private WorldStateContinuum continuum;
	private FunctionBroadcaster broadcaster;
	private Thread receiverThread;
	private Perspective perspective; // Nullable
	
	public ClientConnectionState(Socket socket) {
		continuum = new WorldStateContinuum();
		
		// Setup network
		InputStream in = null;
		OutputStream out = null;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (IOException e) {
			disconnection(e);
		}
		
		broadcaster = new FunctionBroadcaster(out, EclipseCoderFactory.makeEncoder());
		broadcaster.registerEnumClass(ServerFunction.class);
		
		FunctionReceiver receiver = new FunctionReceiver(in, EclipseCoderFactory.makeDecoder());
		receiver.registerFunction(ClientFunction.SET_TIME.ordinal(),
				new InstanceMethod(continuum, "setTime", int.class));
		receiver.registerFunction(ClientFunction.SET_WORLD_STATE.ordinal(),
				new InstanceMethod(continuum, "setWorldState", WorldState.class));
		receiver.registerFunction(ClientFunction.IMPOSE_EVENT.ordinal(),
				new InstanceMethod(this, "imposeEvent", int.class, Consumer.class));
		
		receiverThread = new FunctionReceiverThread(receiver, this::disconnection);
		receiverThread.start();
		
		// Call for setup
		try {
			broadcaster.broadcast(ServerFunction.INIT_CLIENT);
		} catch (IOException e) {
			disconnection(e);
		}
		
		System.out.println("Connected with " + socket);
	}
	
	/**
	 * @param cause nullable.
	 */
	private void disconnection(Exception cause) {
		if (cause != null) {
			System.out.println("Disconnecting " + this + " because:");
			cause.printStackTrace(System.out);
		} else {
			System.out.println("Disconnecting " + this + ", cause unknown.");
		}
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if (perspective != null)
			perspective.transform(g, container);
		// Sort the entities by their render layer ordinals, then render them.
		List<Entity> entities = continuum.getState().getEntities();
		entities.removeIf((Entity entity) -> entity.getRenderLayer() == null);
		entities.sort((Entity a, Entity b) -> a.getRenderLayer().ordinal() - b.getRenderLayer().ordinal());
		for (Entity entity : entities) {
			entity.render(g);
		}
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE))
			container.exit();
		continuum.tick();
	}

	@Override
	public int getID() {
		return EclipseGameState.CLIENT_CONNECTION.ordinal();
	}
	
	/*
	 * Network receiving functions:
	 */
	
	public void imposeEvent(int time, Consumer<WorldState> event) {
		try {
			continuum.imposeEvent(event, time, null);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	
}
