package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import client.DataCreator;


public class SQLDatabase extends AbstractDatabase{
//create or replace user 'restServer'@'localhost' identified by 'restServer';
	private Connection con;
	
	public SQLDatabase() {
		
		
	}
	
	public static SQLDatabase startDatabase() {
		SQLDatabase db=new SQLDatabase();
		try {
			db.con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/ablesung", "restServer","restServer");
			 System.out.println("connected to the Database");

		} catch (final SQLException e) {
				  System.out.println("Fehler: " + e.getMessage());
				  return null;
		}
		return db;
	}
	
	public void shutdownDatabase() {
		try {
			con.close();
			con=null;
		} catch (SQLException e) {

		}
	}
	private void createDatabase() {
		if (con==null) return;
		try {
			Statement st=con.createStatement();
			st.executeUpdate("DROP TABLE IF EXISTS ablesungen;");
			st.executeUpdate("DROP TABLE IF EXISTS kunden;");
			
			st.executeUpdate("CREATE TABLE kunden"
					+ "(id UUID PRIMARY KEY,"
					+ "name VARCHAR(50),"
					+ "vorname VARCHAR(50))"
					+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;");
			
			System.out.println("Created Kunden");
			st.executeUpdate("CREATE TABLE ablesungen"
					+ "(id UUID PRIMARY KEY,"
					+ "zaehlernummer VARCHAR(50),"
					+ "datum DATE,"
					+ "kID UUID,"
					+ "kommentar VARCHAR(255),"
					+ "neuEingebaut BOOLEAN,"
					+ "zaehlerstand INTEGER,"
					+ "CONSTRAINT kunden_fk FOREIGN KEY (kID) REFERENCES kunden(id) ON DELETE SET NULL ON UPDATE CASCADE)"
					+ "ENGINE=InnoDB DEFAULT CHARSET=utf8;");
			System.out.println("Created Ablesungen");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	protected static void wipeDatabase() {
		System.out.println("Wiping the database...");
		startDatabase().createDatabase();
		
		
	}
	private void prepareKunde(PreparedStatement st, Kunde k) throws SQLException {
		prepareKunde(st, k, 0);
	}

	private void prepareKunde(PreparedStatement st, Kunde k, int off) throws SQLException {
		if (off>=0) st.setString(1+off, k.getId().toString());
		st.setString(2+off, k.getName());
		st.setString(3+off, k.getVorname());
	}

	private Kunde kundeFromResult(ResultSet rs) throws SQLException {
		return kundeFromResult(rs,0);
	}

	private Kunde kundeFromResult(ResultSet rs, int off) throws SQLException {
    	return new Kunde(
    			UUID.fromString(rs.getString(1+off)),
    			rs.getString(2+off),
    			rs.getString(3+off)
    	);		    	
	}
	
	private void prepareAblesung(PreparedStatement st, Ablesung a) throws SQLException {
		prepareAblesung(st, a,0);
	}
		
	private void prepareAblesung(PreparedStatement st, Ablesung a,int off) throws SQLException {
//	"insert into ablesungen(id,zaehlernummer,datum,kID,kommentar,neuEingebaut,zaehlerstand) values (?,?,?,?,?,?,?);");
		if (off>=0) st.setString(1+off,a.getId().toString());
		st.setString(2+off,a.getZaehlernummer());
		if (a.getDatum()==null) {
			st.setString(3+off, null);
		} else {
			st.setString(3+off,a.getDatum().toString());
		}
		if (a.getKundenId()==null) {
			st.setString(4+off,null);			
		} else {
			st.setString(4+off,a.getKundenId().toString());
		}
		st.setString(5+off,a.getKommentar());
		st.setBoolean(6+off,a.isNeuEingebaut());
		st.setInt(7+off,a.getZaehlerstand().intValue());
	}

	private Ablesung ablesungFromResult(ResultSet rs) throws SQLException {
		return ablesungFromResult(rs, 0);
	}

	private Ablesung ablesungFromResult(ResultSet rs, int off) throws SQLException {
		UUID kID=null;
		if (rs.getString(7+off)!=null) {
			kID=UUID.fromString(rs.getString(7+off));
		} 
		
		Ablesung abl=new Ablesung(
    			UUID.fromString(rs.getString(1+off)),
    			rs.getString(2+off),
    			rs.getDate(3+off).toLocalDate(),
    			null, //Kunde
    			rs.getString(4+off),
    			rs.getBoolean(5+off),
    			rs.getInt(6+off),
    			kID
    	);
    	//abl.updateKunde();
    	return abl;
	}

	
	@Override
	public ArrayList<Kunde> getKundenListe() {
		ArrayList<Kunde> result=new ArrayList<Kunde>();
		try {
			final Statement st=con.createStatement();
			final ResultSet rs=st.executeQuery("Select id,name,vorname from kunden");
		    while(rs.next()) {
		    	result.add(kundeFromResult(rs));
		    }
		} catch (SQLException e) {
			System.out.println("Datenbankfehler bei getKundenListe - "+e.getMessage());
			return result;
		}
		return result;
	}

	//String zaehlernummer, LocalDate datum, Kunde kunde, String kommentar, boolean neuEingebaut, Integer zaehlerstand
	@Override
	public ArrayList<Ablesung> getAblesungListe() {
		ArrayList<Ablesung> result=new ArrayList<Ablesung>();
		try {
			final Statement st=con.createStatement();
			final ResultSet rs=st.executeQuery("Select id,zaehlernummer,datum,kommentar,neuEingebaut,zaehlerstand,kid from ablesungen;");
		    while(rs.next()) {
		    	result.add(ablesungFromResult(rs));
		    }
		} catch (SQLException e) {
			System.out.println("Datenbankfehler bei getAblesungListe - "+e.getMessage());
			return result;
		}
		return result;
	}

	@Override
	public OPERATION_RESULT addKunde(Kunde k) {
		try {
			final PreparedStatement st=con.prepareStatement("INSERT INTO kunden values (?,?,?);");
			st.setString(1, k.getId().toString());
			st.setString(2, k.getName());
			st.setString(3, k.getVorname());
			st.execute();
		} catch (SQLException e) {
			System.out.println("Datenbankfehler bei addKunde - "+e.getMessage());
			return OPERATION_RESULT.INTERNAL_ERROR;
		}
		return OPERATION_RESULT.SUCCESS;
		
	}

	@Override
	public Kunde getKunde(UUID id) {
		if (id==null) {
			return null;
		}
		try {
			final PreparedStatement st=con.prepareStatement("Select * from kunden where id=?;");
			st.setString(1, id.toString());
			final ResultSet rs=st.executeQuery();
		    if (rs.next()) { //Wurde ein Datensatz gefunden?
		    	return kundeFromResult(rs);    	
		    } else {
		    	return null;
		    }
		} catch (SQLException e) {
			System.out.println("Datenbankfehler bei getKunde - "+e.getMessage());
			return null;
		}
	}

	@Override
	public OPERATION_RESULT updateKunde(Kunde kunde) {
		try {
			final PreparedStatement st=con.prepareStatement("UPDATE kunden SET name=?, vorname=? where id=?;");
			prepareKunde(st, kunde,-1);
			st.setString(3, kunde.getId().toString());
			final int rowCount=st.executeUpdate();
		    if (rowCount>0) { //Wurde ein Datensatz gefunden?
		    	return OPERATION_RESULT.SUCCESS; 	
		    } else {
		    	return OPERATION_RESULT.KUNDE_NOT_FOUND;
		    }
		} catch (SQLException e) {
			System.out.println("Datenbankfehler bei updateKunde - "+e.getMessage());
			return OPERATION_RESULT.INTERNAL_ERROR;
		}
	}

	@Override
	public Map<Kunde, ArrayList<Ablesung>> removeKunde(UUID id) {
		ArrayList<Ablesung> aList=getAblesungList(id);
		Kunde k=getKunde(id);
		if (k==null) {
			return null;
		}
		try {
			final PreparedStatement st=con.prepareStatement("DELETE FROM kunden where id=?;");
			st.setString(1, id.toString());
			final int rowCount=st.executeUpdate();
		    if (rowCount>0) { //Wurde ein Datensatz gefunden?
		    	for (Ablesung a : aList) {
					a.removeKunde();
				}
				Map<Kunde, ArrayList<Ablesung>> map = new HashMap<Kunde, ArrayList<Ablesung>>();
				map.put(k, aList);
				return map;
		    } else {
		    	return null;
		    }
		} catch (SQLException e) {			
			System.out.println("Datenbankfehler bei deleteKunde - "+e.getMessage());
			return null;
		}
	}


	@Override
	public OPERATION_RESULT addAblesung(Ablesung a) {
		if (a==null) {
			return OPERATION_RESULT.ABLESUNG_NOT_FOUND;
		}		
		if (a.getKundenId()==null) {
			return OPERATION_RESULT.KUNDE_NOT_FOUND;
		}
		try { 
			final PreparedStatement st=con.prepareStatement(
					"insert into ablesungen(id,zaehlernummer,datum,kID,kommentar,neuEingebaut,zaehlerstand) values (?,?,?,?,?,?,?);");
			prepareAblesung(st, a);
			st.execute();
			return OPERATION_RESULT.SUCCESS;
		} catch (SQLException e) {
			if (e.getErrorCode()==1452) {
				return OPERATION_RESULT.KUNDE_NOT_FOUND;
			}
			
			System.out.println("Datenbankfehler bei addAblesung - "+e.getMessage());
			return OPERATION_RESULT.INTERNAL_ERROR;
		}
		
		
	}

	@Override
	public Ablesung getAblesung(UUID id) {
		try {
			final PreparedStatement st=con.prepareStatement("Select id,zaehlernummer,datum,kommentar,neuEingebaut,zaehlerstand,kid from ablesungen where id=?");
			st.setString(1, id.toString());
			final ResultSet rs=st.executeQuery();
		    if(rs.next()) {
		    	return ablesungFromResult(rs);
		    } else {
		    	return null;
		    }
		} catch (SQLException e) {
			System.out.println("Datenbankfehler bei getAblesung - "+e.getMessage());
			return null;
		}
	}

	@Override
	public OPERATION_RESULT updateAblesung(Ablesung abNeu) {
		if (abNeu.getId()==null) {
			return OPERATION_RESULT.ABLESUNG_NOT_FOUND;
		}
		try {
			final PreparedStatement st=con.prepareStatement("UPDATE ablesungen SET zaehlernummer=? ,datum=? ,kID=? ,kommentar=? ,neuEingebaut=? ,zaehlerstand=?  where id=?;");
			prepareAblesung(st, abNeu,-1);
			st.setString(7, abNeu.getId().toString());
			final int rowCount=st.executeUpdate();
		    if (rowCount>0) { //Wurde ein Datensatz gefunden?
		    	return OPERATION_RESULT.SUCCESS; 	
		    } else {
		    	return OPERATION_RESULT.KUNDE_NOT_FOUND;
		    }
		} catch (SQLException e) {
			if (e.getErrorCode()==1452) {
				return OPERATION_RESULT.KUNDE_NOT_FOUND;
			}
			System.out.println("Datenbankfehler bei updateAblesunge - "+e.getErrorCode()+": "+e.getMessage());
			return OPERATION_RESULT.INTERNAL_ERROR;
		}
	}

	@Override
	public Ablesung deleteAblesung(UUID id) {
		try {
			final PreparedStatement st=con.prepareStatement("DELETE FROM ablesungen where id=? returning id,zaehlernummer,datum,kommentar,neuEingebaut,zaehlerstand,kid;");
			st.setString(1, id.toString());
			final ResultSet rs=st.executeQuery();
		    if (rs.next()) { //Wurde ein Datensatz gefunden?
		    	return ablesungFromResult(rs);
		    } else {
		    	return null;
		    }
		} catch (SQLException e) {			
			System.out.println("Datenbankfehler bei deleteAblesung - "+e.getMessage());
			return null;
		}
	}

	@Override
	public ArrayList<Ablesung> getAblesungList(UUID kundenId, LocalDate sDate, LocalDate eDate) {
		//return getAblesungListe();
		ArrayList<Ablesung> result=new ArrayList<Ablesung>();
		try {
			String query="Select id,zaehlernummer,datum,kommentar,neuEingebaut,zaehlerstand,kID from ablesungen"
					+ " WHERE (ISNULL(?) OR kID=?)"
					+ " AND (ISNULL(?) OR datum > ?)"
					+ " AND (ISNULL(?) OR datum < ?);";
			
			final PreparedStatement st=con.prepareStatement(query);
			//st.setString(, kundenId.toString());
			String kundenIdString=null;
			if (kundenId!=null) {
				kundenIdString=kundenId.toString();
			}
			st.setString(1, kundenIdString);
			st.setString(2, kundenIdString);
			
			String sDateString=null;
			if(sDate!=null) {
				sDateString=sDate.toString();
			}
			st.setString(3, sDateString);
			st.setString(4, sDateString);
			
			String eDateString=null;
			if(eDate!=null) {
				eDateString=eDate.toString();
			}
			st.setString(5, eDateString);
			st.setString(6, eDateString);
			
			
			final ResultSet rs=st.executeQuery();
		    while(rs.next()) {
		    	result.add(ablesungFromResult(rs));
		    }
		} catch (SQLException e) {
			System.out.println("Datenbankfehler bei getAblesungList - "+e.getMessage());
			return result;
		}
		return result;
	}

	
	protected static AbstractDatabase loadJSON(String file) {
		SQLDatabase db=SQLDatabase.startDatabase();
		AbstractDatabase jdb=JsonDatabase.loadJSON(file);
		
		
		for (Kunde k:jdb.getKundenListe()) {
			db.addKunde(k);
		}
		
		for (Ablesung abl:jdb.getAblesungListe()) {
			db.addAblesung(abl);
		}
		
		return db;
	}
		
	@Override
	protected void saveJSON(String file) {
		saveJSON(file, new JsonDatabase(getKundenListe(), getAblesungListe()));
	}
	
	public static void main(String[] args) {
		SQLDatabase.startDatabase().createDatabase();
		Server.startServer("http://localhost:8081/rest", false,true,false);
		DataCreator.main(null);
		Server.stopServer(false);
	}
}
