package client.zaehlerart;

import javax.swing.table.AbstractTableModel;

import lombok.Getter;

/* Hilfsklasse für die Ausgabeliste, */

@SuppressWarnings("serial")
public class ZaehlerartTableModel extends AbstractTableModel {

	private String[] columnNames = {"Name", "Warnvalue" };

	@Getter
	private ZaehlerartList myList;

	public ZaehlerartTableModel(ZaehlerartList liste) {
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
			temp = myList.getListe().get(row).getName();
		} else if (col == 1) {
			temp = myList.getListe().get(row).getWarnValue();
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
			return String.class;
		case 2:
			return Integer.class;
		default:
			return String.class;

		}
	}

}
