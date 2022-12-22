package ablesebogen;

import java.awt.Component;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import server.Kunde;

public class FilterDialog {
	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;
	private static final int ROWS = 7;
	private static final int COLUMNS = 2;

	private final JDialog dialog;
	private final JComboBox<Kunde> kundenNummer;
	private final JDatePickerImpl startDatePicker;
	private final JDatePickerImpl endDatePicker;
	private final JButton filterButton;

	public FilterDialog(Kunde[] kunden, Ablesebogen baseFrame) {
		dialog = new JDialog();
		dialog.setLayout(new GridLayout(ROWS, COLUMNS));

		kundenNummer = new JComboBox<>(kunden);
		UtilDateModel startDateModel = new UtilDateModel();
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
		dialog.add(new JLabel("ID: "));
		dialog.add(kundenNummer);
		dialog.add(new JLabel("Start Datum:"));
		dialog.add(startDatePicker);
		dialog.add(new JLabel("End Datum:"));
		dialog.add(endDatePicker);
		dialog.add(filterButton);

		filterButton.addActionListener(e -> {
			Kunde selectedItem = (Kunde) kundenNummer.getSelectedItem();
			LocalDate startDate = Util.dateToLocalDate((Date) startDatePicker.getModel().getValue());
			LocalDate endDate = Util.dateToLocalDate((Date) endDatePicker.getModel().getValue());
			ArrayList<String[]> queryParam = new ArrayList<String[]>();

			queryParam.add(Util.createPair("kunde", selectedItem.getId().toString()));
			queryParam.add(Util.createPair("beginn", startDate.toString()));
			queryParam.add(Util.createPair("ende", endDate.toString()));

			baseFrame.getListe().refresh(queryParam);
			baseFrame.outLayout.refresh();
		});

		kundenNummer.setRenderer(new ListCellRenderer<Kunde>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Kunde> list, Kunde value, int index,
					boolean isSelected, boolean cellHasFocus) {
				if (value == null) {
					return new JLabel("");
				}
				String nameundvorname = (value.getName() + ", " + value.getVorname() + " -> "
						+ value.getId().toString());
				JLabel label = new JLabel(nameundvorname);
				if (isSelected) {
					label.setIcon(new ImageIcon(getClass().getResource("check.png")));
				}
				return label;
			}
		});

		dialog.setSize(WIDTH, HEIGHT);
		dialog.setModal(true);
		dialog.setVisible(true);
	}
}