package server.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import server.Ablesung;
import server.Database;
import server.Kunde;
import server.Server;

@Path("kunden")
public class KundenRessource {

	final Database database = Server.getServerData();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addKunde(Kunde kunde) {
		if (!(kunde instanceof Kunde)) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Kunde konnte nicht angelegt werden!")
					.type(MediaType.TEXT_PLAIN).build();
		}
		
		database.addKunde(kunde);
		return Response.status(Response.Status.CREATED).entity(kunde).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response editKunde(Kunde kunde) {
		if (!(kunde instanceof Kunde)) { // Falls kunde keine Instanz von Kunde ist
			return Response.status(Response.Status.BAD_REQUEST).entity("Kunde konnte nicht bearbeitet werden!")
					.type(MediaType.TEXT_PLAIN).build();
		}
		Kunde k = database.updateKunde(kunde);
		if (k == null) { // Falls Kunde null
			return Response.status(Response.Status.NOT_FOUND).entity("Kunde konnte nicht gefunden werden!")
					.type(MediaType.TEXT_PLAIN).build();
		}
		return Response.status(Response.Status.OK).entity("Kunde erfolgreich bearbeitet!").build();

	}


	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response deleteKunde(@PathParam("id") UUID id) {
		ArrayList<Ablesung> ausgabe = database.getAblesungList(id);
		Kunde k = database.removeKunde(id);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("kunde", k);
		map.put("ablesungen", ausgabe);
		if (k == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Kunde konnte nicht gelöscht werden!")
					.type(MediaType.TEXT_PLAIN).build();
		}
		return Response.status(Response.Status.OK).entity(map).build();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllKunden() {
		return Response.status(Response.Status.OK).entity(database.getAllKunden()).build();
	}

}