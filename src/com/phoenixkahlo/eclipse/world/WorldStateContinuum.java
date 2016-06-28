package com.phoenixkahlo.eclipse.world;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.phoenixkahlo.eclipse.EclipseCoderFactory;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.ProtocolViolationException;
import com.phoenixkahlo.utils.BufferCollection;
import com.phoenixkahlo.utils.GCBufferCollection;

/**
 * Holds a gamestate and is responsible for recording it and retconning it.
 */
public class WorldStateContinuum {
	
	private WorldState state;
	private BufferCollection buffers;
	private int time; // Time in ticks. Between calls to tick(), time is the time of the next tick.
	private EncodingProtocol encoder = EclipseCoderFactory.ENCODER;
	private DecodingProtocol decoder = EclipseCoderFactory.DECODER;
	// Events to be imposed on the game state at certain periods in time.
	private Map<Integer, List<Consumer<WorldState>>> events = new HashMap<Integer, List<Consumer<WorldState>>>();
	
	public WorldStateContinuum() {
		state = new WorldState();
		buffers = new GCBufferCollection(180);
		time = 0;
	}
	
	public WorldState getState() {
		return state;
	}
	
	public int getTime() {
		return time;
	}
	
	public void tick() {
		// Record the current state
		try (OutputStream out = buffers.startBuffer(time)) {
			encoder.encode(state, out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// Tick
		state.tick();
		// Impose events
		if (events.containsKey(time)) {
			for (Consumer<WorldState> event : events.get(time)) {
				event.accept(state);
			}
		}
		// Increment
		time++;
	}
	
	/**
	 * @param ifMissing supplies the correct gamestate if it no longer remembers that moment in 
	 * time by requesting it from the server. Nullable.
	 * @throws NoSuchFieldException if ifMissing == null and state doesn't exist.
	 */
	public void revert(int destTime, Supplier<WorldState> ifMissing) throws NoSuchFieldException {
		try (InputStream in = buffers.readBuffer(destTime)) {
			if (in == null)
				if (ifMissing == null)
					throw new NoSuchFieldException("time is " + time + " and cannot revert to " + destTime);
				else
					state = ifMissing.get();
			else
				state = (WorldState) decoder.decode(in);
			time = destTime;
		} catch (IOException | ProtocolViolationException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Imposes the event in the future, present, or past.
	 * @param ifMissing see revert.
	 * @throws NoSuchFieldException see revert.
	 */
	public void imposeEvent(Consumer<WorldState> event, int destTime, Supplier<WorldState> ifMissing) throws NoSuchFieldException {
		// Add event
		if (!events.containsKey(destTime))
			events.put(destTime, new ArrayList<Consumer<WorldState>>());
		events.get(destTime).add(event);
		if (destTime < time) { // If it was supposed to be imposed in the past
			// Revert to that time, then tick to the current time, but this time with the event in the map
			int currentTime = time;
			revert(destTime, ifMissing);
			while (time < currentTime)
				tick();
		}
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public void setWorldState(WorldState state) {
		this.state = state;
	}
	
}
