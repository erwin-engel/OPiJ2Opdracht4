package theaterdata;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import theater.Klant;
/**
* Klasse die de klanten van het theater beheert. De klasse verzorgt alle CRUD acties op klanten 
* in de theater database (voor zover nodig binnen het klantbeheer proces)
* @author Erwin Engel
*/
public class Klantbeheer extends TheaterDatabeheer{  
	private static int volgendeKlantnummer = 0;
	private static Klant klant = null;

	private static PreparedStatement selectKlant = null;
	private static PreparedStatement selectMaxKlantnummer = null;
	private static PreparedStatement insertKlant = null;
	private static ResultSet resultSetKlant = null;
	private static ResultSet resultMaxKlantnummer = null;
	/**
	 * klaarzetten queries
	 * @throws TheaterException 
	 */
	public static void init() throws TheaterException {
		try {
			selectKlant = Connectiebeheer.getCon().prepareStatement("SELECT * FROM klant WHERE naam = ? AND telefoon = ?");
			insertKlant = Connectiebeheer.getCon().prepareStatement("INSERT INTO `theater`.`klant` (`klantnummer`, `naam`, `telefoon`) "
					+ "VALUES (?, ?, ?);");
			selectMaxKlantnummer = Connectiebeheer.getCon().prepareStatement("SELECT max(Klantnummer) from klant");

		}
		catch (SQLException e) {
			throwExceptie("fout bij maken perpared statements tijdens klantbeheer");
		}
	}

	/**
	 * Genereert het volgende beschikbare klantnummer.
	 * @return nieuw klantnummer
	 * @throws TheaterException 
	 */
	public static int getVolgendKlantNummer() throws TheaterException {
		try {
			resultMaxKlantnummer = selectMaxKlantnummer.executeQuery();
			resultMaxKlantnummer.first();
			volgendeKlantnummer = resultMaxKlantnummer.getInt(1) + 1;
			return volgendeKlantnummer;
		}
		catch (SQLException e) {
			throwExceptie("bepalen hoogste klantnummer tijdens klantbeheer is mislukt ");
		}
		return 0;
	}

	/**
	 * Geeft een klant met de gegeven naam en het gegeven telefoonnummer
	 * Als de klant al in de lijst zat, wordt die teruggegeven; anders
	 * wordt er een nieuwe klant gemaakt.
	 * @param naam  van de klant
	 * @param telefoon  van de klant
	 * @return  een klant met zijn naam en telefoon.
	 * @throws TheaterException 
	 */
	public static Klant geefKlant(String naam, String telefoon) throws TheaterException {
		selectKlant(naam, telefoon);
		if (klant == null) {
			insertKlant(naam, telefoon);
		}
		return klant;
	}

	/**
	 * Selecteert klant uit de database. Na geslaagde selectie bevat Klantbeheer een instantie van 
	 * de geselecteerde klant
	 * @param naam naam van te zoeken klant
	 * @param telefoon telefoonnummer van te zoeken klant
	 * @return de klant of null wanneer klant niet is gevonden
	 */
	private static void selectKlant (String naam, String telefoon) throws TheaterException{
		klant = null;
		try {
			selectKlant.setString(1, naam);
			selectKlant.setString(2, telefoon);
			resultSetKlant = selectKlant.executeQuery();
			if (resultSetKlant.isBeforeFirst()){
				resultSetKlant.first();
				int klantnummer = resultSetKlant.getInt(1);
				String klantnaam = resultSetKlant.getString(2);
				String klantTelno = resultSetKlant.getString(3);
				klant = new Klant(klantnummer, klantnaam, klantTelno);
			} else {
				klant = null;
			}
		}
		catch (SQLException e) {
			throwExceptie("selecteren van klant met naam: " + naam +
					" en telno: " + telefoon + " is mislukt.");
		}
	}

	/**
	 * Voegt een nieuwe klant toe aan theater. Na geslaagde insert bevat Klantbeheer een instatie 
	 * van deze nieuwe klant
	 * @param naam  naam van de nieuwe klant
	 * @param telefoon telefoonnummer van de nieuwe klant
	 * @throws TheaterException 
	 */
	private static void insertKlant(String naam, String telefoon) throws TheaterException {
		klant = null;
		int knr;
		try {
			knr = getVolgendKlantNummer();
			insertKlant.setString(1, "" + knr);
			insertKlant.setString(2, naam);
			insertKlant.setString(3, telefoon);
			insertKlant.executeUpdate();
			klant = new Klant(knr, naam, telefoon);
		}
		catch (SQLException e) {
			throwExceptie("vastleggen klant met naam: " + naam +
					" en telno: " + telefoon + " is mislukt tijdens klantbeheer");
		}

	} 
	//use to test query 
	public static void main(String[] args) throws TheaterException {
		try {
			//test voorbereiden
			Connectiebeheer.openDB();
			Klantbeheer.init();
			// test ophalen max klantnummer
			System.out.println("volgende klantnr = " + getVolgendKlantNummer());
			// test selecteren klant
			selectKlant("Sint" , "030-7492885");
			System.out.println(klant.toString());
			// test toevoegen klant (2 keer om ophoging klantnummer te zien
			insertKlant("bok", "911");
			System.out.println(klant.toString());
			insertKlant("bok", "911");
			System.out.println(klant.toString());
			Connectiebeheer.closeDB();
		}
		catch (TheaterException e) {
			throwExceptie("testen klantbeheer mislukt");
		}
	}
}
