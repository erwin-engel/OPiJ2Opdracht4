package theaterdata;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import theater.Klant;
import theater.Voorstelling;

/**
 * Klasse die de voorstellingen van het theater beheert. Op elke datum is er maar één
 * voorstelling. De klasse verzorgt alle CRUD acties op voorstellingen (en de gerelateerde entiteiten
 * van voor het beheren voorstellingen) in de theater database.
 * @author Erwin Engel
 */
public class Voorstellingbeheer extends TheaterDatabeheer {
	private static PreparedStatement selectVoorstellingen = null;
	private static PreparedStatement selectVoorstelling = null;
	private static PreparedStatement selectBezettingen = null;
	private static PreparedStatement insertBezetting = null;
	private static PreparedStatement selectKlant = null;
	private static ResultSet resultSetVoorstellingen = null;
	private static ResultSet resultSetVoorstelling = null;
	private static ResultSet resultSetBezettingen = null;
	private static ResultSet resultSetKlant = null;
	private static Voorstelling voorstelling = null;
	private static Bezetting bezetting = null;
	private static Klant klant = null;

	public Voorstellingbeheer(){
		super();
	}
	/**
	 * alle benodigde QUERIES klaarzetten
	 * @throws TheaterException
	 */
	public static void init() throws TheaterException {
		try {
			selectVoorstellingen = Connectiebeheer.getCon().prepareStatement("SELECT * FROM voorstelling");
			selectVoorstelling = Connectiebeheer.getCon().prepareStatement("SELECT * FROM voorstelling WHERE datum = ?");
			selectBezettingen = Connectiebeheer.getCon().prepareStatement("SELECT * FROM bezetting WHERE voorstelling = ?");
			insertBezetting = Connectiebeheer.getCon().prepareStatement("INSERT INTO `theater`.`bezetting` "
					+ "(`voorstelling`, `rijnummer`, `stoelnummer`, `klant`) "
					+ "VALUES (?, ?, ?, ?)");
			selectKlant = Connectiebeheer.getCon().prepareStatement("SELECT * FROM klant WHERE klantnummer = ?");
		}
		catch (SQLException e) {
			throwExceptie("fout bij maken perpared statements tijdens initaliseren voorstellingbeheer");
		}
	}
	/**
	 * Levert alle datums op van aanwezige voorstellingen
	 * @return lijst met data van voorstellingen
	 * @throws TheaterException
	 */
	public static ArrayList<GregorianCalendar> geefVoorstellingsData() throws TheaterException{
		GregorianCalendar nu = new GregorianCalendar();
		ArrayList<GregorianCalendar> data = new ArrayList<GregorianCalendar>();
		try {
			resultSetVoorstellingen = selectVoorstellingen.executeQuery();
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
			throwExceptie("Het is niet gelukt om de datums van de voorstellingen op te halen");
		}
		return data;
	}
	/**
	 * Zoekt een voorstelling op de gegeven datum. 
	 *
	 * @param datum 
	 * @return een voorstelling op de gegeven datum of null wanneer die
	 *         voorstelling er niet is.
	 * @throws TheaterException
	 */
	public static Voorstelling geefVoorstelling(GregorianCalendar datum) throws TheaterException {
		voorstelling = null;
		try {
			selectVoorstelling(datum);
			maakVoorstelling();
			selectBezettingen(datum);
			while(resultSetBezettingen.next()){
				maakBezetting();
				voorstelling.reserveer(bezetting.getRijnummer(), bezetting.getStoelnummer());
				selectKlant(bezetting.getKlantnummer());
				maakKlant();
				voorstelling.plaatsKlant(bezetting.getRijnummer(), bezetting.getStoelnummer(), klant );
			}
		}
		catch (SQLException e) {
			throwExceptie("sql fout bij maken van de voorstelling");
		}
		return voorstelling;
	}
	/**
	 * Legt een nieuwe Bezetting vast. 
	 * @param gcDatum (formaat is Gregorian Calendar)
	 * @param rijnummer
	 * @param stoelnummer
	 * @param klantnummer
	 * @throws TheaterException
	 */
	public static void bezettingVastleggen(GregorianCalendar gcDatum, int rijnummer, int stoelnummer, int klantnummer ) throws TheaterException{
		bezetting = null;
		try {
			insertBezetting.setString(1, "" + naarSQLDatum(gcDatum).toString());
			// onderstaande regel kan gebruikt worden om foutmelding bij foutieve insert te testen
			// insertBezetting.setString(1, "" + "fsdf");
			insertBezetting.setString(2, "" + rijnummer);
			insertBezetting.setString(3, "" + stoelnummer);

			insertBezetting.setString(4, "" + klantnummer);
			insertBezetting.executeUpdate();

			bezetting = new Bezetting(gcDatum, rijnummer, stoelnummer, klantnummer);
		}
		catch (SQLException e) {
			throwExceptie("het plaatsen van de klant met datum: " + geefJmdDatum(gcDatum) + " is mislukt ");
		}
	}
	/**
	 * Selecteert een voorstelling voor een gegeven datum.
	 * @param gcDatum (formaat is Gregorian Calendar)
	 * @throws TheaterException
	 */
	private static void selectVoorstelling (GregorianCalendar gcDatum) throws TheaterException{
		try {
			selectVoorstelling.setString(1, naarSQLDatum(gcDatum).toString());
			resultSetVoorstelling = selectVoorstelling.executeQuery();
		}
		catch (SQLException e) {
			throwExceptie("het selecteren van voorstelling met datum: " + geefJmdDatum(gcDatum) + " is mislukt ");
		}
	}
	/**
	 * Selecteert bezettingen voor een gegeven datum.
	 * @param gcDatum (formaat is Gregorian Calendar)
	 * @throws TheaterException
	 */
	private static void selectBezettingen (GregorianCalendar gcDatum) throws TheaterException{
		try {
			selectBezettingen.setString(1, naarSQLDatum(gcDatum).toString());
			resultSetBezettingen = selectBezettingen.executeQuery();
		}
		catch (SQLException e) {
			throwExceptie("het selecteren van bezettingen met datum: " + geefJmdDatum(gcDatum) + " is mislukt ");
		}
	}
	/**
	 * Selecteert een klant voor een gegeven klantnummer.
	 * @param klantNummer
	 * @throws TheaterException
	 */
	private static void selectKlant (int klantNummer) throws TheaterException{
		try {
			selectKlant.setString(1, "" + klantNummer);
			resultSetKlant = selectKlant.executeQuery();
		}
		catch (SQLException e) {
			throwExceptie("het selecteren van klant met klantnummer: " + klantNummer + " is mislukt.");
		}
	}
	/**
	 * Maakt van een geselecteerde voorstelling een instantie van Voorstelling
	 * @throws TheaterException
	 */
	private static void maakVoorstelling() throws TheaterException{
		try {
			resultSetVoorstelling.first();
			Date sqlDatum = resultSetVoorstelling.getDate(1);
			String voorstellingnaam = resultSetVoorstelling.getString(2);
			voorstelling = new Voorstelling(voorstellingnaam, naarGCDatum(sqlDatum));
		}
		catch (SQLException e) {
			throwExceptie("het opvragen van de voorstellings gegevens is mislukt");
		}
	}
	/**
	 * Maakt van een geselecteerde bezetting een instantie van Bezetting.
	 * @throws TheaterException
	 */
	private static void maakBezetting()throws TheaterException{
		bezetting = null;
		try {
			GregorianCalendar gcDatum = naarGCDatum(resultSetBezettingen.getDate(2));
			int rijnummer = resultSetBezettingen.getInt(3);
			int stoelnummer = resultSetBezettingen.getInt(4);
			int klantnummer = resultSetBezettingen.getInt(5);
			bezetting = new Bezetting(gcDatum, rijnummer, stoelnummer, klantnummer);
		}
		catch (SQLException e) {
			throwExceptie("het opvragen van de bezettingen mislukt");
		}
	}
	/**
	 * Maakt van een geselecteerde klant een instantie van Klant.
	 * @throws TheaterException
	 */
	private static void maakKlant()throws TheaterException{
		klant = null;
		try {
			resultSetKlant.first();
			int klantnummer = resultSetKlant.getInt(1);
			String klantnaam = resultSetKlant.getString(2);
			String klantTelno = resultSetKlant.getString(3);
			klant = new Klant(klantnummer, klantnaam, klantTelno);
		}
		catch (SQLException e) {
			throwExceptie("het maken van een nieuwe klant is mislukt");
		}
	}

	// use to test public methods
	public static void main(String[] args) throws TheaterException {
		try {
			Connectiebeheer.openDB();
			Voorstellingbeheer.init();
			// test methode geefVoorstellingsData()
			ArrayList<GregorianCalendar> data = geefVoorstellingsData();
			SimpleDateFormat datumFormaat = new SimpleDateFormat("dd-MM-yyyy");
			for (GregorianCalendar datum: data){
				String jmdDatum = datumFormaat.format(datum.getTime());
				System.out.println("datum voorstelling is: " + jmdDatum);
			}

			// test methode geefVoorstelling(): goed situatie
			Voorstelling voorstelling = geefVoorstelling(data.get(0));
			System.out.println(voorstelling.toString());

			// test bezetting vastleggen --> LET OP, voor meerdere keren uitoeren van dit testgeval
			// moet toegevoegde rij eerst verwijderd worden uit DB!
			/*	bezettingVastleggen(data.get(1),3,6,5);
				System.out.println(bezetting.toString());
				Connectiebeheer.closeDB(); */
		}
		catch (TheaterException e) {
			throwExceptie("testen van klasse Voorstellingbeer mislukt");
		}
	} 
}
