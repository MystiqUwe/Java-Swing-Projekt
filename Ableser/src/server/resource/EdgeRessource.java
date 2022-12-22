package server.resource;

import java.time.LocalDate;
import java.util.ArrayList;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import server.Ablesung;
import server.Server;

@Path("hausverwaltung")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EdgeRessource {

	@GET
	@Path("ablesungenVorZweiJahrenHeute")
	public Response getAblesungenVorZweiJahrenHeute() {
		ArrayList<Ablesung> list = Server.getServerData().getAblesungList(null,
				LocalDate.of(LocalDate.now().getYear() - 2, 1, 1), LocalDate.now());
		return Response.status(Response.Status.OK).entity(list).build();

	}
}
