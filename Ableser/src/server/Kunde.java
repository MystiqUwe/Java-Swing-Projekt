package server;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
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

	@Override
	public String toString() {
		return "Kunde [id=" + id + ", name=" + name + ", vorname=" + vorname + "]";
	}
	
	
}
