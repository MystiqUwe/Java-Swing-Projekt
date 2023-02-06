package server.resource;

import java.util.ArrayList;
import java.util.Map;
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
import server.Kunde;
import server.Server;

@Path("hausverwaltung/kunden")
public class KundenRessource {

	/**
	 * @param kunde
	 * @return Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addKunde(Kunde kunde) {
		if (!(kunde instanceof Kunde)) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Kunde konnte nicht angelegt werden!")
					.type(MediaType.TEXT_PLAIN).build();
		}

		kunde.setId(UUID.randomUUID());
		Server.getServerData().addKunde(kunde);
		return Response.status(Response.Status.CREATED).entity(kunde).build();
	}

	/**
	 * @param kunde
	 * @return Response
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response editKunde(Kunde kunde) {
		if (!(kunde instanceof Kunde)) { // Falls kunde keine Instanz von Kunde ist
			return Response.status(Response.Status.BAD_REQUEST).entity("Kunde konnte nicht bearbeitet werden!")
					.type(MediaType.TEXT_PLAIN).build();
		}
		Kunde k = Server.getServerData().updateKunde(kunde);
		if (k == null) { // Falls Kunde null
			return Response.status(Response.Status.NOT_FOUND).entity("Kunde konnte nicht gefunden werden!")
					.type(MediaType.TEXT_PLAIN).build();
		}
		return Response.status(Response.Status.OK).entity("Kunde erfolgreich bearbeitet!").build();

	}

	/**
	 * @param id
	 * @return Response
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response deleteKunde(@PathParam("id") String param) {
		UUID id;
		try {
			id = UUID.fromString(param);
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Keine gültige UUID").type(MediaType.TEXT_PLAIN)
					.build();

		}
		Kunde k = Server.getServerData().getKunde(id);
		if (k == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Kunde konnte nicht gelöscht werden!")
					.type(MediaType.TEXT_PLAIN).build();
		}
		Map<Kunde, ArrayList<Ablesung>> map = Server.getServerData().removeKunde(id);
		return Response.status(Response.Status.OK).entity(map).build();

	}

	/**
	 * @return Response
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllKunden() {
		return Response.status(Response.Status.OK).entity(Server.getServerData().getKundenListe()).build();
	}

	/**
	 * @return Response
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response getKunde(@PathParam("id") String param) {
		UUID id;
		try {
			id = UUID.fromString(param);
		} catch (IllegalArgumentException e) {
			return Response.status(Response.Status.NOT_FOUND).entity("Keine gültige UUID").type(MediaType.TEXT_PLAIN)
					.build();
		}
		Kunde k = Server.getServerData().getKunde(id);
		if (k == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Kunde nicht gefunden").type(MediaType.TEXT_PLAIN)
					.build();
		}
		return Response.status(Response.Status.OK).entity(k).build();
	}

}