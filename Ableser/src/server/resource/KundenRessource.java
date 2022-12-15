package server.resource;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import server.Database;
import server.Kunde;

@Path("kunden")
public class KundenRessource {
	
	final Database database = new Database();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addKunde(Kunde kunde) {
		if(kunde instanceof Kunde){
			database.addKunde(kunde);
			return Response.status(Response.Status.CREATED).entity(kunde).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).entity("Kunde konnte nicht angelegt werden!").type(MediaType.TEXT_PLAIN)
				.build();
	}
}
//        "id": "0168a197-fe06-4383-b3ef-807e94c9c8ae",