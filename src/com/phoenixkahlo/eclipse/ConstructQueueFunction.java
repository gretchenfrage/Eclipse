package com.phoenixkahlo.eclipse;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Consumer;

import com.phoenixkahlo.networking.Function;
import com.phoenixkahlo.networking.ProtocolViolationException;
import com.phoenixkahlo.utils.ArrayUtils;

/**
 * A function that constructs a Consumer<E> and queues it.
 */
public class ConstructQueueFunction<E> implements Function {

	private Constructor<? extends Consumer<E>> constructor;
	private Consumer<Consumer<E>> queue;
	private Object[] extraArgs; // Arguments that go to the constructor before the received arguments.
	
	public ConstructQueueFunction(Constructor<? extends Consumer<E>> constructor, Consumer<Consumer<E>> queue, Object... extraArgs) {
		this.constructor = constructor;
		this.queue = queue;
		this.extraArgs = extraArgs;
	}
	
	@Override
	public void invoke(Object... args) throws ProtocolViolationException {
		args = ArrayUtils.concatenate(extraArgs, args, Object.class);
		Consumer<E> event = null; 
		try {
			event = constructor.newInstance(args);
		} catch (IllegalArgumentException e) {
			throw new ProtocolViolationException(e);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
		queue.accept(event);
	}

	@Override
	public Class<?>[] getArgTypes() {
		Class<?>[] constructorArgs = constructor.getParameterTypes();
		return Arrays.copyOfRange(constructorArgs, extraArgs.length, constructorArgs.length);
	}

}
