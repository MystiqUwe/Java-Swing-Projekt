package server;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpServer;

import lombok.Getter;

public class Server {

	private static ObjectMapper obMap = new ObjectMapper();
	private static final String SERVERFILE = "target/ServerSave.json";
	private static HttpServer server = null;

	private static AbstractDatabase serverData;
	@Getter
	private static boolean serverReady = false;

	public static void startServer(String url, boolean loadFromFile) {
		startServer(url,loadFromFile,true);
	}
	
	public static void startServer(String url, boolean loadFromFile, boolean useSQLDatabase) {
		if (server != null) {
			System.out.println("Server läuft bereits unter: " + server.getAddress());
			return;
		}

		final String pack = "server.resource";

		System.out.println("Server starten unter: " + url);

		if (useSQLDatabase) {
			if (!loadFromFile) {
				SQLDatabase.wipeDatabase();
			}
			serverData=SQLDatabase.startDatabase();
			if (serverData==null) {
				return; //Verbindungsfehler
			}
		} else {
			if (loadFromFile) {
				serverData = loadJSON(SERVERFILE);
			} else {
				serverData = new JsonDatabase();
			}
			((JsonDatabase)serverData).init();
			serverReady = true;
		}
		serverReady = true;

		final ResourceConfig rc = new ResourceConfig().packages(pack);
		server = JdkHttpServerFactory.createHttpServer(URI.create(url), rc);
		System.out.println("Bereit für Anfragen....");
	}

	public static void stopServer(boolean saveToFile) {
		if (server == null) {
			System.out.println("Server läuft nicht");
			return;
		}

		serverReady = false;
		server.stop(0);
		server = null;

		if (saveToFile) {
			SQLDatabase.wipeDatabase();
			saveJSON(SERVERFILE);
		}
		System.out.println("Server angehalten");
	}

	public static AbstractDatabase getServerData() {
		return serverData;
	}

	private static JsonDatabase loadJSON(String file) {
		final File f = new File(file);
		if (f.exists()) {
			try {
				obMap.registerModule(new JavaTimeModule());
				JsonDatabase db = obMap.readValue(f, JsonDatabase.class);

				System.out.format("Datei %s gelesen\n", file);

				return db;
			} catch (final Exception e) {
				e.printStackTrace();
				// ignore
			}
		}
		return new JsonDatabase();

	}

	private static void saveJSON(String file) {
		try {

			// DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			// obMap.setDateFormat(df);
			obMap.registerModule(new JavaTimeModule());
			obMap.writerWithDefaultPrettyPrinter().writeValue(new File(file), serverData);

			System.out.format("Datei %s erzeugt\n", file);
		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	public static void main(String[] args) {
		//startServer("http://localhost:8081/rest", true);
		startServer("http://localhost:8081/rest", true,true);
		/*
		 * Kunde k1=new Kunde("Heinz", "Müller"); Kunde k2=new Kunde("Max","Meier");
		 * serverData.addKunde(k1); serverData.addKunde(k2); serverData.addAblesung(new
		 * Ablesung("a", LocalDate.parse("2000-10-05"), k2, "", false, 123));
		 * serverData.addAblesung(new Ablesung("b", LocalDate.parse("2020-01-05"), k1,
		 * "", false, 1234)); serverData.addAblesung(new Ablesung("c",
		 * LocalDate.parse("2020-10-05"), k2, "", false, 1235));
		 * serverData.addAblesung(new Ablesung("d", LocalDate.parse("2000-12-05"), k1,
		 * "", false, 1236)); serverData.addAblesung(new Ablesung("e",
		 * LocalDate.parse("2020-10-05"), k2, "", false, 1237));/
		 **/
		// stopServer(true);
	}

}
