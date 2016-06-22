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

import com.phoenixkahlo.eclipse.CodableType;
import com.phoenixkahlo.networking.ArrayListDecoder;
import com.phoenixkahlo.networking.ArrayListEncoder;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;
import com.phoenixkahlo.networking.ProtocolViolationException;
import com.phoenixkahlo.networking.UnionDecoder;
import com.phoenixkahlo.networking.UnionEncoder;
import com.phoenixkahlo.utils.BufferCollection;
import com.phoenixkahlo.utils.GCBufferCollection;

/**
 * Holds a gamestate and is responsible for recording it and retconning it.
 */
public class WorldStateContinuum {
	
	private WorldState state;
	private BufferCollection buffers;
	private int time; // Time in ticks. Between calls to tick(), time is the time of the next tick.
	private EncodingProtocol encoder;
	private DecodingProtocol decoder;
	// Events to be imposed on the game state at certain periods in time.
	private Map<Integer, List<Consumer<WorldState>>> events = new HashMap<Integer, List<Consumer<WorldState>>>();
	
	public WorldStateContinuum() {
		state = new WorldState();
		buffers = new GCBufferCollection(180);
		time = 0;
		encoder = makeEncoder();
		decoder = makeDecoder(state);
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
		time++;
		// Impose events
		if (events.containsKey(time)) {
			for (Consumer<WorldState> event : events.get(time)) {
				event.accept(state);
			}
		}
	}
	
	/**
	 * @param ifMissing supplies the correct gamestate if it no longer remembers that moment in 
	 * time by requesting it from the server.
	 */
	public void revert(int destTime, Supplier<WorldState> ifMissing) {
		try (InputStream in = buffers.readBuffer(destTime)) {
			if (in == null)
				state = ifMissing.get();
			else
				state = (WorldState) decoder.decode(in);
		} catch (IOException | ProtocolViolationException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Imposes the event in the future, present, or past.
	 * @param ifMissing see revert.
	 */
	public void imposeEvent(Consumer<WorldState> event, int destTime, Supplier<WorldState> ifMissing) {
		// Add event
		if (!events.containsKey(time))
			events.put(time, new ArrayList<Consumer<WorldState>>());
		events.get(time).add(event);
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
	
	private static EncodingProtocol makeEncoder() {
		UnionEncoder union = new UnionEncoder();
		union.registerProtocol(CodableType.ARRAYLIST.ordinal(), new ArrayListEncoder(union));
		// Encoders must be registered here for each class of entity
		
		return new FieldEncoder(WorldState.class, union);
	}
	
	/**
	 * Imposes the gamestate data on the old gamestate to avoid object creation
	 */
	private static DecodingProtocol makeDecoder(WorldState imposeOn) {
		UnionDecoder union = new UnionDecoder();
		union.registerProtocol(CodableType.ARRAYLIST.ordinal(), new ArrayListDecoder(union));
		// Decoders must be registered here for each class of entity
		
		return new FieldDecoder(WorldState.class, () -> imposeOn, union);
	}
	
}
