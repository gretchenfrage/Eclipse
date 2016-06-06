package com.phoenixkahlo.utils;

import java.util.List;

public class ListUtils {

	private ListUtils() {}
	
	/**
	 * @return if list contains obj such that list.get(i) == obj
	 */
	public static boolean identityContains(List<?> list, Object obj) {
		for (Object item : list)
			if (item == obj) return true;
		return false;
	}
	
}
