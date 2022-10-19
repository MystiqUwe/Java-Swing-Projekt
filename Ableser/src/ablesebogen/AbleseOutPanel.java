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

public class AbleseOutPanel extends JPanel {

	public Ablesebogen baseFrame;
	
	private AbleseTableModel tableModel;
	private RowSorter<AbleseTableModel> sorter;
	private JTable outList;

	public AbleseOutPanel(Ablesebogen bFrame, AbleseList liste) {
		super(new BorderLayout());
		baseFrame=bFrame;
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
	
	public void updateTable() {
		tableModel.fireTableDataChanged();
	}
	
	public void updateTable(String filter) {
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
	    
	}
}
