package theaterdata;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Beheert de connectie met de database.
 * Bevat methoden voor openen en sluiten van connectie met database,
 * en voor opvragen van de connectie.
 * @author Erwin Engel
 */
public class Connectiebeheer extends TheaterDatabeheer {
	private static Connection con = null;

	/**
	 * Maakt een connectie met de database.
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
			throwExceptie(DBConst.DBOPENERROR + DBConst.DRIVERNAAM);
		}
		catch (SQLException e) {
			throwExceptie(DBConst.DBCONNECTERROR + DBConst.URL);
		}
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
  			throwExceptie(DBConst.DBCLOSEERROR + DBConst.URL);  			
  		}
  	}
  }
  /**
   * levert de connectie met de database 
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
			throwExceptie(DBConst.DBCONNECTERROR);
		}
  }  
}
