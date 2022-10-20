package ablesebogen;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultRowSorter;
import javax.swing.JButton;
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
public class AbleseOutPanel extends JPanel {

	public Ablesebogen baseFrame;
	
	private AbleseTableModel tableModel;
	private RowSorter<AbleseTableModel> sorter;
	private JTable outList;
	
	private String card;

	/* bFrame: Basisframe in dem das Panel einfefügt wird, ein CardLayout
	 * liste: Die anzuzeigende Liste
	 * card: Der Name der CardLayout Card die dieses Objekt repräsentieren soll
	 */
	public AbleseOutPanel(Ablesebogen bFrame, AbleseList liste, String card) {
		super(new BorderLayout());
		baseFrame=bFrame;
		this.card=card;
		//out Layout Base Layout
		
		//out Layout Komponenten
		JButton toInButton=new JButton("neuer Datensatz");
		this.add(toInButton,BorderLayout.SOUTH);
		
		toInButton.addActionListener(e -> {
			baseFrame.setTitle("neuer Datensatz");
			((CardLayout) baseFrame.getContentPane().getLayout()).show(baseFrame.getContentPane(),"in");
		});
		
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
				baseFrame.loadWithValue(liste.get(outList.getSelectedRow()));
				((CardLayout) baseFrame.getContentPane().getLayout()).show(baseFrame.getContentPane(),"in");				
			}
		});

	}
	
	/* öffnet die Liste*/
	public void openTable() {
		tableModel.fireTableDataChanged();
		((CardLayout) baseFrame.getContentPane().getLayout()).show(baseFrame.getContentPane(),card);				
	}
	
	/*öffnet die Liste, mit einem Filterparameter, es werden nur die Daten
	 *  angezeigt bei denen die Kundennummer mit diesem Filter beginnt*/
	public void openTable(String filter) {
		RowFilter<AbleseTableModel, Object> rf = null;
		tableModel.fireTableDataChanged();
	    try {
	        rf = RowFilter.regexFilter(filter,0);
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
		((CardLayout) baseFrame.getContentPane().getLayout()).show(baseFrame.getContentPane(),card);				
	    
	}
}
