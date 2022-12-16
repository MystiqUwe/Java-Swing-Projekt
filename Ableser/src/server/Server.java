package server;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpServer;

import lombok.Getter;

public class Server {

	private static ObjectMapper obMap=new ObjectMapper();
	private static final String SERVERFILE = "target/ServerSave.json";
	private static HttpServer server=null;
	
	private static boolean isTest=true; //********Workaround für die Tests*********//
	
	private static Database serverData;
	@Getter
	private static boolean serverReady=false;
	
	public static void startServer(String url, boolean loadFromFile) {
		if (server!=null) {
			System.out.println("Server läuft bereits unter: "+server.getAddress());
			return;
		}
		
		if (isTest) url+="/hausverwaltung"; //********Workaround für die Tests*********//
		final String pack = "server.resource"; 
		
		System.out.println("Server starten unter: "+url); 
		
		if (loadFromFile) {
			serverData=loadJSON(SERVERFILE);
		} else {
			serverData=new Database();
		}
		serverReady=true;
		serverData.init();
		
		final ResourceConfig rc = new ResourceConfig().packages(pack);
		server = JdkHttpServerFactory.createHttpServer( URI.create(url), rc);
		System.out.println("Bereit für Anfragen....");
	}
	
	public static void stopServer(boolean saveToFile) {
		if (server==null) {
			System.out.println("Server läuft nicht");
			return;
		}
		
		serverReady=false;
		server.stop(0);
		server=null;
		
		if (saveToFile) {
			saveJSON(SERVERFILE);
		}
		System.out.println("Server angehalten");
	}

	public static Database getServerData() {
		return serverData;
	}
	private static Database loadJSON(String file) {
		final File f = new File(file);
		if (f.exists()) {
			try {
				obMap.registerModule(new JavaTimeModule());				
				Database db=obMap.readValue(f, Database.class);
				
				System.out.format("Datei %s gelesen\n", file);
				
				return db;
			} catch (final Exception e) {
				e.printStackTrace();
				// ignore
			}
		}
		return new Database();

	}
	
	private static void saveJSON(String file) {
		try {

			//DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			//obMap.setDateFormat(df);
			obMap.registerModule(new JavaTimeModule());
			obMap.writerWithDefaultPrettyPrinter().writeValue(new File(file), serverData);			
			
			System.out.format("Datei %s erzeugt\n", file);
		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
	
	public static void main(String[] args) {
		isTest=false; //********Workaround für die Tests*********//
		startServer("http://localhost:8081/rest",true);
		
		/*Kunde k1=new Kunde("Heinz", "Müller");
		Kunde k2=new Kunde("Max","Meier");
		serverData.addKunde(k1);
		serverData.addKunde(k2);
		serverData.addAblesung(new Ablesung("a", LocalDate.parse("2000-10-05"), k2, "", false, 123));
		serverData.addAblesung(new Ablesung("b", LocalDate.parse("2020-01-05"), k1, "", false, 1234));
		serverData.addAblesung(new Ablesung("c", LocalDate.parse("2020-10-05"), k2, "", false, 1235));
		serverData.addAblesung(new Ablesung("d", LocalDate.parse("2000-12-05"), k1, "", false, 1236));
		serverData.addAblesung(new Ablesung("e", LocalDate.parse("2020-10-05"), k2, "", false, 1237));/**/
		//stopServer(true);
	}
	
	
	
}
