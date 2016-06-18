package theaterdata;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
/**
 * Klasse die generieke databewerkingen (inclusief de daarvoor benodigde exceptieafhandeling) voor theater's data laag: theaterdata bevat. Deze klasse is bedoeld 
 * als super klasse voor alle klassen die CRUD acties en DBMS acties op de theater database uitvoeren.
 * @author Erwin Engel
 */
public abstract class TheaterDatabeheer {
	/**
	 * Zet datum in Gregorian Calendar formaat om naar datum in sql formaat
	 * @param gcDatum:  formaat is Gregorian Calendar
	 * @return datum in sql formaat
	 */
	public static Date naarSQLDatum (GregorianCalendar gcDatum ){
		return (new Date(gcDatum.getTimeInMillis()));
	}
	/**
	 * Zet datum in sql formaat om naar datum in Gregorian Calendar formaat
	 * @param sqlDatum:  formaat is Gregorian Calendar
	 * @return datum in Gregorian Calendar formaat
	 */
	public static GregorianCalendar naarGCDatum (Date sqlDatum ){
		GregorianCalendar gcDatum = new GregorianCalendar();
		gcDatum.setTimeInMillis(sqlDatum.getTime());
		return (gcDatum);
	}
	/**
	 * Zet Gregorian Calendar datum om naar leesbaar string formaat
	 * @param datum
	 * @return
	 */
	public static String geefJmdDatum(GregorianCalendar datum){
		SimpleDateFormat datumFormaat = new SimpleDateFormat("dd-MM-yyyy");
		String jmdDatum = datumFormaat.format(datum.getTime());
		return (jmdDatum.toString());
	}
	/**
	 * Maakt na sluiten database een theater exceptie voor een gegeven foutmelding. Melding wordt ook op console geprint.
	 * @param foutmelding
	 * @throws TheaterException
	 */
	public static void throwExceptie(String foutmelding) throws TheaterException{
		Connectiebeheer.closeDB();
		TheaterException ex = new TheaterException(foutmelding);
		System.out.println(ex.getMessage());
		throw ex;
	}
}
