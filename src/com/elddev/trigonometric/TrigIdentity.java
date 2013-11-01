package com.elddev.trigonometric;

import java.util.ArrayList;

public class TrigIdentity {
	private String name;
	private ArrayList<String> equations;
	
	public TrigIdentity(String name) {
		this.name = name;
		equations = new ArrayList<String>();
	}
	
	public String getName() {
		return name;
	}
	
	public TrigIdentity setName(String name) {
		this.name = name;
		return this;
	}
	
	public TrigIdentity addEq(String equation) {
		equations.add(equation);
		return this;
	}
	
	public TrigIdentity clearEquations() {
		equations.clear();
		return this;
	}
	
	public String toString() {
		String s = name;
		for(String eq : equations) { s += "\n" + eq; }
		return s;
	}
	
	public String toTextViewString() {
		String s = "";
		for(String eq : equations) { s += eq + "\n"; }
		return s.substring(0, s.length() - 1);
	}
}
