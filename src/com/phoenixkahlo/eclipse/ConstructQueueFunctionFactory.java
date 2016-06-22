package com.phoenixkahlo.eclipse;

import java.util.function.Consumer;

public class ConstructQueueFunctionFactory<E> {

	private Consumer<Consumer<E>> queue;
	
	public ConstructQueueFunctionFactory(Consumer<Consumer<E>> queue) {
		this.queue = queue;
	}
	
	public ConstructQueueFunction<E> create(Class<? extends Consumer<E>> clazz, Object[] extraArgs, Class<?> ... argTypes) 
			throws IllegalArgumentException {
		try {
			return new ConstructQueueFunction<E>(clazz.getConstructor(argTypes), queue, extraArgs);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public ConstructQueueFunction<E> create(Class<? extends Consumer<E>> clazz, Class<?>... argTypes) 
			throws IllegalArgumentException {
		return create(clazz, new Object[0], argTypes);
	}
	
}
