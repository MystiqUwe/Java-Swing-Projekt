package client;

import java.time.LocalDate;

import client.ablesungen.AbleseList;
import client.kunden.KundeList;
import client.zaehlerart.ZaehlerartList;
import dataEntities.AbleseEntry;
import dataEntities.Kunde;
import dataEntities.Zaehlerart;

public class DataCreator {

	static String[] kVornamen= {"Mia","Noah","Lara","Matteo","Lena","Theo","Moritz","Sophia","Emil","Leni","Anna","Oskar","Julian","Leonie","Anton","Nora","Hannah","Max","Luisa","Paul","Emily","Liam","Emma","Lukas","Julia","Samuel","felix","Mila","Emilia","Leon","Luca","Marie","Jonas","Lina","Ella","Leo","Maximilian","Sophie","Amelie","Milan"};
	
	public static void main(String[] args) {
		String url = "http://localhost:8081/rest";
		Service s=new Service(url);
		
		System.out.println("Daten werden befüllt");
		KundeList kList=new KundeList(s);
		
		for (int i=0;i<20;i++) {
			int num=(int) (Math.random()*kVornamen.length);
			//System.out.println();
			Kunde k=new Kunde("Kunde "+i,kVornamen[num] );
			kList.add(k);
		}
		System.out.println("-Kunden befüllt");
		ZaehlerartList zList=new ZaehlerartList(s);
		
		if (zList.size()==0) {
			zList.add(new Zaehlerart("Gas", 100000));
			zList.add(new Zaehlerart("Strom", 200000));
			zList.add(new Zaehlerart("Wasser", 300000));
			zList.add(new Zaehlerart("Heizung", 400000));
			
		}
		
		System.out.println("-Zählerarten befüllt");
		AbleseList aList=new AbleseList(s,zList);
		
		Kunde[] kArray=kList.getArray();
		Zaehlerart[] zArray=zList.getArray();
		for (int i=0;i<100;i++) {
			int kNum=(int) (Math.random()*kArray.length);
			int zNum=(int) (Math.random()*zArray.length);
			int yNum=(int) (Math.random()*22+2000);
			int dNum=(int) (Math.random()*365+1);
			int sNum=(int) (Math.random()*100000);
			
			AbleseEntry e=new AbleseEntry(null, kArray[kNum].getId(), zArray[zNum].getId(), i+"",LocalDate.ofYearDay(yNum,dNum), false, sNum,"AUTO2");
			aList.add(e);
		}
		System.out.println("-Ablesungen befüllt");
	}
}
