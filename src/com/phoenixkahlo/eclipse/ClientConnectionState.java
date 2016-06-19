package com.phoenixkahlo.eclipse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.phoenixkahlo.eclipse.world.Entity;
import com.phoenixkahlo.eclipse.world.Perspective;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.WorldStateContinuum;
import com.phoenixkahlo.networking.ArrayListDecoder;
import com.phoenixkahlo.networking.ArrayListEncoder;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;
import com.phoenixkahlo.networking.FunctionBroadcaster;
import com.phoenixkahlo.networking.FunctionReceiver;
import com.phoenixkahlo.networking.FunctionReceiverThread;
import com.phoenixkahlo.networking.InstanceMethod;
import com.phoenixkahlo.networking.UnionDecoder;
import com.phoenixkahlo.networking.UnionEncoder;

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
		
		/*
		// Setup encoder
		UnionEncoder encoder = new UnionEncoder();
		encoder.registerProtocol(CodableType.ARRAYLIST.ordinal(),
				new ArrayListEncoder(encoder));
		encoder.registerProtocol(CodableType.WORLD_STATE.ordinal(),
				new FieldEncoder(WorldState.class, encoder));
		
		// Setup decoder
		UnionDecoder decoder = new UnionDecoder();
		decoder.registerProtocol(CodableType.ARRAYLIST.ordinal(),
				new ArrayListDecoder(decoder));
		decoder.registerProtocol(CodableType.WORLD_STATE.ordinal(),
				new FieldDecoder(WorldState.class, WorldState::new, decoder));
		*/
		
		// Setup broadcaster
		broadcaster = new FunctionBroadcaster(out, EclipseCoderFactory.makeEncoder());
		broadcaster.registerFunctionEnum(ServerFunction.INIT_CLIENT);
		
		// Setup receiver
		FunctionReceiver receiver = new FunctionReceiver(in, EclipseCoderFactory.makeDecoder());
		receiver.registerFunction(ClientFunction.SET_TIME.ordinal(),
				new InstanceMethod(continuum, "setTime", int.class));
		receiver.registerFunction(ClientFunction.SET_WORLD_STATE.ordinal(),
				new InstanceMethod(continuum, "setWorldState", WorldState.class));
		receiver.registerFunction(ClientFunction.SET_PERSPECTIVE_TO_ENTITY.ordinal(),
				new InstanceMethod(this, "setPerspectiveToEntity", int.class));
		
		receiverThread = new FunctionReceiverThread(receiver, this::disconnection);
		receiverThread.start();
	}
	
	/**
	 * @param cause nullable.
	 */
	private void disconnection(Exception cause) {
		if (cause != null) {
			System.out.println("Disconnecting " + this + " because:");
			cause.printStackTrace(System.out);
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
	}

	@Override
	public int getID() {
		return EclipseGameState.CLIENT_CONNECTION.ordinal();
	}
	
	/*
	 * Network receiving functions:
	 */
	
	public void setPerspectiveToEntity(int id) {
		perspective = (Perspective) continuum.getState().getEntity(id);
	}
	
}
