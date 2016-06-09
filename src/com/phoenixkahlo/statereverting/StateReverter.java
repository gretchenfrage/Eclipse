package com.phoenixkahlo.statereverting;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class StateReverter {

	private int length; // The number of total states to save
	private int index = 0; // The index of states to save to next
	private float[] times; // The times of each state save
	private Map<ObjectField, Object[]> saves = new HashMap<ObjectField, Object[]>(); // The state saves
	private Consumer<Float> progressor; // Updates the state of the world by float time.
	
	public StateReverter(Consumer<Float> progressor, int length) {
		this.progressor = progressor;
		this.length = length;
		times = new float[length];
		for (int i = 0; i < times.length; i++) {
			times[i] = -1;
		}
	}
	
	public void addField(ObjectField field) {
		field.breakSecurity();
		saves.put(field, new Object[length]);
	}
	
	public void removeField(ObjectField field) {
		saves.remove(field);
	}
	
	public void save(float time) {
		times[index] = time;
		for (ObjectField field : saves.keySet()) {
			saves.get(field)[index] = field.get();
		}
		index++;
		index %= length;
	}
	
	/**
	 * If state cannot be reverted to
	 */
	public void revert(float time) throws IllegalArgumentException {
		// Find the index of the last state before time
		int scan = index - 1;
		while ()
	}
	
}
