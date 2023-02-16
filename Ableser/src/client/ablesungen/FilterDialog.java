package client.ablesungen;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.Point;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import client.Ablesebogen;
import client.Util;
import dataEntities.Kunde;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class FilterDialog {
	private static final int WIDTH = 400;
	private static final int HEIGHT = 130;
	private static final int ROWS = 3;
	private static final int COLUMNS = 2;

	private final JDialog dialog;
	private final JComboBox<Kunde> kundenNummer;
	private final JDatePickerImpl startDatePicker;
	private final JDatePickerImpl endDatePicker;
	private final JButton filterButton;

	private Kunde allKunde;
	public FilterDialog(Kunde[] kunden, Ablesebogen baseFrame) {
		allKunde=new Kunde("alle","Alle");
		
		dialog = new JDialog();
		dialog.setLayout(new BorderLayout());
		
		JPanel grid=new JPanel(new GridLayout(ROWS, COLUMNS));

		dialog.add(grid,BorderLayout.CENTER);
		
		kundenNummer = new JComboBox<>(this.addAllKunden(kunden));
		UtilDateModel startDateModel = new UtilDateModel();
		startDateModel.setDate(LocalDate.now().getYear()-2, 0, 1);
		startDateModel.setSelected(true);
		UtilDateModel endDateModel = new UtilDateModel();
		endDateModel.setSelected(true);
		JDatePanelImpl startDatePanel = new JDatePanelImpl(startDateModel);
		JDatePanelImpl endDatePanel = new JDatePanelImpl(endDateModel);
		filterButton = new JButton("Filtern");
		startDatePicker = new JDatePickerImpl(startDatePanel);
		startDatePicker.setTextEditable(true);
		endDatePicker = new JDatePickerImpl(endDatePanel);
		endDatePicker.setTextEditable(true);

		dialog.setTitle("Filter: ");
		grid.add(new JLabel("ID: "));
		grid.add(kundenNummer);
		grid.add(new JLabel("Start Datum:"));
		grid.add(startDatePicker);
		grid.add(new JLabel("End Datum:"));
		grid.add(endDatePicker);
		
		dialog.add(filterButton, BorderLayout.SOUTH);

		filterButton.addActionListener(e -> {
			Kunde selectedItem = (Kunde) kundenNummer.getSelectedItem();
			LocalDate startDate = Util.dateToLocalDate((Date) startDatePicker.getModel().getValue());
			LocalDate endDate = Util.dateToLocalDate((Date) endDatePicker.getModel().getValue());
			ArrayList<String[]> queryParam = new ArrayList<String[]>();

			if (!allKunde.equals( selectedItem)) {
				queryParam.add(Util.createPair("kunde", selectedItem.getId().toString()));
			}			
			//queryParam.add(Util.createPair("kunde", selectedItem.getId().toString()));
			queryParam.add(Util.createPair("beginn", startDate.toString()));
			queryParam.add(Util.createPair("ende", endDate.toString()));

			baseFrame.loadData(queryParam);
		});

		kundenNummer.setRenderer(new ListCellRenderer<Kunde>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Kunde> list, Kunde value, int index,
					boolean isSelected, boolean cellHasFocus) {
				if (value == null) {
					return new JLabel("");
				}
				JLabel label;
				if (allKunde.equals(value)) {
					label = new JLabel("Alle Kunden");
				} else {
					String nameundvorname = (value.getName() + ", " + value.getVorname() + " -> "
						+ value.getId().toString());
					label = new JLabel(nameundvorname);
				}
				if (isSelected) {
					label.setIcon(new ImageIcon(getClass().getResource("check.png")));
				}
				return label;
			}
		});

		baseFrame.getKundenListe().addChangeListener(e -> {
			Object k=kundenNummer.getSelectedItem();
			
			DefaultComboBoxModel<Kunde> model = new DefaultComboBoxModel<Kunde>(addAllKunden(e.toArray(new Kunde[0])));
			kundenNummer.setModel(model);
			
			if ((k!=null) && ((e.indexOf(k)>=0)|| k.equals(allKunde))) {
				kundenNummer.setSelectedItem(k);
			} else {
				kundenNummer.setSelectedItem(null);
			}
			return true;
		});
		
		dialog.setSize(WIDTH, HEIGHT);
		dialog.setModalityType(ModalityType.MODELESS);
		
		
//		dialog.setLocation();
		Point baseLocation=baseFrame.getLocation();
		dialog.setLocation((int)baseLocation.getX(), (int)baseLocation.getY()+baseFrame.getHeight());
		
		
		dialog.setVisible(true);
	}
	
	private Kunde[] addAllKunden(Kunde[] oldKunden ) {
		Kunde[] kArray=new Kunde[oldKunden.length+1];
		
		kArray[0]=allKunde;
		//kArray[0].setId(null);
		for (int i = 1; i < kArray.length; i++) {
			kArray[i]=oldKunden[i-1];
		}
		return kArray;
	}
}