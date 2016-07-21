package com.phoenixkahlo.cornsnake;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

public class ParseList implements Iterable<Object> {

	private List<Object> contents = new ArrayList<Object>();
	
	public ParseList(Stack<Token> tokens) throws ParseException {
		if (!tokens.pop().isA(TokenType.LIST_START))
			throw new ParseException("list must start with a list start");
		
		if (tokens.peek().isA(TokenType.LIST_END)) {
			tokens.pop();
			return;
		}
		
		boolean done = false;
		while (!done) {
			contents.add(Parser.parse(tokens));
			Token next = tokens.pop();
			if (next.isA(TokenType.LIST_END))
				done = true;
			else if (!next.isA(TokenType.COMMA))
				throw new ParseException("items in list must be followed by delimiter or list end");
		}
	}
	
	public Object get(int i) {
		return contents.get(i);
	}
	
	public boolean getBoolean(int i) {
		return (boolean) contents.get(i);
	}
	
	public double getDouble(int i) {
		return (double) contents.get(i);
	}
	
	public float getFloat(int i) {
		return (float) getDouble(i);
	}
	
	public int getInt(int i) {
		return (int) getDouble(i);
	}
	
	public String getString(int i) {
		return (String) contents.get(i);
	}
	
	public ParseList getList(int i) {
		return (ParseList) contents.get(i);
	}
	
	public ParseTable getTable(int i) {
		return (ParseTable) contents.get(i);
	}
	
	public int size() {
		return contents.size();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		builder.append('\n');
		for (int i = 0; i < contents.size(); i++) {
			if (contents.get(i) instanceof String)
				builder.append('"');
			builder.append(contents.get(i));
			if (contents.get(i) instanceof String)
				builder.append('"');
			if (i < contents.size() - 1)
				builder.append(',');
			builder.append('\n');
		}
		builder.append(']');
		
		String[] lines = builder.toString().split("\n");
		for (int i = 1; i < lines.length - 1; i++) {
			lines[i] = "    " + lines[i];
		}
		
		builder = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			builder.append(lines[i]);
			if (i < lines.length - 1)
				builder.append('\n');
		}
		
		return builder.toString();
	}

	@Override
	public Iterator<Object> iterator() {
		return contents.iterator();
	}

	public Stream<Object> stream() {
		return contents.stream();
	}
	
}
