package ablesebogen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.TableRowSorter;

@SuppressWarnings("serial")
public class KundeOutPanel extends JAblesebogenPanel {

	private KundeTableModel tableModel;
	private RowSorter<KundeTableModel> sorter;
	private JTable outList;

	/*
	 * bFrame: Basisframe in dem das Panel einfefügt wird, ein CardLayout liste: Die
	 * anzuzeigende Liste
	 */
	public KundeOutPanel(Ablesebogen bFrame, KundeList liste) {
		super(new BorderLayout());
		baseFrame = bFrame;
		// out Layout Base Layout

		// out Layout Komponenten

		// Button Leiste
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
		this.add(buttonPanel, BorderLayout.SOUTH);

		JButton toInButton = new JButton("neuer Datensatz");
		JButton editButton = new JButton("bearbeiten");

		buttonPanel.add(toInButton);
		buttonPanel.add(editButton);

		editButton.addActionListener(e -> edit());

		toInButton.addActionListener(e -> {
			baseFrame.openPage(Ablesebogen.KUNDE_IN);
		});

		// editButton.addActionListener(e-> edit());

		// Tabelle
		tableModel = new KundeTableModel(liste);
		outList = new JTable(tableModel);
		outList.setAutoCreateRowSorter(true);
		sorter = new TableRowSorter<KundeTableModel>(tableModel);
		outList.setRowSorter(sorter);

		JScrollPane scrollPane = new JScrollPane(outList);
		scrollPane.setPreferredSize(new Dimension(380, 280));
		this.add(scrollPane);

		outList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() != 2) {
					return; // nur Doppelklick führt zum editieren
				}
				edit();
			}
		});

	}

	public void refresh() {
		tableModel.fireTableDataChanged();
	}

	private void edit() {
		int row = outList.getSelectedRow();
		if (row < 0)
			return;
		baseFrame.openPage(Ablesebogen.KUNDE_IN, tableModel.getMyList().get(outList.convertRowIndexToModel(row)));
	}

	@Override
	public boolean activate(Object eOpts) {
		if (tableModel.getRowCount() < 1) {
			Util.errorMessage("Liste konnte nicht angezeigt werden");
			return false;
		}

		baseFrame.setTitle("Übersichtsliste Kunden");
		refresh();
		return true;
	}
}
