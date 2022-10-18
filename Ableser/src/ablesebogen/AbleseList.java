package ablesebogen;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

public class AbleseList {

	private static ObjectMapper obMap=new ObjectMapper();
	private static final String FILE = "target/Ablesewerte.json";

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
	public static AbleseList importJson() {
		final File f = new File(FILE);
		if (f.exists()) {
			try {

				AbleseList list= obMap.readValue(new File(FILE), AbleseList.class);
				
				System.out.format("Datei %s gelesen\n", FILE);
				return list;
				
			} catch (final Exception e) {
				e.printStackTrace();
				// ignore
			}
		}
		return new AbleseList();
	}

	public void exportJson() {
		try {

			obMap.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), this);			
			
			System.out.format("Datei %s erzeugt\n", FILE);
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}


}
