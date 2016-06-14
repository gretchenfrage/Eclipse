package com.phoenixkahlo.statereverting;

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
 */
public class GCBufferCollection implements BufferCollection {

	private Map<Object, ByteArrayOutputStream> buffers = new HashMap<Object, ByteArrayOutputStream>();
	private List<Object> orderedKeys = new ArrayList<Object>();
	private int maxItems;
	private int bufferAllocate;
	
	public GCBufferCollection(int maxItems, int bufferAllocate) {
		this.maxItems = maxItems;
		this.bufferAllocate = bufferAllocate;
	}
	
	public GCBufferCollection(int maxItems) {
		this(maxItems, 1000);
	}
	
	@Override
	public OutputStream startBuffer(Object key) {
		if (orderedKeys.size() >= maxItems)
			buffers.remove(orderedKeys.remove(0));
		ByteArrayOutputStream out = new ByteArrayOutputStream(bufferAllocate);
		buffers.put(key, out);
		orderedKeys.add(key);
		return out;
	}

	@Override
	public InputStream readBuffer(Object key) {
		if (!orderedKeys.contains(key))
			return null;
		return new ByteArrayInputStream(buffers.get(key).toByteArray());
	}


}
