package theaterdata;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import theater.Klant;
import theater.Voorstelling;

/**
 * Klasse die de voorstellingen van het theater beheert. Op elke datum is er maar één
 * voorstelling. De klasse verzorgt alle CRUD acties op voorstellingen 
 * (en de gerelateerde entiteiten die nodig zijn voor het beheren voorstellingen) 
 * in de theater database.
 * @author Erwin Engel
 */
public class Voorstellingbeheer extends TheaterDatabeheer {
	private static PreparedStatement psSelectVoorstellingen = null;
	private static PreparedStatement psSelectVoorstelling = null;
	private static PreparedStatement psSelectBezettingen = null;
	private static PreparedStatement psInsertBezetting = null;
	private static PreparedStatement psSelectKlant = null;
	
	public Voorstellingbeheer(){
		super();
	}
	/**
	 * alle benodigde sql statements klaarzetten
	 * @throws TheaterException prepared statement fout
	 */
	public static void init() throws TheaterException {
		try {
			psSelectVoorstellingen = Connectiebeheer.getCon().prepareStatement(DBConst.SELVOORSTELLINGEN);
			psSelectVoorstelling = Connectiebeheer.getCon().prepareStatement(DBConst.SELVOORSTELLING);
			psSelectBezettingen = Connectiebeheer.getCon().prepareStatement(DBConst.SELBEZETTINGEN);
			psInsertBezetting = Connectiebeheer.getCon().prepareStatement(DBConst.INSBEZETTING);
			psSelectKlant = Connectiebeheer.getCon().prepareStatement(DBConst.SELKLANTNR);
		}
		catch (SQLException e) {
			throwExceptie(DBConst.DBPREPSTATERROR);
		}
	}
	/**
	 * Levert alle datums op van aanwezige voorstellingen
	 * @return lijst met data van voorstellingen
	 * @throws TheaterException met selectiefout
	 */
	public static ArrayList<GregorianCalendar> geefVoorstellingsData() throws TheaterException{
		ResultSet resultSetVoorstellingen = null;
		GregorianCalendar nu = new GregorianCalendar();
		ArrayList<GregorianCalendar> data = new ArrayList<GregorianCalendar>();
		try {
			resultSetVoorstellingen = psSelectVoorstellingen.executeQuery();
			while (resultSetVoorstellingen.next()){
				Date sqlDatum = resultSetVoorstellingen.getDate(1);
				GregorianCalendar datum = new GregorianCalendar();
				datum.setTimeInMillis(sqlDatum.getTime());
				if (datum.after(nu)) {
					data.add(datum);
				}
			}
		}
		catch (SQLException e) {
			throwExceptie(DBConst.DBSELECTERROR);
		}
		return data;
	}
	/**
	 * Zoekt een voorstelling op de gegeven datum en geeft deze terug. 
	 * @param datum van de voorstelling (in GregorianCalendar formaat)
	 * @return een voorstelling op de gegeven datum of null wanneer die
	 *         voorstelling er niet is.
	 * @throws TheaterException  database actie fout (generieke melding)
	 */
	public static Voorstelling geefVoorstelling(GregorianCalendar gcDatum) throws TheaterException {
		Voorstelling voorstelling = null;
		ResultSet rsBezetting = null;
		ResultSet rsKlant = null;
		ResultSet rsVoorstelling = null;
		try {
			rsVoorstelling = selectVoorstelling(gcDatum);
			rsVoorstelling.first();
			voorstelling = new Voorstelling(rsVoorstelling.getString(2),naarGCDatum(rsVoorstelling.getDate(1)));
			rsBezetting = selectBezettingen(gcDatum);
			while(rsBezetting.next()){
				voorstelling.reserveer(rsBezetting.getInt(3), rsBezetting.getInt(4));
				rsKlant = selectKlant(rsBezetting.getInt(5)); 
				rsKlant.first();
				Klant k = new Klant(rsKlant.getInt(1), rsKlant.getString(2), rsKlant.getString(3));
				voorstelling.plaatsKlant(rsBezetting.getInt(3), rsBezetting.getInt(4), k );
			}
		}
		catch (SQLException e) {
			throwExceptie(DBConst.DBGENERICERROR);
		}
		return voorstelling;
	}
	/**
	 * Legt een nieuwe Bezetting vast en geeft 'true' terug indien gelukt. 
	 * @param gcDatum (formaat is Gregorian Calendar)
	 * @param rijnummer van bezetting
	 * @param stoelnummer van bezetting
	 * @param klantnummer van de klant die bij bezetting hoort
	 * @throws TheaterException met create fout
	 */
	public static boolean bezettingVastleggen(GregorianCalendar gcDatum, int rijnummer, int stoelnummer, int klantnummer ) throws TheaterException{
		try {
			psInsertBezetting.setString(1, "" + naarSQLDatum(gcDatum).toString());
			// onderstaande regel kan gebruikt worden om foutmelding bij foutieve insert te testen
			// insertBezetting.setString(1, "" + "fsdf");
			psInsertBezetting.setString(2, "" + rijnummer);
			psInsertBezetting.setString(3, "" + stoelnummer);

			psInsertBezetting.setString(4, "" + klantnummer);
			psInsertBezetting.executeUpdate();
		}
		catch (SQLException e) {
			throwExceptie(DBConst.DBCREATEERROR);
		}
		return true;
	} 
	/**
	 * Selecteert een voorstelling voor een gegeven datum, geeft resultset terug.
	 * @param gcDatum van de voorstelling
	 * @return resultset met voorstelling
	 * @throws TheaterException met selectiefout
	 */
	private static ResultSet selectVoorstelling (GregorianCalendar gcDatum) throws TheaterException{
		ResultSet rs = null;
		try {
			psSelectVoorstelling.setString(1, naarSQLDatum(gcDatum).toString());
			rs = psSelectVoorstelling.executeQuery();
		}
		catch (SQLException e) {
			throwExceptie(DBConst.DBSELECTERROR);
		}
		return rs;
	}
	/**
	 * Selecteert bezettingen voor een gegeven datum, geeft resultset terug..
	 * @param gcDatum van de bezettingen
	 * @return resultset met bezetting
	 * @throws TheaterException met selectiefout
	 */
	private static ResultSet selectBezettingen (GregorianCalendar gcDatum) throws TheaterException{
		ResultSet rs = null;
		try {
			psSelectBezettingen.setString(1, naarSQLDatum(gcDatum).toString());
			rs = psSelectBezettingen.executeQuery();
		}
		catch (SQLException e) {
			throwExceptie(DBConst.DBSELECTERROR);
		}
		return rs;
	}
	/**
	 * Selecteert een klant voor een gegeven klantnummer, geeft resultset terug..
	 * @param klantNummer van de klant
	 * @return resultset met klant
	 * @throws TheaterException
	 */
	private static ResultSet selectKlant (int klantNummer) throws TheaterException{
		ResultSet rs = null;
		try {
			psSelectKlant.setString(1, "" + klantNummer);
			rs = psSelectKlant.executeQuery();
		}
		catch (SQLException e) {
			throwExceptie(DBConst.DBSELECTERROR);
		}
		return rs;
	}

