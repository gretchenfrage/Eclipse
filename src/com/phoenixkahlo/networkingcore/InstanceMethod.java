package com.phoenixkahlo.networkingcore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InstanceMethod implements Function {

	private Object object; // Nullable to represent static methods
	private Method method;
	
	public InstanceMethod(Object object, String name, Class<?> ... argTypes) {
		this.object = object;
		try {
			method = object.getClass().getMethod(name, argTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public InstanceMethod(Class<?> clazz, String name, Class<?> ... argTypes) {
		try {
			method = clazz.getMethod(name, argTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	@Override
	public Class<?>[] getArgTypes() {
		return method.getParameterTypes();
	}

	@Override
	public void invoke(Object ... args) throws ProtocolViolationException {
		try {
			method.invoke(object, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ProtocolViolationException(e);
		}
	}
	
}
