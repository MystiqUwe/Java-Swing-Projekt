package de.gc.swing.simple.json;

import java.util.ArrayList;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.Setter;

public class SimpleToDoList {

	@Getter
	@Setter
	private ArrayList<ToDoEntry> liste = new ArrayList<ToDoEntry>();

	public boolean add(ToDoEntry e) {
		return liste.add(e);
	}

	public void clear() {
		liste.clear();
	}

	public ToDoEntry get(int index) {
		return liste.get(index);
	}

	public ToDoEntry remove(int index) {
		return liste.remove(index);
	}

	public int size() {
		return liste.size();
	}

	public Stream<ToDoEntry> stream() {
		return liste.stream();
	}

	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		liste.stream().forEach(en -> buf.append(en.toString()));
		return buf.toString();
	}
}
