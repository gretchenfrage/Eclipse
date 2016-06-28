package com.phoenixkahlo.eclipse;

import java.util.function.Consumer;

public class QueueFunctionFactory<E> {

	private Consumer<Consumer<E>> queue;
	
	public QueueFunctionFactory(Consumer<Consumer<E>> queue) {
		this.queue = queue;
	}
	
	public QueueFunction<E> create(Class<? extends Consumer<E>> clazz, Object[] extraArgs, Class<?> ... argTypes) 
			throws IllegalArgumentException {
		try {
			return new QueueFunction<E>(clazz.getConstructor(argTypes), queue, extraArgs);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public QueueFunction<E> create(Class<? extends Consumer<E>> clazz, Class<?>... argTypes) 
			throws IllegalArgumentException {
		return create(clazz, new Object[0], argTypes);
	}
	
}
