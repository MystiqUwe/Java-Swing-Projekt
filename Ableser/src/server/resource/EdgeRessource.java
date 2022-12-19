package server.resource;

import java.time.LocalDate;
import java.util.ArrayList;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import server.Ablesung;
import server.Server;

public class EdgeRessource {

	@Path("hausverwaltung/ablesungenVorZweiJahrenHeute")
	public Response getAblesungenVorZweiJahrenHeute() {
		ArrayList<Ablesung> list=Server.getServerData().getAblesungList(null, LocalDate.of( LocalDate.now().getYear()-2, 1, 1), LocalDate.now());
		return Response.status(Response.Status.OK).entity(list).build();		
		
	}
}
