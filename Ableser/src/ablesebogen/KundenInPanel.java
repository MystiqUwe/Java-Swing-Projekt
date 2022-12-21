package ablesebogen;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jakarta.ws.rs.core.Response;
import server.Kunde;

public class KundenInPanel extends JPanel{

	public Ablesebogen baseFrame;
	
	private Kunde toEdit;
	private JTextField nameField;
	private JTextField vornameField;
	
	public KundenInPanel(Ablesebogen bFrame) {
		super(new BorderLayout());
		
		this.baseFrame=bFrame;
		//Grid
		JPanel grid=new JPanel(new GridLayout(2, 2));
		this.add(grid,BorderLayout.CENTER);
		nameField=new JTextField();
		vornameField=new JTextField();
		
		grid.add(new JLabel("Name"));
		grid.add(nameField);
		grid.add(new JLabel("Vorname"));
		grid.add(vornameField);

		ArrayList<JComponent> tabOrder=new ArrayList();
		tabOrder.add(nameField);
		tabOrder.add(vornameField);
		Util.handleTabOrder(tabOrder, e-> {return save();});

		// untere Leiste
		JPanel buttonPanel=new JPanel(new GridLayout(1,3));
		this.add(buttonPanel,BorderLayout.SOUTH);
		JButton saveButton = new JButton("Speichern");
		JButton toOutButton = new JButton("Liste Anzeigen");
		JButton deleteButton = new JButton("Löschen");

		buttonPanel.add(saveButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(toOutButton);
		// buttonPanel.add(exportButton);
		saveButton.addActionListener(e -> {
			save();
		});

		toOutButton.addActionListener(e -> {
			if (baseFrame.getKundenListe().size() < 1) {
				Util.errorMessage("Liste konnte nicht angezeigt werden");
				return;
			}
			baseFrame.setTitle("Übersichtsliste");
		});
		/*deleteButton.addActionListener(e -> {
			liste.remove(curEntry);
			newList.remove(curEntry);
			clear();
		});
		toFilterOutButton.addActionListener(e -> {
			if (newList.size() < 1) {
				fehlerMessage("Liste konnte nicht Angezeigt werden");
				return;
			}
			
			Kunde selectedItem = (Kunde) kundenNummer.getSelectedItem();
			filterOutLayout.openTable(selectedItem.getVorname());
			this.setTitle("Daten für " + selectedItem.getVorname());
		});*/
		

		
	}
	
	/**
	 * @return soll das Speichern fortgesetzt werden
	 **/
	public boolean checkChanged(Kunde k) {
		if (k.getId()==null) {
			return true;
		}
		Response res=Ablesebogen.getService().get("kunden/"+k.getId());
		
		if (res.getStatus()!=200) {
			Util.errorMessage(res.readEntity(String.class));

			//TODO Gelöscht - neu Hochladen?
			toEdit=null;
			save();
			return false;
		}
		
		Kunde kServer=res.readEntity(Kunde.class);
		
		if (k.equals(kServer)) {
			return true;
		}
		
		//TODO Geändert - überschreiben?
		return true;
	}
	
	public boolean save() {
		String name=nameField.getText();
		String vorname=vornameField.getText();
		Kunde toSave;
		if (toEdit==null) {
			//Neuer Datensatz
			toSave=new Kunde(name,vorname);
			toSave.setId(null);
			Response res=Ablesebogen.getService().post("kunden",toSave);

			return true;
		} else {
			//Editieren
			toSave=toEdit;
			toSave.setName(name);
			toSave.setVorname(vorname);
			if (!checkChanged(toSave)) {
				return false; //Abbruch
			}
			
		}
		
		Response res=Ablesebogen.getService().put("kunden",toSave);
		
		return true;
		
	}
	
	
	public void activate(Kunde k) {
		((CardLayout) baseFrame.getContentPane().getLayout()).show(baseFrame.getContentPane(),"kundeIn");

		toEdit=k;
		if (k==null) {
			baseFrame.setTitle("Neuer Kunde");
			this.nameField.setText("");
			this.vornameField.setText("");
			return;
		}
		baseFrame.setTitle(k.getId()+" editieren");
		this.nameField.setText(k.getName());
		this.vornameField.setText(k.getVorname());
	}
}
