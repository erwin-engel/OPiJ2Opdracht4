package theaterdata;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import theater.Klant;
/**
 * Klasse die de klanten van het theater beheert. De klasse verzorgt alle CRUD acties op klanten 
 * in de theater database (voor zover nodig binnen het klantbeheer proces)
 * @author Erwin Engel
 */
public class Klantbeheer extends TheaterDatabeheer{  
	private static PreparedStatement psSelectKlant = null;
	private static PreparedStatement psSelectMaxKlantnummer = null;
	private static PreparedStatement psInsertKlant = null;
	/**
	 * alle benodigde sql statements klaarzetten
	 * @throws TheaterException met prepared statement fout
	 */
	public static void init() throws TheaterException {
		try {
			psSelectKlant = Connectiebeheer.getCon().prepareStatement(DBConst.SELKLANTNMTEL);
			psInsertKlant = Connectiebeheer.getCon().prepareStatement(DBConst.INSKLANT);
			psSelectMaxKlantnummer = Connectiebeheer.getCon().prepareStatement(DBConst.SELKLANTMAX);
		}
		catch (SQLException e) {
			throwExceptie(DBConst.DBPREPSTATERROR);
		}
	}
	/**
	 * Genereert het volgende beschikbare klantnummer.
	 * @return het eerst volgende klantnummer
	 * @throws TheaterException met selectiefout
	 */
	public static int getVolgendeKlantNummer() throws TheaterException {
		int volgendeKlantnummer = 0;
		ResultSet rsMaxKlantnummer = null;
		try {
			rsMaxKlantnummer = psSelectMaxKlantnummer.executeQuery();
			rsMaxKlantnummer.first();
			volgendeKlantnummer = rsMaxKlantnummer.getInt(1) + 1;
		}
		catch (SQLException e) {
			throwExceptie(DBConst.DBSELECTERROR);
		}
		return volgendeKlantnummer;
	}
	/**
	 * Geeft een klant met de gegeven naam en het gegeven telefoonnummer
	 * Als de klant al in de lijst zat, wordt die teruggegeven; anders
	 * wordt er eerst een nieuwe klant gemaakt voordat de klant wordt teruggegeven.
	 * @param naam  van de klant
	 * @param telefoon  van de klant
	 * @return  een klant 
	 * @throws TheaterException 
	 */
	public static Klant geefKlant(String naam, String telefoon) throws TheaterException {
		Klant klant = selectKlant(naam, telefoon);
		if (klant == null) {
			klant = new Klant (insertKlant(naam, telefoon),naam, telefoon);
		}
		return klant;
	}
	/**
	 * Selecteert klant uit de database. 
	 * @param naam van de klant
	 * @param telefoon van de klant
	 * @return klant
	 * @throws TheaterException met selectiefout
	 */
	private static Klant selectKlant (String naam, String telefoon) throws TheaterException{
		Klant klant = null;
		ResultSet rsKlant = null;
		try {
			psSelectKlant.setString(1, naam);
			psSelectKlant.setString(2, telefoon);
			rsKlant = psSelectKlant.executeQuery();
			if (rsKlant.isBeforeFirst()){
				rsKlant.first();
				int klantnummer = Integer.parseInt(rsKlant.getString(1));
				String klantnaam = rsKlant.getString(2);
				String klantTelno = rsKlant.getString(3);
				klant = new Klant(klantnummer, klantnaam, klantTelno);
			}
		}
		catch (SQLException e) {
			throwExceptie(DBConst.DBSELECTERROR);
		}
		return klant;
	}
	/**
	 * Voegt een nieuwe klant toe aan theater. Geeft het klantnummer klantnummer terug (
	 * @param naam van de klant
	 * @param telefoon telefoon van de klant
	 * @return klantnummer van de klant
	 * @throws TheaterException met update fout
	 */
	private static Integer insertKlant(String naam, String telefoon) throws TheaterException {
		Integer knr = null;
		try {
			knr = getVolgendeKlantNummer();
			psInsertKlant.setString(1, "" + knr);
			psInsertKlant.setString(2, naam);
			psInsertKlant.setString(3, telefoon);
			psInsertKlant.executeUpdate();
		}
		catch (SQLException e) {
			throwExceptie(DBConst.DBCREATEERROR);
		}
		return knr;
	} 
	
	//use to test query 
	public static void main(String[] args) throws TheaterException {
		try {
			//test voorbereiden
			Connectiebeheer.openDB();
			Klantbeheer.init();
			// test ophalen max klantnummer
			System.out.println("volgende klantnr = " + getVolgendeKlantNummer());
			// test selecteren klant
			Klant klant = selectKlant("Sint" , "030-7492885");
			if(klant != null){
				System.out.println(klant.toString());
			}
			// test toevoegen klant (2 keer om ophoging klantnummer te zien
			Integer knr = insertKlant("bok", "911");
			if (knr != null){
				System.out.println("nieuwe klant 'bok / 911' succesvol vastgelegd met klantnummer: " + knr);
			};
			knr = insertKlant("bok", "112");
			if (knr != null){
				System.out.println("nieuwe klant 'bok / 112' succesvol vastgelegd, met verhoogd klantnummer: " + knr);
			};
			Connectiebeheer.closeDB();
		}
		catch (TheaterException e) {
			e.printStackTrace();
			throwExceptie("testen klantbeheer mislukt");
		}
	}
}
