package com.phoenixkahlo.eclipse.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.function.Consumer;

import org.dyn4j.geometry.Vector2;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.phoenixkahlo.eclipse.EclipseCoderFactory;
import com.phoenixkahlo.eclipse.server.ServerFunction;
import com.phoenixkahlo.eclipse.world.Background;
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
	private Socket socket;
	private StateBasedGame game;
	private Vector2 cachedDirection = new Vector2(0, 0);
	
	public ClientConnectionState(Socket socket, StateBasedGame game) {
		continuum = new WorldStateContinuum();
		this.socket = socket;
		this.game = game;
		
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
				new InstanceMethod(this, "setTime", int.class));
		receiver.registerFunction(ClientFunction.SET_WORLD_STATE.ordinal(),
				new InstanceMethod(this, "setWorldState", WorldState.class));
		receiver.registerFunction(ClientFunction.IMPOSE_EVENT.ordinal(),
				new InstanceMethod(this, "imposeEvent", int.class, Consumer.class));
		receiver.registerFunction(ClientFunction.IMPOSE_GET_PERSPECTIVE_FROM_ENTITY_EVENT.ordinal(),
				new InstanceMethod(this, "imposeGetPerspectiveFromEntityEvent", int.class, int.class));
		
		assert receiver.areAllOrdinalsRegistered(ClientFunction.class) : "Client function(s) not registered";
		
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
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		game.enterState(ClientGameState.MAIN_MENU.ordinal());
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if (perspective != null)
			perspective.transform(g, container);
		Background background = continuum.getState().getBackground();
		if (background != null)
			background.render(g, container, perspective);
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
		
		Input input = container.getInput();
		
		Vector2 direction = new Vector2(0, 0);
		if (input.isKeyDown(Input.KEY_W))
			direction.y--;
		if (input.isKeyDown(Input.KEY_S))
			direction.y++;
		if (input.isKeyDown(Input.KEY_A))
			direction.x--;
		if (input.isKeyDown(Input.KEY_D))
			direction.x++;
		try {
			if (!direction.equals(cachedDirection)) {
				broadcaster.broadcast(ServerFunction.SET_DIRECTION, direction);
				cachedDirection = direction;
			}
			continuum.tick();
		} catch (IOException e) {
			disconnection(e);
		}
	}

	@Override
	public int getID() {
		return ClientGameState.CLIENT_CONNECTION.ordinal();
	}
	
	public void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}
	
	/*
	 * Network receiving functions:
	 */
	
	public void setTime(int time) {
		continuum.setTime(time);
	}
	
	public void setWorldState(WorldState state) {
		continuum.setWorldState(state);
	}
	
	public void imposeEvent(int time, Consumer<WorldState> event) {
		try {
			continuum.imposeEvent(event, time, null);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	
	public void imposeGetPerspectiveFromEntityEvent(int time, int id) {
		imposeEvent(time, new GetPerspectiveFromEntityEvent(id, this));
	}
	
}
