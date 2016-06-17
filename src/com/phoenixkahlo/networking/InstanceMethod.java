package com.phoenixkahlo.networking;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InstanceMethod implements Function {

	private Object object; // Nullable to represent static methods
	private Method method;
	
	public InstanceMethod(Object object, Method method) {
		this.object = object;
		this.method = method;
	}
	
	public InstanceMethod(Object object, String name, Class<?> ... argTypes) {
		this.object = object;
		try {
			method = object.getClass().getMethod(name, argTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
		if (!method.isAccessible())
			throw new IllegalArgumentException(method + " is inaccessible");
		if (method.getExceptionTypes().length > 0)
			throw new IllegalArgumentException(method + " throws exceptions");
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
		} catch (IllegalArgumentException e) {			
			throw new ProtocolViolationException(e);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	}
	
}
