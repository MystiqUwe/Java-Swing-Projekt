package ablesebogen;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import client.Service;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import server.Kunde;

public class Util {
	
	private JDialog filterJDialog;

	public static void handleTabOrder(ArrayList<JComponent> list,Function<Void,Boolean> saveMethod) {
		if (list==null || list.size()<2) {
			return;
		}
		for (int i = 0; i < list.size()-1; i++) {
			final JComponent nextItem=list.get(i+1);
			list.get(i).addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						nextItem.requestFocus();
					}
				}
			});
		}
		
		list.get(list.size()-1).addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (saveMethod.apply(null)) {
						list.get(0).requestFocus();
					}

				}
			}
		});
	}

	private static JFrame dialogFrame = new JFrame();
	public static void errorMessage(String message) {
		JOptionPane.showMessageDialog(dialogFrame, message, "", JOptionPane.ERROR_MESSAGE);
	}
	public static boolean optionMessage(String message) {
		//int result = 0;
		int result = JOptionPane.showConfirmDialog(dialogFrame, message, "",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		return JOptionPane.OK_OPTION==result;
	}

	public static void questionMessage(Kunde[] kunde, Service service) {
		JDialog filterJDialog = new JDialog();
		filterJDialog.setLayout(new GridLayout(7, 2));
		//JComboBox kundenNummer = new JComboBox<>(Ablesebogen.getKundenNrData());
		JComboBox<Kunde> kundenNummer = new JComboBox<Kunde>(kunde);
		kundenNummer.setRenderer(new ListCellRenderer<Kunde>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Kunde> list, Kunde value, int index,
					boolean isSelected, boolean cellHasFocus) {
				if (value==null) {
					return new JLabel("");
				}
				String nameundvorname = (value.getName() + ", " + value.getVorname() + " -> " + value.getId().toString());
				JLabel label = new JLabel(nameundvorname);
				if (isSelected) {
					label.setIcon(new ImageIcon(getClass().getResource("check.png")));
				}
				return label;
			}
		});
		JTextField textID;
		JDatePickerImpl datePickerstart;
		JDatePickerImpl datePickerend;
		JDatePanelImpl datePanel1 = null;
		JDatePanelImpl datePanel2 = null;
		JButton buttonfilter;
		UtilDateModel model1;
		model1 = new UtilDateModel();
		model1.setSelected(true);
		UtilDateModel model2;
		model2 = new UtilDateModel();
		model2.setSelected(true); // init DatePicker Value
		datePanel1 = new JDatePanelImpl(model1);
		datePanel2 = new JDatePanelImpl(model2);
		//textID = new JTextField();
		buttonfilter = new JButton();
		buttonfilter.setText("Filtern");
		datePickerstart = new JDatePickerImpl(datePanel1);
		datePickerstart.setTextEditable(true);
		datePickerend = new JDatePickerImpl(datePanel2);
		datePickerend.setTextEditable(true);
		filterJDialog.setTitle("Filter: ");
		filterJDialog.setSize(400,400);
		filterJDialog.setModal(true);
		filterJDialog.add(new JLabel("ID: "));
		filterJDialog.add(kundenNummer);
		filterJDialog.add(new JLabel("Start Datum:"));
		filterJDialog.add(datePickerstart);
		filterJDialog.add(new JLabel("End Datum:"));
		filterJDialog.add(datePickerend);
		filterJDialog.add(buttonfilter);
		filterJDialog.setVisible(true);
		
		buttonfilter.addActionListener(e -> {
			Kunde selectedItem = (Kunde) kundenNummer.getSelectedItem();
			service.get("ablesungen/" + selectedItem.getId() );
			});
	}


	public static LocalDate dateToLocalDate(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date localDatetoDate(LocalDate dateToConvert) {
		return Date.from(dateToConvert.atStartOfDay().toInstant(ZoneOffset.UTC));
	}

}
