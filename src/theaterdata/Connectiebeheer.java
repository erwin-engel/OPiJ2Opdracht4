package theaterdata;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import theatergui.TheaterFrame;
/**
 * Beheert de connectie met de database.
 * Bevat methoden voor openen en sluiten van connectie met database,
 * en voor opvragen van de connectie.
 * @author Erwin Engel
 */
public class Connectiebeheer extends TheaterDatabeheer {
	private static Connection con = null;

	/**
	 * Maakt een connectie met de database en initialiseert Klantbeheer en VoostellingBeheer.
	 * @throws TheaterException
	 */
  public static void openDB() throws TheaterException {
  	// logger maken, driver laden, connectie maken
  	try {
  	DriverManager.setLogWriter(new PrintWriter(System.out));
  	Class.forName(DBConst.DRIVERNAAM);
		con = DriverManager.getConnection(DBConst.URL, DBConst.GEBRUIKERSNAAM,
				DBConst.WACHTWOORD);
		System.out.println("db open");
  	}
		catch (ClassNotFoundException e) {
			throw new TheaterException("Fout bij openen DB: " + DBConst.DRIVERNAAM + " , "+ e.toString());
			
		}
		catch (SQLException e) {
			throw new TheaterException("fout bij maken connectie" + DBConst.URL);
		}
//    Klantbeheer.init();
//    Voorstellingbeheer.init();
    System.out.println("initialisatie beheer gereed");
  }

  /**
   * Sluit de connectie met de database
   * @throws TheaterException
   */
  public static void closeDB() throws TheaterException {
  	if (con != null) {
  		try{
  			System.out.println("db closed");
  			con.close();
  		}
  		catch (SQLException e) {
  			throw new TheaterException("fout bij maken connectie" + DBConst.URL);  			
  		}
  	}
  }
  /**
   * geeft connectie
   * @return con: database connectie
   */
	public static Connection getCon() {
		return con;
	}

  /**
   * main methode voor testen Connectiebeheer
   * @param args
   * @throws TheaterException
   */
  public static void main(String[] args) throws TheaterException {
  	try {
			Connectiebeheer.openDB();
			Connectiebeheer.closeDB();
		}
		catch (TheaterException e) {
			throwExceptie("fout bij testen connectie");
		}
  }  
}
