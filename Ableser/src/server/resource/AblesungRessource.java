package server.resource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.UUID;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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

@Path("hausverwaltung/ablesungen")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AblesungRessource {

	
	@POST
	public Response createAblesung(Ablesung abl) {
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
		if(abl instanceof Ablesung){
			if (abl.getKunde()==null) {
				return Response.status(Response.Status.NOT_FOUND).entity("Kunde nicht gefunden").build();
			}
			if (Server.getServerData().updateAblesung(abl)) {
				return Response.status(Response.Status.OK).entity("Erfolg").build();
				
			}
			return Response.status(Response.Status.NOT_FOUND).entity("Ablesung nicht gefunden").build();
		}
		return Response.status(Response.Status.BAD_REQUEST).entity("Ungültige Ablesung").type(MediaType.TEXT_PLAIN).build();
	}
	

	@DELETE
	@Path("{id}")
	public Response deleteAblesungById(@PathParam("id") UUID id) {
		Ablesung abl=Server.getServerData().deleteAblesung(id);
		if (abl==null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Ablesung nicht gefunden").type(MediaType.TEXT_PLAIN).build();
		}
		return Response.status(Response.Status.OK).entity(abl).build();		
	}
		
	@GET
	@Path("{id}")
	public Response getAblesungById(@PathParam("id") UUID id) {
		Ablesung abl=Server.getServerData().getAblesung(id);
		if (abl==null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Ablesung nicht gefunden").type(MediaType.TEXT_PLAIN).build();
		}
		return Response.status(Response.Status.OK).entity(abl).build();		
	}

	@GET
	public Response getFiltered(@QueryParam("kunde") UUID kundenId, @QueryParam("beginn") String sDateString,  @QueryParam("ende") String eDateString) {
		System.out.println(kundenId+ " von "+sDateString +" bis " + eDateString);
		LocalDate sDate=null;
		LocalDate eDate=null;
		try {
			if (sDateString!=null)  {
				sDate= LocalDate.parse(sDateString);//,DateTimeFormatter.ISO_LOCAL_DATE);
			} 
			if (eDateString!=null) {
				eDate= LocalDate.parse(eDateString);//,DateTimeFormatter.ISO_LOCAL_DATE);
			}
		} catch (DateTimeParseException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Ungültiges Datum").type(MediaType.TEXT_PLAIN).build();
			
		}
		
		ArrayList<Ablesung> list=Server.getServerData().getAblesungList(kundenId,sDate,eDate);
		if (list.size()>0) {
			return Response.status(Response.Status.OK).entity(list).build();		
		}
		return Response.status(Response.Status.NOT_FOUND).entity("keine Ablesung gefunden").type(MediaType.TEXT_PLAIN).build();

	}
	
	
}
