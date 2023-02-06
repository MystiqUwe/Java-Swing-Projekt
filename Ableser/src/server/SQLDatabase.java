package server;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;


public class SQLDatabase extends AbstractDatabase{
//create or replace user 'restServer'@'localhost' identified by 'restServer';
	private Connection con;
	
	public SQLDatabase() {
		try {
			final Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/ablesung", "restServer","restServer");
			 System.out.println("... connected");
			
			 final DatabaseMetaData meta = con.getMetaData();
			 System.out.format(
					 "Driver : %s %s.%s\n", meta.getDriverName(), meta.getDriverMajorVersion(),meta.getDriverMinorVersion());
			 System.out.format("DB : %s %s.%s (%s)\n", meta.getDatabaseProductName(),
					 meta.getDatabaseMajorVersion(), meta.getDatabaseMinorVersion(),meta.getDatabaseProductVersion());
			 } catch (final SQLException e) {
				  System.out.println("Fehler: " + e.getMessage());
			 }
		
	}
	
	@Override
	public ArrayList<Kunde> getKundenListe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Ablesung> getAblesungListe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addKunde(Kunde k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Kunde getKunde(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Kunde updateKunde(Kunde kunde) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Kunde, ArrayList<Ablesung>> removeKunde(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAblesung(Ablesung a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getAblesungIndex(UUID id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Ablesung getAblesung(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateAblesung(Ablesung abNeu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Ablesung deleteAblesung(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Ablesung> getAblesungList(UUID kundenId, LocalDate sDate, LocalDate eDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
