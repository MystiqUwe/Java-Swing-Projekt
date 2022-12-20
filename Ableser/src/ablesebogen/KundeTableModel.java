package ablesebogen;


	import java.sql.Date;
	import java.util.ArrayList;
import java.util.UUID;

import javax.swing.table.AbstractTableModel;

	import lombok.Getter;
import server.Kunde;

	/* Hilfsklasse für die Ausgabeliste, */

public class KundeTableModel extends AbstractTableModel {

		private String[] columnNames = { "Id", "Name", "Vorname" };
		
		@Getter
		private ArrayList<Kunde> myList;

		public KundeTableModel(ArrayList<Kunde> liste) {
			//myList = liste.getListe();
			myList = liste;
			
		}


		public int getColumnCount() {
			return columnNames.length;
		}
		
		/** 
		 * @return int
		 */
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
		
		/** 
		 * @return int
		 */
		public Object getValueAt(int row, int col) {
			Object temp = null;
			if (col == 0) {
				temp = myList.get(row).getId();
			} else if (col == 1) {
				temp = myList.get(row).getName();
			} else if (col == 2) {
				temp = myList.get(row).getVorname();
			}
			return temp;
		}
		

		   /** 
			* @param col
			* @return String
			*/
		   public String getColumnName(int col) {
			      return columnNames[col];
			   }
		
		/** 
		 * @param row
		 * @param col
		 * @return Object
		 */
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

