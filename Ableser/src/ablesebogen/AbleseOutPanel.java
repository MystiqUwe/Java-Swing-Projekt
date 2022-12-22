package ablesebogen;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.DefaultRowSorter;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableRowSorter;

/*Eine Übersichtsliste von AbleseEntrys "outLayout"
 * -Ein Button in der Fußleiste um einen neuen Datensatz anzulegen
 * -Doppelklick auf einen Datensatz um ihn zu bearbeiten
 * 
 *  Wenn keine Datensätze vorhanden sind kann die Liste nicht geöffnet werden
 *  
 *  openTable() öffnet die Liste, optional mit einem Filterparameter, dann werden nur die Daten
 *  angezeigt bei denen die Kundennummer mit diesem Filter beginnt
*/
public class AbleseOutPanel extends JAblesebogenPanel {

	public Ablesebogen baseFrame;
	
	private AbleseTableModel tableModel;
	private RowSorter<AbleseTableModel> sorter;
	private JTable outList;
	
	/* bFrame: Basisframe in dem das Panel einfefügt wird, ein CardLayout
	 * liste: Die anzuzeigende Liste
	 */
	public AbleseOutPanel(Ablesebogen bFrame, AbleseList liste) {
		super(new BorderLayout());
		baseFrame=bFrame;
		//out Layout Base Layout
		
		//out Layout Komponenten
		
		//Button Leiste
		JPanel buttonPanel=new JPanel(new GridLayout(1,2));
		this.add(buttonPanel,BorderLayout.SOUTH);
		
		JButton toInButton=new JButton("neuer Datensatz");
		JButton editButton=new JButton("bearbeiten");
		JButton filButton=new JButton("Filtern");

		buttonPanel.add(toInButton);
		buttonPanel.add(filButton);
		buttonPanel.add(editButton);

		toInButton.addActionListener(e -> {
			baseFrame.openPage(Ablesebogen.ABLESUNG_IN);
		});
		
		filButton.addActionListener(e -> {
			Util.questionMessage(baseFrame.getKundenNrData());
		});
		
		editButton.addActionListener(e-> edit());
		
		//Tabelle
		tableModel = new AbleseTableModel(liste);
		outList=new JTable(tableModel);
		outList.setAutoCreateRowSorter(true);
		sorter = new TableRowSorter<AbleseTableModel>(tableModel);
		outList.setRowSorter(sorter);
		
		
	    JScrollPane scrollPane = new JScrollPane(outList);
	    scrollPane.setPreferredSize(new Dimension(380,280));
		this.add(scrollPane);

		outList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()!=2) {
					return; //nur Doppelklick führt zum editieren
				}
				edit();
			}
		});

	}
	
	private void edit() {
		int row =outList.getSelectedRow();
		if (row<0) return;
		baseFrame.openPage(Ablesebogen.ABLESUNG_IN,tableModel.getMyList().get(outList.convertRowIndexToModel(row)));
		
	}
	
	public void refresh() {
		tableModel.fireTableDataChanged();
	}

	/** 
	 * Öffnet die Liste, mit einem Filterparameter, es werden nur die Daten
	 * angezeigt bei denen die Kundennummer mit diesem Filter beginnt
	 * 
	 * @param filter
	 */
	private void openTable(UUID filter) {
		RowFilter<AbleseTableModel, Object> rf = null;
	    try {
	        rf = RowFilter.regexFilter(filter.toString(),0);
	    } catch (java.util.regex.PatternSyntaxException e) {
	        return;
	    }
		tableModel.fireTableDataChanged();
		ArrayList<RowSorter.SortKey> sortList = new ArrayList<RowSorter.SortKey>();
		sortList.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) );
		sortList.add( new RowSorter.SortKey(1, SortOrder.ASCENDING) );
		sortList.add( new RowSorter.SortKey(3, SortOrder.ASCENDING) );
		sorter.setSortKeys(sortList);
	    ((DefaultRowSorter<AbleseTableModel, Integer>) sorter).setRowFilter(rf);
	}

	@Override
	public boolean activate(Object eOpts) {
		if (tableModel.getRowCount()<1) {
			Util.errorMessage("Liste konnte nicht angezeigt werden");
			return false;
		}
		if (eOpts instanceof UUID) {
			openTable((UUID) eOpts);
			baseFrame.setTitle("Ablesungen für "+eOpts.toString());
		} else{
			baseFrame.setTitle("Übersichtsliste Ablesungen");
		}
		refresh();

		return true;
	}
}
