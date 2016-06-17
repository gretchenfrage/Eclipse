package com.phoenixkahlo.eclipse.gamestate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;

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
public class GameStateContinuum {

	private static EncodingProtocol makeEncoder() {
		UnionEncoder union = new UnionEncoder();
		union.registerProtocol(0, new ArrayListEncoder(union));
		// Encoders must be registered here for each class of entity
		
		return new FieldEncoder(GameState.class, union);
	}
	
	/**
	 * Imposes the gamestate data on the old gamestate to avoid object creation
	 */
	private static DecodingProtocol makeDecoder(GameState imposeOn) {
		UnionDecoder union = new UnionDecoder();
		union.registerProtocol(0, new ArrayListDecoder(union));
		// Decoders must be registered here for each class of entity
		
		return new FieldDecoder(GameState.class, () -> imposeOn, union);
	}
	
	private GameState gameState;
	private BufferCollection buffers;
	private int time; // Time in ticks
	private EncodingProtocol encoder;
	private DecodingProtocol decoder;
	
	public GameStateContinuum() {
		gameState = new GameState();
		buffers = new GCBufferCollection(180);
		time = 0;
		encoder = makeEncoder();
		decoder = makeDecoder(gameState);
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public void tick() {
		// Record the current state
		try (OutputStream out = buffers.startBuffer(time)) {
			encoder.encode(gameState, out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// Tick
		gameState.tick();
		time++;
	}
	
	public int getTime() {
		return time;
	}
	
	/**
	 * @param ifMissing supplies the correct gamestate if it no longer remembers that moment in 
	 * time by requesting it from the server.
	 */
	public void revert(int destTime, Supplier<GameState> ifMissing) {
		try (InputStream in = buffers.readBuffer(destTime)) {
			if (in == null)
				gameState = ifMissing.get();
			else
				gameState = (GameState) decoder.decode(in);
		} catch (IOException | ProtocolViolationException e) {
			throw new RuntimeException(e);
		}
	}
	
}
