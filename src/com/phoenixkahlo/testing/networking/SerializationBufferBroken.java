package com.phoenixkahlo.testing.networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Not thread safe!
 * TODO: make thread safe
 */
public class SerializationBufferBroken {

	private class Partition {
		
		int start;
		int end;
		
		Partition(int start, int end) {
			this.start = start;
			this.end = end;
		}
		
		InputStream toInputStream() {
			pointer = start;
			return new InputStream() {

				boolean finished = false;
				
				@Override
				public int read() throws IOException {
					if (available() == 0) return -1;
					byte b = buffer[pointer];
					pointer++;
					pointer %= buffer.length;
					return b;
				}
				
				@Override
				public int available() {
					if (finished)
						return 0;
					int relativeEnd = end;
					if (relativeEnd < pointer)
						relativeEnd += buffer.length;
					return end - pointer + 1;
				}
				
			};
		}
		
	}
	
	private byte[] buffer;
	private int pointer = 0; // What the stream is about to read or write
	private Map<Object, Partition> partitions = new HashMap<Object, Partition>();
	
	public SerializationBufferBroken(int size) {
		buffer = new byte[size];
	}
	
	public OutputStream startPartition(Object key) {
		return new OutputStream() {

			int start = pointer;
			int written = 0; // To differentiate between having written nothing and having written over self
			
			@Override
			public void write(int b) throws IOException {
				for (Partition partition : partitions.values()) {
					/*
					 * If this partition has grown to intersect with another partition, the other
					 * partition must be removed
					 */
					if (circularIntersection(start, pointer, partition.start, partition.end, buffer.length))
						partitions.remove(partition);
				}
				/*
				 * If this partition has grown to intersect with itself, this is an exceptional scenario
				 * and an IOException must be thrown
				 */
				if (written >= buffer.length)
					throw new IOException("Insufficient buffer size");
				// It may write
				buffer[pointer] = (byte) b;
				pointer++;
				pointer %= buffer.length;
				written++;
			}
			
			@Override
			public void close() {
				partitions.put(key, new Partition(start, pointer - 1));
			}
			
		};
	}
	
	/**
	 * @throws IllegalStateException if that partition was never created or has been overridden
	 */
	public InputStream readPartition(Object key) throws IllegalStateException {
		if (!partitions.containsKey(key)) throw new IllegalStateException();
		return partitions.get(key).toInputStream();
	}
	
	private static boolean circularIntersection(int start1, int end1, int start2, int end2, int length) {
		class Segment {
			int start;
			int end;
			Segment(int start, int end) {
				this.start = start;
				this.end = end;
			}
			boolean intersects(Segment other) {
				return (other.start >= start && other.start <= end) ||
						(other.end >= start && other.end <= end);
			}
		}
		List<Segment> segments = new ArrayList<Segment>();
		if (start1 <= end1) {
			segments.add(new Segment(start1, end1));
		} else {
			segments.add(new Segment(start1, length));
			segments.add(new Segment(0, end1));
		}
		if (start2 <= end2) {
			segments.add(new Segment(start2, end2));
		} else {
			segments.add(new Segment(start2, length));
			segments.add(new Segment(0, end2));
		}
		for (int i1 = 0; i1 < segments.size(); i1++) {
			for (int i2 = 0; i2 < segments.size(); i2++) {
				if (i1 != i2 && segments.get(i1).intersects(segments.get(i2)))
					return true;
			}
		}
		return false;
	}
	
}
