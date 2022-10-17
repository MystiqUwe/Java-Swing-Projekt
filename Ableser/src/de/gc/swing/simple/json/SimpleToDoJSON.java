package de.gc.swing.simple.json;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SimpleToDoJSON extends JFrame {

	private static final String FILE = "target/simpletodo.json";

	public static void main(final String[] args) {
		new SimpleToDoJSON();
	}

	private JTextField anz;
	private JTextField bez;
	private SimpleToDoList liste;

	private JTextArea text;

	public SimpleToDoJSON() {
		super("SimpleToDo JSON");

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				exit();
			}
		});

		final Container con = getContentPane();
		con.setLayout(new BorderLayout());
		final JPanel pn = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pn.add(bez = new JTextField(40));
		pn.add(anz = new JTextField(4));
		con.add(pn, BorderLayout.NORTH);

		final JPanel ps = new JPanel(new FlowLayout(FlowLayout.CENTER));
		final JButton exit = new JButton("exit");
		final JButton add = new JButton("hinzufügen");
		ps.add(exit);
		ps.add(add);
		con.add(ps, BorderLayout.SOUTH);

		final JScrollPane sp = new JScrollPane(text = new JTextArea());
		con.add(sp, BorderLayout.CENTER);
		text.setEditable(false);

		// actions
		exit.addActionListener(e -> exit());
		add.addActionListener(e -> addEntry());
		// keys
		bez.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final java.awt.event.KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					anz.requestFocus();
				}
			}
		});
		anz.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final java.awt.event.KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					addEntry();
				}
			}
		});

		load();

		pack();
		setSize(530, 200);
		setVisible(true);
	}

	private void addEntry() {
		final ToDoEntry entry = new ToDoEntry(bez.getText(), anz.getText());
		text.append(entry.toString());
		liste.add(entry);
		bez.setText("");
		anz.setText("");
		bez.requestFocus();
	}

	private void exit() {
		save();
		System.exit(0);
	}

	private void load() {
		final File f = new File(FILE);
		if (f.exists()) {
			try {

				// TODO hier fehlt was

				System.out.format("Datei %s gelesen\n", FILE);
			} catch (final Exception e) {
				e.printStackTrace();
				// ignore
			}
		}
		if (liste == null) {
			liste = new SimpleToDoList();
		}
	}

	private void save() {
		try {

			// TODO hier fehlt was

			System.out.format("Datei %s erzeugt\n", FILE);
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
