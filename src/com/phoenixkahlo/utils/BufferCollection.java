package com.phoenixkahlo.utils;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A map between object keys and byte buffers, represented by InputStreams 
 * and OutputStreams. Implementations may discard old buffers for memory 
 * purposes.
 */
public interface BufferCollection {

	/**
	 * Stream expected to be closed when done.
	 */
	OutputStream startBuffer(Object key);
	
	/**
	 * Nullable if buffer has been discarded. 
	 * Stream expected to be closed when done.
	 */
	InputStream readBuffer(Object key);
	
	Object[] getRemeberedKeys();
	
}
