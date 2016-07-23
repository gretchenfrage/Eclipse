package com.phoenixkahlo.eclipse.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.phoenixkahlo.eclipse.EclipseCodingProtocol;
import com.phoenixkahlo.eclipse.QueueFunctionFactory;
import com.phoenixkahlo.eclipse.client.event.BringToTimeEvent;
import com.phoenixkahlo.eclipse.client.event.CreateControlHandlerEvent;
import com.phoenixkahlo.eclipse.client.event.ImposeEventEvent;
import com.phoenixkahlo.eclipse.client.event.RequestSynchronizeTimeEvent;
import com.phoenixkahlo.eclipse.client.event.RequestVerifyChecksumEvent;
import com.phoenixkahlo.eclipse.client.event.SetContinuumEvent;
import com.phoenixkahlo.eclipse.server.ServerFunction;
import com.phoenixkahlo.eclipse.world.Background;
import com.phoenixkahlo.eclipse.world.Perspective;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.WorldStateContinuum;
import com.phoenixkahlo.eclipse.world.entity.Entity;
import com.phoenixkahlo.networking.FunctionBroadcaster;
import com.phoenixkahlo.networking.FunctionReceiver;
import com.phoenixkahlo.networking.FunctionReceiverThread;
import com.phoenixkahlo.utils.CheckSum;
import com.phoenixkahlo.utils.DisconnectionDetectionInputStream;

/**
 * The client's state of being connected to the server.
 */
public class ServerConnection extends BasicGameState {

	private static final long NANOSECONDS_PER_TICK = (long) (WorldState.SECONDS_PER_TICK * 1_000_000_000);
	
	private WorldStateContinuum continuum;
	private FunctionBroadcaster broadcaster;
	private Thread receiverThread;
	private Socket socket;
	private StateBasedGame game;
	private List<Consumer<ServerConnection>> eventQueue = new ArrayList<Consumer<ServerConnection>>();
	private long timeForNextTick = System.nanoTime();
	private ClientControlHandler controlHandler; // Nullable
	
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
			return;
		}
		in = new DisconnectionDetectionInputStream(in);
		
		broadcaster = new FunctionBroadcaster(out, EclipseCodingProtocol.getEncoder());
		broadcaster.registerEnumClass(ServerFunction.class);

		FunctionReceiver receiver = new FunctionReceiver(in, EclipseCodingProtocol.getDecoder());
		QueueFunctionFactory<ServerConnection> factory =
				new QueueFunctionFactory<ServerConnection>(this::queueEvent);
		
		receiver.registerFunction(ClientFunction.SET_CONTINUUM.ordinal(),
				factory.create(SetContinuumEvent.class, WorldStateContinuum.class));
		receiver.registerFunction(ClientFunction.IMPOSE_EVENT.ordinal(),
				factory.create(ImposeEventEvent.class, int.class, Consumer.class));
		receiver.registerFunction(ClientFunction.BRING_TO_TIME.ordinal(),
				factory.create(BringToTimeEvent.class, int.class));
		receiver.registerFunction(ClientFunction.CREATE_CONTROL_HANDLER.ordinal(),
				factory.create(CreateControlHandlerEvent.class, Function.class));
		receiver.registerFunction(ClientFunction.REQUEST_REQUEST_SYNCHRONIZE_TIME.ordinal(),
				factory.create(RequestSynchronizeTimeEvent.class));
		receiver.registerFunction(ClientFunction.REQUEST_VERIFY_CHECKSUM.ordinal(),
				factory.create(RequestVerifyChecksumEvent.class, int.class));
		
		
		assert receiver.areAllOrdinalsRegistered(ClientFunction.class) : "Client function(s) not registered";
		
		receiverThread = new FunctionReceiverThread(receiver, this::disconnect);
		receiverThread.start();
		
		System.out.println("Connected with " + socket);
	}
	
	private void queueEvent(Consumer<ServerConnection> event) {
		synchronized (eventQueue) {
			eventQueue.add(event);
		}
	}
	
	/**
	 * @param cause nullable.
	 */
	public void disconnect(Exception cause) {
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
		Perspective perspective = null;
		if (controlHandler != null)
			perspective = controlHandler.getPerspective();
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
		
		// Tick the continuum
		continuum.tick();
		
		// Have controlHandler handle
		if (controlHandler != null)
			controlHandler.update(container.getInput(), continuum, container);
	}
	
	public void imposeEvent(int time, Consumer<WorldState> event) {
		try {
			continuum.imposeEvent(event, time, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public FunctionBroadcaster getBroadcaster() {
		return broadcaster;
	}
	
	public void broadcastRequestSynchronizeTime() throws IOException {
		broadcaster.broadcast(ServerFunction.REQUEST_SYNCHRONIZE_TIME);
	}
	
	/**
	 * Will ignore if continuum doesn't remember that checksum.
	 */
	public void verifyChecksum(int time) throws IOException {
		CheckSum checksum = continuum.getChecksum(time);
		if (checksum != null)
			broadcaster.broadcast(ServerFunction.VERIFY_CHECKSUM, time, checksum);
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
	
	public void setControlHandler(ClientControlHandler controlHandler) {
		this.controlHandler = controlHandler;
	}
	
	public WorldStateContinuum getContinuum() {
		return continuum;
	}

	public void setContinuum(WorldStateContinuum continuum) {
		this.continuum = continuum;
	}
	
}
