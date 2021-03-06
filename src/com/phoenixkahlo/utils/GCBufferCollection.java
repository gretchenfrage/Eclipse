package com.phoenixkahlo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Old buffers are discarded to the garbage collector.
 * Buffers are stored in byte arrays, really not a very efficient implementation.
 */
public class GCBufferCollection implements BufferCollection {

	private static final int DEFAULT_BUFFER_SIZE = 1000;
	
	private Map<Object, ByteArrayOutputStream> buffers = new HashMap<Object, ByteArrayOutputStream>();
	private List<Object> orderedKeys = new ArrayList<Object>();
	private final int maxItems;
	private final int bufferAllocate;
	
	public GCBufferCollection(int maxItems, int bufferAllocate) {
		this.maxItems = maxItems;
		this.bufferAllocate = bufferAllocate;
	}
	
	public GCBufferCollection(int maxItems) {
		this(maxItems, DEFAULT_BUFFER_SIZE);
	}
	
	@Override
	public OutputStream startBuffer(Object key) {
		synchronized (buffers) {
			if (orderedKeys.contains(key)) {
				orderedKeys.remove(key);
				orderedKeys.add(key);
				ByteArrayOutputStream out = new ByteArrayOutputStream(bufferAllocate);
				buffers.put(key, out);
				return out;
			} else {
				while (orderedKeys.size() >= maxItems) {
					buffers.remove(orderedKeys.remove(0));
				}
				ByteArrayOutputStream out = new ByteArrayOutputStream(bufferAllocate);
				buffers.put(key, out);
				orderedKeys.add(key);
				return out;
			}
		}
	}

	@Override
	public InputStream readBuffer(Object key) {
		synchronized (buffers) {
			if (!orderedKeys.contains(key))
				return null;
			return new ByteArrayInputStream(buffers.get(key).toByteArray());
		}
	}

	@Override
	public Object[] getRemeberedKeys() {
		return orderedKeys.toArray();
	}

}
