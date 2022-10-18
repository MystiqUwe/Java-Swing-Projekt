package ablesebogen;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class AbleseTableModel extends AbstractTableModel {
	 private String[] columnNames = {"Kundennummer","Zählerart","Zählernummer", "Datum", "neu eingebaut", "Zählerstand", "Kommentar"};
	   private ArrayList<AbleseEntry> myList;
	   public AbleseTableModel(AbleseList liste) {
	      myList = liste.getItems();
	   }
	@Override
	   public int getRowCount() {
	      int size;
	      if (myList == null) {
	         size = 0;
	      }
	      else {
	         size = myList.size();
	      }
	      return size;
	   }
	@Override
	   public int getColumnCount() {
	      return columnNames.length;
	   }
	@Override
	   public Object getValueAt(int row, int col) {
	      Object temp = null;
	      if (col == 0) {
	         temp = myList.get(row).getKundenNummer();
	      }
	      else if (col == 1) {
	         temp = myList.get(row).getZaelerArt();
	      }
	      else if (col == 2) {
	         temp = myList.get(row).getZaelernummer();
	      }
	      else if (col == 3) {
		         temp = myList.get(row).getDatum();
		      }
	      else if (col == 4) {
		         temp = myList.get(row).getNeuEingebaut();
		      }
	      else if (col == 5) {
		         temp = myList.get(row).getZaelerstand();
		      }
	      else if (col == 6) {
		         temp = myList.get(row).getKommentar();
		      }
	      return temp;
	   }
	   
}
