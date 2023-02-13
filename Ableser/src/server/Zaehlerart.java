package server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Zaehlerart {

	private int id;
	private String name;
	private int warnValue;
	
	public Zaehlerart() {
		this("",-1);
	}
	
	public Zaehlerart(String name, int warnV) {
		id=(int)(Math.random()*Integer.MAX_VALUE);
		this.name=name;
		this.warnValue=warnV;
		
	}
		
}
