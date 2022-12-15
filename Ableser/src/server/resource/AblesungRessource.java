package server.resource;

import java.util.UUID;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import server.*;

@Path("ablesungen")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AblesungRessource {

	
	@POST
	public Response createAblesung(Ablesung abl) {
		Server.getServerData().addAblesung(abl);
		if(abl instanceof Ablesung){
			if (abl.getKunde()==null) {
				return Response.status(Response.Status.NOT_FOUND).entity("Kunde nicht gefunden").build();
			}
			Server.getServerData().addAblesung(abl);
			return Response.status(Response.Status.CREATED).entity(abl).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).entity("Ungültige Ablesung").type(MediaType.TEXT_PLAIN)
				.build();
	}
	
	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateAblesung(Ablesung abl) {
		System.out.println(abl);
		Server.getServerData().addAblesung(abl);
		if(abl instanceof Ablesung){
			if (abl.getKunde()==null) {
				return Response.status(Response.Status.NOT_FOUND).entity("Kunde nicht gefunden").build();
			}
			if (Server.getServerData().updateAblesung(abl)) {
				return Response.status(Response.Status.OK).entity(abl).build();
				
			}
			return Response.status(Response.Status.NOT_FOUND).entity("Ablesung nicht gefunden").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).entity("Ungültige Ablesung").type(MediaType.TEXT_PLAIN)
				.build();
	}
	
	
	@GET
	public Response getFiltered(@QueryParam("kunde") UUID kundenId, @QueryParam("beginn") String sDate,  @QueryParam("ende") String eDate) {
		//TODO
		return Response.status(Response.Status.OK).entity("NYI").build();
	}
	
	
}
