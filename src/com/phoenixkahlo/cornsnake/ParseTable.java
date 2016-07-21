package com.phoenixkahlo.cornsnake;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ParseTable {

	private Map<String, Object> contents = new HashMap<String, Object>();
	
	public ParseTable(Stack<Token> tokens) throws ParseException {
		if (!tokens.pop().isA(TokenType.TABLE_START))
			throw new ParseException("table must start with a table start");
		
		if (tokens.peek().isA(TokenType.TABLE_END)) {
			tokens.pop();
			return;
		}
		
		boolean done = false;
		while (!done) {
			Token variableToken = tokens.pop();
			if (!variableToken.isA(TokenType.VARIABLE))
				throw new ParseException("expected variable name but parsed " + variableToken);
			String variableName = variableToken.getContents();
			
			if (!tokens.pop().isA(TokenType.EQUALS))
				throw new ParseException("expected equals token");
			
			Object value = Parser.parse(tokens);
			contents.put(variableName, value);
			
			Token next = tokens.pop();
			if (next.isA(TokenType.TABLE_END))
				done = true;
			else if (!next.isA(TokenType.COMMA))
				throw new ParseException("items in table cannot be followed by " + next);
		}
	}
	
	public Object get(String name) {
		return contents.get(name);
	}
	
	public boolean getBoolean(String name) {
		return (boolean) contents.get(name);
	}
	
	public double getDouble(String name) {
		return (double) contents.get(name);
	}
	
	public float getFloat(String name) {
		return (float) getDouble(name);
	}
	
	public int getInt(String name) {
		return (int) getDouble(name);
	}
	
	public String getString(String name) {
		return (String) contents.get(name);
	}
	
	public ParseList getList(String name) {
		return (ParseList) contents.get(name);
	}
	
	public ParseTable getTable(String name) {
		return (ParseTable) contents.get(name);
	}
	
	public boolean has(String name) {
		return contents.containsKey(name);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		builder.append('\n');
		for (Map.Entry<String, Object> entry : contents.entrySet()) {
			builder.append(entry.getKey());
			builder.append(" = ");
			if (entry.getValue() instanceof String)
				builder.append('"');
			builder.append(entry.getValue());
			if (entry.getValue() instanceof String)
				builder.append('"');
			builder.append(',');
			builder.append('\n');
		}
		builder.deleteCharAt(builder.length() - 2);
		builder.append('}');
		
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
	
}