	/**
	 * Deze main is bedoeld om test op VoorstelingBeheer uit te kunnen voeren
	 * @param args
	 * @throws TheaterException
	 */
	public static void main(String[] args) throws TheaterException {
		try {
			Connectiebeheer.openDB();
			Voorstellingbeheer.init();
			// test methode geefVoorstellingsData()
			ArrayList<GregorianCalendar> data = geefVoorstellingsData();
			for (GregorianCalendar datum: data){
				System.out.println("datum voorstelling is: " + geefDmjDatum(datum));
			}
			// test methode geefVoorstelling(): goed situatie
			Voorstelling voorstelling = geefVoorstelling(data.get(0));
			System.out.println(voorstelling.toString());
			// test bezetting vastleggen --> LET OP, voor meerdere keren uitoeren van dit testgeval
			// moet toegevoegde rij eerst verwijderd worden uit DB!
/*			if (bezettingVastleggen(data.get(1),3,6,5)){
				System.out.println("bezetting met succes vastgelegd");
			} else {
				System.out.println("vastleggen bezetting mislukt");
			}
*/
			Connectiebeheer.closeDB(); 
		}
		catch (TheaterException e) {
			throwExceptie(DBConst.DBGENERICERROR);
			e.printStackTrace();
		}
	} 
}
