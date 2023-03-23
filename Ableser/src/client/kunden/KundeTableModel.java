package client.kunden;

import java.util.UUID;

import javax.swing.table.AbstractTableModel;

import lombok.Getter;

/* Hilfsklasse für die Ausgabeliste, */

@SuppressWarnings("serial")
public class KundeTableModel extends AbstractTableModel {

	private String[] columnNames = { "Kundennummer", "Name", "Vorname" };

	@Getter
	private KundeList myList;

	public KundeTableModel(KundeList liste) {
		// myList = liste.getListe();
		this.myList = liste;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	/**
	 * @return int
	 */
	@Override
	public int getRowCount() {
		int size;
		if (myList.getListe() == null) {
			size = 0;
		} else {
			size = myList.getListe().size();
		}
		return size;
	}

	/**
	 * @return int
	 */
	@Override
	public Object getValueAt(int row, int col) {
		Object temp = null;
		if (col == 0) {
			temp = myList.getListe().get(row).getId();
		} else if (col == 1) {
			temp = myList.getListe().get(row).getName();
		} else if (col == 2) {
			temp = myList.getListe().get(row).getVorname();
		}
		return temp;
	}

	/**
	 * @param col
	 * @return String
	 */
	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	/**
	 * @param row
	 * @param col
	 * @return Object
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int column) {
		switch (column) {
		case 1:
			return UUID.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		default:
			return String.class;

		}
	}

}
