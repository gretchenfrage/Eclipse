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
import com.phoenixkahlo.eclipse.client.event.BringToTimeEvent;
import com.phoenixkahlo.eclipse.client.event.ImposeEventEvent;
import com.phoenixkahlo.eclipse.client.event.SetEntityIDEvent;
import com.phoenixkahlo.eclipse.client.event.SetTimeLogiclesslyEvent;
import com.phoenixkahlo.eclipse.client.event.SetWorldStateEvent;
import com.phoenixkahlo.eclipse.server.ServerFunction;
import com.phoenixkahlo.eclipse.world.Background;
import com.phoenixkahlo.eclipse.world.Entity;
import com.phoenixkahlo.eclipse.world.Perspective;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.WorldStateContinuum;
import com.phoenixkahlo.eclipse.world.event.SetRenderAngleEvent;
import com.phoenixkahlo.networking.FunctionBroadcaster;
import com.phoenixkahlo.networking.FunctionReceiver;
import com.phoenixkahlo.networking.FunctionReceiverThread;

/**
 * The client's state of being connected to the server.
 */
public class ServerConnection extends BasicGameState {

	private static final long NANOSECONDS_PER_TICK = (long) (WorldState.SECONDS_PER_TICK * 1_000_000_000);
	private static final double RADIANS_ROTATE_PER_TICK = 0.1;
	private static final double SCALE_FACTOR_PER_TICK = 0.01;
	
	private WorldStateContinuum continuum;
	private FunctionBroadcaster broadcaster;
	private Thread receiverThread;
	private Socket socket;
	private StateBasedGame game;
	private List<Consumer<ServerConnection>> eventQueue = new ArrayList<Consumer<ServerConnection>>();
	private Vector2 cachedDirection = new Vector2(0, 0);
	private boolean cachedIsSprinting = false;
	private float cachedAngle = Float.NaN;
	private long timeForNextTick = System.nanoTime();
	private int entityID = -1;
	private ClientControlHandler controlHandler;
	
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
		
		receiver.registerFunction(ClientFunction.SET_TIME_LOGICLESSLY.ordinal(), 
				factory.create(SetTimeLogiclesslyEvent.class, int.class));
		receiver.registerFunction(ClientFunction.SET_WORLD_STATE.ordinal(), 
				factory.create(SetWorldStateEvent.class, WorldState.class));
		receiver.registerFunction(ClientFunction.IMPOSE_EVENT.ordinal(),
				factory.create(ImposeEventEvent.class, int.class, Consumer.class));
		receiver.registerFunction(ClientFunction.BRING_TO_TIME.ordinal(),
				factory.create(BringToTimeEvent.class, int.class));
		receiver.registerFunction(ClientFunction.SET_ENTITY_ID.ordinal(), 
				factory.create(SetEntityIDEvent.class, int.class));
		
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
		Perspective perspective = controlHandler.getPerspective();
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
		
		// Active wait until time is ready
		while (System.nanoTime() < timeForNextTick) {}
		timeForNextTick += NANOSECONDS_PER_TICK;
		
		// Execute eventQueue
		synchronized (eventQueue) {
			while (!eventQueue.isEmpty()) {
				eventQueue.remove(0).accept(this);
			}
		}
		
		// Have controlHandler handle
		controlHandler.update(container.getInput());
		
		/*
		Input input = container.getInput();
		
		// Broadcast controls
		// Direction
		Vector2 direction = new Vector2();
		if (input.isKeyDown(Input.KEY_W))
			direction.y--;
		if (input.isKeyDown(Input.KEY_S))
			direction.y++;
		if (input.isKeyDown(Input.KEY_A))
			direction.x--;
		if (input.isKeyDown(Input.KEY_D))
			direction.x++;
		Perspective perspective = continuum.getState().getPerspective();
		if (perspective != null) {
			double rotation = perspective.attemptGetRotation();
			if (!Double.isNaN(rotation))
				direction.rotate(rotation);
		}
		try {
			if (!direction.equals(cachedDirection)) {
				broadcaster.broadcast(ServerFunction.SET_DIRECTION, direction);
				cachedDirection = direction;
			}
		} catch (IOException e) {
			disconnect(e);
		}
		// Sprinting
		boolean isSprinting = input.isKeyDown(Input.KEY_LSHIFT);
		try {
			if (isSprinting != cachedIsSprinting) {
				broadcaster.broadcast(ServerFunction.SET_IS_SPRINTING, isSprinting);
				cachedIsSprinting = isSprinting;
			}
		} catch (IOException e) {
			
		}
		// Looking direction
		if (entityID != -1) {
			Vector2 p1 = continuum.getState().getEntity(entityID).getBody().getWorldCenter();
			if (perspective != null)
				p1 = perspective.worldToScreen(p1, new Vector2(container.getWidth(), container.getHeight()));
			Vector2 p2 = new Vector2(input.getMouseX(), input.getMouseY()); 
			p2.subtract(p1);
			float angle = (float) Math.atan2(p2.y, p2.x);
			if (perspective != null && !Double.isNaN(perspective.attemptGetRotation()))
				angle += perspective.attemptGetRotation();
			if (angle != cachedAngle) {
				try {
					broadcaster.broadcast(
							ServerFunction.IMPOSE_EVENT,
							continuum.getTime(),
							new SetRenderAngleEvent(entityID, angle));
				} catch (IOException e) {
					disconnect(e);
				}
				cachedAngle = angle;
			}
		}
		
		// Local controls
		if (perspective != null) {
			// Rotation
			if (input.isKeyDown(Input.KEY_E))
				perspective.suggestAddRotation(RADIANS_ROTATE_PER_TICK);
			if (input.isKeyDown(Input.KEY_Q))
				perspective.suggestAddRotation(-RADIANS_ROTATE_PER_TICK);
			// Scale
			if (input.isKeyDown(Input.KEY_R))
				perspective.suggestRaiseScale(1 + SCALE_FACTOR_PER_TICK);
			if (input.isKeyDown(Input.KEY_F))
				perspective.suggestRaiseScale(1 - SCALE_FACTOR_PER_TICK);
		}
		 */
		
		// Tick the continuum
		continuum.tick();
	}
	
	public void imposeEvent(int time, Consumer<WorldState> event) {
		try {
			continuum.imposeEvent(event, time, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getID() {
		return ClientGameState.SERVER_CONNECTION.ordinal();
	}
	
	public void setTimeLogiclessly(int time) {
		continuum.setTimeLogiclessly(time);
	}
	
	public void bringToTime(int time) {
		try {
			continuum.bringToTime(time, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setWorldState(WorldState state) {
		continuum.setWorldState(state);
	}
	
	public void setEntityID(int entityID) {
		this.entityID = entityID;
	}
	
}
