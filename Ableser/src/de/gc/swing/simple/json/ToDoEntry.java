package de.gc.swing.simple.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ToDoEntry {

	private String bez = "";
	private int anz = 0;

	public ToDoEntry(String... cols) {
		try {
			this.bez = cols[0];
			anz = Integer.parseInt(cols[1]);
		} catch (final Exception e) {
			// ignore
		}
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(bez);
		builder.append(" -> ");
		builder.append(anz);
		builder.append("\n");
		return builder.toString();
	}

}
