package com.phoenixkahlo.utils;

/**
 * Like a byte[], but doesn't waste 75% of the memory.
 */
public class CompoundedByteArray {

	private int[] arr;
	private int length;
	
	public CompoundedByteArray(int size) {
		this.length = size;
		if (size % 4 == 0)
			arr = new int[size / 4];
		else
			arr = new int[size / 4 + 1];
	}
	
	public CompoundedByteArray(byte[] arr) {
		this(arr.length);
		for (int i = 0; i < arr.length; i++) {
			set(i, arr[i]);
		}
	}
	
	public byte get(int index) throws ArrayIndexOutOfBoundsException {
		if (index >= length)
			throw new ArrayIndexOutOfBoundsException();
		int compound = arr[index / 4];
		int remainder = index % 4;
		switch (remainder) {
		case 0:
			return (byte) (compound & 0x000000FF);
		case 1:
			return (byte) ((compound & 0x0000FF00) >> 8);
		case 2:
			return (byte) ((compound & 0x00FF0000) >> 16);
		case 3:
			return (byte) ((compound & 0xFF000000) >> 24);
		default:
			throw new RuntimeException();
		}
	}
	
	public void set(int index, byte n) throws ArrayIndexOutOfBoundsException {
		if (index >= length)
			throw new ArrayIndexOutOfBoundsException();
		int remainder = index % 4;
		index /= 4;
		switch (remainder) {
		case 0:
			arr[index] &= 0xFFFFFF00;
			arr[index] |= n;
		case 1:
			arr[index] &= 0xFFFF00FF;
			arr[index] |= n << 8;
		case 2:
			arr[index] &= 0xFF00FFFF;
			arr[index] |= n << 16;
		case 3:
			arr[index] &= 0x00FFFFFF;
			arr[index] |= n << 24;
		}
	}
	
	public byte[] toArray() {
		byte[] out = new byte[length];
		for (int i = 0; i < length; i++) {
			out[i] = get(i);
		}
		return out;
	}
	
}
