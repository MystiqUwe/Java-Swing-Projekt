package ablesebogen;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/* Hilfsklasse für die Ausgabeliste, */
public class AbleseTableModel extends AbstractTableModel {
	private String[] columnNames = { "Kundennummer", "Zählerart", "Zählernummer", "Datum", "neu eingebaut",
			"Zählerstand", "Kommentar" };
	private ArrayList<AbleseEntry> myList;

	public AbleseTableModel(AbleseList liste) {
		myList = liste.getListe();
		
	}

	@Override
	public int getRowCount() {
		int size;
		if (myList == null) {
			size = 0;
		} else {
			size = myList.size();
		}
		return size;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		Object temp = null;
		if (col == 0) {
			temp = myList.get(row).getKundenNummer();
		} else if (col == 1) {
			temp = myList.get(row).getZaelerArt();
		} else if (col == 2) {
			temp = myList.get(row).getZaelernummer();
		} else if (col == 3) {
			temp=myList.get(row).getDatum();
		} else if (col == 4) {
			temp = (myList.get(row).getNeuEingebaut() ? "Ja" : "Nein");
		} else if (col == 5) {
			temp = myList.get(row).getZaelerstand();
		} else if (col == 6) {
			temp = myList.get(row).getKommentar();
		}
		return temp;
	}
	
	@Override
	public Class getColumnClass(int column) {
		switch (column) {
		case 2:
		case 5:
			return Integer.class;
		case 3:
			return Date.class;
		default:
			return String.class;
				
		}
	}

}
