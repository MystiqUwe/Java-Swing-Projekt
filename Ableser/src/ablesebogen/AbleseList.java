package ablesebogen;

import java.util.ArrayList;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.Setter;

public class AbleseList {

	@Getter
	@Setter
	private ArrayList<AbleseEntry> liste = new ArrayList<AbleseEntry>();

	public boolean add(AbleseEntry e) {
		return liste.add(e);
	}

	public void clear() {
		liste.clear();
	}

	public AbleseEntry get(int index) {
		return liste.get(index);
	}

	public AbleseEntry remove(int index) {
		return liste.remove(index);
	}

	public int size() {
		return liste.size();
	}

	public Stream<AbleseEntry> stream() {
		return liste.stream();
	}

	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		liste.stream().forEach(en -> buf.append(en.toString()));
		return buf.toString();
	}
}
