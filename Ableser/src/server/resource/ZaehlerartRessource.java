package server.resource;

import dataEntities.Zaehlerart;
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
import server.Server;

@Path("hausverwaltung/zaehlerarten")
public class ZaehlerartRessource {

	/**
	 * @param kunde
	 * @return Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addZaehlerart(Zaehlerart za) {
		if (!(za instanceof Zaehlerart)) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Zaehlerart konnte nicht angelegt werden!")
					.type(MediaType.TEXT_PLAIN).build();
		}
		Zaehlerart newZa=Server.getServerData().addZaehlerart(za);
		if (newZa!=null) {
			return Response.status(Response.Status.CREATED).entity(newZa).build();
		} else {
			System.err.println("Interner Fehler beim Erstellen von "+za);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Interner Serverfehler!")
					.type(MediaType.TEXT_PLAIN).build();
		}
	}

	/**
	 * @param kunde
	 * @return Response
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response editZaehlerart(Zaehlerart za) {
		if (!(za instanceof Zaehlerart)) { // Falls za keine Instanz von Zaehlerart ist
			return Response.status(Response.Status.BAD_REQUEST).entity("Zaehlerart konnte nicht bearbeitet werden!")
					.type(MediaType.TEXT_PLAIN).build();
		}
		switch(Server.getServerData().updateZaehlerart(za)) {
		case SUCCESS:
			return Response.status(Response.Status.OK).entity("Zaehlerart erfolgreich bearbeitet!").build();
		case Zaehlerart_NOT_FOUND:
			return Response.status(Response.Status.NOT_FOUND).entity("Zaehlerart konnte nicht gefunden werden!")
					.type(MediaType.TEXT_PLAIN).build();
		default:
			System.err.println("Interner Fehler beim Update von "+za);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Interner Serverfehler!")
					.type(MediaType.TEXT_PLAIN).build();
		}

	}

	/**
	 * @param id
	 * @return Response
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response deleteZaehlerart(@PathParam("id") Integer param) {

		Zaehlerart za = Server.getServerData().deleteZaehlerart(param);
		if (za == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Zaehlerart konnte nicht gelöscht werden!")
					.type(MediaType.TEXT_PLAIN).build();
		}

		return Response.status(Response.Status.OK).entity(za).build();

	}

	/**
	 * @return Response
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllZaehlerart() {
		return Response.status(Response.Status.OK).entity(Server.getServerData().getZaehlerartListe()).build();
	}

	/**
	 * @return Response
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response getZaehlerart(@PathParam("id") Integer param) {
		Zaehlerart za = Server.getServerData().getZaehlerart(param);
		if (za == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Zaehlerart nicht gefunden").type(MediaType.TEXT_PLAIN)
					.build();
		}
		return Response.status(Response.Status.OK).entity(za).build();
	}

}