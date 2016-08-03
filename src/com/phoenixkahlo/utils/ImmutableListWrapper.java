package com.phoenixkahlo.utils;

import java.util.AbstractList;
import java.util.List;

public class ImmutableListWrapper<E> extends AbstractList<E> {

	private List<E> list;
	
	public ImmutableListWrapper(List<E> list) {
		this.list = list;
	}

	@Override
	public E get(int index) {
		return list.get(index);
	}

	@Override
	public int size() {
		return list.size();
	}
	
}
