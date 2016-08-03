package com.phoenixkahlo.utils;

import java.util.AbstractList;

public class ImmutableArrayWrapper<E> extends AbstractList<E> {

	private E[] arr;
	
	public ImmutableArrayWrapper(E[] arr) {
		this.arr = arr;
	}

	@Override
	public E get(int index) {
		return arr[index];
	}

	@Override
	public int size() {
		return arr.length;
	}
	
}
