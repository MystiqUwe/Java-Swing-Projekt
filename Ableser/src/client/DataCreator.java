package client;

import java.time.LocalDate;

import ablesebogen.AbleseEntry;
import ablesebogen.AbleseList;
import ablesebogen.Ablesebogen;
import ablesebogen.KundeList;
import server.Kunde;

public class DataCreator {

	static String[] kVornamen= {"Mia","Noah","Lara","Matteo","Lena","Theo","Moritz","Sophia","Emil","Leni","Anna","Oskar","Julian","Leonie","Anton","Nora","Hannah","Max","Luisa","Paul","Emily","Liam","Emma","Lukas","Julia","Samuel","felix","Mila","Emilia","Leon","Luca","Marie","Jonas","Lina","Ella","Leo","Maximilian","Sophie","Amelie","Milan"};
	
	public static void main(String[] args) {
		String url = "http://localhost:8081/rest";
		Service s=new Service(url);
		
		KundeList kList=new KundeList(s);
		
		for (int i=0;i<20;i++) {
			int num=(int) (Math.random()*kVornamen.length);
			System.out.println();
			Kunde k=new Kunde("Kunde "+i,kVornamen[num] );
			kList.add(k);
		}
		
		AbleseList aList=new AbleseList(s);
		
		Kunde[] kArray=kList.getArray();
		for (int i=0;i<100;i++) {
			int kNum=(int) (Math.random()*kArray.length);
			int yNum=(int) (Math.random()*22+2000);
			int dNum=(int) (Math.random()*365+1);
			int sNum=(int) (Math.random()*100000);
			
			AbleseEntry e=new AbleseEntry(null, kArray[kNum].getId(), "XXX", i+"",LocalDate.ofYearDay(yNum,dNum), false, sNum,"AUTO2");
			aList.add(e);
		}
	}
}
