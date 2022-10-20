package ablesebogen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.Getter;
import lombok.Setter;

//Wrapper für die verwendete Liste, außerdem verantwortlich für den Export/Import mit den Methoden "export<Dateiformat>()
public class AbleseList {

	private static ObjectMapper obMap=new ObjectMapper();
	private static final String FILE = "target/Ablesewerte.json";
	private static final String XMLFILE = "target/Ablesewerte.xml";
	private static final String CSVFILE = "target/Ablesewerte.csv";

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
	
	public int indexOf(AbleseEntry entry) {
		return liste.indexOf(entry);
	}
	
	public AbleseEntry remove(AbleseEntry entry) {
		int index=indexOf(entry);
		if (index<0) {
			return null;
		}
		return remove(index);
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
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				obMap.setDateFormat(df);

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

			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			obMap.setDateFormat(df);
			obMap.writerWithDefaultPrettyPrinter().writeValue(new File(FILE), this);			
			
			System.out.format("Datei %s erzeugt\n", FILE);
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void exportXML() {
		try {
			XmlMapper xmlMapper = new XmlMapper();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			xmlMapper.setDateFormat(df);

			xmlMapper.writerWithDefaultPrettyPrinter().writeValue(new File(XMLFILE),liste);
			System.out.format("Datei %s erzeugt\n", XMLFILE);
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void exportCSV() {
		try {
			final BufferedWriter out = new BufferedWriter(new FileWriter(CSVFILE, StandardCharsets.UTF_8));
		    for (final AbleseEntry entry : liste) {
		    	out.write(entry.getKundenNummer());
		    	out.write(";");
		    	out.write(entry.getZaelerArt());
		    	out.write(";");
		    	out.write(""+entry.getZaelernummer());
		    	out.write(";");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    	out.write(df.format(entry.getDatum()));
		    	out.write(";");
		    	out.write(""+entry.getNeuEingebaut());
		    	out.write(";");
		    	out.write(""+entry.getZaelerstand());
		    	out.write(";");
		    	out.write(entry.getKommentar());
		    	out.write("\n");
		    }
		    out.close();
		    System.out.format("Datei %s erzeugt\n", CSVFILE);
		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(1);
		   }
	}
}
