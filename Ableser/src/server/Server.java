package server;

import java.net.URI;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

import lombok.Getter;

public class Server {

	private static final String SERVERFILE = "target/ServerSave.json";
	private static HttpServer server = null;

	private static AbstractDatabase serverData;
	@Getter
	private static boolean serverReady = false;

	public static void startServer(String url, boolean loadFromFile) {
		startServer(url,loadFromFile,true,true);
	}

	/*
	 * Normales startServer, mit bevorzugten Parametern
	 */
	public static void startServer(String url) {
		startServer(url,false,true,false);

	}

	/**
	 * @param url URL auf dem der server gestartet werden soll
	 * @param loadFromFile soll die Serverdatei eingelesen werden
	 * @param useSQLDatabase SQL Datenbank, oder nur persistierung in eine Datei
	 * @param wipeDatabase **nur für SQL** soll die Datenbank vor dem Start geleert werden
	 */
	public static void startServer(String url, boolean loadFromFile, boolean useSQLDatabase, boolean wipeDatabase) {
		if (server != null) {
			System.out.println("Server läuft bereits unter: " + server.getAddress());
			return;
		}

		final String pack = "server.resource";

		System.out.println("Server starten unter: " + url);

		if (useSQLDatabase) {
			if (wipeDatabase) {
				SQLDatabase.wipeDatabase();
			}
			if (loadFromFile) {
				serverData=SQLDatabase.loadJSON(SERVERFILE);
			} else  {
				serverData=SQLDatabase.startDatabase();
			}
			if (serverData==null) {
				return; //Verbindungsfehler
			}
		} else {
			if (loadFromFile) {
				serverData = AbstractDatabase.loadJSON(SERVERFILE);
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
			serverData.saveJSON(SERVERFILE);
		}
		System.out.println("Server angehalten");
	}

	public static AbstractDatabase getServerData() {
		return serverData;
	}


	public static void main(String[] args) {
		//startServer("http://localhost:8081/rest", true);
		startServer("http://localhost:8081/rest");
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
