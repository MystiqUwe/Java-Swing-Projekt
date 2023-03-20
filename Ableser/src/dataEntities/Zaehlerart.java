package dataEntities;

import java.util.Objects;

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

	@Override
	public String toString() {
		return "Zaehlerart [id=" + id + ", name=" + name + ", warnValue=" + warnValue + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, warnValue);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Zaehlerart other = (Zaehlerart) obj;
		return id == other.id && Objects.equals(name, other.name) && warnValue == other.warnValue;
	}


}
