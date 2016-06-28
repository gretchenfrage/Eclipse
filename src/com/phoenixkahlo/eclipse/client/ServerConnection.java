package com.phoenixkahlo.eclipse.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
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
import com.phoenixkahlo.eclipse.QueueFunctionFactory;
import com.phoenixkahlo.eclipse.client.event.ImposeEventEvent;
import com.phoenixkahlo.eclipse.client.event.SetTimeEvent;
import com.phoenixkahlo.eclipse.client.event.SetWorldStateEvent;
import com.phoenixkahlo.eclipse.server.ServerFunction;
import com.phoenixkahlo.eclipse.world.Background;
import com.phoenixkahlo.eclipse.world.Entity;
import com.phoenixkahlo.eclipse.world.Perspective;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.WorldStateContinuum;
import com.phoenixkahlo.networking.FunctionBroadcaster;
import com.phoenixkahlo.networking.FunctionReceiver;
import com.phoenixkahlo.networking.FunctionReceiverThread;

public class ServerConnection extends BasicGameState {

	private WorldStateContinuum continuum;
	private FunctionBroadcaster broadcaster;
	private Thread receiverThread;
	private Socket socket;
	private StateBasedGame game;
	private Vector2 cachedDirection = new Vector2(0, 0);
	private List<Consumer<ServerConnection>> eventQueue = new ArrayList<Consumer<ServerConnection>>();
	
	public ServerConnection(Socket socket, StateBasedGame game) {
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
			disconnect(e);
		}
		
		broadcaster = new FunctionBroadcaster(out, EclipseCoderFactory.makeEncoder());
		broadcaster.registerEnumClass(ServerFunction.class);

		FunctionReceiver receiver = new FunctionReceiver(in, EclipseCoderFactory.makeDecoder());
		QueueFunctionFactory<ServerConnection> factory =
				new QueueFunctionFactory<ServerConnection>(this::queueEvent);
		
		receiver.registerFunction(ClientFunction.SET_TIME.ordinal(), 
				factory.create(SetTimeEvent.class, int.class));
		receiver.registerFunction(ClientFunction.SET_WORLD_STATE.ordinal(), 
				factory.create(SetWorldStateEvent.class, WorldState.class));
		receiver.registerFunction(ClientFunction.IMPOSE_EVENT.ordinal(),
				factory.create(ImposeEventEvent.class, int.class, Consumer.class));
		
		
		assert receiver.areAllOrdinalsRegistered(ClientFunction.class) : "Client function(s) not registered";
		
		receiverThread = new FunctionReceiverThread(receiver, this::disconnect);
		receiverThread.start();
		
		System.out.println("Connected with " + socket);
		
		// Call for setup
		try {
			broadcaster.broadcast(ServerFunction.INIT_CLIENT);
		} catch (IOException e) {
			disconnect(e);
		}
	}
	
	private void queueEvent(Consumer<ServerConnection> event) {
		synchronized (eventQueue) {
			eventQueue.add(event);
		}
	}
	
	/**
	 * @param cause nullable.
	 */
	private void disconnect(Exception cause) {
		if (cause != null) {
			System.out.println("Disconnecting " + this + " because:");
			cause.printStackTrace(System.out);
		} else {
			System.out.println("Disconnecting " + this + ", cause unknown.");
		}
		try {
			broadcaster.broadcast(ServerFunction.DISCONNECT);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		game.enterState(ClientGameState.MAIN_MENU.ordinal());
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// Transform perspective
		Perspective perspective = continuum.getState().getPerspective();
		if (perspective != null)
			perspective.transform(g, container);
		// Render background
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
		// Check for escaping to main menu
		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE))
			disconnect(null);
		
		// Execute eventQueue
		synchronized (eventQueue) {
			while (!eventQueue.isEmpty()) {
				eventQueue.remove(0).accept(this);
			}
		}
		
		Input input = container.getInput();
		
		// Broadcast movement changes
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
		} catch (IOException e) {
			disconnect(e);
		}
		
		// Tick the continuum
		continuum.tick();
	}

	@Override
	public int getID() {
		return ClientGameState.SERVER_CONNECTION.ordinal();
	}
	
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
	
}
