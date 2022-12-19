package client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import server.Server;

/**
 * Client 
 */
public class Service {


	private final String baseUrl;


	private final Client client;

	/**
	 *
	 * @param baseUrl the base URL for the client
	 */
	public Service(String baseUrl) {
		this.baseUrl = baseUrl;
		this.client = ClientBuilder.newClient();
	}

	/**
	 * POST request
	 *
	 * @param the path for the request
	 * @param requestBody as Object
	 * @return a Response object
	 */
	public Response post(String path, Object requestBody) { //Entity<?> entity
		// Create a WebTarget for the request
		WebTarget target = client.target(baseUrl).path(path);

		// Send the POST request and return the response
		return target.request(MediaType.APPLICATION_JSON).post(Entity.entity(requestBody, MediaType.APPLICATION_JSON)); //post(Entity.entity(requestBody, MediaType.APPLICATION_JSON));
	}
	
	public Response handlePutRequest(String path, Object requestBody) {
		// Send the PUT request with the object as the request body
		WebTarget target = client.target(baseUrl).path(path);
		Response response = target.request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(requestBody, MediaType.APPLICATION_JSON));

		return response;
	}

	/**
	 * GET request
	 *
	 * @param path for the request
	 * @return a Response object
	 */
	public Response get(String path) { //MediaType mediaType
		// Create a WebTarget for the request
		WebTarget target = client.target(baseUrl).path(path);

		// Send the GET request and return the response
		return target.request(MediaType.APPLICATION_JSON).get();
	}
	

	/**
     * DELETE request
     *
     * @param path for the request
     * @return a Response object
     */
    public Response delete(String path) {
        // Create a WebTarget for the request
        WebTarget target = client.target(baseUrl).path(path);

        // Send the DELETE request and return the response
        return target.request().delete();
    }
}