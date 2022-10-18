package ablesebogen;

import javax.swing.JTextArea;

public class AbleseOutList extends JTextArea{

	public AbleseOutList() {
		this.setEditable(false);
	}

	public void clear() {
		this.setText("");
	}

	public void addEntry(AbleseEntry ent) {
		this.append(String.format("%s\t%s\t%d\t%s\t%b\t%d\t%s\n",
				ent.getKundenNummer(),ent.getZaelerArt(),ent.getZaelernummer(),ent.getDatum().toString(),false,ent.getZaelerstand(),ent.getKommentar()));
	}

	public void showList(AbleseList list) {
		clear();
		list.stream().forEach(ent ->addEntry(ent));
	}


}
