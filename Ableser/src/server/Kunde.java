package server;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@JsonTypeName(value = "kunde")
public class Kunde {

	private UUID id;
	@JsonProperty
	private String name;
	@JsonProperty
	private String vorname;
	
	public Kunde() {
		id=UUID.randomUUID();
	}
	
	public Kunde(String name, String vorname) {
		this();
		this.name = name;
		this.vorname = vorname;
	}

	public Kunde(String jsonString) {
		String[] splitString=jsonString.split("=|:");
		this.id=UUID.fromString(splitString[1]);
		this.name=splitString[3];
		this.vorname=splitString[5];
	}
	
	@Override
	public String toString() {
		return "Kunde [id=" + id + ": name=" + name + ": vorname=" + vorname + ":]";
	}
	

}
